package com.billionav.sdkspecial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.billionav.jni.jniLocInfor;
import com.billionav.navi.gps.BTDevice_Info;
import com.billionav.navi.gps.CLocationListener;

/*
 * This class is working for control Bluetooth process,
 * other class call interface to do matching action.
 */
public class BtGpschannel {
	
	// Singleton
	private static final BtGpschannel mInstance = new BtGpschannel();
	
	/*
	 * Instance
	 */
	public static BtGpschannel instance() {
		return mInstance;
	}
	
	/*
     * Constructor. Create a Bluetooth service
     */
    public BtGpschannel() {
    	// get current Bluetooth adapter
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        // set state to nothing to do
        mState = STATE_NONE;
    }

	// Debugging
	private static final String TAG = "BtGpschannel";
	private static final boolean Debug = false;
	
	// Name for the SDP record when creating server socket
	private static final String NAME = "BtGpschannel";
	
	// Unique UUID for this application
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Location listener
    private final CLocationListener loclistener = CLocationListener.Instance();
    
    // External control for analysis data
    private final jniLocInfor mExternalControl = jniLocInfor.getLocInfor();
	
    /**************************Get device******************************/
	// Bluetooth process using
    private BluetoothAdapter 	mAdapter;
    private BluetoothDevice		mDevice;
    
    public BTDevice_Info GetBTDeviceInfo() {

    	// Set device information name and address
    	if (mState == STATE_CONNECTED) {

    		BTDevice_Info btDeviceInfo = new BTDevice_Info();
    		btDeviceInfo.Name = mDevice.getName();
    		btDeviceInfo.Address = mDevice.getAddress();
    		return btDeviceInfo;
    	}
    	else {

    		return null;
    	}
    }
    
    /**************************State Machine******************************/
    // Constants that indicate the current connection state
    private int mState = STATE_NONE;
    public static final int STATE_NONE 			= 0;	// we're doing nothing
    public static final int STATE_ACCEPT		= 1;	// accept state
    public static final int STATE_CONNECTING	= 2;	// try to connect other remote device
    public static final int STATE_CONNECTED 	= 3; 	// now connected with a remote device
	
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
    private synchronized void setState(int state) {

    	// Check the state if same nothing to do
    	if (state == mState) {
    		return;
    	}
    	
    	loclistener.writeGpslogfile("setState() " + mState + " -> " + state);

    	mState = state;
    }

    /**
     * Return the current connection state. 
     */
    public synchronized int getState() {
    	return mState;
    }
    
    // handler for notify screen
    private Handler UIhandler = null;
    
    private boolean bIsSwtichedToInternalGps = false;
    
    /*
     * CurrentBTIsEnable
     */
    public boolean CurrentBTIsEnabled() {
    	// check adapter
    	if (mAdapter == null) {
    		return false;
    	}
    	// return current BT is enable or not
    	return mAdapter.isEnabled();
    }
    
    /*
     * BTActionRequestEnable
     */
    static public String BTActionRequestEnable() {
    	// return ACTION_REQUEST_ENABLE String
    	return BluetoothAdapter.ACTION_REQUEST_ENABLE;
    }
    
