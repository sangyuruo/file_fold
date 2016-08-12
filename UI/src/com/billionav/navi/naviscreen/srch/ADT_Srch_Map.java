package com.billionav.navi.naviscreen.srch;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchControlJNI.UIC_SCM_POIReqParam;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.component.actionbar.ActionBarSearchInput;
import com.billionav.navi.component.dialog.CProgressDialog;
import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.component.dialog.CustomToast;
import com.billionav.navi.component.listcomponent.ListItemMenu;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.favorite.ADT_Favorite;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.naviscreen.route.ADT_Route_PointMap;
import com.billionav.navi.naviscreen.setting.ADT_Schedule_List;
import com.billionav.navi.naviscreen.setting.ADT_Schedule_PointMap;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.SearchTools;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;

/**
 * OnEditorActionListener for click key enter
 * TextWatcher for input text for ExitText
 * */
public class ADT_Srch_Map extends ActivityBase implements OnEditorActionListener, TextWatcher, OnScrollListener, OnItemClickListener, OnClickListener{
	
	private static final String KEY_KEY_WORD = "keyword";
	private static final int INPUT_MAX = ActionBarSearchInput.INPUT_MAX;
	private static final int INPUT_MAX_COUNT = ActionBarSearchInput.INPUT_MAX_COUNT;
	
	private EditText input;
	private ImageView searchButton;
	private ListView listView;
	private Button areaButton;
	
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
	private BaseAdapter adapter;
	private String[] historyNames;
	private boolean isTextChangeFromItemClicked = false;
	private CustomDialog noneSrchresultDialog = null;
	private CustomDialog inputMaxSizeDialogMsg = null;
	private Handler handler = null;
	private Handler cprogressBarHandler = null;
	private int inputHelpNetType = UISearchControlJNI.SRCH_NET_TYPE_OFFLINE;
	private String realInputString;
//	private String inputKeyword = "";
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_srch_input);
		setNoTitle();
		setDefaultBackground();
		adapter = new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				String name = (String) list.get(position).get("name");
				int imageid = (Integer) list.get(position).get("image");
				if(convertView == null){
					convertView = new ListItemMenu(ADT_Srch_Map.this, imageid, name);
				} else {
					((ListItemMenu)convertView).setName(name);
					((ListItemMenu)convertView).setImage(imageid);
				}
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				return list.size();
			}
		};
		if(!getBundleNavi().getPreviousActivityClass().equals(ADT_Schedule_PointMap.class)){
			//forward Favorite activity.
			addActionItem2(R.drawable.navicloud_and_454a, R.string.STR_MM_01_02_01_02, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					closeInputKeyBoard();
					if(MenuControlIF.Instance().SearchWinscape(ADT_Favorite.class) && !MenuControlIF.Instance().SearchWinscape(ADT_Route_PointMap.class)){
						MenuControlIF.Instance().BackSearchWinChange(ADT_Favorite.class);
					}
					else{
						MenuControlIF.Instance().ForwardWinChange(ADT_Favorite.class);
					}
					
				}
			});
			//forward POI activity.
			addActionItem2(R.drawable.navicloud_and_453a, R.string.STR_MM_02_03_01_01, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					closeInputKeyBoard();
					MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_POI_Main.class);
					
				}
			});
		}
		findViews();
		listView.setAdapter(adapter);
		setListeners();
		showInputSoftKeyBoardDelay();
		
		if(isStartSearch()){
			
			showInputSoftKeyBoard();
			
			UISearchControlJNI.Instance().StartSearch(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD,UISearchControlJNI.SRCH_NET_TYPE_OFFLINE);
			UISearchControlJNI.Instance().reqDefaultAreaInfo();
		} else {
			
			String searchKeyword = BundleNavi.getInstance().getString(KEY_KEY_WORD);
			BundleNavi.getInstance().putString(KEY_KEY_WORD, searchKeyword);
			input.setText(searchKeyword);
			
			getHistroyList();
		}
		
		cprogressBarHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(cprogressBarDialog != null) {
					cprogressBarDialog.dismiss();
					CustomToast.showToast(ADT_Srch_Map.this, R.string.STR_MM_02_03_01_07, 500);
				}
			}
		};

	}
	
