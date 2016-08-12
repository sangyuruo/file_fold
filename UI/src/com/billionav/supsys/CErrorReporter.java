package com.billionav.supsys;

import java.io.File;

import com.billionav.jni.jniCErrorRegister;
import com.billionav.supsys.LogDumper;

import android.content.Context;
import android.util.Log;

public class CErrorReporter {
    public void init(Context context) {
		
		File file = context.getFileStreamPath("flag");
		jniCErrorRegister.registSignal(file.getAbsolutePath());
		
		//dump cpp crash log if last time application crashed	
		if (file.exists()){
			//report error
			LogDumper.Instance().dumpCCrash(context);
			
			//delete the file
			if(!file.delete()) {
				Log.e("Supsys","fail to delete "+file.getAbsolutePath());
			}
		}
		
    }
}