    /*
     * Start to accept
     */
	public synchronized boolean startToAccept() {

		if (mState == STATE_ACCEPT) {
			return true;
		}

		loclistener.writeGpslogfile("BT start to accept");
			
		// accept
		accept();
		
		// Check start connecting result
		if (mState == STATE_ACCEPT) {
			
			// By kind
			int start_kind = jniLocInfor.DATA_KIND_NONE;
			switch (loclistener.getExternalDeviceKind()) {
			case CLocationListener.EXTERNAL_DEVICE_KIND_CRADLE:
				start_kind = jniLocInfor.DATA_KIND_CRADLE;
				break;
			case CLocationListener.EXTERNAL_DEVICE_KIND_OBD:
				start_kind = jniLocInfor.DATA_KIND_OBD;
				break;
			default:
				// ERROR
				break;	
			}
			// Start to connect to Cradle
			mExternalControl.ConnectStart(start_kind);
			
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	 * Start to connect with a remove device
	 */
	public synchronized boolean startToConnect(BTDevice_Info device_info) {
		
		if (mState == STATE_CONNECTING) {
			return true;
		}
		
		loclistener.writeGpslogfile("BT start to connect");
		
		// Get device
		BluetoothDevice device = mAdapter.getRemoteDevice(device_info.Address);
		// start connecting
		connect(device);
		
		// Check start connecting result
		if (mState == STATE_CONNECTING) {
			
			// By kind
			int start_kind = jniLocInfor.DATA_KIND_NONE;
			switch (loclistener.getExternalDeviceKind()) {
			case CLocationListener.EXTERNAL_DEVICE_KIND_CRADLE:
				start_kind = jniLocInfor.DATA_KIND_CRADLE;
				break;
			case CLocationListener.EXTERNAL_DEVICE_KIND_OBD:
				start_kind = jniLocInfor.DATA_KIND_OBD;
				break;
			default:
				// ERROR
				break;	
			}
			// Start to connect to Cradle
			mExternalControl.ConnectStart(start_kind);
			
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	 * Stop
	 */
	public synchronized void stop() {
		loclistener.writeGpslogfile("BT stop");
		
		// By kind
		int stop_kind = jniLocInfor.DATA_KIND_NONE;
		switch (loclistener.getExternalDeviceKind()) {
		case CLocationListener.EXTERNAL_DEVICE_KIND_CRADLE:
			stop_kind = jniLocInfor.DATA_KIND_CRADLE;
			break;
		case CLocationListener.EXTERNAL_DEVICE_KIND_OBD:
			stop_kind = jniLocInfor.DATA_KIND_OBD;
			break;
		default:
			// ERROR
			break;	
		}
		// Stop to connect to Cradle
		mExternalControl.ConnectStop(stop_kind);
		
		loclistener.writeGpslogfile("BT stop1");
		
		// Disconnect all
		disconnect();
	}
	
	/**************************Bluetooth Connect State Change******************************/
	private AcceptThread 		mAcceptThread;
	private ConnectingThread	mConnectingThread;
	private ConnectedThread 	mConnectedThread;

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    private synchronized void accept() {
    	
    	loclistener.writeGpslogfile("Begin to accept");

    	if (mConnectingThread != null) {
        	mConnectingThread.cancel();
        	mConnectingThread = null;
        }
        if (mConnectedThread != null) {
        	mConnectedThread.cancel();
        	mConnectedThread = null;
        }
        
        if (mAcceptThread == null) {
        	// Start accept
	    	mAcceptThread = new AcceptThread();
	    	mAcceptThread.start();
        }
        
        // Check valid
        if (mAcceptThread.isValid()) {
            setState(STATE_ACCEPT);
        }
        else {
        	// Reset state to none
        	setState(STATE_NONE);
        }
    }
    
    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    private synchronized void connect(BluetoothDevice device) {
        
    	// Cancel all thread
    	if (mState == STATE_CONNECTING) {
    		// Cancel any thread currently running for connecting
    		if (mConnectingThread != null) {
    			mConnectingThread.cancel();
    			mConnectingThread = null;
    		}
    	}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
        	mConnectedThread.cancel();
        	mConnectedThread = null;
        }
        
    	// Start the thread to connect with the given device
    	mConnectingThread = new ConnectingThread(device);
    	mConnectingThread.start();
        
        // Check valid
        if (mConnectingThread.isValid()) {
            setState(STATE_CONNECTING);
        }
        else {
        	// Reset state to none
        	setState(STATE_NONE);
        }
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param iLastState
     */
    private synchronized void connected(BluetoothSocket socket, int iLastState) {
    	
    	loclistener.writeGpslogfile("connected");
    	
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
        	mConnectedThread.cancel();
        	mConnectedThread = null;
        }
        
        // Cancel the thread that completed the connection
        if (mAcceptThread != null) {
        	mAcceptThread.cancel();
        	mAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
    	mConnectedThread = new ConnectedThread(socket, iLastState);
    	mConnectedThread.start();

        // Check valid
        if (mConnectedThread.isValid()) {
            setState(STATE_CONNECTED);
        }
        else {
        	// Reset state to none
        	setState(STATE_NONE);
        	return;
        }

        // Set current connect device
        mDevice = socket.getRemoteDevice();
		
		// already switched to internal GPS need to
		// switch to external
		if (bIsSwtichedToInternalGps) {
			BTDevice_Info device_info = new BTDevice_Info();
			device_info.Name = mDevice.getName();
			device_info.Address = mDevice.getAddress();
			// switch to external GPS
			loclistener.SwitchToCradleGPS(device_info);
			// not switched to internal GPS
			bIsSwtichedToInternalGps = false;
		}
    }
    
    /**
     * disconnect all
     */
    private synchronized void disconnect() {
    	
    	loclistener.writeGpslogfile("disconnect");
        
        // stop connected thread of input and output
        if (mConnectedThread != null) {
        	mConnectedThread.cancel();
        	mConnectedThread = null;
        }
        
        // Cancel the thread that completed the connection
        if (mAcceptThread != null) {
        	mAcceptThread.cancel();
        	mAcceptThread = null;
        }
        
        // Cancel the thread
        if (mConnectingThread != null) {
        	mConnectingThread.cancel();
        	mConnectingThread = null;
        }
        
        // set state to none
        setState(STATE_NONE);
        
        // reset the flag
        bIsSwtichedToInternalGps = false;
        
        // connected
		loclistener.setCurrentExternalGpsOn(false);
    }

    /**
     * Indicate that the connection was lost.
     */
    private synchronized void connectionLost(BluetoothDevice device, int iLastState) {
    	
		// Check last state to do different action
		switch (iLastState) {

		case STATE_ACCEPT:
			
			// Start to accept
			accept();
			break;

		case STATE_CONNECTING:
			
			// Cancel all thread
			disconnect();
			break;

		default:
			// Nothing to do
			break;
		}
    }
    
    /**************************Bluetooth Connect******************************/
    /*
     * This thread runs while connecting or reading process has exception.
     */
    private class AcceptThread extends Thread {
    	
    	private boolean mmbValid;
    	private boolean mmbQuit;
        private final BluetoothServerSocket mmServerSocket;
    	
        /*
         * Check the thread is create successful or not
         */
        public boolean isValid() {
        	return mmbValid;
        }
        
    	/*
    	 * Constructor for initialization
    	 */
    	public AcceptThread() {
    		
    		mmbQuit = false;
    		mmbValid = false;
    		BluetoothServerSocket tmp = null;
    		
            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
                mmbValid = true;
            } catch (IOException e) {
            	if (Debug) {
            		loclistener.writeGpslogfile("listen() failed" + e);
            	}
            	// Stop
            	BtGpschannel.this.stop();
            }
            
            // Get server socket
            mmServerSocket = mmbValid ? tmp : null;	 	
    	}
    	
    	/*
    	 * thread run to wait for next connect
    	 */
    	public void run() {
    		
    		// Check create successful or not
            if (!mmbValid) {
            	return;
            }
    		
    		loclistener.writeGpslogfile("BEGIN accept thread");
            
    		setName("AcceptThread");
    		
    		BluetoothSocket socket = null;
    		int iExceptCnt = 0;
    		final int EXCEPT_MAX_CNT = 1;

            while(true) {
            	
            	// Check quit
            	if (mmbQuit) {
            		break;
            	}
            	
            	// accept
            	try {
            		loclistener.writeGpslogfile("Begin to listen");
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                	
                	// Check quit
                	if (mmbQuit) {
                        break;
                	}
                	
                	if (Debug) {
                		Log.e(TAG, "accept() failed", e);
                	}
                	loclistener.writeGpslogfile("accept() failed");
                	
                	iExceptCnt++;
                	// check exit the cycle or for next
                	if (iExceptCnt >= EXCEPT_MAX_CNT) {
                		loclistener.writeGpslogfile("accept() break");
                		break;
                	}
                	else {
                		loclistener.writeGpslogfile("accept() for next");
                		continue;
                	}
                }
                
                // If a connection was accepted
				if (socket != null) {
					switch (BtGpschannel.this.getState()) {
					case STATE_ACCEPT:
						// Need to check device name or address
						if (true) {
							loclistener.writeGpslogfile("Beging connected");
							// Situation normal. Start the connected thread.
							connected(socket, STATE_ACCEPT);
							break;
						} else {
							loclistener.writeGpslogfile("socket is invalid to close");
							// Terminate new socket and start for next accept
							try {
								socket.close();
							} catch (IOException e) {
								if (Debug) {
									Log.e(TAG, "Could not close unwanted socket", e);
								}
							}
							continue;
						}
					case STATE_NONE:
					case STATE_CONNECTED:
						loclistener.writeGpslogfile("state is error to close");
						// Either not ready or already connected. Terminate new
						// socket.
						try {
							socket.close();
						} catch (IOException e) {
							if (Debug) {
								Log.e(TAG, "Could not close unwanted socket", e);
							}
						}
						break;
					}
				}
			}
			loclistener.writeGpslogfile("END mAcceptThread");
		}
    	
    	/*
    	 * Cancel
    	 */
    	public void cancel() {
    		loclistener.writeGpslogfile("cancel Accept Thread");
    		mmbQuit = true;
    		try {
    			if (mmbValid) {
    				mmServerSocket.close();
    			}
    		} catch (IOException e) {
    			if (Debug) {
    				Log.e(TAG, "close() of server failed", e);
    			}
    		}
    	}
    }
    
    /**
     * This thread runs while select a device in search list and 
     * connecting with it. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectingThread extends Thread {

    	private boolean mmbValid;
    	private boolean mmbQuit;
        private final BluetoothSocket mmSocket;
        
        /*
         * Check the thread is create successful or not
         */
        public boolean isValid() {
        	return mmbValid;
        }

        /*
         * Constructor for initialization
         */
        public ConnectingThread(BluetoothDevice device) {
            
        	mmbValid = false;
        	mmbQuit = false;
        	BluetoothSocket tmp = null;
            
            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
            	// Get socket
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                mmbValid = true;
            } catch (IOException e) {
            	// Log output
            	if (Debug) {
            		Log.e(TAG, "create() failed", e);
            	}
            }
            
            // Device and socket create
            mmSocket = mmbValid ? tmp : null;
        }

        /*
         * Try to connect with the remote device
         * @see java.lang.Thread#run()
         */
        public void run() {
        	
        	// Check valid
        	if (!mmbValid) {
        		return;
        	}
        	
        	loclistener.writeGpslogfile("BEGIN connecting thread");
        	
        	setName("ConnectingThread");
        	
        	// Always cancel discovery because it will slow down a connection
           	mAdapter.cancelDiscovery();

            // Check quit
            if (mmbQuit) {
                return;
            }
            
            // Make a connection to the BluetoothSocket
            try {
            	// This is a blocking call and will only return on a
            	// successful connection or an exception
            	mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    if (Debug) {
                    	Log.e(TAG, "unable to close() socket during connection failure", e2);
                    }
                }
                
            	// No quit turn to connect failed state
            	if (!mmbQuit) {
            		// Connection failed change to exception thread
            		loclistener.SwitchToInterGPS(true);
            	}
                return;
            }
            synchronized (BtGpschannel.this) {
            	mConnectingThread = null;
            }
            
            // Start the connected thread
            connected(mmSocket, STATE_CONNECTING);
        }
        
        /*
         * Cancel
         */
        public void cancel() {
        	mmbQuit = true;
        	// Close created socket
            try {
            	if (mmbValid) {
            		mmSocket.close();
            	}
            } catch (IOException e) {
            	if (Debug) {
            		Log.e(TAG, "close() of connect socket failed", e);
            	}
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     */
    private class ConnectedThread extends Thread {
    	
    	private boolean mmbValid;
    	private boolean mmbQuit;
        private final BluetoothSocket mmSocket;
        private final int mmLastState;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        /*
         * Check the thread is create successful or not
         */
        public boolean isValid() {
        	return mmbValid;
        }
        
        /*
         * Constructor for initialization
         */
        public ConnectedThread(BluetoothSocket socket, int iLastState) {
        	
        	mmbValid = false;
        	mmbQuit = false;
            mmSocket = socket;
            mmLastState = iLastState;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input and output streams from BluetoothSocket
            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
                mmbValid = true;
            } catch (IOException e) {
            	// Log output
            	if(Debug) {
            		Log.e(TAG, "temp sockets not created", e);
            	}
            	loclistener.writeGpslogfile("socket connect check fail");
            }
            
            // Create socket, input and output
            mmInStream = mmbValid ? tmpIn : null;
            mmOutStream = mmbValid ? tmpOut : null;
        }

        /*
         * Main read and write cycle
         * @see java.lang.Thread#run()
         */
        public void run() {
        	
        	// Check valid
        	if (!mmbValid) {
        		// Close created socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                	if (Debug) {
                		Log.e(TAG, "close() of connect socket failed", e2);
                	}
                }
        		return;
        	}

        	loclistener.writeGpslogfile("BEGIN connected thread");
        	
        	setName("ConnectedThread");
        	
        	// Sleep 1s
        	SystemClock.sleep(1000);
    		
            // By kind
			int init_kind = jniLocInfor.DATA_KIND_NONE;
			switch (loclistener.getExternalDeviceKind()) {
			case CLocationListener.EXTERNAL_DEVICE_KIND_CRADLE:
				init_kind = jniLocInfor.DATA_KIND_CRADLE;
				break;
			case CLocationListener.EXTERNAL_DEVICE_KIND_OBD:
				init_kind = jniLocInfor.DATA_KIND_OBD;
				break;
			default:
				// ERROR
				break;	
			}
            // Start initialize communicate process
            mExternalControl.ConnectInitComm(init_kind);
            
            // connected
            loclistener.setCurrentExternalGpsOn(true);
            
            // Buffer
            byte[] buffer = new byte[2048];
    		
            // Keep listening to the InputStream while connected
            while (true) {
            	
            	// Check quit
            	if (mmbQuit) {
                    break;
            	}
                try {
                	// only cradle need to close internal GPS
                	if (loclistener.getExternalDeviceKind() == CLocationListener.EXTERNAL_DEVICE_KIND_CRADLE) {
                		// need to check close internal GPS timing
                		CheckToCloseInternalGps();
                	}
                	
                	// read data from BT
					final int iLength = mmInStream.read(buffer);
					// parse data and deliver to GpsSnsModule
					ParseCradleData(buffer, iLength);
                } catch (IOException e) {
                	// Check quit
                	if (mmbQuit) {
                        break;
                	}
              
                	// By kind
        			int break_kind = jniLocInfor.DATA_KIND_NONE;
        			switch (loclistener.getExternalDeviceKind()) {
        			case CLocationListener.EXTERNAL_DEVICE_KIND_CRADLE:
        				break_kind = jniLocInfor.DATA_KIND_CRADLE;
        				break;
        			case CLocationListener.EXTERNAL_DEVICE_KIND_OBD:
        				break_kind = jniLocInfor.DATA_KIND_OBD;
        				break;
        			default:
        				// ERROR
        				break;	
        			}
                	// External connect unexpected break down
                	mExternalControl.ConnectBreak(break_kind);
                	// connection lost
                	connectionLost(mmSocket.getRemoteDevice(), mmLastState);
                }
            }
            
            // disconnected
            loclistener.setCurrentExternalGpsOn(false);
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
        	
        	// Check quit
        	if (mmbQuit) {
                return;
        	}
        	try {
        		mmOutStream.write(buffer);
        	} catch (IOException e) {
	           	if (Debug) {
	           		Log.e(TAG, "Exception during write", e);
	           	}
        	}
        }

        /*
         * Cancel
         */
        public void cancel() {
        	loclistener.writeGpslogfile("cancel Connected Thread");
        	mmbQuit = true;
        	loclistener.writeGpslogfile("Begin to quit");
    		// Close created socket
            try {
            	if (mmbValid) {
            		mmInStream.close();
            		mmOutStream.close();
            	}
                mmSocket.close();
            } catch (IOException e2) {
            	if (Debug) {
            		Log.e(TAG, "close() of connect socket failed", e2);
            	}
            }
            loclistener.writeGpslogfile("quitted");
        }
    }

    
    
