package com.billionav.navi.naviscreen.tools;


import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.KeyEvent;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.jni.UIPathControlJNI;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.datasynccontrol.DataSyncControl_ManagerIF;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSAnimationID;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.auth.ADT_Auth_Login;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.navi.naviscreen.map.ADT_DR_Main;
import com.billionav.navi.naviscreen.map.ForwardARscreenControl;
import com.billionav.navi.naviscreen.route.ADT_Route_Profile;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.system.SystemInfo;
import com.billionav.navi.uicommon.UIC_RouteCommon;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.ui.R;

public class ADT_Tools_Main extends PrefenrenceActivityBase {

//	private CheckBoxPreference openDriving = null;
//	private PreferenceScreen drivingReport = null;
//	private PreferenceScreen drivingSetting = null;
//	
	private PreferenceScreen actionReport = null;
	private PreferenceScreen actionList = null;
	
	private PreferenceScreen driveRecord = null;
//	private PreferenceScreen downRoute = null;


	private CProgressDialog dialog = null;
	private final CProgressDialog progressDialog = null;
	
	 private PreferenceCategory analysisCategory;
	 private PreferenceCategory otherCategory;


	private boolean isCancle = false; 
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.OnCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.adt_tools_main);
		setTitle(R.string.STR_MM_10_01_01_10);

		findViews();
		setOnClickEvent();
		if (!SystemTools.isCH()) {
			((PreferenceScreen) getPreferenceManager().findPreference(
					"rootPreference")).removePreference(analysisCategory);
			otherCategory.removePreference(driveRecord);
		}else if(SystemTools.getApkEdition().equals(SystemTools.EDITION_LUXGEN)){
			((PreferenceScreen) getPreferenceManager().findPreference(
					"rootPreference")).removePreference(analysisCategory);
		}
	}
	
	private void findViews() {
//		openDriving = (CheckBoxPreference) getPreferenceManager().findPreference("opendriving");
//		drivingReport = (PreferenceScreen) getPreferenceManager().findPreference("drivingreport");
//		drivingSetting = (PreferenceScreen) getPreferenceManager().findPreference("drivingsetting");
		actionReport = (PreferenceScreen) getPreferenceManager().findPreference("drivingaction");
		actionList = (PreferenceScreen) getPreferenceManager().findPreference("drivingactionlist");
		driveRecord = (PreferenceScreen) getPreferenceManager().findPreference("drivingrecord");
//		downRoute = (PreferenceScreen) getPreferenceManager().findPreference("downloadroute");
		analysisCategory = (PreferenceCategory) getPreferenceManager().findPreference("analysis");
		otherCategory = (PreferenceCategory) getPreferenceManager().findPreference("other");
//		downRoute
	}
	
	private void setOnClickEvent() {
//		openDriving
//				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//
//					@Override
//					public boolean onPreferenceChange(Preference preference,
//							Object newValue) {
//						// TODO Auto-generated method stub
//						setEcoOpen((Boolean) newValue);
//						// return false;
//						return true;
//					}
//				});
//
//		drivingReport
//				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//
//					@Override
//					public boolean onPreferenceClick(Preference preference) {
//						// TODO Auto-generated method stub
//						MenuControlIF.Instance().SetWinchangeAnimation(
//								NSAnimationID.Menu2Menu_Slide_R2L);
//						MenuControlIF.Instance().ForwardWinChange(
//								ADT_Eco_Info.class);
//						return true;
//					}
//				});
//		
//		drivingSetting.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				// TODO Auto-generated method stub
//				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
//				MenuControlIF.Instance().ForwardWinChange(ADT_Eco_Settings.class);
//				return true;
//			}
//		});
//		
		actionReport.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				if(!isNetEnable()){
					return true;
				}
				if(UserControl_ManagerIF.Instance().HasLogin()){
					MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
					MenuControlIF.Instance().ForwardWinChange(ADT_Driving_Behavior_Myreport.class);
				}else{
					showDialog(R.string.STR_MM_10_04_02_20,R.string.STR_MM_10_04_02_21,false,new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							ForwardWinChange(ADT_Auth_Login.class);
						}
					});
				}
				return true;
			}
		});
		
		actionList.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				if(!isNetEnable()){
					return true;
				}
				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
				MenuControlIF.Instance().ForwardWinChange(ADT_Driving_Behavior.class);
				return true;
			}
		});
		
		driveRecord.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_LOW_DM_MODE);
				MenuControlIF.Instance().setWinchangeWithoutAnimation();
				ForwardWinChange(ADT_DR_Main.class);
				return true;
			}
		});
