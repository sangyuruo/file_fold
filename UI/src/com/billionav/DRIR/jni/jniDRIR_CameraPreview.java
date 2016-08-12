package com.billionav.DRIR.jni;

public class jniDRIR_CameraPreview {
	public static final int DRIR_CTL_FOCUS_MODE_AUTO = 0;
	public static final int DRIR_CTL_FOCUS_MODE_INFINITY = 1;
	public static final int DRIR_CTL_FOCUS_MODE_MACRO = 2;
	public static final int DRIR_CTL_FOCUS_MODE_FIXED = 3;
	public static final int DRIR_CTL_FOCUS_MODE_EDOF = 4;
	public static final int DRIR_CTL_FOCUS_MODE_CONTINUOUS_VIDEO = 5;

	public static final int DRIR_CTL_WHITE_BALANCE_AUTO = 0;
	public static final int DRIR_CTL_WHITE_BALANCE_INCANDESCENT = 1;
	public static final int DRIR_CTL_WHITE_BALANCE_FLUORESCENT = 2;
	public static final int DRIR_CTL_WHITE_BALANCE_WARM_FLUORESCENT = 3;
	public static final int DRIR_CTL_WHITE_BALANCE_DAYLIGHT = 4;
	public static final int DRIR_CTL_WHITE_BALANCE_CLOUDY_DAYLIGHT =5;
	public static final int DRIR_CTL_WHITE_BALANCE_TWILIGHT = 6;
	public static final int DRIR_CTL_WHITE_BALANCE_SHADE = 7;
	
	public static final int DRIR_CTL_PIXEL_FORMAT_RGB = 0;
	
	public static native void CaptureData(byte[] buff, int iDatasz);

	public static native void NotifyDataUpdating();
	
	public static native void Changed();
	
	public static native void SetPreviewSize(int iwidth,int iHeight);
	public static native void GetPreviewSize(int[] iSize,int iDataSiz);
	public static native void SetWhiteBalance(String sWhiteBalance,int iSize);
	public static native String  GetWhiteBalance();

	public static native void   SetFocusMode(String sFocusMode,int iSize);
	public static native String GetFocusMode();

	public static native void SetPixelFormat(int iPixelFormat);
	public static native int  GetPixelFormat();

	public static native int  GetCaptureFrameRate();
	public static native void SetCaptureFrameRate(int iCaptureFrameRate);

	public static native int  GetPreviewFrameRate();
	public static native void SetPreviewFrameRate(int ifps);

	public static native void SetMaxExposureCompensation(int iMaxExposure);
	public static native void SetMinExposureCompensation(int iMinExposure);
	public static native int  GetExposureCompensation();
	public static native void SetExposureCompensation(int iExposure);
	public static native String GetCameraDataVersion(int[] iIsMatchCamType);
}
