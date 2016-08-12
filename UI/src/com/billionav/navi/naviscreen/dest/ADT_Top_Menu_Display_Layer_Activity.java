package com.billionav.navi.naviscreen.dest;

import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.ui.R;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ADT_Top_Menu_Display_Layer_Activity extends ActivityBase {
	private ViewControl setting;
	private ViewControl report;
	private ViewControl vr;
	private ViewControl navi;
	private ViewControl ar;
	
	protected ImageView setting_view;
	protected ImageView report_view;
	protected ImageView vr_view;
	protected ImageView navi_view;
	protected ImageView ar_view;
	
	private RelativeLayout backgLayout;
	
	
    @Override
    protected void OnCreate(Bundle savedInstanceState) {
        super.OnCreate(savedInstanceState);
        setContentView(R.layout.adt_top_menu, false);
        findViews();
        initViewControl();
        applyLayout();
        setImageResources();
    }
    
    @Override
    protected void OnResume() {
    	super.OnResume();
        setBackgroundLayout();

    }
    
	private void findViews() {
    	setting_view = (ImageView) findViewById(R.id.setting);
    	report_view = (ImageView) findViewById(R.id.report);
    	vr_view = (ImageView) findViewById(R.id.vr);
    	navi_view = (ImageView) findViewById(R.id.navi);
    	ar_view = (ImageView) findViewById(R.id.ar);
    	
    	backgLayout = (RelativeLayout) findViewById(R.id.layout);
	}
    
    private void setBackgroundLayout() {
		if(isCH()){
	        backgLayout.setBackgroundResource(ScreenMeasure.isPortrait() ? R.drawable.topmenu_540x922_cn : R.drawable.topmenu_960x502_cn);
		}else{
	        backgLayout.setBackgroundResource(ScreenMeasure.isPortrait() ? R.drawable.topmenu_540x922_jp : R.drawable.topmenu_960x502_jp);
		}
    }
    
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	applyLayout();
    	setBackgroundLayout();
    }
    
    private void initViewControl(){
    	if(isCH()) {
    		setting = new Setting();
    		vr = new VR();
    		navi = new Navigation();
    	} else {
    		setting = new Setting_jp();
    		vr = new VR_jp();
    		navi = new Navigation_jp();
   	    }
    	
    	report = new Report();
		ar = new AR();
    }
    
    private void setImageResources() {
    	if(isCH()) {
    		setting_view.setImageResource(R.drawable.topmenu_setting_selector);
    		vr_view.setImageResource(R.drawable.topmenu_vr_selector);
    		navi_view.setImageResource(R.drawable.topmenu_navi_selector);
    		ar_view.setImageResource(R.drawable.topmenu_ar_selector);
    		report_view.setImageResource(R.drawable.topmenu_report_selector);
    	} else {
    		setting_view.setImageResource(R.drawable.topmenu_setting_jp_selector);
    		vr_view.setImageResource(R.drawable.topmenu_vr_jp_selector);
    		navi_view.setImageResource(R.drawable.topmenu_navi_jp_selector);
   		
    		ar_view.setVisibility(View.GONE);
    		report_view.setVisibility(View.GONE);
    		
   	    }
    	
    	setting_view.setClickable(true);
    	vr_view.setClickable(true);
    	navi_view.setClickable(true);
    	ar_view.setClickable(true);
    	report_view.setClickable(true);
	}

    private void applyLayout() {
    	setting.applyLayout(setting_view);
    	report.applyLayout(report_view);
    	vr.applyLayout(vr_view);
    	navi.applyLayout(navi_view);
    	ar.applyLayout(ar_view);
    }
    
    private boolean isCH() {
    	return SystemTools.isCH();
    }
}

class ViewControl{
	public final int topmargin_portait;
	public final int leftmargin_portait;
	
	public final int topmargin_landscape;
	public final int leftmargin_landscape;
	
	public final int width;
	public final int height;
	
	ViewControl(int topmargin_portait, 
			int leftmargin_portait, 
			int topmargin_landscape,
			int leftmargin_landscape,
			int width,
			int height){
		this.topmargin_portait = topmargin_portait;
		this.leftmargin_portait = leftmargin_portait;
		this.topmargin_landscape = topmargin_landscape;
		this.leftmargin_landscape = leftmargin_landscape;
		this.width = width;
		this.height = height;
	}
	
