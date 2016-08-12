package com.billionav.navi.naviscreen.open;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.billionav.jni.UILocationControlJNI;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.W3JNI;
import com.billionav.navi.component.dialog.CProgressBarDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.gps.CLocationListener;
import com.billionav.navi.intentOpen.IntentOpenCtrl;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.MenuControlIF.InitlizationScreen;
import com.billionav.navi.menucontrol.MenuControlIF.MapScreen;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.dest.ADT_Top_Menu;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.system.AplRuntime;
import com.billionav.navi.system.PLog;
import com.billionav.navi.system.StartRuntimeTask;
import com.billionav.navi.system.StartRuntimeTask.TaskSyncController;
import com.billionav.navi.uicommon.UIC_IntentCommon;
import com.billionav.navi.uitools.OpeningTriggerReceiver;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.update.UpdateController;
import com.billionav.ui.R;

public class ADT_Openning extends ActivityBase implements InitlizationScreen,MapScreen{
	
	private CProgressBarDialog progressBarDialog;
	private boolean FirstMapDrawDone = false;
	private boolean switchGPS = false;
	private ImageView imageView;
	private TaskSyncController taskController = StartRuntimeTask.getInstance().getTaskController();
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		Log.d("test", "Opening started");
//		AplLog.Output(jniAL_Log.AL_LOG_KIND_START, "Activity Opening onCreate. [TC]"+API.getTickCount());
		
		PLog.initLogOutputStatus();		
		
		setContentView(R.layout.adt_opening, false);
		
		imageView = (ImageView) findViewById(R.id.openingimage);;
		setBackgroud(getResources().getConfiguration());
		OpeningTriggerReceiver triggerCtrl = OpeningTriggerReceiver.getInstance();
		//from ADT_Opening_Disclaimer and received trigger fist map draw done.
		if(triggerCtrl.hasFirstMapDrawDoneReceived()) {
			Log.d("tests", "send trigger = UIC_MN_TRG_MAP_FIRST_DRAW_DONE");
			NSTriggerInfo triggerInfo = new NSTriggerInfo();
			triggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_MAP_FIRST_DRAW_DONE;
			MenuControlIF.Instance().TriggerForScreen(triggerInfo);
		}
		if(triggerCtrl.hasNdataDownlaodNetError()) {
			NSTriggerInfo triggerInfo = new NSTriggerInfo();
			triggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DLNDATA_NET_ERROR;
			MenuControlIF.Instance().TriggerForScreen(triggerInfo);
		}
		if(triggerCtrl.getNDataCheckResult() > -1) {
			NSTriggerInfo triggerInfo = new NSTriggerInfo();
			triggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DLNDATA_CHECK_FINISHED;
			triggerInfo.m_lParam1 = triggerCtrl.getNDataCheckResult();
			MenuControlIF.Instance().TriggerForScreen(triggerInfo);
		}
		if(triggerCtrl.hasNdataDownlaodFinished()) {
			NSTriggerInfo triggerInfo = new NSTriggerInfo();
			triggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DLNDATA_DOWNLOAD_FINISHED;
			MenuControlIF.Instance().TriggerForScreen(triggerInfo);
		}
		
