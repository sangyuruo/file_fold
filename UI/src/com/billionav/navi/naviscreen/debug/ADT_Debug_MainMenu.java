package com.billionav.navi.naviscreen.debug;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.widget.RelativeLayout;

import com.billionav.jni.FileSystemJNI;
import com.billionav.jni.UILocationControlJNI;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.gps.CLocationListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSAnimationID;
import com.billionav.navi.naviscreen.base.PrefenrenceActivityBase;
import com.billionav.navi.system.StorageAdapter;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;
public class ADT_Debug_MainMenu extends PrefenrenceActivityBase {
	

	private CheckBoxPreference m_pChild_m_pChild_speedadjust = null;
	private CheckBoxPreference m_pChild_SubDriveMode = null;
	private CheckBoxPreference m_pChild_UILog = null;
	private CheckBoxPreference m_pChild_BTSwitcher = null;
	private CheckBoxPreference m_pChild_ForbidExitApl = null;
	private PreferenceScreen m_pChild_ServerSelection = null;
	private PreferenceScreen m_pChild_SmartLoopMapsSelection = null;
	private CheckBoxPreference m_pChild_SmartLoopUseDebugFile = null;
	private CheckBoxPreference m_pChild_DeveloperModel = null;
	private CheckBoxPreference m_pChild_AplFirstRun = null;
	private ListPreference m_pChild_NIGHTCOLOR = null;
	private ListPreference m_pChild_DAYCOLOR = null;
	private PreferenceScreen m_pChild_MapSize_Setting = null;
	private PreferenceScreen m_pChild_SIMInfo = null;
	private ListPreference m_pChild_CertificationSetting = null;
	private PreferenceScreen m_pChild_MapSkyView = null;
	private ListPreference m_pChild_MAPCOLOR = null;
	private ListPreference m_pChild_skinPref = null;
	private CheckBoxPreference m_pChild_LocationWriteLogs = null;
	private PreferenceScreen m_pChild_SetDesignFilePath = null;
	private CheckBoxPreference m_pChild_MapDragLine = null;
	private PreferenceScreen m_pChild_CradleDebugSetting = null;
	private CheckBoxPreference m_pChild_ParcelLineDisplay = null;
	private CheckBoxPreference m_pChild_ChinaDemo = null;
	private PreferenceScreen m_pChild_MyAutomotive = null;
	private PreferenceScreen m_pChild_LogSetting = null;
	private PreferenceScreen m_pChild_DebugOff = null;
	private CheckBoxPreference m_pChild_CeatecSetting = null;
	private ListPreference m_pChild_CDNconnectionsetting = null;
	private PreferenceScreen m_pChild_FlushCacheToFile = null;
	private CheckBoxPreference m_pChild_SearchLog = null;
	private PreferenceScreen m_pChild_LogRunSetting = null;
	private CheckBoxPreference m_pChild_CradleDebugInfo = null;
	private CheckBoxPreference m_pChild_SearchSetting = null;
	private CheckBoxPreference m_pChild_NMEALogBox = null;
	private CheckBoxPreference m_pChild_StartNMEABox = null;
	private CheckBoxPreference m_pChild_CoreLocationBox = null;
	private CheckBoxPreference m_pChild_LogRunningBox = null;
	private CheckBoxPreference m_pChild_GPSDisplay = null;
	private CheckBoxPreference m_pChild_MatchingBox = null;
	private CheckBoxPreference m_pChild_Ameba = null;
	
	private Preference m_pChild_Perfermance = null;
	private Preference m_pChild_Position = null;
	private CheckBoxPreference m_pChild_NameCollisionTest = null;
	
	private EditTextPreference setScrollTimePreference = null;
	private EditTextPreference setScrollDistancePreference = null;
	private Preference mapURL = null;
	private PreferenceScreen m_searchLocal = null;
	private CheckBoxPreference highWayImageRecognitionSwitch;
	
	private CheckBoxPreference displayMapTileID;
	
	private CheckBoxPreference networkExterIp = null;
	private Preference logfile = null;
	
//	private CheckBoxPreference sensorGyro;
	

//	private final jniLocationIF LocationInfo = new jniLocationIF();
//	private final jniAL_Log al_log = new jniAL_Log();
//	private static boolean GPSDisplayType = false;
	private static boolean LogRunningBoxType = false;
	private static boolean CoreLocationBoxType = false;
	private static boolean StartNMEABoxType = false;
	private static boolean NMEALogBoxType = false;
	private static final boolean SearchSettingType = false;
//	private static boolean speedadjustType = false;
//	private static final boolean SearchLogOn = true;
//	private static final boolean SearchLogOff = false;
	private static final int IPCCDNJAPAN = 0;
	private static final int PSETSEVER = 1;
	private int mSelected;
	private final String MAP_DESIGN_FILEPATH = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_PATH) + "/";
//	private final String TRANSFER_FILEPATH = "/NDATA/NDATA/";
	private final String SPLITCHAR = ".xml";
	private String[] mItem_FileName = null;
//	private jniMapLib mapLib = new jniMapLib();
	private List<String> filelist = null;
	// map design
	private final String DIALOGTITLE = "Map Design File Selection";
	private final String DEFAULT_MAP_FILEPATH = FileSystemJNI.instance().getApkDataFullPath() + "/ADATA/map/map_design.xml";
	private final String DEFAULT = "Default";
	
	// skin setting
	private final int SKIN_ORIGINAL = 0;
	private final int SKIN_OVERSEA = 1;
	private final int SKIN_JP_POP_A = 2;
	private final int SKIN_JP_POP_B = 3;
	private final int SKIN_JP_HI_END = 4;
	
	// Certification Setting
//	private DoCoMo mDoCoMo = DoCoMo.Instance();
//	private DocomoAuthDM mDocomoAuthDM = DocomoAuthDM.instance();
	private final int UNKNOWN = 0;	// unknown status
	private final int NEGATIVE = 1;	// unauthenticated
	private final int POSITIVE = 2;	// authenticated
	
	// use for map size setting
	private RelativeLayout mRelativeLayout = null;
	private AlertDialog mMapSizeDlg = null;
	private final int TEXT_VIEW_WIDTH = 0;
	private final int EDIT_TEXT_WIDTH = 1;
	private final int TEXT_VIEW_HEIGHT = 2;
	private final int EDIT_TEXT_HEIGHT = 3;
	
	// smartloop used to write debug file
//	private static boolean smartloopDebugType = true;
//	jniSmartLoopControl mSmartLoopControl = jniSmartLoopControl.Instance();
	private final String SL_DIALOG_TITLE = "SmartLoop Maps Selection";
	private final String SELECTION_ITEMS_HONBANG = "" + '\u672c' + '\u756a' + "MAPS";
	private final String SELECTION_ITEMS_KENSAKU = "" + '\u68c0'+'\u8bc1'+"2";
//	private SharedPreferenceDataPool mDataPool = null;
	private int statue = 0;
	
	private final String SERVER_TAISHOTEST = "" + '\u5bfe' + '\u5411' + '\u8a66' + '\u9a13';
	private final String SERVER_KENSHO2 = "" + '\u691c' + '\u8a3c' + "2";
	private final String SERVER_KENSHO3 = "" + '\u691c' + '\u8a3c' + "3";
