package com.billionav.navi.gps;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import android.util.Log;

import com.billionav.jni.FileSystemJNI;
import com.billionav.jni.jniLocInfor;
import com.billionav.jni.jniSetupControl;
import com.billionav.navi.menucontrol.*;
import com.billionav.sdkspecial.BtGpschannel;
import com.billionav.navi.sensor.LocSnsCradleListener;
import com.billionav.navi.uitools.SetupOptionValue;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.jni.jniGpsSnsModule;

public class CLocationListener {

	/*==================================================================*/
	// Class target
	private static final String TAG = "CLocationListener";
	
	public static final int LOCSNS_CRADLE_USE_DISCONNECT = 0;
	public static final int LOCSNS_AUTO_USE_DISCONNECT = 1;
	public static final int LOCSNS_AUTO_USE_GPSNOTOPEN = 2;
	
	// instance
	private static final CLocationListener mInstance = new CLocationListener();
	
	/*
	 * Instance of singleton
	 */
	public static CLocationListener Instance() {
		return mInstance;
	}
	
	/*
	 * Constructor
	 */
	private CLocationListener() {}
	
	// used for draw view
	public Context mContext = null;
	
	// jniLocInfor used to send information
	private jniLocInfor mJniLocInfor= jniLocInfor.getLocInfor();
	
	private boolean m_SendMsgFlag = false;
	
	/** Called when the activity is first created. */
	public void onCreate(LocationManager LocManager, Context context) {

		writeGpslogfile("CLocationListener - onCreate");
		
		// Initialize all location position listener
		mGpsListener.intilaize(LocManager);
		mNetworkListener.intilaize(LocManager);
		//mNetworkBaiduListener.intilaize(context);
		mNetworkGaodeListener.intilaize(context);

		// keep the context for draw view;
		mContext = context;

		// Get the instance of jniLocInfor;
//		mJniLocInfor = jniLocInfor.getLocInfor();

		jniGpsSnsModule.getGpsSnsModule().SetSnsCradleConnectStatus(false);		
		m_CradleConnectStatus = SharedPreferenceData.getInt(SharedPreferenceData.GPS_CONNECTION_MODE);
		
		if ( SetupOptionValue.LOCATION_FUNC_CRADLE == m_CradleConnectStatus || 
				SetupOptionValue.LOCATION_FUNC_AUTO == m_CradleConnectStatus) {
			ChangeToCradleSource();			
		}
	}
	
	// this function used to removes registration for Location and GPS status
	// listener.
	public void onDestroy() {
		
		// Close internal GPS
		CloseInternalGps();
		// set internal GPS flag to ON
		m_bCurrentInternalGpsOn = false;

		// Close External Cradle connect
		CloseExternalCradle();
		m_bCurrentExternalGpsOn = false;
		
		if (m_OpenCradle) {
			
			//close cradle
			mCradleListener.stop();
			mCradleListener.stopProvider(mContext);
			m_OpenCradle = false;
		}	
		
	}
	public boolean isGpsOK() {

		if (m_CradleUseGps) {

			return mGpsListener.isGpsOK();
		}
		else
		{
			return mCradleListener.isCradleOK();
		}
	}

	public void SetCradleConnectStatus(int status) {		
		
		if (status < SetupOptionValue.LOCATION_FUNC_INNER || status > SetupOptionValue.LOCATION_FUNC_AUTO) {
			return;
		}
		
		if (status == m_CradleConnectStatus) {
			return;
		}
		
		m_CradleConnectStatus = status;
		
		switch (status) {
		case SetupOptionValue.LOCATION_FUNC_INNER:
			ChangeToInternalSource();
			break;
			
		case SetupOptionValue.LOCATION_FUNC_CRADLE:
			ChangeToCradleSource();
			break;
			
		case SetupOptionValue.LOCATION_FUNC_AUTO:
			ChangeToAuto();
			break;
			
			default:
				break;
		}		
		
		
	}
	
	public void ResetCradleConnectStatus() {
		
		//stop
		mCradleListener.stop();
		mCradleListener.stopProvider(mContext);
		//start
		mCradleListener.initialize(mContext);
		mCradleListener.start();
		m_OpenCradle = true;
		m_SendMsgFlag = false;
		
	}
	
