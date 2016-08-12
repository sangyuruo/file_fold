package com.billionav.navi.naviscreen.misc;

import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIVoiceControlJNI;
import com.billionav.navi.component.listcomponent.ListItemNewsResult;
import com.billionav.navi.component.listcomponent.ListItemVrSearchResult;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSAnimationID;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.dest.ADT_Top_Menu;
import com.billionav.navi.naviscreen.map.ADT_AR_Main;
import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
import com.billionav.navi.system.AplRuntime;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.ui.R;
import com.billionav.voicerecog.ContactInfo;
import com.billionav.voicerecog.NewsInfo;
import com.billionav.voicerecog.POIInfo;
import com.billionav.voicerecog.VoiceRecognizer;
import com.billionav.voicerecog.VrListener;

//The tag Map screen is just for zoomin/zoomout
public class ADT_Voice_Recognition extends ActivityBase implements MenuControlIF.MapScreen{
//	private LinearLayout recordLayout;
	private VoiceHistoryControl voiceControl;
	
	private VoiceHelperView microphone;
	private ScrollView scrollView;
	private LinearLayout recordLayout;
//	private ViewGroup resultListLayout;
//	private ListView listView;
	private TextView commandView;
	
	public static String VR = "VoiceRecog";
	
	private final Handler handler = new Handler(Looper.getMainLooper());
	
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setNoTitle();
		setContentView(R.layout.adt_voice_recognition);
		
		findViews();
		
		initialize();
		