//	private final String SERVER_HONBANG = "" + '\u672c' + '\u756a';
	private final String PATH_TAISHOTEST = "DDN_ServerTAIKOU";
	private final String PATH_KENSHO2 = "DDN_ServerTEST2";
	private final String PATH_KENSHO3 = "DDN_ServerTEST3";
	private final String[] PATH_ITEMS = {PATH_KENSHO2, PATH_TAISHOTEST, PATH_KENSHO3};
	private int server_statue = 0;
	private CustomDialog serverSelectionFail = null;
	
	private final String ROOT_PATH = StorageAdapter.getExternalStorageDirectory().getPath() + "/";
	private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private ProgressDialog mWaitingBar = null;
	private AlertDialog mBTSettingErrorDialog = null;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.adt_debug_main_menu);
		setTitle("Debug Setting");
		findViews();
		initialize();
		setListeners();
	}
	
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
//		setMapSize();
//		try {
//			UIC_DebugCommon.Instance().setScrollDistance(
//					Integer.parseInt(setScrollDistancePreference.getText()));
//			UIC_DebugCommon.Instance().setScrollTime(
//					Integer.parseInt(setScrollTimePreference.getText()));
//		} catch (NumberFormatException e) {
//			UIC_DebugCommon.Instance().setScrollDistance(6);
//			UIC_DebugCommon.Instance().setScrollTime(500);
//		}
//		unregisterReceiver(mBluetoothReceiver);
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
//		initSubDriveMode();
//		filelist = new ArrayList<String>();
//		mItem_FileName = null;
//		if (m_pChild_SetDesignFilePath == null) {
//			return;
//		}
//		File file = new File(MAP_DESIGN_FILEPATH);
//		if (file.exists()) {
//			String[] files = file.list();
//			filelist.add(DEFAULT);
//			for (int i = 0; i < files.length; i++) {
//				File subfile = new File(MAP_DESIGN_FILEPATH + files[i]);
//				if (!subfile.isDirectory()) {
//					if (subfile.toString().indexOf(SPLITCHAR) != -1) {
//						filelist.add(subfile.getName());
//					}
//				}
//			}
//		} else {
//			PLog.d("Tora", "No NDATA Dir");
//		}
//		if (filelist != null) {
//			mItem_FileName = new String[filelist.size()];
//			for(int i = 0; i < filelist.size(); i++) {
//				mItem_FileName[i] = filelist.get(i);
//			}
//		}
	}
	
//	private void initSubDriveMode() {
//		if (m_pChild_SubDriveMode != null) {
//			m_pChild_SubDriveMode
//					.setChecked(new jniSetupControl()
//							.GetInitialStatus(jniSetupControl.STUPDM_DRIVE_MODE) == jniSetupControl.STUPDM_COMMON_OFF);
//		}
//	}
	
