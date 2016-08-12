package com.billionav.DRIR.DRDM;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;
import android.view.SurfaceHolder;

import com.billionav.DRIR.jni.jniDRIR_CameraPreview;
import com.billionav.DRIR.jni.jniDRIR_CameraViewCtrl;
import com.billionav.DRIR.jni.jniDRIR_FrameManager;
import com.billionav.DRIR.jni.jniDRIR_MainControl;
import com.billionav.jni.jniFileSystem;
import com.billionav.navi.uitools.SharedPreferenceData;

/**
 * @author Administrator
 *
 */
public class DRIRVideoRecorder implements OnInfoListener, OnErrorListener {
	private static final int VIDEO_ENCODER_TYPE = MediaRecorder.VideoEncoder.H264;
	private static final String TAG = "DRIRVideoRecorder";
	private static final int SOCKET_BUFFERSIZE = 300 * 1024;
	private static final int VIDEO_FRAMERATE = 15;
	private static final int FRAME_SIZE = 1024;
	private static final int BUFFER_SIZE = FRAME_SIZE * 640;
	private static final int HEADER_BUF_SIZE = 100;
	//private static final String fd = jniFileSystem.instance().getSystemPath(jniFileSystem.FILE_USER_PATH) +
		//						"SPSPPS.dat";
	private static final String fd = new  String("/sdcard/USER/SPSPPS.dat");
	
	LocalSocket m_cReceiver;
	LocalSocket m_cSender;
	LocalServerSocket m_cLocalServer;

	private MediaRecorder m_cMediaRecorder = null;
	boolean m_bMediaRecording = false;
	private SurfaceHolder m_cSurfaceHolder = null;
	private Camera m_cCamera = null;
	Thread m_cReceiveThread;
	private byte[] SPS = null;
	private byte[] PPS = null;
	private int mMdatStartIndex = 0;
	private int mLastResolution = -1;
	private int mVideoWidth = 640;
	private int mVideoHeight = 480;
	private boolean mSkipHeader = false;

	//Temp for the debug
	RandomAccessFile raf = null;

	public void setSurfaceHolder(SurfaceHolder m_cSurfaceHolder) {
		this.m_cSurfaceHolder = m_cSurfaceHolder;
	}

	public void setCamera(Camera m_cCamera) {
		
		this.m_cCamera = m_cCamera;
	}

	/**		Calling when the activity onCreate			*/
	public void InitialLocalServer()
	{
		//Initialize the socket
		m_cReceiver = new LocalSocket();
		
		try {
			m_cLocalServer = new LocalServerSocket(TAG);
			m_cReceiver.connect(new LocalSocketAddress(TAG));
			m_cReceiver.setReceiveBufferSize(SOCKET_BUFFERSIZE);
			m_cReceiver.setSendBufferSize(SOCKET_BUFFERSIZE);
			m_cSender = m_cLocalServer.accept();
			m_cSender.setReceiveBufferSize(SOCKET_BUFFERSIZE);
			m_cSender.setSendBufferSize(SOCKET_BUFFERSIZE);
		} catch (IOException e) {
			Log.e(TAG, "Initialize local socket failed");
			e.printStackTrace();
		}
	}
	
