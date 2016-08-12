package com.billionav.navi.component.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.billionav.navi.service.NotificateModel;
import com.billionav.navi.service.NotifyActionIDEnum;
import com.billionav.ui.R;

public class CustomSelectableDialog extends Activity{
	private Button okbtn = null;
	private Button cancelbtn = null;
	private TextView textdata = null;
	
	private NotificateModel model = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_selectable_dialog);
		okbtn = (Button)findViewById(R.id.ok);
		cancelbtn = (Button)findViewById(R.id.cancel);
		textdata = (TextView)findViewById(R.id.textdata);
		
		Bundle extras = getIntent().getBundleExtra(NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG);
		if(null == extras) {
			Log.e("test", "CustomSelectableDialog get bundle = null");
		} else {
			model = (NotificateModel)extras.get(NotifyActionIDEnum.NOTIFICATION_BROADCAST_MODEL_TAG);
		}
		String message = null;
		if(null == model) {
			Log.e("test", "CustomSelectableDialog get model = null");
		} else {
			message = String.format(getString(R.string.STR_COM_046), model.pointName);
		}
		
		textdata.setText(message);
		setOnclickListener();
	}
	
	
	private void setOnclickListener(){
		okbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				Bundle bundle = new Bundle();
				i.setAction("com.suntec.iAuto.btspp.navibt2app");
				i.setType("notify/requestPath");
				bundle.putSerializable(NotifyActionIDEnum.NOTIFICATION_BROADCAST_MODEL_TAG, model);
				i.putExtra(NotifyActionIDEnum.NOTIFICATION_BROADCAST_DATA_TAG, bundle);
				startActivity(i);
				finish();
			}
		});
		
		cancelbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				serviceHelper.cancelNotificationToService(model.tag);
				finish();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			serviceHelper.cancelNotificationToService(model.tag);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
