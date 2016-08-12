package com.billionav.navi.system;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.os.Process;
import android.util.Log;
import android.view.Display;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.jni.FileSystemJNI;
import com.billionav.jni.NaviMainJNI;
import com.billionav.jni.NetJNI;
import com.billionav.jni.UIBaseConnJNI;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIMessageControlJNI;
import com.billionav.jni.UIPathControlJNI;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UITrafficControlJNI;
import com.billionav.jni.UIVoiceControlJNI;
import com.billionav.jni.jniVoicePlayIF;
import com.billionav.navi.download.NDataDownloadManager;
import com.billionav.navi.gps.CLocationListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.base.CradleStateListener;
import com.billionav.navi.naviscreen.base.OfflineMapDataManager;
import com.billionav.navi.naviscreen.hud.HudManager;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.naviscreen.schedule.NotificationDataList;
import com.billionav.navi.net.PConnectReceiver;
import com.billionav.navi.net.PHost;
import com.billionav.navi.net.PThreadManager;
import com.billionav.navi.sensor.LocSnsListener;
import com.billionav.navi.service.serviceHelper;
import com.billionav.navi.uicommon.UIC_SystemCommon;
import com.billionav.navi.uitools.HybridUSTools;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SettingHelper;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.update.CameraUpdate;
import com.billionav.ui.R;
import com.billionav.voicerecog.VoiceRecognizer;
import com.billionav.voicerecog.VrListener;
import com.billionav.voicerecogJP.VRJPManager;

public class AplRuntime {
	private static final int NAVI_APL_INIT_START = 0;
	private static final int NAVI_APL_INIT_LIB = 1;	
	private static final int NAVI_APL_INIT_DATA = 2;
	private static final int NAVI_APL_INIT_NDATA_DOWNLOAD = 3;
	private static final int NAVI_APL_INIT_CONTROL = 4;	
	private static final int NAVI_APL_INIT_DELAY = 5;	
	private static final int NAVI_APL_INIT_END = 9;
	
	private static final String TAG = "AplRuntime";
	private static final String SUBTAG = "AplExit";
	
	private static AplRuntime aplRuntime = null;
	private static int InitStatus = NAVI_APL_INIT_START;
	
	private VrListener vrListener;

	private NDataDownloadManager NDataManager = new NDataDownloadManager();
	
	private static final int NAVI_APL_QUIT_TIMEROUT = 5000;
	private final Timer timer = new Timer(true);
	private final TimerTask task = new TimerTask(){  
		public void run() {
			timer.cancel();
			Process.killProcess(Process.myPid());
		}
	};

	public static  AplRuntime Instance() {
		if (null == aplRuntime){
			aplRuntime = new AplRuntime();
			InitStatus = NAVI_APL_INIT_LIB;
		}
		return aplRuntime;
	}

	public int GetInitStatus() {
		return InitStatus;
	}
	public void DataInitBackThrd(){
		if (InitStatus != NAVI_APL_INIT_LIB) return;
		ADataManager.CopyAppData(NaviViewManager.GetViewManager().getBaseContext());
//		CameraUpdate.updateCameraData();
		InitStatus = NAVI_APL_INIT_DATA;
	}
	public void AplInitBackThrd() {
		if (InitStatus != NAVI_APL_INIT_NDATA_DOWNLOAD) return;
		startNaviMain();
		InitStatus = NAVI_APL_INIT_CONTROL;
	}
	
	public void setVrListener(VrListener l) {
		this.vrListener = l;
	}
	
	public void AplInitMainThrd() {
		if (InitStatus != NAVI_APL_INIT_CONTROL) return;
		Activity activity = NaviViewManager.GetViewManager();
		
		ScreenMeasure.create(activity);
		
		startLocation(activity);
		
		initRouteInfo();
		
		activity.registerReceiver(SDCardListener.getBroadcastReceiver(), SDCardListener.getIntentFilter());

		jniVoicePlayIF.Instance().initialize(activity);
		UIVoiceControlJNI.getInstance().initCurrentVolume();
		
//		VoiceRecognizer.instance().setContext(activity);
		
//		DRIRAppMain.DRIRAppStart(activity.getApplicationContext(), activity.getWindowManager()); 

//		VRJPManager.Instance().initVRManager(NaviViewManager.GetViewManager());

//		initDriveHistpryControl(activity);


		initSettings(activity);
		
        LocSnsListener.instance().initialize(activity);
//
        LocSnsListener.instance().start(); 	   
        
        NotificationDataList.getInstance();
        
       boolean ret = serviceHelper.startService();
       Log.d("test", "Service start result:"+ret);
//		if(OfflineMapDataManager.isExistNeedDownloadPackage()){
//			OfflineMapDataManager.createInstance(activity);
//		}
//		if(SystemTools.EDITION_CRADLE.equals(SystemTools.getApkEdition())) {
//			CradleStateListener.addCradleStateListener(activity);
//		}
		
//		UISearchControlJNI.Instance().reqDefaultAreaInfo();
		
		new UIPathControlJNI().registerUnKnownPlaceNameStr(NSViewManager.GetViewManager().getString(R.string.STR_MM_02_02_04_15));
		
		BuleToothMessageQueue.getInstance().requestLatestBlueTooth();
		
		HybridUSTools.getInstance().addListenerToGloble();
		RouteCalcController.instance().syncRouteInfoFromPath();
		InitStatus = NAVI_APL_INIT_DELAY;
	}

