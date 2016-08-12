package com.billionav.navi.naviscreen.map;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.billionav.jni.UIPathControlJNI;
import com.billionav.navi.app.ext.InstanceManager;
import com.billionav.navi.app.ext.NaviUtil;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.MenuControlIF.MapScreen;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Map;
import com.billionav.navi.sysMsgControl.SysMsgControl;
import com.billionav.navi.system.BuleToothMessageQueue;
import com.billionav.navi.uicommon.IViewControl;
import com.billionav.navi.uitools.DialogTools;
import com.billionav.navi.uitools.GestureDetector;
import com.billionav.navi.uitools.GestureDetector.OnGestureListener;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.update.UpdateController;

public class ADT_Main_Map_Navigation extends ActivityBase implements MapScreen {

	public static final int MAIN_MAP = 10;
	public static final int NAVIGATION = -10;

	private static final String FILE_PATH = "CustomView.properties";

	private final ArrayList<View> views = new ArrayList<View>(0);

	protected Map_Main_Layer2 mapMainLayers;
	protected Map_Navigation_Layers mapNavigationlayers;
	protected Map_Menu_Bar mapMenubar;

	CurrentViewAction currentViewAction;
	// public static boolean isFront = false;

	private OnGestureListener gestureListener = new GestureDetector.SimpleGestureListener() {
		@Override
		public boolean onScroll(boolean isFrist, MotionEvent down,
				MotionEvent move, float distanceX, float distanceY) {
			if (isFrist) {
				if (!isMainMap()) {
					showMain();
				}
				// TODO 2016/3/10
				mapMainLayers.showLeftComponents();
			}
			return true;
		}

		public boolean on2FingerScale(boolean isFrist, MotionEvent down1,
				MotionEvent down2, MotionEvent move, float distanceMove,
				float distanceDown, float distanceDiff) {
			if (isFrist) {
				if (!isMainMap()) {
					showMain();
				}
				// TODO 2016/3/10
				mapMainLayers.showLeftComponents();
			}
			return true;
		};

		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO 2016/3/10
			mapMenubar.onTap();
			return true;
		};

	};

	protected boolean isNeedSetScreenId() {
		return false;
	};

	protected void OnResume() {
		super.OnResume();

		Log.d("test", "mapNavigationlayers.onResume");

		MapOverwriteLayer.getInstance().switchCradleInfo();
		if (isDemo()) {
			POI_Mark_Control.forDemoRun();
		} else {
			POI_Mark_Control.forNavigaitonView();
		}

		if (getBundleNavi().getBoolean("Navigation")) {
			showNavigation();
		} else {
			if (isMainMap()) {
				showMain();
			} else {
				showNavigation();
			}
		}

		MapView.getInstance().setGestureListener(gestureListener);
		MapOverwriteLayer.getInstance().setTapPopupEnable(true);
		mapNavigationlayers.onResume();
		mapMenubar.onResume();
		mapMenubar.refreshARAndVoice();
		final int size = views.size();
		for (int i = 0; i < size; i++) {
			View v = views.get(i);
			if (v instanceof IViewControl) {
				((IViewControl) v).onResume();
			}

		}
		// 20160325 add navigation for hu demorun
		currentViewAction = new CurrentViewAction();
		InstanceManager.getInstance().setCurrentViewAction(currentViewAction);
	};

	@Override
	protected void OnPause() {
		Log.d("test", "mapNavigationlayers.OnPause");

		new UIPathControlJNI().SetScreenAttribute(
				UIPathControlJNI.UIC_PT_FIND_OBJ_AUTOREROUTE,
				UIPathControlJNI.UIC_PT_SCREEN_ATTR_NG);
		MapView.getInstance().setGestureListener(null);
		MapOverwriteLayer.getInstance().setTapPopupEnable(false);
		mapMenubar.removeCloseDeyedHandlerAndCloseButtons();
		super.OnPause();
	}

	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);

		setNoTitle();
		setContentView(getContextView(), false);
		showNavigation();
		setListeners();
		SysMsgControl.getInstance().requestSysMsgFromServer();
		initModeTag();
		// send apk update request
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what != 0x02) {
					return;
				}
				// requestApkNewVersion();
				// CameraUpdate.checkoutCameraData();
				// if(IntentOpenCtrl.getIntentKind() ==
				// IntentOpenCtrl.INTENT_KIND_ROUTE) {
				// POIData data = IntentOpenCtrl.PopPoiData();
				// RouteCalcController.instance().rapidRouteCalculateWithDataFromOtherProgram(
				// data.getM_POIName(), data.getM_Lonlat()[0],
				// data.getM_Lonlat()[1]);
				// }
				BuleToothMessageQueue.getInstance().startTask();
			}
		}.sendEmptyMessageDelayed(0x02, 3000);
		MapView.getInstance().initSizeScaleBar();
		// 20160325 add navigation for hu demorun
		currentViewAction = new CurrentViewAction();
		InstanceManager.getInstance().setCurrentViewAction(currentViewAction);
	}

	protected void initModeTag() {
		// TODO Auto-generated method stub

	}

	private void requestApkNewVersion() {
		UpdateController.getInstance().requestApkList();

	}

	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		mapNavigationlayers.OnTrigger(cTriggerInfo);
		mapMainLayers.OnTrigger(cTriggerInfo);
		mapMenubar.OnTrigger(cTriggerInfo);
		final int size = views.size();
		for (int i = 0; i < size; i++) {
			View v = views.get(i);
			if (v instanceof IViewControl) {
				((IViewControl) v).receiveTrigger(cTriggerInfo);
			}

		}
		return super.OnTrigger(cTriggerInfo);
	}

	private void setListeners() {
		mapMainLayers.setMyLocationClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestSNSData();
				showNavigation();
				MapOverwriteLayer.getInstance().DontKeepPopupInScreen();
			}
		});

	}

	private void requestSNSData() {
		// CLonLat lonlat =(new jniLocationIF()).GetCarPosition();
		// if(!SnsControl.Instance().IsInRange((int)lonlat.Longitude,
		// (int)lonlat.Latitude)) {
		// SnsControl.Instance().RequestSnsDataRestartTimer((int)lonlat.Longitude,
		// (int)lonlat.Latitude);
		// }
	}

	private View getContextView() {
		Log.d("test", "mapNavigationlayers.addView");

		FrameLayout rootView = new FrameLayout(this);
		rootView.addView(mapMainLayers = returnLayer());
		rootView.addView(mapNavigationlayers = new Map_Navigation_Layers(this));
		rootView.addView(mapMenubar = new Map_Menu_Bar(this));
		if (SystemTools.EDITION_CRADLE.equals(SystemTools.getApkEdition())) {
			createViews(FILE_PATH);
			final int size = views.size();
			for (int i = 0; i < size; i++) {
				rootView.addView(views.get(i));
			}
		}
		mapNavigationlayers.setIllustListener(mapMenubar);
		return rootView;
	}

	private void createViews(String propFilePath) {
		// load the asset
		Properties prop = new Properties();
		InputStream is = null;
		try {
			is = this.getAssets().open(propFilePath);
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// reflect the controllers from asset
		Enumeration<?> keyEnum = prop.propertyNames();
		while (keyEnum.hasMoreElements()) {
			String key = (String) keyEnum.nextElement();
			try {

				Class<?> viewCls = Class.forName(prop.getProperty(key));
				Constructor<?> view_Cons = viewCls
						.getConstructor(Context.class);
				View view = (View) view_Cons.newInstance(this);
				views.add(view);
			} catch (ClassNotFoundException e) {
				Log.w("Navi", e.getMessage());
				continue;
			} catch (InstantiationException e) {
				Log.w("Navi", e.getMessage());
				continue;
			} catch (IllegalAccessException e) {
				Log.w("Navi", e.getMessage());
				continue;
			} catch (SecurityException e) {
				Log.w("Navi", e.getMessage());
				continue;
			} catch (NoSuchMethodException e) {
				Log.w("Navi", e.getMessage());
				continue;
			} catch (IllegalArgumentException e) {
				Log.w("Navi", e.getMessage());
				continue;
			} catch (InvocationTargetException e) {
				Log.w("Navi", e.getMessage());
				continue;
			}
		}

		// close the asset
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected Map_Main_Layer2 returnLayer() {
		return new Map_Main_Layer2(this);
	}

	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mapNavigationlayers.closeIllustView()) {
				Log.d("test", "mapNavigationlayers.closeIllustView");
				return true;
			}
			if (isDemo()) {
				NaviUtil.stopDemoDriving();
				// new UIPathControlJNI().StopDemoDriving();
				Log.i("icon", "isdemo::" + isDemo());
				return true;
			}
			// ForwardKeepDepthWinChange(ADT_Top_Menu.class);
			DialogTools.createExitDialog(this).show();

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			mapMenubar.onTap();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_SEARCH && !isDemo()) {
			MenuControlIF.Instance().ForwardWinChange(ADT_Srch_Map.class);
			return true;
		}
		return false;
	}

	private boolean isDemo() {
		return (new UIPathControlJNI().GetDemoStatus() == UIPathControlJNI.UIC_PT_DEMO_STATUS_ON);
	}

	private boolean isMainMap() {
		return mapMainLayers.isShowing();
	}

	public void showMain() {
		Log.d("test", "mapNavigationlayers.hide");
		mapNavigationlayers.hide();

		// TODO 2016/3/10
		mapMainLayers.show();
		// mapMainLayers.hide();
		new UIPathControlJNI().SetScreenAttribute(
				UIPathControlJNI.UIC_PT_FIND_OBJ_AUTOREROUTE,
				UIPathControlJNI.UIC_PT_SCREEN_ATTR_NG);
		mapMenubar.notifyStatusChanged(MAIN_MAP);
	}

	public void showNavigation() {
		Log.d("test", "mapNavigationlayers.show");
		mapMainLayers.hide();
		// TODO 2016/3/10
		mapNavigationlayers.show();
		// mapNavigationlayers.hide();
		new UIPathControlJNI().SetScreenAttribute(
				UIPathControlJNI.UIC_PT_FIND_OBJ_AUTOREROUTE,
				UIPathControlJNI.UIC_PT_SCREEN_ATTR_OK);
		mapMenubar.notifyStatusChanged(NAVIGATION);
	}

	@Override
	protected void OnDestroy() {
		Log.d("test", "mapNavigationlayers.destory");
		mapNavigationlayers.destory();
		mapMenubar.destory();
		BuleToothMessageQueue.getInstance().stopTask();
		// 20160325 add navigation for hu demorun
		InstanceManager.getInstance().setCurrentViewAction(null);
		super.OnDestroy();
	}

	public class CurrentViewAction {
		public CurrentViewAction() {
		}

		public void showNavigation() {
			runOnUiThread(new Runnable() {
				public void run() {
					mapMainLayers.hide();
					mapNavigationlayers.show();
					// mapNavigationlayers.hide();
					new UIPathControlJNI().SetScreenAttribute(
							UIPathControlJNI.UIC_PT_FIND_OBJ_AUTOREROUTE,
							UIPathControlJNI.UIC_PT_SCREEN_ATTR_OK);
					mapMenubar.notifyStatusChanged(NAVIGATION);
					MapOverwriteLayer.getInstance().DontKeepPopupInScreen();
				}
			});
		}

		public void rapidRouteCalculateToMapCenter() {
			runOnUiThread(new Runnable() {
				public void run() {
					RouteCalcController.instance()
							.rapidRouteCalculateToMapCenterWithoutPopup();
				}
			});
		}

	}
}
