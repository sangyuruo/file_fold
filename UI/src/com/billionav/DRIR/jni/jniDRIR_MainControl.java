package com.billionav.DRIR.jni;

public class jniDRIR_MainControl {
	
	///Lane detection on with voice
	public static final int DRIR_SETTING_LANEDETECT_ON_WITH_VOICE = 0;
	///Lane detection on without voice
	public static final int DRIR_SETTING_LANEDETECT_ON_WITHOUT_VOICE = 1;
	///Lane detection off
	public static final int DRIR_SETTING_LANEDETECT_OFF = 2;
	
	///Vehicle detection on with voice
	public static final int DRIR_SETTING_VEHICLEDETECT_ON_WITH_VOICE = 0;
	///Vehicle detection on without voice
	public static final int DRIR_SETTING_VEHICLEDETECT_ON_WITHOUT_VOICE = 1;
	///Vehicle detection off
	public static final int DRIR_SETTING_VEHICLEDETECT_OFF = 2;
	
	///Lane departure info on with voice
	public static final int DRIR_SETTING_LANEDEPARTUREINFO_ON_WITH_VOICE = 0;
	///Lane departure info on without voice
	public static final int DRIR_SETTING_LANEDEPARTUREINFO_ON_WITHOUT_VOICE = 1;
	///Lane departure info off
	public static final int DRIR_SETTING_LANEDEPARTUREINFO_OFF = 2;
	
	///Stop&Go assist before
	public static final int DRIR_SETTING_STOPGO_ASSIST_BEFORE = 0;
	///Stop&Go assist normal
	public static final int DRIR_SETTING_STOPGO_ASSIST_NORMAL = 1;
	///Stop&Go assist after
	public static final int DRIR_SETTING_STOPGO_ASSIST_AFTER = 2;
	///Stop&Go assist off
	public static final int DRIR_SETTING_STOPGO_ASSIST_OFF = 3;

	///Traffic light detection on with voice
	public static final int DRIR_SETTING_TLD_ON_WITH_VOICE = 0;
	///Traffic light detection on without voice
	public static final int DRIR_SETTING_TLD_ON_WITHOUT_VOICE = 1;
	///Traffic light detection off
	public static final int DRIR_SETTING_TLD_OFF = 2;
	
	///Speed Avoidance on with voice
	public static final int DRIR_SETTING_SPEEDAVOID_ON_WITH_VOICE = 0;
	///Speed Avoidance on without voice
	public static final int DRIR_SETTING_SPEEDAVOID_ON_WITHOUT_VOICE = 1;
	///Speed Avoidance off
	public static final int DRIR_SETTING_SPEEDAVOID_OFF = 2;
	
	///Distance info on with voice
	public static final int DRIR_SETTING_DISTANCEINFO_ON_WITH_VOICE = 0;
	///Distance info on without voice
	public static final int DRIR_SETTING_DISTANCEINFO_ON_WITHOUT_VOICE = 1;
	///Distance info off
	public static final int DRIR_SETTING_DISTANCEINFO_OFF = 2;

	///Camera height input from car
	public static final int DRIR_SETTING_CAMERAH_INPUT_CAR = 0;
	///Camera height input 
	public static final int DRIR_SETTING_CAMERAH_INPUT_GROUND = 1;
	
	///Demo mode off
	public static final int DRIR_SETTING_DEMOMODE_OFF = 0;
	///Demo mode on
	public static final int DRIR_SETTING_DEMOMODE_ON = 1;
	
	///Car info distance from car
	public static final int DRIR_SETTING_CARINFO_DISFROMCAR = 0;
	///Car info time gap from car
	public static final int DRIR_SETTING_CARINFO_TIMEFROMCAR = 1;
	///Car info car speed
	public static final int DRIR_SETTING_CARINFO_CARSPEED = 2;
	
	///Speed limit default value
	public static final int DRIR_SETTING_SPEEDLIMIT_DEFAULT = 10;
	
	///Car(yellow)crash alert time default value
	public static final float DRIR_SETTING_CARCRASH_YELLOW_DEFAULT = (float) 2.5;
	
	///Car(red)crash alert time default value
	public static final float DRIR_SETTING_CARCRASH_RED_DEFAULT = (float) 1.5;
	
