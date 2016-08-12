package com.billionav.DRIR.PictureHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.billionav.DRIR.jni.jniDRIR_CameraViewCtrl;
import com.billionav.DRIR.jni.jniDRIR_Location;
import com.billionav.DRIR.jni.jniDRIR_MainControl;
import com.billionav.jni.FileSystemJNI;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;

class PicHandler extends Handler {
	public static final int DR_TAKE_BEFORE = 0;
	public static final int ONE_SHOOT_TAKE = 1;
	public static final int DR_TAKE_AFTER = 2;
	public PicHandler() {

	}

	public PicHandler(Looper looper) {
		super(looper);
	}
	
	@Override
	public void handleMessage(Message msg) {
		
//		Log.i("UPLOAD", "processThread()-->"+Thread.currentThread().getName()); 
		Bundle b = msg.getData();
		
		int iTakeType = b.getInt("TakePic");
		
		switch (iTakeType)
		{
		case DR_TAKE_BEFORE:
			PictureHandler.getInstance().postTakeRequest();
			break;
		case ONE_SHOOT_TAKE:
			PictureHandler.getInstance().postOnShootRequest();
			break;
		case DR_TAKE_AFTER:
			PictureHandler.getInstance().takeAfterPic(b.getInt("StartFlag"), b.getInt("PicType"));
			break;
		default:
			break;
		}
	}

}

public class PictureHandler {
	public static int DR_TAKEPIC_BEFORE = 0;
	public static int VOICE_TAKEPIC = 1;	
	public static int MAP_TAKEPIC = 2;
	public static int DR_TAKEPIC_AFTER = 3;
	
	private HandlerThread mHandlerthread = null;
	private PicHandler mHandler = null;
	
