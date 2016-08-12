package com.billionav.navi.naviscreen;

import java.util.List;

import jp.pioneer.huddevelopkit.IPHUDListener;
import jp.pioneer.huddevelopkit.PHUDConnectManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.billionav.jni.UIHudControl;
import com.billionav.jni.UIVoiceControlJNI;
import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.component.DebugLayout;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.naviscreen.hud.HudManager;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.naviscreen.misc.TelephonyListener;
import com.billionav.navi.naviscreen.open.ADT_Openning;
import com.billionav.navi.sync.AppLinkService;
import com.billionav.navi.system.GlobalVar;
import com.billionav.navi.system.StartRuntimeTask;
import com.billionav.navi.system.SystemInfo;
import com.billionav.navi.uitools.OpeningTriggerReceiver;
import com.billionav.voicerecogJP.VRJPManager;
import com.smartdevicelink.transport.USBTransport;
public class NaviViewManager extends NSViewManager implements IPHUDListener{
	private  boolean m_bFirstLoad = true;
	private  int currentCallState = 0;
	private TelephonyListener telephonyListener;
        /*
        private UsbManager manager = null;
        private SyncReceiver USBStatusReceiver = new SyncReceiver();
        private boolean isRegistered = false;
        public void registerReceiver(){
	        if(isRegistered == false){
		    IntentFilter mIntentFilter = new IntentFilter(); 
		    mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED); 

		    mIntentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
		    mIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED); 	    
		    mIntentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		    mIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);     
		
		    mIntentFilter.addDataScheme("file");  
		    registerReceiver(USBStatusReceiver, mIntentFilter);
		    isRegistered = true;
	        }
        }
        public void unregisterReceiver(){
	    if(isRegistered == true){
		unregisterReceiver(USBStatusReceiver);
		isRegistered = false;
		if(AppLinkService.getInstance() != null){
		    AppLinkService.getInstance().setUseUsbConnected(false);
		}
	    }
        }
	*/
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		Log.i("syx", "@@ [onCreate] ");
		SystemInfo.Initialize(this);
		
		addDebuglayout();
		
//		if(HudManager.getInstance().isHudVersion()) {
//			PHUDConnectManager.pNotifyApplicationCreate( getApplicationContext());
//			PHUDConnectManager.pSetupHUDConnect(this, this);
//		}
		initTelePhoneListener();
	        
		checkUsbAccessoryIntent("Create");

		
		if (AppLinkService.getInstance() == null) {
			Intent startIntent = new Intent(this, AppLinkService.class);
			startIntent.putExtra("type","firstStart");
			startService(startIntent);
		}
		