//	private void setMapSize() {
//		int[] mapsize = mapSizeCheck(last_input_value_X, last_input_value_Y);
//		if (new jniSetupControl().GetMapCanvasOffsetX() == mapsize[0] && new jniSetupControl().GetMapCanvasOffsetY() == mapsize[1]) {
//			return;
//		}
//		new jniSetupControl().SetMapCanvasOffsetX((short)mapsize[0]);
//		new jniSetupControl().SetMapCanvasOffsetY((short)mapsize[1]);
////		jniMapControl mjniMapControl = new jniMapControl();
////		mjniMapControl.Instance();
////		mjniMapControl.ResetCanvasSize();
//	}
 


	private void findViews() {
		
		m_pChild_MatchingBox = (CheckBoxPreference)getPreferenceManager().findPreference("MatchingBox");
		m_pChild_GPSDisplay = (CheckBoxPreference)getPreferenceManager().findPreference("GPSDisplay");
		m_pChild_LogRunningBox = (CheckBoxPreference)getPreferenceManager().findPreference("LogRunningBox");
		m_pChild_CoreLocationBox = (CheckBoxPreference)getPreferenceManager().findPreference("CoreLocationBox");
		m_pChild_StartNMEABox = (CheckBoxPreference)getPreferenceManager().findPreference("StartNMEABox");
		m_pChild_NMEALogBox = (CheckBoxPreference)getPreferenceManager().findPreference("NMEALogBox");
		m_pChild_SearchSetting = (CheckBoxPreference)getPreferenceManager().findPreference("SearchSetting");
		m_pChild_CradleDebugInfo = (CheckBoxPreference)getPreferenceManager().findPreference("CradleDebugInfo");
		m_pChild_LogRunSetting = (PreferenceScreen)getPreferenceManager().findPreference("LogRunSetting");
		m_pChild_SearchLog = (CheckBoxPreference)getPreferenceManager().findPreference("SearchLog");
		m_pChild_FlushCacheToFile = (PreferenceScreen)getPreferenceManager().findPreference("FlushCacheToFile");
		m_pChild_CDNconnectionsetting = (ListPreference)getPreferenceManager().findPreference("CDNconnectionsetting");
		m_pChild_CeatecSetting = (CheckBoxPreference)getPreferenceManager().findPreference("CeatecSetting");
		m_pChild_DebugOff = (PreferenceScreen)getPreferenceManager().findPreference("DebugOff");
		m_pChild_LogSetting = (PreferenceScreen)getPreferenceManager().findPreference("LogSetting");
		m_pChild_MyAutomotive = (PreferenceScreen)getPreferenceManager().findPreference("MyAutomotive");
		m_pChild_ChinaDemo = (CheckBoxPreference)getPreferenceManager().findPreference("ChinaDemo");
		m_pChild_ParcelLineDisplay = (CheckBoxPreference)getPreferenceManager().findPreference("ParcelLineDisplay");
		m_pChild_CradleDebugSetting = (PreferenceScreen)getPreferenceManager().findPreference("CradleDebugSetting");
		m_pChild_MapDragLine = (CheckBoxPreference)getPreferenceManager().findPreference("MapDragLine");
		m_pChild_SetDesignFilePath = (PreferenceScreen)getPreferenceManager().findPreference("SetDesignFilePath");
		m_pChild_LocationWriteLogs = (CheckBoxPreference)getPreferenceManager().findPreference("LocationWriteLogs");
		m_pChild_skinPref = (ListPreference)getPreferenceManager().findPreference("skinPref");
		m_pChild_MAPCOLOR = (ListPreference)getPreferenceManager().findPreference("MAPCOLOR");
		m_pChild_MapSkyView = (PreferenceScreen)getPreferenceManager().findPreference("MapSkyView");
		m_pChild_CertificationSetting = (ListPreference)getPreferenceManager().findPreference("CertificationSetting");
		m_pChild_SIMInfo = (PreferenceScreen)getPreferenceManager().findPreference("SIMInfo");
		m_pChild_MapSize_Setting = (PreferenceScreen)getPreferenceManager().findPreference("MapSize_Setting");
		m_pChild_DAYCOLOR = (ListPreference)getPreferenceManager().findPreference("DAYCOLOR");
		m_pChild_NIGHTCOLOR = (ListPreference)getPreferenceManager().findPreference("NIGHTCOLOR");
		m_pChild_AplFirstRun = (CheckBoxPreference)getPreferenceManager().findPreference("AplFirstRun");
		m_pChild_DeveloperModel = (CheckBoxPreference)getPreferenceManager().findPreference("DeveloperModel");
		m_searchLocal = (PreferenceScreen)getPreferenceManager().findPreference("SearchLocal");
		m_pChild_SmartLoopUseDebugFile = (CheckBoxPreference)getPreferenceManager().findPreference("SmartLoopUseDebugFile");
		m_pChild_SmartLoopMapsSelection = (PreferenceScreen)getPreferenceManager().findPreference("SmartLoopMapsSelection");
		m_pChild_ServerSelection = (PreferenceScreen)getPreferenceManager().findPreference("ServerSelection");
		m_pChild_ForbidExitApl = (CheckBoxPreference)getPreferenceManager().findPreference("ForbidExitApl");

		m_pChild_BTSwitcher = (CheckBoxPreference)getPreferenceManager().findPreference("BTSwitcher");

		m_pChild_UILog = (CheckBoxPreference)getPreferenceManager().findPreference("UILog");

		m_pChild_SubDriveMode = (CheckBoxPreference)getPreferenceManager().findPreference("SubDriveMode");
		m_pChild_Ameba = (CheckBoxPreference)getPreferenceManager().findPreference("m_pChild_Ameba");
		if(!SystemTools.isJP()){
			m_pChild_Ameba.setEnabled(false);
		}
		m_pChild_Perfermance = getPreferenceManager().findPreference("m_pChild_Perfermance");
	
		m_pChild_Position = getPreferenceManager().findPreference("m_pChild_Position");
	
		m_pChild_NameCollisionTest = (CheckBoxPreference) getPreferenceManager().findPreference("m_pChild_NameCollisionTest");
	
		m_pChild_m_pChild_speedadjust = (CheckBoxPreference)getPreferenceManager().findPreference("m_pChild_speedadjust");
		
		setScrollTimePreference = (EditTextPreference) getPreferenceManager().findPreference("setScrollTimePreference");

		setScrollDistancePreference = (EditTextPreference) getPreferenceManager().findPreference("setScrollDistancePreference");
		
		mapURL = getPreferenceManager().findPreference("mapURL");
		
		highWayImageRecognitionSwitch = (CheckBoxPreference) getPreferenceManager().findPreference("highWayImageRecognitionSwitch");
		
//		sensorGyro = (CheckBoxPreference)getPreferenceManager().findPreference("sensorGyro");
		displayMapTileID = (CheckBoxPreference) getPreferenceManager().findPreference("displayMapTileID");
		networkExterIp = (CheckBoxPreference) getPreferenceManager().findPreference("NETWORK_EXTERNIP");
		
		logfile = getPreferenceManager().findPreference("fileLog");
	}
	
	private void initialize() {
//		registerReceiver(mBluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
//		networkExterIp.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_NETWORK_EXTERNIP_INT_KEY));
		m_pChild_MatchingBox.setChecked(SharedPreferenceData.MATCHING_LOG.getBoolean());
//		m_pChild_GPSDisplay.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_MAP_DRAW_GPS_STATION_FLAG));
//		
//		m_pChild_LogRunningBox.setChecked(LogRunningBoxType);
//		m_pChild_CoreLocationBox.setChecked(CoreLocationBoxType);
//		m_pChild_StartNMEABox.setChecked(StartNMEABoxType);
//		m_pChild_NMEALogBox.setChecked(NMEALogBoxType);
//		
//		m_pChild_SearchSetting.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_BEACON_CONNECT));
//		
//		m_pChild_CradleDebugInfo.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_CRADLE_INFO_OUTPUT));
//		
//		m_pChild_SearchLog.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_SEARCH_INFO_OUTPUT));
//
//		m_pChild_CeatecSetting.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_INFO_CEATEC));
//
//		m_pChild_ChinaDemo.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_CHINA_DEMO));
//
//		m_pChild_ParcelLineDisplay.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_PARCEL));
//		m_pChild_MapDragLine.setChecked(UIC_DebugCommon.Instance().isMapDragLineFlg());
		m_pChild_LocationWriteLogs.setChecked(SharedPreferenceData.DEBUG_LOCATIONWRITELOGS.getBoolean());
//		
//		m_pChild_SmartLoopUseDebugFile.setChecked(UIC_DebugCommon.Instance().isUseSmartloopDebugFile());
//
//		
//		m_pChild_ForbidExitApl.setChecked(UIC_DebugCommon.Instance().isExitAplforBackKey());
//
//		
//		if (mBluetoothAdapter != null) {
//			m_pChild_BTSwitcher.setChecked(mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON);
//		}
//
//		m_pChild_UILog.setChecked(PLog.getLogOutputStatus());
//		m_pChild_NameCollisionTest.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_NAME_COLLISION_STATUS));
//		m_pChild_m_pChild_speedadjust.setChecked(SharedPreferenceData.KEY_SPEED_SWITCH.getBoolean());
//		
//		if (mDataPool == null) {
//			mDataPool = new SharedPreferenceDataPool(this
//					.getApplicationInfo().processName, this);
//		}
//		m_pChild_AplFirstRun.setChecked(true);
//		
//		int urlkind = MapDataProxy.GetMapURLKind();
//		if(urlkind == MapDataProxy.MAP_URL_KIND_GOOGLE){
//			mapURL.setTitle("GoogleURL");
//		}else{
//			mapURL.setTitle("PSETURL");
//		}
//		
//		highWayImageRecognitionSwitch.setChecked(new jniNaviCommon().IsRecogHighWayPermited());
//		sensorGyro.setChecked(UIC_DebugCommon.Instance().isOpenSensorGyro());

//		displayMapTileID.setChecked(jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_TILE_ID_DISPLAY_INT_KEY));

//		setSummary();
//		
//		
//		serverSelectionFail = DialogTools.getDialogForSingleButton(this, "Failed", "Failed", 0);
//		createWaitingBar();
//		createBTSettingErrorDialog();

//		createMapSizelayout();
//		createMapSizeDialog();
		
//		int index = new jniSetupControl().GetInitialStatus(jniSetupControl.STUPDM_CDN_CONNECTION_SETTING);
//		m_pChild_CDNconnectionsetting.setSummary(m_pChild_CDNconnectionsetting.getEntries()[index]);
//		m_pChild_CDNconnectionsetting.setValueIndex(index);
//		
//		m_pChild_CertificationSetting.setValueIndex(1);
	}
	
	
	private void setListeners() {
//		networkExterIp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				// TODO Auto-generated method stub
//				if((Boolean)newValue){
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_DEBUG_NETWORK_EXTERNIP_INT_KEY,
//							jniSetupControl.STUPDM_COMMON_ON);
//				}else{
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_DEBUG_NETWORK_EXTERNIP_INT_KEY,
//							jniSetupControl.STUPDM_COMMON_OFF);
//				}
//				return true;
//			}
//		});
		m_pChild_MatchingBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				SharedPreferenceData.MATCHING_LOG.setValue((Boolean) newValue);
				if ((Boolean) newValue) {
					
					UILocationControlJNI.getInstance().StartLogging(0);
				} else {
					UILocationControlJNI.getInstance().StopLogging();
					
				}
				return true;
			}
		});
//		
//		m_pChild_GPSDisplay.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				if ((Boolean) newValue) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_DRAW_GPS_STATION_FLAG,
//							jniSetupControl.STUPDM_COMMON_ON);
////					GPSDisplayType = true;
//				} else {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_DRAW_GPS_STATION_FLAG,
//							jniSetupControl.STUPDM_COMMON_OFF);
////					GPSDisplayType = false;
//				}
//				return true;
//			}
//		});
		
//		m_pChild_LogRunningBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				if ((Boolean) newValue) {
////					if (LocationInfo.StartLogRun(jniLocationIF.LOC_MEDIA_HDD, "") == true) {
//						LogRunningBoxType = true;
////					} else {
////					}
//				} else {
//					if (LocationInfo.StopLogRun()) {
//						LogRunningBoxType = false;
//						UIC_DebugCommon.Instance().setLogRunState(false);
//					} else {
//					}
//				}
//				return true;
//			}
//		});
		
//		m_pChild_CoreLocationBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				if ((Boolean) newValue) {
//					if (LocationInfo.StartCoreLocationLog()) {
//						CoreLocationBoxType = true;
//					} 
//				} else {
//					if (LocationInfo.StopCoreLocationLog()) {
//						CoreLocationBoxType = false;
//					} 
//				}
//				return true;
//			}
//		});
		