	//IR setting ID
	public static final int DRIR_SETTING_IRID_ALTERLEVEL = 100;
    public static final int DRIR_SETTING_IRID_GSENSORADJUST = 101;
    public static final int DRIR_SETTING_IRID_DEMOMODE = 102;
    public static final int DRIR_SETTING_IRID_FRONTCARINFO = 103;
    public static final int DRIR_SETTING_IRID_CRASHTIMEALARM = 104;
    public static final int DRIR_SETTING_IRID_CARMODEL = 105;
    public static final int DRIR_SETTING_IRID_SPEEDLIMIT = 106;
    
    //Waring sens
    public static final int DRIR_SETTING_IR_ALTERLEVEL_HIGH = 0;
    public static final int DRIR_SETTING_IR_ALTERLEVEL_MIDDLE = 1;
    public static final int DRIR_SETTING_IR_ALTERLEVEL_LOW = 2;
    
    //Perspective adjust
    public static final int DRIR_SETTING_IR_GSENSORADJUST_ON = 1;
    public static final int DRIR_SETTING_IR_GSENSORADJUST_OFF = 0;
    
    //Experience mode
    public static final int DRIR_SETTING_IR_DEMOMODE_ON = 1;
    public static final int DRIR_SETTING_IR_DEMOMODE_OFF = 0;
    
    //Front car safe display
    public static final int DRIR_SETTING_IR_FRONTCARINFO_CAR = 0;
    public static final int DRIR_SETTING_IR_FRONTCARINFO_TIME = 1;
    
    //Crash time warning
    public static final int DRIR_SETTING_IR_CRASHTIMEALARM_LEVEL1 = 1;
    public static final int DRIR_SETTING_IR_CRASHTIMEALARM_LEVEL2 = 2;
    public static final int DRIR_SETTING_IR_CRASHTIMEALARM_LEVEL3 = 3;
    public static final int DRIR_SETTING_IR_CRASHTIMEALARM_LEVEL4 = 4;
    public static final int DRIR_SETTING_IR_CRASHTIMEALARM_LEVEL5 = 5;
    public static final int DRIR_SETTING_IR_CRASHTIMEALARM_DEFAULT = 1;
    
    //Car module
    public static final int DRIR_SETTING_IR_CARMODE_NORMAL = 0; 
    public static final int DRIR_SETTING_IR_CARMODE_SUV = 1;
    
    //Speed limit
    public static final int DRIR_SETTING_IR_SPEEDLIMIT_LOW = 10;
    public static final int DRIR_SETTING_IR_SPEEDLIMIT_MIDDLE = 20;
    public static final int DRIR_SETTING_IR_SPEEDLIMIT_HIGH = 30;
    
    public static native void SetIRSetting(int iSettingID, int iValue);
    public static native void SetIRSetting(int iSettingID, float fValue);
    
	///DR Setting ID 
	public static final int DRIR_SETTING_DRID_ONOFF = 1;			
	public static final int DRIR_SETTING_DRID_RECMODE = 2;		
	public static final int DRIR_SETTING_DRID_RECDQUALITY = 3;		
	public static final int DRIR_SETTING_DRID_GSNSLEVEL = 4;		
	public static final int DRIR_SETTING_DRID_SDRECMODE = 5;		
	public static final int DRIR_SETTING_DRID_GSNSFUC = 6;			
	public static final int DRIR_SETTING_DRID_SDCAPACITY = 7;		
	public static final int DRIR_SETTING_DRID_LOG = 8;				
	public static final int DRIR_SETTING_DRID_UPLOADFUC = 9;		
	public static final int DRIR_SETTING_DRID_FRAMERATE = 10;
	
	public static final int DRIR_SETTING_DR_ON = 1;
	public static final int DRIR_SETTING_DR_OFF = 0;
	public static final int DRIR_SETTING_DR_FUC_DEFAULT = DRIR_SETTING_DR_OFF;
	
	public static final int DRIR_SETTING_DR_RECMODE_HQ = 0;
	public static final int DRIR_SETTING_DR_RECMODE_STD = 1;
	public static final int DRIR_SETTING_DR_RECMODE_LT = 2;
	public static final int DRIR_SETTING_DR_RECMODE_DEFAULT = DRIR_SETTING_DR_RECMODE_LT;
	
	public static final int DRIR_SETTING_DR_RECQUALITY_HIGH = 0;
	public static final int DRIR_SETTING_DR_RECQUALITY_MIDDLE = 1;
	public static final int DRIR_SETTING_DR_RECQUALITY_LOW = 2;
	
