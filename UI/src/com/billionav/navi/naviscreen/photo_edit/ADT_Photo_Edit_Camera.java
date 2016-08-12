package com.billionav.navi.naviscreen.photo_edit;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.navi.camera.CameraView;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.map.ForwardARscreenControl;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;

public class ADT_Photo_Edit_Camera extends ActivityBase{
	private ImageButton editPhotograph;
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_photo_edit_camera_view);
		setNoTitle();
		
		if (SystemTools.isCH()) {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ForwardARscreenControl.getinstance().DRIRFunChange();
				}
			},1000);
			
		}
		
		findViews();	
		initialize();	
		setListener();
	}

	private void setListener() {
		editPhotograph.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (SystemTools.isCH()) {
					DRIRAppMain.TakeOneShootPic();
				}
				
			}
		});
		
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
	}

	private void findViews() {
		editPhotograph = (ImageButton)findViewById(R.id.adt_photo_edit_Photograph);
		
	}

	public boolean OnTrigger(NSTriggerInfo triggerInfo){
		int iTriggerID = triggerInfo.GetTriggerID();
		switch (iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_UC_PM_UPLOADDATA:
			return true;
		case NSTriggerID.UIC_MN_TRG_DRIR_TAKEPIC_END:
			String pic_path = triggerInfo.GetString1();
			BundleNavi.getInstance().putString("returnImage", pic_path);
			BackSearchWinChange(ADT_Photo_Edit.class);
			return true;
		default:
			return super.OnTrigger(triggerInfo);
		}		
		
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
			MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_CAMEEA_MODE);
					}
				},1000);
				
			CameraView.Instance().Maximize();
	}
	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
		if (SystemTools.isCH()) {
			DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_CAMEEA_MODE, DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE);
		}
//		CameraView.Instance().Minimize();
// 		MapView.getInstance().setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void OnStop() {
		// TODO Auto-generated method stub
		super.OnStop();
		if (SystemTools.isCH()) {
			MenuControlIF.Instance().SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			CameraView.Instance().Minimize();
		}
	}

	@Override
	protected void OnDestroy() {
		super.OnDestroy();
	}

	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
//		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
//			BackWinChange();
//		}
		return super.OnKeyDown(keyCode, event);
	}
	
}