//	@Override
//	protected void OnResume() {
//		super.OnResume();
//		String areaName = getBundleNavi().getString("AreaName");
//		if(!TextUtils.isEmpty(areaName)) {
//			Log.d("UIMsgControl", "OnResume areaName = " + areaName);
//			areaButton.setText(areaName);
//			areaButton.setVisibility(View.VISIBLE);
//		}
//	}
	
	private static final int MESSAGE_ID_01 = 0x1110;
	private void showNoneResultDialog(int netType){
		if(noneSrchresultDialog == null){
			noneSrchresultDialog = new CustomDialog(this);
			noneSrchresultDialog.setEnterBackKeyAllowClose(true);
		}
		if(handler == null){
			handler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					noneSrchresultDialog.dismiss();
				}
				
			};
		}
		if(handler.hasMessages(MESSAGE_ID_01)){
			handler.removeMessages(MESSAGE_ID_01);
		}
		noneSrchresultDialog.setTitle(R.string.STR_MM_02_02_01_05);
		noneSrchresultDialog.setMessage(R.string.MSG_02_01_01_01);
		Log.d("UIMsgControl", "OnPressBtn : error netType = " + netType);
		if(netType == UISearchControlJNI.SRCH_NET_TYPE_ONLINE){
			noneSrchresultDialog.setPositiveButton(R.string.STR_MM_02_02_01_03, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(!TextUtils.isEmpty(input.getText().toString())){
						searchInputString(input.getText().toString(),UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
					} 
				}
			});
		}else{
			noneSrchresultDialog.setPositiveButton("",null);
		}
		noneSrchresultDialog.setNegativeButton(R.string.STR_MM_02_02_01_04, null);
		noneSrchresultDialog.show();
//		handler.sendEmptyMessageDelayed(MESSAGE_ID_01, 3000);
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			closeInputKeyBoard();
			if(ADT_Route_Main.class == MenuControlIF.Instance().GetHierarchyBelowWinscapeClass()){
				RouteCalcController.instance().setRoutePointFindPurpose(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE);
			}
		}
		return super.OnKeyDown(keyCode, event);
	}
	
	private boolean isStartSearch() {
		return true;
	}

	private void getHistroyList() {
		UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
		updateHistoryToList(searchResult);
	}
	
	private void updateResultToList(UISearchResultJNI searchResult) {
		
		int count = (int) searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HELP);
		if(count > INPUT_MAX){
			count = INPUT_MAX;
		}
		
		String[] hisNames = getFilterList(historyNames, input.getText().toString());
		
		String[] fetchNames = new String[count];
		for(int i=0; i<count; i++){
			fetchNames[i] =  searchResult.GetListItemNameAt(i, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HELP);
		}
		
		setItems(hisNames.length,jointArrays(hisNames, fetchNames));
		
	}
	
	private void updateHistoryToList(UISearchResultJNI searchResult) {
		
		int count = (int) searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HISTORY);
		historyNames = new String[count];
		for(int i=0; i<count; i++){
			historyNames[i] =  searchResult.GetListItemNameAt(i, UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HISTORY);
		}
		setItems(historyNames.length,historyNames);
		
	}
	
	private void findViews() {
		input = (EditText) findViewById(R.id.actionbar_input_edit_text);
		searchButton = (ImageView) findViewById(R.id.actionbar_input_searsh);
		listView = (ListView) findViewById(R.id.list_container);
		areaButton = (Button) findViewById(R.id.area);
		
	}
	
	private void setListeners() {
		input.setOnEditorActionListener(this);
		input.addTextChangedListener(this);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
		searchButton.setOnClickListener(this);
		areaButton.setOnClickListener(this);
	}

	
	
	public void setItems(int historySize, String... names) {
		list.clear();
		if(names == null){
			return;
		}
		
		for(int i=0; i<names.length; i++){
			if(i < historySize){
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("name", names[i]);
				hashMap.put("image", R.drawable.navicloud_and_462a);
				list.add(hashMap);
			}
			else{
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("name", names[i]);
				hashMap.put("image", 0);
				list.add(hashMap);
			}
		} 
		
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
	
		if(triggerInfo.m_iTriggerID == NSTriggerID.SEARCH_RESPONSE_FOR_DEFAULT_AREA) {
			String areaName = getBundleNavi().getString("AreaName");
			if(triggerInfo.m_lParam2 == 0 && TextUtils.isEmpty(areaName)){
				areaButton.setEnabled(false);
			}else{
				areaButton.setEnabled(true);
			}
			String name = UISearchControlJNI.Instance().getDefaultArea();
			Log.d("UIMsgControl", "SEARCH_RESPONSE_FOR_DEFAULT_AREA error = " + triggerInfo.m_lParam1 + " name = " + name);
			areaButton.setText(name);
			areaButton.setVisibility(View.VISIBLE);
			return true;
		}
		
		
		if(triggerInfo.m_iTriggerID != NSTriggerID.UIC_MN_TRG_POINT_SEARCH_FINISHED) {
			return false;
		}
		
		if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI 
				&&(cprogressBarDialog == null || !cprogressBarDialog.isShowing())){
			return false;
		}
		dismissProgressDialog();
		
		