	public static final int DRIR_SETTING_DR_GSNSLEVEL_HIGH = 0;
	public static final int DRIR_SETTING_DR_GSNSLEVEL_MIDDLE = 1;
	public static final int DRIR_SETTING_DR_GSNSLEVEL_LOW = 2;
	
	public static final int DRIR_SETTING_DR_SDRECMODE_OVERWRITE = 0;
	public static final int DRIR_SETTING_DR_SDRECMODE_STOP = 1;
	
	public static final int DRIR_SETTING_DR_GSNSFUC_ON = 1;
	public static final int DRIR_SETTING_DR_GSNSFUC_OFF = 0;
	
	public static final int DRIR_SETTING_DR_SDCAP1 = 1024;
	public static final int DRIR_SETTING_DR_SDCAP2 = 2048;
	public static final int DRIR_SETTING_DR_SDCAP3 = 3072;
	public static final int DRIR_SETTING_DR_SDCAP4 = 4096;
	public static final int DRIR_SETTING_DR_MAX_SDCAPACITY = -1;
	
	public static final int DRIR_SETTING_DR_LOG_ON = 1;
	public static final int DRIR_SETTING_DR_LOG_OFF = 0;
	
	public static final int DRIR_SETTING_DR_UPLOAD_ON = 1;
	public static final int DRIR_SETTING_DR_UPLOAD_OFF = 0;
	
	public static final int DRIR_SETTING_DR_FRAME_5 = 0;
	public static final int DRIR_SETTING_DR_FRAME_10 = 1;
	public static final int DRIR_SETTING_DR_FRAME_15 = 2;
	
	public static native int DRIRPause();
	public static native int DRIRResume();
	
	public static native void SetLaneDetectStatus(int iStatus);
	public static native int GetLaneDetectStatus();
	
	public static native void SetVehicleDetectStatus(int iStatus);
	public static native int GetVehicleDetectStatus();
	
	public static native void SetLaneDepartInfoStatus(int iStatus);
	public static native int GetLaneDepartInfoStatus();
	
	public static native void SetStopGoStatus(int iStatus);
	public static native int GetStopGoStatus();
	
	public static native void SetTLDStatus(int iStatus);
	public static native int GetTLDStatus();
	
	public static native void SetSpeedAvoidStatus(int iStatus);
	public static native int GetSpeedAvoidStatus();
	
	public static native void SetDistanceInfoStatus(int iStatus);
	public static native int GetDistanceInfoStatus();
	
	public static native void SetCameraHInputStatus(int iStatus);
	public static native int GetCameraHInputStatus();
	
	public static native void SetDemoMode(int iStatus);
	public static native int GetDemoMode();
	
	public static native void SetSpeedLimit(int iSpeedLimit);
	public static native int GetSpeedLimit();
	
	public static native void SetYellowAlterTime(float fAlterTime);
	public static native float GetYellowAlterTime();
	
	public static native void SetRedAlterTime(float fAlterTime);
	public static native float GetRedAlterTime();
	
	public static native void SetCarInfoStatus(int iStatus);
	public static native int GetCarInfoStatus();
	
	public static native void SetDRSetting(int iSettingID, int iValue);
	
	/// IR debug setting ID
	public static final int DRIR_DEBUG_SETTING_IRID_DEMORUNSPPED = 200;
	public static final int DRIR_DEBUG_SETTING_IRID_ROADSDET = 201;
	public static final int DRIR_DEBUG_SETTING_IRID_LDW = 202;
	public static final int DRIR_DEBUG_SETTING_IRID_VIRTUALLANE = 203;
	public static final int DRIR_DEBUG_SETTING_IRID_RESERVE1 = 204;
	public static final int DRIR_DEBUG_SETTING_IRID_RESERVE2 = 205;
	public static final int DRIR_DEBUG_SETTING_IRID_RESERVE3 = 206;
	public static final int DRIR_DEBUG_SETTING_IRID_RESERVE4 = 207;
	public static final int DRIR_DEBUG_SETTING_IRID_RESERVE5 = 208;
	
