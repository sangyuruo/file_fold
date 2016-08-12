package com.billionav.navi.sync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIPathControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchControlJNI.UIC_SCM_POIReqParam;
import com.billionav.navi.app.ext.EglBufferItem;
import com.billionav.navi.app.ext.EglBufferSemaphorePool;
import com.billionav.navi.app.ext.InstanceManager;
import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.app.ext.ToHuData;
import com.billionav.navi.app.ext.log.NaviLogUtil;
import com.billionav.navi.app.ext.sdl.TouchEventHandler;
import com.billionav.navi.app.ext.util.MediaCoderHelper;
import com.billionav.navi.app.ext.util.MediaCoderNewHelper;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.guidebar.base.GuideInfoDataManager;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation.CurrentViewAction;
import com.billionav.navi.naviscreen.mef.EglHelper;
import com.billionav.navi.uicommon.UIC_RouteCommon;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.ui.R;
import com.smartdevicelink.exception.SdlException;
import com.smartdevicelink.exception.SdlExceptionCause;
import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.RPCRequestFactory;
import com.smartdevicelink.proxy.SdlProxyALM;
import com.smartdevicelink.proxy.TTSChunkFactory;
import com.smartdevicelink.proxy.callbacks.OnServiceEnded;
import com.smartdevicelink.proxy.callbacks.OnServiceNACKed;
import com.smartdevicelink.proxy.interfaces.IProxyListenerALM;
import com.smartdevicelink.proxy.rpc.AddCommandResponse;
import com.smartdevicelink.proxy.rpc.AddSubMenuResponse;
import com.smartdevicelink.proxy.rpc.Alert;
import com.smartdevicelink.proxy.rpc.AlertManeuverResponse;
import com.smartdevicelink.proxy.rpc.AlertResponse;
import com.smartdevicelink.proxy.rpc.ChangeRegistrationResponse;
import com.smartdevicelink.proxy.rpc.CreateInteractionChoiceSetResponse;
import com.smartdevicelink.proxy.rpc.DeleteCommandResponse;
import com.smartdevicelink.proxy.rpc.DeleteFileResponse;
import com.smartdevicelink.proxy.rpc.DeleteInteractionChoiceSetResponse;
import com.smartdevicelink.proxy.rpc.DeleteSubMenuResponse;
import com.smartdevicelink.proxy.rpc.DiagnosticMessageResponse;
import com.smartdevicelink.proxy.rpc.DialNumberResponse;
import com.smartdevicelink.proxy.rpc.EndAudioPassThruResponse;
import com.smartdevicelink.proxy.rpc.GenericResponse;
import com.smartdevicelink.proxy.rpc.GetDTCsResponse;
import com.smartdevicelink.proxy.rpc.GetVehicleDataResponse;
import com.smartdevicelink.proxy.rpc.ListFilesResponse;
import com.smartdevicelink.proxy.rpc.OnAudioPassThru;
import com.smartdevicelink.proxy.rpc.OnButtonEvent;
import com.smartdevicelink.proxy.rpc.OnButtonPress;
import com.smartdevicelink.proxy.rpc.OnCommand;
import com.smartdevicelink.proxy.rpc.OnDriverDistraction;
import com.smartdevicelink.proxy.rpc.OnHMIStatus;
import com.smartdevicelink.proxy.rpc.OnHashChange;
import com.smartdevicelink.proxy.rpc.OnKeyboardInput;
import com.smartdevicelink.proxy.rpc.OnLanguageChange;
import com.smartdevicelink.proxy.rpc.OnLockScreenStatus;
import com.smartdevicelink.proxy.rpc.OnPermissionsChange;
import com.smartdevicelink.proxy.rpc.OnStreamRPC;
import com.smartdevicelink.proxy.rpc.OnSystemRequest;
import com.smartdevicelink.proxy.rpc.OnTBTClientState;
import com.smartdevicelink.proxy.rpc.OnTouchEvent;
import com.smartdevicelink.proxy.rpc.OnVehicleData;
import com.smartdevicelink.proxy.rpc.PerformAudioPassThruResponse;
import com.smartdevicelink.proxy.rpc.PerformInteractionResponse;
import com.smartdevicelink.proxy.rpc.PutFileResponse;
import com.smartdevicelink.proxy.rpc.ReadDIDResponse;
import com.smartdevicelink.proxy.rpc.ResetGlobalPropertiesResponse;
import com.smartdevicelink.proxy.rpc.ScrollableMessageResponse;
import com.smartdevicelink.proxy.rpc.SendLocationResponse;
import com.smartdevicelink.proxy.rpc.SetAppIconResponse;
import com.smartdevicelink.proxy.rpc.SetDisplayLayoutResponse;
import com.smartdevicelink.proxy.rpc.SetGlobalPropertiesResponse;
import com.smartdevicelink.proxy.rpc.SetMediaClockTimerResponse;
import com.smartdevicelink.proxy.rpc.ShowConstantTbtResponse;
import com.smartdevicelink.proxy.rpc.ShowResponse;
import com.smartdevicelink.proxy.rpc.SliderResponse;
import com.smartdevicelink.proxy.rpc.SpeakResponse;
import com.smartdevicelink.proxy.rpc.StreamRPCResponse;
import com.smartdevicelink.proxy.rpc.SubscribeButton;
import com.smartdevicelink.proxy.rpc.SubscribeButtonResponse;
import com.smartdevicelink.proxy.rpc.SubscribeVehicleDataResponse;
import com.smartdevicelink.proxy.rpc.SystemRequestResponse;
import com.smartdevicelink.proxy.rpc.TTSChunk;
import com.smartdevicelink.proxy.rpc.UnsubscribeButton;
import com.smartdevicelink.proxy.rpc.UnsubscribeButtonResponse;
import com.smartdevicelink.proxy.rpc.UnsubscribeVehicleDataResponse;
import com.smartdevicelink.proxy.rpc.UpdateTurnListResponse;
import com.smartdevicelink.proxy.rpc.enums.AppHMIType;
import com.smartdevicelink.proxy.rpc.enums.ButtonEventMode;
import com.smartdevicelink.proxy.rpc.enums.ButtonName;
import com.smartdevicelink.proxy.rpc.enums.FileType;
import com.smartdevicelink.proxy.rpc.enums.Language;
import com.smartdevicelink.proxy.rpc.enums.Result;
import com.smartdevicelink.proxy.rpc.enums.SdlDisconnectedReason;
import com.smartdevicelink.proxy.rpc.enums.SpeechCapabilities;
import com.smartdevicelink.transport.TCPTransportConfig;
import com.smartdevicelink.transport.USBTransportConfig;

public class AppLinkService extends Service implements IProxyListenerALM {

	private static String TAG = NaviConstant.TAG;
	private static String TAG_PROXY_SERVICE = NaviConstant.TAG_PROXY_SERVICE;
	private static String TAG_PROXY_CIRCLE = NaviConstant.TAG_PROXY_CIRCLE;
	private static String TAG_HMI = NaviConstant.TAG_HMI;
	private static String TAG_LOOP_SEND = NaviConstant.TAG_LOOP_SEND;
	private static String TAG_LOOP_SEND_ERROR = NaviConstant.TAG_LOOP_SEND_ERROR;
	private static String TAG_LOOP_SEND_SUCCESS = NaviConstant.TAG_LOOP_SEND_SUCCESS;

	private static String TAG_LOOP_SEND_TOHU = NaviConstant.TAG_LOOP_SEND_TOHU;

	private static NaviViewManager _mainInstance;
	private static AppLinkService _instance;
	private static SdlProxyALM _syncProxy;
	private BluetoothAdapter mBtAdapter;
	protected Intent USBIntent = null;

	private Thread sendDataThread;
	private GuideInfoDataManager guideInfoDataManager;

	boolean isThreadRunning = false;
	boolean isServiceStarted = false;
	boolean useUsbConnected = false;

	private boolean isRegisterButton = true;
	private Integer autoIncCorrId = 1;

	Surface mInputSurface = null;

	public void onCreate() {
		super.onCreate();
		NaviLogUtil.debug(TAG_PROXY_SERVICE, "AppLinkService oncreate ");
		_instance = this;
		registerIntent();
		guideInfoDataManager = GuideInfoDataManager.Instance();
		NaviLogUtil.debug(TAG_PROXY_SERVICE, "delete files in "
				+ Environment.getExternalStorageDirectory().getPath());
		deleteFile(new File(Environment.getExternalStorageDirectory().getPath()
				+ "//" + "syxh264"));// 视频文件
		deleteFile(new File(Environment.getExternalStorageDirectory().getPath()
				+ "//" + "syxTestData"));// 用buffer截成的图片
		deleteFile(new File(Environment.getExternalStorageDirectory().getPath()
				+ "//" + "syxlog"));
	}

	public void setUseUsbConnected(boolean useUSB) {
		useUsbConnected = useUSB;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		if (VERBOSE)
			NaviLogUtil.debug(TAG_PROXY_SERVICE, "onStartCommand");
		// startProxyIfNetworkConnected();
		// _mainInstance = NaviViewManager.GetViewManager();
		String type = intent.getStringExtra("type");
		Log.v(TAG_PROXY_SERVICE,
				"onStartCommand:" + "type" + intent.getStringExtra("type"));
		if (type != null && type.equals("USBconnected")) {
			useUsbConnected = true;
			_mainInstance = NaviViewManager.GetViewManager();
			startProxyIfNetworkConnected();
		} else if (type != null && type.equals("firstStart")) {
			_mainInstance = NaviViewManager.GetViewManager();
		} else {
			_mainInstance = NaviViewManager.GetViewManager();
			startProxyIfNetworkConnected();
		}
		// this.startSendH264();
		return START_NOT_STICKY;
	}

