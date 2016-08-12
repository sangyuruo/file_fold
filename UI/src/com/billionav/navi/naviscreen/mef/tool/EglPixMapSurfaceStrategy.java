package com.billionav.navi.naviscreen.mef.tool;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLSurface;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.EGL14;
import android.view.SurfaceHolder;

import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.app.ext.log.NaviLogUtil;

public class EglPixMapSurfaceStrategy extends AbstractEglSurfaceStrategy
		implements EglSurfaceStrategy {
	private EglPixMapSurfaceStrategy() {
	}

	static EglPixMapSurfaceStrategy strategy = null;
	Bitmap bmp = null;

	public static synchronized EglPixMapSurfaceStrategy getInstance() {
		if (null == strategy) {
			strategy = new EglPixMapSurfaceStrategy();
		}
		return strategy;
	}

	private void testGetConfigs() {
		int[] num_conf = new int[1];
		mEgl.eglGetConfigs(mEglDisplay, null, 0, num_conf); // if configuration
															// array is null it
															// still returns the
															// number of
															// configurations
		int size = 0;
		int error = mEgl.eglGetError();
		if (error == EGL10.EGL_SUCCESS) {
			size = num_conf[0];
			NaviLogUtil.debugEglConfig(String.format(
					"eglGetConfigs num_config is %d", size));
		} else {
			NaviLogUtil.debugEglStep("eglGetConfigs error " + error);
		}

		if (size > 0) {
			EGLConfig[] configs = new EGLConfig[size];
			mEgl.eglGetConfigs(mEglDisplay, configs, size, num_conf);
			error = mEgl.eglGetError();
			if (error == EGL10.EGL_SUCCESS) {
				for (int i = 0; i < configs.length; i++) {
					EGLConfig eglConfig = configs[i];

					int[] depth = new int[1];
					mEgl.eglGetConfigAttrib(mEglDisplay, eglConfig,
							EGL10.EGL_DEPTH_SIZE, depth);
					int[] redSize = new int[1];
					mEgl.eglGetConfigAttrib(mEglDisplay, eglConfig,
							EGL10.EGL_RED_SIZE, redSize);
					int[] surfaceType = new int[1];
					try {
						mEgl.eglGetConfigAttrib(mEglDisplay, eglConfig,
								EGL10.EGL_SURFACE_TYPE, surfaceType);
					} catch (Throwable ex) {
						NaviLogUtil.debugEglConfig("error:" + ex.getMessage());
					}
					NaviLogUtil
							.debugEglConfig(String
									.format("eglConfig depth is %d , redSize is %d , surfaceType is %d",
											depth[0], redSize[0],
											surfaceType[0]));
				}
			} else {
				NaviLogUtil.debugEglStep("eglGetConfigs error " + error);
			}
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		NaviLogUtil.debugEglStep("Get an EGL instance");
		/*
		 * Get an EGL instance
		 */
		mEgl = (EGL10) EGLContext.getEGL();
		NaviLogUtil.debugEglStep("Get to the default display.");

		// try {
		// Thread.sleep(5000);
		// String version = mEgl
		// .eglQueryString(mEglDisplay, EGL10.EGL_VERSION);
		// String vendor = mEgl.eglQueryString(mEglDisplay, EGL10.EGL_VENDOR);
		// NaviLogUtil.debugEglStep(String.format(
		// "version is %s , vendor is %s ", version, vendor));
		// } catch (Throwable ex) {
		// NaviLogUtil.debugEglStep(ex.getMessage());
		// }

		/*
		 * Get to the default display.
		 */
		mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

		NaviLogUtil.debugEglStep("We can now initialize EGL for that display.");
		/*
		 * We can now initialize EGL for that display
		 */
		int[] version = new int[2];
		mEgl.eglInitialize(mEglDisplay, version);

		int error = mEgl.eglGetError();
		if (error == EGL10.EGL_NOT_INITIALIZED) {
			NaviLogUtil.debugEglStep("initialize error " + error);
		} else {
			NaviLogUtil
					.debugEglStep(String
							.format("initialize success, version(1) is %d and version(2) is %d ",
									version[0], version[1]));
		}

		testGetConfigs();

		int[] num_config = new int[1];
		int[] configWant = { EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PIXMAP_BIT,
				// EGL10.EGL_RED_SIZE, 8,
				// EGL10.EGL_GREEN_SIZE, 8,
				// EGL10.EGL_BLUE_SIZE, 8,
				// EGL10.EGL_DEPTH_SIZE, 16,
				EGL10.EGL_NONE };

		mEgl.eglChooseConfig(mEglDisplay, configWant, null, 0, num_config);
		int numConfigs = num_config[0];

		error = mEgl.eglGetError();
		if (error == EGL10.EGL_SUCCESS) {
			NaviLogUtil
					.debugEglStep("1 eglChooseConfig success ,  numConfigs is "
							+ numConfigs);
		} else {
			NaviLogUtil.debugEglStep("1 eglChooseConfig error " + error);
		}

		num_config = new int[1];
		int[] configWant2 = {
				EGL10.EGL_SURFACE_TYPE,
				EGL10.EGL_PIXMAP_BIT,
				// EGL10.EGL_RED_SIZE, 8,
				// EGL10.EGL_GREEN_SIZE, 8,
				// EGL10.EGL_BLUE_SIZE, 8,
				// EGL10.EGL_DEPTH_SIZE, 16,
				EGL10.EGL_RED_SIZE, 8, EGL10.EGL_GREEN_SIZE, 8,
				EGL10.EGL_BLUE_SIZE, 8, EGL10.EGL_ALPHA_SIZE, 8,

				EGL10.EGL_NONE };

		mEgl.eglChooseConfig(mEglDisplay, configWant2, null, 0, num_config);
		numConfigs = num_config[0];

		error = mEgl.eglGetError();
		if (error == EGL10.EGL_SUCCESS) {
			NaviLogUtil
					.debugEglStep("2 eglChooseConfig success ,  numConfigs is "
							+ numConfigs);
		} else {
			NaviLogUtil.debugEglStep("2 eglChooseConfig error " + error);
		}

		num_config = new int[1];
		int[] configWant3 = {
				EGL10.EGL_SURFACE_TYPE,
				EGL10.EGL_PIXMAP_BIT,
				// EGL10.EGL_RED_SIZE, 8,
				// EGL10.EGL_GREEN_SIZE, 8,
				// EGL10.EGL_BLUE_SIZE, 8,
				// EGL10.EGL_DEPTH_SIZE, 16,

				EGL10.EGL_RED_SIZE, 8, EGL10.EGL_GREEN_SIZE, 8,
				EGL10.EGL_BLUE_SIZE, 8, EGL10.EGL_ALPHA_SIZE, 8,
				EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,

				EGL10.EGL_NONE };

		mEgl.eglChooseConfig(mEglDisplay, configWant3, null, 0, num_config);
		numConfigs = num_config[0];

		error = mEgl.eglGetError();
		if (error == EGL10.EGL_SUCCESS) {
			NaviLogUtil
					.debugEglStep("3 eglChooseConfig success ,  numConfigs is "
							+ numConfigs);
		} else {
			NaviLogUtil.debugEglStep("3 eglChooseConfig error " + error);
		}

		num_config = new int[1];
		int[] configWant4 = {
				EGL10.EGL_SURFACE_TYPE,
				EGL10.EGL_PIXMAP_BIT,
				// EGL10.EGL_RED_SIZE, 8,
				// EGL10.EGL_GREEN_SIZE, 8,
				// EGL10.EGL_BLUE_SIZE, 8,
				// EGL10.EGL_DEPTH_SIZE, 16,

				EGL10.EGL_RED_SIZE, 5, EGL10.EGL_GREEN_SIZE, 6,
				EGL10.EGL_BLUE_SIZE, 5, EGL10.EGL_RENDERABLE_TYPE,
				EGL14.EGL_OPENGL_ES2_BIT, EGL10.EGL_NONE };

		mEgl.eglChooseConfig(mEglDisplay, configWant4, null, 0, num_config);
		numConfigs = num_config[0];

		error = mEgl.eglGetError();
		if (error == EGL10.EGL_SUCCESS) {
			NaviLogUtil
					.debugEglStep("4 eglChooseConfig success ,  numConfigs is "
							+ numConfigs);
		} else {
			NaviLogUtil.debugEglStep("4 eglChooseConfig error " + error);
		}

		num_config = new int[1];
		int[] configWant5 = {
				EGL10.EGL_SURFACE_TYPE,
				EGL10.EGL_PIXMAP_BIT,
				// EGL10.EGL_RED_SIZE, 8,
				// EGL10.EGL_GREEN_SIZE, 8,
				// EGL10.EGL_BLUE_SIZE, 8,
				// EGL10.EGL_DEPTH_SIZE, 16,
				EGL10.EGL_RED_SIZE, 5, EGL10.EGL_GREEN_SIZE, 6,
				EGL10.EGL_BLUE_SIZE, 5, EGL10.EGL_NONE };

		mEgl.eglChooseConfig(mEglDisplay, configWant5, null, 0, num_config);
		numConfigs = num_config[0];

		error = mEgl.eglGetError();
		if (error == EGL10.EGL_SUCCESS) {
			NaviLogUtil
					.debugEglStep("5 eglChooseConfig success ,  numConfigs is "
							+ numConfigs);
		} else {
			NaviLogUtil.debugEglStep("5 eglChooseConfig error " + error);
		}

		if (numConfigs <= 0) {
			NaviLogUtil.debugEglStep("No configs match configSpec");
			throw new IllegalArgumentException("No configs match configSpec");
		}

		int[] configWant6 = { EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PIXMAP_BIT,
				// EGL10.EGL_RED_SIZE, 8,
				// EGL10.EGL_GREEN_SIZE, 8,
				// EGL10.EGL_BLUE_SIZE, 8,
				// EGL10.EGL_DEPTH_SIZE, 16,

				// EGL10.EGL_RED_SIZE, 8, EGL10.EGL_GREEN_SIZE, 8,
				// EGL10.EGL_BLUE_SIZE, 8, EGL10.EGL_ALPHA_SIZE, 8,
				// EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,

				EGL10.EGL_NONE };

		EGLConfig[] configs = new EGLConfig[1];
		mEgl.eglChooseConfig(mEglDisplay, configWant6, configs, 1, num_config);

		error = mEgl.eglGetError();
		if (error == EGL10.EGL_SUCCESS) {
			NaviLogUtil.debugEglStep("eglChooseConfig success ");
		} else {
			NaviLogUtil.debugEglStep("eglChooseConfig error " + error);
		}

		EGLConfig config = configs[0];
		if (config == null) {
			NaviLogUtil.debugEglStep("No config chosen");
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

		error = mEgl.eglGetError();
		if (error == EGL10.EGL_SUCCESS) {
			NaviLogUtil.debugEglStep("eglCreateContext success ");
		} else {
			NaviLogUtil.debugEglStep("eglCreateContext error " + error);
		}

		if (mEglContext == null || mEglContext == EGL10.EGL_NO_CONTEXT) {
			NaviLogUtil.debugEglStep("createContext failed");
			throw new RuntimeException("createContext failed");
		}

		mEglSurface = null;
	}

	@Override
	public EGLSurface createSurface(SurfaceHolder holder) {
		bmp = Bitmap.createBitmap(800, 480, Config.ARGB_8888);
		EGLSurface surface = mEgl.eglCreatePixmapSurface(mEglDisplay,
				mEglConfig, bmp, null);

		int error = mEgl.eglGetError();
		if (error == EGL10.EGL_SUCCESS) {
			NaviLogUtil.debugEglStep("createPixmapSurface success ");
		} else {
			NaviLogUtil.debugEglStep("createPixmapSurface error " + error);
		}

		if (surface == null) {
			NaviLogUtil.error(NaviConstant.TAG_EGL_ERROR,
					"createPixmapSurface failed, surface == null");
			throw new RuntimeException("createWindowSurface failed");
		} else if (surface == EGL10.EGL_NO_SURFACE) {
			NaviLogUtil.error(NaviConstant.TAG_EGL_ERROR,
					"createPixmapSurface failed");
			throw new RuntimeException(
					"createWindowSurface failed,surface == EGL10.EGL_NO_SURFACE");
		}
		return surface;
	}

	public Bitmap getBmp() {
		return bmp;
	}

}