	private final int BUFF_NUM = 1;
	private byte m_imageLastBuf[][];
	private byte m_imageCurBuf[];
	private int m_imageIndex = 0;
//	private Handler mHandler = new Handler();
	private int m_iPreviewWidth = 0;
	private int m_iPreviewHeight = 0;
	private boolean m_bMakingPic = false;
	private boolean m_bOneShootMaking = false;
	private static PictureHandler sInstance;
	private boolean m_bIsTakePic = false;
	private boolean m_bIsOneShoot = false;
	private int m_iFrameCnt = 0;
	private int m_iStartIndex = -1;
	private int m_iStartFlag = -1;
	private int m_iTakePicType = -1;
	private int mLastRotate = 0;
	public final static String DRIR = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH);
	private static final int FRAMECNT_GAP = 30;
	private static final int TAKEPIC_SPEEDUP = 0;
	private static final int TAKEPIC_SPEEDDOWN = 1;
	private static final int TAKEPIC_TRUN = 2;
	private static final String SPEEDUP_PRDFIX = "SPEEDUP_";
	private static final String SPEEDDOWN_PRDFIX = "SPEEDDOWN_";
	private static final String TRUN_PRDFIX = "TRUN_";
	private static final String IMG_PRDFIX = "IMG_";
	private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
	private static final boolean PICDEBUG = false;
	
	public static PictureHandler getInstance()
	{
		if (null == sInstance)
		{
			sInstance = new PictureHandler();
		}
		return sInstance;
	}
	public void setTakePicFlag(boolean bIsTakePic, int iStartFlag, long lTakePicType) {
		if (-1 == this.m_iStartFlag)
		{
			this.m_bIsTakePic = bIsTakePic;
			this.m_iStartFlag = iStartFlag;
			this.m_iTakePicType = (int) lTakePicType;
		}		
	}
	
	public void setOneShootFlag(boolean bIsOneShoot, int iStartFlag, long lTakePicType) {
		this.m_bIsOneShoot = bIsOneShoot;
		this.m_iStartFlag = iStartFlag;
		this.m_iTakePicType = (int) lTakePicType;
	}


	private void Init(int iWidth, int iHeight){
		
		if((iWidth != m_iPreviewWidth)
				|| (iHeight != m_iPreviewHeight)){
			if (null == mHandlerthread)
			{
				mHandlerthread = new HandlerThread("PicThread");
				mHandlerthread.start();
				mHandler =  new PicHandler(mHandlerthread.getLooper());
			}	
			m_iPreviewWidth = iWidth;
			m_iPreviewHeight = iHeight;
			m_bMakingPic = false;
			m_imageLastBuf = null;
//			m_imageLastBuf = new byte[BUFF_NUM][m_iPreviewWidth * m_iPreviewHeight *3 / 2];
			try{
				m_imageLastBuf = new byte[BUFF_NUM][m_iPreviewWidth * m_iPreviewHeight *3 / 2];
		    }
		    catch (OutOfMemoryError e)
		    {
		    	e.printStackTrace();
		    	return;
		    }
			m_imageCurBuf = null;
//			m_imageCurBuf = new byte[m_iPreviewWidth * m_iPreviewHeight *3 / 2];
			try{
				m_imageCurBuf = new byte[m_iPreviewWidth * m_iPreviewHeight *3 / 2];
		    }
		    catch (OutOfMemoryError e)
		    {
		    	e.printStackTrace();
		    	return;
		    }
		}
	}
	
	public void DeInit()
	{
		if (null != mHandlerthread)
		{
			mHandlerthread.quit();
			mHandlerthread = null;
		}
		
		mHandler = null;
		m_imageLastBuf = null;
		m_imageCurBuf = null;
	}
	
	private void setRawData(byte image[]){
		setLastBuf(image);
		setCurBuf(image);
	}
	
	private void makePicFile(byte image[], int iStartFlag, int iPicType){
		// to do 
		FileOutputStream outStream = null;   
		Log.i("CameraView", "makePicFile function calling..." + "m_iPreviewWidth " + m_iPreviewWidth
				+ " m_iPreviewHeight " + m_iPreviewHeight); 
		try{
			YuvImage yuvimage = new YuvImage(image, ImageFormat.NV21, m_iPreviewWidth, m_iPreviewHeight, null);   
			ByteArrayOutputStream baos = new ByteArrayOutputStream();   
			yuvimage.compressToJpeg(new Rect(0, 0, m_iPreviewWidth, m_iPreviewHeight), 80, baos);   
			
			if (0 != mLastRotate)
			{
				Bitmap bmp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
				Matrix matrix = new Matrix();
				matrix.setRotate(mLastRotate);
				Bitmap rBmp = Bitmap.createBitmap(bmp,                
						0, 0, bmp.getWidth(),  bmp.getHeight(), 
						matrix, true);
				baos.reset();
				rBmp.compress(CompressFormat.JPEG, 100, baos); 
			}		

			
			File rootDir = new File(DRIR + jniDRIR_MainControl.DRIRGetDrirRootPath());
			Log.i("CameraView", "Root path: " + rootDir.getAbsolutePath());
			if (!rootDir.exists())
			{
				rootDir.mkdir();
			}
			String sPicPath = DRIR + jniDRIR_MainControl.DRIRGetPicFilePath();
			Log.i("CameraView", "Pic path: " + sPicPath);
			File dir = new File(sPicPath);
			if(!dir.exists())
			{
				dir.mkdir();
			}
			
			//File cPicFile = new File(String.format((sPicPath + "%d.jpg"), System.currentTimeMillis()));
			//if (false == cPicFile.exists())
			//{
				//cPicFile.createNewFile();
			//}
			
			if ((iStartFlag == DR_TAKEPIC_BEFORE)
					|| (iStartFlag == DR_TAKEPIC_AFTER))
			{
				switch (iPicType)
				{
				case TAKEPIC_SPEEDUP:
					sPicPath += SPEEDUP_PRDFIX;
					break;
				case TAKEPIC_SPEEDDOWN:
					sPicPath += SPEEDDOWN_PRDFIX;
					break;
				case TAKEPIC_TRUN:
					sPicPath += TRUN_PRDFIX;
					break;
				default:
					break;
				}
			}
			else
			{
				sPicPath += IMG_PRDFIX;
			}
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			String strDate = dateFormat.format(date);
			String cFilePath = String.format((sPicPath + "%s.jpg"), strDate);
			outStream = new FileOutputStream(cFilePath);
			outStream.write(baos.toByteArray());
			outStream.close();
			AddPicAddition(cFilePath);
			if (iStartFlag == VOICE_TAKEPIC)
			{
				jniDRIR_MainControl.DRIRNotifyPicFilePath(cFilePath, cFilePath.length(), iPicType);
			}
			else if (iStartFlag == MAP_TAKEPIC)
			{
				NSTriggerInfo cTriggerInfo = new NSTriggerInfo();
				cTriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_DRIR_TAKEPIC_END;
				cTriggerInfo.m_String1 = cFilePath;
				MenuControlIF.Instance().TriggerForScreen(cTriggerInfo);
				
			}
			else if (iStartFlag == DR_TAKEPIC_BEFORE)
			{
				//Do nothing
			}
				
			jniDRIR_CameraViewCtrl.PlayShootSound();
			//Log.d("Picture", "Pic Absolute Path: " + cPicFile.getAbsolutePath() + "; length: " + cPicFile.getAbsolutePath().length());
			//Log.d("Picture", "makePicFile - wrote bytes: " + image.length); 
		}catch (FileNotFoundException e) {
			try {
				outStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			try {
				outStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	

	public void postTakeRequest(){
	            if (DR_TAKEPIC_BEFORE == m_iStartFlag)
	            {
	            	if (m_iStartIndex >= m_imageIndex)
	            	{
	            		int i = 0;
	            		for (i = m_iStartIndex; i < BUFF_NUM; i++)
		            	{
	            			//Log.i("PIC", "Make picture buffer index: " + i);	
	            			makePicFile(m_imageLastBuf[i], m_iStartFlag, m_iTakePicType);
		            	}
	            		
	            		for (i = 0; i < m_iStartIndex; i++)
	            		{
	            			//Log.i("PIC", "Make picture buffer index: " + i);	
	            			makePicFile(m_imageLastBuf[i], m_iStartFlag, m_iTakePicType);
	            		}
	            	}
	            	else
	            	{
	            		for (int i = 0; i < m_imageIndex; i++)
	            		{
	            			//Log.i("PIC", "Make picture buffer index: " + i);	
	            			makePicFile(m_imageLastBuf[i], m_iStartFlag, m_iTakePicType);
	            		}
	            	
	            	}
	            	
	            	Message msg = mHandler.obtainMessage();
	    			Bundle b = new Bundle();
	    			b.putInt("TakePic", PicHandler.DR_TAKE_AFTER);
	    			b.putInt("StartFlag", DR_TAKEPIC_AFTER);
	    			b.putInt("PicType", m_iTakePicType);
	    			msg.setData(b);
	    			mHandler.sendMessageDelayed(msg, 3 * 1000);
	            }
	            else if (DR_TAKEPIC_AFTER == m_iStartFlag)
	            {
	            	makePicFile(m_imageCurBuf, m_iStartFlag, m_iTakePicType);
	            }
	            m_iFrameCnt = 0;
	            m_imageIndex = 0;
	            m_iStartIndex = -1;
	            m_bMakingPic = false;
	            m_bIsTakePic = false;
	            m_iStartFlag = -1;
	            m_iTakePicType = -1;
	}
	
	public void postOnShootRequest()
	{		
		makePicFile(m_imageCurBuf, m_iStartFlag, m_iTakePicType);
		m_bIsOneShoot = false;
		m_bOneShootMaking = false;
		m_iStartFlag = -1;
		m_iTakePicType = -1;
	}
	
	public void takeAfterPic(int iStartFlag, int iPicType)
	{
		//if (1 == jniDRIR_CameraViewCtrl.GetCradleStatus())
		{
		   /*makePicFile(m_imageCurBuf, iStartFlag, iPicType );*/
			setTakePicFlag(true, iStartFlag, iPicType);
		} 
	}
	
	public void takePicture(int iWidth, int iHeight, byte image[], int iRotate){
		Init(iWidth, iHeight);
		setRawData(image);
		mLastRotate = iRotate;
		if ((m_bIsTakePic)
				&& (false == m_bMakingPic))
		{
			m_bMakingPic = true;
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt("TakePic", PicHandler.DR_TAKE_BEFORE);
			msg.setData(b);
			// send msg to PicHandler
			msg.sendToTarget();
		}
		
		if ((m_bIsOneShoot)
				&& (false == m_bOneShootMaking))
		{
			m_bOneShootMaking = true;
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt("TakePic", PicHandler.ONE_SHOOT_TAKE);
			msg.setData(b);
			// send msg to PicHandler
			msg.sendToTarget();
		}
	}
	
	private void AddPicAddition(String fileName)
	{
		ExifInterface cInExif;
		try {
			cInExif = new ExifInterface(fileName);
	        String   strDate   =   Long.toString(System.currentTimeMillis());  
	        Log.i("PictureHandler", "Date :" + strDate);
	        //String outStr1 = cInExitf.getAttribute(ExifInterface.TAG_DATETIME);
	        cInExif.setAttribute(ExifInterface.TAG_DATETIME, strDate);
	        long[] lLonLat = new long[2];
	        jniDRIR_Location.GetLonLat(lLonLat);
	        cInExif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, Long.toString(lLonLat[0]));
	        cInExif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, Long.toString(lLonLat[1]));
	        cInExif.saveAttributes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        /*ExifInterface cOutExitf;
		try {
			cOutExitf = new ExifInterface(fileName);
			String outStr = cOutExitf.getAttribute(ExifInterface.TAG_DATETIME);
	        Log.i("PictureHandler", "Out Date :" + outStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private void setLastBuf(byte image[])
	{
		if(m_bMakingPic){
			return;
		}
		++m_iFrameCnt;
		
		if (0 != (m_iFrameCnt - 1) % FRAMECNT_GAP)
		{
			return;
		}
		
		m_iFrameCnt = 1;
		
		if (m_imageIndex >= BUFF_NUM){
			m_iStartIndex = 1;
			m_imageIndex = m_imageIndex%BUFF_NUM;
		}
		else
		{
			if (m_iStartIndex >= m_imageIndex)
			{
				++m_iStartIndex;
				if (m_iStartIndex == BUFF_NUM)
				{
					m_iStartIndex %= BUFF_NUM;
				}
			}
		}		
		
		if (PICDEBUG)
		{
			Log.i("CameraView", "image buffer save time:" + System.currentTimeMillis() + "Image buff index: " + m_imageIndex
					+	"Start index" + m_iStartIndex);
		}		
		System.arraycopy(image, 0, m_imageLastBuf[m_imageIndex++], 0, image.length);
		/*m_imageLastBuf[m_imageIndex++] = image.clone();*/
	}
	
	private void setCurBuf(byte image[])
	{
		if ((false == m_bIsTakePic)
				&& (false == m_bIsOneShoot))
		{
			return;
		}
			
		if((true == m_bMakingPic)
				|| (m_bOneShootMaking)){
			return;
		}
		System.arraycopy(image, 0, m_imageCurBuf, 0, image.length);
		/*m_imageCurBuf = image.clone();*/
	}
}
