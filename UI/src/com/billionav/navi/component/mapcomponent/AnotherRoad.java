package com.billionav.navi.component.mapcomponent;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.billionav.jni.UILocationControlJNI;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.uitools.DialogTools;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;

public class AnotherRoad extends RelativeLayout{
	
	private ImageView anotherRoadIcon;
	public AnotherRoad(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(); 
	}

	private void initialize() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.guide_another_road, this);
	    
	    findViews();
	    adjustLayout();
	    setVisibility(View.GONE);
	}

	private void findViews() {
		anotherRoadIcon = (ImageView)findViewById(R.id.another_road);
		
		setListeners();
	}

	private void setListeners() {
		anotherRoadIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if(!UILocationControlJNI.getInstance().StartAnotherRoad()){
//					DialogTools.getDialogForSingleButton(getContext(), R.string.STR_MM_01_01_04_29, R.string.STR_MM_01_01_04_30, 0).show();
//				}else{
//					Log.d("test","startAnotherRoad clicked");
//				}
				
				UILocationControlJNI.getInstance().StartAnotherRoad();
				ban();
			}
		});
		
	}
	private void ban() {
		anotherRoadIcon.setEnabled(false);
		anotherRoadIcon.getBackground().setAlpha(100);
	}
	private void enable(){
		anotherRoadIcon.setEnabled(true);
		anotherRoadIcon.getBackground().setAlpha(255);
	}
	
	public void nodifyDemoState(boolean isDemo) {
		anotherRoadIcon.setEnabled(!isDemo);
	}
	
	public boolean notifyTrigger(NSTriggerInfo info){
		if(info.GetTriggerID() == NSTriggerID.UIC_MN_TRG_LOC_ANOTHER_ROAD){
			Log.d("test","getanotherroad");
			showAnotherRoadDialog(info);
			enable();
			return true;
		}
		return false;
	}
	private Dialog dialog;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0x112){
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
			}
		};
	};
	private void showAnotherRoadDialog(NSTriggerInfo info) {
		int dialogID = 0;
		switch ((int) info.GetlParam1()) {
		case UILocationControlJNI.LDM_ANOTHER_ROAD_FAIL_UNKNOWN: {
			dialogID = R.string.MSG_01_01_04_07;
			break;
		}
		case UILocationControlJNI.LDM_ANOTHER_ROAD_FAIL_TOLL: {
			dialogID = R.string.MSG_01_01_04_04;
			break;
		}
		case UILocationControlJNI.LDM_ANOTHER_ROAD_FAIL_NORMAL: {
			dialogID = R.string.MSG_01_01_04_02;
			break;
		}
		case UILocationControlJNI.LDM_ANOTHER_ROAD_SUCCESS_TOLL: {
			dialogID = R.string.MSG_01_01_04_03;
			break;
		}
		case UILocationControlJNI.LDM_ANOTHER_ROAD_SUCCESS_NORMAL: {
			dialogID = R.string.MSG_01_01_04_05;
			break;
		}
		default:
			dialogID = R.string.MSG_01_01_04_07;
			break;
		}
		dialog = DialogTools.getDialogForSingleButton(getContext(), R.string.STR_MM_01_01_04_32, dialogID, 0);
		dialog.show();
		handler.sendEmptyMessageDelayed(0x112, 5000);
	}
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		adjustLayout();
		super.onConfigurationChanged(newConfig);
	}
	private void adjustLayout(){
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)anotherRoadIcon.getLayoutParams();
		int portraitBottomValue = SystemTools.EDITION_CRADLE.equals(SystemTools.getApkEdition()) ?106:56;
		int landscapeBottomValue = SystemTools.EDITION_CRADLE.equals(SystemTools.getApkEdition()) ?40:0;
		if(ScreenMeasure.isPortrait()){
			lp.setMargins(0, 0, 0, DensityUtil.dp2px(getContext(), portraitBottomValue));
		}else{
			lp.setMargins(0, 0, 0, DensityUtil.dp2px(getContext(), landscapeBottomValue));
		}
	}

	public void onResume() {
		adjustLayout();
		
	}
}
