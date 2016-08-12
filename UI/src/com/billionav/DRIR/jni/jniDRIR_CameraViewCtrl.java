package com.billionav.DRIR.jni;

import com.billionav.navi.camera.CameraView;

public class jniDRIR_CameraViewCtrl {
	public static native void	J2CAttach();
	public static native void	J2CDetach();
	public native static void onSurfaceChange();
	public native static void onSurfaceDestory();
	public native static void SetMaxMode();
	public native static void SetMinMode(boolean bIsPreScreen);
	public native static void SetHQMode();
	public native static void SetLQMode();
	public native static void SetQualityMode(int iMode);
	public native static void SetPreviewStatus(int iStatus);
	public native static void NotifyHQStartResult(boolean bResult);
	public native static void MoveToBack();
	public native static void MoveToFront();
	public native static void PlayShootSound();
	public native static long GetCradleStatus();
	public native static void StartPreviewNative();
	
	public static boolean StartPreview()
	{
		return CameraView.Instance().StartPreview();
	}
	
	public static boolean StopPreview(int iFlag)
	{
		return CameraView.Instance().StopPreview(iFlag);
	}
	
	public static boolean StartHQRec()
	{
		return CameraView.Instance().StartHQRec();
	}
	
	public static boolean StopHQRec()
	{
		return CameraView.Instance().StopHQRec();
	}
	
	public static boolean SetCameraParam()
	{
		CameraView.Instance().setCameraParam();
		
		return true;
	}
	
	public static boolean setExposure(){
		CameraView.Instance().setExposure();		
		return true;		
	}
	
	public static boolean setMinStatus(boolean bIsMin)
	{
		CameraView.Instance().setMinStatus(bIsMin);
		return true;
	}
}
