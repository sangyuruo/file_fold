package com.billionav.navi.naviscreen.mef;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.billionav.jni.*;
import com.billionav.navi.app.ext.log.NaviLogUtil;

public class EglRenderer implements EglView.Renderer {
	private static EglRenderer msInstance = null;

	private static boolean bNativeInited = false;
	
    private EglRenderer()
    {
    }
    
    public static synchronized EglView.Renderer getInstance() {
    	if (null == msInstance) {
    		msInstance = new EglRenderer();
    	}
    	
    	return msInstance;
    }

    public boolean onDrawFrame(GL10 gl, int nativerunmode) {
    	if (!bNativeInited) {
    		return false;
    	}
    	NaviLogUtil.debugEglStep("onDrawFrame");
    	return MapEngineJNI.render(nativerunmode);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	MapEngineJNI.setSurfaceSize(width, height);
    	MapEngineJNI.ForceUpdate();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	if (!bNativeInited) {
    		MapEngineJNI.initRenderer();
    		bNativeInited = true;
    	}
    	MapEngineJNI.ForceUpdate();
    }

    public void onSurfaceDestroyed(GL10 gl)
    {
    	//MapEngineJNI.releaseRenderResource();
    }
}
