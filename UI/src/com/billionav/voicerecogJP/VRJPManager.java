package com.billionav.voicerecogJP;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchControlJNI.UIC_SCM_POIReqParam;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.srch.ADT_Srch_List;
import com.billionav.navi.uicommon.UIC_RouteCommon;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.ui.R;
import com.billionav.voicerecogJP.UI.ContactControl;
import com.billionav.voicerecogJP.UI.ContactInfo;
import com.billionav.voicerecogJP.VR.IntentionClient;
import com.billionav.voicerecogJP.VR.IntentionClient.ResultListener;
import com.billionav.voicerecogJP.VR.VRClient;
import com.billionav.voicerecogJP.VR.VoiceRecognizer;
import com.billionav.voicerecogJP.VR.VrAnalysisResult;
import com.billionav.voicerecogJP.VR.VrListener;
import com.billionav.voicerecogJP.VR.VrResultAnalysiserIF;

public class VRJPManager {
	public static boolean isDocomoDemo = false;
	
	public final static int VOLUME_ADJUST_STEP = 5;
	
	public final static int VOICE_COMMAND_FREEWORD = 3;
	public final static int VOICE_COMMAND_POI = 4;
	public final static int VOICE_COMMAND_ROUTE_DELETE = 5;
	public final static int VOICE_COMMAND_SCALE_ZOOMIN = 6;
	public final static int VOICE_COMMAND_SCALE_ZOOMOUT = 7;
	public final static int VOICE_COMMAND_VOLUME_UP = 9;
	public final static int VOICE_COMMAND_VOLUME_DOWN = 16;
	public final static int VOICE_COMMAND_VOLUME_MUTE = 17;
	public final static int VOICE_COMMAND_MAP_NORTH_UP = 18;
	public final static int VOICE_COMMAND_MAP_HEADING_UP = 19;
	public final static int VOICE_COMMAND_MAP_SKY_VIEW = 20;
	public final static int VOICE_COMMAND_PHONE_CALL = 32;

	private boolean isSearchFromVoiceCommand = false;
	public String keyword;
	private int searchType;
	
	private static VRJPManager instanceObj;
	private static VRClient mVrClient = new VRClient();
	private CProgressDialog loadingView;

	public static VRJPManager Instance() {
		if (instanceObj == null) {
			instanceObj = new VRJPManager();
		}
		return instanceObj;
	}
	
	public void initVRManager(Context c) {
		VoiceRecognizer.instance().initialize(c);
		initIntentClient();
	}

	private void initIntentClient() {
		IntentionClient intentClient = IntentionClient.instance();
		intentClient.init();
		intentClient.registerResultListener(resultListener);
	}
	
	public void startService(Activity activity) {
		
		int vrStatus = mVrClient.startRecognition(activity, uiVrListener);
		if(vrStatus == VRClient.VRC_NO_ENGINE
				|| vrStatus == VRClient.VRC_ENGINE_OPEN_FAILED)
		{
			Toast.makeText(activity, getString(R.string.MSG_VR_01_02), Toast.LENGTH_SHORT).show();
		}else if(vrStatus == VRClient.VRC_WRONG_STATE){
			Toast.makeText(activity, getString(R.string.MSG_VR_01_03), Toast.LENGTH_SHORT).show();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (VRClient.VR_REQUEST_CODE == requestCode) {
			mVrClient.notifyResult(resultCode, data);
		}
	}
	
	public void onTrigger(NSTriggerInfo cTriggerInfo) {
		if(!isSearchFromVoiceCommand){
			return;
		}
		
		if(cTriggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED) {
			return;
		}
		
		Log.d("test","VRJPManager.onTrigger");
		
		if(cTriggerInfo.m_lParam2 != 0) {
			closeLoadingView();
			CustomToast.showToast(NaviViewManager.GetViewManager(), R.string.STR_MM_02_03_01_07, 1000);
			return;
		}
		
		UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
		if(cTriggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) {
			if(searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) == 0) {
				showNoSrchResultToast();;
			} else {
				BundleNavi.getInstance().put("keyword", keyword);
				MenuControlIF.Instance().ForwardWinChange(ADT_Srch_List.class);
			}
		}
		closeLoadingView();
		return;
	}

