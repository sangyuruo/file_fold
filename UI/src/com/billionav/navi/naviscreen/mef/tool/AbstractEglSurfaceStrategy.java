package com.billionav.navi.naviscreen.mef.tool;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;

import android.opengl.EGL14;
import android.opengl.EGLExt;
import android.view.Surface;

import com.billionav.navi.app.ext.log.NaviLogUtil;
import com.billionav.navi.naviscreen.mef.EglHelper;

/**
 * @author sangjun
 */
public abstract class AbstractEglSurfaceStrategy implements EglSurfaceStrategy {
	static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

	EGL10 mEgl;
	EGLDisplay mEglDisplay;
	EGLSurface mEglSurface;
	EGLConfig mEglConfig;
	EGLContext mEglContext;

	static EglHelper msInstance = null;

	public void setPresentationTime(long nsecs) {
        EGLExt.eglPresentationTimeANDROID(EGL14.eglGetCurrentDisplay(), EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW), nsecs);
    }
	
	public void finish() {
		if (mEglContext != null) {
			mEgl.eglDestroyContext(mEglDisplay, mEglContext);
			mEglContext = null;
		}
		if (mEglDisplay != null) {
			mEgl.eglTerminate(mEglDisplay);
			mEglDisplay = null;
		}
	}
	
	public void destroySurface(EGLSurface surface) {
		if (mEglSurface == surface) {
			mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
					EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
		}

		if (surface != null && surface != EGL10.EGL_NO_SURFACE) {
			mEgl.eglDestroySurface(mEglDisplay, surface);
		}
	}

	public GL bindSurface(EGLSurface drawSurface,EGLSurface readSurface) {
		if (drawSurface != mEglSurface) {
			/*
			 * Before we can issue GL commands, we need to make sure the context
			 * is current and bound to a surface.
			 */
			if (!mEgl
					.eglMakeCurrent(mEglDisplay, drawSurface, readSurface, mEglContext)) {
				throw new RuntimeException("eglMakeCurrent failed.");
			}
			mEglSurface = drawSurface;
		}
		return mEglContext.getGL();
	}
	
	public GL bindSurface(EGLSurface surface) {
		if (surface != mEglSurface) {
			/*
			 * Before we can issue GL commands, we need to make sure the context
			 * is current and bound to a surface.
			 */
			if (!mEgl
					.eglMakeCurrent(mEglDisplay, surface, surface, mEglContext)) {
				throw new RuntimeException("eglMakeCurrent failed.");
			}
			mEglSurface = surface;
		}
		return mEglContext.getGL();
	}
	
	/**
	 * Display the current render surface.
	 * 
	 * @return false if the context has been lost.
	 */
	public boolean swap() {
		// mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);
		
		if (!mEgl.eglSwapBuffers(mEglDisplay, mEglSurface)) {
			int error = mEgl.eglGetError();
			switch (error) {
			case EGL11.EGL_CONTEXT_LOST:
				NaviLogUtil.error("EglHelper",
						"eglSwapBuffers returned EGL_CONTEXT_LOST. error="
								+ error);
				return false;
			case EGL10.EGL_BAD_NATIVE_WINDOW:
				NaviLogUtil.error("EglHelper",
						"eglSwapBuffers returned EGL_BAD_NATIVE_WINDOW. tid="
								+ Thread.currentThread().getId());
				return false;
			case EGL10.EGL_BAD_SURFACE:
				NaviLogUtil.error("EglHelper",
						"eglSwapBuffers returned EGL_BAD_SURFACE. tid="
								+ Thread.currentThread().getId());
				return false;
			case EGL10.EGL_SUCCESS:
				return true;
			default:
				NaviLogUtil.debug("EglHelper", "eglSwapBuffers returned default. error="
						+ error);
				break;
			}
		}
		return true;
		/*
		 * Always check for EGL_CONTEXT_LOST, which means the context and all
		 * associated data were lost (For instance because the device went to
		 * sleep). We need to sleep until we get a new surface.
		 */
		// return mEgl.eglGetError() != EGL11.EGL_CONTEXT_LOST;
	}
	
	public void finalize() {
		// finalization code here
		finish();
	}
	

	@Override
	public EGLSurface createSurface(Surface surface) {
		return null;
	}
	
	public EGLSurface createSurface(Surface drawSurface,Surface readSurface){
		return null;
	}

}
