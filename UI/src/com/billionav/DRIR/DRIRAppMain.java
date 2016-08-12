package com.billionav.DRIR;

import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.billionav.DRIR.GSensorLevel.DRIRGSensorLevel;
import com.billionav.DRIR.GSensorLevel.DRIRGSensorLevelGet;
import com.billionav.DRIR.HWInfo.HWinfo;
import com.billionav.DRIR.PictureHandler.PictureHandler;
import com.billionav.DRIR.Upload.UploadHandler;
import com.billionav.DRIR.Utils.BatteryWatcher;
import com.billionav.DRIR.Utils.VersionInfo;
import com.billionav.DRIR.jni.jniDRIR_CameraViewCtrl;
import com.billionav.DRIR.jni.jniDRIR_CreateUuid;
import com.billionav.DRIR.jni.jniDRIR_DispControl;
import com.billionav.DRIR.jni.jniDRIR_GSensor;
import com.billionav.DRIR.jni.jniDRIR_HWInfo;
import com.billionav.DRIR.jni.jniDRIR_ImgDataServer;
import com.billionav.DRIR.jni.jniDRIR_MainControl;
import com.billionav.DRIR.jni.jniDRIR_ScreenParameters;
import com.billionav.DRIR.jni.jniDRIR_TimeZone;
import com.billionav.DRIR.jni.jniDRIR_UploadControl;
import com.billionav.DRIR.jni.jniDRIR_VersionInfo;
import com.billionav.jni.jniSetupControl;
import com.billionav.navi.camera.CameraView;
import com.billionav.navi.system.SystemInfo;
import com.billionav.navi.uitools.SharedPreferenceData;
 
public class DRIRAppMain {
	public static final int DRIRAPP_AR_MODE = 0;
	public static final int DRIRAPP_DM_MODE = 1;
	public static final int DRIRAPP_CAMEEA_MODE = 2;
	public static final int DRIRAPP_INTERNAL_OTHER_MODE = 3;
	public static final int DRIRAPP_EXTERNAL_OTHER_MODE = 4;
	public static final int DRIRAPP_LOW_DM_MODE = 5;
	
	private static final int HIGH_QUALITY_MODE = 0;
	private static final int LOW_QUALITY_MODE = 1;
	private static final int PIC_ONLY_MODE = 2;
	private static final int LOW_QUALIY_DM_MODE = 3;
	
	public static void DRIRAppStart(Context c, WindowManager mng)
	{
		//DRIR_GSensor.getInstance(c).register();
		
		//To get display metrics
		DisplayMetrics metrics = new DisplayMetrics(); 
		mng.getDefaultDisplay().getMetrics(metrics); 
		HWinfo.SetDisplayMetrics(metrics.heightPixels, metrics.widthPixels);
				
		//To get hardware info
		HWinfo.SetHWInfo();
		//Init UploadHandler
		UploadHandler.getInstance().Init();
		
		int iNumCores = CPUTool.getNumCores();
		int iCPUFreq = CPUTool.getMaxCpuFreq()/1000;
		HWinfo.SetCPUInfo(iNumCores,iCPUFreq);
		
		BatteryWatcher.CreateInstance(c).RegisterBatteryWatcher();
		//Set device ID
		//String device_id = Secure.getString(c.getContentResolver(), Secure.ANDROID_ID);
		String device_id = SystemInfo.GetSysUUID ();
		//Log.i("deviceID", device_id+"deviceID1 length:"+device_id.length());
		if(null != device_id)
		{
			jniDRIR_HWInfo.SetDeviceID(device_id, device_id.length());
		}
		
		//Set apl version
		int aplversion = VersionInfo.getAplVersion(c);
		jniDRIR_VersionInfo.SetAplVersion(aplversion);
		
		//set the time zone
		TimeZone  timeZone = TimeZone.getDefault();
		int iTimeZone = timeZone.getRawOffset()/(1000*60*60);	//milliseconds->hour
		jniDRIR_TimeZone.SetTimeZone(iTimeZone);
		
		///////yihongjun temp code
		//To get screen parameters
		DisplayMetrics dm=new DisplayMetrics();  
		mng.getDefaultDisplay().getMetrics(dm);  
		float width = dm.widthPixels * dm.density;  
		float height = dm.heightPixels * dm.density; 
		jniDRIR_ScreenParameters.SetHeight(height);
		jniDRIR_ScreenParameters.SetWidth(width);
		
		jniDRIR_CameraViewCtrl.J2CAttach();
		// attach 
		jniDRIR_DispControl.J2CAttach();
		// attach 	
		jniDRIR_CreateUuid.J2CAttach();
		// attach 	
		jniDRIR_UploadControl.J2CAttach();
		
		setDRIRValue();		
		InitGSensorLevel();
		
		/// Temp code by sunzhicheng
		jniDRIR_MainControl.DRIRModuleStart();
				
		jniDRIR_ImgDataServer.SetCameraParameter(640, 480, jniDRIR_ImgDataServer.IMGDS_NV21);
		jniDRIR_CameraViewCtrl.SetMinMode(false);
		jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
		
		jniDRIR_CameraViewCtrl.onSurfaceChange();
		CameraView.Instance().EnableOrientationChange();
	}