	private void ChangeToInternalSource() {
		
		//switch to internal gps 
		CloseCradleSource();
		mGpsListener.SetGpsEnable(true);
		m_CradleUseGps = true;
		jniGpsSnsModule.getGpsSnsModule().SetSnsCradleConnectStatus(false);
		
	}
	
	private void ChangeToInternalSourceForNotCloseCradle() {
		
		//only change to gps
		mGpsListener.SetGpsEnable(true);
		m_CradleUseGps = true;
		jniGpsSnsModule.getGpsSnsModule().SetSnsCradleConnectStatus(false);
	}
	
	private void ChangeToCradleSource() {
		
		m_SendMsgFlag = false;
		//switch to cradle
		OpenCradleSource();
		
		//set gps enable , cradle use gps ,it will be 
		// in the cradle linstener.
		// when cradle connected, it will call 
		//CloseInternalGpsStatus()
		
		
		
	}
	
	private void ChangeToAuto() {
		
		m_SendMsgFlag = false;
		//first, try to open cradle
		OpenCradleSource();
		
		// if cradle disable , CradleListener will
		//call CradleConnectDisable		
		
		
		
	}
	
	
	private void OpenCradleSource() {
		
		if (true == m_OpenCradle) {
			CloseCradleSource();
			
		}
		
		
		// open cradle 
		mCradleListener.initialize(mContext);
		mCradleListener.start();
		m_OpenCradle = true;
		
	}
	
	private void CloseCradleSource() {
		
		if (false == m_OpenCradle) {
			
			return;
		}
		
		//close cradle
		mCradleListener.stop();
		mCradleListener.stopProvider(mContext);
		m_OpenCradle = false;
		
	}
	
	public void CloseInternalGpsStatus() {
		
		if (SetupOptionValue.LOCATION_FUNC_INNER == m_CradleConnectStatus) {			
			return;
		}
		
		mGpsListener.SetGpsEnable(false);
		m_CradleUseGps = false;
		jniGpsSnsModule.getGpsSnsModule().SetSnsCradleConnectStatus(true);
	}	
	
	
	public void CradleConnectDisable() {
		
		
		
		//close 
		if (SetupOptionValue.LOCATION_FUNC_INNER == m_CradleConnectStatus) {
			
			//for use internal gps , close cradle
			CloseCradleSource();
			
		}
		
		if (SetupOptionValue.LOCATION_FUNC_CRADLE == m_CradleConnectStatus) {
			
			if (!m_SendMsgFlag) {
				
				NSTriggerInfo info = new NSTriggerInfo();
				info.m_iTriggerID = NSTriggerID.UIC_MN_TRG_SENSOR_CRADLE_STATE_CHANGED;
				info.SetlParam1(LOCSNS_CRADLE_USE_DISCONNECT);
				MenuControlIF.Instance().TriggerForScreen(info);	
			}
					
			
		}
		else
		{
			if (!m_SendMsgFlag) {
				
				NSTriggerInfo info = new NSTriggerInfo();
				info.m_iTriggerID = NSTriggerID.UIC_MN_TRG_SENSOR_CRADLE_STATE_CHANGED;
				info.SetlParam1(LOCSNS_AUTO_USE_DISCONNECT);
				MenuControlIF.Instance().TriggerForScreen(info);
				
				if (!mGpsListener.isGpsOpen()) {
					
					NSTriggerInfo info_gps = new NSTriggerInfo();
					info_gps.m_iTriggerID = NSTriggerID.UIC_MN_TRG_SENSOR_CRADLE_STATE_CHANGED;
					info_gps.SetlParam1(LOCSNS_AUTO_USE_GPSNOTOPEN);
					MenuControlIF.Instance().TriggerForScreen(info_gps);
				}
			}
			
			
			ChangeToInternalSourceForNotCloseCradle();
			
		}
		m_SendMsgFlag = true;
	}
	
	
	/*==================================================================*/
	/*
	 * GPS from Android system
	 */
	private final LocGpsListener mGpsListener = LocGpsListener.instance();
	private final LocNetworkListener mNetworkListener = LocNetworkListener.instance();
	//private LocNetworkBaiduListener mNetworkBaiduListener = LocNetworkBaiduListener.instance();
	private	final LocNetworkGaodeListener mNetworkGaodeListener = LocNetworkGaodeListener.instance();
	
