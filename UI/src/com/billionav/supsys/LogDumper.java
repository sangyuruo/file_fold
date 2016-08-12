/**
 * 
 */
package com.billionav.supsys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;

/**
 * @author zhuangyao
 *
 */
public class LogDumper {
	
	private String appName=null;
	
	private static LogDumper instance=new LogDumper();
	/**
	 * 
	 */
	public static LogDumper Instance() {
		return instance;
	}
	
	private LogDumper(){
		
	}
	
	
	/**
	 * @return the appName
	 */
	private final String setAppName(Context context) {
		appName=context.getPackageName();
		return appName;
	}
	
	public void dumpFreezeState(Context context) {
        String[] prog = { "dumpmesg" };
		String logcatOutput = Logcatdump(prog);
		saveLog(context, logcatOutput); 
	}

	public void dumpJCrash(Context context, Thread t, Throwable e) {
		StringBuilder dumplog=new StringBuilder();
		
        String shortResult=Log.getStackTraceString(e);
		dumplog.append(shortResult);
		
        Throwable cause = e.getCause();
        while (cause != null) {
            cause = cause.getCause();
            shortResult=Log.getStackTraceString(cause);
            dumplog.append(shortResult);
        }
        saveLog(context,dumplog.toString());
        
        String[] prog = { "logcat", "-v", "time","-d", "*:V" };
		String logcatOutput = Logcatdump(prog);
		saveLog(context, logcatOutput);   
	}
	
	public void dumpCCrash(Context context) {
		String[] prog = { "logcat", "-v", "time","-d", "*:V" };
		String logcatOutput = Logcatdump(prog);
		
		saveLog(context, logcatOutput);
	}
	
	private String Logcatdump(final String[] prog) {
		StringBuilder dumplog=new StringBuilder();
		
		Process logcatProc=null;
		BufferedReader mReader =null;
		try {
			logcatProc = Runtime.getRuntime().exec(prog);
			mReader = new BufferedReader(new InputStreamReader(
					logcatProc.getInputStream()), 1024);
			
			String line;
			while ((line = mReader.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				dumplog.append(line+"\n");
			}
		}
		catch (IOException ioe){
			Log.e("LogDumper",Log.getStackTraceString(ioe));
		}
		catch (SecurityException se){
			Log.e("LogDumper",Log.getStackTraceString(se));
		}finally{
			if (logcatProc!=null){
				logcatProc.destroy();
				logcatProc=null;
			}
			try {
				mReader.close();
			}
			catch (IOException ioe){
				Log.e("LogDumper",Log.getStackTraceString(ioe));
			}
		}
		
		return dumplog.toString();
	}
	
	private void saveLog(Context context,String log){
        setAppName(context);
    	final SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss.SSSZ");
    	String strpath=String.format("/sdcard/%s", appName);
    	final File path = new File(strpath);
		final File file = new File(path + "/"+appName+"."
				+ LOG_FILE_FORMAT.format(new Date()) + ".txt");

		if (!path.exists()) {
			if(!path.mkdirs()) {
				Log.e("LogDumper","fail to create error record folder,maybe sdcard not exist");
				return;
			}
		}

		BufferedWriter bw = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			bw = new BufferedWriter(new FileWriter(file), 1024);
			//file maybe already exist so append the content
			bw.append(log);
		} catch (IOException ioe) {
			Log.e("LogDumper", "error saving log", ioe);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe) {
					Log.e("LogDumper", "error closing log", ioe);
				}
			}
		}
	}
	
//	private void sendmail(final Context context,final String con) {
//		new Thread(new Runnable() {
//			public void run() {
//				String content = con;
//
//				Intent emailIntent = new Intent(
//						android.content.Intent.ACTION_SEND);
//				emailIntent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK|android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK );
//				// emailIntent.setType(mPrefs.isEmailHtml() ? "text/html"
//				// : "text/plain");
//				emailIntent.setType("message/rfc822");
//				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
//						"Android Log: " +(new Date()));
//				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
//						true ? Html.fromHtml(content) : content);
//				context.startActivity(Intent.createChooser(emailIntent, "Send log ..."));
//			}
//		}).start();
//	}
}