	public static void setDRIRValue() {
		//Init status 
//		
//		jniSetupControl setupCtl = new jniSetupControl();
//		jniDRIR_MainControl.SetLaneDetectStatus(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DEBUG_LANE_DETECTION_STATUS));
//		jniDRIR_MainControl.SetVehicleDetectStatus(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DEBUG_VEHICLE_DETECTION_STATUS));
//		jniDRIR_MainControl.SetLaneDepartInfoStatus(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DEBUG_LANE_DEPARTURE_INDORMATION_STATUS));
//		jniDRIR_MainControl.SetStopGoStatus(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DEBUG_STOP_GO_ASSIST_STATUS));
//		jniDRIR_MainControl.SetTLDStatus(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DEBUG_TRAFFICLIGHT_DETECTION_STATUS));
//		jniDRIR_MainControl.SetSpeedAvoidStatus(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DEBUG_SPEED_AVOIDANC_STATUS));
//		jniDRIR_MainControl.SetCarInfoStatus(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DEBUG_CAR_INFORMATION_STATUS));
//		jniDRIR_MainControl.SetDistanceInfoStatus(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DEBUG_DISTANCE_INDOMATION_STATUS));
//		jniDRIR_MainControl.SetCameraHInputStatus(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DEBUG_CAMERAH_INPUT_STATUS));
//		jniDRIR_MainControl.SetDemoMode(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DRIR_DEMO_MODE));
//		jniDRIR_MainControl.SetSpeedLimit(setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DRIR_SPEED_LIMIT));
//		jniDRIR_MainControl.SetYellowAlterTime(setupCtl.getFloat(jniSetupControl.STUPDM_DRIR_YELLOW_ALTER_TIME));
//		jniDRIR_MainControl.SetRedAlterTime(setupCtl.getFloat(jniSetupControl.STUPDM_DRIR_RED_ALTER_TIME));
//				 
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_ONOFF, SharedPreferenceData.DRIR_SETTING_DRID_ONOFF.getInt());
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_RECMODE, SharedPreferenceData.DRIR_SETTING_DRID_RESOLUTION.getInt());
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_RECDQUALITY, SharedPreferenceData.DRIR_SETTING_DRID_RECDQUALITY.getInt());
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_GSNSLEVEL, SharedPreferenceData.DRIR_SETTING_DRID_GSNSLEVEL.getInt());
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_SDRECMODE, SharedPreferenceData.DRIR_SETTING_DRID_DMRECMODE.getInt());
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_GSNSFUC, SharedPreferenceData.DRIR_SETTING_DRID_GSNSFUC.getInt());
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_SDCAPACITY, SharedPreferenceData.DRIR_SETTING_DRID_SDCAPACITY.getInt());
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_LOG, SharedPreferenceData.DRIR_SETTING_DRID_LOG.getInt());
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_UPLOADFUC, SharedPreferenceData.DRIR_SETTING_DRID_UPLOADFUC.getInt());
		
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_RECMODE, SharedPreferenceData.DRIR_SETTING_DRID_RECMODE.getInt());
		jniDRIR_MainControl.SetDRSetting(jniDRIR_MainControl.DRIR_SETTING_DRID_SDRECMODE, SharedPreferenceData.DRIR_SETTING_DRID_SDRECMODE.getInt());
		
		Log.i("DRIRAppMain", "Car model value:" + SharedPreferenceData.DRIR_SETTING_IRID_CARMODEL.getInt());
		jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_CARMODEL, SharedPreferenceData.DRIR_SETTING_IRID_CARMODEL.getInt());
		jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_FRONTCARINFO, SharedPreferenceData.DRIR_SETTING_IRID_FRONTCARINFO.getInt());
		jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_SPEEDLIMIT, SharedPreferenceData.DRIR_SETTING_IRID_SPEEDLIMIT.getInt());
		jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_CRASHTIMEALARM, SharedPreferenceData.DRIR_SETTING_IRID_CRASHTIMEALARM.getInt());
		jniDRIR_MainControl.SetIRSetting(jniDRIR_MainControl.DRIR_SETTING_IRID_ALTERLEVEL, SharedPreferenceData.DRIR_SETTING_IRID_ALTERLEVEL.getInt());
