package com.billionav.jni;


public class UILocationControlJNI {
	
	
	//another road result selection according to "another road proto"
	public static final int LDM_ANOTHER_ROAD_FAIL_FAIL_INVALID = 1;
	public static final int LDM_ANOTHER_ROAD_FAIL_UNKNOWN = 2;
	public static final int LDM_ANOTHER_ROAD_FAIL_TOLL = 3;
	public static final int LDM_ANOTHER_ROAD_FAIL_NORMAL = 4;
	public static final int LDM_ANOTHER_ROAD_SUCCESS_TOLL = 5;
	public static final int LDM_ANOTHER_ROAD_SUCCESS_NORMAL = 6;
	
	
	private static final UILocationControlJNI instance = new UILocationControlJNI();
	
	private UILocationControlJNI() {
	}
	
	public static UILocationControlJNI getInstance() {
		return instance;
	}
	
    public native int GetGpsDimension();
	
	public native boolean SetPosition(int iSetOpt, long lSetLon, long lSetLat, int iSetDir);
	
	public native boolean StartLogRun(int logRunMedia, String strFileName);
	
	public native boolean StopLogRun();
	
	public native boolean StartLogging(int loggingMedia);
	
	public native boolean StopLogging();
	
	public native boolean StartAnotherRoad();
	
	public native boolean StartLog(byte byLogType);
	
	public native void StopLog();
	
	public native byte GetGpsLogCondition ();
	
	public native boolean StartLogRunning();
	
	public native void StopLogRunning();
	
	public native boolean GetLogRunningStatus();
	
	public native int GetTripMeter();
	
	public native int GetHeading();
	
	public native int GetVelocity();
	
	public native int GetDataOrder();
	
	public native boolean GetRegionDataExist();
	
	public native boolean GetTollRoad();
	
	public native boolean GetPathShiftProhibition();
	
	public native short GetDispClass();
	
	public native short GetLinkRowNum();
	
	public native short	GetStartNodeNum();
	
	public native short	GetEndNodeNum();
	
	public native short	GetLinkAttribute();
	
	public native short	GetLinkOffset();
	
	public native boolean GetOnRoad();
	
	
	//get LocLinkID_SLinkID
	private native char[] GetLocSLinkID();
	
	//Get LocLinkID_ELinkID
	private native char GetLocELinkID();
	
	//Get Car's position (native interface)
	public static native long[] GetPosition(int iReqType);
	
	
	// Get car position OK or not
	public native boolean GetPositionOk();
	
	/*------------------------------------------------------*/
	// Cradle
	public native void SetShowCradleDebugInfo(boolean bShowCradleDebugInfo);
	
	public static native void setDemoSpeed(int state);

//	//Start Core Log that recorded by CLocationListener
//	public boolean StartCoreLocationLog()
//	{
//	}
//	
//	//Stop Core Log that recorded by CLocationListener
//	public boolean  StopCoreLocationLog()
//	{
//	}
//  
//	//get log status;
//    public boolean GetCoreLocationLogStatus()
//    {
//    }
//	
//	//show GPS information view
//	public boolean ShowGpsView()
//	{
//	}
//	
//	//close GPS information view
//	public boolean CloseGpsView()
//	{
//	}
//	
//	public boolean GetGpsViewFlag()
//	{
//	}
}
