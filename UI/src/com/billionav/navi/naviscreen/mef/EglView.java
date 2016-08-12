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
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.billionav.jni.MapEngineJNI;
import com.billionav.navi.app.ext.EncoderService;
import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.app.ext.gles.FullFrameRect;
import com.billionav.navi.app.ext.gles.Texture2dProgram;
import com.billionav.navi.app.ext.log.NaviLogUtil;
import com.billionav.navi.app.ext.util.MediaCoderNewHelper;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.sync.AppLinkService;

/* copied from GLSurfaceView */
public class EglView extends SurfaceView implements SurfaceHolder.Callback {
	private final static boolean LOG_THREADS = false;
	private final static boolean HENIUS_DEBUG = false;

	private final static int mMaxFps = 15;
	private final static int mMinInterval = 1000 / mMaxFps;

	private static GLThread mGLThread = null;
	private boolean threadStarted = false;

	private int widthVehicle = 800;
	private int heightVehicle = 480;
	private int[] mapBytes = null;
	private int[] preMapBytes = null;
	SurfaceHolder holder = null;
	private EncoderService mediaCoderHelper;
	/**
	 * Standard View constructor. In order to render something, you must call
	 * {@link #setRenderer} to register a renderer.
	 */
	public EglView(Context context) {
		super(context);
		init();
	}

	/**
	 * Standard View constructor. In order to render something, you must call
	 * {@link #setRenderer} to register a renderer.
	 */
	public EglView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		this.holder = holder;

		if (HENIUS_DEBUG) {
			System.out.println("--henius EglView init");
		}

