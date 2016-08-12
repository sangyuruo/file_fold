package com.billionav.navi.system;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.util.Log;

public class PLog {

	private PLog() {
		// TODO Auto-generated constructor stub
	}
	
	private static final String LOGFLAGFILE = "/sdcard/logflag";
	private static AtomicBoolean m_bLogout = new AtomicBoolean(false);
	
	public synchronized static boolean initLogOutputStatus() {
		//flag file exist true else false
		File f = new File(LOGFLAGFILE);
		if (f.exists()) {
			m_bLogout.set(true);
		} else {
			m_bLogout.set(false);
		}
		return m_bLogout.get();
	}
	
	public synchronized static void setLogOutputStatus(boolean g ) {
		File f = new File(LOGFLAGFILE);
		if (g) {
			//write to a flag file 
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//if flag file already exist
			//delete it
			if (f.exists()) {
				f.delete();
			}
		}
		m_bLogout.set(g);
	}
	
	public synchronized static boolean getLogOutputStatus() {
		return m_bLogout.get();
	}
	
	
	//Send a DEBUG log message.
	public static int	 d(String tag, String msg){
		if (m_bLogout.get()) {
			return Log.d(tag,msg);
		}
		return -1;
	}
	
	//Send an ERROR log message.
	public static int	 e(String tag, String msg) {
		if (m_bLogout.get()) { 
			return Log.e(tag, msg);
		}
		return -1;
	}

	//Send an INFO log message.
	public static int	 i(String tag, String msg){
		if (m_bLogout.get()) { 
			return Log.i(tag, msg);
		}
		return -1;
	}

	//Send a VERBOSE log message.
	public static int	 v(String tag, String msg) {
		if (m_bLogout.get()) { 
			return Log.v(tag, msg);
		}
		return -1;
	}

	//Send a WARN log message.
	public static int	 w(String tag, String msg) {
		if (m_bLogout.get()) {
			return Log.w(tag, msg);
		}
		return -1;
	}
	
}