	/*
	 * Check to close internal GPS timing
	 */
	private void CheckToCloseInternalGps() {
		// get close internal GPS Opportunity
		if (loclistener.isNeedBTThreadToCloseInternalGps()
		&& loclistener.GetCloseInternalGpsOpportunity()) {
			try {
				// send message to close internal GPS
				loclistener.SendCloseInternalGpsMsg();
				loclistener.writeGpslogfile("run - Opportunity to close internal GPS done");
			} catch (Exception e) {
				loclistener.writeGpslogfile("run - Opportunity to close internal GPS error");
			}
		}
	}
	
	/*
	 * Send data
	 */
	private void ParseCradleData(byte[] buff, int length) {
		if (mState == STATE_CONNECTED && loclistener != null) {
			// send data to cradle
			loclistener.SendCradleData(buff, length);
		}
	}

	private static final Object writeSync = new Object();	// synchronized object for write
	
	/*
	 * write feedback data to cradle by BT
	 */
	public void WriteToCradle(byte[] buffer, int size) {
		if (mState == STATE_CONNECTED && mConnectedThread != null) {
			synchronized (writeSync) {
				// Write data to Cradle
				mConnectedThread.write(buffer);
				// Sleep 20ms for next write
				SystemClock.sleep(20);
			}
		}
	}