	public void initSettings(Activity activity) {
		UITrafficControlJNI.setSystemInfo(android.os.Build.MODEL, 
				android.os.Build.VERSION.RELEASE,
				android.os.Build.VERSION.SDK, 
				SystemTools.getVersionString(), 
				SystemInfo.GetSysUUID(),
				activity.getPackageName());
		
		UIBaseConnJNI.setDayNightOption(SharedPreferenceData.getInt(SharedPreferenceData.DAY_NIGHT_SETTING));
		UIMapControlJNI.setMapFontSize(SharedPreferenceData.getInt(SharedPreferenceData.MAP_FONT_SIZE));
		SettingHelper.setCameraTip(SharedPreferenceData.getBoolean(SharedPreferenceData.CAMERA_TIP));
		//setHUD
//		UIC_SystemCommon.setHUD();
		//set UserAgent to common http header
		String packageInfo = "";
		try {
			packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PHost.addCommonHttpHeader("user-Agent", "billionav/"+packageInfo);
	}

	public void AplInitDelay() {
		if (InitStatus != NAVI_APL_INIT_DELAY) return;
		InitStatus = NAVI_APL_INIT_END;	
	}
	
	public void AplExit() {

		timer.schedule(task, NAVI_APL_QUIT_TIMEROUT, NAVI_APL_QUIT_TIMEROUT);
		NotificationDataList.getInstance().deleteItems();
		Activity activity = NaviViewManager.GetViewManager();
		long time = System.currentTimeMillis();
		Log.d(TAG, SUBTAG + "exit start "+time);
		try{
			OfflineMapDataManager.getInstance().cancelAllnotification();
		}catch(Exception e){
		}
		Log.d(TAG, SUBTAG + "exit 1 "+(System.currentTimeMillis()-time));
//		try{
//			DRIRAppMain.DRIRAppEnd(activity.getApplicationContext());
//		}catch(Exception e){
//		}
		Log.d(TAG, SUBTAG + "exit 2 "+(System.currentTimeMillis()-time));
		try{
			LocSnsListener.instance().stop();
		}catch(Exception e){
		}
		if(SystemTools.EDITION_CRADLE.equals(SystemTools.getApkEdition())) {
			CradleStateListener.removeCradleStateListener();
		}
		Log.d(TAG, SUBTAG + "exit 3 "+(System.currentTimeMillis()-time));
//		try{
//			VoiceRecognizer.instance().release();
//		}catch(Exception e){
//		}
		Log.d(TAG, SUBTAG + "exit 4 "+(System.currentTimeMillis()-time));
		try{
			MapView.getInstance().onDestory();
		}catch(Exception e){
		}
		Log.d(TAG, SUBTAG + "exit 5 "+(System.currentTimeMillis()-time));
		
//		try{
//			HudManager.getInstance().destroy();
//		}catch(Exception e) {
//		}
		Log.d(TAG, SUBTAG + "exit 6 "+(System.currentTimeMillis()-time));

		try{
			MenuControlIF.Instance().Quit();
		}catch(Exception e){
		}
		Log.d(TAG, SUBTAG + "exit 7 "+(System.currentTimeMillis()-time));
		try{
			PThreadManager.instance().stopThread();
		}catch(Exception e){
		}
		Log.d(TAG, SUBTAG + "exit 8 "+(System.currentTimeMillis()-time));
		try{	
			MenuControlIF.Destroy(activity.getApplicationContext());
		}catch(Exception e){
		}
		Log.d(TAG, SUBTAG + "exit 9 "+(System.currentTimeMillis()-time));
		try{	
			activity.unregisterReceiver(SDCardListener.getBroadcastReceiver());
		}catch(Exception e){
		}
		Log.d(TAG, SUBTAG + "exit 10 "+(System.currentTimeMillis()-time));
//		try{
//			jniSetupControl setup = new jniSetupControl();
//			setup.SetInitialStatus(jniSetupControl.STUPDM_OPENING_GPS_STATUS, setup
//					.GetInitialStatus(jniSetupControl.STUPDM_LAST_GPS_SETTING));
//		}catch(Exception e){
//		}
		try{
			CLocationListener.Instance().onDestroy();
		}catch(Exception e){
		}
		Log.d(TAG, SUBTAG + "exit 11 "+(System.currentTimeMillis()-time));
		NaviMainJNI.UnloadNaviMain();
		Log.d(TAG, SUBTAG + "exit 12 "+(System.currentTimeMillis()-time));
		activity.finish();
		Log.d(TAG, SUBTAG + "exit 13 "+(System.currentTimeMillis()-time));
		System.exit(0);
	}  
	
	private void startNaviMain()
	{
		Display display = NaviViewManager.GetViewManager().getWindowManager().getDefaultDisplay();
		int   screenWidth = display.getWidth();
		int   screenHeight = display.getHeight();
		if( screenWidth > screenHeight)
		{
			//use width and  height 's  max value as height
			int temp = screenWidth;
			screenWidth = screenHeight;
			screenHeight = temp;
		}
		
		UIMessageControlJNI.initialize();
		
//	    W3RequestExecutor.instance();

		PHost.initialize();
		PHost.setUUID(SystemInfo.GetSysUUID());
		PConnectReceiver.initialize();

		NaviMainJNI.DoMain();
		
//		NSTriggerInfo triggerInfo = new NSTriggerInfo();
//		triggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_MAP_FIRST_DRAW_DONE;
//		MenuControlIF.Instance().TriggerForScreen(triggerInfo);
	}

	private void initRouteInfo() {
		UIPathControlJNI pathControl = new UIPathControlJNI();
		pathControl.RouteInitialize();
	}
	
	private static void initDriveHistpryControl(Activity a) {
//		String strIMEI =  SystemInfo.GetSysUUID();
//		String strVersion = "";
//		PackageManager packMan = (PackageManager) a.getPackageManager();
//		try {
//			strVersion = packMan.getPackageInfo(a.getPackageName(), 0).versionName;
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		byte byFileType = 0;
//		byte byFileVer = 0;
//		jniDriveHistoryControl.Instance().SetCtrolData(strIMEI, strVersion,
//				byFileType, byFileVer, 1);
	}
	
	private void startLocation(Activity activity){
		  //Add Location Listener For GPS
	    CLocationListener.Instance().onCreate((LocationManager) activity.getSystemService(Context.LOCATION_SERVICE), activity);
//	    
//		if (jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_LOCATIONWRITELOGS)) {
//			CLocationListener.Instance().setWriteLogsToFileEnable(true);
//		}
//		
//		if (jniSetupControl.isOn(jniSetupControl.STUPDM_DEBUG_MATCHINGLOG)) {
//			jniLocationIF LocationInfo = new jniLocationIF();
//			if (!LocationInfo.StartLogging(jniLocationIF.LOC_MEDIA_CARD)) {
//				jniSetupControl.setOff(jniSetupControl.STUPDM_DEBUG_MATCHINGLOG);
//			}
//		}

	}
	/*
	 * call checkNData File  supplied by Tangshaohua to check NdataFiles, 
	 * true == check started success
	 * false == check started failed
	 * receive trigger UIC_MN_TRG_DLNDATA_NET_ERROR || UIC_MN_TRG_DLNDATA_CHECK_FINISHED to judge NData State
	 **/
	public boolean CheckNDataState() {
		if(NAVI_APL_INIT_DATA != InitStatus){
			return false;
		}
		
		InitStatus = NAVI_APL_INIT_NDATA_DOWNLOAD;
		if(needCheckNDataFile()){
			return true;
		}else{
			return false;
		}
	}
	/*
	 * judge if need to check NData file according to version comparation between last version & prog version
	 * return value:false == don't need to check
	 * true == need to check
	 * 
	 **/
	private boolean needCheckNDataFile() {
		if("0.0.0".equals(SystemTools.getVersionString())){
			InitStatus = NAVI_APL_INIT_NDATA_DOWNLOAD;
			return false;
		}
		String last = getLatestNDataVersion();
		String current = NDataDownloadManager.getNDataVersion();
		if(null == last){
			return true;
		}
		if((last.trim()).equals(current)){
			return VerfingNData();
		}else{
			return true;
		}
//		InitStatus = NAVI_APL_INIT_NDATA_DOWNLOAD;
//		return false;
	}
	
	private boolean VerfingNData() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * start download thread 
	 * -1 = check error, state back to NAVI_APL_INIT_DATA;
	 * 0 = no files need to download 
	 * >0 = num of files
	 **/
	public void startNDataDownloadIfNeeded(long num) {
		if(NAVI_APL_INIT_NDATA_DOWNLOAD != InitStatus){
			return;
		}
		if(num < 0){
			return;
		} else if(num > 0){
			NDataManager.downloadNDataFiles();
		}
		
	}

	private static final String NDataVersionPath = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_PATH)+"NdataVersion.txt";
	private String getLatestNDataVersion() {
		Log.d("test", NDataVersionPath);
		File NdataFile = new File(NDataVersionPath);
		return SystemTools.getFileContext(NdataFile);
	}
	
	/*
	 * save apl version code into sharePreference
	 */
	public void setLatestNDataVer() {

		SystemTools.writeFileContext(new File(NDataVersionPath), NDataDownloadManager.getNDataVersion());	
	}

}