	public void applyLayout(View view) {
		RelativeLayout.LayoutParams l = (LayoutParams) view.getLayoutParams();
		if(isPortrait()) {
			l.topMargin = (int) (topmargin_portait * ratioVertical());
			l.leftMargin = (int) (leftmargin_portait * ratioHorizontal());
			l.width = (int) (width * ratioHorizontal());
			l.height = (int) (height * ratioVertical());
			l.rightMargin = getScreenWidth() - l.leftMargin - l.width;
		} else {
			l.topMargin = (int) (topmargin_landscape * ratioVertical());
			l.leftMargin = (int) (leftmargin_landscape * ratioHorizontal());
			l.width = (int) (width * ratioHorizontal() );
			l.height = (int) (height * ratioVertical() );
			l.rightMargin = getScreenWidth() - l.leftMargin - l.width;
		}
	}
	
	private float ratioVertical(){
		if(isPortrait()) {
			return getScreenHeight()/922f;
		} else {
			return getScreenHeight()/502f;
		}
	}
	
	private float ratioHorizontal(){
		if(isPortrait()) {
			return getScreenWidth()/540f;
		} else {
			return getScreenWidth()/960f;
		}
	}
	
	private int getScreenHeight() {
		return ScreenMeasure.getHeight() - ScreenMeasure.getHightOfStatusbar();
	}
	
	private int getScreenWidth() {
		return ScreenMeasure.getWidth();
	}
	
	private boolean isPortrait() {
		return ScreenMeasure.isPortrait();
	}
}

class VR extends ViewControl{
	VR() {
		super(topmargin_portait, leftmargin_portait, topmargin_landscape,
				leftmargin_landscape, width, height);
	}
	public static final int topmargin_portait = 493;
	public static final int leftmargin_portait = -8;
	
	public static final int topmargin_landscape = 283;
	public static final int leftmargin_landscape = 183;
	
	public static final int width = 190;
	public static final int height = 196;

}

class VR_jp extends ViewControl{
	VR_jp() {
		super(topmargin_portait, leftmargin_portait, topmargin_landscape,
				leftmargin_landscape, width, height);
	}
	public static final int topmargin_portait = 387;
	public static final int leftmargin_portait = -2;
	
	public static final int topmargin_landscape = 177;
	public static final int leftmargin_landscape = 190;
	
	public static final int width = 190;
	public static final int height = 176;

}

class Setting extends ViewControl{
	Setting() {
		super(topmargin_portait, leftmargin_portait, topmargin_landscape, leftmargin_landscape, width, height);
	}
	public static final int topmargin_portait = 287;
	public static final int leftmargin_portait = 188;
	
	public static final int topmargin_landscape = 77;
	public static final int leftmargin_landscape = 378;
	
	public static final int width = 146;
	public static final int height = 144;
	
}

class Setting_jp extends ViewControl{
	Setting_jp() {
		super(topmargin_portait, leftmargin_portait, topmargin_landscape, leftmargin_landscape, width, height);
	}
	public static final int topmargin_portait = 370;
	public static final int leftmargin_portait = 378;
	
	public static final int topmargin_landscape = 160;
	public static final int leftmargin_landscape = 570;
	
	public static final int width = 174;
	public static final int height = 182;
	
}

class Navigation extends ViewControl{
	Navigation() {
		super(topmargin_portait, leftmargin_portait, topmargin_landscape, leftmargin_landscape, width, height);
	}
	public static final int topmargin_portait = 446;
	public static final int leftmargin_portait = 183;
	
	public static final int topmargin_landscape = 236;
	public static final int leftmargin_landscape = 373;
	
	public static final int width = 178;
	public static final int height = 202;
	
}

class Navigation_jp extends ViewControl{
	Navigation_jp() {
		super(topmargin_portait, leftmargin_portait, topmargin_landscape, leftmargin_landscape, width, height);
	}
	public static final int topmargin_portait = 307;
	public static final int leftmargin_portait = 190;
	
	public static final int topmargin_landscape = 95;
	public static final int leftmargin_landscape = 382;
	
	public static final int width = 178;
	public static final int height = 182;
	
}

class AR extends ViewControl{
	AR() {
		super(topmargin_portait, leftmargin_portait, topmargin_landscape, leftmargin_landscape, width, height);
	}
	public static final int topmargin_portait = 475;
	public static final int leftmargin_portait = 374;
	
	public static final int topmargin_landscape = 265;
	public static final int leftmargin_landscape = 564;
	
	public static final int width = 174;
	public static final int height = 200;
	
}

class Report extends ViewControl{
	Report() {
		super(topmargin_portait, leftmargin_portait, topmargin_landscape, leftmargin_landscape, width, height);
	}
	public static final int topmargin_portait = 288;
	public static final int leftmargin_portait = 348;
	
	public static final int topmargin_landscape = 77;
	public static final int leftmargin_landscape = 538;
	
	public static final int width = 144;
	public static final int height = 144;
	
}