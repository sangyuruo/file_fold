package com.billionav.navi.naviscreen.mef.tool;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLSurface;

import com.billionav.navi.app.ext.log.NaviLogUtil;

import android.view.SurfaceHolder;

/**
 * 
 * @author sangjun
 */
public class EglWindowSurfaceStrategy extends AbstractEglSurfaceStrategy
		implements EglSurfaceStrategy {
	
	private EglWindowSurfaceStrategy(){ 
	}
	
	static EglWindowSurfaceStrategy strategy = null;

	public static synchronized EglSurfaceStrategy getInstance() {
		if (null == strategy) {
			strategy = new EglWindowSurfaceStrategy();
		}
		return strategy;
	}

	@Override
	public void init() {
		/*
		 * Get an EGL instance
		 */
		mEgl = (EGL10) EGLContext.getEGL();

		/*
		 * Get to the default display.
		 */
		mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

		/*
		 * We can now initialize EGL for that display
		 */
		int[] version = new int[2];
		mEgl.eglInitialize(mEglDisplay, version);

		int[] num_config = new int[1];
		int[] config_spec = { EGL10.EGL_RENDERABLE_TYPE, 0x04,
				EGL10.EGL_DEPTH_SIZE, 16,
				// EGL10.EGL_RED_SIZE, 8,
				// EGL10.EGL_GREEN_SIZE, 8,
				// EGL10.EGL_BLUE_SIZE, 8,
				// EGL10.EGL_ALPHA_SIZE, 8,
				EGL10.EGL_NONE };

		mEgl.eglChooseConfig(mEglDisplay, config_spec, null, 0, num_config);

		int numConfigs = num_config[0];

		if (numConfigs <= 0) {
			throw new IllegalArgumentException("No configs match configSpec");
		}

		EGLConfig[] configs = new EGLConfig[numConfigs];
		mEgl.eglChooseConfig(mEglDisplay, config_spec, configs, numConfigs,
				num_config);
		EGLConfig config = configs[0];
		if (config == null) {
			throw new IllegalArgumentException("No config chosen");
		}
		mEglConfig = config;

		/*
		 * Create an OpenGL ES context. This must be done only once, an OpenGL
		 * context is a somewhat heavy object.
		 */

		int[] attrib_list = { EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE };

		mEglContext = mEgl.eglCreateContext(mEglDisplay, mEglConfig,
				EGL10.EGL_NO_CONTEXT, attrib_list);

		if (mEglContext == null || mEglContext == EGL10.EGL_NO_CONTEXT) {
			throw new RuntimeException("createContext failed");
		}

		mEglSurface = null;
	}

	@Override
	public EGLSurface createSurface(SurfaceHolder holder) {
		/*
		 * Create an EGL surface we can render into.
		 */
		EGLSurface surface = mEgl.eglCreateWindowSurface(mEglDisplay,
				mEglConfig, holder, null);
		if (surface == null || surface == EGL10.EGL_NO_SURFACE) {
			NaviLogUtil.error("EglHelper", "createWindowSurface failed");
			throw new RuntimeException("createWindowSurface failed");
		}

		return surface;
	}

}