		setListeners();
//		VoiceRecognizer.instance().enterVrMode();

	}
	private void initialize() {
		 
		 setBackground();
		 
	}

	private void setListeners() {
		microphone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				VoiceRecognizer.instance().enterVrMode();
				UIVoiceControlJNI.getInstance().startVR();
			}
		});
		
	}

	
	private void findViews() {
		recordLayout = (LinearLayout) findViewById(R.id.record_Layout);
		voiceControl = new VoiceHistoryControl(recordLayout);
		
		microphone = (VoiceHelperView) findViewById(R.id.microphone);
		
		scrollView = (ScrollView) findViewById(R.id.record_scrollview);
		
//		resultListLayout = (ViewGroup) findViewById(R.id.search_result_layout);
//		listView = (ListView) findViewById(R.id.search_result_list);
		commandView = (TextView) findViewById(R.id.voice_command_text);
	}
	
	private void startVioce(){
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				microphone.switchState(VoiceHelperView.VOICE_HELPER_STATE_IDLE);
			}
		}.sendEmptyMessage(1);
	}
	
	private void stopVioceAndStartRecognize() {
		microphone.switchState(VoiceHelperView.VOICE_HELPER_STATE_RECOGNIZING);
	}
	
	private void stopSpeedRecognize(){
		microphone.switchState(VoiceHelperView.VOICE_HELPER_STATE_IDLE);
	}
	
	
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			VoiceRecognizer.instance().exitVrMode();
			UIVoiceControlJNI.getInstance().stopVoiceRecog();
			
			//	DRIRAppMain.WinChange2Map();
			if(getBundleNavi().getPreviousActivityClass().equals(ADT_AR_Main.class)){
				DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_AR_MODE);
				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_OUT_DELAYED);
				ForwardKeepDepthWinChange(ADT_AR_Main.class);
				return true;
			}
			if(getBundleNavi().getPreviousActivityClass().equals(ADT_Top_Menu.class)){
				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_SLIDE_IN_BOTTOM_DELAYED);
			}
			
		}
		return super.OnKeyDown(keyCode, event);
	}

	@Override
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo) {
		switch (cTriggerInfo.m_iTriggerID) {
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_VOICE_SHOW_CONTACT_LIST:
			break;
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_VOICE_SHOW_NEWS:
			setNewsResult(UIVoiceControlJNI.getInstance().getNewsList(), (int)cTriggerInfo.GetlParam2());
			break;
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_VOICE_SHOW_VOICE_HELP:
			break;
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_POI_LIST_SHOW:
			setSearchResult(UIVoiceControlJNI.getInstance().getPoiList(), (int)cTriggerInfo.GetlParam2());
			//where are these search results?
			break;
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_TEXT_SHOW:
			onTriggerForSNS(cTriggerInfo);
			break;
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_STATE_CHANGED:
			//0 : voice switch to STATE_IDLE, screen return
//			if((int) cTriggerInfo.GetlParam1() == 3) {
//				BackWinChange();
//				return true;
//			}
			microphone.switchState((int) cTriggerInfo.GetlParam1());
			break;
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_PIC_SHOW:
			setImageRecord(cTriggerInfo.m_String1);
			break;
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_EXIT:
			//do nothing
			
//			if(getBundleNavi().getPreviousActivityClass().equals(ADT_AR_Main.class)){
//				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_OUT_DELAYED);
//				DRIRAppMain.DRIRFunChange(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE, DRIRAppMain.DRIRAPP_AR_MODE);
//				ForwardKeepDepthWinChange(ADT_AR_Main.class);
//			}else if(getBundleNavi().getPreviousActivityClass().equals(ADT_Top_Menu.class)){
//				MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_SLIDE_IN_BOTTOM_DELAYED);
//				BackWinChange();
//			}else{
//				BackWinChange();
//			}
			break;
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_HIDEPOILIST:
			
			//To Be Removed
//			showRecord();
			break;
		case NSTriggerID.UIC_MN_TRG_VOICE_RECOG_NAVI_TO_POI_LIST:
			POIInfo poiInfo = UIVoiceControlJNI.getInstance().getNaviToPoiList().get(0);
			RouteCalcController.instance().rapidRouteCalculateWithVioce(poiInfo);
			break;
		default:
			break;
		}
		return super.OnTrigger(cTriggerInfo);
	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
//		AplRuntime.Instance().setVrListener(vrListener);
		UIVoiceControlJNI.getInstance().startVR();
	}
	
	
	
	@Override
	protected void OnPause() {
		// TODO Auto-generated method stub
		super.OnPause();
		UIVoiceControlJNI.getInstance().stopVoiceRecog();
	}
	@Override
	protected void OnDestroy() {
		// TODO Auto-generated method stub
		super.OnDestroy();
//		VoiceRecognizer.instance().exitVrMode();
		AplRuntime.Instance().setVrListener(null);
	}
	
	private void onTriggerForSNS(NSTriggerInfo triggerInfo){
		switch ((int)triggerInfo.m_lParam1) {
//		case VoiceRecognizer.NOTIFY_RECOG_START:
//			Log.d(VR, "trigger: UIC_MN_TRG_VOICE_RECOG_TEXT_SHOW param : NOTIFY_RECOG_START");
//			startVioce();
//			break;
//		case VoiceRecognizer.NOTIFY_RECOG_SPEECHDONE:
//			Log.d(VR, "trigger: UIC_MN_TRG_VOICE_RECOG_TEXT_SHOW param : NOTIFY_RECOG_SPEECHDONE");
//			stopVioceAndStartRecognize();
//			break;
		case VoiceRecognizer.NOTIFY_VR_PLAY:
			stopSpeedRecognize();
			if(!TextUtils.isEmpty(triggerInfo.m_String1)) {
				setUserSpeak(triggerInfo.m_String1);
			}
			break;
		case VoiceRecognizer.NOTIFY_COMMAND_PROMPT_HINT:
			commandView.setText(triggerInfo.m_String1);
			break;
		case VoiceRecognizer.NOTIFY_PROMPT_PLAY:
			if(!TextUtils.isEmpty(triggerInfo.m_String1)) {
				setRobertSpeak(triggerInfo.m_String1);
			}
			break;
//		case VoiceRecognizer.NOTIFY_PICTURE_READY:
//			Log.d(VR, "trigger: UIC_MN_TRG_VOICE_RECOG_TEXT_SHOW param : NOTIFY_PICTURE_READY ; string: " + triggerInfo.m_String1);
//			setImageRecord(triggerInfo.m_String1);
//			break;
//		case VoiceRecognizer.NOTIFY_COMMAND_PROMPT_HINT:
//			Log.d(VR, "trigger: UIC_MN_TRG_VOICE_RECOG_TEXT_SHOW param : NOTIFY_COMMAND_PROMPT_HINT ; string: " + triggerInfo.m_String1);
//			setCommandStr(triggerInfo.m_String1);
//			break;
		default:
			break;
		}

	}
	
	private void setCommandStr(String m_String1) {
		
		commandView.setText(m_String1);
	}
	private void setRobertSpeak(String str) {
		voiceControl.addRobertRecord(str);
		scrollToBottom(scrollView, recordLayout);
	}
	
	private void setSearchResult(List<POIInfo> srchResult, int tag) {
		showSearchResultList(srchResult, tag);
		scrollToBottom(scrollView, recordLayout);
	}
	
	private void setNewsResult(List<NewsInfo> newsResult, int tag) {
		showNewsResultList(newsResult, tag);
		scrollToBottom(scrollView, recordLayout);
	}
	
	private void setUserSpeak(String str) {
		voiceControl.addUserRecord(str);
		scrollToBottom(scrollView, recordLayout);
	}
	
	private void setImageRecord(String pathName) {
		voiceControl.addPictureRecord(pathName);
		scrollToBottom(scrollView, recordLayout);
	}
	
	private static void scrollToBottom(final View scroll, final View inner) {

		Handler mHandler = new Handler();

		mHandler.post(new Runnable() {
			public void run() {
				if (scroll == null || inner == null) {
					return;
				}

				int offset = inner.getMeasuredHeight() - scroll.getHeight();
				if (offset < 0) {
					offset = 0;
				}

				scroll.scrollTo(0, offset);
			}
		});
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setBackground();
	}
	
	private void setBackground() {
		boolean isPortarit = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
		findViewById(R.id.root_Layout).setBackgroundResource(isPortarit ? R.drawable.navicloud_and_517a : R.drawable.navicloud_and_517b);
	}
	
	private void showNewsResultList(final List<NewsInfo> list, final int requestID) {
		if(list == null){
			return ;
		}
		
		ListView lv = getCertainListView(list, requestID, new onGetViewListener() {
			
			@Override
			public View onGetView(int position, View convertView, ViewGroup parent) {
				if(null == convertView){
					convertView = new ListItemNewsResult(ADT_Voice_Recognition.this, list.get(position).getTitle(), list.get(position).getDescription());
				}
				((TextView)convertView.findViewById(R.id.list_Item_News_Title)).setText(list.get(position).getTitle());
				((TextView)convertView.findViewById(R.id.list_Item_News_Detail)).setText(list.get(position).getDescription());
				return convertView;
			}
		});
//		lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				UIVoiceControlJNI.getInstance().onItemSelected((Integer)(lv.getTag()), position);
//			}
//		});
		recordLayout.addView(lv);
		scrollToBottom(scrollView, recordLayout);
	}
	
	private void showSearchResultList(final List<POIInfo> list, final int requestID) {
		if(list == null){
			return ;
		}
		final ListView lv = getCertainListView(list, requestID, new onGetViewListener() {
			
			public View onGetView(int position, View convertView, ViewGroup parent) {
				int dis = 0;
				try {
					dis = Integer.parseInt(list.get(position).getDistance());
				} catch (Exception e) {
					dis = 0;
				}
				if(null == convertView){
					convertView =  new ListItemVrSearchResult(ADT_Voice_Recognition.this, 
							list.get(position).getName(), list.get(position).getAddr(), 
							dis);
				}
				((TextView)convertView.findViewById(R.id.list_Item_Guide_Poi_text01)).setText(list.get(position).getName());
				((TextView)convertView.findViewById(R.id.list_Item_Guide_Poi_text02)).setText(list.get(position).getAddr());
				((TextView)convertView.findViewById(R.id.list_Item_Guide_Poi_text03)).setText(RouteTool.getDisplayDistance(dis));
				return convertView;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UIVoiceControlJNI.getInstance().onItemSelected((Integer)(lv.getTag()), position);
			}
		});
		recordLayout.addView(lv);
		lv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getListViewHeight(lv)));
	}

	private int getListViewHeight(ListView lv) {
		ListAdapter adapter = lv.getAdapter();
		int height = 0;
		for(int i=0; i < adapter.getCount(); ++i) {
			View listItem = adapter.getView(i, null, lv);
			listItem.measure(0, 0);
			height += listItem.getMeasuredHeight();
		}
		height += (lv.getDividerHeight() * (lv.getCount() - 1));
		return height;
	}
	
	private <T> ListView getCertainListView(final List<T> list, final int requestID, final onGetViewListener l){
		if(null == list) {
			return null;
		}
		BaseAdapter adapter = new MyBaseAdapter<T>(list, l);
		final ListView lv = new MyListView(this); 
		lv.setCacheColorHint(0);
		lv.setBackgroundColor(0);
//		lv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, list.size()*itemHeight));
		lv.setAdapter(adapter);
//		lv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, getListViewHeight(lv)));
		
		lv.setTag(requestID);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UIVoiceControlJNI.getInstance().onItemSelected((Integer)(lv.getTag()), position);
			}
		});
		return lv;
	}
	
