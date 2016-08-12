package com.billionav.navi.camera;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.billionav.DRIR.DRDM.DRIRVideoRecorder;
import com.billionav.DRIR.PictureHandler.PictureHandler;
import com.billionav.DRIR.jni.jniDRIR_CameraPreview;
import com.billionav.DRIR.jni.jniDRIR_CameraViewCtrl;
import com.billionav.DRIR.jni.jniDRIR_ImgDataServer;
import com.billionav.navi.uitools.SharedPreferenceData;

public class CameraView extends SurfaceView implements
		SurfaceHolder.Callback, Camera.PreviewCallback{
	
	private static final String TAG = "CameraView";
	private static final String ORIENTATION_PARAM_NAME = "orientation";
	private static final String ROTATION_PARAM_NAME = "rotation";
	private static final int DEFAULT_PRE_WIDTH = 640;
	private static final int DEFAULT_PRE_HEIGHT = 480;
	private static final int MAX_PREVIEW_FRAMERATE = 30;
	
	private static final int START_PREVIEW_FAILED = 0;
	private static final int START_PREVIEW_WAIT = 1;
	private static final int START_PREVIEW_SUCCESSFUL = 2;
	private static final int STOP_PREVIEW_SUCCESSFUL = 3;
	private static final int STOP_PREVIEW_FAILED = 4;
	
	private static final int ANDROID_3_0_API_LEVEL = 11;
	private static final int ANDROID_3_1_API_LEVEL = 12;
	private static final int ANDROID_3_2_API_LEVEL = 13;
	
	private final Handler mHandler = new MainHandler();
	private static final int MSG_STARTPREVIEW = 0;
	private static final int MSG_FIRSTMAX = 1;
	private static final int TURNNING_FRAMERATE_COUNT = 100;
	
	private Camera mCamera;
	private int mPreviewX;
	private int mPreviewY;
	private boolean m_bIsSurfaceChg = false;  // If surfaceChange function calling the flag set true
											  // other case the flag set false
	private boolean m_bIsSupportPre = false;
	private Timer mTimer;
	private byte[] mBuf;
	private DRIRVideoRecorder mRec = null;
	private boolean bIsInValidFrame = true;
	private boolean bIsFirstMax = true;
	
	private OrientationEventListener mOrientationListener;
	private int mLastOrientation = OrientationEventListener.ORIENTATION_UNKNOWN;
	
	private int mPreviewStatus = STOP_PREVIEW_SUCCESSFUL;
	private int mFrameRate = 1;
	private long timestamp=0;
	private int mLastTimer = -1;
	private int mGapCnt = 0;
	private int mGap = 0;
	private boolean mbIsMin = false;
	private int mCallbackCnt = 0;
	private static final int INVALID_FRAME_CNT = 2;

	public CameraView(final Context context) {
		super(context);
		setSurfaceType();
		mRec = new DRIRVideoRecorder();
		Thread loadServiceThread = new Thread(new Runnable() {
            public void run() {
                mOrientationListener = new OrientationEventListener(context) {
                    public void onOrientationChanged(int orientation) {
                        // We keep the last known orientation. So if the user
                        // first orient the camera then point the camera to
                        // floor/sky, we still have the correct orientation.
                        if (orientation != ORIENTATION_UNKNOWN)
                        {
                        	int iTempOrientation = 0;
              
                        	if (IsPad()) 
                        	{
                        		iTempOrientation = roundOrientation(orientation);
                        	}
                        	else
                        	{
                        		iTempOrientation = roundOrientation(orientation + 90);
                        	}
                        	
                        	if (mLastOrientation != iTempOrientation)
                        	{
                        		mLastOrientation = iTempOrientation;                       		
                        	}
                        }	
                            
                    }
                };
            }
        }, "loadServiceThread");
        loadServiceThread.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated...");
		m_bIsSurfaceChg = false;
		CameraSettings.instance().setPortrait(this.getWidth() < this.getHeight());		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "surfaceChanged w=" + width + " h=" + height);
		//setCameraParam();
		Log.d(TAG, "mPreviewX" + mPreviewX + " mPreviewY" + mPreviewY);

		jniDRIR_CameraViewCtrl.onSurfaceChange();
		m_bIsSurfaceChg = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surfaceChanged w= surface destory");
		m_bIsSurfaceChg = false;
		m_bIsSupportPre = false;
		
		jniDRIR_CameraViewCtrl.onSurfaceDestory();
		CameraClose();
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		TurnningFrameRate();
		//Log.e(TAG, "onPreviewFrame calling, X " + mPreviewX + " Y " + mPreviewY);
		if (false == bIsInValidFrame)
		{
			jniDRIR_ImgDataServer.CameraDataInput(data, data.length);
			jniDRIR_CameraPreview.NotifyDataUpdating();
			PictureHandler.getInstance().takePicture(mPreviewX, mPreviewY, 
					data, mLastOrientation);
			mCallbackCnt = 0;
		}
		else
		{
			++mCallbackCnt;
		}
		if (mCallbackCnt == INVALID_FRAME_CNT)
		{
			bIsInValidFrame = false;
		}
			 
	}
	
	public boolean StartPreview()
	{
		CameraOpen();
		int iRet = START_PREVIEW_FAILED;
		if (null != mCamera)
		{
			Log.i(TAG, "Start preview camera not null");
			if (m_bIsSurfaceChg)
			{
				Log.i(TAG, "Start preview surface change end");
				mFrameRate = SharedPreferenceData.DRIR_DEBUG_SETTING_DRID_FRAMERATE.getInt();
				setCameraParam();
				if (m_bIsSupportPre)
				{
					Log.i(TAG, "Start preview size support");
					try
					{	
						mCamera.setPreviewCallbackWithBuffer(this);
						mBuf = new byte[mPreviewX * mPreviewY * 3 / 2];
						mCamera.addCallbackBuffer(mBuf);
						mCamera.startPreview();
						ResetTurningFrameRate();
						mCallbackCnt = 0;
						mTimer = new Timer();
						mLastTimer = (200 / (mFrameRate + 1));
						mTimer.schedule(new MyTimerTask(), 200, mLastTimer);
						
						
						iRet = START_PREVIEW_SUCCESSFUL;
						bIsInValidFrame = true;
					}catch(RuntimeException e){
						mCamera.setPreviewCallbackWithBuffer(null);
						if (null != mBuf)
						{
							mBuf = null;
						}
						
						if (null != mTimer)
						{
							mTimer.cancel();
							mTimer = null;
						}						
						
						e.printStackTrace();
					}
				}
			}
			else
			{
				mHandler.sendEmptyMessageDelayed(MSG_STARTPREVIEW, 20);
				iRet = START_PREVIEW_WAIT; 
			}
		}
		else
		{
			Log.e(TAG, "Start preview failed---Camera pointer null");
		}
		jniDRIR_CameraViewCtrl.SetPreviewStatus(iRet);
		mPreviewStatus = iRet;
		if (START_PREVIEW_FAILED == iRet)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean StopPreview(int iFlag)
	{
		boolean bRet = false;
		switch (iFlag)
		{
		case START_PREVIEW_SUCCESSFUL:
			{
				Log.i(TAG, "StopPreview calling");
				mCamera.setPreviewCallbackWithBuffer(null);
				mCamera.stopPreview();
				mTimer.cancel();
				mTimer = null;
				mBuf = null;
				bRet = true;
			}
			break;
		case START_PREVIEW_WAIT:
			{
				mHandler.removeMessages(MSG_STARTPREVIEW);
				bRet = true;
			}
			break;
		default:
			break;
		}
		if (bRet)
		{
			mPreviewStatus = STOP_PREVIEW_SUCCESSFUL;
			jniDRIR_CameraViewCtrl.SetPreviewStatus(STOP_PREVIEW_SUCCESSFUL);
			CameraClose();
		}
		else
		{
			mPreviewStatus = STOP_PREVIEW_FAILED;
			jniDRIR_CameraViewCtrl.SetPreviewStatus(STOP_PREVIEW_FAILED);
			
		}
		return bRet;
	}
	
	public boolean StopPreview()
	{
		return StopPreview(mPreviewStatus);
	}
	
	public boolean StartHQRec()
	{	
		CameraOpen();
		boolean bRet = false;
		if ((null != mCamera)
				&& (null != mRec))
		{
			mRec.InitialLocalServer();
			mRec.setCamera(mCamera);
			mRec.setSurfaceHolder(this.getHolder());
			mRec.StartVideoRecording();
			bRet = true;
		}		
		return bRet;
	}
	
	public boolean StopHQRec()
	{
		boolean bRet = false;
		
		if (null != mRec)
		{
			Log.i(TAG, "Java StopHQRec...");
			mRec.StopVideoRecording();
			mRec.ReleaseLocalServer();
			bRet = true;
		}	
		CameraClose();
		return bRet;
	}

	private void setSurfaceType()
	{
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroy
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public void setCameraParam()
	{
		adjustPreviewSize();
		
		jniDRIR_CameraPreview.SetPreviewSize(mPreviewX, mPreviewY);
	
		if (null != mCamera)
		{
			CameraSettings.instance().setPortrait(this.getWidth() < this.getHeight());
			Camera.Parameters parameters = mCamera.getParameters();
			Log.d(TAG, "Camera parameters=[" + parameters.flatten() + "]");
			
			int iMaxExposure = parameters.getMaxExposureCompensation();
			jniDRIR_CameraPreview.SetMaxExposureCompensation(iMaxExposure);
			int iMinExposure = parameters.getMinExposureCompensation();
			jniDRIR_CameraPreview.SetMinExposureCompensation(iMinExposure);
			

			if (CameraSettings.instance().isPortrait())
			{
				parameters.set(ORIENTATION_PARAM_NAME, "portrait");
				parameters.set(ROTATION_PARAM_NAME, "90");
			}
			else
			{
				parameters.set(ORIENTATION_PARAM_NAME, "landscape");
				parameters.set(ROTATION_PARAM_NAME, "0");
			}
						
			if(Build.MODEL.equals("LG-P993"))
			{
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
			}
			else
			{
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
			}
			
			if (mbIsMin)
			{
				int iFrameRate = getMinSurportFrameRate(parameters);
				Log.i(TAG, "Set the preview frame rate " +  iFrameRate);
				
				parameters.setPreviewFrameRate( iFrameRate );
				
			}
			else
			{
				Log.i(TAG, "Set the preview frame rate " + MAX_PREVIEW_FRAMERATE);
				parameters.setPreviewFrameRate(MAX_PREVIEW_FRAMERATE);
			}			
			
			if (m_bIsSupportPre)
			{
				setPreviewSize(parameters);
			}
			
			//add by mapengwei for  ExposureCompensation adjust
			int iExposure = jniDRIR_CameraPreview.GetExposureCompensation();
			parameters.setExposureCompensation(iExposure);
			
			try {
				mCamera.setParameters(parameters);
			}catch (RuntimeException e) 
			{
				Log.i(TAG, "Eecpertion Occour");
				e.printStackTrace();
			}
			
			Camera.Parameters param = mCamera.getParameters();
			Log.i(TAG, "Actrual Preview Framerate" + param.getPreviewFrameRate());
			
			
		}
		else
		{
			Log.e(TAG, "setCameraParam pointer null");
		}
	}
	
	public void setExposure(){
		if ((null != mCamera)
				&& m_bIsSurfaceChg
				&& (START_PREVIEW_SUCCESSFUL == mPreviewStatus))
		{
			Camera.Parameters parameters = mCamera.getParameters();

			//add by mapengwei for  ExposureCompensation adjust
			int iExposure = jniDRIR_CameraPreview.GetExposureCompensation();
			int icurrentExposure = parameters.getExposureCompensation();
			if (icurrentExposure != iExposure){
				parameters.setExposureCompensation(iExposure);		
				try {
					mCamera.setParameters(parameters);
				}catch (RuntimeException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			Log.e(TAG, "setCameraParam pointer null");
		}
	}
	private void setPreviewSize(Camera.Parameters parameters){
		Log.d(TAG, "preview size: x=" + mPreviewX + ",y=" + mPreviewY);
		if(mPreviewX>mPreviewY){
			parameters.setPreviewSize(mPreviewX, mPreviewY);
		}
		else{
			parameters.setPreviewSize(mPreviewY, mPreviewX);
		}
		
	}
	
	private boolean adjustPreviewSize()
	{
		   m_bIsSupportPre = false;
		   if (null != mCamera)
		   {
			   List<Size> lst = mCamera.getParameters().getSupportedPreviewSizes();
			   Iterator<Size> i = lst.iterator();
			   Size cResult = mCamera.new Size(0, 0);
			   for(; i.hasNext();)
			   {
				    Size cTmp = i.next();
				    
				    if ((cTmp.width == DEFAULT_PRE_WIDTH)
				    		&& (cTmp.height == DEFAULT_PRE_HEIGHT))
				    {
				    	cResult = cTmp;
				    	m_bIsSupportPre = true;
				    	break;
				    }				    
				    else if ((cTmp.width > DEFAULT_PRE_WIDTH)
				    		&& (cTmp.height > DEFAULT_PRE_HEIGHT))
				    {
				    	if ((cResult.width == 0)
				    			&& (cResult.height == 0))
				    	{
				    		cResult = cTmp;
				    		m_bIsSupportPre = true;
				    	}
				    	else
				    	{
				    		if (cTmp.width * cTmp.height < cResult.width * cResult.height)
				    		{
				    			cResult = cTmp;
				    			m_bIsSupportPre = true;
				    		}
				    	}
				    }
				    else
				    {
				    	
				    } 
					
			   }
			   
			   if (m_bIsSupportPre)
			   {
				   mPreviewX = cResult.width;
				   mPreviewY = cResult.height;
			   }
		   }
		   
		   return m_bIsSupportPre;
		   
	}
	
	private static CameraView instance; 
	   
	public static CameraView createInstance(Context context){
		   if(instance == null){
			   instance = new CameraView(context);
			   instance.setId(1022); 
			   instance.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		   }
	       return instance;
	}
	       
	public static CameraView Instance() {
		return instance;
	}

	public void Maximize() {
		Log.w(TAG, "Maximize");
		
		if (bIsFirstMax)
		{
			Minimize();
			mHandler.sendEmptyMessageDelayed(MSG_FIRSTMAX, 20);
		}
		else
		{
			Maxprocess();
		}
		bIsFirstMax = false;
	}

	public void Maxprocess()
	{
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) instance
			.getLayoutParams();
		lp.width = FrameLayout.LayoutParams.FILL_PARENT;
		lp.height = FrameLayout.LayoutParams.FILL_PARENT;
		instance.setLayoutParams(lp);
		jniDRIR_CameraViewCtrl.SetMaxMode();
		
	}
	public void Minimize() {
		Log.w(TAG, "Minimize");
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) instance
				.getLayoutParams();
		lp.width = 1;
		lp.height = 1;
		instance.setLayoutParams(lp);
	}
	
	public void EnableOrientationChange()
	{
		if (null != mOrientationListener)
			mOrientationListener.enable();
	}
	
	public void DiableOrientationChange()
	{
		if (null != mOrientationListener)
			mOrientationListener.disable();
		mLastOrientation = -1;
	}
	
	
	private class MainHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what)
			{
			case MSG_STARTPREVIEW:
				jniDRIR_CameraViewCtrl.StartPreviewNative();
				break;
			case MSG_FIRSTMAX:
				Maxprocess();
				break;
			default:
				super.handleMessage(msg);
				break;
			}
			
		}
	}
	
	private class MyTimerTask extends TimerTask
	{

		@Override
		public void run() {
			if (null != mCamera)
			{
				mCamera.addCallbackBuffer(mBuf);
			}
		}
		
	}
	
    public int roundOrientation(int orientationInput) {
        int orientation = orientationInput;
        if (orientation == -1)
            orientation = 0;

        orientation = orientation % 360;
        int retVal;
        if (orientation < (0*90) + 45) {
            retVal = 0;
        } else if (orientation < (1*90) + 45) {
            retVal = 90;
        } else if (orientation < (2*90) + 45) {
            retVal = 180;
        } else if (orientation < (3*90) + 45) {
            retVal = 270;
        } else {
            retVal = 0;
        }
        return retVal;
    }

	public boolean isM_bIsSurfaceChg() {
		return m_bIsSurfaceChg;
	}
	
	public void setMinStatus(boolean bIsMin)
	{
		mbIsMin = bIsMin;
	}
	
	private void CameraOpen()
	{
		if (null == mCamera)
		{
			try {
				mCamera = Camera.open();
				mCamera.setPreviewDisplay(getHolder());
			} catch (IOException e) {
				mCamera.release();
				mCamera = null;
				e.printStackTrace();
			} catch (RuntimeException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void CameraClose()
	{
		if (null != mCamera)
		{
			mCamera.release();
			mCamera = null;
		}
		else
		{
			Log.e(TAG, "Camera instance null");
		}
	}
	
	private void TurnningFrameRate()
	{
		long tempTick = System.currentTimeMillis();
		int iGap = (int) (tempTick-timestamp);
		Log.i("PreviewGap", "Preview gap is " + iGap);
		timestamp=tempTick;
		
		if (TURNNING_FRAMERATE_COUNT == mGapCnt)
		{
			int iTimerGap = (200 / (mFrameRate + 1)) * (200 / (mFrameRate + 1)) * mGapCnt / mGap;
			
			if ((null != mTimer)
					&& (iTimerGap != mLastTimer))
			{
				mLastTimer = iTimerGap;
				mTimer.cancel();
				mTimer = null;
				mTimer = new Timer();
				mTimer.schedule(new MyTimerTask(), 0, mLastTimer);
			}
			ResetTurningFrameRate();
			return;
		}
	    if (!bIsInValidFrame)
	    {
	    	++mGapCnt;
	    	mGap = (int) (mGap + iGap);
	    }
	    
	}
	
	private void ResetTurningFrameRate()
	{
		mGap = 0;
		mGapCnt = 0;
	}
	
	private boolean IsPad()
	{
		boolean bRet = false;
		if ((ANDROID_3_0_API_LEVEL == android.os.Build.VERSION.SDK_INT)
				|| (ANDROID_3_1_API_LEVEL == android.os.Build.VERSION.SDK_INT)
				|| (ANDROID_3_2_API_LEVEL == android.os.Build.VERSION.SDK_INT))
		{
			bRet = true;
		}
		return bRet;
	}

	
	private int getMinSurportFrameRate(Camera.Parameters params)
	{
		int iRet = (mFrameRate + 1) * 6;
		
		if (null != params)
		{
			List<Integer> listFrameRate = params.getSupportedPreviewFrameRates();
			Iterator<Integer> i = listFrameRate.iterator();
			for(; i.hasNext();)
			{
				int iTemp = i.next();
				if (iTemp >= iRet)
				{
					iRet = iTemp;
					break;
				}
			}
		}
		
		return iRet;
	}
	
}
