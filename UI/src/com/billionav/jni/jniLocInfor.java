package com.billionav.jni;

import com.billionav.navi.gps.CLocationListener;

public class jniLocInfor {
	
	private static jniLocInfor instance = new jniLocInfor();
	
	public boolean	gpsUpdate;		// GPS signal update flag
	public double 	lat;    		//the latitude of this fix.
	public double 	lon;    		//the longitude of this fix.
	public long 	time;     		//in second since January 1, 1970.
	
	public boolean	altitudeFlag;   //true if this fix contains altitude information, false otherwise.
	public double 	altitude;		//the altitude of this fix.
	
	public boolean 	speedFlag;     	//true if this fix contains speed information, false otherwise.
	public float 	speed;			//speed of the device over ground in meters/second
	
	public boolean 	bearingFlag;	//true if the provider is able to report bearing information, false otherwise.
	public float 	bearing;		//the direction of travel in degrees East of true North.
	
	public boolean 	accuracyFlag;	//true if the provider is able to report accuracy information, false otherwise.
	public float 	accuracy;		//the accuracy of the fix in meters.
	
	public float 	ttff;			//time to first fix.(milliseconds)
	public int 		numSatellite;	// the number of the satellite used for fix;
	
	public static final int MAX_GPS_SATELLITE = 32;
	public int 		SatelliteID[] 		= new int[MAX_GPS_SATELLITE];	//SatelliteID
	public float 	SignalStrength[] 	= new float[MAX_GPS_SATELLITE];	//SignalStrength 		
	public float 	Azimuth[] 			= new float[MAX_GPS_SATELLITE];	//Azimuth (0~360)[DEG] 
	public float 	ElevationAngle[] 	= new float[MAX_GPS_SATELLITE];	//ElevationAngle (0~90)[DEG] 

	public boolean 	hasAlmanac[] 		= new boolean[MAX_GPS_SATELLITE];	//true if the GPS engine has almanac data for the satellite.
	public boolean 	hasEphemeris[] 		= new boolean[MAX_GPS_SATELLITE];	//true if the GPS engine has ephemeris data for the satellite.
	public boolean 	usedInFix[] 		= new boolean[MAX_GPS_SATELLITE];	//true if the satellite was used by the GPS engine when calculating the most recent GPS fix.
	
	private boolean LogStatus 			= false;	//logging status;
	private boolean bShowGpsInfo 		= false;	//Show GPS information flag;

	public static jniLocInfor getLocInfor() {return instance;}
	
	private jniLocInfor() {InitForCradle();}
	
	public void makeAndSendInfo()
	{	
		//send Location information to sGpsSnsMailData by JNI;
		SendLocInfo();
		
		//send first fix time to sGpsSnsMailData by JNI;
		SendFixTime();
			
		//send satellite number to sGpsSnsMailData by JNI;
		SendSatelliteNumber();
		
		//send satelliteID to sGpsSnsMailData by JNI;
		SendSatelliteID(SatelliteID, numSatellite);
		
		//send signal strength to sGpsSnsMailData by JNI;
		SendSignalStrength(SignalStrength, numSatellite);
		
		//send Azimuth to sGpsSnsMailData by JNI;
		SendAzimuth(Azimuth, numSatellite);
		
		//send elevationAngle to sGpsSnsMailData by JNI;
		SendElevationAngle(ElevationAngle, numSatellite);
		
		//send UsedFixFlag to sGpsSnsMailData by JNI;
		SendUsedInFixFlag(usedInFix, numSatellite);
		
		//send information to GPSEngine by JNI.
		SendInfo();
	}
	
	public void ClearLocInfo()
	{
		// Keep longitude and latitude as last value
		// And then clear all flags and satellite count
		gpsUpdate = false;
		
		// Clear altitude flag
		altitudeFlag = false;
		
		// Clear speed flag
		speedFlag = false;
		
		// Clear heading flag
		bearingFlag = false;
		
		// Clear accuracy flag
		accuracyFlag = false;
		
		// Clear satellite number
		numSatellite = 0;
	}

	// Write Log file in CLocationListener;
	public boolean StartLog() {
		if (!LogStatus) {
			LogStatus = true;
			return true;
		}
		return false;
	}
	