	/*
	 * Get all bonded device
	 */
	public ArrayList<HashMap<String, Object>> GetBondedDevice() {
		// check adapter object
		if (mAdapter == null) {
			mAdapter = BluetoothAdapter.getDefaultAdapter();
		}
		// adapter fault and can not get default adapter (only for simulator)
		if (mAdapter == null) {
			return null;
		}
		// current BT is not enabled, do not return bonded list
		if (!CurrentBTIsEnabled()) {
			return null;
		}
		
		int cnt = 0;
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Set<BluetoothDevice> setDevice = mAdapter.getBondedDevices();
		BluetoothDevice bondeddevice;
		Iterator<BluetoothDevice> iterator = setDevice.iterator();
		HashMap<String, Object> map = new HashMap<String, Object>();
		while (iterator.hasNext()) {
			BTDevice_Info btinfo = new BTDevice_Info();
			bondeddevice = (BluetoothDevice) iterator.next();
			if (Debug)
				Log.d(TAG, bondeddevice.getName());
			btinfo.Name = bondeddevice.getName();
			btinfo.Address = bondeddevice.getAddress();
			map.put(String.valueOf(cnt), btinfo);
			list.add(map);
			cnt++;
		}
		return list;
	}

	/*
	 * Search process
	 */
	public void doDiscovery(Context context, Handler handler) {
		if (Debug) {
			Log.d(TAG, "doDiscovery()");
		}
		UIhandler = handler;
		
        	// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		context.registerReceiver(mReceiver, filter);
		
		// If we're already discovering, stop it
		if (mAdapter.isDiscovering()) {
			mAdapter.cancelDiscovery();
		}
		// Request discover from BluetoothAdapter
		mAdapter.startDiscovery();
	}
	