	public void ReleaseLocalServer()
	{
		try {
			if (null != m_cReceiver)
			{
				m_cReceiver.close();
				m_cReceiver = null;
			}
			
			if (null != m_cSender)
			{
				m_cSender.close();
				m_cSender = null;
			}
			
			if (null != m_cLocalServer)
			{
				m_cLocalServer.close();
				m_cLocalServer = null;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean InitalMediaRecorder()
	{
		boolean bRet = false;

		if ((null == m_cSurfaceHolder)
			|| (null == m_cCamera))
		{
			return bRet;
		}

		m_bMediaRecording = true;

		//If media recorder not alloc
		if (null == m_cMediaRecorder)
		{
			m_cMediaRecorder = new MediaRecorder();
		}
		//Else
		else
		{
			m_cMediaRecorder.reset();
		}//EndIf
		//Log.i("DRIRVideoRecorder", "Before setting " + m_cCamera.getParameters().getFocusMode());
		Camera.Parameters param = m_cCamera.getParameters();
		param.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
		int iExposure = jniDRIR_CameraPreview.GetExposureCompensation();
		int icurrentExposure = param.getExposureCompensation();
		
		if (icurrentExposure != iExposure){
			param.setExposureCompensation(iExposure);		
		}
		try{
			m_cCamera.setParameters(param);
		}catch(RuntimeException e)
		{
			e.printStackTrace();
		}
		
		//Log.i("DRIRVideoRecorder", "After setting " + m_cCamera.getParameters().getFocusMode());
		m_cCamera.unlock();
		CamcorderProfile cProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
		m_cMediaRecorder.setCamera(m_cCamera);
		m_cMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		m_cMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		m_cMediaRecorder.setVideoFrameRate(VIDEO_FRAMERATE);
		GetVideoSize();
		m_cMediaRecorder.setVideoSize(mVideoWidth, mVideoHeight);
		jniDRIR_FrameManager.SetVideoWidth(mVideoWidth);
		jniDRIR_FrameManager.SetVideoHeight(mVideoHeight);
		Log.i("CameraView", "Height" + cProfile.videoFrameHeight + " width " + cProfile.videoFrameWidth);
		
		m_cMediaRecorder.setVideoEncodingBitRate(cProfile.videoBitRate);
		m_cMediaRecorder.setVideoEncoder(VIDEO_ENCODER_TYPE);
		m_cMediaRecorder.setPreviewDisplay(m_cSurfaceHolder.getSurface());
		if (null == SPS)
		{
			m_cMediaRecorder.setOutputFile(fd);
		}
		else
		{
			m_cMediaRecorder.setOutputFile(m_cSender.getFileDescriptor());
		}
   
		try { 
			m_cMediaRecorder.setOnInfoListener(this);
			m_cMediaRecorder.setOnErrorListener(this);
			m_cMediaRecorder.prepare();
			m_cMediaRecorder.start();  
			bRet = true;  
		} catch (IllegalStateException e) {
			ReleaseMediaRecorder();
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			ReleaseMediaRecorder();
			e.printStackTrace();
			return false;
		} catch (RuntimeException e)
		{
			ReleaseMediaRecorder();
			e.printStackTrace();
			return false;
		}
		return bRet;
	}

	public void ReleaseMediaRecorder()
	{
		if (null != m_cMediaRecorder)
		{
			Log.i("CameraView", "ReleaseMediaRecorder enter");
			if (m_bMediaRecording)
			{
				try{
					m_cMediaRecorder.setOnErrorListener(null);
					m_cMediaRecorder.setOnInfoListener(null);
					m_cMediaRecorder.stop();
				}catch (RuntimeException e){

				}
				m_bMediaRecording = false;
			}

			m_cMediaRecorder.reset();
			m_cMediaRecorder.release();
			m_cMediaRecorder = null;
			try {
				m_cCamera.reconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			}
			
			Log.i("CameraView", "ReleaseMediaRecorder end");
		}
	}

	public void StartVideoRecording()
	{
		InitalMediaRecorder();
		(m_cReceiveThread = new Thread(){
			public void run(){
				//byte[] bBuffer = new byte[BUFFER_SIZE];
				byte[] bBuffer = null;
			    try{
			    	bBuffer = new byte[BUFFER_SIZE];
			    }
			    catch (OutOfMemoryError e)
			    {
			    	e.printStackTrace();
			    	return;
			    }
				byte[] bTempHeaderBuf = null;
				int iPerReadSize = 0;
				int iReadCnt = 0;
				InputStream cFis = null;
				try {
					cFis = m_cReceiver.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				/*try {
					File cFile = new File("/sdcard/DRDM.h264");

					if (cFile.exists())
					{
						cFile.delete();
					}
					raf = new RandomAccessFile(cFile, "rw");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				
				if (SPS == null)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ReleaseMediaRecorder();
					
					/*try {
						raf.write(bBuffer, 0, number);
						raf.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
					FindSpsPps();
					
					InitalMediaRecorder();
					//Log.i("DRIRVideoRecorder", "The SPS buffe len " + number);
				}
				
				
				DataInputStream cDis = new DataInputStream(cFis);

				//Skip the header
				try {
					bTempHeaderBuf = new byte[HEADER_BUF_SIZE];
					int iRet = cDis.read(bTempHeaderBuf, 0, HEADER_BUF_SIZE);
					while(iRet != HEADER_BUF_SIZE)
					{
						iRet += cDis.read(bTempHeaderBuf, iRet, HEADER_BUF_SIZE - iRet);
					}
					Log.e("DRIRVideoRecorder", "Read len: +" + iRet);
					getMdatIndex(bTempHeaderBuf, HEADER_BUF_SIZE);
					mSkipHeader = true;
				} catch (IOException e) {
					try {
						cDis.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				jniDRIR_FrameManager.Start();
				jniDRIR_FrameManager.SetSpsPps(SPS, SPS.length, PPS, PPS.length);
   
				jniDRIR_CameraViewCtrl.NotifyHQStartResult(true);
				while(true)
				{
					try {
						if (null == m_cLocalServer)
						{
							break;
						}
						int iH264Len = 0;
						if (mSkipHeader)
						{
							byte[] bTemp = new byte[4];
							int i = 1;
							while(true)
							{
								int iIndex = mMdatStartIndex + (i - 1)*4;
								System.arraycopy(bTempHeaderBuf, iIndex, bTemp, 0, 4);
								//Log.i("DRIRVideoRecorder", "Src " + bTempHeaderBuf[iIndex] + " " + bTempHeaderBuf[iIndex + 1]
								//	+ " " + bTempHeaderBuf[iIndex + 2] + " " + bTempHeaderBuf[iIndex + 3]);
								iH264Len = bytes2Int(bTemp);
								if ((iH264Len <=0)
										|| (iH264Len > 500 * FRAME_SIZE))
								{
									++i;
								}
								else
								{
									break;
								}
							}
							
							bTemp = null;
							iReadCnt = HEADER_BUF_SIZE - mMdatStartIndex - (4 * i);
							System.arraycopy(bTempHeaderBuf, mMdatStartIndex + 4, bBuffer, 0, iReadCnt);
							bTempHeaderBuf = null;
							mSkipHeader = false;
						}
						else
						{
							iH264Len = cDis.readInt();
							iReadCnt = 0;
						}
						//Log.i("HQREC", "Read count " + iReadCnt);
						//Log.i("HQREC", "Receive the frame len " + iH264Len);
						//raf.writeInt(iH264Len);
						while (iReadCnt < iH264Len) 
						{
							int iLost = iH264Len - iReadCnt;
							iPerReadSize = cFis.read(bBuffer, iReadCnt, (FRAME_SIZE < iLost ? FRAME_SIZE : iLost));
							iReadCnt += iPerReadSize;
						}    
						
						//Write into the video encoder buffer
						//raf.write(bBuffer, 0, iReadCnt);
						jniDRIR_FrameManager.AddFrameVideoData(bBuffer, iH264Len);
						//jniDRIR_FrameManager.AddFrameOptionData(bBuffer, 78);
						//jniDRIR_FrameManager.AddFrameAudioData(bBuffer, 26);
					} catch (IOException e) {    
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	} 

	public void StopVideoRecording()
	{
		if (m_bMediaRecording
			|| (null != m_cMediaRecorder))
		{
			if (null != m_cReceiveThread)
			{
				m_cReceiveThread.interrupt();
			}
			ReleaseMediaRecorder();
		}
		 
		jniDRIR_FrameManager.Stop();
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		// TODO Auto-generated method stub

	}
	
	private void FindSpsPps()
	{
		File file = new File (fd);
		FileInputStream fileInput;
		try {
			fileInput = new FileInputStream (file);
			int length = (int)file.length();
//			byte[] data = new byte[length];
			byte[] data = null;
			try{
				data = new byte[length];
			}
			catch (OutOfMemoryError e)
		    {
				fileInput.close();
		    	e.printStackTrace();
		    	return;
		    }
			fileInput.read(data);
			getSpsPps(data, length);
			fileInput.close();
			if (file.exists())
			{
				file.delete();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	private void getMdatIndex(byte[] bBuf, int iLen)
	{
		final byte[] mdat = new byte[]{0x6D,0x64,0x61,0x74};
		//Find the mdat start index
		for (int i = 0; i < iLen; ++i)
		{
			if ((bBuf[i] == mdat[0]) && (bBuf[i + 1] == mdat[1])
					&& (bBuf[i + 2] == mdat[2]) && (bBuf[i + 3] == mdat[3]))
			{
				mMdatStartIndex = i + 4;
				break;
			}
		}
		Log.e("DRIRVideoRecorder", "StartMdatPlace:"+mMdatStartIndex);
	}
	
	private void getSpsPps(byte[] bBuf, int iLen)
	{
		
		final byte[] avcc = new byte[]{0x61,0x76,0x63,0x43};		
		
		//Find the Sps and pps
		for (int i = iLen - 1; i >= 3; --i)
		{
			if ((bBuf[i] == avcc[3]) && (bBuf[i - 1] == avcc[2])
					&& (bBuf[i - 2] == avcc[1]) && (bBuf[i - 3] == avcc[0]))
			{
				int sps_start = i+7;
				
				byte[] sps_3gp = new byte[2];
				sps_3gp[1] = bBuf[sps_start];
				sps_3gp[0] = bBuf[sps_start+1];
				
				sps_start+=2;//skip length
				
				int sps_length = bytes2short(sps_3gp);
				Log.e("DRIRVideoRecorder", "SPS LENGTH:"+sps_length);
				
				//SPS=new byte[sps_length];
				try{
					SPS=new byte[sps_length];
				}
				catch (OutOfMemoryError e)
			    {
			    	e.printStackTrace();
			    	return;
			    }
				System.arraycopy(bBuf, sps_start, SPS, 0, sps_length);
				
				int pps_start = sps_start+sps_length+1;
				byte[] pps_3gp =new byte[2];
				pps_3gp[1] = bBuf[pps_start];
				pps_3gp[0] =bBuf[pps_start+1];
				int pps_length = bytes2short(pps_3gp);
				Log.e("DRIRVideoRecorder", "PPS LENGTH:"+pps_length);
				
				pps_start+=2;
				
				//PPS = new byte[pps_length];
				try{
			    	PPS = new byte[pps_length];
				}
				catch (OutOfMemoryError e)
			    {
			    	e.printStackTrace();
			    	return;
			    }
				System.arraycopy(bBuf, pps_start, PPS,0,pps_length);
			}
		}
	}
	
	  public short bytes2short(byte[] b)
      {
	            short mask=0xff;
	            short temp=0;
	            short res=0;
	            for(int i=0;i<2;i++)
	            {
	                res<<=8;
	                temp=(short)(b[1-i]&mask);
	                res|=temp;
	            }
	            return res;
      }
	  
	  public int bytes2Int(byte[] b)
	  {
		  return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) |
          ((b[2] & 0xff) << 8) | (b[3] & 0xff);
	  }
	  
	  private void GetVideoSize()
	  {
		  
		  int iSettingValue = SharedPreferenceData.DRIR_SETTING_DRID_RECMODE.getInt();
		  if (mLastResolution != iSettingValue)
		  {
			  SPS = null;
			  PPS = null;
			  switch (iSettingValue)
			  {
			  case jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_HQ:
				  mVideoWidth = 1280;
				  mVideoHeight = 720;
				  break;
			  case jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_STD: 
				  mVideoWidth = 640;
				  mVideoHeight = 480;
				  break;
			  case jniDRIR_MainControl.DRIR_SETTING_DR_RECMODE_LT:
				  mVideoWidth = 320;
				  mVideoHeight = 240;
				  break;
			  default:
				  mVideoWidth = 640;
				  mVideoHeight = 480;
				  break;
			  }
			  Log.i("DRIRVideoRecorder", "Setting change width " + mVideoWidth
					  + " height " + mVideoHeight + " iSettingValue " + iSettingValue);
			  mLastResolution = iSettingValue;
		  }
	  }
}