	// Stop write file in CLocationListener;
	public boolean StopLog() {
		if (LogStatus) {
			LogStatus = false;
			return true;
		}
		return false;
	}
	
	// Get Logging Status in CLocationListener;
	public boolean GetLoggingStatus() {
		return LogStatus;
	}
	
	// Show GPS information view
	public boolean ShowGpsView() {
		if (!bShowGpsInfo) {
			bShowGpsInfo = true;
			return true;
		}
		return false;
	}
	
	// Close GPS information view
	public boolean CloseGpsView() {
		if (bShowGpsInfo) {
			bShowGpsInfo = false;
			return true;
		}
		return false;
	}
	
	// Get GPS view flag;
	public boolean GetGpsViewFlag() {
		return bShowGpsInfo;
	}

	//send information to GPSEngine by JNI.
	public native void SendInfo();
	
	//send Location information to sGpsSnsMailData by JNI;
	public native void SendLocInfo();
	
	//send first fix time to sGpsSnsMailData by JNI;
	public native void SendFixTime();
	
	//send satellite number to sGpsSnsMailData by JNI;
	public native void SendSatelliteNumber();
	
	//send satelliteID to sGpsSnsMailData by JNI;
	public native void SendSatelliteID(int[] SatellitedID, int Length);
	
	//send signal strength to sGpsSnsMailData by JNI;
	public native void SendSignalStrength(float[] SignalStrength, int Length);
	
	//send Azimuth to sGpsSnsMailData by JNI;
	public native void SendAzimuth(float[] Azimuth, int length);
	
	//send elevationAngle to sGpsSnsMailData by JNI;
	public native void SendElevationAngle(float[] ElevationAngle, int length);
	
	//send AlmanacFlag to sGpsSnsMailData by JNI;
	public native void SendAlmanacFlag(boolean[] hasAlmanac, int length);
	
	//send Ephemeris to sGpsSnsMailData by JNI;
	public native void SendEphemerisFlag(boolean[] hasEphemeris, int length);
	
	//send UsedFixFlag to sGpsSnsMailData by JNI;
	public native void SendUsedInFixFlag(boolean[] usedInFix, int length);
	
	// Send position located by network
	// position with longitude, latitude, altitude
	public native void SendNetworkPosition(boolean valid, double[] position, int length, boolean securityModuleFlag);
	
	// Send current connect status to GpsSnsModule by JNI
	public native void SendCurrentConnectStatus(int connectStatus);
	
	// Get close internal GPS opportunity
	public native boolean GetCloseInternalGpsOpportunity();
	
	/**
	* Get cradle working level status
	*
	* @param	no
	* @return	INT
	* @retval	0:	Cradle OFF INVALID
	*			1:	Cradle ON Build-in GPS 
	*			2:	Cradle ON GPS only
	*			3:	Cradle ON GPS + Gyro/G-sns
	*			4:	Cradle ON GPS NG + Gyro + G-sns
	*			5:	Cradle ON GPS + Gyro + G-sns
	*/	
	public native int GetCradleWorkingLevel();
	
	/**
	 * SetCanReceiveGpsSignalOn
	 * Set GpsSnsModule can receive GPS signal or NOT
	 *
	 * @param :		NP_BOOL bIsGpsSignalOn (NP_TRUE: can / NP_FALSE: can not)
	 * @return :	NONE
	 */
	public native void SetCanReceiveGpsSignal(boolean bCanRecGpsSignal);
	
	/* -------- Sensors Value -------- */
	// Start the sensor engine
	public native void SnsEngineStart();
	
	// Stop the sensor engine
	public native void SnsEngineStop();
	
	/* -------- Communication interface -------- */
	// Communication with external device kind
	public static final int DATA_KIND_NONE 		= 0;
	public static final int DATA_KIND_CRADLE 	= 1;
	public static final int DATA_KIND_APPRADIO 	= 2;
	public static final int DATA_KIND_NMEA 		= 3;
	public static final int DATA_KIND_OBD 		= 4;
	
	// Initialize  for Cradle
	public native void InitForCradle();
	