//		m_pChild_StartNMEABox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				if ((Boolean) newValue) {
//					if (LocationInfo.StartLog(jniLocationIF.GPS_NORMAL_LOG) == true) {
//						StartNMEABoxType = true;
//					}
//				} else {
//					LocationInfo.StopLog();
//					StartNMEABoxType = false;
//				}
//				return true;
//			}
//		});
		
//		m_pChild_NMEALogBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				if ((Boolean) newValue) {
//					if (LocationInfo.StartLogRunning() == true) {
//						NMEALogBoxType = true;
//					} 
//				} else {
//					LocationInfo.StopLogRunning();
//					NMEALogBoxType = false;
//				}
//				return true;
//			}
//		});
		
//		m_pChild_SearchSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				jniSetupControl setup = new jniSetupControl();
//				if ((Boolean) newValue) {
//					setup.SetInitialStatus(
//							jniSetupControl.STUPDM_DEBUG_BEACON_CONNECT,
//							jniSetupControl.STUPDM_COMMON_ON);
//
//				} else {
//					setup.SetInitialStatus(
//							jniSetupControl.STUPDM_DEBUG_BEACON_CONNECT,
//							jniSetupControl.STUPDM_COMMON_OFF);
//				}
//				return true;
//			}
//		});
		
//		m_pChild_CradleDebugInfo.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				jniSetupControl setup = new jniSetupControl();
//				if ((Boolean) newValue) {
//					setup.SetInitialStatus(
//							jniSetupControl.STUPDM_DEBUG_CRADLE_INFO_OUTPUT,
//							jniSetupControl.STUPDM_COMMON_ON);
//
//				} else {
//					setup.SetInitialStatus(
//							jniSetupControl.STUPDM_DEBUG_CRADLE_INFO_OUTPUT,
//							jniSetupControl.STUPDM_COMMON_OFF);
//				}
//				return true;
//			}
//		});
		
		m_pChild_LogRunSetting.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
				MenuControlIF.Instance().ForwardWinChange(ADT_Debug_LogRunSetting.class);
				return true;
			}
		});
		
//		m_pChild_SearchLog.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				jniSetupControl.converse(jniSetupControl.STUPDM_DEBUG_SEARCH_INFO_OUTPUT);
//				return true;
//			}
//		});
//		
//		m_pChild_FlushCacheToFile.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
////				boolean returnValue = al_log.FlushCache();
//				new AlertDialog.Builder(ADT_Debug_MainMenu.this)
//					.setMessage("Success")
//					.setTitle("Flush Cache To File")
//					.setPositiveButton("OK", new OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//					}
//					
//				}).create().show();
//
//				return true;
//			}
//		});
//		
//		m_pChild_CDNconnectionsetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				int index = Integer.parseInt(newValue.toString());
//				jniSetupControl.set(jniSetupControl.STUPDM_CDN_CONNECTION_SETTING, index);
//				m_pChild_CDNconnectionsetting.setSummary(m_pChild_CDNconnectionsetting.getEntries()[index]);
//				return true;
//			}
//		});
//		
//		m_pChild_CeatecSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				jniSetupControl.converse(jniSetupControl.STUPDM_DEBUG_INFO_CEATEC);
//				return true;
//			}
//		});
//		
//		m_pChild_DebugOff.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				
//				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_L2R);
//				jniSetupControl setup = new jniSetupControl();
//				setup.SetInitialStatus(jniSetupControl.STUPDM_DEBUG_DEVELOP_DBG,
//						jniSetupControl.STUPDM_COMMON_OFF);
//				MenuControlIF.Instance().BackWinChange();
//				return true;
//			}
//		});
//		m_searchLocal.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
////				MenuControlIF.Instance().ForwardWinChange(ADT_SearchLC_Main.class);
//				return true;
//			}
//		});
		m_pChild_LogSetting.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
				MenuControlIF.Instance().ForwardWinChange(ADT_Debug_LogSetting.class);
				return true;
			}
		});
//		
//		m_pChild_MyAutomotive.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
//				MenuControlIF.Instance().ForwardWinChange(ADT_Debug_MyActoActivity.class);
//				return true;
//			}
//		});
//		
//		m_pChild_ChinaDemo.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				jniSetupControl.converse(jniSetupControl.STUPDM_DEBUG_CHINA_DEMO);
//				return true;
//			}
//		});
//		
//		m_pChild_ParcelLineDisplay.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				jniSetupControl.converse(jniSetupControl.STUPDM_DEBUG_PARCEL);
//				return true;
//			}
//		});
//		
//		m_pChild_CradleDebugSetting.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
//				MenuControlIF.Instance().ForwardWinChange(ADT_Debug_CradleSetting.class);
//				return true;
//			}
//		});
//		
//		m_pChild_MapDragLine.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				UIC_DebugCommon.Instance().setMapDragLineFlg((Boolean)newValue);
//				return true;
//			}
//		});
//		
//		m_pChild_SetDesignFilePath.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				if (mItem_FileName == null || mItem_FileName.length == 0) {
//					new AlertDialog.Builder(
//							ADT_Debug_MainMenu.this).setTitle("CAUTION!").setMessage("No XML File").
//							setNeutralButton("OK", new DialogInterface.OnClickListener() {
//								public void onClick(
//										DialogInterface dialoginterface, int i) {
////									jniVoicePlayIF.Instance().PlayBeep(
////											jniVoicePlayIF.BEEP_ID_TONE1);
//								}
//							}).create().show();
//				} else {
//					new AlertDialog.Builder(
//							ADT_Debug_MainMenu.this).setTitle(DIALOGTITLE)
//							.setItems(mItem_FileName, new DialogInterface.OnClickListener() {
//
////								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									// TODO Auto-generated method stub
////									jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE1);
//									if (which == 0) {
//										new jniSetupControl().SetDesignFilePath(DEFAULT_MAP_FILEPATH.toCharArray(), DEFAULT_MAP_FILEPATH.length());
//									} else {
//										String temp = MAP_DESIGN_FILEPATH + mItem_FileName[which];
////										boolean returnVal = mapLib.SetDesignFilePath(TRANSFER_FILEPATH + mItem_FileName[which]);
//										new jniSetupControl().SetDesignFilePath(temp.toCharArray(), temp.length());
//									}
////									char[] temp = new char[2000];
////									PLog.d("Tora", String.valueOf(new jniSetupControl().GetDesignFilePath()));
////									for(int i = 0; i < a.length; i++) {
////										if(a[i] == 0) {
////											break;
////										}
////										temp[i] = (char)a[i];
////									}
////									String ab = String.valueOf(temp);
////									PLog.d("Tora", ab);
//								}
//							}).setOnKeyListener(new BeepKeyListener()).create().show();
//				}
//				return true;
//			}
//		});
//		
		m_pChild_LocationWriteLogs.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				SharedPreferenceData.DEBUG_LOCATIONWRITELOGS.setValue((Boolean) newValue);
				CLocationListener.Instance().setWriteLogsToFileEnable((Boolean) newValue);
				
				return true;
			}
		});