	private final LocSnsCradleListener mCradleListener = LocSnsCradleListener.instance();
	
	// Using internal GPS formats
	// public static final int GPS_USING_FROMAT_KIND_NONE = 0;
	public static final int GPS_USING_FROMAT_KIND_ANDROID = 1;
	public static final int GPS_USING_FROMAT_KIND_NMEA = 2;
	public static final int GPS_USING_FROMAT_KIND_BOTH = 3;
	
	//13 cradle 
	private int m_CradleConnectStatus = 0;
	private boolean m_CradleUseGps = true;
	private boolean m_OpenCradle = false;
	
	/*
	 * Open internal GPS by Android API
	 */
	private void OpenInternalGps(int iGpsUsingFormatKind) {
		
		// Start to get location position
		mGpsListener.start();
		mNetworkGaodeListener.start();
		mNetworkListener.start();
	}
	
	/*
	 * Close internal GPS by Android API
	 */
	private void CloseInternalGps() {
		
		// Stop all location listener
		mGpsListener.stop();
		mNetworkGaodeListener.stop();
		mNetworkListener.stop();
	}
	
	/*
	 * Close net-work listener / Gaode listener
	 */
	public void CloseNetworkListeners() {
		
		// Stop net-work location listeners
		mNetworkListener.stop();
		mNetworkGaodeListener.stop();
	}

	/*==================================================================*/
	/*
	 * Bluetooth connect with PIONEER Cradle
	 */
	// External Bluetooth connect
	private static final BtGpschannel CradleBtGPS = BtGpschannel.instance();
	
	/*
	 * Open external connect with cradle by Bluetooth
	 */
	private void OpenExternalCradld(BTDevice_Info device) {
		
		// Search for OBD
		String obd_regext = ".*OBD.*";
		Pattern p = Pattern.compile(obd_regext);
		Matcher m = p.matcher(device.Name);
		if(m.find()) {
			m_eExternalDeviceKind = EXTERNAL_DEVICE_KIND_OBD;
		}
		else {
			m_eExternalDeviceKind = EXTERNAL_DEVICE_KIND_CRADLE;
		}
		
		// Check connect state
		if (CradleBtGPS.getState() == BtGpschannel.STATE_NONE) {
			
			// Check device kind
			if (m_eExternalDeviceKind == EXTERNAL_DEVICE_KIND_CRADLE){
				
				// Begin to listen to External Cradle
				CradleBtGPS.startToAccept();
			}
			else if (m_eExternalDeviceKind == EXTERNAL_DEVICE_KIND_OBD){
				
				// connect to OBD Adapter
				CradleBtGPS.startToConnect(device);
			}
		}
	}
	
	/*
	 * Close external connect with cradle by Bluetooth
	 */
	private void CloseExternalCradle() {
		// Check state
		if (CradleBtGPS.getState() != BtGpschannel.STATE_NONE) {
			// disconnect External Cradle
			CradleBtGPS.stop();
		}
		// Switch device kind to none
		m_eExternalDeviceKind = EXTERNAL_DEVICE_KIND_NONE;
	}
	
	/*
	 *  Get Bluetooth device information
	 */
	public BTDevice_Info GetBTDeviceInfo() {
		return CradleBtGPS.GetBTDeviceInfo();
	}
	
	/*
	 * Send data from Cradle to Phone
	 */
	public void SendCradleData(byte[] data, int length) {
		// By kind
		switch (m_eExternalDeviceKind) {
		case EXTERNAL_DEVICE_KIND_CRADLE:
			if(m_bWriteLogsToFileEnable) {
				// for log
				String strOutput = "";
				int iCnt = 0;
				for (iCnt = 0; iCnt < length; ++iCnt) {
					if (data[iCnt] != '\r'
						&& data[iCnt] != '\n') {
						// ASCII to string
						strOutput += String.format("%02X", data[iCnt]);
					}
				}
				// display all data
				writeGpslogfile("EXT_PION[" + String.valueOf(length) + "]:" + strOutput);
			}
			// Send cradle information
			mJniLocInfor.GetData(jniLocInfor.DATA_KIND_CRADLE, data, length);
			break;
		case EXTERNAL_DEVICE_KIND_OBD:
			if(m_bWriteLogsToFileEnable) {
				// for log
				String strOutput = "";
				int iCnt = 0;
				for (iCnt = 0; iCnt < length; ++iCnt) {
					if (data[iCnt] != '\r'
						&& data[iCnt] != '\n') {
						// ASCII to string
						strOutput += String.format("%C", data[iCnt]);
					}
				}
				// display all data
				writeGpslogfile("EXT_OBDC[" + String.valueOf(length) + "]:" + strOutput);
			}
			// Send cradle information
			mJniLocInfor.GetData(jniLocInfor.DATA_KIND_OBD, data, length);
			break;
		default:
			// ERROR
			// Nothing to do
			break;
		}
	}