	// Start ExternalConncetLib model
	public native void ConnectStart(int device_kind);
	
	// Start to initialize communicate process
	public native void ConnectInitComm(int device_kind);
	
	// External connect unexpected break down
	public native void ConnectBreak(int device_kind);
	
	// ExternalConnectStop
	public native void ConnectStop(int device_kind);

	// Get data from external device
	public native void GetData(int device_kind, byte[] data, int length);

	// Write feedback data to external device
	public static void WriteData(int device_kind, byte[] data, int length)
	{		
		switch (device_kind) {
		case DATA_KIND_CRADLE:
		case DATA_KIND_OBD:
			CLocationListener.Instance().WriteDataToCradle(data, length);
			break;
		default:
			// ERROR nothing to do
			break;	
		}
	}
	
	/* -------- ExternalConnectLib interface -------- */
	// ExternalConnectLib return value define
	public static final int INALID 						= 0x00;
	public static final int STATE_OFF 					= 0x01;
	public static final int STATE_ON 					= 0x02;
	public static final int LED_SETTING_OFF 			= 0x10;
	public static final int LED_SETTING_ECO 			= 0x11;
	public static final int LED_SETTING_RED 			= 0x12;
	public static final int LED_SETTING_GREEN 			= 0x13;
	public static final int ASL_SETTING_OFF 			= 0x20;
	public static final int ASL_SETTING_LOW 			= 0x21;
	public static final int ASL_SETTING_MID 			= 0x22;
	public static final int ASL_SETTING_HIGH 			= 0x23;
	public static final int BTP_SETTING_HFP_A2DP_SPP 	= 0x30;
	public static final int BTP_SETTING_HFP_SPP 		= 0x31;
	public static final int BTP_SETTING_A2DP_SPP 		= 0x32;
	public static final int BTP_SETTING_SPP 			= 0x33;
	public static final int SYS_LOG_LVL_FATAL			= 0x40;
	public static final int SYS_LOG_LVL_ERROR			= 0x41;
	public static final int SYS_LOG_LVL_WARN			= 0x42;
	public static final int SYS_LOG_LVL_INFO			= 0x43;
	public static final int SYS_LOG_LVL_DEBUG			= 0x44;
	
	/**
	 * Get certificate result
	 *
	 * @param	NONE
	 * @return	true: successful / false: fault
	 */
	public native boolean GetCertificateRet();
	
	/**
	 * Get video out state
	 *
	 * @param	NONE
	 * @return	Get value
	 */
	public native int GetVideoOutState();
	
	/**
	 * Set video out state
	 *
	 * @param	int iState [IN]
	 * @return	NP_TRUE:	Set successful
	 *			NP_FALSE:	Set fault
	 */
	public native boolean SetVideoOutState(int iState);
	
	/**
	 * Get cradle inside SP out state
	 *
	 * @param	NONE
	 * @return	Get value
	 */
	public native int GetCradleInsideSPOutState();
	
	/**
	 * Set cradle inside SP out state
	 *
	 * @param	int iState [IN]
	 * @return	NP_TRUE:	Set successful
	 *			NP_FALSE:	Set fault
	 */
	public native boolean SetCradleInsideSPOutState(int iState);
	
	/**
	 * Get LED setting value
	 *
	 * @param	NONE
	 * @return	Get value
	 */
	public native int GetLedSettingValue();
	
	/**
	 * Set LED setting value
	 *
	 * @param	int iValue [IN]
	 * @return	NP_TRUE:	Set successful
	 *			NP_FALSE:	Set fault
	 */
	public native boolean SetLedSettingValue(int iValue);
	
	/**
	 * Get ASL setting value
	 *
	 * @param	NONE
	 * @return	Get value
	 */
	public native int GetAslSettingValue();
	
	/**
	 * Set ASL setting value
	 *
	 * @param	int iValue [IN]
	 * @return	NP_TRUE:	Set successful
	 *			NP_FALSE:	Set fault
	 */
	public native boolean SetAslSettingValue(int iValue);
	
	/**
	 * Get bluetooth profile setting value
	 *
	 * @param	NONE
	 * @return	Get value
	 */
	public native int GetBtpSettingValue();
	