//		
//		m_pChild_skinPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				String value = newValue.toString();
//				preference.setSummary(value);
//				if("Original".equals(value)) {
//					jniSetupControl.set(jniSetupControl.STUPDM_SYS_MENU_SKIN, jniSetupControl.STUPDM_SKIN_ORIGIN);
//				} else if("World Pioneer".equals(value)) {
//					jniSetupControl.set(jniSetupControl.STUPDM_SYS_MENU_SKIN, jniSetupControl.STUPDM_SKIN_OVERSEA);
//				} else if("Japanese popular A".equals(value)) {
//					jniSetupControl.set(jniSetupControl.STUPDM_SYS_MENU_SKIN, jniSetupControl.STUPDM_SKIN_RAKU_WHIDE);
//				} else if("Japanese popular B".equals(value)) {
//					jniSetupControl.set(jniSetupControl.STUPDM_SYS_MENU_SKIN, jniSetupControl.STUPDM_SKIN_RAKU_BLACK);
//				} else if("Japanese Hi-end".equals(value)) {
//					jniSetupControl.set(jniSetupControl.STUPDM_SYS_MENU_SKIN, jniSetupControl.STUPDM_SKIN_CYBER);
//				}
//				return true;
//			}
//		});
//		
//		m_pChild_MAPCOLOR.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				int index = Integer.parseInt(newValue.toString());
//				switch (index) {
//				case jniSetupControl.STUPDM_MAP_COLOR_CHANGE_BY_TIME:
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAPCOLOR_CHANGE,
//							jniSetupControl.STUPDM_MAP_COLOR_CHANGE_BY_TIME);
//					m_pChild_MAPCOLOR.setSummary("Change with Time");
//					PLog.v("mapColor","Time");
//					break;
//				case jniSetupControl.STUPDM_MAP_COLOR_CHANGE_DAY:
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAPCOLOR_CHANGE,
//							jniSetupControl.STUPDM_MAP_COLOR_CHANGE_DAY);
//					m_pChild_MAPCOLOR.setSummary("Fixed/Daytime");
//					PLog.v("mapColor","Day");
//					break;
//				case jniSetupControl.STUPDM_MAP_COLOR_CHANGE_NIGHT:
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAPCOLOR_CHANGE,
//							jniSetupControl.STUPDM_MAP_COLOR_CHANGE_NIGHT);
//					m_pChild_MAPCOLOR.setSummary("Fixed/Nighttime");
//					PLog.v("mapColor","Night");
//					break;
//				default:
//					break;
//			}
//				return false;
//			}
//		});
//		
//		m_pChild_MapSkyView.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class);
//				return true;
//			}
//		});
//		
//		m_pChild_CertificationSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				m_pChild_CertificationSetting.setSummary("ccid:" /*+ mDocomoAuthDM.getCCID()+", spid:"+mDocomoAuthDM.getSPID()*/);
//				m_pChild_CertificationSetting.setValueIndex(1);
//				return true;
//			}
//			
//		});
//		
//		m_pChild_SIMInfo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.Menu2Menu_Slide_R2L);
//				MenuControlIF.Instance().ForwardWinChange(ADT_Debug_SIMInfo.class);
//				return true;
//			}
//		});
//		
//		m_pChild_MapSize_Setting.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				refreshTextValue();
//				mMapSizeDlg.show();
//				ShowSoftKeyBoard(null, true);
//				return true;
//			}
//		});
//		
//		m_pChild_DAYCOLOR.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				if (newValue.equals("red")) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_DAY_COLOR,
//							jniSetupControl.STUPDM_MAP_COLOR_WORLD_RED);
//					m_pChild_DAYCOLOR.setSummary("World Pioneer Red");
//				}
//				if (newValue.equals("blue")) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_DAY_COLOR,
//							jniSetupControl.STUPDM_MAP_COLOR_WORLD_BLUE);
//					m_pChild_DAYCOLOR.setSummary("World Pioneer Blue");
//				}
//				if (newValue.equals("popular_a")) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_DAY_COLOR,
//							jniSetupControl.STUPDM_MAP_COLOR_JAPAN_POPULAR_A);
//					m_pChild_DAYCOLOR.setSummary("Japanese popular A");
//				}
//				if (newValue.equals("popular_b")) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_DAY_COLOR,
//							jniSetupControl.STUPDM_MAP_COLOR_JAPAN_POPULAR_B);
//					m_pChild_DAYCOLOR.setSummary("Japanese popular B");
//				}
//				if (newValue.equals("hi_end")) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_DAY_COLOR,
//							jniSetupControl.STUPDM_MAP_COLOR_JAPAN_HI_END);
//					m_pChild_DAYCOLOR.setSummary("Japanese Hi-end");
//				}
//				return true;
//			}
//		});
//		
//		m_pChild_NIGHTCOLOR.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				if (newValue.equals("red")) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_NIGHT_COLOR,
//							jniSetupControl.STUPDM_MAP_COLOR_WORLD_RED);
//					m_pChild_NIGHTCOLOR.setSummary("World Pioneer Red");
//				}
//				if (newValue.equals("blue")) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_NIGHT_COLOR,
//							jniSetupControl.STUPDM_MAP_COLOR_WORLD_BLUE);
//					m_pChild_NIGHTCOLOR.setSummary("World Pioneer Blue");
//				}
//				if (newValue.equals("popular_a")) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_NIGHT_COLOR,
//							jniSetupControl.STUPDM_MAP_COLOR_JAPAN_POPULAR_A);
//					m_pChild_NIGHTCOLOR.setSummary("Japanese popular A");
//				}
//				if (newValue.equals("popular_b")) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_NIGHT_COLOR,
//							jniSetupControl.STUPDM_MAP_COLOR_JAPAN_POPULAR_B);
//					m_pChild_NIGHTCOLOR.setSummary("Japanese popular B");
//				}
//				if (newValue.equals("hi_end")) {
//					new jniSetupControl().SetInitialStatus(
//							jniSetupControl.STUPDM_MAP_NIGHT_COLOR,
//							jniSetupControl.STUPDM_MAP_COLOR_JAPAN_HI_END);
//					m_pChild_NIGHTCOLOR.setSummary("Japanese Hi-end");
//				}
//				return true;
//			}
//		});
//		
//		m_pChild_AplFirstRun.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
////				mDataPool.putBoolean("AplFirstRun", m_pChild_AplFirstRun.isChecked());
//				return false;
//			}
//		});
//		m_pChild_DeveloperModel.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		});
//		
//		m_pChild_SmartLoopUseDebugFile.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				UIC_DebugCommon.Instance().setUseSmartloopDebugFile((Boolean) newValue);
//				return true;
//			}
//		});
//		
//		m_pChild_SmartLoopMapsSelection.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				String temp[] = new String[]{SELECTION_ITEMS_KENSAKU, SELECTION_ITEMS_HONBANG};
//				statue = new jniSetupControl().GetInitialStatus(jniSetupControl.STUPDM_SMARTLOOP_MAPURL);
//				new AlertDialog.Builder(
//						ADT_Debug_MainMenu.this).setTitle(SL_DIALOG_TITLE)
//						.setOnKeyListener(new BeepKeyListener())
//						.setSingleChoiceItems(temp, statue, 
//								new OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								PLog.d("Tora", "onClick which:" + which);
////								jniVoicePlayIF.Instance().PlayBeep(
////										jniVoicePlayIF.BEEP_ID_TONE1);
//								boolean hasChanged = statue != which;
//								if (hasChanged) {
//									jniSmartLoopControl.Instance().UseURLMaps(
//											which != 0);
////									jniDirectionsIF.Instance().UseURLMaps(
////											which != 0);
//									UISearchControlJNI.Instance().UseURLMaps(
//											which != 0);
//									new jniSetupControl()
//											.SetInitialStatus(
//													jniSetupControl.STUPDM_SMARTLOOP_MAPURL,
//													which != 0 ? jniSetupControl.STUPDM_COMMON_ON
//															: jniSetupControl.STUPDM_COMMON_OFF);
//								}
//								dialog.cancel();
//							}
//							
//						} ).create().show();
//				return true;
//			}
//		});
//		
//		m_pChild_ServerSelection.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				String items[] = {SERVER_KENSHO2, SERVER_TAISHOTEST, SERVER_KENSHO3};
//				server_statue = readFile(ROOT_PATH);
//				new AlertDialog.Builder(
//						ADT_Debug_MainMenu.this).setTitle(SL_DIALOG_TITLE)
//						.setOnKeyListener(new BeepKeyListener())
//						.setSingleChoiceItems(items, server_statue, 
//								new OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								if (server_statue != which) {
//									if (deleteFiles(ROOT_PATH)) {
//										if (createFile(PATH_ITEMS[which], ROOT_PATH)) {
//											UIC_DebugCommon.Instance().setExitAplforBackKey(false);
//											SystemTools.exitSystem();
//										}
//									}
//								} else {
//									dialog.cancel();
//								}
//							}
//							
//						} ).create().show();
//				return true;
//			}
//		});
//		
//		m_pChild_ForbidExitApl.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				UIC_DebugCommon.Instance().setExitAplforBackKey((Boolean) newValue);
//				return true;
//			}
//		});
//		
//		m_pChild_BTSwitcher.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				if (mBluetoothAdapter == null) {
//					return false;
//				}
//				if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
//					m_pChild_BTSwitcher.setChecked(false);
//					if (!openBT()) {
//						if (mBTSettingErrorDialog != null) {
//							mBTSettingErrorDialog.show();
//						}
//						return false;
//					}
//				} else if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
//					m_pChild_BTSwitcher.setChecked(true);
//					if (!closeBT()) {
//						if (mBTSettingErrorDialog != null) {
//							mBTSettingErrorDialog.show();
//						}
//						return false;
//					}
//				}
//				if (mWaitingBar != null) {
//					mWaitingBar.show();
//				}
//				return true;
//			}
//		});
//		
//		m_pChild_UILog.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				PLog.setLogOutputStatus((Boolean) newValue);
//				return true;
//			}
//		});
//		
//		m_pChild_SubDriveMode.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				if(!(Boolean)newValue) {
//					new jniSetupControl().SetInitialStatus(jniSetupControl.STUPDM_DRIVE_MODE,
//							jniSetupControl.STUPDM_COMMON_ON);
//					MenuControlIF.Instance().DriveModeChanged();
//				} else {
//					m_pChild_SubDriveMode.setChecked(false);
//					new AlertDialog.Builder(ADT_Debug_MainMenu.this)
//					.setIcon(android.R.drawable.ic_dialog_alert)
//					.setTitle("Fellow Passenger Operation").setMessage(
//							"While driving, if driver pays attenion to navi operation, it is very easy to cause accident. Very danger!Please don't operate while driving.").setNegativeButton(
//							"Disagree", new OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
////									jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE2);
//								}
//							}).setPositiveButton("Agree",
//							new OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
////									jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE1);
//									new jniSetupControl().SetInitialStatus(jniSetupControl.STUPDM_DRIVE_MODE,
//											jniSetupControl.STUPDM_COMMON_OFF);
//									MenuControlIF.Instance().DriveModeChanged();
//									m_pChild_SubDriveMode.setChecked(true);
//								}
//							}).setOnKeyListener(new OnKeyListener() {
//
//								public boolean onKey(DialogInterface dialog, int keyCode,
//										KeyEvent event) {
////									jniVoicePlayIF.Instance().PlayBeep(
////											jniVoicePlayIF.BEEP_ID_TONE2);
//									dialog.cancel();
//									return false;
//								}
//								
//							}).create().show();
//				}
//				return true;
//			}
//		});
//		
//		m_pChild_Perfermance.setOnPreferenceClickListener(new OnPreferenceClickListener(){
//			public boolean onPreferenceClick(Preference preference) {
//				MenuControlIF.Instance().ForwardWinChange(ADT_Debug_PerformanceTest.class);
//				return true;
//		}});
//		m_pChild_Position.setOnPreferenceClickListener(new OnPreferenceClickListener(){
//			public boolean onPreferenceClick(Preference preference) {
//				MenuControlIF.Instance().ForwardWinChange(ADT_Vehicle_Adjust.class);
//				return true;
//		}});
//		
//		m_pChild_NameCollisionTest.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				boolean value = (Boolean) newValue;
//				if(value){
//					new jniSetupControl().SetInitialStatus(jniSetupControl.STUPDM_DEBUG_NAME_COLLISION_STATUS, jniSetupControl.STUPDM_COMMON_ON);
//				}else{
//					new jniSetupControl().SetInitialStatus(jniSetupControl.STUPDM_DEBUG_NAME_COLLISION_STATUS, jniSetupControl.STUPDM_COMMON_OFF);
//				}
//				return true;
//			}
//		});
//		
//		m_pChild_m_pChild_speedadjust.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				SharedPreferenceData.KEY_SPEED_SWITCH.setValue((Boolean)newValue);
//				return true;
//			}
//		});
//		
//		mapURL.setOnPreferenceClickListener(new OnPreferenceClickListener(){
//			public boolean onPreferenceClick(Preference preference) {
//				if("PSETURL".equals(preference.getTitle())){
//					MapDataProxy.SetMapURLKind(MapDataProxy.MAP_URL_KIND_GOOGLE);
//					mapURL.setTitle("GoogleURL");
//				}else{
//					MapDataProxy.SetMapURLKind(MapDataProxy.MAP_URL_KIND_PSET);
//					mapURL.setTitle("PSETURL");
//				}
//				return true;
//			}
//		});
//
//		highWayImageRecognitionSwitch.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//
//			@Override
//			public boolean onPreferenceChange(Preference preference,
//					Object newValue) {
//				boolean value = (Boolean)newValue;
////				new jniNaviCommon().SetRecogHighWayPermission(value);
//				return true;
//			}});
//		
//		if(SystemTools.isJP()){
//			m_pChild_Ameba.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//				
//				@Override
//				public boolean onPreferenceChange(Preference preference, Object newValue) {
//					if((Boolean)newValue){
//						m_pChild_Ameba.setChecked(false);
//					    AlertDialog.Builder b = new AlertDialog.Builder(ADT_Debug_MainMenu.this);
//					    b.setTitle("Setting Remain Battery");
//						b.setSingleChoiceItems(new String[]{"5","10","15"}, 1, new OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								DataSyncControl_AmebaParam amebaParam = new DataSyncControl_AmebaParam();
//							    amebaParam.setStrCarID("10000");
//							    amebaParam.setStrModelID("0");
//							    amebaParam.setStrDriveID("0");
//							    amebaParam.setStrLon("141.139443");
//							    amebaParam.setStrLat("39.698889");
//							    amebaParam.setStrRoadType("UNKNOWN");
//							    amebaParam.setStrDepartureTime("20120421080000");
//							    amebaParam.setStrConition("ECO_HIGHWAY");
//								switch (which) {
//								case 0:
//									amebaParam.setStrRemainBattery("5");
//									break;
//								case 1:
//									amebaParam.setStrRemainBattery("10");
//									break;
//								case 2:
//									amebaParam.setStrRemainBattery("15");
//									break;
//								default:
//									break;
//								}
//								amebaParam.setStrAirCon("MANUAL");
//							    amebaParam.setStrAtrConStep("0");
//							    amebaParam.setStrGeoSystem("TOKYO97");
//							    amebaParam.setStrResolution("2");
//							    amebaParam.setStrRemainRate("1.0");
//							    DataSyncControl_ManagerIF.Instance().OnAmebaRequest(amebaParam);
//								dialog.dismiss();
//							}
//						}).create().show();
//					}
//					else{
////						MapEngineJni.SetAmeba("", false);
//					}
//					
//					return true;
//				}
//			
//			});
//		}
//		
//		displayMapTileID.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				if((Boolean)newValue) {
//					jniSetupControl.setOn(jniSetupControl.STUPDM_DEBUG_TILE_ID_DISPLAY_INT_KEY);
//				} else {
//					jniSetupControl.setOff(jniSetupControl.STUPDM_DEBUG_TILE_ID_DISPLAY_INT_KEY);
//				}
//				return true;
//			}
//		});
//
//		sensorGyro.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				UIC_DebugCommon.Instance().setOpenSensorGyro((Boolean)newValue);
//				if((Boolean)newValue) {
//					LocSnsListener.instance().OpenSensorGyro();
//				} else {
//					LocSnsListener.instance().CloseSensorGyro();
//				}
//				return true;
//			}
//		});
		
		logfile.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				ForwardWinChange(ADT_Debug_FileLog.class);
				return true;
			}
		});
	}
