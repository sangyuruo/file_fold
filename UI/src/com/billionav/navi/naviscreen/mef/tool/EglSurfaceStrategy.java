package com.billionav.navi.naviscreen.mef.tool;

import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;

import android.view.Surface;
import android.view.SurfaceHolder;

/**
 * @author sangjun
 */
public interface EglSurfaceStrategy {
	public void init();

	public void destroySurface(EGLSurface surface);

	public EGLSurface createSurface(SurfaceHolder holder);

	public EGLSurface createSurface(Surface surface);
	
	public EGLSurface createSurface(Surface drawSurface,Surface readSurface);

	public void finish();

	public GL bindSurface(EGLSurface drawSurface,EGLSurface readSurface);
	public GL bindSurface(EGLSurface surface);

	public boolean swap();

	public void finalize();
	
	public void setPresentationTime(long nsecs);
}
