package com.billionav.jni;

public class jniGpsSnsModule {
	
	private static jniGpsSnsModule instance = new jniGpsSnsModule();

	public static jniGpsSnsModule getGpsSnsModule() {return instance;}
	
	private jniGpsSnsModule() {}

	// sensor mode status bits
	public static final byte SNS_MODE_STATUS_BIT_GYRO 	= 0x01;
	public static final byte SNS_MODE_STATUS_BIT_GSNS 	= 0x02;
	public static final byte SNS_MODE_STATUS_BIT_TSNS 	= 0x04;
	public static final byte SNS_MODE_STATUS_BIT_PSNS 	= 0x08;
	public static final byte SNS_MODE_STATUS_BIT_PULSE 	= 0x10;
	public static final byte SNS_MODE_STATUS_BIT_BACK 	= 0x20;

	// sensor stop state
	public static final byte SNS_STOP_STATE_MOVING 		= 0;
	public static final byte SNS_STOP_STATE_STOPPING 	= 1;
	public static final byte SNS_STOP_STATE_UNKNOWN 	= 2;

	// set save raw sensor log
	public native void SetSaveRawSensorLog(boolean bSetOn);

	// get save raw sensor log
	public native boolean GetSaveRawSensorLog();

	// set save sensor log
	public native void SetCardSnsLog(boolean bSetOn);

	// get save sensor log
	public native boolean GetCardSnsLog();
	
	// start sensor log play
	public native void StartSnsLogPlay();

	// stop sensor log play
	public native void StopSnsLogPlay();

	// get sensor log play state
	public native boolean GetSnsLogState();

	// set sensor mode
	public native void SetSnsValid(byte bySnsBit, boolean bValid);

	// set sensor function
	public native void SetSnsFunction(boolean bSetOn);

	// get sensor function
	public native boolean GetSnsFunction();

	// set sensor function
	public native void SetStopStateWatch(boolean bSetOn);

	// get sensor function
	public native boolean GetStopStateWatch();

	// get sensor function
	public native byte GetSnsStopState();
	
	// get sensor function
	public native boolean GetGyroOKStatus();
	
	// get sensor function
	public native boolean GetGsnsOKStatus();
	
	// set cradle connect status
	public native void SetSnsCradleConnectStatus(boolean bStatus);
}
