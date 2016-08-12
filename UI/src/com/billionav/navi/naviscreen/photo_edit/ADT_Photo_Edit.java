package com.billionav.navi.naviscreen.photo_edit;


import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.map.ForwardARscreenControl;
import com.billionav.navi.naviscreen.srch.ADT_Srch_InfoEdit;
import com.billionav.ui.R;

public class ADT_Photo_Edit extends ActivityBase{
	private ImageButton btn_Camera;
	private ImageButton btn_browse;
//	private Button btn_submit;
//	private Button btn_delete;
	private Bitmap bm;
	private CameraLayout selectView;
//	private RelativeLayout imageLayout;

	

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_photo_edit_activity);
		findViews();
		initialize();
		setListener();
	}
	private void setListener() {
		btn_Camera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//call camera
				ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_CAMEEA_MODE);
				ForwardWinChange(ADT_Photo_Edit_Camera.class);
			}
		});
		btn_browse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ForwardWinChange(ADT_Photo_Album.class);
			}
		});
		addActionItem3(R.string.STR_MM_02_02_06_08, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap mBitmap = selectView.getCutBitmap();
				byte[] transbp =BitmapTransportHelper.getInstance().compressBitmap(mBitmap);
				BundleNavi.getInstance().putInt("command", BitmapTransportHelper.COMMAND_GET_PICTURE);
				BundleNavi.getInstance().put("cutPic", transbp);
				BackSearchWinChange(ADT_Srch_InfoEdit.class);
				
			}
		});
		addActionItem3(R.string.STR_MM_02_02_06_07, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BundleNavi.getInstance().putInt("command", BitmapTransportHelper.COMMAND_DEL_PICTURE);
				BackSearchWinChange(ADT_Srch_InfoEdit.class);
			}
		});

	}

	private void initialize() {
		setDefaultBackground();
		//string save position edit in Chinese
		setTitleText(R.string.STR_MM_02_02_06_06);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(null != selectView){
			selectView.resetVars();
		}
		if(null != bm){
			selectView.setPic(bm);
		}
		super.onConfigurationChanged(newConfig);
	}
	private void findViews() {
//		imageLayout = (RelativeLayout)findViewById(R.id.adt_favourite_cam_view_layout);
		btn_Camera = (ImageButton)findViewById(R.id.adt_favourite_pic_came);
		btn_browse = (ImageButton)findViewById(R.id.adt_favourite_pic_brow);
//		btn_submit = (Button)findViewById(R.id.adt_favourite_pic_submit);
//		btn_delete = (Button)findViewById(R.id.adt_favourite_pic_delete);
		selectView = (CameraLayout)findViewById(R.id.adt_favourite_cam_view_selectView);
	}
	@Override
	public void OnShow() {
		super.OnShow();
	}

	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
		}
		return super.OnKeyDown(keyCode, event);
		
	}
	@Override
	protected void OnResume() {
		super.OnResume();
//		MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		byte buff[]=(byte[])BundleNavi.getInstance().get("image"); 
		if (null == buff){
			String path = BundleNavi.getInstance().getString("returnImage");
			if(path != null){
				bm = BitmapTransportHelper.getInstance().getSuitableSizeBitmap(path);
			}
		}else{
			bm = BitmapFactory.decodeByteArray(buff, 0, buff.length);
		}
		if(null != bm){
			selectView.setPic(bm);
		}
	}

	@Override
	protected void OnPause() {
//		MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		super.OnPause();
	}
	@Override
	protected void OnDestroy() {
		selectView.recycleBitmapBuff();
		// TODO Auto-generated method stub
		super.OnDestroy();
	}


	

}
