package com.billionav.navi.naviscreen.dest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.download.ADT_Download_Map;
import com.billionav.navi.naviscreen.map.ADT_AR_Main;
import com.billionav.navi.naviscreen.map.ADT_AR_Main_Tip;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.map.ForwardARscreenControl;
import com.billionav.navi.naviscreen.misc.ADT_Voice_Recognition;
import com.billionav.navi.naviscreen.report.ADT_report_main;
import com.billionav.navi.naviscreen.setting.ADT_Settings_Main;
import com.billionav.navi.uitools.DialogTools;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.ui.R;
import com.billionav.voicerecogJP.VRJPManager;
//import com.billionav.voicerecog.VoiceRecognizer;

public class ADT_Top_Menu extends ADT_Top_Menu_Display_Layer_Activity implements OnClickListener{
	
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		
		setListeners();
		
		int[] lonlat = UIMapControlJNI.GetCenterLonLat();
//		SnsControl.Instance().RequestSnsDataRestartTimer(lonlat[0],lonlat[1]);
		
		
	}

	private void setListeners() {
		setting_view.setOnClickListener(this);
		report_view.setOnClickListener(this);
		vr_view.setOnClickListener(this);
		navi_view.setOnClickListener(this);
		ar_view.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if(v == setting_view) {
			ForwardWinChange(ADT_Settings_Main.class);
		} else if(v == report_view) {
			ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_CAMEEA_MODE);
			ForwardWinChange(ADT_report_main.class);	
		} else if(v == vr_view) {
			if(VRJPManager.isDocomoDemo){
				getBundleNavi().putBoolean("Navigation", true);
				ForwardKeepDepthWinChange(ADT_Main_Map_Navigation.class);
				VRJPManager.Instance().startService(NaviViewManager.GetViewManager());
			} else {
				ForwardWinChange(ADT_Voice_Recognition.class);
			}
		} else if(v == navi_view) {
			getBundleNavi().putBoolean("Navigation", true);
			ForwardKeepDepthWinChange(ADT_Main_Map_Navigation.class);
		} else if(v == ar_view) {
			if(SharedPreferenceData.IS_NEED_AR_TIP.getBoolean()){
				ForwardWinChange(ADT_AR_Main_Tip.class);
			}else{
				MenuControlIF.Instance().setWinchangeWithoutAnimation();
				ForwardARscreenControl.getinstance().setDRIRFunChangeState( DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE,DRIRAppMain.DRIRAPP_AR_MODE);
				ForwardWinChange(ADT_AR_Main.class);
			}
		}
		
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			DialogTools.createExitDialog(this).show();
			return true;
		}		
		return super.OnKeyDown(keyCode, event);
	}

}
