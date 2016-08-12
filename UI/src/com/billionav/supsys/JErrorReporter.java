/**
 * 
 */
package com.billionav.supsys;

//import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
import android.util.Log;

/**
 * @author zhuangyao
 *
 */
public class JErrorReporter implements UncaughtExceptionHandler {
	private Context mcontext=null;
	
	private UncaughtExceptionHandler mDefaultExceptionHandler;
	/**
	 * 
	 */
	public JErrorReporter() {
		// TODO Auto-generated constructor stub
	}

    public void init(Context context) {
    	if (mDefaultExceptionHandler==null) {
    		mDefaultExceptionHandler=Thread.getDefaultUncaughtExceptionHandler();
    		Thread.setDefaultUncaughtExceptionHandler(this);
    		//for application information
    		mcontext=context;
    	}
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub	
		LogDumper.Instance().dumpJCrash(mcontext, thread, ex);
		
		//return the power to the default handler 
		mDefaultExceptionHandler.uncaughtException(thread, ex);
	}

}