	/**
	 * Set bluetooth profile setting value
	 *
	 * @param	int iValue [IN]
	 * @return	NP_TRUE:	Set successful
	 *			NP_FALSE:	Set fault
	 */
	public native boolean SetBtpSettingValue(int iValue);
	
	/**
	 * Send bluetooth profile request
	 *
	 * @param	NONE
	 * @return	NP_TRUE:	Set successful
	 *			NP_FALSE:	Set fault
	 */
	public native boolean SendBtpRequest();
	
	/**
	 * Reset sensor learning status
	 *
	 * @param	NONE
	 * @return	NP_TRUE:	Can reset
	 *			NP_FALSE:	Can not reset
	 */
	public native boolean ResetSnsLearning();

	/**
	 * Reset ECO status
	 *
	 * @param	NONE
	 * @return	NP_TRUE:	Can reset
	 *			NP_FALSE:	Can not reset
	 */
	public native boolean ResetEcoStatus();
	
	/* -------- Cradle sensor information -------- */
	public static final int CRADLD_INFO_GPS_DIM_0D 	= 0;
	public static final int CRADLD_INFO_GPS_DIM_1D	= 1;
	public static final int CRADLD_INFO_GPS_DIM_2D 	= 2;
	public static final int CRADLD_INFO_GPS_DIM_3D 	= 3;

	public static final int CRADLD_INFO_SNS_LEARN_INVALID 	= 0x0;
	public static final int CRADLD_INFO_SNS_LEARN_PULSE_OK 	= 0x1;
	public static final int CRADLD_INFO_SNS_LEARN_GYRO_OK 	= 0x2;
	public static final int CRADLD_INFO_SNS_LEARN_GSNS_OK 	= 0x4;
	public static final int CRADLD_INFO_SNS_LEARN_ALL_OK 	= 0xFFFF;
	
	public double byteToDouble(byte[] b, int count) {
		
		long l = 0;
		l=			b[0 + count];		l&=0xff;
		l+=((long)	b[1 + count]<<8);	l&=0xffff;
		l+=((long)	b[2 + count]<<16);	l&=0xffffff;
		l+=((long)	b[3 + count]<<24);	l&=0xffffffffl;
		l+=((long)	b[4 + count]<<32);	l&=0xffffffffffl;
		l+=((long)	b[5 + count]<<40);	l&=0xffffffffffffl;
		l+=((long)	b[6 + count]<<48);	l&=0xffffffffffffffl;
		l+=((long)	b[7 + count]<<56);	l&=0xffffffffffffffffl;
		return Double.longBitsToDouble(l);
	}
	
	public int byteToInt(byte[] b, int count) {
		
		int i = 0;
		i=			b[0 + count];		i&=0xff;
		i+=((long)	b[1 + count]<<8);	i&=0xffff;
		i+=((long)	b[2 + count]<<16);	i&=0xffffff;
		i+=((long)	b[3 + count]<<24);	i&=0xffffffffl;
		return i;
	}
	
	public float byteToFloat(byte[] b, int count) {
		
		int i = 0;
		i=			b[0 + count];		i&=0xff;
		i+=((long)	b[1 + count]<<8);	i&=0xffff;
		i+=((long)	b[2 + count]<<16);	i&=0xffffff;
		i+=((long)	b[3 + count]<<24);	i&=0xffffffffl;
		return Float.intBitsToFloat(i);
	}
	
	public class CradleInfo {
		
		public boolean	bValid 			= false;
		public double	dLatitude 		= 0.0f;
		public double	dLongitude 		= 0.0f;
		public int		iGpsDimension 	= 0;
		public int		iSateliteCnt 	= 0;
		public boolean	bPulseOk 		= false;
		public boolean	bGyroOk 		= false;
		public boolean	bGsnsOk 		= false;
		public double	dSumOfDeltaD	= 0.0f;
		public float	fHeading		= 0.0f;
		
