package com.billionav.DRIR.DRDM;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;

public class DRIRAudioRecorder implements OnInfoListener, OnErrorListener {
	private static final String TAG = "DRIRAudioRecorder";
	private static final int SONY_HEADEROFFSET = 44;
	private static final int HTC_HEADEROFFSET = 32;
	private static final int SOCKET_BUFFERSIZE = 500000;
	private static final int FRAME_SIZE = 26;
	private static final int BUFFER_SIZE = FRAME_SIZE * 64;
	
	LocalSocket m_cReceiver;
	LocalSocket m_cSender;
	LocalServerSocket m_cLocalServer;
	
	private MediaRecorder m_cMediaRecorder = null;
	boolean m_bMediaRecording = false;
	
	Thread m_cReceiveThread;

	//Temp for the debug
	RandomAccessFile raf = null;
	
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

		m_cMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		m_cMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		m_cMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		m_cMediaRecorder.setOutputFile(m_cSender.getFileDescriptor());


		try {
			m_cMediaRecorder.setOnInfoListener(this);
			m_cMediaRecorder.setOnErrorListener(this);
			m_cMediaRecorder.prepare();
			m_cMediaRecorder.start();
			bRet = true;
		} catch (IllegalStateException e) {
			ReleaseMediaRecorder();
			e.printStackTrace();
		} catch (IOException e) {
			ReleaseMediaRecorder();
			e.printStackTrace();
		}
		return bRet;
	}
	
	public void ReleaseMediaRecorder()
	{
		if (null != m_cMediaRecorder)
		{
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
		}
	}

	public void StartAudioRecording()
	{
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
			    
				int iPerReadSize = 0;
				int iReadCnt = 0;
				InputStream cFis = null;
				try {
					cFis = m_cReceiver.getInputStream();
				} catch (IOException e) {
					try {
						cFis.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}

				ReleaseMediaRecorder();
				
				InitalMediaRecorder();

				DataInputStream cDis = new DataInputStream(cFis);

				try {
					File cFile = new File("/sdcard/DRDMAudio.AMR_NB");

					if (cFile.exists())
					{
						cFile.delete();
					}
					raf = new RandomAccessFile(cFile, "rw");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//Skip the header
				int iHeader = SONY_HEADEROFFSET;
				try {
					int iRet = cDis.read(bBuffer, 0, iHeader);
					while (iHeader != iRet)
					{
						iRet += cDis.read(bBuffer, iRet, iHeader - iRet);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while(true)
				{
					try {
						iReadCnt = 0;
						while (iReadCnt < FRAME_SIZE)
						{
							int iLost = FRAME_SIZE - iReadCnt;
							iPerReadSize = cFis.read(bBuffer, iReadCnt, iLost);
							iReadCnt += iPerReadSize;
						}

						//Write into the video encoder buffer
						//raf.write(bBuffer, 0, iReadCnt);
					} catch (IOException e) {
						try {
							cFis.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}


				}
			}
		}).start();
	}
	
	public void StopAudioRecording()
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
	}
	
	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		// TODO Auto-generated method stub
		
	}
}