		//manager = (UsbManager) getSystemService(Context.USB_SERVICE);
		//registerReceiver();
		if( NaviConstant.OPEN_POI ){
			new Thread(new Runnable() {
				@Override
				public void run() {
					startOpenApp("com.poisearch");  
				}
			}).start();
		}
	}

	private void initRuntimeTask(){
		try {
			StartRuntimeTask task = StartRuntimeTask.getInstance();
			task.execute(0);	
		} catch (Exception e) {
			NSTriggerInfo triggerInfo = new NSTriggerInfo();
			triggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_MAP_FIRST_DRAW_DONE;
			MenuControlIF.Instance().TriggerForScreen(triggerInfo);
		}
	}
	
	@Override
	protected void OnStart() {
		Log.i("syx", "@@ [OnStart] ");

//		if(HudManager.getInstance().isHudVersion()) {
//			PHUDConnectManager.pNotifyStart();
//		}
	}

	@Override
	protected void OnRestart() {
		Log.i("syx", "@@ [OnRestart] ");
	}
	
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		if(!HudManager.getInstance().isHudVersion()) {
//			return;
//		}
//		
//		DebugLayout.instance().log("get intent"+intent.getAction());
//		
//		if (intent.getAction().equals("android.hardware.usb.action.USB_ACCESSORY_ATTACHED")) {
//			long duration = System.currentTimeMillis();
//			PHUDConnectManager.pNotifyUsbAccessoryAttached();
//			DebugLayout.instance().log("usb intent ACCESSORY ATTACHED duration = "+(System.currentTimeMillis() - duration));
//		} 
//
//	}

	public static NaviViewManager GetViewManager() {
		return (NaviViewManager) NSViewManager.GetViewManager();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		VRJPManager.Instance().onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void OnResume() {
		Log.i("syx", "@@ [OnResume] ");
//		if(HudManager.getInstance().isHudVersion()) {
//			PHUDConnectManager.pNotifyResume();
//		}
		if(m_bFirstLoad){
			m_bFirstLoad = false;				
			addCameraView();
			MenuControlIF.Create(getApplicationContext());
//			DRIRGlobalToast.CreateContext(getApplicationContext());
			OpeningTriggerReceiver.getInstance().addToGloble();
			initRuntimeTask();
			MenuControlIF.Instance().setWinchangeWithoutAnimation();
//			if(SharedPreferenceData.OPEN_DISCAIMER_SETTING.getBoolean()){
				MenuControlIF.Instance().ForwardWinChange(ADT_Openning.class);	
//			} else {
//				MenuControlIF.Instance().ForwardWinChange(ADT_Opening_Disclaimer.class);
//			}
			getWindow().setBackgroundDrawable(null);
			initGlobalInfo();
		}	
		
		
//		if(SystemTools.isCH()) {
//			DRIRAppMain.DRIRMoveToFornt();
//		}
		if (null != MapView.getInstance()) {
//			MapView.getInstance().onResume();
		}
		
	}

	@Override
	protected void OnPause() {
		Log.i("syx", "@@ [OnPause] ");

		if(HudManager.getInstance().isHudVersion()) {
			PHUDConnectManager.pNotifyPause();
		}
		if (GlobalVar.getPowerManager().isScreenOn()){
//			jniVoicePlayIF.Instance().setBackground(true);
		}
//		if(SystemTools.isCH()) {
//			DRIRAppMain.DRIRMoveToBack();
//		}
		if (null != MapView.getInstance()) {
//			MapView.getInstance().onPause();
        }
		
	}

	@Override
	protected void OnStop() {
		Log.i("syx", "@@ [OnStop] ");

		if(HudManager.getInstance().isHudVersion()) {
			PHUDConnectManager.pNotifyStop();
		}
	}

	@Override
	protected void OnDestroy() {
		Log.i("syx", "@@ [OnDestroy] ");

		if(HudManager.getInstance().isHudVersion()) {
			HudManager.getInstance().destroy();
		}
		
		DeleteTelePhoneListener();
	}

	public void initGlobalInfo() {
		addGlobalView();
	}
	
	private void addGlobalView() {
		addMapOverwrite();
	}


	private void addCameraView(){
//		CameraView cCameraView = CameraView.createInstance(this);	
//		
//		ViewParent parent = cCameraView.getParent();
//		if(parent instanceof ViewGroup)
//		{
//			ViewGroup group = (ViewGroup)parent;
//			group.removeView(cCameraView);
//			Log.d(TAG, "NaviViewManager ---> remove camera view");
//		}
//		Log.d(TAG, "NaviViewManager ---> add camera view");
//		GetViewManager().GetGlobalDnLayer().addView(cCameraView,0);
//		cCameraView.Minimize();
	
	}
	
	public void removeCameraView() {
//		if(CameraView.Instance() == null) {
//			return;
//		}
//		if(GetViewManager().GetGlobalDnLayer().indexOfChild(CameraView.Instance())<0) {
//			return;
//		}
//		Log.d(TAG, "NaviViewManager --->remove camera view");
//		GetViewManager().GetGlobalDnLayer().removeView(CameraView.Instance());
	}
	
	private void addMapOverwrite() {
		
		MapOverwriteLayer layer = MapOverwriteLayer.getInstance();
		if(layer == null){
			layer = new MapOverwriteLayer(this);
		}
		
		if(layer.getParent() instanceof ViewGroup)
		{
			ViewGroup group = (ViewGroup)layer.getParent();
			group.removeView(layer);
			Log.d(TAG, "NaviViewManager --->remove map over write layer");
		}
		
		Log.d(TAG, "NaviViewManager --->add map over write layer");
		GetViewManager().GetGlobalDnLayer().addView(layer,GetViewManager().GetGlobalDnLayer().getChildCount());
	}
	
	private void addDebuglayout() {
		GetViewManager().GetGlobalUpLayer().addView(new DebugLayout(this));
	}
	
	public View OnCreateViewMenuBase(View viewactiviy,Class activitycls)
	{
		Log.d(TAG, "OnCreateViewMenuBase");
		if(null==viewactiviy || null==activitycls){
			return super.OnCreateViewMenuBase(viewactiviy,activitycls);
		}
		
		MenuControlIF.MN_WinAttr attr = MenuControlIF.Instance().GetWinAttribute(activitycls);
		switch(attr.BackgroundType){
			case MenuControlIF.MN_WinAttr.BG_TYPE_NONE:
				return super.OnCreateViewMenuBase(viewactiviy,activitycls);
			case MenuControlIF.MN_WinAttr.BG_TYPE_LIGHT:
				{
					RelativeLayout bigWin = (RelativeLayout) RelativeLayout.inflate(this, com.billionav.ui.R.layout.nsviewmenu_day, null);
					bigWin.addView(viewactiviy);
					return bigWin;	
				}
			case MenuControlIF.MN_WinAttr.BG_TYPE_BLACK:
			default:
				{
					RelativeLayout bigWin = (RelativeLayout) RelativeLayout.inflate(this, com.billionav.ui.R.layout.nsviewmenu_night, null);
					bigWin.addView(viewactiviy);
					return bigWin;
				}
		}		
	}
	
	public void setTelephonyListener(TelephonyListener listener){
		telephonyListener = listener;
	}
	
	PhoneStateListener m_phoneStateListener = new PhoneStateListener(){
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			Log.d("test", "current STATE = "+state);
			switchGuideVolumnByCallState(state);
			switchVRVolumnByCallState(state);
		}
	};
	
	private void initTelePhoneListener() {
		TelephonyManager iTelManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		currentCallState = iTelManager.getCallState();
		iTelManager.listen(m_phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	private void switchGuideVolumnByCallState(int callState) {
		switch(callState) {
			case TelephonyManager.CALL_STATE_IDLE:
				UIVoiceControlJNI.getInstance().volumeResume();
				break;
			default:
				UIVoiceControlJNI.getInstance().volumeMute();
				break;
		}
	}
	
	private void switchVRVolumnByCallState(int callState){
		switch(callState) {
		case TelephonyManager.CALL_STATE_IDLE:
			UIVoiceControlJNI.getInstance().setTheLineIsBusy(false);
			if(telephonyListener != null){
				telephonyListener.callStateIdle();
			}
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			UIVoiceControlJNI.getInstance().stopVR();
			if(telephonyListener != null){
				telephonyListener.callStateOffhook();
			}
			break;
		case TelephonyManager.CALL_STATE_RINGING:
//			UIVoiceControlJNI.getInstance().stopVR();
//			if(telephonyListener != null){
//				telephonyListener.callStateRinging();
//			}
			break;
		default:
			break;
	}
	}
	
	private void DeleteTelePhoneListener() {
		TelephonyManager m_telManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		m_telManager.listen(m_phoneStateListener, 0);
	}
	
	private boolean isCommunicated = false;
	private boolean isLibLoaded = false;

	@Override
	public void onStartCommunicate() {
		isCommunicated = true;
		DebugLayout.instance().log("onStartCommunicate");
		if(isLibLoaded) {
			callHUDCommunicated();
		}
	}

	@Override
	public void onStopCommunicate() {
		if(HudManager.getInstance().isHudVersion()) {
			isCommunicated = false;
			DebugLayout.instance().log("onStopCommunicate");
			UIHudControl.notifyStopCommunicate();
			HudManager.getInstance().onStopComm();
		}
	}

	public void notifyLibLoaded() {
		if(HudManager.getInstance().isHudVersion()) {
			isLibLoaded = true;
			DebugLayout.instance().log("notifyLibLoaded");
			if(isCommunicated) {
				callHUDCommunicated();
			}
		}
	}
	
	public void callHUDCommunicated() {
		if(HudManager.getInstance().isHudVersion()) {
			DebugLayout.instance().log("callHUDCommunicated");
			UIHudControl.notifyStartCommunicate();
			HudManager.getInstance().onStartComm();
		}
	}

        private void startOpenApp(String packageName){
			PackageInfo pi = null;
			try {
				pi = getPackageManager().getPackageInfo(packageName, 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				Log.v("AbelDebugOtherApp", "catch :" + e.toString());
			}
		
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			if (pi != null) {
				resolveIntent.setPackage(pi.packageName);
			}
		
			List<ResolveInfo> apps = getPackageManager().queryIntentActivities(
					resolveIntent, 0);
		
			ResolveInfo ri = apps.iterator().next();
			if (ri != null) {
				String packageName1 = ri.activityInfo.packageName;
				String className = ri.activityInfo.name;
				Log.v("AbelDebugOtherApp", "package name:" + packageName1
						+ "  class name:" + className);
		
				Intent intent = new Intent(Intent.ACTION_MAIN);
				// intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ComponentName cn = new ComponentName(packageName1, className);
				intent.setComponent(cn);
				Bundle bundle = new Bundle();
				bundle.putString("type", "background");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
        
        private void startAppLinkUSB(Intent intent){
	        AppLinkService serviceInstance = AppLinkService.getInstance();
		Context context = NaviViewManager.this;
		if (NaviViewManager.GetViewManager() == null) {
		    Intent startIntent = new Intent(context, NaviViewManager.class);
		    startIntent.putExtras(intent);
		    context.startService(startIntent);
		}
		if (serviceInstance == null) {
		    Intent startIntent = new Intent(context, AppLinkService.class);  
		    startIntent.putExtra("type","USBconnected");
		    context.startService(startIntent);  
		}
		if (serviceInstance == null) {
		    serviceInstance = AppLinkService.getInstance();        
		} 
		else {	
		    serviceInstance.setUseUsbConnected(true);			       
		    serviceInstance.startProxy();
		}      
	}
        private void checkUsbAccessoryIntent(String sourceAction) {
            final Intent intent = getIntent();
            String action = intent.getAction();
            Log.d(TAG, sourceAction + " with action: " + action);

            if (UsbManager.ACTION_USB_ACCESSORY_ATTACHED.equals(action)) {
                Intent usbAccessoryAttachedIntent =
                        new Intent(USBTransport.ACTION_USB_ACCESSORY_ATTACHED);
                usbAccessoryAttachedIntent.putExtra(UsbManager.EXTRA_ACCESSORY,
                intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY));
                usbAccessoryAttachedIntent.putExtra(UsbManager.EXTRA_PERMISSION_GRANTED,
                            intent.getParcelableExtra(
                                    UsbManager.EXTRA_PERMISSION_GRANTED));
		sendBroadcast(usbAccessoryAttachedIntent);
		startAppLinkUSB(intent);
            }
        }
}