	/*
	 *  write feedback data from Phone to Cradle by BT
	 */
	public void WriteDataToCradle(byte[] data, int length) {
		// By kind
		switch (m_eExternalDeviceKind) {
		case EXTERNAL_DEVICE_KIND_CRADLE:
			if(m_bWriteLogsToFileEnable) {
				// for log
				String strOutput = "";
				int iCnt = 0;
				for (iCnt = 0; iCnt < length; ++iCnt) {
					// ASCII to string
					strOutput += String.format("%02X", data[iCnt]);
				}
				// display all data
				writeGpslogfile("EXT_WRIT[" + String.valueOf(length) + "]:" + strOutput);
			}
			break;
		case EXTERNAL_DEVICE_KIND_OBD:
			if(m_bWriteLogsToFileEnable) {
				// for log
				String strOutput = "";
				int iCnt = 0;
				for (iCnt = 0; iCnt < length; ++iCnt) {
					// ASCII to string
					strOutput += String.format("%C", data[iCnt]);
				}
				// display all data
				writeGpslogfile("EXT_WRIT[" + String.valueOf(length) + "]:" + strOutput);
			}
			break;
		default:
			// ERROR
			// Nothing to do
			break;
		}
			
		// Get certificate fault command to stop BT connection 
		// And switch to internal GPS
		// command is "9F020201039F03"
		if (data[2] == 0x02 && data[3] == 0x01) {	
			// Switch to internal GPS
			// and need to stop cradle
			writeGpslogfile("CERTIFICATE_FAULT_CMD switch to internal");
			SwitchToInterGPS(true);
		} else {
			// Write SmartPhone data to Cradle
			CradleBtGPS.WriteToCradle(data, length);
		}
	}
	
	/*
	 *  get close internal GPS opportunity for JNI
	 */
	public boolean GetCloseInternalGpsOpportunity() {
		return mJniLocInfor.GetCloseInternalGpsOpportunity();
	}
	
	public ArrayList<HashMap<String, Object>> GetBondedDevice() {
		return CradleBtGPS.GetBondedDevice();
	}

	public void Dodiscover(Handler handler) {
		CradleBtGPS.doDiscovery(mContext, handler);
	}

	public void cancelDiscover() {
		CradleBtGPS.cancelDiscover();
	}
	
	/*
	 * Check current Bluetooth connect state
	 */
	public boolean CurrentBTIsEnabled()
	{
		return CradleBtGPS.CurrentBTIsEnabled();
	}

	public String BTActionRequestEnable() {
		return BtGpschannel.BTActionRequestEnable();
	}
	
	/*==================================================================*/
	/*
	 * Internal, External, Disable state control
	 */
	// status change
	// enumerate 3 status: disable, internal, external(Cradle)
	public static final int GPS_CONNECT_STATUS_DISABLE = 0;
	public static final int GPS_CONNECT_STATUS_INTERNAL = 1;
	public static final int GPS_CONNECT_STATUS_EXTERNAL = 2;
	
	// Bluetooth connect status define for send trigger
	public static final int BT_CONNECT_ACTION_EXCEPTION = 20;
	
	// Bluetooth connect device kind
	public static final int EXTERNAL_DEVICE_KIND_NONE 	= 0;
	public static final int EXTERNAL_DEVICE_KIND_CRADLE = 1;
	public static final int EXTERNAL_DEVICE_KIND_OBD 	= 2;
	
	private int m_eExternalDeviceKind = EXTERNAL_DEVICE_KIND_NONE;
	