//	@Override
//	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
//		if(cTriggerInfo.GetTriggerID() == NSTriggerID.UIC_MN_TRG_DS_GETAMEBA){			
//			if(SystemTools.isJP()){
//			//0 = success,
//			//1 = server failure,
//			//2 = local failure
//				if(cTriggerInfo.GetlParam1() != 0){
//					m_pChild_Ameba.setChecked(false);
//					Toast.makeText(this, "ameba request failure!", 1000).show();
//					return true;
//				}
//				m_pChild_Ameba.setChecked(true);
//				DataSyncControl_AmebaData data = DataSyncControl_ManagerIF.Instance().getM_cAmebaData();
//				String result = data.getStrMutiplePolygonsLonLat();
////				MapEngineJni.SetAmeba(result, true);
//				MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class);
//			}
//			return true;
//		}
//
//		return false;
//	}
//	private void setSummary() {
//		if (m_pChild_skinPref != null) {
//			int skinType = new jniSetupControl()
//					.GetInitialStatus(jniSetupControl.STUPDM_SYS_MENU_SKIN);
//			switch (skinType) {
//			case jniSetupControl.STUPDM_SKIN_ORIGIN:
//				m_pChild_skinPref.setSummary("Original");
//				m_pChild_skinPref.setValueIndex(0);
//				break;
//			case jniSetupControl.STUPDM_SKIN_OVERSEA:
//				m_pChild_skinPref.setSummary("World Pioneer");
//				m_pChild_skinPref.setValueIndex(1);
//				break;
//			case jniSetupControl.STUPDM_SKIN_RAKU_WHIDE:
//				m_pChild_skinPref.setSummary("Japanese popular A");
//				m_pChild_skinPref.setValueIndex(2);
//				break;
//			case jniSetupControl.STUPDM_SKIN_RAKU_BLACK:
//				m_pChild_skinPref.setSummary("Japanese popular B");
//				m_pChild_skinPref.setValueIndex(3);
//				break;
//			case jniSetupControl.STUPDM_SKIN_CYBER:
//				m_pChild_skinPref.setSummary("Japanese Hi-end");
//				m_pChild_skinPref.setValueIndex(4);
//				break;
//			default:
//				break;
//			}
//		}
//		
//		switch (new jniSetupControl()
//				.GetInitialStatus(jniSetupControl.STUPDM_MAPCOLOR_CHANGE)) {
//		case jniSetupControl.STUPDM_MAP_COLOR_CHANGE_BY_TIME:
//			m_pChild_MAPCOLOR.setSummary("Change with Time");
//			m_pChild_MAPCOLOR.setValueIndex(0);
//			break;
//		case jniSetupControl.STUPDM_MAP_COLOR_CHANGE_DAY:
//			m_pChild_MAPCOLOR.setSummary("Fixed/Daytime");
//			m_pChild_MAPCOLOR.setValueIndex(1);
//			break;
//		case jniSetupControl.STUPDM_MAP_COLOR_CHANGE_NIGHT:
//			m_pChild_MAPCOLOR.setSummary("Fixed/Nighttime");
//			m_pChild_MAPCOLOR.setValueIndex(2);
//			break;
//		default:
//			break;
//		}
//
//		// Map Day Color
//		switch (new jniSetupControl()
//				.GetInitialStatus(jniSetupControl.STUPDM_MAP_DAY_COLOR)) {
//		case jniSetupControl.STUPDM_MAP_COLOR_WORLD_RED:
//			m_pChild_DAYCOLOR.setSummary("World Pioneer Red");
//			m_pChild_DAYCOLOR.setValue("red");
//			break;
//		case jniSetupControl.STUPDM_MAP_COLOR_WORLD_BLUE:
//			m_pChild_DAYCOLOR.setSummary("World Pioneer Blue");
//			m_pChild_DAYCOLOR.setValue("blue");
//			break;
//		case jniSetupControl.STUPDM_MAP_COLOR_JAPAN_POPULAR_A:
//			m_pChild_DAYCOLOR.setSummary("Japanese popular A");
//			m_pChild_DAYCOLOR.setValue("popular_a");
//			break;
//		case jniSetupControl.STUPDM_MAP_COLOR_JAPAN_POPULAR_B:
//			m_pChild_DAYCOLOR.setSummary("Japanese popular B");
//			m_pChild_DAYCOLOR.setValue("popular_b");
//			break;
//		case jniSetupControl.STUPDM_MAP_COLOR_JAPAN_HI_END:
//			m_pChild_DAYCOLOR.setSummary("Japanese Hi-end");
//			m_pChild_DAYCOLOR.setValue("hi_end");
//			break;
//		default:
//			break;
//		}
//
//		// Map Night Color
//		switch (new jniSetupControl()
//				.GetInitialStatus(jniSetupControl.STUPDM_MAP_NIGHT_COLOR)) {
//		case jniSetupControl.STUPDM_MAP_COLOR_WORLD_RED:
//			m_pChild_NIGHTCOLOR.setSummary("World Pioneer Red");
//			m_pChild_NIGHTCOLOR.setValue("red");
//			break;
//		case jniSetupControl.STUPDM_MAP_COLOR_WORLD_BLUE:
//			m_pChild_NIGHTCOLOR.setSummary("World Pioneer Blue");
//			m_pChild_NIGHTCOLOR.setValue("blue");
//			break;
//		case jniSetupControl.STUPDM_MAP_COLOR_JAPAN_POPULAR_A:
//			m_pChild_NIGHTCOLOR.setSummary("Japanese popular A");
//			m_pChild_NIGHTCOLOR.setValue("popular_a");
//			break;
//		case jniSetupControl.STUPDM_MAP_COLOR_JAPAN_POPULAR_B:
//			m_pChild_NIGHTCOLOR.setSummary("Japanese popular B");
//			m_pChild_NIGHTCOLOR.setValue("popular_b");
//			break;
//		case jniSetupControl.STUPDM_MAP_COLOR_JAPAN_HI_END:
//			m_pChild_NIGHTCOLOR.setSummary("Japanese Hi-end");
//			m_pChild_NIGHTCOLOR.setValue("hi_end");
//			break;
//		default:
//			break;
//		}
//	}
//	
//	private void createBTSettingErrorDialog() {
//		mBTSettingErrorDialog = new AlertDialog.Builder(this).setMessage(
//				"Setting Error").setTitle("Setting Error").setNegativeButton(
//				"Cancel", new OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//				}).create();
//	}
//	
//	private void createWaitingBar() {
//		mWaitingBar = new ProgressDialog(this);
//		mWaitingBar.setIndeterminate(true);
//		mWaitingBar.setMessage("Waiting");
//		mWaitingBar.setCancelable(false);
//		mWaitingBar.setTitle("Waiting");
//	}
//	
//	private void cancelWaitingBar() {
//		if (mWaitingBar != null && mWaitingBar.isShowing()) {
//			mWaitingBar.cancel();
//		}
//	}
//	
//	private boolean openBT() {
//		if (mBluetoothAdapter == null) {
//			return false;
//		}
//		return mBluetoothAdapter.enable();
//	}
//	
//	private boolean closeBT() {
//		if (mBluetoothAdapter == null) {
//			return false;
//		}
//		return mBluetoothAdapter.disable();
//	}