		triggerCtrl.removeFromGloble();
		
//		new Handler().postDelayed(new Runnable() {
//			public void run() {
//				AplRuntime.Instance().AplInitMainThrd();
//				NaviViewManager.GetViewManager().initGlobalInfo();
//				checkGpsStates();
//			}
//		}, 4000);
	}
	
	protected int onConnectedScreenId() {
		return SCRMapID.ADT_ID_Navigation;
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		if(switchGPS){
			new Handler(){
				public void handleMessage(android.os.Message msg) {
					if(msg.what != 0x10) { 
						return;
                    }
					switchGPS = false;
					CLocationListener.Instance().SwitchToInterGPS(false);
					checkNextScreen();
				}
			}.sendEmptyMessageDelayed(0x10, 500);
		}
//		MapView.getInstance().startMap();
	}
	
	
	
	public int checkDataFormat() {
		return UpdateController.getInstance().checkDataFormat();
	}
	@Override
	protected boolean isNeedSetScreenId() {
		return false;
	}
	
	private void checkGpsStates() {
		CLocationListener.Instance().SwitchToInterGPS(true);
		
		if(SharedPreferenceData.MATCHING_LOG.getBoolean()){
			UILocationControlJNI.getInstance().StartLogging(0);
		} else {
			UILocationControlJNI.getInstance().StopLogging();
		}
		
		if(isGpsOn()) {
//			int nGPSStatus = jniSetupControl.get(jniSetupControl.STUPDM_OPENING_GPS_STATUS);
//			jniSetupControl.set(jniSetupControl.STUPDM_LAST_GPS_SETTING, nGPSStatus);
//			CLocationListener.Instance().SwitchToInterGPS(false);
			
			checkNextScreen();
		} else {
			
			if(needRemind()){
				showGPSDialogs();
			} else {
//				if (jniSetupControl.isOn(jniSetupControl.STUPDM_OPENING_GPS_STATUS)) {
//					jniSetupControl.setOn(jniSetupControl.STUPDM_LAST_GPS_SETTING);
				    CLocationListener.Instance().SwitchToInterGPS(false);
//				} else {
//					jniSetupControl.setOff(jniSetupControl.STUPDM_LAST_GPS_SETTING);
//				}
				
				checkNextScreen();
			}
		}
	}
	
	private boolean needRemind() {
//		return SharedPreferenceData.OPEN_GPS_NEED_TIP.getBoolean();
		return false;
	}

	private boolean isGpsOn() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
		boolean isGpsOn = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
		return isGpsOn;
	}
	
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		
		// after LIB start could receive UIC_MN_TRG_MAP_DRAW_DONE/UIC_MN_TRG_ROUTE_RECOVER_INIT_DONE trigger
		int iTriggerID = triggerInfo.GetTriggerID();
		switch(iTriggerID)
		{
//			case 1000000:checkGpsStates();break;
			case NSTriggerID.UIC_MN_TRG_MAP_FIRST_DRAW_DONE:
			case NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE:
				if ( FirstMapDrawDone == false )
				{
					AplRuntime.Instance().AplInitMainThrd();
//					NaviViewManager.GetViewManager().initGlobalInfo();
//					UIMapControlJNI.SetScreenID(SCRMapID.ADT_ID_Navigation);
					MapView.getInstance().startMap();

					checkGpsStates();
//					AplLog.Output(jniAL_Log.AL_LOG_KIND_START, "First Map Draw Done. [TC]"+API.getTickCount());
					
					FirstMapDrawDone = true;					
				}
				break;
			case NSTriggerID.UIC_MN_TRG_ROUTE_RECOVER_INIT_DONE:
//				AplLog.Output(jniAL_Log.AL_LOG_KIND_START, "Route Recover Init Done. [TC]"+API.getTickCount());
				
				break;
			case NSTriggerID.UIC_MN_TRG_DLNDATA_NET_ERROR:
				AplRuntime.Instance().startNDataDownloadIfNeeded(-1);
				showNDataNetErrorDialog();
				break;
			case NSTriggerID.UIC_MN_TRG_DLNDATA_CHECK_FINISHED:
//				AplRuntime.Instance().startNDataDownloadIfNeeded(triggerInfo.GetlParam1());
//				if(triggerInfo.GetlParam1() > 0){
//					showNDataDownloadDialog();
//				}else{
//					taskController.raiseSignal();
//				}
				
				showStartDownloadNDataDialog();
				break;
			case NSTriggerID.UIC_MN_TRG_DLNDATA_DOWNLOADED_FILE:
				if(null != progressBarDialog){
					progressBarDialog.setProgress((int)(triggerInfo.GetlParam2()*100/triggerInfo.GetlParam1()));
				}else{
					showNDataDownloadDialog();
				}
				break;
			case NSTriggerID.UIC_MN_TRG_DLNDATA_DOWNLOAD_FINISHED:
				if(null != progressBarDialog){
					progressBarDialog.cancel();
				}
				Log.d("test","UIC_MN_TRG_DLNDATA_DOWNLOAD_FINISHED received, trigger info="+triggerInfo.GetlParam1());
				AplRuntime.Instance().setLatestNDataVer();
				taskController.raiseSignal();
				break;
			default:
				super.OnTrigger(triggerInfo);
				break;
		}
    	return true;
	}

	private void stopBackgroundThread() {
		StartRuntimeTask.getInstance().setExitbackgroundThread(true);
		taskController.raiseSignal();
	}
	
	private void showNDataNetErrorDialog() {
		CustomDialog cd = new CustomDialog(this);
		cd.setTitle(R.string.STR_COM_029);
		cd.setMessage(R.string.MSG_COM_01_03);
		cd.setNegativeButton(R.string.MSG_01_02_01_01_04,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				stopBackgroundThread();
				OpeningTriggerReceiver.getInstance().removeNdataDownlaodNetErrorFlag();
				NaviViewManager.GetViewManager().finish();
			}
		} );
