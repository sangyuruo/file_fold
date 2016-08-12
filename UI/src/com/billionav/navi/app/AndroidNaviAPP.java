/**
 * 
 */
package com.billionav.navi.app;

import java.io.File;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.billionav.jni.FileSystemJNI;
import com.billionav.jni.NaviMainJNI;
import com.billionav.jni.UIMenuControlJNI;
import com.billionav.navi.app.ext.DispatchingStrategy;
import com.billionav.navi.app.ext.MessageDispatcher;
import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.app.ext.log.NaviLogBean;
import com.billionav.navi.app.ext.log.NaviLogUtil;
import com.billionav.navi.app.ext.sdl.ScreenUtil;
import com.billionav.navi.system.GlobalVar;
import com.billionav.navi.system.ProcStateMonitor;
import com.billionav.navi.system.ScreenOffManager;
import com.billionav.supsys.SupSys;

/**
 * @author zhuangyao
 *
 */
public class AndroidNaviAPP extends Application {
	static String TAG = NaviConstant.TAG;
	String className = AndroidNaviAPP.class.getName();
	public static final int LANGUAGE_JP = 0;
	public static final int LANGUAGE_CH = 1;
	public static final int LANGUAGE_US = 2;

	private static AndroidNaviAPP instance = null;

	private static int m_appLanguage = LANGUAGE_CH;

	public static AndroidNaviAPP getInstance() {
		return instance;
	}

	private ScreenOffManager sScreenManger = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onConfigurationChanged(android.content.res.
	 * Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		setLanguage();
	}

	/**
	 * 
	 */
	public AndroidNaviAPP() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		Log.d(TAG, className + ": on create ");
		initFilePath();
		Log.d(TAG, className + ": LoadLibrary ");
		NaviMainJNI.LoadLibrary();

		// for system crash dummping
		Log.d(TAG, className + ": SupSys init ");
		SupSys.init(this);

		Log.d(TAG, className + ": ui control jni attach ");
		UIMenuControlJNI.J2CAttach();

		// for screen wake lock
		startProcStateMonitor();
		Log.d(TAG, className + ": mapping privatedatas ");
		// mapping privatedatas
		FileSystemJNI.instance().mapPath(getApplicationContext());
		Log.d(TAG, className + ": init navimain jni ");
		NaviMainJNI.Initialize();

		instance = this;
		setLanguage();
		Log.d(TAG, className + ": setLanguage " + m_appLanguage);

		if (NaviConstant.WRITE_COST_TIME_TOFILE) {
			initLogThread();
			// TODO 0322
//			SdlLogManager.getInstance().init();
		}