//		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_ONOFF, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_ONOFF.getInt());
		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_RECDQUALITY, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_RECDQUALITY.getInt());
		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_GSNSFUC, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_GSNSFUC.getInt());
		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_LOG, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_LOG.getInt());
		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_FRAMERATE, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_FRAMERATE.getInt());
		
		jniDRIR_MainControl.SetIRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_DEMORUNSPPED, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_DEMORUNSPPED.getInt());
		jniDRIR_MainControl.SetIRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_ROADSDET, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_ROADSDET.getInt());
		jniDRIR_MainControl.SetIRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_LDW, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_LDW.getInt());
		jniDRIR_MainControl.SetIRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_RESERVE1, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_RESERVE1.getInt());
		jniDRIR_MainControl.SetIRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_RESERVE2, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_RESERVE2.getInt());
		jniDRIR_MainControl.SetIRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_RESERVE3, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_RESERVE3.getInt());
		jniDRIR_MainControl.SetIRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_RESERVE4, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_RESERVE4.getInt());
		jniDRIR_MainControl.SetIRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_IRID_RESERVE5, SharedPreferenceData.DRIR_DEBUG_SETTING_IRID_RESERVE5.getInt());
				
		
//		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_ONOFF, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_ONOFF.getInt());
		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_RECDQUALITY, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_RECDQUALITY.getInt());
		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_GSNSFUC, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_GSNSFUC.getInt());
		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_RECDQUALITY, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_RECDQUALITY.getInt());
		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_LOG, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_LOG.getInt());
		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_RECORDCALIBRATE, SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_RECORD_CALIBRATE.getInt());
		jniDRIR_MainControl.SetDRDebugSetting(jniDRIR_MainControl.DRIR_DEBUG_SETTING_DRID_GSNSLEVEL , SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_GSNSLEVEL.getInt());
		jniDRIR_MainControl.SettingCommit();
	}
	
	public static void DRIRAppEnd(Context c)
	{
		Log.i("JNI_NATIVEC", "Navigation activity onPause function call DRIR modulestop start");
		//DRIR_GSensor.getInstance(c).unregister();
		CameraView.Instance().DiableOrientationChange();
		UploadHandler.getInstance().DeInit();
		jniDRIR_MainControl.DRIRModuleStop();
		
		BatteryWatcher.CreateInstance(c).UnRegisterBatteryWatcher();
		
		PictureHandler.getInstance().DeInit();
		jniDRIR_CreateUuid.J2CDetach();
		jniDRIR_DispControl.J2CDetach();
		jniDRIR_UploadControl.J2CDetach();
		jniDRIR_CameraViewCtrl.J2CDetach();
		Log.i("JNI_NATIVEC", "Navigation activity onPause function call DRIR modulestop end");
	}
	
	public static boolean checkHasCamera(Context c)
	{
		return HWinfo.IsExistCamera(c);
	}
	
	public static void DRIRScreenPause()
	{
		//jniDRIR_MainControl.DRIRFG2BG();
		//DRIRAudioRecord.createInstance().stopRecord();
		//jniDRIR_AudioData.StopRecord();
	}
	
	public static void DRIRScreenResume()
	{
		/*jniDRIR_CameraViewCtrl.SetMaxMode();
		if (SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_ONOFF.getInt() == 1)
			jniDRIR_CameraViewCtrl.SetLQMode();
		else
			jniDRIR_CameraViewCtrl.SetHQMode();*/
		
		//jniDRIR_MainControl.DRIRBG2FG();
		//DRIRAudioRecord.createInstance().init();
		//DRIRAudioRecord.createInstance().startRecord();
	}
	
	public static void WinChange2Map()
	{
		//jniDRIR_CameraViewCtrl.SetMinMode(false);
		//jniDRIR_CameraViewCtrl.SetLQMode();
	}
	
	public static void WinChange2Preview()
	{
		//jniDRIR_CameraViewCtrl.SetMinMode(true);
		//jniDRIR_CameraViewCtrl.SetLQMode();
	}
	
	
	public static void TakeOneShootPic()
	{
		PictureHandler.getInstance().setOneShootFlag(true, PictureHandler.MAP_TAKEPIC, -1);
	}

	
	/*
	 * For example, map to ar:DRIRFunChange(DRIRAPP_INTERNAL_OTHER_MODE, DRIRAPP_AR_MODE)
	 * AR to DM, DRIRFunChange(DRIRAPP_AR_MODE, DRIRAPP_DM_MODE)
	 * 
	 */
	public static void DRIRFunChange(int iLastMode, int iCurrentMode)
	{
		Log.i("CameraView", "iLastMode: " + iLastMode
				+ " iCurrentMode: " + iCurrentMode);
		switch (iLastMode)
		{
		case DRIRAPP_AR_MODE:
		{
			switch (iCurrentMode)
			{
			case DRIRAPP_AR_MODE:
				//Do nothing
				break;
			case DRIRAPP_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(HIGH_QUALITY_MODE);
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			case DRIRAPP_CAMEEA_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(PIC_ONLY_MODE);
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			case DRIRAPP_INTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(false);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_EXTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(true);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_LOW_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALIY_DM_MODE);
				CameraView.Instance().Maxprocess();		
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			default:
				break;
			}
		}
			break;
		case DRIRAPP_DM_MODE:
		{
			switch (iCurrentMode)
			{
			case DRIRAPP_AR_MODE:
			{
				jniDRIR_CameraViewCtrl.SetMaxMode();
				//if (SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_ONOFF.getInt() == 1)
					jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				//else
					//jniDRIR_CameraViewCtrl.SetQualityMode(HIGH_QUALITY_MODE);
				jniDRIR_CameraViewCtrl.onSurfaceChange();
			}
				break;
			case DRIRAPP_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(HIGH_QUALITY_MODE);
				break;
			case DRIRAPP_CAMEEA_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(PIC_ONLY_MODE);
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			case DRIRAPP_INTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(false);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_EXTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(true);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_LOW_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALIY_DM_MODE);
				CameraView.Instance().Maxprocess();
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			default:
				break;
			}
		}	
			break;
		case DRIRAPP_CAMEEA_MODE:
		{
			switch (iCurrentMode)
			{
			case DRIRAPP_AR_MODE:
			{
				jniDRIR_CameraViewCtrl.SetMaxMode();
				//if (SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_ONOFF.getInt() == 1)
					jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				jniDRIR_CameraViewCtrl.onSurfaceChange();
			}
				break;
			case DRIRAPP_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(HIGH_QUALITY_MODE);
				jniDRIR_CameraViewCtrl.onSurfaceChange();	
				break;
			case DRIRAPP_CAMEEA_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(PIC_ONLY_MODE);
				break;
			case DRIRAPP_INTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(false);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_EXTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(true);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_LOW_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALIY_DM_MODE);
				CameraView.Instance().Maxprocess();
				jniDRIR_CameraViewCtrl.onSurfaceChange();	
				break;
			default:
				break;
			}
		}
			break;
		case DRIRAPP_INTERNAL_OTHER_MODE:
		{
			switch (iCurrentMode)
			{
			case DRIRAPP_AR_MODE:
			{
				jniDRIR_CameraViewCtrl.SetMaxMode();
				//if (SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_ONOFF.getInt() == 1)
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				//else
					//jniDRIR_CameraViewCtrl.SetQualityMode(HIGH_QUALITY_MODE);
				CameraView.Instance().Maxprocess();
				jniDRIR_CameraViewCtrl.onSurfaceChange();	
			}
				break;
			case DRIRAPP_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(HIGH_QUALITY_MODE);
				CameraView.Instance().Maxprocess();
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			case DRIRAPP_CAMEEA_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(PIC_ONLY_MODE);
				CameraView.Instance().Maxprocess();
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			case DRIRAPP_INTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(false);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_EXTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(true);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_LOW_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALIY_DM_MODE);
				CameraView.Instance().Maxprocess();
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			default:
				break;
			}
		}
			break;
		case DRIRAPP_EXTERNAL_OTHER_MODE:
		{
			switch (iCurrentMode)
			{
			case DRIRAPP_AR_MODE:
			{
				jniDRIR_CameraViewCtrl.SetMaxMode();
				//if (SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_ONOFF.getInt() == 1)
					jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				//else
					//jniDRIR_CameraViewCtrl.SetQualityMode(HIGH_QUALITY_MODE);
			}
				break;
			case DRIRAPP_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(HIGH_QUALITY_MODE);
				break;
			case DRIRAPP_CAMEEA_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(PIC_ONLY_MODE);
				break;
			case DRIRAPP_INTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(false);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_EXTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(true);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_LOW_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALIY_DM_MODE);
				CameraView.Instance().Maxprocess();
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			default:
				break;
			}
		}
			break;
		case DRIRAPP_LOW_DM_MODE:
		{
			switch (iCurrentMode)
			{
			case DRIRAPP_AR_MODE:
				//Do nothing
				break;
			case DRIRAPP_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(HIGH_QUALITY_MODE);
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			case DRIRAPP_CAMEEA_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(PIC_ONLY_MODE);
				jniDRIR_CameraViewCtrl.onSurfaceChange();
				break;
			case DRIRAPP_INTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(false);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_EXTERNAL_OTHER_MODE:
				jniDRIR_CameraViewCtrl.SetMinMode(true);
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALITY_MODE);
				break;
			case DRIRAPP_LOW_DM_MODE:
				jniDRIR_CameraViewCtrl.SetMaxMode();
				jniDRIR_CameraViewCtrl.SetQualityMode(LOW_QUALIY_DM_MODE);
				CameraView.Instance().Maxprocess();
				break;
			default:
				break;
			}
		}
			break;
		default:
			break;
		}
	}
	
	public static void DRIRMoveToBack()
	{
		jniDRIR_CameraViewCtrl.MoveToBack();
	}
	
	public static void DRIRMoveToFornt()
	{
		jniDRIR_CameraViewCtrl.MoveToFront();
		
		if ((null != CameraView.Instance())
				&& (CameraView.Instance().isM_bIsSurfaceChg()))
		{
			jniDRIR_CameraViewCtrl.onSurfaceChange();
		}
			
	}
	
	private static void InitGSensorLevel()
	{
		List<DRIRGSensorLevel>levels = DRIRGSensorLevelGet.getGSLevel();
		
		if (null != levels)
		{
			int i = 0;
			for (Iterator iterator = levels.iterator(); iterator.hasNext();) {
				DRIRGSensorLevel level = (DRIRGSensorLevel) iterator.next();
				jniDRIR_GSensor.setGSLevelFromXML(i, level.getN1(), level.getN2(), 
						level.getA(), level.getB());
				++i;
			}	
		}
		else
		{
			jniDRIR_GSensor.setGSLevelFromMemory();
		}
	}
	
	public static class CPUTool{
		
	    private final static String kCpuInfoMaxFreqFilePath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";

	    public static int getMaxCpuFreq() {
	        int result = 0;
	        FileReader fr = null;
	        BufferedReader br = null;
	        try {
	            fr = new FileReader(kCpuInfoMaxFreqFilePath);
	            br = new BufferedReader(fr);
	            String text = br.readLine();
	            result = Integer.parseInt(text.trim());
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e){
	            e.printStackTrace();
	        } finally{
	            if (fr != null)
	                try {
	                    fr.close();
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            if (br != null)
	                try{
	                    br.close();
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	        }
	        Log.d("MYPROG", "Max FreqCPU: "+ result);
	        return result;
	    }

	    private final static String kCpuInfoMinFreqFilePath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";

	    /* ��ȡCPU��СƵ�ʣ���λKHZ�� */
	    public static int getMinCpuFreq(){
	        int result = 0;
	        FileReader fr = null;
	        BufferedReader br = null;
	        try {
	            fr = new FileReader(kCpuInfoMinFreqFilePath);
	            br = new BufferedReader(fr);
	            String text = br.readLine();
	            result = Integer.parseInt(text.trim());
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e){
	            e.printStackTrace();
	        } finally {
	            if (fr != null)
	                try {
	                    fr.close();
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            if (br != null)
	                try {
	                    br.close();
	                } catch (IOException e){
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	        }
	        Log.d("MYPROG", "Min FreqCPU: "+ result);
	        return result;
	    }

	    private final static String kCpuInfoCurFreqFilePath = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";

	    /* ʵʱ��ȡCPU��ǰƵ�ʣ���λKHZ�� */
	    public static int getCurCpuFreq(){
	        int result = 0;
	        FileReader fr = null;
	        BufferedReader br = null;
	        try {
	            fr = new FileReader(kCpuInfoCurFreqFilePath);
	            br = new BufferedReader(fr);
	            String text = br.readLine();
	            result = Integer.parseInt(text.trim());
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (fr != null)
	                try {
	                    fr.close();
	                } catch (IOException e){
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            if (br != null)
	                try{
	                    br.close();
	                } catch (IOException e){
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	        }
	        Log.d("MYPROG", "Current FreqCPU: "+ result);
	        return result;
	    }

	    /* ��ȡCPU���� */
	    public static String getCpuName(){
	        FileReader fr = null;
	        BufferedReader br = null;
	        try
	        {
	            fr = new FileReader("/proc/cpuinfo");
	            br = new BufferedReader(fr);
	            String text = br.readLine();
	            String[] array = text.split(":\\s+", 2);
	            for (int i = 0; i < array.length; i++){
	            }
	            Log.d("MYPROG", "CPU Name: "+ array[1]);
	            return array[1];
	        } catch (FileNotFoundException e){
	            e.printStackTrace();
	        } catch (IOException e){
	            e.printStackTrace();
	        } finally{
	            if (fr != null)
	                try{
	                    fr.close();
	                } catch (IOException e){
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            if (br != null)
	                try{
	                    br.close();
	                } catch (IOException e){
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	        }
	        Log.d("MYPROG", "CPU Name: FAIL");
	        return null;
	    }
	    
		public static int getNumCores() {
		    //Private Class to display only CPU devices in the directory listing
		    class CpuFilter implements FileFilter {
		        @Override
		        public boolean accept(File pathname) {
		            //Check if filename is "cpu", followed by a single digit number
		            if(Pattern.matches("cpu[0-9]", pathname.getName())) {
		                return true;
		            }
		            return false;
		        }      
		    }

		    try {
		        //Get directory containing CPU info
		        File dir = new File("/sys/devices/system/cpu/");
		        //Filter to only list the devices we care about
		        File[] files = dir.listFiles(new CpuFilter());
		        Log.d("MYPROG", "CPU Count: "+files.length);
		        //Return the number of cores (virtual CPU devices)
		        return files.length;
		    } catch(Exception e) {
		        //Print exception
		        Log.d("MYPROG", "CPU Count: Failed.");
		        e.printStackTrace();
		        //Default to return 1 core
		        return 1;
		    }
		}
	}
}
