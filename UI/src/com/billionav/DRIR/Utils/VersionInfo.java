package com.billionav.DRIR.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class VersionInfo {
	
	private static final String TAG = "fanghk";
	
	public static int getAplVersion(Context context){
		
		String aplVersion = "";
		PackageManager pkm = (PackageManager)context.getPackageManager();
		String pkmName = context.getPackageName();	

		try {
			aplVersion = pkm.getPackageInfo(pkmName, 0).versionName;//PackageManager.GET_RECEIVERS
			 
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("fanghk", "Java apl ver1:" + aplVersion);
		String[] x = aplVersion.split("\\.");
		
		String temp = "";
		for(int i = 0; i < x.length - 1; i++){
			temp += x[i];
		}

		Log.i("fanghk", "Java apl ver2:" + temp);
		int aplVer = Integer.valueOf(temp, 16);
		 
		return aplVer;
		

	}
}