		// Constructor
		CradleInfo(byte[] buffer, int iSize) {
			
			if (iSize < 40) {
				bValid = false;
			}
			else {
				bValid						= true;
				dLatitude 					= byteToDouble(buffer, 0);
				dLongitude 					= byteToDouble(buffer, 8);
				iGpsDimension 				= byteToInt(buffer, 16);
				iSateliteCnt 				= byteToInt(buffer, 20);
				final int iSnsLearnState 	= byteToInt(buffer, 24);
				dSumOfDeltaD				= byteToDouble(buffer, 28);
				fHeading					= byteToFloat(buffer, 36);
				
				if (iSnsLearnState != CRADLD_INFO_SNS_LEARN_INVALID) {
					int ret = 0;
					// Get Pulse OK
					ret = iSnsLearnState & CRADLD_INFO_SNS_LEARN_PULSE_OK;
					if (ret == CRADLD_INFO_SNS_LEARN_PULSE_OK) {
						bPulseOk = true;
					}
					// Get Gyro OK
					ret = iSnsLearnState & CRADLD_INFO_SNS_LEARN_GYRO_OK;
					if (ret == CRADLD_INFO_SNS_LEARN_GYRO_OK) {
						bGyroOk = true;
					}
					// Get G-Sns OK
					ret = iSnsLearnState & CRADLD_INFO_SNS_LEARN_GSNS_OK;
					if (ret == CRADLD_INFO_SNS_LEARN_GSNS_OK) {
						bGsnsOk = true;
					}
				}
			}
		}
	};
	
	public CradleInfo GetCradleInformation() {
		
		// Buffer
		byte[] buffer = new byte[64];
		
		// Call native interface
		final int iSize = GetCradleInfo(buffer);
		
		// Create the class
		CradleInfo sCradleInfo = new CradleInfo(buffer, iSize);
		return sCradleInfo;
	}
	
	/**
	 * Get cradle information
	 *
	 * @param	byte[] buffer [IN]
	 * @return	int result size
	 */
	public native int GetCradleInfo(byte[] buffer);
	
	// Get cradle soft version
	public static class CradleVersion {
		
		public boolean bValid = false;
		public byte byX = 0;
		public byte byY = 0;
		public byte byZ = 0;
		
		// Constructor
		CradleVersion(byte[] buffer, int iSize) {
			
			if (iSize < 3) {
				
				bValid = false;
			}
			else {
				
				bValid = true;
				byX = buffer[0];
				byY = buffer[1];
				byZ = buffer[2];
			}
		}
	};
	
	public CradleVersion GetCradleVersion() {
		
		// Buffer
		byte[] buffer = new byte[16];
		
		// Call native interface
		final int iSize = GetCradleSoftVersion(buffer);
		
		// Create the class
		CradleVersion cCradleVersion = new CradleVersion(buffer, iSize);
		return cCradleVersion;
	}
	
	/**
	 * Get soft version
	 *
	 * @param	sSoftVer	&sSoftVersion [OUT]
	 * @return	int			data size
	 */
	public native int GetCradleSoftVersion(byte[] buffer);
	
	/* -------- Cradle sensor additional information -------- */
	public static final int CRADLE_SNS_ADD_DATA_TYPE_NONE		= 0;	// Not set additional data
	public static final int CRADLE_SNS_ADD_DATA_TYPE_1			= 1;	// Set type 1 data
	
	public static final int CRADLE_PULSE_STATUSE_NO_DATA		= 0;	// Can not get pulse status
	public static final int CRADLE_PULSE_STATUSE_NOERROR		= 1;	// �����ʤ�
	public static final int CRADLE_PULSE_STATUSE_BREAK			= 2;	// ܇���źž�����줿
	public static final int CRADLE_PULSE_STATUSE_DATA_NG		= 3;	// ܇�٥ѥ륹�źŤ�����
	
	public class CradleSnsAddInfo {
		
		public int 		iSnsAddDataType = CRADLE_SNS_ADD_DATA_TYPE_NONE;	// ����׷�����Type
		public int 		iAccX			= 0;								// ǰ�᷽��μ��ٶ� [0.001G] ��Ť���
		public int 		iAccY			= 0;								// �᷽��μ��ٶ� [0.001G] ��Ť��꣨�M�з���-���: �ޥ��ʥ�, �҂�: �ץ饹)
		public int 		iPulseStatus	= CRADLE_PULSE_STATUSE_NO_DATA;		// ܇�٥ѥ륹���Ʃ`����
		public int 		iPulseCnt		= 0;								// ܇�٥ѥ륹������Ȃ�
	