//	private void showResult(){
//		slideIn.reset();
//		resultListLayout.setAnimation(slideIn);
//		slideIn.start();
//		
//		slideOut.reset();
//		recordLayout.setAnimation(slideOut);
//		slideOut.start();
//		resultListLayout.setVisibility(View.VISIBLE);
//		recordLayout.setVisibility(View.GONE);
//	}
//	private void showRecord(){
//		slideOut2.reset();
//		resultListLayout.setAnimation(slideOut2);
//		slideOut2.start();
//		
//		slideIn2.reset();
//		recordLayout.setAnimation(slideIn2);
//		slideIn2.start();
//		resultListLayout.setVisibility(View.GONE);
//		recordLayout.setVisibility(View.VISIBLE);
//	}
	private class MyListView extends ListView {
		public MyListView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public MyListView(Context context, AttributeSet as) {
			super(context, as);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int expandSpec = MeasureSpec.makeMeasureSpec(
					Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);
		}
	}
	private class MyBaseAdapter<T> extends BaseAdapter{

		private List<T> dataList;
		private onGetViewListener l;
		public MyBaseAdapter(List<T> list, onGetViewListener l)
		{
			dataList = list;
			this.l = l;
		}
		@Override
		public int getCount() {
			if(null == dataList) {
				return 0;
			}
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			if(null == dataList) {
				return null;
			}
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(l != null) {
				return l.onGetView(position, convertView, parent);
			}
			return null;
		}
		
	};
	private interface onGetViewListener{
		public View onGetView(int position, View convertView, ViewGroup parent);
	}
}


