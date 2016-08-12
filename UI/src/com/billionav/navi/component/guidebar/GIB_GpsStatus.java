package com.billionav.navi.component.guidebar;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.gps.CLocationListener;
import com.billionav.ui.R;

public class GIB_GpsStatus extends ImageView {
	private static final int GPS_UPDATE_HANDLER_MESSAGE_ID = 0x1010;
	private AnimationDrawable dr;
	private boolean isGPSON = CLocationListener.Instance().isGpsOK();
//	private final jniLocationIF location = new  jniLocationIF();
	public GIB_GpsStatus(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		
		gpsHandler.sendEmptyMessage(GPS_UPDATE_HANDLER_MESSAGE_ID);
	}
	private boolean updateGPSStatus() {
		boolean latestGPS = CLocationListener.Instance().isGpsOK();
		Log.d("GPS", "latestGPS = " + latestGPS);
		if(latestGPS != isGPSON){
			isGPSON = latestGPS;
			onDataChanged();
		}
		return latestGPS;
	}
	private void init(Context context) {
		initView();
//	    isGPSON = location.GetGpsDimension() > jniLocationIF.GPS_DIM_1D;
	    dr = new AnimationDrawable(){};
	    gpsHandler.post(new Runnable(){
	    	@Override
	    	public void run() {
	    		dr.addFrame(getResources().getDrawable(R.drawable.navicloud_and_010b), 1000);
	    		dr.addFrame(new ColorDrawable(0x00000000), 300);
	    		dr.setOneShot(false);
	    		onDataChanged();
	    	}
	    });
	}
	private void initView() {
		setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dp2px(getContext(), 56), DensityUtil.dp2px(getContext(), 11)));
		setPadding(0, DensityUtil.dp2px(getContext(), 3), 0, DensityUtil.dp2px(getContext(), 3));
		setImageResource(R.drawable.navicloud_and_010a);
	}
	private Handler gpsHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == GPS_UPDATE_HANDLER_MESSAGE_ID){
				updateGPSStatus();
				sendEmptyMessageDelayed(msg.what, 2000);
			}
		};
	};
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if(visibility ==View.GONE){
			if(gpsHandler.hasMessages(GPS_UPDATE_HANDLER_MESSAGE_ID)){
				gpsHandler.removeMessages(GPS_UPDATE_HANDLER_MESSAGE_ID);
			}
		}else if(visibility == View.VISIBLE){
			if(!gpsHandler.hasMessages(GPS_UPDATE_HANDLER_MESSAGE_ID)){
				gpsHandler.sendEmptyMessage(GPS_UPDATE_HANDLER_MESSAGE_ID);
			}
		}
	}
	public void onDataChanged() {
		if(!isGPSON){
			gpsHandler.post(new Runnable() {
				
				@Override
				public void run() {
						
					dr.stop();
					setImageDrawable(dr);
					dr.start();
				}
			});

		}else{
			setImageResource(R.drawable.navicloud_and_010a);
		}
	}
	public void destory() {
		if(gpsHandler.hasMessages(GPS_UPDATE_HANDLER_MESSAGE_ID)){
			gpsHandler.removeMessages(GPS_UPDATE_HANDLER_MESSAGE_ID);
		}
	}
}