	private VrListener uiVrListener = new VrListener() {

		public void onError(int err, int engType) {
			if (err == VRClient.ERR_VRPKG_INVALID) {
				
			} else {
				
			}
			closeLoadingView();
			mVrClient.stopRecognition();
		}

		@Override
		public void onResults(List<String> results, int engType) {
			
			NaviViewManager.GetViewManager().runOnUiThread(new Runnable() {
				public void run() {
					closeLoadingView();					
				}
			});
			
			final String result = results.get(0);
			
			if (engType == VRClient.ENG_TYPE_IFLY
					|| engType == VRClient.ENG_TYPE_GOOGLE)
			{
				NaviViewManager.GetViewManager().runOnUiThread(new Runnable() {
					public void run() {
						showWaitingingLoadingViewWithText(result);						
					}
				});
				
			} else {
//				ArrayList<String> resStrList = new ArrayList<String>();
//				resStrList.add(result);
//				Command command = VoiceRecognizer.instance().parseSpeechResult(
//						resStrList);
//				if (command != null) {
//					doActionAfterVoiceCommand(command.getCmdCode(),
//							command.getContent());
//				} else {
//					doActionAfterVoiceCommand(Command.CMD_UNNOWN, "");
//				}
			}
		}

		@Override
		public void onStart(int engType) {
			if(engType == VRClient.ENG_TYPE_IFLY){
				NaviViewManager.GetViewManager().runOnUiThread(new Runnable() {
					public void run() {
						showRecognizingViewWithText(getString(R.string.MSG_VR_01_04));					}
				});
			}
		}
	};
	
	ResultListener resultListener = new ResultListener() {
		@Override
		public void onResult(JSONObject result) {
			VrResultAnalysiserIF.Instance().AnalysisResult(result);
			VrAnalysisResult vrresult = VrResultAnalysiserIF.Instance().getResult();
			String key = vrresult.getKeyword1();
			if(key != null && !"".equals(key)){
				analyzeKeyword(key);
			}
			mVrClient.stopRecognition();
			
			NaviViewManager.GetViewManager().runOnUiThread(new Runnable() {
				public void run() {
					closeLoadingView();
					int cmdId = VrResultAnalysiserIF.Instance().getResult().getCommand_id();
					parseCommandID(cmdId);
				}
			});
		}
	};

	private void showWaitingingLoadingViewWithText(String text) {
		loadingView = CProgressDialog.makeProgressDialog(MenuControlIF
				.Instance().GetCurrentActivity(), text);
		loadingView.setCancelable(false);
		loadingView.show();
	}
	
