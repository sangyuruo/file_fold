package com.billionav.navi.component.guidebar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.jni.UIGuideControlJNI;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.guidebar.base.GuideInfoController.DataChangeListener;
import com.billionav.navi.component.guidebar.base.GuideInfoDataManager;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.sync.AppLinkService;
import com.billionav.ui.R;
import android.util.Log;

public class GIB_TurningInfo extends RelativeLayout implements DataChangeListener{
	
	private ImageView	guideArrow;
	private RelativeLayout	guideDistance;
	
	private TextView	guideDistanceNum;
	private TextView	guideDistanceUnit;
	
	private RelativeLayout guideTurningInfoView;
	
	private int resID = 0;


	public GIB_TurningInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		initialize();
	}

	private void initialize(){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.guidebar_turning_info, this);
	    
	    findViews();
	    
	}
	
	private void findViews(){
		guideArrow = (ImageView) findViewById(R.id.gib_turning_arrow);
		guideDistance = (RelativeLayout) findViewById(R.id.gib_distance);
		guideDistanceNum = (TextView)findViewById(R.id.gib_distance_num);
		guideDistanceUnit = (TextView)findViewById(R.id.gib_distance_unit);
		guideTurningInfoView = (RelativeLayout) findViewById(R.id.gib_turning_layout);
	}
	
	public void illustShow(boolean show){
//		RelativeLayout.LayoutParams lp = (LayoutParams) guideArrow.getLayoutParams();
//		boolean needRefresh = false;
//		if(show){
//			if(lp.leftMargin != (int) (15*getResources().getDisplayMetrics().density +.5f)){
//				lp.leftMargin = (int) (15*getResources().getDisplayMetrics().density +.5f);
//				needRefresh = true;
//			}
//		}else{
//			if(lp.leftMargin != (int) (15*getResources().getDisplayMetrics().density +.5f)){
//				lp.leftMargin = (int) (15*getResources().getDisplayMetrics().density +.5f);
//				needRefresh = true;
//			}
//		}
//		if(needRefresh){
//			guideArrow.setLayoutParams(lp);
//		}
		
	}
	
	public void setTurningInfoClickListener(OnClickListener l){
		guideArrow.setOnClickListener(l);
	}
	
	public void setGuideArrow(int imageID){
		if(resID != imageID){
			resID = imageID; 
			guideArrow.setImageResource(imageID);
		}
	}
	
	public void setGuideArrow(Bitmap imageBmp){
		guideArrow.setImageBitmap(imageBmp);
	}
	
	public void setGuideArrow(Drawable imageDrawable){
		guideArrow.setImageDrawable(imageDrawable);
	}
	
	
	public void setGuideDistance(String distance){
		String num = distance.replaceAll("[a-z]*", "");
		String unit = distance.replaceAll("[0-9]*\\.?[0-9]*", "");
		if(!guideDistanceNum.getText().toString().equals(num)){
			guideDistanceNum.setText(num);
		}
		if(!guideDistanceUnit.getText().toString().equals(unit)){
			guideDistanceUnit.setText(unit);
		}

	}
	
	public void setGuideDistanceColor(int color){
		guideDistanceNum.setTextColor(color);
		guideDistanceUnit.setTextColor(color);
	}
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
		int resID = GuideInfoDataManager.Instance().getTurningImageID();
		String distance = GuideInfoDataManager.Instance().getTurningDistance();
		setGuideArrow(resID);
		setGuideDistance(distance);
		//AppLinkService service = AppLinkService.getInstance();
		//if(null != service ){
		//    service.onGuideInfoChanged();
		//}		
		//else{
		//   Log.v("AbelDebugGuide","GIB_TurningInfo service is null"); 
		//}
	}

	public void resizeLayout() {
		RelativeLayout.LayoutParams turnIconLp = (LayoutParams)guideArrow.getLayoutParams();
		RelativeLayout.LayoutParams guideLp = (LayoutParams)guideDistance.getLayoutParams();
		RelativeLayout.LayoutParams layoutLp = (LayoutParams)getLayoutParams();
		if(ScreenMeasure.isPortrait()){
			guideLp.addRule(RelativeLayout.BELOW, R.id.gib_turning_arrow);
			guideLp.addRule(RelativeLayout.RIGHT_OF, 0);
			guideLp.addRule(RelativeLayout.CENTER_VERTICAL,0);
			guideLp.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
//			layoutLp.addRule(RelativeLayout.LEFT_OF, 0);
			layoutLp.width = DensityUtil.dp2px(getContext(), 82);
			layoutLp.height = DensityUtil.dp2px(getContext(), 87);
			turnIconLp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			turnIconLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
//			guideDistance.setTextSize(20);
			if(GuideInfoDataManager.Instance().getGuidePointKind() != UIGuideControlJNI.DPGUDEF_CMN_ROAD_HWY){
				guideTurningInfoView.setBackgroundResource(R.drawable.navicloud_and_713a);
			}else{
				guideTurningInfoView.setBackgroundResource(R.drawable.navicloud_and_713b);
			}
		}else{
			guideLp.addRule(RelativeLayout.BELOW, 0);
			guideLp.addRule(RelativeLayout.RIGHT_OF, R.id.gib_turning_arrow);

			guideLp.addRule(RelativeLayout.CENTER_HORIZONTAL,0);
			guideLp.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
//			layoutLp.addRule(RelativeLayout.LEFT_OF, R.id.streetinfo);
			layoutLp.width = DensityUtil.dp2px(getContext(), 148);
			layoutLp.height = DensityUtil.dp2px(getContext(), 55);
			turnIconLp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
			turnIconLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//			guideDistance.setTextSize(30);
			
			if(GuideInfoDataManager.Instance().getGuidePointKind() == UIGuideControlJNI.DPGUDEF_CMN_ROAD_HWY){
				guideTurningInfoView.setBackgroundResource(R.drawable.navicloud_and_708b);
			}else{
				guideTurningInfoView.setBackgroundResource(R.drawable.navicloud_and_708a);
			}
		}
		guideDistance.setLayoutParams(guideLp);
		setLayoutParams(layoutLp);
		guideArrow.setLayoutParams(turnIconLp);
	}
	@Override
	public void onRoutePointKindChanged() {
		Log.d("test","onRoutePointKindChanged turning info");
		int pointKind = GuideInfoDataManager.Instance().getGuidePointKind();
		int backgroundRes = 0;
		if(ScreenMeasure.isPortrait()){
			if(pointKind != UIGuideControlJNI.DPGUDEF_CMN_ROAD_HWY){
				backgroundRes = R.drawable.navicloud_and_713a;
			}else{
				backgroundRes = R.drawable.navicloud_and_713b;
			}
		}else{
			if(pointKind != UIGuideControlJNI.DPGUDEF_CMN_ROAD_HWY){
				backgroundRes = R.drawable.navicloud_and_708a;
			}else{
				backgroundRes = R.drawable.navicloud_and_708b;
			}
		}
		guideTurningInfoView.setBackgroundResource(backgroundRes);
	}
}