	// for m_eExternalDeviceKind flag
	public final int getExternalDeviceKind() {
		return m_eExternalDeviceKind;
	}

	// current GPS status and internal GPS connect and external(Cradle) connect
	private int m_eCurrentGpsStatus = GPS_CONNECT_STATUS_DISABLE;
	private boolean m_bCurrentInternalGpsOn = false;
	private boolean m_bCurrentExternalGpsOn = false;
	private boolean m_bNeedBTThreadToCloseInternalGps = false;
	
	// for m_bCurrentInternalGpsOn flag
	public final void setCurrentInternalGpsOn(boolean bCurrentInternalGpsOn) {
		this.m_bCurrentInternalGpsOn = bCurrentInternalGpsOn;
	}

	public final boolean isCurrentInternalGpsOn() {
		return m_bCurrentInternalGpsOn;
	}

	// for m_bCurrentExternalGpsOn flag
	public final void setCurrentExternalGpsOn(boolean bCurrentExternalGpsOn) {
		this.m_bCurrentExternalGpsOn = bCurrentExternalGpsOn;
	}

	public final boolean isCurrentExternalGpsOn() {
		return m_bCurrentExternalGpsOn;
	}

	// for m_bNeedBTThreadToCloseInternalGps flag
	public final void setNeedBTThreadToCloseInternalGps(
			boolean bNeedBTThreadToCloseInternalGps) {
		this.m_bNeedBTThreadToCloseInternalGps = bNeedBTThreadToCloseInternalGps;
	}

	public final boolean isNeedBTThreadToCloseInternalGps() {
		return m_bNeedBTThreadToCloseInternalGps;
	}
	
	// Get current GPS connect status Disable/Internal/External(Cradle)
	public final int GetCurrentGpsConnectStatus() {
		return m_eCurrentGpsStatus;
	}
	
	// Send current state by trigger
	public void SendCurrentStateByTrigger(int iState) {
		NSTriggerInfo cTriggerInfo = new NSTriggerInfo();
		cTriggerInfo.SetTriggerID(NSTriggerID.UIC_MN_TRG_LOC_GPS_CONNECT_STATUS);
		cTriggerInfo.SetlParam1(iState);
		MenuControlIF.Instance().TriggerForScreen(cTriggerInfo);
	}

	/*
	 * SwitchToCradleGPS
	 * 
	 * switch current connect state to cradle
	 * if already connected to a cradle, to be connected to the same one, do nothing
	 * to be connected to another one will stop current and start another
	 * 
	 * @param 	String name [in]: need to connect cradle name
	 * 			String addr [in]: need to connect cradle MAC address only
	 * @return	boolean true: switch successful
	 * 					false: switch fault
	 */
	public boolean SwitchToCradleGPS(BTDevice_Info device) {
		// file log
		writeGpslogfile("CLocationListener->SwitchToCradleGPS");
		
		// status change
		switch (m_eCurrentGpsStatus) {
		case GPS_CONNECT_STATUS_DISABLE:
			writeGpslogfile("SwitchToCradleGPS - old status GPS_CONNECT_STATUS_DISABLE");
			// connect Internal GPS
			try {
				// send message to open internal GPS
				SendOpenInternalGpsMsg();
			} catch (Exception e) {
				return false;
			}
			// connect External Cradle
			try {
				// send message to start External Cradle
				SendStartExternalGpsMsg(device);
			} catch (Exception e) {
				return false;
			}
			break;
		case GPS_CONNECT_STATUS_INTERNAL:
			writeGpslogfile("SwitchToCradleGPS - old status GPS_CONNECT_STATUS_INTERNAL");
			// connect external cradle
			try {
				// send message to start External Cradle
				SendStartExternalGpsMsg(device);
			} catch (Exception e) {
				return false;
			}
			break;
		case GPS_CONNECT_STATUS_EXTERNAL:
			writeGpslogfile("SwitchToCradleGPS - old status GPS_CONNECT_STATUS_EXTERNAL");
			// stop old connect
			try {
				// send message to stop External Cradle
				SendStopExternalGpsMsg();
			} catch (Exception e) {
				return false;
			}
			// start new cradle GPS
			try {
				// send message to start External Cradle
				SendStartExternalGpsMsg(device);
			} catch (Exception e) {
				return false;
			}
			break;
		default:
			// ERROR
			break;
		}
		// change current status
		m_eCurrentGpsStatus = GPS_CONNECT_STATUS_EXTERNAL;
		mJniLocInfor.SendCurrentConnectStatus(GPS_CONNECT_STATUS_EXTERNAL);
		SendCurrentStateByTrigger(GPS_CONNECT_STATUS_EXTERNAL);
		jniLocInfor.getLocInfor().SetCanReceiveGpsSignal(true);
		return true;
	}
	
