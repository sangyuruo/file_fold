package com.billionav.navi.versioncontrol.Response;

import java.io.File;
import java.io.FileOutputStream;

import android.util.Log;

import com.billionav.navi.net.PResponse;
import com.billionav.navi.update.CameraUpdate;

public class VersionControl_ResponseGetCameraPara extends VersionControl_ResponseBase{

	public VersionControl_ResponseGetCameraPara(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse(){
		int iResCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			Log.d(CameraUpdate.TAG, "filePath : " + CameraUpdate.filePath);
			Log.d(CameraUpdate.TAG, "-------------------------------find file before-------------------------------");
			File bdir = new File(CameraUpdate.filePath);
			File[] files = bdir.listFiles();
			for(int i=0;i<files.length;i++){
				Log.d(CameraUpdate.TAG, (files[i].isDirectory()?"directory:":"file:")+files[i].getName());
			}
			
			
			File file = new File(CameraUpdate.tempFilePath);
			FileOutputStream outputStream;
			try {
				outputStream = new FileOutputStream(file);
				outputStream.write(getReceivebuf());
				outputStream.close();
				Log.d(CameraUpdate.TAG, "Save temp file Success !!");
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			Log.d(CameraUpdate.TAG, "-------------------------------find file after-------------------------------");
			File dir = new File(CameraUpdate.filePath);
			File[] afiles = dir.listFiles();
			for(int i=0;i<afiles.length;i++){
				Log.d(CameraUpdate.TAG, (afiles[i].isDirectory()?"directory:":"file:")+afiles[i].getName());
			}
			
		}
	}
}
