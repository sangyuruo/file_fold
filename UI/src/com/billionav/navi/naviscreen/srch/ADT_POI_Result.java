package com.billionav.navi.naviscreen.srch;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.billionav.jni.UISearchControlJNI;
import com.billionav.jni.UISearchResultJNI;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.component.mapcomponent.PopupPOI;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.menucontrol.MenuControlIF.MapScreen;
import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.naviscreen.base.OnScreenBackListener;
import com.billionav.navi.naviscreen.map.POI_Mark_Control;
import com.billionav.navi.naviscreen.map.TouchMapControl;
import com.billionav.navi.naviscreen.map.TouchMapControl.PointBean;
import com.billionav.navi.system.PLog;
import com.billionav.navi.uitools.MapTools;
import com.billionav.navi.uitools.SearchPointBean;
import com.billionav.ui.R;

public class ADT_POI_Result extends ActivityBase implements OnScreenBackListener, MapScreen{
	private RelativeLayout showList;
	private int index;
	private PopupPOI popup;
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_poi_result);
		setTitleBackgroundBlack();
		findViews();
		setListener();
		initActionbar();
//		initializesShowlistbtnWidth();
//		
	}
	
//	private void initializesShowlistbtnWidth() {
//		int width = ScreenMeasure.isPortrait() ? ScreenMeasure.getWidth() : ScreenMeasure.getHeight();
//		showListbackground.getLayoutParams().width = width;
//	}
	
	@Override
	protected void OnResume() {
		super.OnResume();
		
		POI_Mark_Control.forPOISearchResultView();
		
		if(getBundleNavi().getBoolean("isCheakMap")) {
			if(getBundleNavi().getInt("searchType") == TouchMapControl.TYPE_LONGPRESS){
				PointBean bean = (PointBean) getBundleNavi().get("PointBean");
				showPopupInCenter(bean);
			}
			else{
				SearchPointBean bean = (SearchPointBean) getBundleNavi().get("SearchPointBean");
				showPopupInCenter(bean);
			}
		}
		
//		UIC_MapEngineFrameView.getInstance().setGestureListener(genstureListener);
		MapOverwriteLayer.getInstance().showMapElement();
		MapOverwriteLayer.getInstance().setTapPopupEnable(true);
//		MapOverwriteLayer.getInstance().needShowCursor(true);

	}
	
	@Override
	protected void OnPause() {
		super.OnPause();
//		UIC_MapEngineFrameView.getInstance().setGestureListener(null);
		MapOverwriteLayer.getInstance().closeMapElement();
		MapOverwriteLayer.getInstance().setTapPopupEnable(false);
//		MapOverwriteLayer.getInstance().needShowCursor(false);
	}
	
	private void showPopupInCenter(final SearchPointBean bean) {
		
		MapTools.setCenterLonlat((int)bean.getLongitude(), (int)bean.getLatitude());
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				popup.showPopup(bean);
			}
		});
	}

	private void showPopupInCenter(final PointBean bean) {
		MapTools.setCenterLonlat((int)bean.getLonlat()[0], (int)bean.getLonlat()[1]);
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				popup.showPopup(bean);
			}
		});
	}
	
	@Override
	protected int onConnectedScreenId() {
		return SCRMapID.ADT_ID_POIResult;
	}
	
	private void initActionbar() {
		int genreIndex = getBundleNavi().getInt("genreIndex");
		setActionbarInfo(index = genreIndex);
		
	}
	
	private void setActionbarInfo(int genreIndex) {
		Log.i("icon", "result screen:"+genreIndex);
		int srch_type = UISearchControlJNI.UIC_SCM_SRCH_TYPE_NEARBY;
		UISearchResultJNI srchResult = UISearchControlJNI.Instance().GetSearchResult(srch_type);
		String name = srchResult.GetListItemNameAt(genreIndex, UISearchControlJNI.UIC_SCM_LIST_ID_CUSTOM_GENRE);
		setTitle(name);
	}
	
	private void findViews() {
		showList = (RelativeLayout)findViewById(R.id.button_show_list);
		popup = MapOverwriteLayer.getInstance().getPopup();
//		tonavigation.setVisibility(View.GONE);

	}
	
	private void setListener() {
		showList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getBundleNavi().putInt("genreIndex", index);
				popup.dismissPopup();
				MenuControlIF.Instance().BackSearchWinChange(ADT_POI_List.class);
			}
		});
	}
	
	
	@Override
	public boolean OnTrigger(NSTriggerInfo triggerInfo) {
		int iTriggerID = triggerInfo.GetTriggerID();
		PLog.i("Trigger","Trigger");
		switch(iTriggerID)
		{
		case NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE:
			return true;
		case NSTriggerID.UIC_MN_TRG_UISR_SEARCH_OK: 
			return false;
		case NSTriggerID.UIC_MN_TRG_UISR_SEARCH_ERROR:
			PLog.i("Trigger", "UIC_MN_TRG_UISR_SEARCH_ERROR");
			return false;
		case NSTriggerID.UIC_MN_TRG_UISR_SEARCH_EXCEPTION:
			PLog.i("Trigger", "UIC_MN_TRG_UISR_SEARCH_EXCEPTION");
			return false;
		}
		return false;
	}
	

	@Override
	public void onBack() {
		popup.dismissPopup();
		MenuControlIF.Instance().BackWinChange();
	}
	
	@Override
	protected void OnDestroy() {
		super.OnDestroy();
		popup.dismissPopup();
	}

}
