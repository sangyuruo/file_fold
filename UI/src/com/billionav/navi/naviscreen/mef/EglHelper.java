/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.billionav.navi.naviscreen.mef;

import java.util.concurrent.CountDownLatch;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;

import android.view.Surface;
import android.view.SurfaceHolder;

import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.app.ext.util.MediaCoderHelper;
import com.billionav.navi.naviscreen.mef.tool.EglPbufferSurfaceStrategy;
import com.billionav.navi.naviscreen.mef.tool.EglPixMapSurfaceStrategy;
import com.billionav.navi.naviscreen.mef.tool.EglSurfaceStrategy;
import com.billionav.navi.naviscreen.mef.tool.EglWindowSurfaceForMediacoderStrategy;
import com.billionav.navi.naviscreen.mef.tool.EglWindowSurfaceStrategy;

public class EglHelper {
	static EglSurfaceStrategy eglSurfaceStrategy;
	CountDownLatch mediaStartCountDownLatch = new CountDownLatch(1);
	long guardedRunStartTime = System.currentTimeMillis();

	CountDownLatch loopSendStartsSignal = new CountDownLatch(1);
	CountDownLatch waitSendHUThreadSignal = new CountDownLatch(1);

	private boolean isStartH264 = false;
	private boolean isSignalLoopSendStart = false;

	public void awaitSendHUThread() {
		try {
			waitSendHUThreadSignal.await();
		} catch (InterruptedException e) {
		}
	}

	public void countDownWaitSendHUThread() {
		waitSendHUThreadSignal.countDown();
	}

	public boolean isStartH264() {
		return isStartH264;
	}

	public void setStartH264(boolean isStartH264) {
		this.isStartH264 = isStartH264;
	}

	public void init() {
		isStartH264 = false;
		isSignalLoopSendStart = false;
	}

	public void waitLoopSendStart() {
		try {
			loopSendStartsSignal.await();
		} catch (InterruptedException e) {
		}
	}

	public void signLoopSendStart() {
		if (!isSignalLoopSendStart) {
			isSignalLoopSendStart = true;
			loopSendStartsSignal.countDown();
		}
	}

	public long getGuardedRunStartTime() {
		return guardedRunStartTime;
	}

	public void setPresentationTime(long nsecs) {
		eglSurfaceStrategy.setPresentationTime(nsecs);
	}

	public void setPresentationTime(int frameRate, int frameIndex) {
		eglSurfaceStrategy.setPresentationTime(computePresentationTime(frameRate,frameIndex)* 1000);
	}

	public static long computePresentationTimeNsec(int frameIndex) {
		final long ONE_BILLION = 1000000000;
		return frameIndex * ONE_BILLION / MediaCoderHelper.FRAME_RATE;
	}

	public static long computePresentationTime(int frameRate, int frameIndex) {
		final long ONE_BILLION = 1000000;
		return 123 + frameIndex * ONE_BILLION / frameRate;
	}

	public void waitMediaStart() {
		try {
			mediaStartCountDownLatch.await();
		} catch (InterruptedException e) {
		}
	}

	public void signMediaStart() {
		mediaStartCountDownLatch.countDown();
	}

	private EglHelper() {

	}

	protected void finalize() {
		// finalization code here
		finish();
	}

	public static synchronized EglHelper getInstance() {
		if (null == msInstance) {
			msInstance = new EglHelper();
			if (NaviConstant.SURFACE_TYPE == 0) {
				eglSurfaceStrategy = EglWindowSurfaceStrategy.getInstance();
			} else if (NaviConstant.SURFACE_TYPE == EGL10.EGL_PBUFFER_BIT) {
				eglSurfaceStrategy = EglPbufferSurfaceStrategy.getInstance();
			} else if (NaviConstant.SURFACE_TYPE == EGL10.EGL_PIXMAP_BIT) {
				eglSurfaceStrategy = EglPixMapSurfaceStrategy.getInstance();
			} else if (NaviConstant.SURFACE_TYPE == 3) {
				eglSurfaceStrategy = EglWindowSurfaceForMediacoderStrategy
						.getInstance();
			}
			msInstance.start();
		}
		return msInstance;
	}

	/**
	 * Initialize EGL for a given configuration spec.
	 * 
	 * @param configSpec
	 */
	protected void start() {
		this.init();
		eglSurfaceStrategy.init();
		guardedRunStartTime = System.currentTimeMillis();
	}

	protected void finish() {
		eglSurfaceStrategy.finish();
	}

	/*
	 * React to the creation of a new surface by creating and returning an
	 * OpenGL interface that renders to that surface.
	 */
	public EGLSurface createSurface(SurfaceHolder holder) {
		return eglSurfaceStrategy.createSurface(holder);
	}

	/*
	 * React to the creation of a new surface by creating and returning an
	 * OpenGL interface that renders to that surface.
	 */
	public EGLSurface createSurface(Surface surface) {
		return eglSurfaceStrategy.createSurface(surface);
	}

	public EGLSurface createSurface(Surface drawSurface, Surface readSurface) {
		return eglSurfaceStrategy.createSurface(drawSurface, readSurface);
	}

	public void destroySurface(EGLSurface surface) {
		eglSurfaceStrategy.destroySurface(surface);
	}

	public GL bindSurface(EGLSurface surface) {
		return eglSurfaceStrategy.bindSurface(surface);
	}

	public GL bindSurface(EGLSurface drawSurface, EGLSurface readSurface) {
		return eglSurfaceStrategy.bindSurface(drawSurface, readSurface);
	}

	/**
	 * Display the current render surface.
	 * 
	 * @return false if the context has been lost.
	 */
	public boolean swap() {
		return eglSurfaceStrategy.swap();
		/*
		 * Always check for EGL_CONTEXT_LOST, which means the context and all
		 * associated data were lost (For instance because the device went to
		 * sleep). We need to sleep until we get a new surface.
		 */
		// return mEgl.eglGetError() != EGL11.EGL_CONTEXT_LOST;
	}

	private static EglHelper msInstance = null;
}