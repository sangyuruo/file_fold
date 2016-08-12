package com.billionav.navi.component.guidebar;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.jni.UIGuideControlJNI;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.guidebar.base.GuideInfoController.DataChangeListener;
import com.billionav.navi.component.guidebar.base.GuideInfoDataManager;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.sync.AppLinkService;
import com.billionav.ui.R;
import android.util.Log;

public class GIB_StreetInfo extends RelativeLayout implements DataChangeListener{
	
	private TextView streetName;
	private HorizontalScrollView hScrollView;
	
	private boolean isAutoScroll;
//	private static int TEXT_WIDTH; 
	public GIB_StreetInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initialize();
	}

	private void initialize(){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.guidebar_street_info, this);
	    
	    setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dp2px(getContext(), 46), ViewGroup.LayoutParams.MATCH_PARENT));
	    setBackgroundResource(R.drawable.navicloud_and_709a);
	    setPadding(DensityUtil.dp2px(getContext(), 35), 0, DensityUtil.dp2px(getContext(), 35), 0);
	    
	    findViews();
	    formatTextWidth();
	}
	
	private void formatTextWidth() {
//		if(ScreenMeasure.isPortrait()){
//			TEXT_WIDTH = DensityUtil.dp2px(getContext(), 276) + 20;
//		}else{
//			TEXT_WIDTH = ScreenMeasure.getWidth() - 2 * DensityUtil.dp2px(getContext(), 148) - 20;
//		}
//		TEXT_WIDTH -= (streetName.getPaddingLeft() << 1);
	}

	private void findViews(){
		streetName = (TextView) findViewById(R.id.gib_streetname);
		hScrollView = (HorizontalScrollView) streetName.getParent();
//		hScrollView.setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
		
		
		streetName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(hScrollView.getScrollX()==0){
					mHandler.post(ScrollRunnable);
				} else {
					mHandler.removeCallbacks(ScrollRunnable);
					resetTextStatus();
				}
			}
		});
	}
	
	private void resetTextStatus(){
		isAutoScroll = false;
		hScrollView.scrollTo(0, 0);
	}
	private void startAutoScroll(){
		mHandler.removeCallbacks(ScrollRunnable);
		isAutoScroll = true;
		mHandler.postDelayed(ScrollRunnable, 200);
	}
	private final Handler mHandler = new Handler();
	private Runnable ScrollRunnable = new Runnable() {
		@Override
		public void run() {
			int off = streetName.getMeasuredWidth() - hScrollView.getWidth();// �жϸ߶�
			if (off > 0) {
				hScrollView.scrollBy(4, 0);

				if (hScrollView.getScrollX() == off) {
					Thread.currentThread().interrupt();
					if (isAutoScroll) {
						resetTextStatus();
					}
				} else {
					mHandler.postDelayed(this, 50);
				}
			}
		}
	};

	
	public void setStreetName(String name){
		if(!streetName.getText().toString().equals(name)){
			
//			int maxTextCount = streetName.getPaint().breakText(name, true, TEXT_WIDTH, null);
//			if(name.length() > maxTextCount){
//				int cut =  name.length() - maxTextCount;
//				if(name.contains(" ")){
//					int idx = name.indexOf(" ")-(cut);
//					if(idx < 2){
//						idx = 2;
//					}
//					name = name.substring(0, idx)+"..."+name.substring(name.indexOf(" "), name.length());
//				}else{
//					int idx = name.length() - cut;
//					if(idx < 2){
//						idx = 2;
//					}
//					name = name.substring(0, idx)+"...";
//				}
//			}
			streetName.setText(name);
			if(null != hScrollView && hScrollView.getScrollX() > 0){
				resetTextStatus();
			}
			startAutoScroll();
		}
		
	}
	
	public void setStreetName(int stringID){
		setStreetName(getContext().getString(stringID));
//		streetName.setText(stringID);
	}
	
	public void setStreetNameColor(int color){
		streetName.setTextColor(color);
	}
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		formatTextWidth();
		setStreetName(GuideInfoDataManager.Instance().getStreetName());
		super.onConfigurationChanged(newConfig);
	}
	public void resizeLayout(){
		RelativeLayout.LayoutParams lp = (LayoutParams)getLayoutParams();
		if(ScreenMeasure.isPortrait()){
			lp.addRule(BELOW, R.id.routedetail);
			lp.addRule(LEFT_OF, 0);
		}else{
			lp.addRule(BELOW, 0);
			lp.addRule(LEFT_OF, R.id.routedetail);
		}
	}
	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
		String name = "";
		//AppLinkService service = AppLinkService.getInstance();
		if(SystemTools.isJP()){
			name = UIGuideControlJNI.getInstance().GetGuidePointInfo_CrossName();
		}else{
			name = GuideInfoDataManager.Instance().getStreetName();
		}
		if(!streetName.getText().toString().equals(name)) {
		    setStreetName(name);
		    //if(null != service  ){
		    //    service.onGuideInfoChanged();
		    //}
		    //else{
		    //    Log.v("AbelDebugGuide","GIB_SteetInfo service is null"); 
		    //}	
		}
	}
	@Override
	public void onRoutePointKindChanged() {
		if(GuideInfoDataManager.Instance().getGuidePointKind() != UIGuideControlJNI.DPGUDEF_CMN_ROAD_HWY){
			setBackgroundResource(R.drawable.navicloud_and_709a);
		}else{
			setBackgroundResource(R.drawable.navicloud_and_709b);
		}
	}

}