class VoiceHistoryControl {
	private static final int MAX_RECORD = 100;
	private final LinearLayout rootLayout;

	public VoiceHistoryControl(LinearLayout rootLayout) {
		this.rootLayout = rootLayout;
	}
	
	public void addUserRecord(String str) {
		rootLayout.addView(new UserRecord(rootLayout.getContext(), str));
		removeOverflowRecord();
	}
	
	public void addRobertRecord(String str) {
		rootLayout.addView(new RobertRecord(rootLayout.getContext(), str));
		removeOverflowRecord();
	}
	
	public void addPictureRecord(String pathName) {
		rootLayout.addView(new PictureRecord(rootLayout.getContext(), pathName));
		removeOverflowRecord();
	}

	private void removeOverflowRecord() {
		if(rootLayout.getChildCount() > MAX_RECORD) {
			rootLayout.removeViewAt(0);
		}
	}
	
	private static class UserRecord extends RelativeLayout {
		
		public UserRecord(Context context, String str) {
			super(context);
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    inflater.inflate(R.layout.adt_voice_recognition_user_text, this);
		    TextView text = (TextView) findViewById(R.id.user_text);
		    text.setText(str);
		}
		
	}

	private static class RobertRecord extends RelativeLayout {
		
		public RobertRecord(Context context, String str) {
			super(context);
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    inflater.inflate(R.layout.adt_voice_recognition_robert_text, this);
		    TextView text = (TextView) findViewById(R.id.robert_text);
		    text.setText(str);
		}
		
	}
	
	private static class PictureRecord extends RelativeLayout {

		public PictureRecord(Context context, String pathName) {
			super(context);
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    inflater.inflate(R.layout.adt_voice_recognition_image, this);
		    ImageView iv = (ImageView) findViewById(R.id.take_photo);
		    Bitmap bm = BitmapFactory.decodeFile(pathName);
		    iv.setImageBitmap(bm);
		}
		
	}

}


