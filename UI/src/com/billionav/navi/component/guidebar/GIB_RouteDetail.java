package com.billionav.navi.component.guidebar;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.navi.app.ext.NaviConstant;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.guidebar.base.GuideInfoController;
import com.billionav.navi.component.guidebar.base.GuideInfoController.DataChangeListener;
import com.billionav.navi.component.guidebar.base.GuideInfoDataManager;
import com.billionav.navi.uitools.RouteTool;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.sync.AppLinkService;
import com.billionav.ui.R;

import android.util.Log;

public class GIB_RouteDetail extends RelativeLayout implements DataChangeListener{
	
	private ImageView routeDetailIcon;
	private TextView  remainingTime;
	private TextView  remainingDistance;
	private ProgressBar detailProg;
	private LinearLayout textinfoView;
	
	private boolean		displayRemainTime = true;
	
	public GIB_RouteDetail(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		initialize();
	}

	private void initialize(){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.guidebar_routedetail_info, this);
	    findViews();
	    setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Do nothing!
				
			}
		});
	}

	private void findViews(){
		routeDetailIcon = (ImageView) findViewById(R.id.gib_time_remain_icon);
		remainingTime = (TextView) findViewById(R.id.gib_times_remain);
		remainingDistance = (TextView) findViewById(R.id.gib_distance_remain);
		textinfoView = (LinearLayout)findViewById(R.id.gib_route_detail_textinfo_layout);
		detailProg = (ProgressBar)findViewById(R.id.gib_route_detail_progress);
		remainingTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				displayRemainTime = !displayRemainTime;
				updateRemainTime();
			}
		});
	}
	
	private void updateRemainTime(){
		String time;
		guideInfoController.updateETAInfo();
		if(displayRemainTime){
			time = GuideInfoDataManager.Instance().getRemainingTimeString();
		}else{
			time = GuideInfoDataManager.Instance().getArriveTimeString();
		}
		setTimeRemainText(time);
	}
	
	
	public void setTimeRemainImage(int resID){
		routeDetailIcon.setImageResource(resID);
	}
	
	public void setTimeRemainText(String time){
		if(!time.equals(remainingTime.getText().toString()) && !RouteTool.substitutionTime(time).equals(remainingTime.getText().toString())){
//			remainingTime.setText("150");
			//TODO
			Log.d(NaviConstant.TAG_MAP, "TimeRemain is " + time);
			if(!displayRemainTime){
				remainingTime.setText(time);
			}else{			
				remainingTime.setText(RouteTool.substitutionTime(time));
			}
		}
	}
	
	public void setDistanceRemainText(String distance){
		String distanceText = RouteTool.substitutionDistance(distance);
		if(!distanceText.equals(remainingDistance.getText().toString())){
			remainingDistance.setText(RouteTool.substitutionDistance(distance));
		}
	}
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}
	public void resizeLayout(){
		RelativeLayout.LayoutParams lp = (LayoutParams)getLayoutParams();
		int progTemp = detailProg.getProgress();
		if(ScreenMeasure.isPortrait()){
			lp.addRule(RIGHT_OF, R.id.turninginfo);
			lp.width = DensityUtil.dp2px(getContext(), 276);
			lp.height = DensityUtil.dp2px(getContext(), 31);
			textinfoView.setOrientation(Configuration.ORIENTATION_LANDSCAPE);
		}else{
			lp.addRule(RIGHT_OF, 0);
			lp.width = DensityUtil.dp2px(getContext(), 148);
			lp.height = DensityUtil.dp2px(getContext(), 55);
			textinfoView.setOrientation(Configuration.ORIENTATION_PORTRAIT);
		}
		detailProg.setProgress(progTemp);
	}

	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
		String distance = GuideInfoDataManager.Instance().getRemainingDistanceString();
		setDistanceRemainText(distance);
		updateRemainTime();
		updateProgress();

		//AppLinkService service = AppLinkService.getInstance();
		//if(null != service){
		//    service.onGuideInfoChanged();
		//}
		//else{
		//   Log.v("AbelDebugGuide","GIB_RouteDetail service is null "); 
		//}

	}
	private void updateProgress(){
		int progressCurrent = GuideInfoDataManager.Instance().getGuideProgress();
		if(detailProg.getProgress() != progressCurrent){
			detailProg.setProgress(progressCurrent);
		}
	}
	
	public void setGuideInfoController(GuideInfoController guideInfoController) {
		this.guideInfoController = guideInfoController;
	}
	
	private GuideInfoController guideInfoController;

	@Override
	public void onRoutePointKindChanged() {
		// TODO Auto-generated method stub
		
	}
}
