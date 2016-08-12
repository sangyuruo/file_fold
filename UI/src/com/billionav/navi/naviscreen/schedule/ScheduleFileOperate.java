package com.billionav.navi.naviscreen.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;

import com.billionav.jni.FileSystemJNI;

public class ScheduleFileOperate {

	
	private static ScheduleFileOperate instance;
	
	private static final String FILE_NAME = "Schedule.txt";
	private ScheduleFileOperate() {
	}
	
	public static ScheduleFileOperate getInstance() {
		if(null == instance) {
			instance = new ScheduleFileOperate();
		}
		return instance;
	}
	
	public void saveSchedule(String message) {
		writeToFile(message);
	}
	
	public String readSchedule() {
		return readFromFile();
	}
	
	public void deleteEmptyFile(){
		String path = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH) + "/"+ FILE_NAME;
		File f = new File(path);
		if (!f.exists()) {
			return;
		}else{
			Log.d("HybridUS", "delete file !");
			f.delete();
		}
	}
	
	private String readFromFile() {
		String path = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH) + "/"+ FILE_NAME;
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		FileReader reader = null;
		BufferedReader bfReader  = null;
		try{
			reader = new FileReader(f);
			bfReader = new BufferedReader(reader);
			String tmpStr = "";
			StringBuffer ret = new StringBuffer();
			while((tmpStr = bfReader.readLine()) != null) {
				ret.append(tmpStr + "\n");
			}
			return ret.toString();
		}catch (Exception e) {
			return null;
		}finally {
			if(null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if(null != bfReader) {
				try {
					bfReader.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	private void writeToFile(String message) {
		String path = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH) + "/"+ FILE_NAME;
		Log.d("test", "path = "+path);
		Log.d("test", "write msg = "+message);
		File f = new File(path);
		if (!f.exists()) {
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
//				e1.printStackTrace();
			}
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(f, false);
			writer.write(message);
			Log.d("test", "write OK!!!1");
		} catch (IOException e) {
			Log.d("test", "write error!!!1");
		} finally{
			if(null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
}
