package com.billionav.navi.versioncontrol.Response;


import android.util.Log;

import com.billionav.DRIR.jni.jniDRIR_CameraPreview;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;

public class VersionControl_ResponseGetCameraVer extends VersionControl_ResponseBase{

	public static String TAG = "[VersionControl]";
	
	public VersionControl_ResponseGetCameraVer(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse(){
		int iResCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			byte[] ret = getReceivebuf();
			if(null != ret && ret.length > 0) {
				String serverCamaVer = new String(ret);
				int[] iIsMatchCamType = new int[1];
				Log.d(TAG, "start checkoutCameraData");
				String cameraLocalVersion = jniDRIR_CameraPreview.GetCameraDataVersion(iIsMatchCamType);
				Log.d(TAG, "iIsMatchCamType = "+iIsMatchCamType[0]);
				Log.d(TAG, "cameraLocalVersion = ----" + cameraLocalVersion + "----");
				
				if(!cameraLocalVersion.equals(serverCamaVer)){
					VersionControl_ManagerIF.Instance().GetCameraInfos();
				} else {
					Log.d(TAG, "camera version is latest");
				}
				
			} else {
				Log.e(TAG, "ResponseGetCameraVer receive buff = null");
			}
			
			
			
		} else {
			Log.e(TAG, "ResponseGetCameraVer response error code :" + iResCode);
		}
	}
}
