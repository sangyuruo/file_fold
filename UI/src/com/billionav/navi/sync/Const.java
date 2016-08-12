package com.billionav.navi.sync;

import com.smartdevicelink.proxy.rpc.enums.Language;
import com.smartdevicelink.proxy.rpc.enums.SpeechCapabilities;

/** Stores application-wide constants. */
public class Const {
	// Shared preference name for protocol properties
	public static final String PREFS_NAME = "CameraToGLPreviewToMP4Prefs";

	// Protocol properties
	public static final String PREFS_KEY_PROTOCOLVERSION = "VersionNumber";
	public static final String PREFS_KEY_ISMEDIAAPP = "isMediaApp";
	public static final String PREFS_KEY_APPNAME = "appName";
	
	public static final String PREFS_KEY_APPSYNONYM1 = "appSynonym1";
	public static final String PREFS_KEY_APPSYNONYM2 = "appSynonym2";
	
	public static final String PREFS_KEY_APP_TTS_TEXT = "appTTSName";
	public static final String PREFS_KEY_APP_TTS_TYPE = "appTTSType";
	
	public static final String PREFS_KEY_APPID = "appId";
	public static final String PREFS_KEY_ESN = "esn";
	public static final String PREFS_KEY_LANG = "desiredLang";
	public static final String PREFS_KEY_HMILANG = "desiredHMILang";
	public static final String PREFS_KEY_AUTOSETAPPICON = "autoSetAppIcon";
	public static final String PREFS_KEY_DISABLE_LOCK_WHEN_TESTING = "disableLockWhenTesting";
	
	public static final String PREFS_KEY_DURATION = "Duration";
	public static final String PREFS_KEY_FRAMERATE = "Framerate";
	public static final String PREFS_KEY_BITRATE = "Bitrate";
	
	// Default values
	public static final int PREFS_DEFAULT_PROTOCOLVERSION = 2;
	public static final boolean PREFS_DEFAULT_ISMEDIAAPP = true;
	public static final String PREFS_DEFAULT_APPSYNONYM1 = "Synonym1";
	public static final String PREFS_DEFAULT_APPSYNONYM2 = "Synonym2";
	
	public static final String PREFS_DEFAULT_APP_TTS_TEXT = "88Map";
	public static final String PREFS_DEFAULT_APP_TTS_TYPE = SpeechCapabilities.TEXT.name();
	
	public static final String PREFS_DEFAULT_APPID = "2037240362";  //"1766825573";
	public static final String PREFS_DEFAULT_ESN = "CP7E0297";
	public static final String PREFS_DEFAULT_LANG = Language.EN_US.name();
	public static final String PREFS_DEFAULT_HMILANG = Language.EN_US.name();
	public static final boolean PREFS_DEFAULT_AUTOSETAPPICON = true;
	public static final boolean PREFS_DEFAULT_DISABLE_LOCK_WHEN_TESTING = false;

	public static final String PREFS_DEFAULT_DURATION = "5";
	public static final String PREFS_DEFAULT_FRAMERATE = "30";
	public static final String PREFS_DEFAULT_BITRATE = "6";
	// Transport properties
	public static final class Transport {
		// Protocol properties
		public static final String PREFS_KEY_TRANSPORT_TYPE = "TransportType";
		public static final String PREFS_KEY_TRANSPORT_PORT = "TCPPort";
		public static final String PREFS_KEY_TRANSPORT_IP = "IPAddress";
		public static final String PREFS_KEY_TRANSPORT_RECONNECT = "AutoReconnect";

		public static final String TCP = "WiFi";
		public static final String BLUETOOTH = "Bluetooth";
		public static final String USB = "USB";
		public static final int KEY_TCP = 1;
		public static final int KEY_BLUETOOTH = 2;
		public static final int KEY_USB = 3;

		//public static final int PREFS_DEFAULT_TRANSPORT_TYPE = KEY_TCP;
		public static final int PREFS_DEFAULT_TRANSPORT_TYPE = KEY_USB;
		public static final int PREFS_DEFAULT_TRANSPORT_PORT = 12345;
		public static final String PREFS_DEFAULT_TRANSPORT_IP = "192.168.";
		public static final boolean PREFS_DEFAULT_TRANSPORT_RECONNECT_DEFAULT = true;
	}

	// Keys to pass objects via IntentHelper
	public static final String INTENTHELPER_KEY_SOFTBUTTON = "IntentSoftButton";
	public static final String INTENTHELPER_KEY_SOFTBUTTONSLIST = "IntentSoftButtonsList";

	// Keys to pass values via Intent
	public static final String INTENT_KEY_SOFTBUTTONS_MAXNUMBER = "MaxSoftButtonsNumber";

	// Request id for SoftButtonEditActivity
	public static final int REQUEST_EDIT_SOFTBUTTON = 42;
	// Request id for SoftButtonsListActivity
	public static final int REQUEST_LIST_SOFTBUTTONS = 43;
}