	/*
	 * SwitchToInterGPS
	 * 
	 * switch current connect state to internal GPS
	 * 
	 * @param 	boolean bIsNeedToStopCradle [in]: need to stop connected cradle if old state is external
	 * @return	boolean true: switch successful
	 * 					false: switch fault
	 */
	public boolean SwitchToInterGPS(boolean bIsNeedToStopCradle) {
		// file log
		writeGpslogfile("CLocationListener->SwitchToInterGPS" + bIsNeedToStopCradle);
		
		// status change
		switch (m_eCurrentGpsStatus) {
		case GPS_CONNECT_STATUS_DISABLE:
			writeGpslogfile("SwitchToInterGPS - old status GPS_CONNECT_STATUS_DISABLE");
			// connect internal GPS
			try {
				// send message to open internal GPS
				SendOpenInternalGpsMsg();
			} catch (Exception e) {
				return false;
			}
			break;
		case GPS_CONNECT_STATUS_INTERNAL:
			writeGpslogfile("SwitchToInterGPS - old status GPS_CONNECT_STATUS_INTERNAL");
			// check need to stop cradle flag
			// Just for SwitchToInterGPS(false) -> SwitchToInterGPS(true)
			if (bIsNeedToStopCradle) {
				// disconnect External Cradle
				try {
					// send message to stop External Cradle
					SendStopExternalGpsMsg();
				} catch (Exception e) {
					return false;
				}
			}
			// connect internal GPS
			try {
				// send message to open internal GPS
				SendOpenInternalGpsMsg();
			} catch (Exception e) {
				return false;
			}
			break;
		case GPS_CONNECT_STATUS_EXTERNAL:
			writeGpslogfile("SwitchToInterGPS - old status GPS_CONNECT_STATUS_EXTERNAL");
			// check need to stop cradle flag
			if (bIsNeedToStopCradle) {
				// disconnect External Cradle
				try {
					// send message to stop External Cradle
					SendStopExternalGpsMsg();
				} catch (Exception e) {
					return false;
				}
			}
			// connect internal GPS
			try {
				// send message to open internal GPS
				SendOpenInternalGpsMsg();
			} catch (Exception e) {
				return false;
			}
			break;
		default:
			// ERROR
			break;
		}
		// change current status
		m_eCurrentGpsStatus = GPS_CONNECT_STATUS_INTERNAL;
		mJniLocInfor.SendCurrentConnectStatus(GPS_CONNECT_STATUS_INTERNAL);
		SendCurrentStateByTrigger(GPS_CONNECT_STATUS_INTERNAL);
		jniLocInfor.getLocInfor().SetCanReceiveGpsSignal(true);
		return true;
	}