//	private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (m_pChild_BTSwitcher == null || mBluetoothAdapter == null) {
//				PLog.w("Tora", "BluetoothAdapter is null or m_pChild_BTSwitcher is null");
//				return;
//			}
//			if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
//				cancelWaitingBar();
//				m_pChild_BTSwitcher.setChecked(false);
//			} else if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
//				cancelWaitingBar();
//				m_pChild_BTSwitcher.setChecked(true);
//			}
//		}
//
//	};
//	
//	private InputFilter mNumberFilter = new InputFilter() {
//		public CharSequence filter(CharSequence source, int start,
//				int end, Spanned dest, int dstart, int dend) {
//			if (source.toString().matches("[0-9]{0,3}")) {
//				return source;
//			}
//			return "";
//		}
//	};
//	
//	private void ShowSoftKeyBoard(View v, boolean bIsShown) {
//		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//		if (true == bIsShown) {
//			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//		} else {
//			if (null == v) {
//				return;
//			}
//			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//		}
//	}
//	
//	private EditText et_width  = null;
//	private EditText et_height  = null;
//	private int last_input_value_X = 0;
//	private int last_input_value_Y = 0;
//	private void createMapSizelayout() {
//		mRelativeLayout = new RelativeLayout(this);
//		TextView tv_width = new TextView(this);
//		tv_width.setTextColor(Color.WHITE);
//		tv_width.setTextSize(20);
//		tv_width.setText("Width");
//		
//		et_width = new EditText(this);
//		et_width.setFilters(new InputFilter[] { mNumberFilter , new InputFilter.LengthFilter(3)});
//		et_width.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
//		
//		TextView tv_height = new TextView(this);
//		tv_height.setTextColor(Color.WHITE);
//		tv_height.setTextSize(20);
//		tv_height.setText("Height");
//		
//		et_height = new EditText(this);
//		et_height.setFilters(new InputFilter[] { mNumberFilter , new InputFilter.LengthFilter(3)});
//		et_height.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
//		
//		mRelativeLayout.addView(tv_width, TEXT_VIEW_WIDTH);
//		mRelativeLayout.addView(et_width, EDIT_TEXT_WIDTH);
//		mRelativeLayout.addView(tv_height, TEXT_VIEW_HEIGHT);
//		mRelativeLayout.addView(et_height, EDIT_TEXT_HEIGHT);
//		RelativeLayout.LayoutParams param_tv_width = (RelativeLayout.LayoutParams)tv_width.getLayoutParams();
//		RelativeLayout.LayoutParams param_tv_height = (RelativeLayout.LayoutParams)tv_height.getLayoutParams();
//		RelativeLayout.LayoutParams param_et_width = (RelativeLayout.LayoutParams)et_width.getLayoutParams();
//		RelativeLayout.LayoutParams param_et_height = (RelativeLayout.LayoutParams)et_height.getLayoutParams();
//		param_tv_width.topMargin = 10;
//		param_tv_width.leftMargin = 30;
//		param_tv_width.width = 100;
//		
//		param_et_width.leftMargin = 200;
//		param_et_width.width = 100;
//		
//		param_tv_height.leftMargin = 30;
//		param_tv_height.topMargin = 80;
//		param_tv_height.width = 100;
//		
//		param_et_height.leftMargin = 200;
//		param_et_height.topMargin = 80;
//		param_et_height.width = 100;
//		
//		tv_width.setLayoutParams(param_tv_width);
//		tv_height.setLayoutParams(param_tv_height);
//		et_width.setLayoutParams(param_et_width);
//		et_height.setLayoutParams(param_et_height);
//	}
//	
//	private void createMapSizeDialog() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Map Size Setting")
//		.setNegativeButton("Cancel", new OnClickListener() {
//
////			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				ShowSoftKeyBoard(mRelativeLayout, false);
//			}
//			
//		}).setPositiveButton("OK", new OnClickListener() {
//
////			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				int offsetX, offsetY;
//				try {
//					offsetX = Integer.parseInt(et_width.getText().toString());
//					offsetY = Integer.parseInt(et_height.getText().toString());
//				} catch (NumberFormatException e) {
//					offsetX = 0;
//					offsetY = 0;
//				}
//				int[] mapsize = mapSizeCheck(offsetX, offsetY);
//				last_input_value_X = mapsize[0];
//				last_input_value_Y = mapsize[1];
//				ShowSoftKeyBoard(mRelativeLayout, false);
//			}
//		}).setView(mRelativeLayout);
//		last_input_value_X = new jniSetupControl().GetMapCanvasOffsetX();
//		last_input_value_Y = new jniSetupControl().GetMapCanvasOffsetY();
//		mMapSizeDlg = builder.create();
//	}
//	
//	private int[] mapSizeCheck(int offsetX, int offsetY) {
//		WindowManager mWindowManager = (WindowManager)super.getSystemService(WINDOW_SERVICE);
//		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
//		mWindowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
//		int width = mDisplayMetrics.widthPixels;
//		int height = mDisplayMetrics.heightPixels;
//		int temp = 0;
//		if(width > height) {
//			temp = width;
//			width = height;
//			height = temp;
//		}
//		if (width + offsetX >= 1200) {
//			offsetX = 1200 - width;
//		} else if (offsetX <= 0) {
//			offsetX = 0;
//		}
//		if (height + offsetY >= 1200) {
//			offsetY = 1200 - height; 
//		} else if (offsetY <= 0) {
//			offsetY = 0;
//		}
//		return new int[] {offsetX, offsetY};
//	}	
//	
//	private void refreshTextValue() {
//		et_width.setText(String.valueOf(last_input_value_X));
//		et_height.setText(String.valueOf(last_input_value_Y));
//	}
//	
//	private int readFile(String filePath) {
//		for (int i = 0; i < PATH_ITEMS.length; i++) {
//			File file = new File(filePath + PATH_ITEMS[i]);
//			if (file.exists()) {
//				return i;
//			}
//		}
//		return PATH_ITEMS.length - 1;
//	}
//	
////	private boolean readFile(String filePath, String fileName) {
////		File file = new File(filePath + fileName);
////		return file.exists();
////	}
//	
//	private boolean createFile(String fileName, String filePath) {
//		File file = new File(filePath + fileName);
//		try {
//			if (!file.createNewFile()) {
//				serverSelectionFail.show();
//				return false;
//			} 
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			serverSelectionFail.show();
//			return false;
//		}
//		return true;
//	}
//	
//	private boolean deleteFiles(String filePath) {
//		for (int i = 0; i < PATH_ITEMS.length; i++) {
//			File file = new File(filePath + PATH_ITEMS[i]);
//			if (file.exists()) {
//				if (!file.delete()) {
//					serverSelectionFail.show();
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//
//	class BeepKeyListener implements OnKeyListener {
//
////		@Override
//		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//			// TODO Auto-generated method stub
//			if (keyCode == KeyEvent.KEYCODE_BACK) {
////				jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE2);
//				dialog.cancel();
//			}
//			return false;
//		}
//		
//	}

}