		mapBytes = new int[widthVehicle * heightVehicle];
		if (null == mGLThread) {
			mGLThread = new GLThread();
		}
	}

	public void startMap() {
		if ((null != mGLThread) && (!threadStarted)) {
			mGLThread.start();
			threadStarted = true;
		}
		NaviLogUtil.debugMap("MapRender startMap");
	}

	/**
	 * This method is part of the SurfaceHolder.Callback interface, and is not
	 * normally called or subclassed by clients of GLSurfaceView.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		if (HENIUS_DEBUG) {
			System.out.println("--henius report surfaceCreated");
		}
		mGLThread.surfaceCreated();
	}

	/**
	 * This method is part of the SurfaceHolder.Callback interface, and is not
	 * normally called or subclassed by clients of GLSurfaceView.
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return
		if (HENIUS_DEBUG) {
			System.out.println("--henius report surfaceDestroyed");
		}
		threadStarted = false;
		mGLThread.surfaceDestroyed();
	}

	/**
	 * This method is part of the SurfaceHolder.Callback interface, and is not
	 * normally called or subclassed by clients of GLSurfaceView.
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		mGLThread.onWindowResize(w, h);
	}

	/**
	 * Inform the view that the activity is paused. The owner of this view must
	 * call this method when the activity is paused. Calling this method will
	 * pause the rendering thread. Must not be called before a renderer has been
	 * set.
	 */
	public void onPause() {
		if (HENIUS_DEBUG) {
			System.out.println("--henius onPause " + this.getId());
		}
		mGLThread.onPause();
	}

	/**
	 * Inform the view that the activity is resumed. The owner of this view must
	 * call this method when the activity is resumed. Calling this method will
	 * recreate the OpenGL display and resume the rendering thread. Must not be
	 * called before a renderer has been set.
	 */
	public void onResume() {
		if (HENIUS_DEBUG) {
			System.out.println("--henius onResume " + this.getId());
		}
		mGLThread.onResume();
	}

	public void onDestory() {
		if (HENIUS_DEBUG) {
			System.out.println("--henius onDestory " + this.getId());
		}
		mGLThread.requestExitAndWait();
	}

	/**
	 * This method is used as part of the View class and is not normally called
	 * or subclassed by clients of GLSurfaceView. Must not be called before a
	 * renderer has been set.
	 */
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (HENIUS_DEBUG) {
			System.out.println("--henius onDetachedFromWindow " + this.getId());
		}
		// mGLThread.requestExitAndWait();
	}

	/* copied from GLSurfaceView */
	public interface Renderer {
		void onSurfaceCreated(GL10 gl, EGLConfig config);

		void onSurfaceChanged(GL10 gl, int width, int height);

		boolean onDrawFrame(GL10 gl, int nativerunmode);

		void onSurfaceDestroyed(GL10 gl);
	}

	/* copied from GLSurfaceView */
	class GLThread extends Thread {

		boolean isPhoneSurface = true;

		boolean isSetHuSurfaceSize = false;
		boolean isSetPhoneSurfaceSize = false;

		boolean isFirstFrame = true;

		GLThread() {
			super();
			mDone = false;
			// mPaused = true;
			mPaused = false;
			mEglInitialized = false;
			mHasSurface = false;
			mWidth = 0;
			mHeight = 0;
			mSizeChanged = false;
			mWaiter = null;

			mRenderer = EglRenderer.getInstance();
			mediaCoderHelper = EncoderService.getInstance();
			mEglSurface = null;
			mGL = null;
			setName("GLThread " + getId());
			mLastTick = 0;
		}

		@Override
		public void run() {
			NaviLogUtil.debugMap("starting tid=" + getId());
			android.os.Process
					.setThreadPriority(android.os.Process.THREAD_PRIORITY_DISPLAY);
			try {
				mediaCoderHelper.prepareEncoder();
				// Thread.sleep(10000);

				// mediaCoderHelper.setmInputSurface(new
				// EXInputSurface(getHolder()));
				// guardedRun();
				// surfaceRun();
				// guardedRunMedia();
				if (NaviConstant.MODE_MEDIA_SURFACE == 1) {
					isSetHuSurfaceSize = false;
					isSetPhoneSurfaceSize = false;
					isFirstFrame = false;
					guardedRunOnlyOneSide();
				} else if (NaviConstant.MODE_MEDIA_SURFACE == 2) {
					guardedRunMedia();
				}
			} catch (InterruptedException e) {
				NaviLogUtil.error(NaviConstant.TAG_ERROR,
						"eglview: " + e.getMessage());
				// fall thru and exit normally
			} finally {
				sGLThreadManager.threadExiting(this);
			}
		}

		/*
		 * This private method should only be called inside a
		 * synchronized(sGLThreadManager) block.
		 */
		private void stopEglLocked() {
			if (null != mEglSurface) {
				mRenderer.onSurfaceDestroyed((GL10) mGL);
				destroyEglSurface();
				mediaCoderHelper.releaseEncoder();
			}
		}
		 

		boolean isMediaSurface = false;
		FullFrameRect mFullFrameBlit;
		private SurfaceTexture mCameraTexture;  // receives the output from the camera preview
	    private int mTextureId;

	    /**
		 * 在车机和手机上都显示地图
		 * 
		 * @throws InterruptedException
		 */
		private void guardedRunMedia() throws InterruptedException {
			try {
				/*
				 * This is our main activity thread's loop, we go until asked to
				 * quit.
				 */

				if (!mEglInitialized) {
					EglHelper.getInstance();
					mEglInitialized = true;
				}
				int count = 0;
				long startTime = 0;
				long endTime = 0;
				long drawFrameTime = 0;
				long drawFrameEndTime = 0;
				long swapTime = 0;
				long swapEndTime = 0;
				long guardedRunStartTime = EglHelper.getInstance()
						.getGuardedRunStartTime();
				long doSendHuTime = 0;
				long doSendHuEndTime = 0;
				boolean isSetHuSurfaceSize = false;
				boolean isSetPhoneSurfaceSize = false;
				int index = 0;
				
				boolean curIsRun = false;
				boolean preIsRun = false;
				while (!isDone()) {
					AppLinkService service = AppLinkService.getInstance();
					if (null != service && service.isRunning()) {
						if (!isSetHuSurfaceSize) {
							// MapEngineJNI.setSurfaceSize(800, 480);
							isSetHuSurfaceSize = true;
							isSetPhoneSurfaceSize = false;
						}
					}
					if (null == service || !service.isRunning()) {
						if (!isSetPhoneSurfaceSize) {
							// MapEngineJNI.setSurfaceSize(mWidth, mHeight);
							isSetHuSurfaceSize = false;
							isSetPhoneSurfaceSize = true;
						}
					}


					// TODO Test
					count++;
					startTime = System.currentTimeMillis()
							- guardedRunStartTime;
					NaviLogUtil.debug(NaviConstant.TAG, "guardedRun Loop...");
					/*
					 * Update the asynchronous state (window size)
					 */
					int w = 0;
					int h = 0;
					boolean sizechanged = false;
					boolean paused = false;
					boolean hassurface = false;
					CountDownLatch curWaiter = null;
					synchronized (sGLThreadManager) {
						w = mWidth;
						h = mHeight;
						paused = mPaused;
						hassurface = mHasSurface;
						curWaiter = mWaiter;
						mWaiter = null;
						if (mSizeChanged && !paused) {
							NaviLogUtil.debug("EglHelper", "size change "
									+ mSizeChanged);
							sizechanged = true;
							mSizeChanged = false;
						}
						sGLThreadManager.notifyAll();
					} // end of synchronized(sGLThreadManager)

					/* Running */
					boolean nativerunmode = false;
					// if (!paused) {
					// if (hassurface) {
					if (mEglSurface == null) {
						createEglSurface();

						if (mEglSurface != null) {
							NaviLogUtil.debugMap("onSurfaceCreated...");
							
							mFullFrameBlit = new FullFrameRect(
					                new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT));
							mTextureId = mFullFrameBlit.createTextureObject();
					        mCameraTexture = new SurfaceTexture(mTextureId);
							mRenderer.onSurfaceCreated(mGL, null);
						}
						NSTriggerInfo info = new NSTriggerInfo();
						info.m_iTriggerID = 1000000;
						MenuControlIF.Instance().TriggerForScreen(info);
					} else {
						if (sizechanged) {
							if (!isMediaSurface) {
								NaviLogUtil.debug("EglHelper",
										"recreate EglSurface");
								destroyEglSurface(selfSurface);
								selfSurface = null; // it is important
								createEglSurface();
							}
						}
					}
					

					if (isMediaSurface) {
						mEglSurface = mediaSurface;
					} else {
						mEglSurface = selfSurface;
					}

					EglHelper.getInstance().bindSurface(mEglSurface);
//					if (isMediaSurface) {
//						// if (null != service && service.isRunning() ){
//						
//						// }
//					} else {
//						EglHelper.getInstance().bindSurface(mEglSurface);
//					}

					if (sizechanged) {
						NaviLogUtil.debugMap("onSurfaceChanged,w:" + w + "h:"
								+ h);
						if (null == service || !service.isRunning()) {
							mRenderer.onSurfaceChanged(mGL, w, h);
						}
					}

					if (mEglSurface != null) {
						nativerunmode = true;
					}

					int operate = 0;
					if (nativerunmode && !paused) {
						operate = 1;
					}
					operate = 3;
					NaviLogUtil.debug(NaviConstant.TAG, "onDrawFrame:"
							+ operate);
					if (w <= 0 && h <= 0) {
						NaviLogUtil.error(NaviConstant.TAG_ERROR,
								"w <= 0 && h <= 0");
						// continue;
					}
					drawFrameTime = System.currentTimeMillis()
							- guardedRunStartTime;
					
					
					curIsRun = mRenderer.onDrawFrame(mGL, operate);
//					EglHelper.getInstance().setPresentationTime(mMaxFps, count);
					if (curIsRun || preIsRun ) { 
						preIsRun = curIsRun;
						drawFrameEndTime = System.currentTimeMillis()
								- guardedRunStartTime;
						NaviLogUtil.debug(NaviConstant.TAG,
								"----- applink get buffer");
						NaviLogUtil.debug(NaviConstant.TAG,
								"time: " + System.currentTimeMillis());
						if( isMediaSurface ){
							mediaCoderHelper.frameAvailableSoon();
						}
						EglHelper.getInstance().setPresentationTime(mMaxFps, count);
//			            mEncoderSurface.setPresentationTime(mCameraTexture.getTimestamp());
					}
						try {
							swapTime = System.currentTimeMillis()
									- guardedRunStartTime;
							boolean isSuccess = EglHelper.getInstance().swap();
							swapEndTime = System.currentTimeMillis()
									- guardedRunStartTime;
							doSendHuTime = System.currentTimeMillis()
									- guardedRunStartTime;
//							if( isSuccess ){
//								if (null != service && service.isRunning()) {
//									service.doSendHU(count);
//								} else {
//									if( isMediaSurface ){
//										service.doSendHU(count);
//									}
//								}
//							}else{
//								try{
//									service.doSendHU(count);
//								}catch(Throwable ex){
//									NaviLogUtil.error(NaviConstant.TAG_ERROR,
//											"doSendHU: " + ex.getMessage());
//								}
//							}
							doSendHuEndTime = System.currentTimeMillis()
									- guardedRunStartTime;
							endTime = System.currentTimeMillis()
									- guardedRunStartTime;

							NaviLogUtil.debugEglDrawCostTime(count + ","
									+ startTime + "," + drawFrameTime + ","
									+ drawFrameEndTime + "," + doSendHuTime
									+ "," + doSendHuEndTime + "," + swapTime
									+ "," + swapEndTime + "," + endTime);
						} catch (Exception ex) {
							NaviLogUtil.error(NaviConstant.TAG_ERROR,
									"guardedRun: " + ex.getMessage());
						}
//					} else {
//						if( isMediaSurface ){
//							service.doSendHU(count);
//						}
//					}
					
					if (isMediaSurface) {
						isMediaSurface = false;
					} else {
						isMediaSurface = true;
					}
					
					// }
					if (null != curWaiter) {
						curWaiter.countDown();
					}
//					long tick = System.currentTimeMillis();
//					if (tick - mLastTick < mMinInterval) {
//						Thread.sleep(mMinInterval - (tick - mLastTick));
//						tick = System.currentTimeMillis();
//					}
//					mLastTick = tick;
					
				}
			} catch (Throwable ex) {
				NaviLogUtil.error(NaviConstant.TAG_ERROR,
						"guardedRun: " + ex.getMessage());
			} finally {
				/*
				 * clean-up everything...
				 */
				synchronized (sGLThreadManager) {
					stopEglLocked();
				}
			}
		}

		/**
		 * 只在车机或者手机显示
		 * 
		 * @throws InterruptedException
		 */
		private void guardedRunOnlyOneSide() throws InterruptedException {
			// TODO guardedRunOnlyOneSide
			// boolean isReturn = true;
			// if( isReturn ){
			// guardedRunOnlyOneSide2();
			// return;
			// }
			try {
				/*
				 * This is our main activity thread's loop, we go until asked to
				 * quit.
				 */
				if (!mEglInitialized) {
					EglHelper.getInstance();
					mEglInitialized = true;
				}
				int count = 0;
				long startTime = 0;
				long endTime = 0;
				long drawFrameTime = 0;
				long drawFrameEndTime = 0;
				long swapTime = 0;
				long swapEndTime = 0;
				long guardedRunStartTime = EglHelper.getInstance()
						.getGuardedRunStartTime();
				long doSendHuTime = 0;
				long doSendHuEndTime = 0;

				int index = 0;
				boolean isConnectHU = false;
				NaviLogUtil.debugEglStep("start guardedRunOnlyOneSide loop...");
				while (!isDone()) {
					AppLinkService service = AppLinkService.getInstance();
					if (null == service || !service.isRunning()) {
						if (!isSetPhoneSurfaceSize) {
							NaviLogUtil.debugEglStep(String
									.format("setSurfaceSize %s, %s...", mWidth,
											mHeight));
							// 切换到手机画面前需要bind并且set surface
							if (null != mEglSurface) {
								bindMEglSurface();
								// EglHelper.getInstance().bindSurface(mEglSurface);
								// MapEngineJNI.setSurfaceSize(mWidth, mHeight);
								// isSetHuSurfaceSize = false;
								// isSetPhoneSurfaceSize = true;
							}
						}
					}

					// TODO Test
					count++;
					startTime = System.currentTimeMillis()
							- guardedRunStartTime;
					/*
					 * Update the asynchronous state (window size)
					 */
					int w = 0;
					int h = 0;
					boolean sizechanged = false;
					boolean paused = false;
					boolean hassurface = false;
					CountDownLatch curWaiter = null;
					synchronized (sGLThreadManager) {
						w = mWidth;
						h = mHeight;
						paused = mPaused;
						hassurface = mHasSurface;
						curWaiter = mWaiter;
						mWaiter = null;
						if (mSizeChanged && !paused) {
							NaviLogUtil.debugEglStep("size change "
									+ mSizeChanged);
							sizechanged = true;
							mSizeChanged = false;
						}
						sGLThreadManager.notifyAll();
					} // end of synchronized(sGLThreadManager)

					/* Running */
					boolean nativerunmode = false;
					// if (!paused) {
					// if (hassurface) {
				
					handlerSizeChange(sizechanged, w, h);

					if (sizechanged) {
						isConnectHU = false;
					}

					if (mEglSurface != null) {
						nativerunmode = true;
					}

					int operate = 0;
					if (nativerunmode && !paused) {
						operate = 1;
					}
					operate = 3;
					if (w <= 0 && h <= 0) {
						NaviLogUtil.error(NaviConstant.TAG_ERROR,
								"w <= 0 && h <= 0");
						// continue;
					}

					service = AppLinkService.getInstance();
					if (null != service && service.isRunning()
							&& !service.isEmpty()) {
						if (null != curWaiter) {
							curWaiter.countDown();
						}
						// long tick = System.currentTimeMillis();
						// mLastTick = tick;
						Thread.sleep(5L);
						continue;
					}
					drawFrameTime = System.currentTimeMillis()
							- guardedRunStartTime;
					boolean isRun = mRenderer.onDrawFrame(mGL, operate);
					EglHelper.getInstance().setPresentationTime(mMaxFps, count);
					if (isRun) {
						drawFrameEndTime = System.currentTimeMillis()
								- guardedRunStartTime;
						try {
							swapTime = System.currentTimeMillis()
									- guardedRunStartTime;
							EglHelper.getInstance().swap();
							swapEndTime = System.currentTimeMillis()
									- guardedRunStartTime;
							doSendHuTime = System.currentTimeMillis()
									- guardedRunStartTime;
							isConnectHU = sendToHU(count, sizechanged,
									isConnectHU);
							doSendHuEndTime = System.currentTimeMillis()
									- guardedRunStartTime;

							endTime = System.currentTimeMillis()
									- guardedRunStartTime;

							NaviLogUtil.debugEglDrawCostTime(count + ","
									+ startTime + "," + drawFrameTime + ","
									+ drawFrameEndTime + "," + doSendHuTime
									+ "," + doSendHuEndTime + "," + swapTime
									+ "," + swapEndTime + "," + endTime);
						} catch (Exception ex) {
							NaviLogUtil.error(NaviConstant.TAG_ERROR,
									"guardedRun: " + ex.getMessage());
						}
					}

					// }
					if (null != curWaiter) {
						curWaiter.countDown();
					}
					long tick = System.currentTimeMillis();
					if (tick - mLastTick < mMinInterval) {
						Thread.sleep(mMinInterval - (tick - mLastTick));
						tick = System.currentTimeMillis();
					}
					mLastTick = tick;
				}
			} catch (Throwable ex) {
				NaviLogUtil.error(NaviConstant.TAG_ERROR,
						"guardedRun: " + ex.getMessage());
			} finally {
				/*
				 * clean-up everything...
				 */
				synchronized (sGLThreadManager) {
					stopEglLocked();
				}
			}
		}

		private void handlerSizeChange(boolean sizechanged, int w, int h) {
			if (mEglSurface == null) {
				createEglSurface();

				if (mEglSurface != null) {
					NaviLogUtil.debugEglStep("onSurfaceCreated ");
					mRenderer.onSurfaceCreated(mGL, null);
				}
				NSTriggerInfo info = new NSTriggerInfo();
				info.m_iTriggerID = 1000000;
				MenuControlIF.Instance().TriggerForScreen(info);
			} else {
				if (sizechanged) {
					NaviLogUtil.debugEglStep("recreate EglSurface ");
					destroyEglSurface(mEglSurface);
					mEglSurface = null; // it is important
					createEglSurface();
				}
			}

			if (sizechanged) {
				AppLinkService service = AppLinkService.getInstance();
				if (isAppLinkReady(service)) {
					NaviLogUtil.debugEglStep(String.format(
							"onSurfaceChanged,w:%s,h:%s", 800, 480));
					// MapEngineJNI.setSurfaceSize(800, 480);
				} else {
					NaviLogUtil.debugEglStep(String.format(
							"onSurfaceChanged,w:%s,h:%s", w, h));
					mRenderer.onSurfaceChanged(mGL, w, h);
				}
			}
		}

		private boolean isAppLinkReady(AppLinkService service) {
			boolean isReady = (null != service && service.isRunning());
			if (isReady) {
				NaviLogUtil.debugEglStep("Applink is ready");
			} else {
				NaviLogUtil.debugEglStep("Applink is not ready");
			}
			return isReady;
		}

		private boolean sendToHU(int count, boolean sizechanged,
				boolean isConnectHU) {
			AppLinkService service = AppLinkService.getInstance();
			if (isAppLinkReady(service)) {
				if (isConnectHU) {
					if (!isSetHuSurfaceSize || sizechanged) {
						// MapEngineJNI.setSurfaceSize(800, 480);
						if (null != mediaSurface) {
							NaviLogUtil
									.debugEglStep("setSurfaceSize 800, 480...");
							bindMediaSurface();
							// EglHelper.getInstance().bindSurface(mediaSurface);
							// isSetHuSurfaceSize = true;
							// isSetPhoneSurfaceSize = false;
						} else {
							NaviLogUtil
									.debugEglStep("setSurfaceSize failed, because mediaSurface is null");
						}
					}
					// service.doSendHU(count);
				} else {
					isConnectHU = true;
					bindMEglSurface();
				}
			} else {
				NaviLogUtil.debugEglStep("service is null or not running");
				service.doSendHU(count);
				// to test, release must comment
				// service.doSendHU(count);
			}
			return isConnectHU;
		}

		private boolean isDone() {
			synchronized (sGLThreadManager) {
				return mDone;
			}
		}

		public void surfaceCreated() {
			if (HENIUS_DEBUG) {
				System.out.println("--henius surfaceview surface created");
			}
			synchronized (sGLThreadManager) {
				mHasSurface = true;
				sGLThreadManager.notifyAll();
			}
		}

		public void surfaceDestroyed() {
			if (HENIUS_DEBUG) {
				System.out.println("--henius surfaceview surface destroyed");
			}
			synchronized (sGLThreadManager) {
				mHasSurface = false;
				sGLThreadManager.notifyAll();
			}
		}

		public void onPause() {
			synchronized (sGLThreadManager) {
				mPaused = true;
				// mHasSurface = false;

				sGLThreadManager.notifyAll();
			}
		}

		public void onResume() {
			synchronized (sGLThreadManager) {
				mPaused = false;
				sGLThreadManager.notifyAll();
			}
		}

		public void onWindowResize(int w, int h) {
			CountDownLatch waitRefresh = new CountDownLatch(1);

			synchronized (sGLThreadManager) {
				boolean bIsInit = ((0 == mWidth) || (0 == mHeight));
				mWidth = w;
				mHeight = h;
				mSizeChanged = true;

				if ((!mPaused) && (mHasSurface) && (!bIsInit) && (!mDone)) {
					mWaiter = waitRefresh;
				} else {
					mWaiter = null;
					waitRefresh = null;
				}

				sGLThreadManager.notifyAll();
			}

			if (null != waitRefresh) {
				try {
					waitRefresh.await(mMinInterval * 10, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					NaviLogUtil.error(NaviConstant.TAG_ERROR,
							"eglview: " + e.getMessage());
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		}

		public void onInit() {
			synchronized (sGLThreadManager) {
				mDone = false;
				mPaused = true;
				mHasSurface = false;
				mWidth = 0;
				mHeight = 0;
				mSizeChanged = false;
				mWaiter = null;
				sGLThreadManager.notifyAll();
			}
			if (HENIUS_DEBUG) {
				System.out.println("--henius onInit end");
			}
		}

		public void requestExitAndWait() {
			// don't call this from GLThread thread or it is a guaranteed
			// deadlock!
			synchronized (sGLThreadManager) {
				mDone = true;
				sGLThreadManager.notifyAll();
			}
			try {
				join();
			} catch (InterruptedException ex) {
				NaviLogUtil.error(NaviConstant.TAG_ERROR,
						"eglview: " + ex.getMessage());
				Thread.currentThread().interrupt();
			}
		}

		private void createEglSurface() {
			if (NaviConstant.MODE_MEDIA_SURFACE == 1) {
				createEglSurfaceForOnlyShowOneSide();
			} else if (NaviConstant.MODE_MEDIA_SURFACE == 2) {
				createEglSurfaceForTwoSwapSurface();
			} else if (NaviConstant.MODE_MEDIA_SURFACE == 3) {
				createMediaSurface();
			}
		}

		EGLSurface mediaSurface = null;
		EGLSurface selfSurface = null;

		/**
		 * 切换显示，既有手机，也有车机地图显示.
		 */
		private void createEglSurfaceForTwoSwapSurface() {
			createSelfSurface();
			createMediaSurface();
			if (isMediaSurface) {
				mEglSurface = mediaSurface;
			} else {
				mEglSurface = selfSurface;
			}

			if (null != mEglSurface) {
				bindMEglSurface();
			}

			if ((null == mEglSurface) || (null == mGL)) {
				if (HENIUS_DEBUG) {
					System.out
							.println("Error!!!, failed to create egl surface");
				}
				// throw new
				// AndroidRuntimeException("failed to create egl surface");
			}
		}

		private void createMEglSurface() {
			if (null == mEglSurface) {
				try {
					mEglSurface = EglHelper.getInstance().createSurface(
							getHolder());
				} catch (Exception ex) {
					NaviLogUtil.error(
							NaviConstant.TAG_ERROR,
							"eglview when create mEglSurface: "
									+ ex.getMessage());
					destroyEglSurface(mEglSurface);
				}

			}
		}

		private void createSelfSurface() {
			if (null == selfSurface) {
				try {
					selfSurface = EglHelper.getInstance().createSurface(
							getHolder());
				} catch (Exception ex) {
					NaviLogUtil
							.error(NaviConstant.TAG_ERROR,
									"eglview when createEglSurface: "
											+ ex.getMessage());
					destroyEglSurface(selfSurface);
					selfSurface = null;
				}
			}
		}

		private void createMediaSurface() {
			if (null == mediaSurface) {
				try {
					mediaSurface = EglHelper.getInstance().createSurface(
							mediaCoderHelper.getSurface());
				} catch (Exception ex) {
					NaviLogUtil
							.error(NaviConstant.TAG_ERROR,
									"eglview when createEglSurface: "
											+ ex.getMessage());
					destroyEglSurface(mediaSurface);
					mediaSurface = null;
				}
			}
		}

		private void bindMEglSurface() {
			try {
				// mGL = (GL10) EglHelper.getInstance().bindSurface(
				// mEglSurface);
				mGL = (GL10) EglHelper.getInstance().bindSurface(mEglSurface,
						mEglSurface);
				MapEngineJNI.setSurfaceSize(mWidth, mHeight);
				isSetHuSurfaceSize = false;
				isSetPhoneSurfaceSize = true;
			} catch (Exception ex) {
				NaviLogUtil.error(
						NaviConstant.TAG_ERROR,
						"eglview when bindSurface mEglSurface: "
								+ ex.getMessage());
				destroyEglSurface(mEglSurface);
			}
		}

		private void bindMediaSurface() {
			if (null != mediaSurface) {
				try {
					EglHelper.getInstance().bindSurface(mediaSurface);
					MapEngineJNI.setSurfaceSize(800, 480);
					isSetHuSurfaceSize = true;
					isSetPhoneSurfaceSize = false;
				} catch (Exception ex) {
					NaviLogUtil.error(
							NaviConstant.TAG_ERROR,
							"eglview when bindSurface mediaSurface: "
									+ ex.getMessage());
				}
			}
		}

		private void createSurfaceForTest(int count) {
			createMEglSurface();
			// TODO 注意此处代码,不能写入mEglSurface的判断中.
			createMediaSurface();
			if (isFirstFrame) {
				if (null != mEglSurface) {
					bindMEglSurface();
					isFirstFrame = false;
				}
			} else {
				if (count < 5) {
					if (null != mEglSurface) {
						bindMEglSurface();
					}
				} else {
					if (null != mediaSurface) {
						bindMediaSurface();
					}
				}
			}

			if ((null == mEglSurface) || (null == mGL)) {
				if (HENIUS_DEBUG) {
					System.out
							.println("Error!!!, failed to create egl surface");
				}
				// throw new
				// AndroidRuntimeException("failed to create egl surface");
			}
		}

		/**
		 * 只显示车机或者手机地图
		 */
		private void createEglSurfaceForOnlyShowOneSide() {
			createMEglSurface();
			// TODO 注意此处代码,不能写入mEglSurface的判断中.
			createMediaSurface();
			// test for 0721
			// bindMediaSurface();
			// return;

			if (isFirstFrame) {
				if (null != mEglSurface) {
					bindMEglSurface();
					isFirstFrame = false;
				}
			} else {
				AppLinkService service = AppLinkService.getInstance();
				if (null == service || !service.isRunning()) {
					if (null != mEglSurface) {
						bindMEglSurface();
					}
				} else {
					if (null != mediaSurface) {
						bindMediaSurface();
					}
				}
			}

			if ((null == mEglSurface) || (null == mGL)) {
				if (HENIUS_DEBUG) {
					System.out
							.println("Error!!!, failed to create egl surface");
				}
			}
		}

		private void destroyEglSurface(EGLSurface eglSurface) {
			if (null != eglSurface) {
				try {
					EglHelper.getInstance().destroySurface(eglSurface);
				} catch (Exception ex) {
					NaviLogUtil.error(
							NaviConstant.TAG_ERROR,
							"eglview when destroyEglSurface: "
									+ ex.getMessage());
				}
				eglSurface = null;
			}
		}

		private void destroyEglSurface() {
			if (null != selfSurface) {
				try {
					EglHelper.getInstance().destroySurface(selfSurface);
				} catch (Exception ex) {
					NaviLogUtil.error(
							NaviConstant.TAG_ERROR,
							"eglview when destroyEglSurface: "
									+ ex.getMessage());
				}
				selfSurface = null;
			}

			if (null != mediaSurface) {
				try {
					EglHelper.getInstance().destroySurface(mediaSurface);
				} catch (Exception ex) {
					NaviLogUtil.error(
							NaviConstant.TAG_ERROR,
							"eglview when destroyEglSurface: "
									+ ex.getMessage());
				}
				mediaSurface = null;
			}

			if (null != mEglSurface) {
				try {
					EglHelper.getInstance().destroySurface(mEglSurface);
				} catch (Exception ex) {
					NaviLogUtil.error(
							NaviConstant.TAG_ERROR,
							"eglview when destroyEglSurface: "
									+ ex.getMessage());
				}
				mEglSurface = null;
			}
			mGL = null;
		}

		// Once the thread is started, all accesses to the following member
		// variables are protected by the sGLThreadManager monitor
		private boolean mEglInitialized;
		private boolean mDone;
		private boolean mPaused;
		private boolean mHasSurface;
		private int mWidth;
		private int mHeight;
		private boolean mSizeChanged;
		private CountDownLatch mWaiter;
		// End of member variables protected by the sGLThreadManager monitor.

		private final Renderer mRenderer;
		private EGLSurface mEglSurface;
		private GL10 mGL;

		private long mLastTick;

		private Bitmap bmp;
	}

	private static class GLThreadManager {

		public synchronized void threadExiting(GLThread thread) {
			if (LOG_THREADS) {
				Log.i("GLThread", "exiting tid=" + thread.getId());
			}
			thread.mDone = true;
			notifyAll();
		}
	}

	private static final GLThreadManager sGLThreadManager = new GLThreadManager();

}