	/*
	 * SwitchToDisable
	 * 
	 * switch current connect state to disable none GPS used
	 * 
	 * @param 	boolean bIsNeedToStopCradle [in]: need to stop connected cradle if old state is external
	 * @return	boolean true: switch successful
	 * 					false: switch fault
	 */
	public boolean SwitchToDisable(boolean bIsNeedToStopCradle) {
		// file log
		writeGpslogfile("CLocationListener->SwitchToDisable" + bIsNeedToStopCradle);

		// status change
		switch (m_eCurrentGpsStatus) {
		case GPS_CONNECT_STATUS_DISABLE:
			writeGpslogfile("SwitchToDisable - old status GPS_CONNECT_STATUS_DISABLE");
			// check need to stop cradle flag
			// Just for SwitchToDisable(false) -> SwitchToDisable(true)
			if (bIsNeedToStopCradle) {
				// disconnect External Cradle
				try {
					// send message to stop External Cradle
					SendStopExternalGpsMsg();
				} catch (Exception e) {
					return false;
				}
			}
			break;
		case GPS_CONNECT_STATUS_INTERNAL:
			writeGpslogfile("SwitchToDisable - old status GPS_CONNECT_STATUS_INTERNAL");
			// check need to stop cradle flag
			if (bIsNeedToStopCradle) {
				// disconnect External Cradle
				try {
					// send message to stop External Cradle
					SendStopExternalGpsMsg();
				} catch (Exception e) {
					return false;
				}
			}
			// disconnect internal GPS
			try {
				// send message to close internal GPS
				SendCloseInternalGpsMsg();
			} catch (Exception e) {
				return false;
			}
			break;
		case GPS_CONNECT_STATUS_EXTERNAL:
			writeGpslogfile("SwitchToDisable - old status GPS_CONNECT_STATUS_EXTERNAL");
			// check need to stop cradle flag
			if (bIsNeedToStopCradle) {
				// disconnect External Cradle
				try {
					// send message to stop External Cradle
					SendStopExternalGpsMsg();
				} catch (Exception e) {
					return false;
				}
			}
			// disconnect internal GPS
			try {
				// send message to close internal GPS
				SendCloseInternalGpsMsg();
			} catch (Exception e) {
				return false;
			}
			break;
		default:
			// ERROR
			break;
		}
		// change current status
		m_eCurrentGpsStatus = GPS_CONNECT_STATUS_DISABLE;
		mJniLocInfor.SendCurrentConnectStatus(GPS_CONNECT_STATUS_DISABLE);
		SendCurrentStateByTrigger(GPS_CONNECT_STATUS_DISABLE);
		jniLocInfor.getLocInfor().SetCanReceiveGpsSignal(false);
		return true;
	}
	
	/*==================================================================*/
	/*
	 * Open and close GPS or Bluetooth in Asynchronous
	 */
	// declare location listener handler class
	public static class CLocationListenerHandler extends Handler {
		public CLocationListenerHandler() {
			super(Looper.getMainLooper());
		}

		public void handleMessage(Message msg) {
			CLocationListener.Instance().handleMessage(msg);
		}
	}

	// handler message object
	private static final CLocationListenerHandler m_scLocationListenerHandler = new CLocationListenerHandler();

	// CLocationListenerHandler_MSG_ID
	private static final int MSG_OpenInternalGps 	= 800;
	private static final int MSG_CloseInternalGps 	= 801;
	private static final int MSG_StartExternalGps 	= 802;
	private static final int MSG_StopExternalGps 	= 803;

	// handler singleton object new
	private CLocationListenerHandler GetHandler() {

		return m_scLocationListenerHandler;
	}

	public void SendOpenInternalGpsMsg() {
		// send message to open internal GPS
		CLocationListenerHandler handler = GetHandler();
		Message mgs = handler.obtainMessage(MSG_OpenInternalGps);
		handler.sendMessage(mgs);
	}

	public void SendCloseInternalGpsMsg() {
		// send message to close internal GPS
		CLocationListenerHandler handler = GetHandler();
		Message mgs = handler.obtainMessage(MSG_CloseInternalGps);
		handler.sendMessage(mgs);
	}
	
	public void SendStartExternalGpsMsg(BTDevice_Info device) {
		// send message to start external GPS of Cradle Bluetooth
		CLocationListenerHandler handler = GetHandler();
		Message mgs = handler.obtainMessage(MSG_StartExternalGps, device);
		handler.sendMessage(mgs);
	}

	public void SendStopExternalGpsMsg() {
		// send message to stop external GPS of Cradle Bluetooth
		CLocationListenerHandler handler = GetHandler();
		Message mgs = handler.obtainMessage(MSG_StopExternalGps);
		handler.sendMessage(mgs);
	}