		public CradleSnsAddInfo(byte[] buffer, int size) {
			
			if (size < 20) {
				// ERROR
				return;
			}
			else {
				// Get right data
				iSnsAddDataType	= byteToInt(buffer, 0);
				iAccX 			= byteToInt(buffer, 4);
				iAccY 			= byteToInt(buffer, 8);
				iPulseStatus 	= byteToInt(buffer, 12);
				iPulseCnt 		= byteToInt(buffer, 16);
			}
		}
	};
	
	public CradleSnsAddInfo GetSnsAddInfo() {
		
		// Buffer
		byte[] buffer = new byte[64];
		
		// Get data from native
		final int size = GetSnsAddDataNative(buffer);
		
		// Create the sensor additional information class
		CradleSnsAddInfo info = new CradleSnsAddInfo(buffer, size);
		return info;
	}
	
	/*
	 * Get sensor additional data from Cradle
	 */
	public native int GetSnsAddDataNative(byte[] buffer);
	
	
	/* -------- Debug info -------- */
	public static final int DEBUG_INFO_KIND_SNS_LOG 	= 0;	// SNS log phone output
	public static final int DEBUG_INFO_KIND_SNS_RESTORE = 1;	// SNS Cradle restore
	public static final int DEBUG_INFO_KIND_ECO_LOG 	= 2;	// ECO log phone output
	public static final int DEBUG_INFO_KIND_ECO_RESTORE = 3;	// ECO Cradle restore
	public static final int DEBUG_INFO_KIND_ASL_LOG 	= 4;	// ASL log phone output
	public static final int DEBUG_INFO_KIND_ASL_RESTORE = 5;	// ASL log phone output
	
	/**
	* Set ASL file information by name
	*
	* @param	const CHAR* strFileName		file name
	* @return	NP_TRUE:	successful
	*			NP_FALSE:	ERROR
	*/
	public native boolean SetAslFileInfoByName(String strFileName);
	
	/**
	* Send request of debug information
	*
	* @param	NONE
	* @return	NP_TRUE:	send successful
	*			NP_FALSE:	send fault
	*/
	public native boolean SendDebugInfoReqeuset();

	/**
	* Get debug information by kind
	*
	* @param	int eKind [IN]
	* @return	int [OUT] INALID, STATE_OFF, STATE_ON
	*/
	public native int GetDebugInfoByKind(int eKind);

	/**
	* Start to write debug logs by kind
	*
	* @param	ECL_WriteLogKind eKind	debug logs kind default ALL
	* @return	NP_TRUE:	successful
	*			NP_FALSE:	ERROR
	*/
	public native boolean StartDebugInfoByKind(int eKind);

	/**
	* Stop to write debug logs by kind
	*
	* @param	ECL_WriteLogKind eKind	debug logs kind default ALL
	* @return	NP_TRUE:	successful
	*			NP_FALSE:	ERROR
	*/
	public native boolean StopDebugInfoByKind(int eKind);
	
	/**
	* Force set dummy learning state
	*
	* @param	NP_BOOL bForce	NP_FALSE clear / NP_TRUE store
	* @return	NP_TRUE:	successful
	*			NP_FALSE:	ERROR
	*/
	public native boolean SetDummyLearn(boolean bForce);
	
	/**
	 * Get system log level
	 *
	 * @param	NONE
	 * @return	Get level
	 */
	public native int GetSystemLogLevel();
	
	/**
	 * Set system log level
	 *
	 * @param	int iSysLogLvl [IN]
	 * @return	NP_TRUE:	Set successful
	 *			NP_FALSE:	Set fault
	 */
	public native boolean SetSystemLogLevel(int iSysLogLvl);
	
	/**
	 * Send system log level request
	 *
	 * @param	NONE
	 * @return	NP_TRUE:	Set successful
	 *			NP_FALSE:	Set fault
	 */
	public native boolean SendSystemLogLevelRequest();
}