	private void startProxyIfNetworkConnected() {
		final SharedPreferences prefs = getSharedPreferences(Const.PREFS_NAME,
				MODE_PRIVATE);
		final int transportType = prefs.getInt(
				Const.Transport.PREFS_KEY_TRANSPORT_TYPE,
				Const.Transport.PREFS_DEFAULT_TRANSPORT_TYPE);

		NaviLogUtil
				.debug(TAG_PROXY_SERVICE, "start proxy via " + transportType);

		if (transportType == Const.Transport.KEY_BLUETOOTH) {
			mBtAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBtAdapter != null) {
				if (mBtAdapter.isEnabled()) {
					startProxy();
				}
			}
		} else {
			startProxy();
		}
	}

	public void startProxy() {
		NaviLogUtil.debug(TAG_PROXY_SERVICE, "startProxy() is called.");
		if (_syncProxy == null) {
			try {
				SharedPreferences settings = getSharedPreferences(
						Const.PREFS_NAME, 0);
				boolean isMediaApp = settings.getBoolean(
						Const.PREFS_KEY_ISMEDIAAPP,
						Const.PREFS_DEFAULT_ISMEDIAAPP);
				int versionNumber = settings.getInt(
						Const.PREFS_KEY_PROTOCOLVERSION,
						Const.PREFS_DEFAULT_PROTOCOLVERSION);
				String appName = settings.getString(
						Const.PREFS_KEY_APPNAME,
						NaviViewManager.GetViewManager().getString(
								R.string.application_name));

				String appSynonym1 = settings.getString(
						Const.PREFS_KEY_APPSYNONYM1,
						Const.PREFS_DEFAULT_APPSYNONYM1);

				String appSynonym2 = settings.getString(
						Const.PREFS_KEY_APPSYNONYM2,
						Const.PREFS_DEFAULT_APPSYNONYM2);

				String appTTSTextName = settings.getString(
						Const.PREFS_DEFAULT_APP_TTS_TEXT,
						Const.PREFS_DEFAULT_APP_TTS_TEXT);

				String appTTSType = settings.getString(
						Const.PREFS_DEFAULT_APP_TTS_TYPE,
						Const.PREFS_DEFAULT_APP_TTS_TYPE);

				SpeechCapabilities appTTSTextType = SpeechCapabilities
						.valueForString(appTTSType);

				Vector<TTSChunk> chunks = new Vector<TTSChunk>();

				TTSChunk ttsChunks = TTSChunkFactory.createChunk(
						appTTSTextType, appTTSTextName);

				chunks.add(ttsChunks);

				Vector<AppHMIType> appType = new Vector<AppHMIType>();
				appType.add(AppHMIType.NAVIGATION);

				String appId = settings.getString(Const.PREFS_KEY_APPID,
						Const.PREFS_DEFAULT_APPID);
				Language lang = Language.valueOf(settings.getString(
						Const.PREFS_KEY_LANG, Const.PREFS_DEFAULT_LANG));
				Language hmiLang = Language.valueOf(settings.getString(
						Const.PREFS_KEY_HMILANG, Const.PREFS_DEFAULT_HMILANG));
				int transportType = settings.getInt(
						Const.Transport.PREFS_KEY_TRANSPORT_TYPE,
						Const.Transport.PREFS_DEFAULT_TRANSPORT_TYPE);
				if (useUsbConnected) {
					transportType = Const.Transport.KEY_USB;
					useUsbConnected = false;
				}
				String ipAddress = settings.getString(
						Const.Transport.PREFS_KEY_TRANSPORT_IP,
						Const.Transport.PREFS_DEFAULT_TRANSPORT_IP);
				int tcpPort = settings.getInt(
						Const.Transport.PREFS_KEY_TRANSPORT_PORT,
						Const.Transport.PREFS_DEFAULT_TRANSPORT_PORT);
				boolean autoReconnect = settings
						.getBoolean(
								Const.Transport.PREFS_KEY_TRANSPORT_RECONNECT,
								Const.Transport.PREFS_DEFAULT_TRANSPORT_RECONNECT_DEFAULT);

				Vector<String> vrSynonyms = new Vector<String>();

				vrSynonyms.add(appSynonym1);
				vrSynonyms.add(appSynonym2);

				if (VERBOSE)
					NaviLogUtil.debug(TAG_PROXY_SERVICE, "transportType"
							+ transportType);

				Log.v("AbelDebugUSB", "appserver transportType "
						+ transportType + " USB " + USBIntent);

				NaviLogUtil
						.debug(TAG_PROXY_SERVICE, " start proxy params is: ");
				NaviLogUtil.debug(TAG_PROXY_SERVICE, "appName:" + appName);
				NaviLogUtil.debug(TAG_PROXY_SERVICE, "chunks:" + chunks);
				NaviLogUtil
						.debug(TAG_PROXY_SERVICE, "vrSynonyms:" + vrSynonyms);
				NaviLogUtil
						.debug(TAG_PROXY_SERVICE, "isMediaApp:" + isMediaApp);
				NaviLogUtil.debug(TAG_PROXY_SERVICE, "language:" + lang);
				NaviLogUtil.debug(TAG_PROXY_SERVICE, "Display Language:"
						+ hmiLang);
				NaviLogUtil.debug(TAG_PROXY_SERVICE, "appType:" + appType);
				NaviLogUtil.debug(TAG_PROXY_SERVICE, "appId:" + appId);

				if (transportType == Const.Transport.KEY_BLUETOOTH) {
					_syncProxy = new SdlProxyALM(this,
					/* sync proxy configuration resources */null,
					/* enable advanced lifecycle management true, */
					appName, chunks,
					/* ngn media app */null,
					/* vr synonyms */vrSynonyms,
					/* is media app */isMediaApp,
					/* syncMsgVersion */null,
					/* language desired */lang,
					/* HMI Display Language Desired */hmiLang,
					/* appHMIType */appType,
					/* App ID */appId,
					/* autoActivateID */null,
					/* callbackToUIThread */false,
					/* preRegister */false);
				} else if (transportType == Const.Transport.KEY_TCP) {
					_syncProxy = new SdlProxyALM(this,
					/* sync proxy configuration resources */null,
					/* enable advanced lifecycle management true, */
					appName, chunks,
					/* ngn media app */null,
					/* vr synonyms */null,
					/* is media app */isMediaApp,
					/* syncMsgVersion */null,
					/* language desired */lang,
					/* HMI Display Language Desired */hmiLang,
					/* appHMIType */appType,
					/* App ID */appId,
					/* autoActivateID */null,
					/* callbackToUIThre1ad */false,
					/* preRegister */false, new TCPTransportConfig(tcpPort,
							ipAddress, autoReconnect));

					// } else if (transportType == Const.Transport.KEY_USB &&
					// USBIntent != null) {
				} else if (transportType == Const.Transport.KEY_USB) {
					// UsbAccessory accessory = (UsbAccessory)
					// USBIntent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
					_syncProxy = new SdlProxyALM(this,
					/* sync proxy configuration resources */null,
					/* enable advanced lifecycle management true, */
					appName, chunks,
					/* ngn media app */null,
					/* vr synonyms */null,
					/* is media app */isMediaApp,
					/* syncMsgVersion */null,
					/* language desired */lang,
					/* HMI Display Language Desired */hmiLang,
					/* appHMIType */appType,
					/* App ID */appId,
					/* autoActivateID */null,
					/* callbackToUIThre1ad */false,
					/* preRegister */false, new USBTransportConfig(
							_mainInstance));
				}
				isServiceStarted = true;
			} catch (SdlException e) {
				e.printStackTrace();
				// error creating proxy, returned proxy = null
				if (_syncProxy == null) {
					stopSelf();
				}
			}
			setFordShow(true);
		}
	}

	public boolean isRunning() {
		return isThreadRunning;
	}

	public boolean isServieStarted() {
		return isServiceStarted;
	}

	public void stopSync() {
		Log.i(TAG_PROXY_CIRCLE, "[stopSync() is called.]");
		// TODO: why?
		/*
		 * try { JSONObject jo = new JSONObject(); jo.put("stopSync", true);
		 * sendDataToFord(jo.toString()); } catch (JSONException e) {
		 * e.printStackTrace(); }
		 */
		isServiceStarted = false;
		isThreadRunning = false;

		needSendData = false;
		mEncoder = null;
		mBufferInfo = null;
		notifyDemoRun(false);
		disposeSyncProxy();
	}

	public void onDestroy() {
		disposeSyncProxy();
		_instance = null;
		super.onDestroy();
	}

	public void disposeSyncProxy() {
		Log.i(TAG_PROXY_CIRCLE, "[disposeSyncProxy() is called.]");
		if (isRegisterButton) {
			unsubscribeButton();
		}
		if (_syncProxy != null) {
			try {
				Log.i(TAG_PROXY_CIRCLE, "start to dispose _syncProxy.");
				_syncProxy.dispose();
				Log.i(TAG_PROXY_CIRCLE, "_syncProxy is dispose.");
			} catch (SdlException e) {
				e.printStackTrace();
			}
			_syncProxy = null;
		}
		isHMIValidate = false;
		Log.i(TAG_PROXY_SERVICE,
				"At disposeSyncProxy(), _syncProxy is set null.");
	}

	private void reset() {
		isHMIValidate = false;
		if (_syncProxy != null) {
			try {
				_syncProxy.resetProxy();
			} catch (Exception e) {
				e.printStackTrace();
				Log.i(TAG_PROXY_CIRCLE, "reset proxy error." + e.toString());
				// something goes wrong, &the proxy returns as null, stop the
				// service.
				// do not want a running service with a null proxy
				if (_syncProxy == null) {
					Log.i(TAG_PROXY_CIRCLE, "_syncProxy is null.");
					stopSelf();
				}
			}
		} else {
			startProxy();
		}
	}

	public static SdlProxyALM getProxyInstance() {
		return _syncProxy;
	}

	public static AppLinkService getInstance() {
		return _instance;
	}

	public boolean isWorking() {
		return needSendData;
	}

	public void setUSBIntent(Intent intent) {
		Log.v("AbelDebugUSB", "setUSBIntent");
		this.USBIntent = intent;
	}

	private boolean isHMIValidate = false;

	@Override
	public void onOnHMIStatus(OnHMIStatus notification) {
		switch (notification.getSystemContext()) {
		case SYSCTXT_MAIN:
			break;
		case SYSCTXT_VRSESSION:
			break;
		case SYSCTXT_MENU:
			break;
		default:
			return;
		}

		switch (notification.getAudioStreamingState()) {
		case AUDIBLE:
			break;
		case NOT_AUDIBLE:
			break;
		default:
			return;
		}
		NaviLogUtil.debugEglStep(String.format(
				"SDL onOnHMIStatus is called.status is %s", notification
						.getHmiLevel().name()));

		switch (notification.getHmiLevel()) {
		case HMI_FULL:
			isHMIValidate = true;
			if (isRegisterButton) {
				Log.i(NaviConstant.TAG_HMI, "isSubscribeButton:"
						+ isSubscribeButton);
				if (!isSubscribeButton) {
					subscribeButton();
				}
			}

			if (null != _syncProxy && isThreadRunning == false) {
				onGuideInfoChanged();
				NaviLogUtil.debug(NaviConstant.TAG_HMI,
						"start senddata runnable");
				NaviLogUtil
						.debugEglStep("SDL onOnHMIStatus start senddata runnable");
				sendDataThread = null;
				sendDataThread = new Thread(getSendRunnable(),
						"Thread-SendDataToSync");

				sendDataThread.setPriority(Thread.MAX_PRIORITY);
				isThreadRunning = true;
				sendDataThread.start();
			}

			break;
		case HMI_LIMITED:
			isHMIValidate = false;
			break;
		case HMI_BACKGROUND:
			isHMIValidate = false;
			break;
		case HMI_NONE:
			if (isHMIValidate) {
				isHMIValidate = false;
				// TODO 20160318
				// closeProxy();
			}
			NaviViewManager.GetViewManager().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;
		default:
			isHMIValidate = false;
			return;
		}
		// setFordShow(isHMIValidate);
	}

	private void setFordShow(final boolean show) {

		NaviViewManager.GetViewManager().runOnUiThread(new Runnable() {
			public void run() {
				// NaviViewManager.GetViewManager().applinkLogo.setVisibility(show?View.VISIBLE:View.GONE);
			}
		});

	}

	private void notifyScaleUpDown(boolean isUp) {

		UIMapControlJNI.ScaleUpDown(isUp);
	}

	private void notifyDemoRun(boolean run) {
		if (run) {
			NaviLogUtil.debug(TAG_HMI, "demo run...");
			updateProgressFlag(false);
			sendDemoStartStatus();
			new UIPathControlJNI().StartDemoDriving(1);
		} else {
			NaviLogUtil.debug(TAG_HMI, "demo stop...");
			sendDemoStopStatus();
			updateProgressFlag(false);
			new UIPathControlJNI().StopDemoDriving();
		}
	}

	private void closeProxy() {
		// TODO
		// isServiceStarted = false;
		// isThreadRunning = false;
		// needSendData = false;
		// mEncoder = null;
		// mBufferInfo = null;
		// notifyDemoRun(false);
		disposeSyncProxy();
		startProxyIfNetworkConnected();
		// reset();
	}

	@Override
	public void onProxyClosed(String info, Exception e,
			SdlDisconnectedReason reason) {
		Log.i(TAG_PROXY_CIRCLE, "[onProxyClosed.]");
		Log.i(TAG_PROXY_CIRCLE, "info:" + info);
		Log.i(TAG_PROXY_CIRCLE, "Exception:" + e.toString());
		Log.i(TAG_PROXY_CIRCLE, "Reason:" + reason);
		isHMIValidate = false;
		SdlExceptionCause cause = ((SdlException) e).getSdlExceptionCause();
		if (cause != SdlExceptionCause.SDL_PROXY_CYCLED) {
			disposeSyncProxy();
		} else {
			Log.i(TAG_PROXY_CIRCLE, "SdlExceptionCause.SDL_PROXY_CYCLED");
			if (reason == SdlDisconnectedReason.TRANSPORT_ERROR) {
				// stopSync();
				// startProxy();
				NaviLogUtil.debug(TAG_PROXY_CIRCLE, "reason is " + reason);
				isServiceStarted = false;
				isThreadRunning = false;

				needSendData = false;
				mEncoder = null;
				mBufferInfo = null;
				notifyDemoRun(false);
				_syncProxy = null;
			} else {
				reset();
			}
		}
	}

	@Override
	public void onError(String info, Exception e) {
		Log.i(TAG_PROXY_CIRCLE, "[onError]");
		Log.i(TAG_PROXY_CIRCLE, "info:" + info);
		Log.i(TAG_PROXY_CIRCLE, "Exception:" + e.toString());
	}

	@Override
	public void onGenericResponse(GenericResponse response) {
		Log.i(TAG, "[onGenericResponse]" + response.toString());
	}

	@Override
	public void onOnCommand(OnCommand notification) {
		Log.i(TAG, "[onOnCommand]" + notification.toString());
	}

	@Override
	public void onAddCommandResponse(AddCommandResponse response) {
		Log.i(TAG, "[onAddCommandResponse]");
	}

	@Override
	public void onAddSubMenuResponse(AddSubMenuResponse response) {
		Log.i(TAG, "[onAddSubMenuResponse]");
	}

	@Override
	public void onCreateInteractionChoiceSetResponse(
			CreateInteractionChoiceSetResponse response) {
		Log.i(TAG, "[onCreateInteractionChoiceSetResponse]");
	}

	@Override
	public void onAlertResponse(AlertResponse response) {
		Log.i(TAG, "[onAlertResponse]");
	}

	@Override
	public void onDeleteCommandResponse(DeleteCommandResponse response) {
		Log.i(TAG, "[onDeleteCommandResponse]");
	}

	@Override
	public void onDeleteInteractionChoiceSetResponse(
			DeleteInteractionChoiceSetResponse response) {
		Log.i(TAG, "[onDeleteInteractionChoiceSetResponse]");
	}

	@Override
	public void onDeleteSubMenuResponse(DeleteSubMenuResponse response) {
		Log.i(TAG, "[onDeleteSubMenuResponse]");
	}

	@Override
	public void onPerformInteractionResponse(PerformInteractionResponse response) {
		Log.i(TAG, "[onPerformInteractionResponse]");
	}

	@Override
	public void onResetGlobalPropertiesResponse(
			ResetGlobalPropertiesResponse response) {
		Log.i(TAG, "[onResetGlobalPropertiesResponse]");
	}

	@Override
	public void onSetGlobalPropertiesResponse(
			SetGlobalPropertiesResponse response) {
		Log.i(TAG, "[onSetGlobalPropertiesResponse]");
	}

	@Override
	public void onSetMediaClockTimerResponse(SetMediaClockTimerResponse response) {
		Log.i(TAG, "[onSetMediaClockTimerResponse]");
	}

	@Override
	public void onShowResponse(ShowResponse response) {
		Log.i(TAG, "[onShowResponse]");
	}

	@Override
	public void onSpeakResponse(SpeakResponse response) {
		Log.i(TAG, "[onSpeakResponse]" + response.toString());
	}

	@Override
	public void onOnButtonEvent(OnButtonEvent notification) {
		Log.i(TAG_HMI, "[onOnButtonEvent] is called.");
		if (notification.getButtonEventMode() == ButtonEventMode.BUTTONDOWN) {
			Log.i(TAG_HMI, "Button [" + notification.getButtonName()
					+ "] is pressed.");
			Log.i(TAG_HMI,
					"CustomButtonID = " + notification.getCustomButtonID());
			if (isRegisterButton) {
				switch (notification.getButtonName()) {
				case PRESET_0: {
					Integer id = notification.getCustomButtonID();
					if (null == id) {
						notifyScaleUpDown(true);
					} else {
						dealMapAppOperate(id);
					}
					break;
				}
				case PRESET_8: {
					if (null == notification.getCustomButtonID()) {
						// sendPOISearchFailed();
						// break;
						point_search_index = POI_SEARCH_HOTEL;
					} else {
						point_search_index = notification.getCustomButtonID();
					}
					Intent intent = new Intent(SEND_POI_SEARCH);
					intent.putExtra("SearchID", point_search_index);
					sendBroadcast(intent);
					// huRouteCalculate();
					break;
				}
				case PRESET_9: {
					// huRouteCalculate();
					if (null == notification.getCustomButtonID()) {
						// sendPOISearchFailed();
						// break;
						calculate_route_index = 4002;
					} else {
						calculate_route_index = notification
								.getCustomButtonID();
					}
					Intent intent = new Intent(SEND_ROUTE_CALCULATE);
					intent.putExtra("RouteID", calculate_route_index);
					sendBroadcast(intent);
					break;
				}
				default:
					break;
				}
			}
		} else if (notification.getButtonEventMode() == ButtonEventMode.BUTTONUP) {
			Log.i(TAG_HMI, "Button [" + notification.getButtonName()
					+ "] is released.");
		} else {
			// nothing
			Log.i(TAG_HMI, "Button Name:" + notification.getButtonName());
		}
	}

	private int point_search_index = 0;
	private int calculate_route_index = 0;

	@Override
	public void onOnButtonPress(OnButtonPress notification) {
		Log.i(TAG_HMI, "[onOnButtonPress] is called.");
		Log.i(TAG_HMI, "Button [" + notification.getButtonName()
				+ "] is pressed.");
	}

	@Override
	public void onSubscribeButtonResponse(SubscribeButtonResponse response) {
		Log.i(TAG_HMI, "[onSubscribeButtonResponse]");
		Log.i(TAG_HMI, "Response:" + response.getResultCode());
		if (isRegisterButton) {
			if (Result.SUCCESS == response.getResultCode()) {
				isSubscribeButton = true;
			}
		}
	}

	@Override
	public void onUnsubscribeButtonResponse(UnsubscribeButtonResponse response) {
		Log.i(TAG_HMI, "[onUnsubscribeButtonResponse]");
		Log.i(TAG_HMI, "Response:" + response.getResultCode());
	}

	@Override
	public void onOnPermissionsChange(OnPermissionsChange notification) {
		Log.i(TAG_HMI, "[onOnPermissionsChange]");
	}

	@Override
	public void onSubscribeVehicleDataResponse(
			SubscribeVehicleDataResponse response) {
		Log.i(TAG_HMI, "[onSubscribeVehicleDataResponse]");
	}

	@Override
	public void onUnsubscribeVehicleDataResponse(
			UnsubscribeVehicleDataResponse response) {
		Log.i(TAG_HMI, "[onUnsubscribeVehicleDataResponse]");
	}

	@Override
	public void onGetVehicleDataResponse(GetVehicleDataResponse response) {
		Log.i(TAG_HMI, "[onGetVehicleDataResponse]");
	}

	@Override
	public void onReadDIDResponse(ReadDIDResponse response) {
		Log.i(TAG_HMI, "[onReadDIDResponse]");
	}

	@Override
	public void onGetDTCsResponse(GetDTCsResponse response) {
		Log.i(TAG_HMI, "[onGetDTCsResponse]");
	}

	@Override
	public void onOnVehicleData(OnVehicleData notification) {
		Log.i(TAG_HMI, "[onOnVehicleData]");
	}

	@Override
	public void onPerformAudioPassThruResponse(
			PerformAudioPassThruResponse response) {
		Log.i(TAG_HMI, "[onPerformAudioPassThruResponse]");
	}

	@Override
	public void onEndAudioPassThruResponse(EndAudioPassThruResponse response) {
		Log.i(TAG_HMI, "[onEndAudioPassThruResponse]");
	}

	@Override
	public void onOnAudioPassThru(OnAudioPassThru notification) {
		Log.i(TAG_HMI, "[onOnAudioPassThru]");
	}

	@Override
	public void onPutFileResponse(PutFileResponse response) {
		Log.i(TAG_HMI, "[onPutFileResponse]");
	}

	@Override
	public void onDeleteFileResponse(DeleteFileResponse response) {
		Log.i(TAG_HMI, "[onDeleteFileResponse]");
	}

	@Override
	public void onListFilesResponse(ListFilesResponse response) {
		Log.i(TAG_HMI, "[onListFilesResponse]");
	}

	@Override
	public void onSetAppIconResponse(SetAppIconResponse response) {
		Log.i(TAG_HMI, "[onSetAppIconResponse]");
	}

	@Override
	public void onScrollableMessageResponse(ScrollableMessageResponse response) {
		Log.i(TAG_HMI, "[onScrollableMessageResponse]");
	}

	@Override
	public void onChangeRegistrationResponse(ChangeRegistrationResponse response) {
		Log.i(TAG_HMI, "[onChangeRegistrationResponse]  "
				+ response.getInfo().trim());
	}

	@Override
	public void onSetDisplayLayoutResponse(SetDisplayLayoutResponse response) {
		Log.i(TAG_HMI, "[onSetDisplayLayoutResponse]");
	}

	@Override
	public void onOnLanguageChange(OnLanguageChange notification) {
		Language lan = notification.getLanguage();

		if (lan == Language.ZH_CN) {
			notifyScaleUpDown(true);
		} else if (lan == Language.ZH_TW) {
			notifyScaleUpDown(false);
		} else if (lan == Language.JA_JP) {
			notifyDemoRun(false);
		} else if (lan == Language.KO_KR) {
			notifyDemoRun(true);
		}

	}

	@Override
	public void onSliderResponse(SliderResponse response) {
		Log.i(TAG_HMI, "[onSliderResponse]");
	}

	@Override
	public void onOnDriverDistraction(OnDriverDistraction notification) {
		Log.i(TAG_HMI, "[onOnDriverDistraction]");
	}

	@Override
	public void onOnTBTClientState(OnTBTClientState notification) {
		Log.i(TAG_HMI, "[onOnTBTClientState]");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG_PROXY_SERVICE, "[onBind]");
		return null;
	}

	public int bitRate = 3000000;

	private static int videoWidth = 800;
	private static int videoHeight = 480;

	private int mapWidth = 0;
	private int mapHeight = 0;
	private int[] mMapBuffer;
	private int bufferSeq = 0;
	private static boolean is420SP = false;

	private MediaCodec.BufferInfo mBufferInfo;
	private MediaCodec mEncoder;
	private PipedOutputStream mOutputStream;
	private boolean needSendData = false;

	private boolean VERBOSE = true;

	private static MediaCodecInfo selectCodec(String mimeType) {
		int numCodecs = MediaCodecList.getCodecCount();
		for (int i = 0; i < numCodecs; i++) {
			MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

			if (!codecInfo.isEncoder()) {
				continue;
			}

			String[] types = codecInfo.getSupportedTypes();
			for (int j = 0; j < types.length; j++) {
				if (types[j].equalsIgnoreCase(mimeType)) {
					return codecInfo;
				}
			}
		}
		return null;
	}

	/**
	 * Returns a color format that is supported by the codec and by this test
	 * code. If no match is found, this throws a test failure -- the set of
	 * formats known to the test should be expanded for new platforms.
	 */
	private static int selectColorFormat(MediaCodecInfo codecInfo,
			String mimeType) {
		MediaCodecInfo.CodecCapabilities capabilities = codecInfo
				.getCapabilitiesForType(mimeType);
		for (int i = 0; i < capabilities.colorFormats.length; i++) {
			int colorFormat = capabilities.colorFormats[i];
			if (isRecognizedFormat(colorFormat)) {
				return colorFormat;
			}
		}
		return 0; // not reached
	}

	/**
	 * Returns true if this is a color format that this test code understands
	 * (i.e. we know how to read and generate frames in this format).
	 */
	private static boolean isRecognizedFormat(int colorFormat) {
		switch (colorFormat) {
		// these are the formats we know how to handle for this test
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
			is420SP = false;
			return true;
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
		case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
			is420SP = true;
			return true;
		default:
			return false;
		}
	}

	private void prepareEncoder() {
		NaviLogUtil.debug(TAG_LOOP_SEND, "call prepareEncoder on "
				+ this.getClass().getName());
		mBufferInfo = new MediaCodec.BufferInfo();

		MediaCodecInfo codecInfo = selectCodec(MediaCoderHelper.MIME_TYPE);
		if (codecInfo == null) {
			// Don't fail CTS if they don't have an AVC codec (not here,
			// anyway).
			return;
		}
		int colorFormat = selectColorFormat(codecInfo,
				MediaCoderHelper.MIME_TYPE);

		MediaFormat format = MediaFormat.createVideoFormat(
				MediaCoderHelper.MIME_TYPE, videoWidth, videoHeight);
		format.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
		format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
		format.setInteger(MediaFormat.KEY_FRAME_RATE,
				MediaCoderHelper.FRAME_RATE);
		format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL,
				MediaCoderHelper.IFRAME_INTERVAL);

		// mEncoder = MediaCodec.createByCodecName(codecInfo.getName());
		mEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
		mEncoder.start();
	}

	private void initDatas(EglBufferItem data) {
		int[] dataBuffer = data.getMapBytes();
		int width = data.getWidthVehicle();
		int height = data.getHeightVehicle();
		int index = data.getIndex();
		this.sendDatasToSync(dataBuffer, width, height, index);
		// if (null != dataBuffer && dataBuffer.length > 0) {
		// NaviLogUtil.debug(TAG_LOOP_SEND, "call sendDatasToSync on "
		// + this.getClass().getName());
		// NaviLogUtil.debug(TAG_LOOP_SEND, "sendDatasToSync len: "
		// + dataBuffer.length);
		// if ((videoWidth != width) || (videoHeight != height)) {
		// videoWidth = width;
		// videoHeight = height;
		// }
		//
		// if (null == mEncoder) {
		// prepareEncoder();
		// if (isThreadRunning && null != _syncProxy) {
		// Object input = _syncProxy.startH264();
		// if (null != input) {
		// mOutputStream = (PipedOutputStream) input;
		// }
		// }
		// }
		// this.bufferSeq = index;
		// mapWidth = width;
		// mapHeight = height;
		// // synchronized (lock) {
		// Log.d(NaviConstant.TAG_RECV, "Thread id: "
		// + Thread.currentThread().getId());
		// mMapBuffer = dataBuffer;
		// // }
		// needSendData = true;
		// } else {
		//
		// }

	}

	public void sendDatasToSync(int[] dataBuffer, int width, int height,
			int index) {
		if (null != dataBuffer && dataBuffer.length > 0) {
			NaviLogUtil.debug(TAG_LOOP_SEND, "call sendDatasToSync on "
					+ this.getClass().getName());
			NaviLogUtil.debug(TAG_LOOP_SEND, "sendDatasToSync len: "
					+ dataBuffer.length);
			if ((videoWidth != width) || (videoHeight != height)) {
				videoWidth = width;
				videoHeight = height;
			}

			if (null == mEncoder) {
				prepareEncoder();
				mOutputStream = (PipedOutputStream) _syncProxy.startH264();
			}
			this.bufferSeq = index;
			mapWidth = width;
			mapHeight = height;
			synchronized (lock) {
				Log.d(NaviConstant.TAG_LOOP_SEND, "Thread id: "
						+ Thread.currentThread().getId());
				mMapBuffer = dataBuffer;
			}
			needSendData = true;
		} else {

		}

	}

	// public void sendData(int[] dataBuffer, int width, int height) {
	// NaviLogUtil.debug("SDLNaviCreate", "call sendData on " +
	// this.getClass().getName());
	// if (!isThreadRunning) {
	// return;
	// }
	//
	// if (null != dataBuffer && dataBuffer.length > 0) {
	// NaviLogUtil.debug(TAG_LOOP_SEND, "sendData len: " + dataBuffer.length);
	// mapWidth = width;
	// mapHeight = height;
	// mMapBuffer = dataBuffer;
	// needSendData = true;
	//
	// if (null == mEncoder) {
	// videoWidth = width;
	// videoHeight = height;
	// prepareEncoder();
	// mOutputStream = (PipedOutputStream) _syncProxy.startH264();
	// NaviLogUtil.debug(TAG_LOOP_SEND, "mOutputStream : " + mOutputStream);
	// } else {
	// if ((videoWidth != width) || (videoHeight != height)) {
	// videoWidth = width;
	// videoHeight = height;
	// prepareEncoder();
	// }
	// }
	// byte convertBytes[] = convertYUV(dataBuffer, width, height);
	// NaviLogUtil.debug(TAG_LOOP_SEND, "convertBytes len : " +
	// convertBytes.length);
	// writeDataToStream(false, convertBytes);
	// }
	//
	// }

	private static boolean isReady = false;

	private byte[] convertYUV(int[] mapBytes, int mWidth, int mHeight) {
		int totalCount = mWidth * mHeight;
		byte convertBytes[] = new byte[totalCount * 3 / 2];
		// byte convertUBytes[] = new byte[totalCount];
		// byte convertVBytes[] = new byte[totalCount];

		int offsetU = totalCount;
		int offsetV = totalCount + (totalCount >> 2);
		int uvCount = 0;
		int filHeight = 0;

		for (int height = mHeight - 1; height >= 0; --height) {
			boolean yuvStatus = false;
			if (height % 2 == 0) {
				yuvStatus = true;
			}
			int offsetY = mWidth * filHeight;
			++filHeight;
			for (int wid = 0; wid < mWidth; ++wid) {
				int id = totalCount - offsetY - mWidth + wid;
				if (id + mWidth + 1 >= mapBytes.length)
					continue;
				int temp = mapBytes[id];// mapDrawBuffer.get(id);

				convertBytes[offsetY + wid] = (byte) ((temp & 0xFF));
				if (yuvStatus && (wid % 2 == 0)) {
					// is420SP = false;
					/*
					 * byte v = (byte)((((mapBytes[id + 1] & 0xFF00) +
					 * (mapBytes[id + mWidth] & 0xFF00) + (mapBytes[id + mWidth
					 * + 1] & 0xFF00) + (temp & 0xFF00)) >> 8) >> 2); byte u =
					 * (byte)((((mapBytes[id + 1] & 0xFF0000) + (mapBytes[id +
					 * mWidth] & 0xFF0000) + (mapBytes[id + mWidth + 1] &
					 * 0xFF0000) + (temp & 0xFF0000)) >> 16) >> 2);
					 */

					byte u = (byte) ((((mapBytes[id + 1] & 0xFF00)
							+ (mapBytes[id + mWidth] & 0xFF00)
							+ (mapBytes[id + mWidth + 1] & 0xFF00) + (temp & 0xFF00)) >> 8) >> 2);
					byte v = (byte) ((((mapBytes[id + 1] & 0xFF0000)
							+ (mapBytes[id + mWidth] & 0xFF0000)
							+ (mapBytes[id + mWidth + 1] & 0xFF0000) + (temp & 0xFF0000)) >> 16) >> 2);

					if (is420SP) {
						// convertBytes[uvCount + offsetU] = u;
						// convertBytes[uvCount + offsetU + 1] = v;

						convertBytes[uvCount + offsetU] = v;
						convertBytes[uvCount + offsetU + 1] = u;
						uvCount += 2;
					} else {
						convertBytes[uvCount + offsetU] = u;
						convertBytes[uvCount + offsetV] = v;
						uvCount++;
					}
				}
				/*
				 * convertBytes[offsetY + wid] = (byte) ((temp & 0xFF00) >> 8);
				 * 
				 * if (yuvStatus && (wid % 2 == 0)) { //is420SP = false; byte u
				 * = (byte)((((mapBytes[id + 1] & 0xFF0000) + (mapBytes[id +
				 * mWidth] & 0xFF0000) + (mapBytes[id + mWidth + 1] & 0xFF0000)
				 * + (temp & 0xFF0000)) >> 16) >> 2); byte v =
				 * (byte)((((mapBytes[id + 1] & 0xFF) + (mapBytes[id + mWidth] &
				 * 0xFF) + (mapBytes[id + mWidth + 1] & 0xFF) + (temp & 0xFF)))
				 * >> 2); if (is420SP) {
				 * 
				 * //convertBytes[uvCount + offsetU] = u; //convertBytes[uvCount
				 * + offsetU + 1] = v;
				 * 
				 * convertBytes[uvCount + offsetU] = v; convertBytes[uvCount +
				 * offsetU + 1] = u; uvCount += 2; } else { convertBytes[uvCount
				 * + offsetU] = u; convertBytes[uvCount + offsetV] = v;
				 * uvCount++; } }
				 */
				/*
				 * convertBytes[offsetY + wid] = (byte) ((temp & 0xFF0000) >>
				 * 16);
				 * 
				 * if (yuvStatus && (wid % 2 == 0)) { is420SP = false; byte v =
				 * (byte)((((mapBytes[id + 1] & 0xFF00) + (mapBytes[id + mWidth]
				 * & 0xFF00) + (mapBytes[id + mWidth + 1] & 0xFF00) + (temp &
				 * 0xFF00)) >> 8) >> 2); byte u = (byte)((((mapBytes[id + 1] &
				 * 0xFF) + (mapBytes[id + mWidth] & 0xFF) + (mapBytes[id +
				 * mWidth + 1] & 0xFF) + (temp & 0xFF))) >> 2); if (is420SP) {
				 * convertBytes[uvCount + offsetU] = u; convertBytes[uvCount +
				 * offsetU + 1] = v; uvCount += 2; } else { convertBytes[uvCount
				 * + offsetU] = u; convertBytes[uvCount + offsetV] = v;
				 * uvCount++; } }
				 */
			}
		}

		// try {
		// Log.i("syxtest", "write file");
		// File file = new File(Environment.getExternalStorageDirectory(),
		// "syxtestyuv");//Environment.getExternalStorageDirectory()获取当前手机默认的sd卡路径
		//
		// FileOutputStream outStream = new FileOutputStream(file, true);
		//
		// outStream.write(convertBytes);
		//
		// outStream.close();
		// }
		// catch (Exception ex) {
		//
		// }

		return convertBytes;

	}

	boolean isPreItem = false;

	private Runnable getSendRunnable() {

		Runnable dataSendRunnable = new Runnable() {
			public void run() {
				if (NaviConstant.SURFACE_TYPE == 3) {
					int index = 1;
					if (NaviConstant.isNonLoopForWriteHU) {
						return;
					}
					NaviLogUtil.debugEglStep("start wait LoopSend Start...");
					// EglHelper.getInstance().waitLoopSendStart();
					NaviLogUtil.debugEglStep("end wait LoopSend Start...");
					while (isThreadRunning && null != _syncProxy) {
						loopSends(index++);
//						loopSendsNew(index++);
					}
					return;
				}
				NaviLogUtil.debug(TAG_LOOP_SEND,
						"start send h264 data runnable ... ");
				EglBufferItem preItem = null;
				while (isThreadRunning && null != _syncProxy) {
					NaviLogUtil
							.debug(TAG_LOOP_SEND, "dataSendRunnable running");
					if (NaviConstant.IS_BUFFER_POOL_FOR_EGL) {
						EglBufferItem item = null;
						item = EglBufferSemaphorePool.getInstance().getItem();
						if (null != item) {
							isPreItem = false;
							preItem = item;
							initDatas(item);
							boolean isBreak = loopDone();
							if (isBreak) {
								break;
							}
						} else {
							if (null != preItem) {
								isPreItem = true;
								sendPreDataToHu();
							} else {
								continue;
							}
							// try {
							// Thread.sleep(NaviConstant.BUFFER_LOOP_WAIT_TIME);
							// } catch (InterruptedException e) {
							// }
							// if (isThreadRunning && null != _syncProxy && null
							// != preFrameData) {
							// writeDataToStream(false, preFrameData);
							// }
						}
					} else {
						boolean isBreak = loopDone();
						if (isBreak) {
							break;
						}
					}

					if (!NaviConstant.IS_BUFFER_POOL_FOR_EGL) {
						if (NaviConstant.SEND_SLEEP_TIME > 0) {
							try {
								Thread.sleep(NaviConstant.SEND_SLEEP_TIME);
							} catch (InterruptedException e) {
								Log.e(TAG_LOOP_SEND_ERROR, e.getMessage());
							}
						}
					}
				}
			};
		};

		return dataSendRunnable;
	}

	private boolean loopDone() {
		boolean isBreak = false;
		int[] mapData = null;
		if (null != mMapBuffer && mMapBuffer.length > 0) {
			mapData = mMapBuffer;
		}
		NaviLogUtil.debugLoopTime(bufferSeq + "," + System.currentTimeMillis());

		if (null == mEncoder) {
			prepareEncoder();
			NaviLogUtil.debug(TAG_LOOP_SEND, "init h264 proxy  ... ");
			mOutputStream = (PipedOutputStream) _syncProxy.startH264();
			Log.i(TAG_LOOP_SEND, "mOutputStream : " + mOutputStream);
		}

		if (mapData == null || mapWidth == 0 || mapHeight == 0) {
			SystemClock.sleep(30);
			if (!isThreadRunning || _syncProxy == null)
				isBreak = true;

		} else {
			NaviLogUtil.debug(TAG_LOOP_SEND, "send h264 draw data  ... ");
			testSendDrawData(mapData);
			if (!isThreadRunning || _syncProxy == null)
				isBreak = true;
		}
		return isBreak;
	}

	private void sendGuideInfo() {
		/*
		 * boolean isRouteExist = UIC_RouteCommon.Instance()
		 * .isRouteExistAndGuideOn(); if (!isRouteExist) { try { JSONObject
		 * jsonObj = new JSONObject(); jsonObj.put("hasGuideInfo", false); info
		 * = jsonObj.toString(); sendDataToFord(info); } catch (JSONException e)
		 * { e.printStackTrace(); } }
		 * 
		 * sendDataToFord(info);
		 */
	}

	private ArrayList<Runnable> runnables = new ArrayList<Runnable>();

	private Object lock = new Object();

	private void testSendDrawData(int[] mapData) {
		NaviLogUtil.debug(TAG_LOOP_SEND, "call testSendDrawData on "
				+ this.getClass().getName());
		// int[] mapData = null;
		// synchronized (lock) {
		// if (null != mMapBuffer && mMapBuffer.length > 0) {
		// mapData = mMapBuffer;
		// }
		// }
		if (null != mapData && mapData.length > 0) {
			// if (null != mMapBuffer && mMapBuffer.length > 0) {
			// final int[] mapData = mMapBuffer;
			// Log.i(TAG,
			// "@@ [testSendDrawData] 1 time : " + System.currentTimeMillis()
			// + " Thread : " + +Thread.currentThread().getId());
			byte[] frameData = convertYUV(mapData, mapWidth, mapHeight);
			// Log.i(TAG,
			// "@@ [testSendDrawData] 2 time : " + System.currentTimeMillis());

			if (null != frameData && frameData.length > 0) {
				NaviLogUtil.debug(TAG_LOOP_SEND, "testSendDrawData len: "
						+ frameData.length);
				preFrameData = frameData;
				// writeDataToStreamTest(false, frameData);
				writeDataToStream(false, frameData);
			}
			// Log.i(TAG,
			// "@@ [testSendDrawData] 3 time : " + System.currentTimeMillis());
		} else {

		}
	}

	private void sendDrawData() {
		NaviLogUtil.debug(TAG_LOOP_SEND, "call sendDrawData on "
				+ this.getClass().getName());
		if (runnables.size() >= 2) {
			return;
		}

		if (VERBOSE)
			NaviLogUtil.debug(TAG_LOOP_SEND, "sendDrawData" + runnables.size());

		final int[] mapData = mMapBuffer;
		Runnable runnable = new Runnable() {
			public void run() {
				// byte[] frameData = drainEncoder(false, mapData);
				byte[] frameData = convertYUV(mapData, mapWidth, mapHeight);
				if (VERBOSE)
					NaviLogUtil
							.debug(TAG_LOOP_SEND, "drainEncoder" + frameData);
				if (null == frameData) {
					runnables.remove(this);
					return;
				}
				while (true) {
					if (runnables.indexOf(this) == 0) {
						sendGuideInfo();
						writeDataToStream(false, frameData);
						runnables.remove(this);
						break;
					}
					SystemClock.sleep(50);
				}
			}
		};

		runnables.add(runnable);
		new Thread(runnable).start();

	}

	private byte[] drainEncoder(boolean endOfStream, int[] buffer) {
		NaviLogUtil.debug(TAG_LOOP_SEND, "call drainEncoder on "
				+ this.getClass().getName());
		if (endOfStream) {
			// mEncoder.signalEndOfInputStream();
		}

		if (buffer == null) {
			return null;
		}

		// scale buffer
		Bitmap old = Bitmap.createBitmap(mapWidth, mapHeight,
				Bitmap.Config.ARGB_8888);
		old.setPixels(buffer, 0, mapWidth, 0, 0, mapWidth, mapHeight);
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) videoWidth / mapWidth);
		float scaleHeight = ((float) videoHeight / mapHeight);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(old, 0, 0, mapWidth, mapHeight,
				matrix, true);
		int[] newPixels = new int[videoWidth * videoHeight];
		newbmp.getPixels(newPixels, 0, videoWidth, 0, 0, videoWidth,
				videoHeight);
		old.recycle();
		old = null;
		newbmp.recycle();
		newbmp = null;

		// translate buffer
		byte frameData[] = new byte[videoWidth * videoHeight * 3 / 2];
		int nY = 0;
		int HALF_WIDTH = videoWidth / 2;
		int len = videoWidth * videoHeight;
		int uStart = len;
		int vStart = len + len / 4;
		for (int y = videoHeight - 1; y >= 0; y--, nY++) {
			for (int x = 0; x < videoWidth; x++) {
				int pixelIndex = y * videoWidth + x;
				if (pixelIndex >= newPixels.length) {
					continue;
				}
				int pixel = newPixels[pixelIndex];
				int rgb = pixel & 0x00FFFFFF;

				int r = rgb & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb >> 16) & 0xFF;

				int Y, U, V;
				Y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
				Y = Y < 16 ? 16 : (Y > 255 ? 255 : Y);

				int YIndex = nY * videoWidth + x;
				// fillY
				frameData[YIndex] = (byte) Y;

				// fill u,v
				if ((x & 0x01) == 0 && (nY & 0x01) == 0) {
					int UIndex;
					int VIndex;
					U = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
					U = U < 0 ? 0 : (U > 255 ? 255 : U);
					V = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
					V = V < 0 ? 0 : (V > 255 ? 255 : V);
					if (is420SP) {
						UIndex = uStart + nY * HALF_WIDTH + x;
						VIndex = uStart + nY * HALF_WIDTH + x + 1;
						frameData[UIndex] = (byte) U;
						if (VIndex < frameData.length) {
							frameData[VIndex] = (byte) V;
						}
					} else {
						UIndex = uStart + (nY >> 1) * HALF_WIDTH + (x >> 1);
						VIndex = vStart + (nY >> 1) * HALF_WIDTH + (x >> 1);
						frameData[UIndex] = (byte) U;
						if (VIndex < frameData.length) {
							frameData[VIndex] = (byte) V;
						}
					}
				}
			}
		}

		return frameData;

		// writeDataToStream(endOfStream, frameData);
	}

	private byte[] preFrameData;

	private void sendPreDataToHu() {
		if (null != preFrameData && preFrameData.length > 0 && isPreItem) {
			writeDataToStream(false, preFrameData);
		}
	}

	private void writeDataToStream(boolean isEnd, byte[] frameData) {
		NaviLogUtil.debug(TAG_LOOP_SEND, "call writeDataToStream on "
				+ this.getClass().getName());
		try {
			// set to mediacodec
			ByteBuffer[] inputBuffers = mEncoder.getInputBuffers();
			int inputBufferIndex = mEncoder.dequeueInputBuffer(0);
			if (inputBufferIndex >= 0) {
				ByteBuffer input = inputBuffers[inputBufferIndex];
				input.clear();
				Log.i(TAG_LOOP_SEND, "input remaining:" + input.remaining());
				Log.i(TAG_LOOP_SEND, "frameData length:" + frameData.length);
				if (input.remaining() >= frameData.length) {
					input.put(frameData);
					prevOutputPTSUs = getPTSUs();
					if (NaviConstant.IS_HUAWEI) {
						mEncoder.queueInputBuffer(inputBufferIndex, 0,
								frameData.length, 0, 0);
					} else {
						mEncoder.queueInputBuffer(inputBufferIndex, 0,
								frameData.length, prevOutputPTSUs, 0);
					}
				} else {
					return;
				}
			}
			NaviLogUtil.debug(TAG_LOOP_SEND, "handle queue data success...");
			ByteBuffer[] encoderOutputBuffers = mEncoder.getOutputBuffers();
			while (true) {
				int encoderStatus = mEncoder
						.dequeueOutputBuffer(mBufferInfo, 0);
				NaviLogUtil.debug(TAG_LOOP_SEND, "encoderStatus(in while):"
						+ encoderStatus);

				if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
					// no output available yet
					if (!isEnd) {
						Log.i("syxtest", "!!! INFO_TRY_AGAIN_LATER");

						break; // out of while
					} else {
					}
				} else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
					// not expected for an encoder
					encoderOutputBuffers = mEncoder.getOutputBuffers();
				} else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
				} else if (encoderStatus < 0) {
					// let's ignore it
				} else {
					NaviLogUtil.debug(TAG_LOOP_SEND,
							"dequeue data , encoderStatus is " + encoderStatus);

					if (null != mBufferInfo && mBufferInfo.size != 0) {
						byte[] dataToWrite = new byte[mBufferInfo.size];
						encoderOutputBuffers[encoderStatus].get(dataToWrite,
								mBufferInfo.offset, mBufferInfo.size);

						// if( writeFileCount < 10 ){
						// writeFileCount ++ ;
						// Log.i(TAG, "write to file.");
						// try {
						// Log.i("syxtest", "write file after fix");
						// File file = new File(
						// Environment.getExternalStorageDirectory(), "syxh264"
						// );
						// //Environment.getExternalStorageDirectory()获取当前手机默认的sd卡路径
						// FileOutputStream outStream = new FileOutputStream(
						// file, true);
						// outStream.write(dataToWrite);
						// outStream.close();
						// } catch (Exception ex) {
						//
						// }
						// }
						// testWriteFile(dataToWrite);
						if (isThreadRunning) {
							NaviLogUtil
									.debugSendSuccess("mOutputStream write tickcount : "
											+ System.currentTimeMillis()
											+ "size : "
											+ mBufferInfo.size
											+ " Thread : "
											+ Thread.currentThread().getId());
							if (mOutputStream != null) {
								NaviLogUtil
										.debugSendSuccess("@@ mOutputStream != null");
								// try {
								// Log.i("syxtest", "write file after fix");
								// String logInfo =
								// "mOutputStream write tickcount : " +
								// System.currentTimeMillis() + "size : " +
								// mBufferInfo.size;
								// FileWriter file = new FileWriter( new
								// File(Environment.getExternalStorageDirectory(),
								// "syxlog"),
								// true);//Environment.getExternalStorageDirectory()获取当前手机默认的sd卡路径
								// file.write(logInfo);
								// file.close();
								// }
								// catch (Exception ex) {
								//
								// }
								NaviLogUtil
										.debugSendSuccess("write data to mOutputStream and send to HU");
								long startSendDataTime = Calendar.getInstance()
										.getTimeInMillis();
								// mOutputStream.write(dataToWrite, 0,
								// mBufferInfo.size);
								// if( NaviConstant.IS_ADD_HEADER ){
								// seq ++ ;
								// String header = "SDLV";//dataToWrite.length
								// byte[] headerByte = header.getBytes();
								// byte[] seqByte = intToByteArray1(seq);
								// byte[] sendByte = new byte[dataToWrite.length
								// + seqByte.length + headerByte.length];
								// System.arraycopy(headerByte, 0, sendByte, 0,
								// headerByte.length);
								// System.arraycopy(seqByte, 0, sendByte,
								// headerByte.length, seqByte.length);
								// System.arraycopy(dataToWrite, 0, sendByte,
								// (headerByte.length + seqByte.length) ,
								// dataToWrite.length);
								// mOutputStream.write(sendByte, 0,
								// sendByte.length);
								// }else{
								mOutputStream.write(dataToWrite, 0,
										mBufferInfo.size);
								// }
								long endSendDataTime = Calendar.getInstance()
										.getTimeInMillis();
								// long distanceTime = 0;
								// if (curTime == 0) {
								// curTime = Calendar.getInstance()
								// .getTimeInMillis();
								// } else {
								// distanceTime = tmpTime - curTime;
								// curTime = Calendar.getInstance()
								// .getTimeInMillis();
								// }
								// data size , sendfile time , 每次发送的间隔时间
								NaviLogUtil.debugSendToHU(bufferSeq + ","
										+ dataToWrite.length + ","
										+ startSendDataTime + ","
										+ endSendDataTime);
							} else {
								NaviLogUtil
										.debugSendSuccess("@@ mOutputStream == null");
							}
						} else {
							NaviLogUtil.debug(TAG_LOOP_SEND,
									"isThreadRunning is " + isThreadRunning);
						}
					}

					// while (mBufferInfo.presentationTimeUs / 1000 > System
					// .currentTimeMillis() - startMs) {
					// try {
					// Thread.sleep(10);
					// } catch (InterruptedException e) {
					// e.printStackTrace();
					// break;
					// }
					// }

					mEncoder.releaseOutputBuffer(encoderStatus, false);
					if ((null != mBufferInfo && (mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0)) {
						if (!isEnd) {
						} else {
						}
						break; // out of while
					}
				}
			}
		} catch (Exception e) {
			if (e != null && e instanceof IOException) {
				NaviLogUtil.error(TAG_LOOP_SEND_ERROR,
						"mediacodec error :" + e.toString());
				NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "isThreadRunning : "
						+ isThreadRunning);
				NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "_syncProxy : "
						+ _syncProxy);

				if (isThreadRunning && _syncProxy != null) {
					mOutputStream = (PipedOutputStream) _syncProxy.startH264();
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "mOutputStream : "
							+ mOutputStream);
				}
			} else {
				NaviLogUtil.error(TAG_LOOP_SEND_ERROR,
						"mediacodec error :" + e.toString());
			}
		}
	}

	public static byte[] intToByteArray1(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	Integer seq = 0;
	long curTime = 0;
	int writeFileCount = 0;

	private void testWriteFile(byte[] dataToWrite) {
//		if (writeFileCount < 10) {
			writeFileCount++;
			Log.i(TAG, "write to file.");
			try {
				Log.i("syxtest", "write file after fix");
				File sdlFold = new File(Environment.getExternalStorageDirectory() + "/" + "sdlnavi" );
				if( !sdlFold.exists() ){
					sdlFold.mkdir();
				}
				File videoFile = new File(
						Environment.getExternalStorageDirectory() + "/" + "sdlnavi" , "syxh264"
								+ "_" + writeFileCount);
				// 获取当前手机默认的sd卡路径 Environment.getExternalStorageDirectory()
				FileOutputStream outStream = new FileOutputStream(videoFile,
						true);
				outStream.write(dataToWrite);
				outStream.close();
			} catch (Exception ex) {

			}
//		}
	}

	private void sendDataToFord(String infos) {
		if (_syncProxy != null && infos != null && infos.length() > 0) {
			try {
				// _syncProxy.sendRPCRequest(RPCRequestFactory.buildSpeak(infos,
				// 9000));
				_syncProxy.speak(infos, speakId++);
			} catch (SdlException e) {
				Log.v("AbelDebugGuide", "sendDataToFord " + e.toString());
				e.printStackTrace();
			}
		}
	}

	private void sendGuideInfoToFord(Vector<TTSChunk> tts) {
		NaviLogUtil.debug(TAG_PROXY_SERVICE, "call sendGuideInfoToFord on "
				+ this.getClass().getName());
		if (_syncProxy != null && tts != null) {
			try {
				_syncProxy.speak(tts, speakId++);
				NaviLogUtil.debug(TAG_PROXY_SERVICE,
						"call sendGuideInfoToFord on "
								+ this.getClass().getName() + " success!");
				// _syncProxy.speak(tts.toString(),speakId++);
			} catch (SdlException e) {
				Log.v("AbelDebugGuide", "sendDataToFord " + e.toString());
				e.printStackTrace();
			}
		}
	}

	private String info;

	public void sendData(String infos) {
		info = infos;
	}

	private int speakId = 0;

	public String getArrowKindByID(int resID) {
		NaviLogUtil.debug(TAG_PROXY_SERVICE, "call getArrowKindByID on "
				+ this.getClass().getName());
		String arrowKind = "";
		if (resID == R.drawable.navicloud_and_077a_01) {
			arrowKind = "STRAIGHT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_03) {
			arrowKind = "TURN_R";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_02) {
			arrowKind = "TURN_L";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_05) {
			arrowKind = "TURN_FR";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_04) {
			arrowKind = "TURN_FL";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_07) {
			arrowKind = "TURN_RR";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_06) {
			arrowKind = "TURN_RL";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_11) {
			arrowKind = "BST_R";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_10) {
			arrowKind = "BST_L";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_09) {
			arrowKind = "UTERN_R";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_08) {
			arrowKind = "UTERN_L";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_08) {
			arrowKind = "UTERN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_12) {
			arrowKind = "R45DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_13) {
			arrowKind = "R90DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_14) {
			arrowKind = "R135DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_15) {
			arrowKind = "R180DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_16) {
			arrowKind = "R225DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_17) {
			arrowKind = "R270DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_18) {
			arrowKind = "R315DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_19) {
			arrowKind = "R360DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_20) {
			arrowKind = "R45DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_21) {
			arrowKind = "R90DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_22) {
			arrowKind = "R135DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_23) {
			arrowKind = "R180DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_24) {
			arrowKind = "R225DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_25) {
			arrowKind = "R270DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_26) {
			arrowKind = "R315DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_27) {
			arrowKind = "R360DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_28) {
			arrowKind = "L45DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_29) {
			arrowKind = "L90DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_30) {
			arrowKind = "L135DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_31) {
			arrowKind = "L180DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_32) {
			arrowKind = "L225DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_33) {
			arrowKind = "L270DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_34) {
			arrowKind = "L315DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_35) {
			arrowKind = "L360DEG_OUT";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_36) {
			arrowKind = "L45DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_37) {
			arrowKind = "L90DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_38) {
			arrowKind = "L135DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_39) {
			arrowKind = "L180DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_40) {
			arrowKind = "L225DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_41) {
			arrowKind = "L270DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_42) {
			arrowKind = "L315DEG_IN";
			return arrowKind;
		} else if (resID == R.drawable.navicloud_and_077a_43) {
			arrowKind = "L360DEG_IN";
			return arrowKind;
		} else {
			arrowKind = "";
			return arrowKind;
		}

	}

	Vector<TTSChunk> preGuideInfos = null;

	private Vector<TTSChunk> createGuideInfos() {
		Vector<TTSChunk> tts = new Vector<TTSChunk>();
		// GUIDE_INFO
		Hashtable guideInfo = new Hashtable<String, String>();
		guideInfo.put("type", SpeechCapabilities.TEXT);
		guideInfo.put("text", "GUIDE_INFO");
		tts.add(new TTSChunk(guideInfo));
		// isShow
		guideInfo = new Hashtable<String, String>();
		guideInfo.put("type", SpeechCapabilities.TEXT);
		if (UIC_RouteCommon.Instance().isRouteExistAndGuideOn())
			guideInfo.put("text", "true");
		else
			guideInfo.put("text", "false");
		tts.add(new TTSChunk(guideInfo));

		// turning image type
		guideInfo = new Hashtable<String, String>();
		guideInfo.put("type", SpeechCapabilities.TEXT);
		guideInfo.put("text",
				getArrowKindByID(guideInfoDataManager.getTurningImageID()));
		tts.add(new TTSChunk(guideInfo));
		// turning distance
		guideInfo = new Hashtable<String, String>();
		guideInfo.put("type", SpeechCapabilities.TEXT);
		guideInfo.put("text", guideInfoDataManager.getTurningDistance());
		tts.add(new TTSChunk(guideInfo));
		// streent name
		guideInfo = new Hashtable<String, String>();
		guideInfo.put("type", SpeechCapabilities.TEXT);
		guideInfo.put("text", guideInfoDataManager.getStreetName());
		tts.add(new TTSChunk(guideInfo));
		// remain distance
		guideInfo = new Hashtable<String, String>();
		guideInfo.put("type", SpeechCapabilities.TEXT);
		guideInfo
				.put("text", guideInfoDataManager.getRemainingDistanceString());
		tts.add(new TTSChunk(guideInfo));
		// remain time
		guideInfo = new Hashtable<String, String>();
		guideInfo.put("type", SpeechCapabilities.TEXT);
		guideInfo.put("text", guideInfoDataManager.getRemainingTimeString());
		tts.add(new TTSChunk(guideInfo));
		// arrive time
		guideInfo = new Hashtable<String, String>();
		guideInfo.put("type", SpeechCapabilities.TEXT);
		guideInfo.put("text", guideInfoDataManager.getArriveTimeString());
		tts.add(new TTSChunk(guideInfo));

		// progress
		guideInfo = new Hashtable<String, String>();
		guideInfo.put("type", SpeechCapabilities.TEXT);
		guideInfo.put("text",
				String.valueOf(guideInfoDataManager.getGuideProgress()));

		tts.add(new TTSChunk(guideInfo));

		return tts;
	}

	AtomicLong sendGuidInfoTime = new AtomicLong(0);

	/**
	 * 
	 * @param guideInfoDataManager
	 * @return
	 */
	private synchronized boolean progressNeedChange(
			GuideInfoDataManager guideInfoDataManager) {
		AtomicBoolean nextSecond = new AtomicBoolean(false);
		AtomicBoolean nextProgress = new AtomicBoolean(false);
		int index = 8;
		if (null == preGuideInfos) {
			return true;
		}
		String preProgress = preGuideInfos.elementAt(index).getText();
		String curProgress = String.valueOf(guideInfoDataManager
				.getGuideProgress());
		nextProgress.set(!preProgress.equals(curProgress));

		long currentTime = System.currentTimeMillis() / 1000;
		if (currentTime - sendGuidInfoTime.get() > 0) {
			sendGuidInfoTime.set(currentTime);
			nextSecond.set(true);
		} else {
			sendGuidInfoTime.set(currentTime);
			nextSecond.set(false);
		}
		return nextProgress.get() || nextSecond.get();
	}

	public void onGuideInfoChanged() {
		NaviLogUtil.debug(TAG_PROXY_SERVICE, "call onGuideInfoChanged on "
				+ this.getClass().getName());
		/*
		 * if(guideInfoDataManager != null){ Hashtable guideInfo = new
		 * Hashtable<String,String>();
		 * guideInfo.put("type",SpeechCapabilities.TEXT);
		 * guideInfo.put("text","GUIDE_INFO");
		 * if(UIC_RouteCommon.Instance().isRouteExistAndGuideOn())
		 * guideInfo.put("isShow","true"); else guideInfo.put("isShow","false");
		 * guideInfo.put("streetName",guideInfoDataManager.getStreetName());
		 * guideInfo
		 * .put("turningDistance",guideInfoDataManager.getTurningDistance());
		 * guideInfo
		 * .put("turningImageType",getArrowKindByID(guideInfoDataManager
		 * .getTurningImageID()));
		 * guideInfo.put("remainDistance",guideInfoDataManager
		 * .getRemainingDistanceString());
		 * guideInfo.put("remainTime",guideInfoDataManager
		 * .getRemainingTimeString());
		 * guideInfo.put("arriveTime",guideInfoDataManager
		 * .getArriveTimeString()); Vector<TTSChunk> tts = new
		 * Vector<TTSChunk>(); tts.add(new TTSChunk(guideInfo));
		 * sendGuideInfoToFord(tts); }
		 */

		if (guideInfoDataManager != null) {
			int progress = guideInfoDataManager.getGuideProgress();
			if (inProgressFlag.get()) {
				if (progress >= 99 || progress == 0) {
					NaviLogUtil.debug(TAG_HMI, "stop demo when progress is "
							+ progress);
					sendDemoStopStatus();
				}
			} else {
				if (progress > 0 && !inProgressFlag.get()) {
					NaviLogUtil.debug(TAG_HMI,
							"set progress flag to true while progress is "
									+ progress);
					updateProgressFlag(true);
				}
			}

			Vector<TTSChunk> tts = createGuideInfos();

			// if( progressNeedChange(guideInfoDataManager) ){
			NaviLogUtil.debug(TAG_HMI, "TurningImageID: "
					+ guideInfoDataManager.getTurningImageID());
			// NaviLogUtil.debug(TAG_HMI, "TurningDistance: "
			// + guideInfoDataManager.getTurningDistance());
			// NaviLogUtil.debug(TAG_HMI, "RemainingDistanceString: "
			// + guideInfoDataManager.getRemainingDistanceString());
			// NaviLogUtil.debug(TAG_HMI,
			// "StreetName: " + guideInfoDataManager.getStreetName());
			// NaviLogUtil.debug(
			// TAG_HMI,
			// "RemainingTime: "
			// + guideInfoDataManager.getRemainingTimeString());
			// NaviLogUtil
			// .debug(TAG_HMI,
			// "ArriveTime: "
			// + guideInfoDataManager
			// .getArriveTimeString());
			sendGuideInfoToFord(tts);
			// }else{
			// NaviLogUtil.debug(TAG_HMI, "same guide info ... ");
			// }
			preGuideInfos = tts;
		}
	}

	// isRegisterButton -->
	private int nextCorrID() {
		autoIncCorrId++;
		return autoIncCorrId;
	}

	private void subscribeButton() {
		Log.i(TAG_HMI, "subscribeButton");
		SubscribeButton buttonReq1;
		SubscribeButton buttonReq2;
		buttonReq1 = RPCRequestFactory.buildSubscribeButton(
				ButtonName.PRESET_8, nextCorrID());
		Log.i(TAG_HMI, "ButtonName.PRESET_8 is sent.");
		sendRequest(buttonReq1);
		buttonReq2 = RPCRequestFactory.buildSubscribeButton(
				ButtonName.PRESET_9, nextCorrID());
		Log.i(TAG_HMI, "ButtonName.PRESET_9 is sent.");
		sendRequest(buttonReq2);

		SubscribeButton buttonMap;
		buttonMap = RPCRequestFactory.buildSubscribeButton(ButtonName.PRESET_0,
				nextCorrID());
		sendRequest(buttonMap);

	}

	private boolean isSubscribeButton = false;

	private void unsubscribeButton() {
		Log.i(TAG_HMI, "[unsubscribeButton() is called.]");
		if (!isSubscribeButton) {
			return;
		} else {
			isSubscribeButton = false;
		}
		UnsubscribeButton buttonReq1;
		UnsubscribeButton buttonReq2;
		buttonReq1 = RPCRequestFactory.buildUnsubscribeButton(
				ButtonName.PRESET_8, nextCorrID());
		Log.i(TAG_HMI, "ButtonName.PRESET_8 is sent.");
		sendRequest(buttonReq1);
		buttonReq2 = RPCRequestFactory.buildUnsubscribeButton(
				ButtonName.PRESET_9, nextCorrID());
		Log.i(TAG_HMI, "ButtonName.PRESET_9 is sent.");
		sendRequest(buttonReq2);

		UnsubscribeButton buttonMap;
		buttonMap = RPCRequestFactory.buildUnsubscribeButton(
				ButtonName.PRESET_0, nextCorrID());
		sendRequest(buttonMap);
	}

	private void sendRequest(RPCRequest request) {
		Log.i(TAG_HMI, "sendRequest");
		if (_syncProxy != null) {
			try {
				_syncProxy.sendRPCRequest(request);
			} catch (SdlException e) {
				e.printStackTrace();
				Log.i(TAG_HMI, e.getMessage());
			}
		} else {
			Log.i(TAG_HMI, "_syncProxy is null.");
		}
	}

	private void sendDataToNavi(String filename, String data) {
		Log.i(TAG_HMI, "Send Data:" + data);
		byte[] temp = data.toString().getBytes();
		try {
			if (null != _syncProxy) {
				// _syncProxy.speak(data,speakId++);
				_syncProxy.putfile(filename, FileType.JSON, false, temp,
						nextCorrID());
				// NaviLogUtil.debugSendToHU("write data to HU success, file is "+
				// filename +
				// ", type is "+ FileType.JSON+", size is "+ temp.length);
			}
		} catch (Exception e) {
			NaviLogUtil.errorPoi(NaviConstant.TAG_POI_ERROR, "send data error:"
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	final private int MAP_APP_OPERATE_SCALE_UP = 101;
	final private int MAP_APP_OPERATE_SCALE_DOWN = 102;
	final private int MAP_APP_OPERATE_DEMO_START = 103;
	final private int MAP_APP_OPERATE_DEMO_STOP = 104;
	final private int MAP_APP_OPERATE_DELETE_ROUTE = 105;

	private void dealMapAppOperate(int id) {
		NaviLogUtil.debug(TAG, "call dealMapAppOperate on "
				+ this.getClass().getName());
		if (id == MAP_APP_OPERATE_SCALE_UP) {
			// scale up
			NaviLogUtil.debug(TAG, " deal MapAppOperate scale up ");
			// TODO 20160325
			// huRouteCalculate();
			UIMapControlJNI.ScaleUpDown(true);
		} else if (id == MAP_APP_OPERATE_SCALE_DOWN) {
			// scale down
			NaviLogUtil.debug(TAG, " deal MapAppOperate scale down ");
			UIMapControlJNI.ScaleUpDown(false);
		} else if (id == MAP_APP_OPERATE_DEMO_START) {
			// demo start
			NaviLogUtil.debugEglStep("deal MapAppOperate demo start ");
			// 20160325 add navigation for hu demorun
			showNavigation();
			notifyDemoRun(true);
		} else if (id == MAP_APP_OPERATE_DEMO_STOP) {
			// demo stop
			NaviLogUtil.debugEglStep("deal MapAppOperate demo stop ");
			notifyDemoRun(false);
		} else if (id == MAP_APP_OPERATE_DELETE_ROUTE) {
			// Delete route
			NaviLogUtil.debug(TAG, " deal MapAppOperate Delete route ");
			// TODO 2016-03-25 modify by sangjun 对应选目的地算路
			huRouteCalculate();
			// RouteCalcController.instance().DeleteRoute();
		} else {

		}
	}

	// isRegisterButton --<

	final private int POI_SEARCH_PETROL_STATION = 101;
	final private int POI_SEARCH_SHOPPING_CENTRE = 102;
	final private int POI_SEARCH_CAR_PARK = 103;
	final private int POI_SEARCH_HOTEL = 104;
	final private int POI_SEARCH_RESTAURANT = 105;
	final private int POI_SEARCH_CASH_MACHINE = 106;
	final private int POI_SEARCH_ENTERTAINMENT = 10700;
	final private int POI_SEARCH_HOSPITAL = 107;
	final private int POI_SEARCH_TOYOTA_DEALER = 10900;

	final private int GENREID_PETROL_STATION = 1;
	final private int GENREID_SHOPPING_CENTRE = 2;
	final private int GENREID_CAR_PARK = 3;
	final private int GENREID_HOTEL = 4;
	final private int GENREID_RESTAURANT = 5;
	final private int GENREID_CASH_MACHINE = 6;
	final private int GENREID_ENTERTAINMENT = 7;
	final private int GENREID_HOSPITAL = 8;
	final private int GENREID_TOYOTA_DEALER = 9;

	final private String GENREKY_PETROL_STATION = "Petrol Station";
	final private String GENREKEY_SHOPPING_CENTRE = "Shopping Centre";
	final private String GENREKEY_CAR_PARK = "Car Park";
	final private String GENREKEY_HOTEL = "Hotel";
	final private String GENREKEY_RESTAURANT = "Restaurant";
	final private String GENREKEY_CASH_MACHINE = "Cash Machine";
	final private String GENREKEY_ENTERTAINMENT = "Entertainment";
	final private String GENREKEY_HOSPITAL = "Hospital";
	final private String GENREKEY_TOYOTA_DEALER = "Toyota Dealer";

	public void startPOISearch(int index) {
		Log.i(TAG, "startPOISearch index = " + index);
		int genreID = 0;
		String keyword = "";
		switch (index) {
		case POI_SEARCH_PETROL_STATION:
			Log.i(TAG, "POI_SEARCH_PETROL_STATION=" + POI_SEARCH_PETROL_STATION);
			// GenreID 444530688 Petrol Station
			genreID = GENREID_PETROL_STATION;
			keyword = GENREKY_PETROL_STATION;
			break;
		case POI_SEARCH_SHOPPING_CENTRE:
			// GenreID 446758912 Shopping Centre
			genreID = GENREID_SHOPPING_CENTRE;
			keyword = GENREKEY_SHOPPING_CENTRE;
			break;
		case POI_SEARCH_CAR_PARK:
			// GenreID 444203008 Car Park
			genreID = GENREID_CAR_PARK;
			keyword = GENREKEY_CAR_PARK;
			break;
		case POI_SEARCH_HOTEL:
			// GenreID 441712640 Hotel
			genreID = GENREID_HOTEL;
			keyword = GENREKEY_HOTEL;
			break;
		case POI_SEARCH_RESTAURANT:
			// GenreID 446300160 Restaurant
			genreID = GENREID_RESTAURANT;
			keyword = GENREKEY_RESTAURANT;
			break;
		case POI_SEARCH_CASH_MACHINE:
			// GenreID 436797440 Cash Machine
			genreID = GENREID_CASH_MACHINE;
			keyword = GENREKEY_CASH_MACHINE;
			break;
		case POI_SEARCH_ENTERTAINMENT:
			// GenreID 436731904 Entertainment
			genreID = GENREID_ENTERTAINMENT;
			keyword = GENREKEY_ENTERTAINMENT;
			break;
		case POI_SEARCH_HOSPITAL:
			// GenreID 441647104 Hospital
			genreID = GENREID_HOSPITAL;
			keyword = GENREKEY_HOSPITAL;
			break;
		case POI_SEARCH_TOYOTA_DEALER:
			// GenreID 448200704 Toyota Dealer
			genreID = GENREID_TOYOTA_DEALER;
			keyword = GENREKEY_TOYOTA_DEALER;
			break;
		default:
			NaviLogUtil.errorPoi(NaviConstant.TAG_POI_ERROR,
					"startPOISearch index error");
			// error
			sendPOISearchFailed();
			return;
		}

		UIC_SCM_POIReqParam param = new UIC_SCM_POIReqParam();
		param.type = UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY;
		// param.type = UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD;
		param.btn_id = UISearchControlJNI.UIC_SCM_BTN_ID_SEARCH;
		param.act_id = UISearchControlJNI.UIC_SCM_ACT_ID_NORMAL;
		if (UIMapControlJNI.GetCarPositonMode()) {
			param.list_id = UISearchControlJNI.UIC_SCM_LIST_ID_CATEGORY_NEAR_CAR;
		} else {
			param.list_id = UISearchControlJNI.UIC_SCM_LIST_ID_CATEGORY_NEAR_CURSOR;
		}
		// param.nettype = UISearchControlJNI.SRCH_NET_TYPE_OFFLINE;
		param.nettype = UISearchControlJNI.SRCH_NET_TYPE_ONLINE;
		param.keyword = keyword.trim();
		param.genre_id = genreID;

		int[] lonLat = UIMapControlJNI.GetCenterLonLat();
		Log.i(TAG, "lon=" + lonLat[0] + "\tlat=" + lonLat[1]);

		// param.keyword = "wu";
		UISearchControlJNI.Instance().OnPressBtn(param);
	}

	public void sendPOISearchFailed() {
		Log.i(TAG, "sendPOISearchFailed");
		try {
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonVersion = new JSONObject();
			jsonVersion.put("ifVersion", "1.0");
			jsonArray.put(jsonVersion);
			JSONObject jsonResult = new JSONObject();
			jsonResult.put("result", "1");
			jsonArray.put(jsonResult);
			JSONObject jsonType = new JSONObject();
			jsonType.put("type", point_search_index);
			jsonArray.put(jsonType);

			Intent intent = new Intent(SEND_POI_SEARCH_RESULT);
			intent.putExtra("JSON", jsonArray.toString());
			intent.putExtra("TYPE", point_search_index);
			intent.putExtra("Result", false);
			sendBroadcast(intent);
			NaviLogUtil.errorPoi(NaviConstant.TAG_POI_ERROR,
					"sendPOISearchFailed:" + jsonArray.toString());

			// if (isRegisterButton) {
			// sendDataToNavi("POISearch.xml", jsonArray.toString());
			// }
		} catch (JSONException e) {
			e.printStackTrace();
			NaviLogUtil.errorPoi(NaviConstant.TAG_POI_ERROR,
					"Failed to encode messages to JSON.");
		}
	}

	private static ArrayList<UIPointData> POISearchResult = new ArrayList<UIPointData>();

	public void sendPOISearchSuccessed(ArrayList<UIPointData> pointData) {
		Log.i(TAG, "sendPOISearchSuccessed");
		POISearchResult.clear();
		for (int i = 0; i < pointData.size(); ++i) {
			POISearchResult.add(pointData.get(i));
		}

		try {
			JSONObject jsonFile = new JSONObject();
			jsonFile.put("ifVersion", "1.0");
			jsonFile.put("result", "0");
			jsonFile.put("type", point_search_index);

			// ArrayList<JSONArray> POIList = new ArrayList<JSONArray>();
			// POIList.clear();
			int index = 1000 * point_search_index;

			JSONArray POIList = new JSONArray();
			int count = pointData.size();
			if (count > 20) {
				count = 20;
			}
			for (int i = 0; i < count; ++i) {
				POIList.put(serializeVector(++index, pointData.get(i)));
			}

			jsonFile.put("POIList", POIList);

			Intent intent = new Intent(SEND_POI_SEARCH_RESULT);
			intent.putExtra("JSON", jsonFile.toString());
			intent.putExtra("TYPE", point_search_index);
			intent.putExtra("Result", true);
			sendBroadcast(intent);

			// if (isRegisterButton) {
			// sendDataToNavi("POISearch.xml",jsonFile.toString().replaceAll("\\\\",
			// ""));
			// }
		} catch (JSONException e) {
			e.printStackTrace();
			NaviLogUtil.errorPoi(NaviConstant.TAG_POI_ERROR,
					"Failed to encode messages to JSON.");
		}
	}

	private JSONObject serializeVector(int index, UIPointData pointData)
			throws JSONException {
		JSONObject toPut = new JSONObject();
		toPut.put("index", String.valueOf(index));
		toPut.put("distence", String.valueOf(pointData.getDistance()));
		toPut.put("telNo", pointData.getTelNo());
		toPut.put("address", pointData.getAddress());
		toPut.put("name", pointData.getName());

		Log.i(TAG, "+++++++++++++++++++++++++++");
		Log.i(TAG, "index = " + index);
		Log.i(TAG, "name = " + pointData.getName());
		Log.i(TAG, "address = " + pointData.getAddress());
		Log.i(TAG, "telNo = " + pointData.getTelNo());
		Log.i(TAG, "distance = " + pointData.getDistance());
		Log.i(TAG, "---------------------------");
		Log.i(TAG, "JSON = " + toPut.toString().trim());
		Log.i(TAG, "---------------------------");

		return toPut;
	}

	private void startRouteCalculate() {
		Log.i(TAG, "[startRouteCalculate]");

		// get the point info
		int index = calculate_route_index % 1000;
		if ((index < 1) || (index > POISearchResult.size())) {
			NaviLogUtil.errorPoi(NaviConstant.TAG_POI_ERROR,
					"calculate_route_index is error.\nindex=" + index
							+ "\t result size = " + POISearchResult.size());
			routeCalculateFailed();
			return;
		}

		UIPointData pointData = POISearchResult.get(index - 1);
		String name = new String(pointData.getName());
		String address = new String(pointData.getAddress());
		long lat = pointData.getLat();
		long lon = pointData.getLon();
		String tel = new String(pointData.getTelNo());

		RouteCalcController.instance().rapidRouteCalculateWithDataFromNavi(
				name, lon, lat, address, tel);

		// get area name
		// UIMapControlJNI.requestAreaName(lon, lat);

		// scale up or down
		// true:up false:down
		// UIMapControlJNI.ScaleUpDown(true);

		// delete route
		// RouteCalcController.instance().DeleteRoute();
	}

	public void routeCalculateSuccessed() {
		Log.i(TAG, "routeCalculateSuccessed");
		CustomToast.showToast(this, "route successed", 3000);

		Intent intent = new Intent(SEND_ROUTE_CALCULATE_RESULT);
		intent.putExtra("Result", true);
		sendBroadcast(intent);

		if (isRegisterButton) {
			// start demo run
			Intent newintent = new Intent(SEND_ROUTE_DEMORUN);
			newintent.putExtra("demorun", true);
			sendBroadcast(newintent);
		}
	}

	public void routeCalculateFailed() {
		Log.i(TAG, "routeCalculateFailed");
		CustomToast.showToast(this, "route failed", 3000);
		Intent intent = new Intent(SEND_ROUTE_CALCULATE_RESULT);
		intent.putExtra("Result", false);
		sendBroadcast(intent);

		if (isRegisterButton) {
			try {
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonVersion = new JSONObject();
				jsonVersion.put("ifVersion", "1.0");
				jsonArray.put(jsonVersion);
				JSONObject jsonResult = new JSONObject();
				jsonResult.put("result", "1");
				jsonArray.put(jsonResult);
				if (isRegisterButton) {
					sendDataToNavi("RouteCalculate.xml", jsonArray.toString());
				}

				NaviLogUtil.errorPoi(NaviConstant.TAG_POI_ERROR,
						"routeCalculateFailed:" + jsonArray.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				NaviLogUtil.errorPoi(NaviConstant.TAG_POI_ERROR,
						"Failed to encode messages to JSON");
			}
		}
	}

	// 模拟走形
	private void startDemoRun() {
		Log.i(TAG, "startDemoRun");
		// start demorun
		notifyDemoRun(true);

		if (!MenuControlIF.Instance().BackSearchWinChange(
				ADT_Main_Map_Navigation.class)) {
			Log.i(TAG, "BackSearchWinChange false");
			MenuControlIF.Instance().ForwardDefaultWinChange(
					ADT_Main_Map_Navigation.class);
		}
	}

	@Override
	public void onOnHashChange(OnHashChange notification) {
		// TODO Auto-generated method stub
		Log.i(TAG, "[onOnHashChange]");
		Log.i(TAG, "HashID:" + notification.getHashID());
	}

	@Override
	public void onOnSystemRequest(OnSystemRequest notification) {
		// TODO Auto-generated method stub
		Log.i(TAG, "[onOnSystemRequest]");
	}

	@Override
	public void onSystemRequestResponse(SystemRequestResponse response) {
		// TODO Auto-generated method stub
		Log.i(TAG, "[onSystemRequestResponse]");
		Log.i(TAG, "Result Code:" + response.getResultCode());
	}

	@Override
	public void onOnKeyboardInput(OnKeyboardInput notification) {
		// TODO Auto-generated method stub
		Log.i(TAG, "[onOnKeyboardInput]");
	}

	@Override
	public void onOnTouchEvent(OnTouchEvent notification) {
		Log.i(TAG, "[onOnTouchEvent]");
		TouchEventHandler.getInstance().execute(notification);
	}

	@Override
	public void onDiagnosticMessageResponse(DiagnosticMessageResponse response) {
		// TODO Auto-generated method stub
		Log.i(TAG, "[onDiagnosticMessageResponse]");
	}

	@Override
	public void onOnLockScreenNotification(OnLockScreenStatus notification) {
		// TODO Auto-generated method stub
		Log.i(TAG, "[onOnLockScreenNotification]");
		Log.i(TAG, "Notification:" + notification.getShowLockScreen());
	}

	private final static String SEND_POI_SEARCH = "send.POI.search";
	private final static String SEND_ROUTE_CALCULATE = "send.route.calculate";
	private final static String SEND_ROUTE_DEMORUN = "send.route.demorun";

	private final static String SEND_POI_SEARCH_RESULT = "send.POI.search.result";
	private final static String SEND_ROUTE_CALCULATE_RESULT = "send.route.calculate.result";

	private BroadcastReceiver myBroadCast = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.i(TAG, "AppLinkService Receive intent:" + action);
			if (action.equals(SEND_POI_SEARCH)) {
				Toast.makeText(context, "接收到了一条广播为" + SEND_POI_SEARCH,
						Toast.LENGTH_LONG).show();
				point_search_index = intent.getIntExtra("SearchID", 0);
				startPOISearch(point_search_index);
			} else if (action.equals(SEND_ROUTE_CALCULATE)) {
				Toast.makeText(context, "接收到了一条广播为" + SEND_ROUTE_CALCULATE,
						Toast.LENGTH_LONG).show();
				notifyDemoRun(false);
				calculate_route_index = intent.getIntExtra("RouteID", 0);
				startRouteCalculate();
			} else if (action.equals(SEND_ROUTE_DEMORUN)) {
				Toast.makeText(context, "接收到了一条广播为" + SEND_ROUTE_DEMORUN,
						Toast.LENGTH_LONG).show();
				// 20160325 add navigation for hu demorun
				// ADT_Main_Map_Navigation ammn =
				// InstanceManager.getInstance().getAdtMainMapNavigationInstance();
				// if( null != ammn ){
				// try{
				// ammn.showNavigation();
				// }catch(Exception ex){
				// NaviLogUtil.debugEglStep("showNavigation error: " +
				// ex.getMessage() );
				// }
				// }
				startDemoRun();
			}
		}
	};

	public void registerIntent() {
		Log.i(TAG, "AppLinkService registerIntent()");
		IntentFilter myFilter = new IntentFilter();
		myFilter.addAction(SEND_POI_SEARCH);
		myFilter.addAction(SEND_ROUTE_CALCULATE);
		myFilter.addAction(SEND_ROUTE_DEMORUN);
		this.registerReceiver(myBroadCast, myFilter);
	}

	public void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {

			Log.i(TAG, "[deleteFile] file is not exist " + file.getPath());
		}
	}

	private void sendDemoStartStatus() {
		sendDemoStatus(true);
	}

	private void sendDemoStopStatus() {
		sendDemoStatus(false);
	}

	private void sendDemoStopStatus(int progress) {
		if (progress >= 95) {
			sendDemoStatus(false);
		} else {
			NaviLogUtil.debug(TAG_HMI, " progress < 100 ..."
					+ guideInfoDataManager.getGuideProgress());
		}
	}

	public void sendDemoStatus(boolean isStart) {
		if (_syncProxy != null) {
			// TODO notification HU to stop
			NaviLogUtil.debug(TAG_HMI, "demo status change ..., isStart: "
					+ isStart);
			Alert alert = new Alert();
			alert.setAlertText1(String.valueOf(isStart));
			alert.setAlertText2(null);
			alert.setAlertText3(null);
			alert.setDuration(5000);
			alert.setPlayTone(false);
			alert.setCorrelationID(nextCorrID());
			alert.setTtsChunks(null);
			try {
				_syncProxy.sendRPCRequest(alert);
			} catch (SdlException e) {
				Log.e(NaviConstant.TAG_LOOP_SEND_ERROR, e.getMessage());
			}
		}
	}

	private void startSendH264() {
		sendDataThread = null;
		sendDataThread = new Thread(getSendRunnableTest(),
				"Thread-SendDataToSync");
		isThreadRunning = true;
		sendDataThread.start();
	}

	private Runnable getSendRunnableTest() {
		Runnable dataSendRunnable = new Runnable() {
			public void run() {
				NaviLogUtil.debug(TAG_LOOP_SEND,
						"start send h264 data runnable ... ");
				// while (isThreadRunning && null != _syncProxy) {
				while (isThreadRunning) {
					if (VERBOSE)
						NaviLogUtil.debug(TAG_LOOP_SEND,
								"dataSendRunnable running");
					if (null == mEncoder) {
						prepareEncoder();
						NaviLogUtil.debug(TAG_LOOP_SEND,
								"init h264 proxy  ... ");
						// TODO Test
						// mOutputStream = (PipedOutputStream) _syncProxy
						// .startH264();
						// Log.i(TAG_LOOP_SEND, "mOutputStream : " +
						// mOutputStream);
					}

					if (mMapBuffer == null || mapWidth == 0 || mapHeight == 0) {
						SystemClock.sleep(30);
						if (!isThreadRunning)
							break;

					} else {
						NaviLogUtil.debug(TAG_LOOP_SEND,
								"send h264 draw data  ... ");
						testSendDrawDataTest();
						if (!isThreadRunning)
							break;
					}
				}
			};
		};

		return dataSendRunnable;
	}

	private void writeDataToStreamTest(boolean isEnd, byte[] frameData) {
		NaviLogUtil.debug(TAG_LOOP_SEND, "call writeDataToStreamTest on "
				+ this.getClass().getName());
		NaviLogUtil
				.debug(NaviConstant.TAG_MCODE_TIME,
						"call writeDataToStreamTest time "
								+ System.currentTimeMillis());

		long startMs = System.currentTimeMillis();
		// set to mediacodec
		try {
			ByteBuffer[] inputBuffers = mEncoder.getInputBuffers();
			int inputBufferIndex = mEncoder.dequeueInputBuffer(0);
			if (inputBufferIndex >= 0) {
				ByteBuffer input = inputBuffers[inputBufferIndex];
				input.clear();
				Log.i(TAG_LOOP_SEND, "input remaining:" + input.remaining());
				Log.i(TAG_LOOP_SEND, "frameData length:" + frameData.length);
				Log.i(TAG_LOOP_SEND, "time:"
						+ (System.currentTimeMillis() - startMs));
				if (input.remaining() >= frameData.length) {
					NaviLogUtil.debug(NaviConstant.TAG_MCODE_TIME,
							"input frameData " + System.currentTimeMillis());
					input.put(frameData);
					mEncoder.queueInputBuffer(inputBufferIndex, 0,
							frameData.length, getPTSUs(), 0);
					NaviLogUtil.debug(NaviConstant.TAG_MCODE_TIME,
							"queueInputBuffer " + System.currentTimeMillis());
				} else {
					return;
				}
			}
			NaviLogUtil.debug(TAG_LOOP_SEND, "handle queue data success...");
			ByteBuffer[] encoderOutputBuffers = mEncoder.getOutputBuffers();
			while (true) {
				NaviLogUtil.debug(
						NaviConstant.TAG_MCODE_TIME,
						"start dequeueOutputBuffer "
								+ System.currentTimeMillis());
				int encoderStatus = mEncoder
						.dequeueOutputBuffer(mBufferInfo, 0);
				NaviLogUtil.debug(TAG_LOOP_SEND, "encoderStatus(in while):"
						+ encoderStatus);
				NaviLogUtil.debug(
						NaviConstant.TAG_MCODE_TIME,
						"dequeueOutputBuffer finish "
								+ System.currentTimeMillis());

				if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
					// no output available yet
					if (!isEnd) {
						Log.i("syxtest", "!!! INFO_TRY_AGAIN_LATER");

						break; // out of while
					} else {
					}
				} else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
					// not expected for an encoder
					encoderOutputBuffers = mEncoder.getOutputBuffers();
				} else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
				} else if (encoderStatus < 0) {
					// let's ignore it
				} else {
					NaviLogUtil.debug(TAG_LOOP_SEND,
							"dequeue data , encoderStatus is " + encoderStatus);
					NaviLogUtil.debug(
							NaviConstant.TAG_MCODE_TIME,
							"dequeue data success "
									+ System.currentTimeMillis());

					if (mBufferInfo.size != 0) {
						byte[] dataToWrite = new byte[mBufferInfo.size];
						encoderOutputBuffers[encoderStatus].get(dataToWrite,
								mBufferInfo.offset, mBufferInfo.size);

						if (VERBOSE)
							NaviLogUtil.debug(TAG_LOOP_SEND_SUCCESS,
									"will mOutputStream.write :"
											+ isThreadRunning + ","
											+ mBufferInfo.size);
						if (isThreadRunning) {
							Log.i(TAG_LOOP_SEND_SUCCESS,
									"mOutputStream write tickcount : "
											+ System.currentTimeMillis()
											+ " size : " + mBufferInfo.size
											+ " Thread : "
											+ Thread.currentThread().getId());
							Log.v(TAG_LOOP_SEND_SUCCESS,
									"@@ mOutputStream != null");
							NaviLogUtil
									.debug(TAG_LOOP_SEND_SUCCESS,
											"write data to mOutputStream and send to HU");
							long lastTime = 0;
							if (curTime == 0) {
								curTime = Calendar.getInstance()
										.getTimeInMillis();
								lastTime = curTime;
							} else {
								lastTime = curTime;
								curTime = Calendar.getInstance()
										.getTimeInMillis();
							}
							// TODO
							NaviLogUtil.debug(TAG_LOOP_SEND_SUCCESS,
									"write data to HU success, size is "
											+ mBufferInfo.size
											+ ", time distance is "
											+ (curTime - lastTime)
											+ ",width is " + mapWidth
											+ ", height is " + mapHeight);
						} else {
							NaviLogUtil.debug(TAG_LOOP_SEND,
									"isThreadRunning is " + isThreadRunning);
						}
					}

					mBufferInfo.presentationTimeUs = getPTSUs();
					prevOutputPTSUs = mBufferInfo.presentationTimeUs;

					NaviLogUtil.debug(
							NaviConstant.TAG_MCODE_TIME,
							"releaseOutputBuffer start "
									+ System.currentTimeMillis());
					mEncoder.releaseOutputBuffer(encoderStatus, false);
					NaviLogUtil.debug(
							NaviConstant.TAG_MCODE_TIME,
							"releaseOutputBuffer success "
									+ System.currentTimeMillis());

					if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
						if (!isEnd) {
						} else {
						}
						break; // out of while
					}
				}
			}
		} catch (Exception e) {
			if (e != null && e instanceof IOException) {
				if (VERBOSE) {
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "mediacodec error :"
							+ e.toString());
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "isThreadRunning : "
							+ isThreadRunning);
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "_syncProxy : "
							+ _syncProxy);
				}
			} else {
				if (VERBOSE)
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "mediacodec error :"
							+ e.toString());
			}
		}
	}

	private void testSendDrawDataTest() {
		NaviLogUtil.debug(TAG_LOOP_SEND, "call testSendDrawData on "
				+ this.getClass().getName());
		if (null != mMapBuffer && mMapBuffer.length > 0) {
			final int[] mapData = mMapBuffer;
			// Log.i(TAG,
			// "@@ [testSendDrawData] 1 time : " + System.currentTimeMillis()
			// + " Thread : " + +Thread.currentThread().getId());
			NaviLogUtil.debug(NaviConstant.TAG_MCODE_TIME, "start convertYUV "
					+ System.currentTimeMillis());
			byte[] frameData = convertYUV(mapData, mapWidth, mapHeight);
			NaviLogUtil.debug(NaviConstant.TAG_MCODE_TIME,
					"convertYUV success " + System.currentTimeMillis());
			// Log.i(TAG,
			// "@@ [testSendDrawData] 2 time : " + System.currentTimeMillis());

			if (null != frameData && frameData.length > 0) {
				NaviLogUtil.debug(TAG_LOOP_SEND, "testSendDrawData len: "
						+ frameData.length);
				writeDataToStreamTest(false, frameData);
				// writeDataToStream(false, frameData);
			}
			// Log.i(TAG,
			// "@@ [testSendDrawData] 3 time : " + System.currentTimeMillis());
		} else {

		}
	}

	private long prevOutputPTSUs = 0;

	/**
	 * get next encoding presentationTimeUs
	 * 
	 * @return
	 */
	protected long getPTSUs() {
		long result = System.nanoTime() / 1000L;
		// presentationTimeUs should be monotonic
		// otherwise muxer fail to write
		if (result < prevOutputPTSUs)
			result = (prevOutputPTSUs - result) + result;
		NaviLogUtil.debugSendSuccess("pt sus time is :" + result);
		return result;
	}

	public AtomicBoolean inProgressFlag = new AtomicBoolean(false);

	public synchronized void updateProgressFlag(boolean value) {
		inProgressFlag.set(value);
	}

	boolean isStartH264 = false;
	BlockingQueue<ToHuData> tohus = new LinkedBlockingQueue<ToHuData>();

	public boolean isEmpty() {
		return tohus.isEmpty();
	}

	// BlockingQueue<E>

	ToHuData toHuData = null;

	public void doSendHU2(final int count) {
		final long guardedRunStartTime = EglHelper.getInstance()
				.getGuardedRunStartTime();
		if (null != _syncProxy) {
			if (!EglHelper.getInstance().isStartH264()) {
				mOutputStream = (PipedOutputStream) _syncProxy.startH264();
				EglHelper.getInstance().setStartH264(true);
			}

			try {
				MediaCoderHelper.getInstance().drainEncoder(false,
						new MediaCoderHelper.EncoderHandler() {
							@Override
							public void execute(byte[] dataToWrite, int dataSize) {
								long startTime = System.currentTimeMillis()
										- guardedRunStartTime;
								long endTime = 0;
								if (null != dataToWrite && dataSize > 0) {
									try {
										mOutputStream.write(dataToWrite, 0,
												dataSize);
										endTime = System.currentTimeMillis()
												- guardedRunStartTime;
										NaviLogUtil
												.debugQueueToHUCostTime(count
														+ ","
														+ dataSize
														+ ","
														+ Thread.currentThread()
																.getId() + ","
														+ startTime + ","
														+ endTime + 1);
									} catch (IOException e) {
										throw new RuntimeException(e
												.getMessage());
									}
								} else {
									try {
										Thread.sleep(5);
									} catch (InterruptedException e) {
									}
									endTime = System.currentTimeMillis()
											- guardedRunStartTime;
									NaviLogUtil.debugQueueToHUCostTime(count
											+ "," + dataSize + ","
											+ Thread.currentThread().getId()
											+ "," + startTime + "," + endTime
											+ 1);
								}
							}
						});
			} catch (Exception e) {
				if (e != null && e instanceof IOException) {
					NaviLogUtil.error(TAG_LOOP_SEND_ERROR, "mediacodec error :"
							+ e.toString());
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "isThreadRunning : "
							+ isThreadRunning);
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "_syncProxy : "
							+ _syncProxy);

					if (isThreadRunning && _syncProxy != null) {
						mOutputStream = (PipedOutputStream) _syncProxy
								.startH264();
						NaviLogUtil.debug(TAG_LOOP_SEND_ERROR,
								"mOutputStream : " + mOutputStream);
					}
				} else {
					NaviLogUtil.error(TAG_LOOP_SEND_ERROR, "mediacodec error :"
							+ e.toString());
				}
			}
		} else {
			isStartH264 = false;
		}
	}

	public synchronized void doSendHU(final int count) {
		final long guardedRunStartTime = EglHelper.getInstance()
				.getGuardedRunStartTime();
		final long decoderStartTime = System.currentTimeMillis()
				- guardedRunStartTime;
		if (null != _syncProxy && this.isRunning()) {
			if (!EglHelper.getInstance().isStartH264()) {
				// MediaCoderHelper.getInstance().start();
				NaviLogUtil.debugEglStep("first start H264");
				if (null == mOutputStream) {
					NaviLogUtil.debugEglStep("mOutputStream is null");
					mOutputStream = (PipedOutputStream) _syncProxy.startH264();
					NaviLogUtil.debugEglStep("startH264 success");
				} else {
					NaviLogUtil.debugEglStep("mOutputStream is not null");
				}
				EglHelper.getInstance().setStartH264(true);
			}

			// if (!isStartH264) {
			// NaviLogUtil.debugEglStep("first start H264");
			// mOutputStream = (PipedOutputStream) _syncProxy.startH264();
			// isStartH264 = true;
			// }
			NaviLogUtil.debugEglStep(String.format("start send hu(%d)", count));
			try {
				MediaCoderNewHelper.getInstance().drainEncoder(false,
						new MediaCoderNewHelper.EncoderHandler() {
							@Override
							public void execute(byte[] dataToWrite, int dataSize) {
								long decoderEndTime = System
										.currentTimeMillis()
										- guardedRunStartTime;
								long toQueueTime = 0, toQueueStartTime = 0;
								if (null != dataToWrite && dataSize > 0) {
									toQueueStartTime = System
											.currentTimeMillis()
											- guardedRunStartTime;
									tohus.add(new ToHuData(dataToWrite, 0,
											dataSize, count));
									// EglHelper.getInstance().signLoopSendStart();
									NaviLogUtil
											.debugEglStep("sign LoopSend Start...");
									toQueueTime = System.currentTimeMillis()
											- guardedRunStartTime;
								}
								NaviLogUtil.debugDecoderCostTime(count + ","
										+ dataSize + "," + decoderStartTime
										+ "," + decoderEndTime + ","
										+ toQueueStartTime + "," + toQueueTime
										+ "," + tohus.size());
							}

						});
				NaviLogUtil.debugEglStep(String.format("send hu(%d) success",
						count));
			} catch (NullPointerException e) {
				NaviLogUtil.debugEglStep("decoder error:NullPointerException");
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : elements) {
					NaviLogUtil
							.debugEglStep(String
									.format("error: class is %s, method name is %s , linenumber is %d",
											stackTraceElement.getClassName(),
											stackTraceElement.getMethodName(),
											stackTraceElement.getLineNumber()));
				}
			} catch (IllegalStateException e) {
				NaviLogUtil.debugEglStep("decoder error:IllegalStateException");
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : elements) {
					NaviLogUtil
							.debugEglStep(String
									.format("error: class is %s, method name is %s , linenumber is %d",
											stackTraceElement.getClassName(),
											stackTraceElement.getMethodName(),
											stackTraceElement.getLineNumber()));
				}
			} catch (Throwable e) {
				// MediaCoderHelper.getInstance().reset();
				NaviLogUtil.debugEglStep("decoder error:" + e.getMessage());
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : elements) {
					NaviLogUtil
							.debugEglStep(String
									.format("error: class is %s, method name is %s , linenumber is %d",
											stackTraceElement.getClassName(),
											stackTraceElement.getMethodName(),
											stackTraceElement.getLineNumber()));
				}
				if (e != null && e instanceof IOException) {
					NaviLogUtil.error(TAG_LOOP_SEND_ERROR, "mediacodec error :"
							+ e.toString());
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "isThreadRunning : "
							+ isThreadRunning);
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "_syncProxy : "
							+ _syncProxy);

					if (isThreadRunning && _syncProxy != null) {
						mOutputStream = (PipedOutputStream) _syncProxy
								.startH264();
						NaviLogUtil.debug(TAG_LOOP_SEND_ERROR,
								"mOutputStream : " + mOutputStream);
					}
				} else {
					NaviLogUtil.error(TAG_LOOP_SEND_ERROR, "mediacodec error :"
							+ e.toString());
				}
			}
		} else {
			NaviLogUtil.debugEglStep("isStartH264 is false");
			// isStartH264 = false;
			EglHelper.getInstance().setStartH264(false);
			try {
				synchronized (encoderLock) {
					MediaCoderNewHelper.getInstance().drainEncoder( false,
							new MediaCoderNewHelper.EncoderHandler() {
								@Override
								public void execute(byte[] dataToWrite, int dataSize) {
									NaviLogUtil
											.debugEglStep("mOutputStream write size : "
													+ dataSize);
	//								testWriteFile( dataToWrite );
									// MediaCoderHelper.getInstance().resetSurface();
								}
							});
				}
			} catch (IllegalStateException e) {
				NaviLogUtil.debugEglStep("decoder error:IllegalStateException");
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : elements) {
					NaviLogUtil
							.debugEglStep(String
									.format("error: class is %s, method name is %s , linenumber is %d",
											stackTraceElement.getClassName(),
											stackTraceElement.getMethodName(),
											stackTraceElement.getLineNumber()));
				}
			} catch (Exception e) {
				NaviLogUtil.debugEglStep("decoder error:" + e.getMessage());
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : elements) {
					NaviLogUtil
							.debugEglStep(String
									.format("error: class is %s, method name is %s , linenumber is %d",
											stackTraceElement.getClassName(),
											stackTraceElement.getMethodName(),
											stackTraceElement.getLineNumber()));
				}
			}
		}
	}

	Object encoderLock = new Object();
	
	private void loopSendsNew(final int count) {
		final long guardedRunStartTime = EglHelper.getInstance()
				.getGuardedRunStartTime();
		if (null != _syncProxy && this.isRunning()) {
			if (!EglHelper.getInstance().isStartH264()) {
				// MediaCoderHelper.getInstance().start();
				NaviLogUtil.debugEglStep("first start H264");
				if (null == mOutputStream) {
					NaviLogUtil.debugEglStep("mOutputStream is null");
					mOutputStream = (PipedOutputStream) _syncProxy.startH264();
					NaviLogUtil.debugEglStep("startH264 success");
				} else {
					NaviLogUtil.debugEglStep("mOutputStream is not null");
				}
				EglHelper.getInstance().setStartH264(true);
			}
			
			// if (!isStartH264) {
			// NaviLogUtil.debugEglStep("first start H264");
			// mOutputStream = (PipedOutputStream) _syncProxy.startH264();
			// isStartH264 = true;
			// }
			NaviLogUtil.debugEglStep(String.format("start send hu(%d)", count));
			try {
				MediaCoderHelper.getInstance().drainEncoder(false,
						new MediaCoderHelper.EncoderHandler() {
							@Override
							public void execute(byte[] dataToWrite, int dataSize) {
								if (null != dataToWrite && dataSize > 0) {
									if (mOutputStream != null) {
										long loopStartTime = System
												.currentTimeMillis()
												- guardedRunStartTime;
										long poolTime = System
												.currentTimeMillis()
												- guardedRunStartTime;
										NaviLogUtil.debugEglStep(String
												.format("write data(%d) to mOutputStream and send to HU, size is %d",
														count ,
														dataSize ));
										long toHuFinishTime = 0;
										try {
											mOutputStream.write(
													dataToWrite, 0,
													dataToWrite.length);
											toHuFinishTime = System
													.currentTimeMillis()
													- guardedRunStartTime;
											NaviLogUtil
													.debugQueueToHUCostTime(count
															+ ","
															+ dataSize
															+ ","
															+ count
															+ ","
															+ loopStartTime
															+ ","
															+ poolTime
															+ ","
															+ toHuFinishTime
															+ "," + 1);
										} catch (IOException e) {
											throw new RuntimeException(e
													.getMessage());
										}
									} else {
										NaviLogUtil
												.debugEglStep("@@ mOutputStream == null");
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
										}
									}
								}
							}

						});
				NaviLogUtil.debugEglStep(String.format("send hu(%d) success",
						count));
			} catch (NullPointerException e) {
				NaviLogUtil.debugEglStep("decoder error:NullPointerException");
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : elements) {
					NaviLogUtil
							.debugEglStep(String
									.format("error: class is %s, method name is %s , linenumber is %d",
											stackTraceElement.getClassName(),
											stackTraceElement.getMethodName(),
											stackTraceElement.getLineNumber()));
				}
			} catch (IllegalStateException e) {
				NaviLogUtil.debugEglStep("decoder error:IllegalStateException");
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : elements) {
					NaviLogUtil
							.debugEglStep(String
									.format("error: class is %s, method name is %s , linenumber is %d",
											stackTraceElement.getClassName(),
											stackTraceElement.getMethodName(),
											stackTraceElement.getLineNumber()));
				}
			} catch (Throwable e) {
				// MediaCoderHelper.getInstance().reset();
				NaviLogUtil.debugEglStep("decoder error:" + e.getMessage());
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : elements) {
					NaviLogUtil
							.debugEglStep(String
									.format("error: class is %s, method name is %s , linenumber is %d",
											stackTraceElement.getClassName(),
											stackTraceElement.getMethodName(),
											stackTraceElement.getLineNumber()));
				}
				if (e != null && e instanceof IOException) {
					NaviLogUtil.error(TAG_LOOP_SEND_ERROR, "mediacodec error :"
							+ e.toString());
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "isThreadRunning : "
							+ isThreadRunning);
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "_syncProxy : "
							+ _syncProxy);

					if (isThreadRunning && _syncProxy != null) {
						mOutputStream = (PipedOutputStream) _syncProxy
								.startH264();
						NaviLogUtil.debug(TAG_LOOP_SEND_ERROR,
								"mOutputStream : " + mOutputStream);
					}
				} else {
					NaviLogUtil.error(TAG_LOOP_SEND_ERROR, "mediacodec error :"
							+ e.toString());
				}
			}
		} else {
			NaviLogUtil.debugEglStep("isStartH264 is false");
			// isStartH264 = false;
			EglHelper.getInstance().setStartH264(false);
			try {
				MediaCoderHelper.getInstance().drainEncoder(false,
						new MediaCoderHelper.EncoderHandler() {
							@Override
							public void execute(byte[] dataToWrite, int dataSize) {
								NaviLogUtil
										.debugEglPixMap("mOutputStream write size : "
												+ dataSize);
//								testWriteFile( dataToWrite );
								// MediaCoderHelper.getInstance().resetSurface();
							}
						});
			} catch (IllegalStateException e) {
				NaviLogUtil.debugEglStep("decoder error:IllegalStateException");
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : elements) {
					NaviLogUtil
							.debugEglStep(String
									.format("error: class is %s, method name is %s , linenumber is %d",
											stackTraceElement.getClassName(),
											stackTraceElement.getMethodName(),
											stackTraceElement.getLineNumber()));
				}
			} catch (Exception e) {
				NaviLogUtil.debugEglStep("decoder error:" + e.getMessage());
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement stackTraceElement : elements) {
					NaviLogUtil
							.debugEglStep(String
									.format("error: class is %s, method name is %s , linenumber is %d",
											stackTraceElement.getClassName(),
											stackTraceElement.getMethodName(),
											stackTraceElement.getLineNumber()));
				}
			}
		}
	}

	private void loopSends(int index) {
		long guardedRunStartTime = EglHelper.getInstance()
				.getGuardedRunStartTime();
		if (mOutputStream != null) {
			long loopStartTime = System.currentTimeMillis()
					- guardedRunStartTime;
			ToHuData toHuData = tohus.poll();
			long poolTime = System.currentTimeMillis() - guardedRunStartTime;
			if (null != toHuData) {
				NaviLogUtil
						.debugEglStep(String
								.format("write data(%d) to mOutputStream and send to HU, size is %d",
										toHuData.getFrameCount(),
										toHuData.getLen()));
				long toHuFinishTime = 0;
				try {
					mOutputStream.write(toHuData.getDataToWrite(), 0,
							toHuData.getLen());
					toHuFinishTime = System.currentTimeMillis()
							- guardedRunStartTime;
					NaviLogUtil.debugQueueToHUCostTime(index + ","
							+ toHuData.getDataToWrite().length + ","
							+ toHuData.getFrameCount() + "," + loopStartTime
							+ "," + poolTime + "," + toHuFinishTime + "," + 1);
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
			} else {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
				}
				long toHuFinishTime = System.currentTimeMillis()
						- guardedRunStartTime;
				NaviLogUtil.debugQueueToHUCostTime(index + "," + 0 + ","
						+ Thread.currentThread().getId() + "," + loopStartTime
						+ "," + poolTime + "," + toHuFinishTime + "," + 0);
			}
		} else {
			NaviLogUtil.debugEglStep("@@ mOutputStream == null");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public void doSendHUAll(final int count) {
		final long guardedRunStartTime = EglHelper.getInstance()
				.getGuardedRunStartTime();
		final long decoderStartTime = System.currentTimeMillis()
				- guardedRunStartTime;
		int hasDataFlag = 0;
		int isSend = 0;
		long toQueueStartTime = 0, toQueueEndTime = 0;
		if (null != _syncProxy) {
			if (!isStartH264) {
				NaviLogUtil.debugEglStep("first start H264");
				mOutputStream = (PipedOutputStream) _syncProxy.startH264();
				isStartH264 = true;
			}

			final TmpHuData tmpHuData = new TmpHuData();
			try {
				MediaCoderHelper.getInstance().drainEncoder(false,
						new MediaCoderHelper.EncoderHandler() {
							@Override
							public void execute(byte[] dataToWrite, int dataSize) {
								// long decoderEndTime = System
								// .currentTimeMillis()
								// - guardedRunStartTime;
								if (null != dataToWrite && dataSize > 0) {
									tmpHuData.add(dataToWrite, dataSize);
									// datas.add(new ToHuData(dataToWrite, 0,
									// dataSize, count));
									// tohus.add( new
									// ToHuData(dataToWrite,0,dataSize,count) );
								}
							}

						});
				toQueueStartTime = System.currentTimeMillis()
						- guardedRunStartTime;
				if (tmpHuData.size > 0) {
					hasDataFlag = 1;
					if (tohus.isEmpty()) {
						isSend = 1;
						tohus.add(new ToHuData(tmpHuData.getBytes(), 0,
								tmpHuData.getSize(), count));
						toQueueEndTime = System.currentTimeMillis()
								- guardedRunStartTime;
						NaviLogUtil.debugDecoderCostTime(count + ","
								+ decoderStartTime + "," + toQueueStartTime
								+ "," + toQueueEndTime + "," + hasDataFlag
								+ "," + isSend);
					} else {
						NaviLogUtil.debugDecoderCostTime(count + ","
								+ decoderStartTime + "," + toQueueStartTime
								+ "," + toQueueEndTime + "," + hasDataFlag
								+ "," + isSend);
					}
				}
			} catch (Exception e) {
				if (e != null && e instanceof IOException) {
					NaviLogUtil.error(TAG_LOOP_SEND_ERROR, "mediacodec error :"
							+ e.toString());
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "isThreadRunning : "
							+ isThreadRunning);
					NaviLogUtil.debug(TAG_LOOP_SEND_ERROR, "_syncProxy : "
							+ _syncProxy);

					if (isThreadRunning && _syncProxy != null) {
						mOutputStream = (PipedOutputStream) _syncProxy
								.startH264();
						NaviLogUtil.debug(TAG_LOOP_SEND_ERROR,
								"mOutputStream : " + mOutputStream);
					}
				} else {
					NaviLogUtil.error(TAG_LOOP_SEND_ERROR, "mediacodec error :"
							+ e.toString());
				}
			}
		} else {
			isStartH264 = false;
			MediaCoderHelper.getInstance().drainEncoder(false,
					new MediaCoderHelper.EncoderHandler() {
						@Override
						public void execute(byte[] dataToWrite, int dataSize) {
							NaviLogUtil
									.debugEglPixMap("mOutputStream write size : "
											+ dataSize);
							// MediaCoderHelper.getInstance().resetSurface();
						}
					});
		}
	}

	private void huRouteCalculate() {
		CurrentViewAction ammn = InstanceManager.getInstance()
				.getCurrentViewAction();
		if (null != ammn) {
			try {
				ammn.rapidRouteCalculateToMapCenter();
			} catch (Exception ex) {
				NaviLogUtil.debugEglStep("RouteCalculate error: "
						+ ex.getMessage());
			}
		} else {
			NaviLogUtil.debugEglStep("RouteCalculate error: ammn is null ");
		}
	}

	private void showNavigation() {
		CurrentViewAction ammn = InstanceManager.getInstance()
				.getCurrentViewAction();
		if (null != ammn) {
			try {
				ammn.showNavigation();
			} catch (Exception ex) {
				NaviLogUtil.debugEglStep("showNavigation error: "
						+ ex.getMessage());
			}
		} else {
			NaviLogUtil.debugEglStep("showNavigation error: ammn is null ");
		}
	}

	class TmpHuData {
		private byte[] bytes = null;
		private int size = 0;

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public void add(byte[] b, int len) {
			if (null == bytes) {
				if (len > 0) {
					bytes = new byte[len];
					System.arraycopy(b, 0, bytes, 0, len);
					this.size = len;
				}
			} else {
				if (len > 0) {
					byte[] newBytes = new byte[this.size + len];
					System.arraycopy(bytes, 0, newBytes, 0, this.size);
					System.arraycopy(b, 0, newBytes, this.size, len);
					this.size = this.size + len;
					this.bytes = newBytes;
				}
			}
		}
	}

	@Override
	public void onServiceEnded(OnServiceEnded serviceEnded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceNACKed(OnServiceNACKed serviceNACKed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOnStreamRPC(OnStreamRPC notification) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStreamRPCResponse(StreamRPCResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialNumberResponse(DialNumberResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendLocationResponse(SendLocationResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShowConstantTbtResponse(ShowConstantTbtResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAlertManeuverResponse(AlertManeuverResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateTurnListResponse(UpdateTurnListResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceDataACK() {
		// TODO Auto-generated method stub
		
	}

 

}