		ScreenUtil.getInstance().init(this);
		super.onCreate();
//		startOpenApp();
	}

	private void initFilePath() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}

		String rootPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/.kanavi";
		String userPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/kanavi";
		String language = "";
		String edition = "";
		try {

			ApplicationInfo info = getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA);
			language = info.metaData.getString("language");
			edition = info.metaData.getString("edition");
			selectLanguage(language);
		} catch (Exception e) {
			Log.e("test", "AndroidNaviApp initPath error " + getPackageName());
			e.printStackTrace();
			return;
		}
		File root;
		File user;
		if ("trunk".equalsIgnoreCase(edition)) {
			root = new File(rootPath + language + "/");
			user = new File(userPath + language + "/");
		} else {
			root = new File(rootPath + "suning" + "/");
			user = new File(userPath + "suning" + "/");
		}
		Log.d(TAG, className + ":file path = " + root.getAbsolutePath());
		if (!root.exists()) {
			root.mkdirs();
		}
		if (!user.exists()) {
			user.mkdirs();
		}
	}

	private void startProcStateMonitor() {
		GlobalVar
				.setActivityManager((ActivityManager) getSystemService(ACTIVITY_SERVICE));
		GlobalVar.setContentResolver(getContentResolver());
		GlobalVar
				.setPowerManager((PowerManager) getSystemService(Context.POWER_SERVICE));

		// Start Monitor;
		ProcStateMonitor.start(2000);

		// ScreenManger
		sScreenManger = new ScreenOffManager();
		ProcStateMonitor.addStateChangeListener(sScreenManger);
	}

	private void stopProcStateMonitor() {
		ProcStateMonitor.removeStateChangeListener(sScreenManger);
		ProcStateMonitor.stop();
	}

	public void setLanguage() {
		Resources resource = getResources();
		Configuration config = getResources().getConfiguration();
		int langtype = getLanguageType();
		switch (langtype) {
		case LANGUAGE_US:
			config.locale = Locale.ENGLISH;
			break;
		case LANGUAGE_CH:
			config.locale = Locale.SIMPLIFIED_CHINESE;
			break;
		case LANGUAGE_JP:
			config.locale = Locale.JAPANESE;
			break;
		default:
			break;
		}
		config.locale = Locale.ENGLISH;
		DisplayMetrics dm = resource.getDisplayMetrics();
		resource.updateConfiguration(config, dm);
	}

	public int getLanguageType() {
		return m_appLanguage;
	}

	public int selectLanguage(String local) {
		m_appLanguage = AndroidNaviAPP.LANGUAGE_CH;
		if ("jp".equals(local)) {
			m_appLanguage = AndroidNaviAPP.LANGUAGE_JP;
		}
		if ("cn".equals(local) || "suning".equals(local)) {
			m_appLanguage = AndroidNaviAPP.LANGUAGE_CH;
		}
		if ("us".equals(local)) {
			m_appLanguage = AndroidNaviAPP.LANGUAGE_US;
		}

		return AndroidNaviAPP.LANGUAGE_CH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		stopProcStateMonitor();
		if (null != logHandler) {
			logHandler.dispose();
			logHandler = null;
		}
//		SdlLogManager.getInstance().close();
		super.onTerminate();
	}

	private void initLogThread() {
		synchronized (LOG_MESSAGE_QUEUE_THREAD_LOCK) {
			// Ensure incomingProxyMessageDispatcher is null
			if (logHandler != null) {
				logHandler.dispose();
				logHandler = null;
			}

			logHandler = new MessageDispatcher<NaviLogBean>(
					"INCOMING_MESSAGE_DISPATCHER",
					new DispatchingStrategy<NaviLogBean>() {
						@Override
						public void dispatch(NaviLogBean message) {
							NaviLogUtil.writeLogToFile(message.getFileName(),
									message.getMsg());
						}

						@Override
						public void handleDispatchingError(String info,
								Exception ex) {
							NaviLogUtil.writeLogToFile("dispatchingError.txt",
									ex.getMessage());
						}

						@Override
						public void handleQueueingError(String info,
								Exception ex) {
							NaviLogUtil.writeLogToFile("queueingError.txt",
									ex.getMessage());
						}
					});
		}
	}

	public static MessageDispatcher<NaviLogBean> getLogHandler() {
		return logHandler;
	}

	public static MessageDispatcher<NaviLogBean> logHandler;
	private static final Object LOG_MESSAGE_QUEUE_THREAD_LOCK = new Object();

	private void startOpenApp() {
		final Context context = this.getApplicationContext();
		new Thread(new Runnable() {
			@Override
			public void run() {
				openApp(context, "com.poisearch");
				// Intent intent = new Intent(Intent.ACTION_MAIN);
				// intent.setComponent(new ComponentName("com.poisearch",
				// "com.poisearch.app.POIMain"));
				// //
				// com.ford.syncV4.android.mediaplayerapp/com.ford.syncV4.android.app.MediaPlayerMain
				// // com.poisearch/.app.POIMain
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// this.getApplicationContext().startActivity(intent);
			}
		}).start();
	}
	
	private static void debug(String msg) {
		Log.d("open_app", msg);
	}

	public static boolean openApp(Context context, String packageName) {
		debug("start open app:" + packageName);
		try {
			PackageManager manager = context.getPackageManager();
			Intent i = manager.getLaunchIntentForPackage(packageName);
			if (i == null) {
				debug("open app: intent is null");
				return false;
				// throw new PackageManager.NameNotFoundException();
			}
			i.addCategory(Intent.CATEGORY_LAUNCHER);
			context.startActivity(i);
			return true;
		} catch (Throwable ex) {
			debug("open app error:" + ex.getMessage());
			return false;
		}
	}
}
