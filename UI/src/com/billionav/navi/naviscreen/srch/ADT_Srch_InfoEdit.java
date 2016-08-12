package com.billionav.navi.naviscreen.srch;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.photo_edit.ADT_Photo_Edit;
import com.billionav.navi.naviscreen.photo_edit.BitmapTransportHelper;
import com.billionav.navi.system.PLog;
import com.billionav.navi.uitools.PointTools;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;

public class ADT_Srch_InfoEdit extends ActivityBase {
	private EditText name;
	private EditText address;
	private EditText telNo;
	private ImageView picture;
	private Bitmap bitmap;
	private LinearLayout changeLayout = null;
	private boolean issaveimage = false;
	private int recordid = 0;
	
	private UIPointData pointdata = null;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_srch_info_edit);
		
		findViews();
		
		initialize();
		
		setListener();
	}

	private void findViews() {
		name = (EditText) findViewById(R.id.et_srch_info_edit_name);
		telNo = (EditText) findViewById(R.id.et_srch_info_edit_telno);
		address = (EditText) findViewById(R.id.et_srch_info_edit_address);
		picture = (ImageView) findViewById(R.id.pictrue_imageView);
		changeLayout = (LinearLayout)findViewById(R.id.changelinearlayout);
	}
	
	private void initialize() {
		setDefaultBackground();
		setTitle(R.string.STR_MM_02_02_05_06);
		 ((TextView) findViewById(R.id.textView4)).getPaint().setFakeBoldText(true);
		 ((TextView) findViewById(R.id.textView1)).getPaint().setFakeBoldText(true);
		 ((TextView) findViewById(R.id.textView2)).getPaint().setFakeBoldText(true);
		 ((TextView) findViewById(R.id.textView3)).getPaint().setFakeBoldText(true);
		addActionItem3(R.string.STR_MM_02_02_06_08, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String names = name.getText().toString().trim();
				String telNos = telNo.getText().toString().trim();
				String addstr = address.getText().toString().trim();
				if(TextUtils.isEmpty(names)){
					CustomToast.showToast(ADT_Srch_InfoEdit.this, R.string.MSG_02_02_05_01, 1000);
					return;
				}
				pointdata = pointdata.clone();
				pointdata.setAddress(addstr);
				pointdata.setName(names);
				pointdata.setTelNo(telNos);
				
				if(issaveimage){
					PointTools.getInstance().createFaveratePhoto(pointdata.getUuid(), bitmap);
				}
				
				UIPointControlJNI.Instance().reqUpdatePoint(pointdata.getID(), pointdata);
				showProgress();
				
//				jniPointControl_new.Instance().GetPointDataByRecordID(recordid).setName(names);
//				jniPointControl_new.Instance().GetPointDataByRecordID(recordid).setTel(telNos);
//				jniPointControl_new.Instance().GetPointDataByRecordID(recordid).setAddress(addstr);

//				jniPointControl_new.Instance().GetPointDataByRecordID(recordid).Save();
				
			}
		});
		recordid = getBundleNavi().getInt("record");
		pointdata = (UIPointData) getBundleNavi().get("pointdata");
		
		name.setText(pointdata.getName());
		telNo.setText(pointdata.getTelNo());
		address.setText(pointdata.getAddress());
		Uri uri = PointTools.getInstance().returnFaveratePhoto(pointdata.getUuid());
		if(null == uri){
		picture.setImageResource(R.drawable.navicloud_and_591a);
		}else{
			picture.setImageURI(uri);
		}
//		Bitmap bp = PointTools.returnFaveratePhoto(recordid);
//		picture.setImageBitmap(bp);
//		
//		if(bp != null){
//			picture.setImageBitmap(bp);
//			bitmap = bp;
//		}
	}
	
	private void setListener() {
		picture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap bp=((BitmapDrawable)picture.getDrawable()).getBitmap();
				BitmapTransportHelper.getInstance().putBitmapIntoBundle(bp);
				ForwardWinChange(ADT_Photo_Edit.class);
				
			}
		});
	}
	@Override
	protected void OnResume() {
		super.OnResume();
		resetlayout();
		int command = BitmapTransportHelper.COMMAND_NONE;
		command = BitmapTransportHelper.getInstance().getCommand();
		if(command == BitmapTransportHelper.COMMAND_DEL_PICTURE){
			//do del work
			issaveimage = false;
			picture.setImageResource(R.drawable.navicloud_and_591a);
			//delete photo
			PointTools.getInstance().deleteFaveratePhoto(pointdata.getUuid());
		}else if(command == BitmapTransportHelper.COMMAND_GET_PICTURE){
			issaveimage = true;
			bitmap = BitmapTransportHelper.getInstance().getBitmapFromBundle();
			picture.setImageBitmap(bitmap);
		}
	}

	
	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		PLog.i("icon", "cTriggerInfo id::"+cTriggerInfo.GetTriggerID());
		int Triggerid  = cTriggerInfo.GetTriggerID();
		if (Triggerid == NSTriggerID.POINT_RESPONSE_UPDATE_POINT){
			getBundleNavi().putBoolean("editback", true);
			getBundleNavi().put("pointData", pointdata);
			BackWinChange();
		}
		return super.OnTrigger(cTriggerInfo);
	}

	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		resetlayout();
	}

	private void resetlayout() {
		if(ScreenMeasure.isPortrait()){
			changeLayout.setOrientation(LinearLayout.VERTICAL);
		}
		else{
			changeLayout.setOrientation(LinearLayout.HORIZONTAL);
		}
	}
	@Override
	protected void OnDestroy() {
//		if(bitmap != null && !bitmap.isRecycled()){
//			bitmap.recycle();
//			bitmap = null;
//		}
		super.OnDestroy();
	}
}
