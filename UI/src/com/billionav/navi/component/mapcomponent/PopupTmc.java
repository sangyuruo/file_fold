package com.billionav.navi.component.mapcomponent;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.naviscreen.map.TouchMapControl.PointBean;
import com.billionav.navi.naviscreen.map.TouchMapControl.TmcPointBean;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;

public class PopupTmc extends RelativeLayout{
	private static final int POI_WIDTH_DIP = 176;
	private static final int POI_HEIGHT_DIP = 200;
	
	private TextView eventContent;
	private TextView eventStartTime;
	private TextView eventEndTime;
	private long[] lonlat;
	
	public static final int TYPE_LONGPRESS = -1;
	
	public PopupTmc(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public PopupTmc(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	public PopupTmc(Context context) {
		super(context);
		initialize();
	}
	
	private void initialize() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.mapcomponent_popup_tmc, this);
	    findViews();
	    setVisibility(View.GONE);
	    setClickable(false);
	}
	
	public void setLonlat(long lon, long lat){
		lonlat = new long[]{lon, lat};
	}

	public final void refreshLoaction() {
		if(isShowing()) {
			float[] point = UIMapControlJNI.ConvertLonLatToDispPoint((int)lonlat[0], (int)lonlat[1]);
			setPosition((int)point[0], (int)point[1]);
			requestLayout();
		}
	}
	
	   
	private void findViews() {
		eventContent = (TextView) findViewById(R.id.event_info_text);
		eventStartTime = (TextView)findViewById(R.id.starttime_info_text);
		eventEndTime = (TextView)findViewById(R.id.endtime_info_text);
	}
	
	public final void addToViewGroup(RelativeLayout vg) {
		vg.addView(this);
	}
	
	private final void setPosition(int x, int y){
		
		LayoutParams l = (LayoutParams) getLayoutParams();
		l.topMargin = y - DensityUtil.dp2px(getContext(), POI_HEIGHT_DIP);
		l.bottomMargin = ScreenMeasure.getHeight() - y - ScreenMeasure.getHightOfStatusbar();
		
		l.leftMargin = x - DensityUtil.dp2px(getContext(), POI_WIDTH_DIP/2);
		l.rightMargin = ScreenMeasure.getWidth() - x - DensityUtil.dp2px(getContext(), POI_WIDTH_DIP);
    }

	public final void showPopup(PointBean pointBean) {
		TmcPointBean bean = (TmcPointBean) pointBean;
		clearPopupInfo();
		lonlat = bean.getLonlat();
		String eventinfoIcon = getResources().getString(R.string.STR_MM_01_01_01_16);
		String starttimeIcon = getResources().getString(R.string.STR_MM_01_01_01_14);
		String endtimeIcon = getResources().getString(R.string.STR_MM_01_01_01_15);
		int starttimecolorlength = starttimeIcon.length();
		int endtimecolorlengh = endtimeIcon.length();
		int eventcolorlength = eventinfoIcon.length();
		SpannableString eventinfo = new SpannableString(eventinfoIcon+bean.getName());
		SpannableString starttimeinfo = new SpannableString(starttimeIcon+bean.getStartTime()); 
		SpannableString endtimeinfo = new SpannableString(endtimeIcon+bean.getEndTime());
		eventinfo.setSpan(new ForegroundColorSpan(Color.BLACK), 0,eventcolorlength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		starttimeinfo.setSpan(new ForegroundColorSpan(Color.BLACK), 0,starttimecolorlength,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		endtimeinfo.setSpan(new ForegroundColorSpan(Color.BLACK), 0,endtimecolorlengh,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		eventContent.setText(eventinfo);
		eventStartTime.setText(starttimeinfo);
		eventEndTime.setText(endtimeinfo);
		setVisibility(VISIBLE);
		refreshLoaction();
	}
	
	public final void dismissPopup(){
		setVisibility(View.GONE);
		setClickable(false);
		clearPopupInfo();
	}
	
	public final void clearPopupInfo() {
		eventContent.setText("");
		eventStartTime.setText("");
		eventEndTime.setText("");
		lonlat = null;
	}
	
	public final boolean isShowing() {
		return getVisibility() == VISIBLE;
	}
	

	
	public interface PopListener{
		void onShow();
		void onDismiss();
	}
}