//		downRoute.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				// TODO Auto-generated method stub
//				if (isNetEnable()) {
//					lockForClickListener();
//					if(UserControl_ManagerIF.Instance().HasLogin()){
//						DataSyncControl_ManagerIF.Instance().GetTheWebRouteExistStatus(
//							SystemInfo.getDeviceNo());
////						progressDialog1 = CProgressDialog.makeProgressDialog(ADT_Tools_Main.this, "���Ժ�");
////						progressDialog1.show();
//						showCustomDialog12(R.string.MSG_10_01_01_02_11,new OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								// TODO Auto-generated method stub
//								isCancle = true;
//							}
//						});
//					}else{
//						showDialog(R.string.STR_MM_10_04_02_20,R.string.STR_MM_10_04_02_36,true,new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								// TODO Auto-generated method stub
//								ForwardWinChange(ADT_Auth_Login.class);
//							}
//						});
//					}
//					
//				}
//				return true;
//			}
//		});
		
	}
	private  boolean isNetEnable() {
		boolean netDisconntect = (PConnectReceiver.getConnectType() != PConnectReceiver.CONNECT_TYPE_NONE);
		if(!netDisconntect) {
			CustomToast.showToast(this, R.string.STR_MM_01_01_01_13, 2000);
			return false;
		}
		
		return true;
	}
	
	public final void lockForClickListener() {
		LockScreen();
		unLockHandler.sendEmptyMessageDelayed(1, 300);
	}
	
	private Handler unLockHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what == 1) {
				UnlockScreen();
			}
		};
	};


	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		int iTriggerID = triggerInfo.GetTriggerID();
		int iParams = (int) triggerInfo.GetlParam1();
		switch(iTriggerID)
		{
		case NSTriggerID.UIC_MN_TRG_DS_GETROUTESTATUS:
			if(isCancle == true){
				isCancle = false;
				return true;
			}
			if(dialog != null){
				dialog.dismiss();
			}
		    if(iParams == 0){
		    	int status = DataSyncControl_ManagerIF.Instance().getM_cRouteInfo().getRouteStatus();
		    	if(status == 0){
		    		CustomToast.showToast(ADT_Tools_Main.this, getString(R.string.MSG_10_01_01_02_14), 1000);	
		    	}else if(status == 1 || status == 2){
		    		if(UIC_RouteCommon.Instance().isRouteExistAndGuideOn()){
		    			//has route
		    			showRouteDownDialog();
		    		}else{
		    			//no route
		    			DataSyncControl_ManagerIF.Instance().GetTheWebRoute(SystemInfo.getDeviceNo());
		    			showCustomDialog();
		    		}
		    	}
		    }else {
		    	CustomToast.showToast(ADT_Tools_Main.this, R.string.MSG_10_01_01_02_13, 1000);	
		    }
			return true;
		case NSTriggerID.UIC_MN_TRG_DS_GETROUTE_SYNC_RESULT:
			if(isCancle == true){
				isCancle = false;
				return true;
			}
			if(iParams == 0){
				UIPathControlJNI path = new UIPathControlJNI();
				path.SetDownloadFilePath(triggerInfo.GetString1());
				path.StartUIPathFinding(UIPathControlJNI.UIC_PT_ROUTE_DOWNLOAD);	
			}else{
				if(dialog!=null){
					dialog.dismiss();
				}
				if(progressDialog!=null){
					progressDialog.dismiss();
				}
				CustomToast.showToast(ADT_Tools_Main.this, R.string.MSG_10_01_01_02_15, 1000);
			}
			return true;
		case NSTriggerID.UIC_MN_TRG_PATH_FIND_START:
	//		showCustomDialog();
			if(isCancle == true){
				isCancle = false;
				return true;
			}
			return true;
		case NSTriggerID.UIC_MN_TRG_PATH_FIND_FINISH_RESULT_FAIL:
			if(dialog!=null){
				dialog.dismiss();
			}
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
			switch((int)triggerInfo.GetlParam3()){
			case 0:
				ForwardWinChange(ADT_Route_Profile.class);
				return true;
			case 1:
				CustomToast.showToast(ADT_Tools_Main.this, R.string.MSG_10_01_01_02_15, 1000);	
				return true;
				default:
					CustomToast.showToast(ADT_Tools_Main.this, R.string.MSG_10_01_01_02_15, 1000);	
					return true;	
			}
		default:
			return super.OnTrigger(triggerInfo);
		}
	}

	private void showRouteDownDialog() {
		CustomDialog cDialog = new CustomDialog(this);
		cDialog.setTitle(R.string.MSG_10_01_01_02_06);
		cDialog.setMessage(R.string.MSG_10_01_01_02_07);
		cDialog.setEnterBackKeyAllowClose(true);
		cDialog.setNegativeButton(R.string.MSG_10_01_01_02_03,null);
		cDialog.setPositiveButton(R.string.MSG_10_01_01_02_04, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DataSyncControl_ManagerIF.Instance().GetTheWebRoute(SystemInfo.getDeviceNo());
				showCustomDialog12(R.string.MSG_10_01_01_01_02,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						isCancle = true;
					}
				});
			}
		});
		cDialog.show();
	}
	private void showCustomDialog(){
		dialog = new CProgressDialog(this);
		dialog.setText(R.string.MSG_10_01_01_01_02);
		dialog.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					dialog.dismiss();
					isCancle = true;
				}
				return false;
			}
		});
		dialog.setNegativeButton(R.string.STR_COM_001, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				isCancle = true;
			}
		});
		dialog.show();
	}
	
	private void showCustomDialog12(int textId,OnClickListener l){
		dialog = new CProgressDialog(this);
		dialog.setText(textId);
		dialog.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					dialog.dismiss();
					isCancle = true;
				}
				return false;
			}
		});
		dialog.setNegativeButton(R.string.STR_COM_001,l);
		dialog.show();
	}
	
   private void showDialog(int titleID,int messageID, boolean allowClose, DialogInterface.OnClickListener listener){
	   CustomDialog b = new CustomDialog(
				ADT_Tools_Main.this);
		b.setMessage(messageID);
		b.setEnterBackKeyAllowClose(allowClose);
		b.setTitle(titleID);
		b.setNegativeButton(R.string.STR_MM_10_04_02_22, null);
		b.setPositiveButton(R.string.STR_MM_10_04_02_23,listener);
		b.show();
   }
	
//	private void setEcoOpen(boolean value){
////		int v = (value)?jniSetupControl.STUPDM_COMMON_ON:jniSetupControl.STUPDM_COMMON_OFF;
////	    jniSetupControl.set(jniSetupControl.STUPDM_ECO_DRIVE, v);
//	}

}