	public static final int DRIR_DEBUG_SETTING_IR_DEMORUNSPEED_DEFAULT = -1;
	public static final int DRIR_DEBUG_SETTING_IR_ROADSDET_ON = 1;			//Road sign detect on
	public static final int DRIR_DEBUG_SETTING_IR_ROADSDET_OFF = 0;			//Road sign detect off
	public static final int DRIR_DEBUG_SETTING_IR_LDW_DEFAULT = 0;
	public static final int DRIR_DEBUG_SETTING_IR_RESERVE1_ON = 1;
	public static final int DRIR_DEBUG_SETTING_IR_RESERVE1_OFF = 0;
	public static final int DRIR_DEBUG_SETTING_IR_RESERVE2_ON = 1;
	public static final int DRIR_DEBUG_SETTING_IR_RESERVE2_OFF = 0;
	public static final int DRIR_DEBUG_SETTING_IR_RESERVE3_DEFAULT = 50;
	public static final int DRIR_DEBUG_SETTING_IR_RESERVE4_DEFAULT = 50;
	public static final int DRIR_DEBUG_SETTING_IR_RESERVE5_DEFAULT = 50;
	public static native void SetIRDebugSetting(int iSettingID, int iValue);
	
	public static final int DRIR_DEBUG_SETTING_DRID_ONOFF = 300;					
	public static final int DRIR_DEBUG_SETTING_DRID_RECDQUALITY = 301;				
	public static final int DRIR_DEBUG_SETTING_DRID_GSNSFUC = 302;				
	public static final int DRIR_DEBUG_SETTING_DRID_LOG = 303;						
	public static final int DRIR_DEBUG_SETTING_DRID_FRAMERATE = 304;
	public static final int DRIR_DEBUG_SETTING_DRID_RECORDCALIBRATE = 305;
	public static final int DRIR_DEBUG_SETTING_DRID_GSNSLEVEL = 306;
	
	public static final int DRIR_DEBUG_SETTING_DR_ON = 1;
	public static final int DRIR_DEBUG_SETTING_DR_OFF = 0;
	
	public static final int DRIR_DEBUG_SETTING_DR_RECQUALITY_HIGH = 0;
	public static final int DRIR_DEBUG_SETTING_DR_RECQUALITY_MIDDLE = 1;
	public static final int DRIR_DEBUG_SETTING_DR_RECQUALITY_LOW = 2;
		
	public static final int DRIR_DEBUG_SETTING_DR_GSNSFUC_ON = 1;
	public static final int DRIR_DEBUG_SETTING_DR_GSNSFUC_OFF = 0;
	
	public static final int DRIR_DEBUG_SETTING_DR_LOG_ON = 1;
	public static final int DRIR_DEBUG_SETTING_DR_LOG_OFF = 0;
	
	public static final int DRIR_DEBUG_SETTING_DR_FRAME_5 = 0;
	public static final int DRIR_DEBUG_SETTING_DR_FRAME_10 = 1;
	public static final int DRIR_DEBUG_SETTING_DR_FRAME_15 = 2;
	
	public static final int DRIR_DEBUG_SETTING_DR_RECCALB_OFF = 0;
	public static final int DRIR_DEBUG_SETTING_DR_RECCALB_ON = 1;
	
	public static final int DRIR_DEBUG_SETTING_DR_GSNSLEVEL1 = 0;
	public static final int DRIR_DEBUG_SETTING_DR_GSNSLEVEL2 = 1;
	public static final int DRIR_DEBUG_SETTING_DR_GSNSLEVEL3 = 2;
	public static final int DRIR_DEBUG_SETTING_DR_GSNSLEVEL4 = 3;
	public static final int DRIR_DEBUG_SETTING_DR_GSNSLEVEL5 = 4;
	public static final int DRIR_DEBUG_SETTING_DR_GSNSLEVEL_DEFAULT = 2;
	
	public static native void SetDRDebugSetting(int iSettingID, int iValue);
	
	public static native void SettingCommit();
	
	// DR & DM start/stop control
	public static native void LongPush();
	public static native void ShortPush();
	
	public static native void DRIRModuleStart();
	public static native void DRIRModuleStop();
	public static native void DRIRBG2FG();
	public static native void DRIRFG2BG();
	
	/// Get DM preview file path
	public static native String DRIRGetDMFilePath();
	/// Get DR preview file path
	public static native String DRIRGetDRFilePath();
	/// Get picture file path
	public static native String DRIRGetPicFilePath();
	/// Get Drir root path
	public static native String DRIRGetDrirRootPath();
	
	public static native boolean IsDMSave();
	
	/// Notify the path and name of the picture file 
	public static native void DRIRNotifyPicFilePath(String sPath, int iSize, int iPicType);
}