	private void showSearchLoadingViewWithText(String text) {
		loadingView = CProgressDialog.makeProgressDialog(MenuControlIF
				.Instance().GetCurrentActivity(), text);
		loadingView.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					if(event.getAction() == KeyEvent.ACTION_DOWN){
						UISearchControlJNI.Instance().OnCancel(searchType);
					}
				}
				return false;
			}
		});
		loadingView.show();
	}
	
	private void showRecognizingViewWithText(String text) {
		loadingView = CProgressDialog.makeProgressDialog(MenuControlIF
				.Instance().GetCurrentActivity(), text);
		loadingView.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					if(event.getAction() == KeyEvent.ACTION_DOWN){
						mVrClient.stopRecognition();
					}
				}
				return false;
			}
		});
		loadingView.show();
	}

	private void closeLoadingView() {
		if (loadingView != null && loadingView.isShowing()) {
			loadingView.dismiss();
			loadingView = null;
		}
	}
	
	private void parseCommandID(int id) {
		switch (id) {
		case VOICE_COMMAND_FREEWORD:
			searchType = UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD;
			doFreewordSearch();
			break;
		case VOICE_COMMAND_POI:
			searchType = UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY;
			doPOISearch();
			break;
		case VOICE_COMMAND_ROUTE_DELETE:
			tryDeleteRoute();
			break;
		case VOICE_COMMAND_SCALE_ZOOMIN:
			doMapScaleChange(true);
			break;
		case VOICE_COMMAND_SCALE_ZOOMOUT:
			doMapScaleChange(false);
			break;
		case VOICE_COMMAND_MAP_NORTH_UP:
		case VOICE_COMMAND_MAP_HEADING_UP:
		case VOICE_COMMAND_MAP_SKY_VIEW:
			doMapDirectionChange(id);
			break;
		case VOICE_COMMAND_VOLUME_UP:
		case VOICE_COMMAND_VOLUME_DOWN:
		case VOICE_COMMAND_VOLUME_MUTE:
			doAdjustVolume(id);
			break;
		case VOICE_COMMAND_PHONE_CALL:
			doPhoneCall();
			break;
		default:
			showToast(getString(R.string.MSG_VR_01_05), true);
			break;
		}
	}

	/* -------------------Search functions ---------------------- */
	private void analyzeKeyword(String key){
		int spIndex = key.indexOf("-");
		
		keyword = key.substring(0, spIndex);
		
		String intentWord = key.substring(spIndex+1);
		
		if("address".equals(intentWord)){
		}
		
		if("upoi".equals(intentWord)){
		}
		
		if("gpoi".equals(intentWord)){
		}
		
	}
	
	private void doFreewordSearch(){
		isSearchFromVoiceCommand = true;
		UIC_SCM_POIReqParam param = new UIC_SCM_POIReqParam();
		param.type = searchType;
		param.btn_id = UISearchControlJNI.UIC_SCM_BTN_ID_SEARCH;
		param.act_id = UISearchControlJNI.UIC_SCM_ACT_ID_NORMAL;
		if(UIMapControlJNI.GetCarPositonMode()){
			param.list_id = UISearchControlJNI.UIC_SCM_LIST_ID_CATEGORY_NEAR_CAR;
		}else{
			param.list_id = UISearchControlJNI.UIC_SCM_LIST_ID_CATEGORY_NEAR_CURSOR;
		}
		param.keyword =keyword;
		UISearchControlJNI.Instance().OnPressBtn(param);
		
		String searching = NaviViewManager.GetViewManager().getString(R.string.STR_MM_02_02_01_01);
		showSearchLoadingViewWithText("<"+keyword+">\n"+searching);
	}
	
	private void doPOISearch(){
		isSearchFromVoiceCommand = true;
		isSearchFromVoiceCommand = true;
		UIC_SCM_POIReqParam param = new UIC_SCM_POIReqParam();
		param.type = searchType;
		param.btn_id = UISearchControlJNI.UIC_SCM_BTN_ID_SEARCH;
		param.act_id = UISearchControlJNI.UIC_SCM_ACT_ID_NORMAL;
		if(UIMapControlJNI.GetCarPositonMode()){
			param.list_id = UISearchControlJNI.UIC_SCM_LIST_ID_CATEGORY_NEAR_CAR;
		}else{
			param.list_id = UISearchControlJNI.UIC_SCM_LIST_ID_CATEGORY_NEAR_CURSOR;
		}
		param.keyword =keyword;
		UISearchControlJNI.Instance().OnPressBtn(param);
		
		String searching = NaviViewManager.GetViewManager().getString(R.string.STR_MM_02_02_01_01);
		showSearchLoadingViewWithText("<"+keyword+">\n"+searching);
	}
	
	
	private String getSearchKeyword1() {
		return keyword;
	}
	
	private String getString(int resId){
		return NaviViewManager.GetViewManager().getString(resId);
	}

	private void showNoSrchResultToast() {
		showToast(getSearchKeyword1() +"/n" + getString(R.string.MSG_02_01_01_01), false);
	}
	/* -------------------Search functions ---------------------- */

	/* -------------------Map adjust functions ---------------------- */

	private void tryDeleteRoute() {
		if (UIC_RouteCommon.Instance().isRouteExistAndGuideOn()) {
			RouteCalcController.instance().DeleteRoute();
		} else {
//			showToast("PROMPT_NO_ROUTE", VR_TOAST_NO_ROUTE, true);
		}
	}

	private void doMapScaleChange(boolean scaleUp) {
		if (scaleUp) {
			UIMapControlJNI.ScaleUpDown(true);
		} else {
			UIMapControlJNI.ScaleUpDown(false);
		}
	}

	private void doMapDirectionChange(int cmdID) {
		short mapDirectionKind = -1;
		switch (cmdID) {
		case VOICE_COMMAND_MAP_NORTH_UP:
			mapDirectionKind = UIMapControlJNI.MAP_DIR_MODE_NORTHUP;
			UIMapControlJNI.SetMapDir(mapDirectionKind);
			break;
		case VOICE_COMMAND_MAP_HEADING_UP:
			mapDirectionKind = UIMapControlJNI.MAP_DIR_MODE_HEADINGUP;
			UIMapControlJNI.SetMapDir(mapDirectionKind);
			break;
		case VOICE_COMMAND_MAP_SKY_VIEW:
			UIMapControlJNI.SetMapAngle(UIMapControlJNI.GetMaxAngle());
			break;
		default:
			break;
		}
	}
	/* -------------------Map adjust functions ---------------------- */

	/* -------------------Volume adjust functions ---------------------- */
	private void doAdjustVolume(int id) {
		int cmdId = VrResultAnalysiserIF.Instance().getResult().getCommand_id();
		int subId = cmdId & 0x0000FFF;
		switch (subId) {
		case VOICE_COMMAND_VOLUME_UP:
			setVolume2NextStep(true);
			break;
		case VOICE_COMMAND_VOLUME_DOWN:
			setVolume2NextStep(false);
			break;
		case VOICE_COMMAND_VOLUME_MUTE:
			setVolumeMute();
			break;
		default:
			break;
		}
	}
	
	private void setVolumeMute(){
		Activity acticity = MenuControlIF.Instance().GetCurrentActivity();
		int minVolume = 0;
		setSystemVolume(acticity, minVolume, AudioManager.FLAG_SHOW_UI);
	}

	private void setVolume2NextStep(boolean isUp) {
		int step = -VOLUME_ADJUST_STEP;
		if (isUp) {
			step = VOLUME_ADJUST_STEP;
		}

		Activity acticity = MenuControlIF.Instance().GetCurrentActivity();

		int curVolume = getSystemVolume(acticity);
		int nextVolume = curVolume + step;

		int maxVolume = getSystemMaxVolume(acticity);
		int minVolume = 0;

		if (nextVolume > maxVolume) {
			nextVolume = maxVolume;
		} else if (nextVolume < minVolume) {
			nextVolume = minVolume;
		}

		setSystemVolume(acticity, nextVolume, AudioManager.FLAG_SHOW_UI);
	}

	public void setSystemVolume(Activity activity, int volume, int flag) {
		AudioManager am = (AudioManager) activity
				.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, flag);
	}

	private int getSystemVolume(Activity activity) {
		AudioManager am = (AudioManager) activity
				.getSystemService(Context.AUDIO_SERVICE);
		return am.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	private int getSystemMaxVolume(Activity activity) {
		AudioManager am = (AudioManager) activity
				.getSystemService(Context.AUDIO_SERVICE);
		return am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}

	/* -------------------Volume adjust functions ---------------------- */
	
	private void doPhoneCall(){
		ArrayList<ContactInfo> infoList = ContactControl.getContactControlInstance().getPhoneContactsByName(keyword);
		
		int listSize = infoList.size();
		if(listSize <= 0){
			showToast(getString(R.string.MSG_VR_01_01), false);
		}else if(listSize == 1){
			ContactControl.getContactControlInstance().callOnePerson(infoList.get(0).getNumber());
		}else {
			
		}
	}


	/* -------------------Toast & AlertView functions ---------------------- */
	public void showToast(String msg, boolean bWaitForEnd) {
		Activity activity = NaviViewManager.GetViewManager();
		Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
		toast.show();
		playVoice(activity, bWaitForEnd);
	}

	public void showToast(int msgID, boolean bWaitForEnd) {
		Activity activity = NaviViewManager.GetViewManager();
		String msg = activity.getString(msgID);
		showToast(msg, bWaitForEnd);
	}
	
	public void playVoice(Activity activity, boolean bWaitForEnd) {
		if (bWaitForEnd) {
			startService(activity);
		}
	}
	/* -------------------Toast & AlertView functions ---------------------- */
}