//		cd.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//		
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			// TODO Auto-generated method stub
//			AplRuntime.Instance().startNDataDownloadIfNeeded(1);
//		}
//	} );
		cd.setCancelable(false);
		cd.show();
	}
	
	
	private void showNDataDownloadDialog(){
		progressBarDialog = CProgressBarDialog.makeProgressDialog(this, R.string.MSG_COM_01_04);
		progressBarDialog.setProgress(0);
		progressBarDialog.setCancelable(false);
		progressBarDialog.show();
	}
	
	// show gps  dialog
	private void showGPSDialogs(){
		Log.d("tests", "show GPS : ADT_Openning");
        final CustomDialog dialog = new CustomDialog(this);
        dialog.setTitle(R.string.MSG_01_00_01_01);
        dialog.setMessage(R.string.MSG_01_00_01_04);
        dialog.setCheckBox(R.string.MSG_01_00_01_03, false);
		
        dialog.setPositiveButton(R.string.STR_COM_035, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dg, int which) {
				gpsDialogCancel(dialog, true);
			}
		});
		
		
        dialog.setNegativeButton(R.string.STR_COM_001, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dg, int which) {
				gpsDialogCancel(dialog, false);
				
			}
		});
		
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return true;
			}
		});
		
		
	 	dialog.setCancelable(false);
	 	dialog.show();
	}
	
	private void showStartDownloadNDataDialog(){
		
        final CustomDialog dialog = new CustomDialog(this);
        dialog.setTitle(R.string.STR_MM_01_00_01_02);
        int dataSize = 0;
        String dataSizeStr = "";
        try{
        	dataSize = Integer.parseInt(W3JNI.getConfigValue("NDataSize"));
        }catch (Exception e){
        }
        dataSizeStr = String.format("%.2fMB", ((double)dataSize)/1024/1024);
        dialog.setMessage(getString(R.string.MSG_01_00_01_09, dataSizeStr));
		
        dialog.setPositiveButton(R.string.STR_MM_01_04_01_101, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dg, int which) {
				AplRuntime.Instance().startNDataDownloadIfNeeded(1);
				OpeningTriggerReceiver.getInstance().removedataDownlaodFinishedFlag();
			}
		});
		
		
        dialog.setNegativeButton(R.string.STR_MM_02_02_02_09, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dg, int which) {
				OpeningTriggerReceiver.getInstance().removedataDownlaodFinishedFlag();
				SystemTools.exitSystem();
			}
		});
		
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return true;
			}
		});
		
		
	 	dialog.setCancelable(false);
	 	dialog.show();
	}
	
	
	void gpsDialogCancel(CustomDialog dialog, boolean isPostivePress){
//		int status = isPostivePress ? jniSetupControl.STUPDM_COMMON_ON:jniSetupControl.STUPDM_COMMON_OFF;
//		
//		jniSetupControl setup = new jniSetupControl();
//		jniSetupControl.set(jniSetupControl.STUPDM_LAST_GPS_SETTING, status);
//		
//		if(dialog.isChecked()) {
//			jniSetupControl.setOn(jniSetupControl.STUPDM_OPENING_GPS_ABORT_STATUS);
			SharedPreferenceData.OPEN_GPS_NEED_TIP.setValue(!dialog.isChecked());
//		}
//		else {
//			jniSetupControl.setOff(jniSetupControl.STUPDM_OPENING_GPS_ABORT_STATUS);
//		}			
//		setup.SetInitialStatus(jniSetupControl.STUPDM_OPENING_GPS_STATUS, status);
		
		if(isPostivePress) {
//			int backupList = UIC_CradleCommon.getNumOfBackupList();

//			if (backupList <= 0 /*|| !UIC_CradleCommon.autoConnect()*/)
//			{
				startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				switchGPS = true;
//			}
		} else {
//			CLocationListener.Instance().SwitchToDisable(true);		
			checkNextScreen();
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setBackgroud(newConfig);
	}

	public ImageView getImageView(){
		return imageView;
	}
	
	protected void setBackgroud(Configuration newConfig) {
		if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			imageView.setBackgroundResource(R.drawable.navicloud_and_517a);
	  	}else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
	  		imageView.setBackgroundResource(R.drawable.navicloud_and_517b);
		}
	}
	
	private void checkNextScreen(){
		MenuControlIF.Instance().setWinchangeWithoutAnimation();
		
		String edition = SystemTools.getApkEdition();
		if(SystemTools.EDITION_HUD.equals(edition)) {
			NaviViewManager.GetViewManager().notifyLibLoaded();
		} else if(SystemTools.EDITION_LUXGEN.equals(edition)) {
//			if(IntentOpenCtrl.getIntentKind() == IntentOpenCtrl.INTENT_KIND_NONE) {
//				MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Top_Menu.class);
//			} else{
//				if(IntentOpenCtrl.getIntentKind() == IntentOpenCtrl.INTENT_KIND_ROUTE) {
					MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Main_Map_Navigation.class);
//				}
//			}
			OpeningCommon.loginByIMEI();
			return;
		} 
		
		//common process check whether show Disclaimer dialog
		if(UIC_IntentCommon.Instance().isIntentCallVaild()) {
			ForwardKeepDepthWinChange(ADT_Main_Map_Navigation.class);
		} else {
			OpeningCommon.checkLoginStatus();
		}
		
	
		
		
	}
	
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
	}

}