//		if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HELP) {
//			if(triggerInfo.m_lParam2 != 0) {
//				if(inputHelpNetType == UISearchControlJNI.SRCH_NET_TYPE_OFFLINE){
//					inputHelpNetType = UISearchControlJNI.SRCH_NET_TYPE_ONLINE;
//					Log.d("UIMsgControl", "OnInput SRCH_NET_TYPE_ONLINE");
//					UISearchControlJNI.Instance().OnInput(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD, 
//							inputKeyword, SearchTools.getCenterLonlat(),UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
//				}else{
//					inputHelpNetType = UISearchControlJNI.SRCH_NET_TYPE_NONE;
//					CustomToast.showToast(this, "error", 1000);
//				}
//				return true;
//			}
//			inputHelpNetType = UISearchControlJNI.SRCH_NET_TYPE_NONE;
//			updateResultToList(UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD));
//		}
		
		if(triggerInfo.m_lParam2 != 0) {
			if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI){
				if(inputHelpNetType == UISearchControlJNI.SRCH_NET_TYPE_OFFLINE){
					showNoneResultDialog(UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
				}else{
					showNoneResultDialog(UISearchControlJNI.SRCH_NET_TYPE_OFFLINE);
				}
				return true;
			}
			Log.d("UIMsgControl", "OnPressBtn : error type = " + SearchTools.getErrorType((int)triggerInfo.m_lParam2));
			CustomToast.showToast(this, R.string.STR_MM_02_03_01_07, 1000);
			return true;
		}
		
		UISearchResultJNI searchResult = UISearchControlJNI.Instance().GetSearchResult(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
		
		if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) {
			if(searchResult.GetListItemCount(UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_POI) == 0) {
				if(inputHelpNetType == UISearchControlJNI.SRCH_NET_TYPE_OFFLINE){
					showNoneResultDialog(UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
				}else{
					showNoneResultDialog(UISearchControlJNI.SRCH_NET_TYPE_OFFLINE);
				}
			} else {
				BundleNavi.getInstance().put(KEY_KEY_WORD, input.getText().toString());
				closeInputKeyBoard();
				onSearchFinished();
			}
		} else if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_AREA){
			ForwardWinChange(ADT_Srch_Area_Select.class);
		} else if(triggerInfo.m_lParam1 == UISearchControlJNI.SearchResult_UIC_SCM_LIST_ID_INPUT_HISTORY){
			updateHistoryToList(searchResult);
		} else {
			
		}
		return true;
	}
	
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
	}

	private void onSearchFinished() {
		if(getBundleNavi().getPreviousActivityClass().equals(ADT_Schedule_PointMap.class)){
			ForwardKeepDepthWinChange(ADT_Schedule_List.class);
		}else{
			BundleNavi.getInstance().putInt("inputHelpNetType", inputHelpNetType);
			ForwardKeepDepthWinChange(ADT_Srch_List.class);
		}
	}

	private void jump2Map() {
		MenuControlIF.Instance().BackWinChange();
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
//		if(isNetEnable()){
		/*long needWait =*/ UISearchControlJNI.Instance().OnPressBtn(param);
//		SearchRequestType.getInstace().saveSearchRequestInfo(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD, SearchRequestType.FREEWORD_PRESS_BUTTON);
//		if(needWait == jniSearchControl.UIC_SCM_SCREEN_WAIT){
		showProgressDialog();
//		}
			
//		PointTools.getInstance().requestCenterName(null);
//		}
	}

	private CProgressDialog cprogressBarDialog = null;
	
	private CProgressDialog getProgressDailog() {
		CProgressDialog cprogressDialog = new CProgressDialog(this);
		cprogressDialog.setText(R.string.STR_MM_02_02_01_01);
		cprogressDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction()==KeyEvent.ACTION_UP)){
//					jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE2);
					UISearchControlJNI.Instance().OnCancel(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD);
				}
				return false;
			}});
		cprogressDialog.setOnDismissListener(new OnDismissListener(){

			@Override
			public void onDismiss(DialogInterface dialog) {
			}
			
		});
		
		return cprogressDialog;
	}
	
	private void showProgressDialog(){
		if(cprogressBarDialog == null){
			cprogressBarDialog = getProgressDailog();
		}
		
		cprogressBarDialog.show();
		cprogressBarHandler.sendEmptyMessageDelayed(0,15000);
	}
	
	private void dismissProgressDialog() {
		if(cprogressBarDialog != null) {
			cprogressBarDialog.dismiss();
			cprogressBarHandler.removeMessages(0);
		}
	}

	private void showInputSoftKeyBoardDelay(){
		new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1) {
					showInputSoftKeyBoard();
				}
			}
		}.sendEmptyMessage(1);
	}

	private final void showInputSoftKeyBoard() {
	    //show soft key board
		input.setFocusable(true);
		input.requestFocus();
	    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(input, 0);
	}
	
	private final void closeInputKeyBoard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
	}
	
	private String getItemString(int index){
		return (String)list.get(index).get("name");
	}
	
	private String[] getFilterList(String[] names, String s){
		if(names == null){
			return new String[]{};
		}
		ArrayList<String> list = new ArrayList<String>();
		for(String str: names){
			if(str.startsWith(s)) {list.add(str);
            }
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
	
	
	public final void triggerSearch(String name) {
		isTextChangeFromItemClicked = true;
		input.setText(name);
		searchInputString(name,UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
	}
	
	private void updateResultToListWhenInput(CharSequence s){
		setItems(getFilterList(historyNames, s.toString()).length,getFilterList(historyNames, s.toString()));
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if(actionId == EditorInfo.IME_ACTION_SEARCH) {
			if(!TextUtils.isEmpty(input.getText().toString())){
				searchInputString(input.getText().toString(),UISearchControlJNI.SRCH_NET_TYPE_ONLINE);		
			} 
		}
		return false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(SystemTools.isJP()) {
			return;
		}
		try {
			String tempString = new String(s.toString().trim().getBytes("GBK"), "ISO8859_1");
			if(tempString.length() > INPUT_MAX_COUNT){
				realInputString = s.toString().trim();
				realInputString = (String) realInputString.subSequence(0 , realInputString.length() - 1);
				input.setText(realInputString);
				input.setSelection(realInputString.length());
				CustomToast.showToast(this, R.string.MSG_02_02_01_04, Toast.LENGTH_LONG);
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
		
//		inputHelpNetType = UISearchControlJNI.SRCH_NET_TYPE_OFFLINE;
//		inputKeyword = s.toString();
//		Log.d("UIMsgControl", "----OnInput SRCH_NET_TYPE_OFFLINE");
//		UISearchControlJNI.Instance().OnInput(UISearchControlJNI.UIC_SCM_SRCH_TYPE_FREEWORD, 
//				inputKeyword, SearchTools.getCenterLonlat(),UISearchControlJNI.SRCH_NET_TYPE_OFFLINE);
	}
	
	private void showInputMaxSizeMsg(){
		if(inputMaxSizeDialogMsg == null){
			inputMaxSizeDialogMsg = new CustomDialog(this);
		}
		inputMaxSizeDialogMsg.setTitle(R.string.MSG_02_02_01_05);
		inputMaxSizeDialogMsg.setMessage(R.string.MSG_02_02_01_04);
		inputMaxSizeDialogMsg.setPositiveButton(R.string.STR_MM_02_04_07_01, null);
		inputMaxSizeDialogMsg.show();
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		if(TextUtils.isEmpty(s)){
			searchButton.setEnabled(false);
		} else {
			searchButton.setEnabled(true);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_TOUCH_SCROLL) {
			closeInputKeyBoard();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String name = getItemString(position);
		triggerSearch(name);

	}

	@Override
	public void onClick(View v) {
		if(v == searchButton) {
			if(!TextUtils.isEmpty(input.getText().toString())){
				searchInputString(input.getText().toString(),UISearchControlJNI.SRCH_NET_TYPE_ONLINE);
			} 
		} else if(v == areaButton) {
			UISearchControlJNI.Instance().reqAreaList(""); 
		}
		
	}
	
}