	// handle message call by CLocationListenerHandler.handleMessage
	public void handleMessage(Message msg) {
		// handle
		switch (msg.what) {
		case MSG_OpenInternalGps:
			writeGpslogfile("handleMessage - message type MSG_OpenInternalGps");
			try {
				if (!m_bCurrentInternalGpsOn) {
					// Open internal GPS
					OpenInternalGps(GPS_USING_FROMAT_KIND_ANDROID);
					// set internal GPS flag to ON
					m_bCurrentInternalGpsOn = true;
					// need to close internal GPS in BT thread
					m_bNeedBTThreadToCloseInternalGps = true;
					// file log
					writeGpslogfile("CLocationListener - add internal GPS done");
				}
			} catch (Exception e) {
				writeGpslogfile("CLocationListener - add internal GPS ERROR");
			}
			break;
		case MSG_CloseInternalGps:
			// file log
			writeGpslogfile("handleMessage - message type MSG_CloseInternalGps");
			try {
				if (m_bCurrentInternalGpsOn) {
					// Close internal GPS
					CloseInternalGps();
					// set internal GPS flag to OFF
					m_bCurrentInternalGpsOn = false;
					// reset need to close internal GPS in BT thread flag
					m_bNeedBTThreadToCloseInternalGps = false;
					// file log
					writeGpslogfile("CLocationListener - remove internal GPS done");
				}
			} catch (Exception e) {
				writeGpslogfile("CLocationListener - remove internal GPS ERROR");
			}
			break;
		case MSG_StartExternalGps:
			// file log
			writeGpslogfile("handleMessage - message type MSG_StartExternalGps");
			try {
				// Open external connect with Cradle by Bluetooth
				OpenExternalCradld((BTDevice_Info)msg.obj);
				// file log
				writeGpslogfile("CLocationListener - start external done");
			} catch (Exception e) {
				writeGpslogfile("CLocationListener - start external ERROR");
			}
			break;
		case MSG_StopExternalGps:
			// file log
			writeGpslogfile("handleMessage - message type MSG_StopExternalGps");
			try {
				// Close external connect with Cradle by Bluetooth
				CloseExternalCradle();
				// file log
				writeGpslogfile("CLocationListener - stop external done");
			} catch (Exception e) {
				writeGpslogfile("CLocationListener - stop external ERROR");
			}
			break;
		default:
			// ERROR
			writeGpslogfile("handleMessage - message type do not define");
			break;
		}
	}
	
	/*==================================================================*/
	/*
	 * Save logs process
	 */
	// the file path for log files.
	public static final String FilePath =  FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH) + "/RW/GpsLog/";
	// true if the file name has been created
	private static boolean mFileNameFlag = false;
	// store the file name.
	private String mflieName = "";
	
	// for m_bWriteLogsToFileEnable flag
	private boolean m_bWriteLogsToFileEnable = false;
	public final void setWriteLogsToFileEnable(boolean bEnable) {
		m_bWriteLogsToFileEnable = bEnable;
	}
	public final boolean getWriteLogsFile() {
		return m_bWriteLogsToFileEnable;
	}

	// write log file
	public void WriteFile(String filename, String data) {

		synchronized (this) {

			FileWriter file = null;
			try {
				// open file;
				file = new FileWriter(filename, true);
				// append data to the end of the file;
				file.append(data);
				file.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// close file;
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// create file path.
	public void createFolder(String folderPath) {

		try {
			java.io.File myFilePath = new java.io.File(folderPath);
			// create folder if file path isn't exist.
			if (!myFilePath.exists()) {

				if (myFilePath.mkdir() == false) {
					ShowMessage(" LocationListener Create Folder Failed");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ShowMessage(String message) {
		if (mJniLocInfor.GetGpsViewFlag()) {
			System.out.println(message);
			Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}
	}

	public void writeGpslogfile(String logdata) {
		// Write logs
		if (m_bWriteLogsToFileEnable) {
			// print logs to console
			Log.d(TAG, logdata);

			if (mFileNameFlag == false) {
				// change time format
				Calendar caldr = Calendar.getInstance();
				Date time1;
				time1 = caldr.getTime();
				// String time1 = String.valueOf(getTime());
				String date = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
						.format(time1);
				mflieName = FilePath + "CradleGps" + date;

				mflieName += ".txt";
				mFileNameFlag = true;

				// Create file folder //"/sdcard/GpsLog/";
				createFolder(FilePath);
			}
		
			// get time
			Calendar caldr = Calendar.getInstance();
			Date curTime;
			curTime = caldr.getTime();
			String date = new java.text.SimpleDateFormat("MM-dd HH:mm:ss.SSS  ").format(curTime);
			logdata = date + logdata;
			
			// write logs to file
			WriteFile(mflieName, logdata + "\r\n");
		}
	}
}
