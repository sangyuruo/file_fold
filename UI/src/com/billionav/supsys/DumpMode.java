package com.billionav.supsys;

import java.io.File;

import android.os.Environment;


public class DumpMode {

	private DumpMode() {
		// TODO Auto-generated constructor stub
	}
	
	public synchronized static boolean isDumpModeOn() {
		//jniConfigManager.Instance().isDumpModeOn();
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), FILE_NAME);
			return file.exists();
		}
		return false;
	}
	
	//after this setting application must restart
	public synchronized static void setDumpMode(boolean g ) {
//		jniConfigManager.Instance().setDumpModestate(g);
	}

	private static final String FILE_NAME = "billionav.supsys.dumpmodeon";
}
