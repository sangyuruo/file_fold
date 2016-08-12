package com.billionav.navi.component.actionbar;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.billionav.jni.UIMapControlJNI;
//import com.billionav.jni.MapEngineJni;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchControlJNI.UIC_SCM_POIReqParam;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.component.actionbar.ActionBarInputItem.OnSearchButtonListener;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.naviscreen.route.ADT_Route_PointMap;
import com.billionav.navi.naviscreen.srch.ADT_Srch_Map;
import com.billionav.navi.uitools.SearchTools;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;

public class ActionBarSearchInput extends ActionbarInput implements
		TextWatcher, OnSearchButtonListener, OnItemClickListener, OnScrollListener, OnEditorActionListener{
	private static final String KEY_KEY_WORD = "keyword";
	public static final int INPUT_MAX = 30;
	public static final int INPUT_MAX_COUNT = 32;
	
	private String[] historyNames;
	private boolean isTextChangeFromItemClicked = false;
	String searchKeyword = "";
	private int inputHelpNetType = UISearchControlJNI.SRCH_NET_TYPE_OFFLINE;
//	private String inputKeyword = "";
	private Handler cprogressBarHandler = null;
	
	private OnSearchFinishedListener searchListener;
	
	public ActionBarSearchInput(Context context) {
		super(context);
		initialize();
	}

	public ActionBarSearchInput(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	private void initialize() {
		historyNames = null;
		setSearchButtonListener(this);
		setOnItemClickListener(this);
		addTextChangedListener(this);
		setOnScrollListener(this);
		setOnEditorActionListener(this);
		if(isStartSearch()){
			assistList.show();
			
			showInputSoftKeyBoard();
			
			UISearchControlJNI.Instance().StartSearch(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD,UISearchControlJNI.SRCH_NET_TYPE_OFFLINE);
			
		} else {
			
			searchKeyword = BundleNavi.getInstance().getString(KEY_KEY_WORD);
			Log.d("SRCH", "BundleNavi KEY_KEY_WORD searchKeyword = " + searchKeyword);
			BundleNavi.getInstance().putString(KEY_KEY_WORD, searchKeyword);
			inputItem.setEditText(searchKeyword);
			
			getHistroyList();
			assistList.hide();
		}
		
		cprogressBarHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(progressDialog != null) {
					progressDialog.dismiss();
					CustomToast.showToast(getContext(), R.string.STR_MM_02_03_01_07, 500);
				}
			}
		};
	}
	
	private boolean isStartSearch() {
		return BundleNavi.getInstance().getCurrentActivityClass().equals(ADT_Srch_Map.class)
				&& (BundleNavi.getInstance().getPreviousActivityClass().equals(ADT_Main_Map_Navigation.class)
				|| BundleNavi.getInstance().getPreviousActivityClass().equals(ADT_Route_Main.class)
				|| BundleNavi.getInstance().getPreviousActivityClass().equals(ADT_Route_PointMap.class));
	}
	
	private void getHistroyList() {
		UISearchControlJNI.Instance().UpdateResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
		UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
		updateHistoryToList(searchResult);
	}
	
	private void showInputSoftKeyBoard(){
		new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1) {
					inputItem.showInputSoftKeyBoard();
				}
			}
		}.sendEmptyMessage(1);
	}
	
	
	private String[] getFilterList(String[] names, String s){
		if(names == null){
			return new String[]{};
		}
		ArrayList<String> list = new ArrayList<String>();
		for(String str: names){
			if(str.startsWith(s))list.add(str);
		}
		String[] array = list.toArray(new String[]{});
		Arrays.sort(array);
		return array;
	}
	
	private String[] jointArrays(String[] str1, String[] str2){
		String[] strings = new String[str1.length + str2.length];
		
		System.arraycopy(str1, 0, strings, 0, str1.length);
		System.arraycopy(str2, 0, strings, str1.length, str2.length);
		
		return strings;
	}
	
	private void updateResultToListWhenInput(CharSequence s){
		setItems(getFilterList(historyNames, s.toString()));
	}

	
	@Override
	public boolean notifyTriggerReceived(NSTriggerInfo triggerInfo) {
		if(triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED){
			return false;
		}
		
		dismissProgressDialog();
		
		
		
//		boolean bRet = UISearchControlJNI.Instance().UpdateResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
//		if(!bRet){
//			return true;
//		}
//		if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HELP) {
//			if(triggerInfo.m_lParam2 != 0) {
//				if(inputHelpNetType == UISearchControlJNI.SRCH_NET_TYPE_OFFLINE){
//					inputHelpNetType = UISearchControlJNI.SRCH_NET_TYPE_ONLINE;
//					UISearchControlJNI.Instance().OnInput(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD, 
//							inputKeyword, SearchTools.getCenterLonlat(),UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
//				}else{
//					inputHelpNetType = UISearchControlJNI.SRCH_NET_TYPE_NONE;
//					CustomToast.showToast(getContext(), "error", 1000);
//				}
//				return true;
//			}
//			inputHelpNetType = UISearchControlJNI.SRCH_NET_TYPE_NONE;
//			updateResultToList(UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD));
//		}
		
		
		if(triggerInfo.m_lParam1 != UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HELP && triggerInfo.m_lParam2 != 0) {
//			jump2Map();
			if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI &&
					triggerInfo.m_lParam2 == UISearchControlJNI.UIC_SCM_ERROR_TYPE_DATA){
				onlineSearch();
				return true;
			}
			Log.d("UIMsgControl", "continue OnPressBtn : error type = " + SearchTools.getErrorType((int)triggerInfo.m_lParam2));
			CustomToast.showToast(getContext(), R.string.STR_MM_02_03_01_07, 1000);
			return true;
		}
		
		UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
		
		if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) {
			BundleNavi.getInstance().put(KEY_KEY_WORD, inputItem.getText());
			assistList.hide();
			inputItem.closeInputKeyBoard();
			if(searchListener != null) {
				searchListener.onSearchFinished();
			}
		} else if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HISTORY){
			updateHistoryToList(searchResult);
		} else {
			
		}
		
		return true;
	}
	
	private void updateResultToList(UISearchResultJNI searchResult) {
		
			int count = (int) searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HELP);
			if(count > INPUT_MAX){
				count = INPUT_MAX;
			}
			
			String[] hisNames = getFilterList(historyNames, inputItem.getText());
			String[] fetchNames = new String[count];
			for(int i=0; i<count; i++){
				fetchNames[i] =  searchResult.GetListItemNameAt(i, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HELP);
			}
			
			assistList.setItems(jointArrays(hisNames, fetchNames));
	}
	
	private void updateHistoryToList(UISearchResultJNI searchResult) {
		
		int count = (int) searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HISTORY);
		historyNames = new String[count];
		for(int i=0; i<count; i++){
			historyNames[i] =  searchResult.GetListItemNameAt(i, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HISTORY);
		}
		assistList.setItems(historyNames);
		
	}

	
	@Override
	public void onSearchButtonClicked(View view, String inputText) {
		searchKeyword = inputText;
		searchInputString(inputText,UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		assistList.show();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		try {
			String tempString = new String(s.toString().trim().getBytes("GBK"), "ISO8859_1");
			if(tempString.length() > INPUT_MAX_COUNT){
				String realInputString = s.toString().trim();
				realInputString = (String) realInputString.subSequence(0 , realInputString.length() - 1);
				inputItem.setEditText(realInputString);
				inputItem.setEidtSelection(realInputString.length());
				CustomToast.showToast(getContext(), R.string.MSG_02_02_01_04, Toast.LENGTH_LONG);
				return;
			}
			CustomToast.cancelToast();
		} catch (UnsupportedEncodingException e) {
			Log.d("UIMsgControl", "input max size : UnsupportedEncodingException!");
			e.printStackTrace();
		} 
		
		updateResultToListWhenInput(s);
		
		if(TextUtils.isEmpty(s)){
			return;
		}
		
		if(isTextChangeFromItemClicked) {
			isTextChangeFromItemClicked = false;
			return;
		}
		
		if(SystemTools.isJP()) {
			return;
		}
//		inputKeyword = s.toString();
//		UISearchControlJNI.Instance().OnInput(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD, 
//				inputKeyword, SearchTools.getCenterLonlat(),UISearchControlJNI.SRCH_NET_TYPE_OFFLINE);
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String name = assistList.getString(position);
		Log.i("icon", "name-"+name);
		Log.d("SRCH", "triggerSearch name = " + name);
		triggerSearch(name);
		
	}
	
	public final void triggerSearch(String name) {
		isTextChangeFromItemClicked = true;
		setEditText(name);
		searchKeyword = name;
		searchInputString(name,UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
	}
	
	public final void onlineSearch(){
		searchInputString(searchKeyword,UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
	}
	
	public final int getNetType(){
		return inputHelpNetType;
	}
	
	private void searchInputString(String s,int nettype){
		UIC_SCM_POIReqParam param = new UIC_SCM_POIReqParam();
		param.type = UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD;
		param.btn_id = UISearchControlJNI.UIC_SCM_BTN_ID_SEARCH;
		param.act_id = UISearchControlJNI.UIC_SCM_ACT_ID_NORMAL;
		if(UIMapControlJNI.GetCarPositonMode()){
			param.list_id = UISearchControlJNI.UIC_SCM_LIST_ID_CATEGORY_NEAR_CAR;
		}else{
			param.list_id = UISearchControlJNI.UIC_SCM_LIST_ID_CATEGORY_NEAR_CURSOR;
		}
		param.nettype = nettype;
		inputHelpNetType = nettype;
		param.keyword =s.trim();
		/*long needWait =*/ UISearchControlJNI.Instance().OnPressBtn(param);
//		if(needWait == jniSearchControl.UIC_SCM_SCREEN_WAIT){
			showProgressDialog();
//		}
			
//		PointTools.getInstance().requestCenterName((PointNameListener)null);
	}
	
	private CProgressDialog progressDialog;
	
	private CProgressDialog getProgressDailog() {
		CProgressDialog progressDialog = new CProgressDialog(getContext());
		progressDialog.setText(R.string.STR_MM_02_02_01_01);
		progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction()==KeyEvent.ACTION_UP)){
//					jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE2);
					UISearchControlJNI.Instance().OnCancel(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
				}
				return false;
			}});
		progressDialog.setOnDismissListener(new OnDismissListener(){

			@Override
			public void onDismiss(DialogInterface dialog) {
			}
			
		});
		
		return progressDialog;
	}
	
	private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = getProgressDailog();
		}
		
		progressDialog.show();
		cprogressBarHandler.sendEmptyMessageDelayed(0,10000);
	}
	
	private void dismissProgressDialog() {
		if(progressDialog != null) {
			progressDialog.dismiss();
			cprogressBarHandler.removeMessages(0);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_TOUCH_SCROLL) {
			inputItem.closeInputKeyBoard();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	
	public void setOnSearchFinishedListener(OnSearchFinishedListener l){
		this.searchListener = l;
	}

	public interface OnSearchFinishedListener{
		void onSearchFinished();
	}
    
	
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		Log.d("SRCH", "onEditorAction actionId = " + actionId);
		if(actionId == EditorInfo.IME_ACTION_SEARCH) {
			if(!TextUtils.isEmpty(inputItem.getText().toString())){
				searchKeyword = inputItem.getText().toString();
				searchInputString(searchKeyword,UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
				} 
		}
		return false;
	}
	
	public void hideSoftInputFromWindow() {
		inputItem.closeInputKeyBoard();
	}
}
