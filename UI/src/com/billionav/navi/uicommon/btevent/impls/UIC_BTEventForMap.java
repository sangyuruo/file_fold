package com.billionav.navi.uicommon.btevent.impls;

import com.billionav.navi.system.PLog;
import com.billionav.navi.uicommon.UIC_CradleCommon;
import com.billionav.navi.uicommon.btevent.UIC_BTEventAdapter;

public class UIC_BTEventForMap implements UIC_BTEventAdapter{

	@Override
	public void offBluetooth() {
	}

	@Override
	public void onBluetooth() {
		if(UIC_CradleCommon.isManualConnectCradle()){
			return;
		}
		//according to flow do something
//		jniSetupControl m_cSetupControl = new jniSetupControl();
//		if (m_cSetupControl
//				.GetInitialStatus(jniSetupControl.STUPDM_LAST_GPS_SETTING) == jniSetupControl.STUPDM_COMMON_OFF) {
//			PLog.v("GPS", "not use gps");
//			return;
//		}
//		if (jniLocInfor.getLocInfor().GetCertificateRet()
//				&& (CLocationListener.Instance().isCurrentExternalGpsOn() == true)) {
//			PLog.v("GPS", "use ext gps");
//			return;
//		}
//		if (UIC_CradleCommon.getNumOfBackupList() < 1) {
//			PLog.v("GPS", "getNumOfBackupList() < 1");
//			//					   CLocationListener.Instance().SwitchToInterGPS(true);
//			return;
//		}
//		if (UIC_CradleCommon.getNumOfMixDevices() == -1) {
//			PLog.v("GPS", "NumOfMixDevices() == -1");
//			//					   CLocationListener.Instance().SwitchToInterGPS(true);
//			return;
//		}
//		if (!com.billionav.navi.uicommon.UIC_CradleCommon
//				.autoConnect()) {
//			PLog.v("GPS", "autoConnect false");
//			//					   CLocationListener.Instance().SwitchToInterGPS(true);
//			return;
//		} else {
//			PLog.v("LOCATION_UI", "Try to out gps");
////			NaviCheckOutGps = true;
//		}
	}

	@Override
	public void turningOffBluetooth() {
	}

	@Override
	public void turningOnBluetooth() {
	}
	
	private UIC_BTEventForMap(){};
	
	private static UIC_BTEventForMap instance;
	
	public static UIC_BTEventForMap getInstance(){
		if(instance == null){
			instance = new UIC_BTEventForMap();
		}
		
		return instance;
	}

	@Override
	public void startAction() {
	}

}