	/*
	 * Cancel discover process
	 */
	public void cancelDiscover() {
		if (mAdapter != null) {
			mAdapter.cancelDiscovery();
			loclistener.writeGpslogfile("sample release 0128-cancel deiscovering");
			if (Debug) {
				Log.d(TAG, "cancelDiscover");
			}
		} else {
			if (Debug) {
				Log.d(TAG, "cancelDiscover : adatpet is null");
			}
		}
	}

	/*
	 *  The BroadcastReceiver that listens for discovered devices and
	 * changes the title when discovery is finished
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			loclistener.writeGpslogfile("onReceive action = " + action);
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					// TO do
					sendDeviceInfoToUi(device.getName(), device.getAddress());
				}
			// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

			}
		}
	};

	private void sendDeviceInfoToUi(String name, String addr) {
		BTDevice_Info btinfo = new BTDevice_Info();
		btinfo.Name = name;
		btinfo.Address = addr;
		Message msg = Message.obtain(UIhandler, 1, btinfo);
		msg.sendToTarget();

	}

	private void notifyUIConnectRemoteDeviceFail(String name, String addr) {

		BTDevice_Info btinfo = new BTDevice_Info();
		btinfo.Name = name;
		btinfo.Address = addr;
		Message msg = Message.obtain(UIhandler, 2, btinfo);
		msg.sendToTarget();
	}

	private void notifyUIConnectRemoteDeviceOK(String name, String addr) {

		BTDevice_Info btinfo = new BTDevice_Info();
		btinfo.Name = name;
		btinfo.Address = addr;
		Message msg = Message.obtain(UIhandler, 3, btinfo);
		msg.sendToTarget();
	}

	private void notifyUIConnectRemoteDeviceDisconneted(String name, String addr) {
		BTDevice_Info btinfo = new BTDevice_Info();
		btinfo.Name = name;
		btinfo.Address = addr;
		Message msg = Message.obtain(UIhandler, 4, btinfo);
		msg.sendToTarget();
	}
}

