//package com.billionav.navi.naviscreen.debug;
//
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.view.View.OnTouchListener;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.billionav.jni.UIMapControlJNI;
//import com.billionav.jni.jniLocationIF;
//import com.billionav.navi.component.mapcomponent.CarView;
//import com.billionav.navi.menucontrol.MenuControlIF;
//import com.billionav.navi.menucontrol.MenuControlIF.MapScreen;
//import com.billionav.navi.naviscreen.base.ActivityBase;
//import com.billionav.navi.naviscreen.map.ADT_Main_Map_Navigation;
//import com.billionav.ui.R;
//
//public class ADT_Vehicle_Adjust extends ActivityBase implements MapScreen{
//	private TextView confrim;
//	private ImageView clockwise;
//	private ImageView anticlockwise;
//	private CarView carView;
//	
//	private final jniLocationIF mLocationIF = new jniLocationIF();
//	private final int origDegree = mLocationIF.GetHeading();
//
//	@Override
//	protected void OnCreate(Bundle savedInstanceState) {
//		super.OnCreate(savedInstanceState);
//		
//		setContentView(R.layout.adt_debug_vehicle_adjust);
//		
//		setTitle("Vehicle Adjust");
//		
//		findViews();
//		
//		setListeners();
//		
//		initilize();
//	}
//
//	private void initilize() {
//		if(UIMapControlJNI.GetMapDir() == UIMapControlJNI.MAP_DIR_MODE_NORTHUP){
//			float degree = origDegree*360/65536 - (UIMapControlJNI.GetMapDir()*360/65535);
//			carView.setDegress(degree);
//		}
//
//		
//	}
//
//	private void setListeners() {
//		confrim.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				int degree;
//				if(UIMapControlJNI.GetMapDir() == UIMapControlJNI.MAP_DIR_MODE_NORTHUP){
//					degree = (int)((carView.getDegress() + (UIMapControlJNI.GetMapDir()*360/65535))*65536/360);
//				} else if(UIMapControlJNI.GetMapDir() == UIMapControlJNI.MAP_DIR_MODE_HEADINGUP){
//					degree = (int)(carView.getDegress()*65536/360) + origDegree;
//				} else {
//					degree = origDegree;
//				}
//				degree %= 65536;
//				mLocationIF.SetPosition(jniLocationIF.LOC_SET_POS_DIR, UIMapControlJNI.GetCenterLonLat()[0], UIMapControlJNI.GetCenterLonLat()[1], degree);
//				
//				MenuControlIF.Instance().BackSearchWinChange(ADT_Main_Map_Navigation.class);
//			}
//		});
//		
//		clockwise.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				carView.decDegress();
//			}
//		});
//		
//		anticlockwise.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				carView.incDegress();
//			}
//		});
//		
//		clockwise.setOnLongClickListener(new OnLongClickListener(){
//
//			@Override
//			public boolean onLongClick(View v) {
//				carView.startRotate(true);
//				return true;
//			}});
//		anticlockwise.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				carView.startRotate(false);
//				return true;
//			}
//		});
//		
//		clockwise.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if(event.getAction() == MotionEvent.ACTION_UP) {
//					carView.endRotate();
//				}
//				return false;
//			}
//		});
//		
//		anticlockwise.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if(event.getAction() == MotionEvent.ACTION_UP) {
//					carView.endRotate();
//				}
//				return false;
//			}
//		});
//		
//	}
//
//	private void findViews() {
//		confrim = (TextView) findViewById(R.id.button_confirm);
//		clockwise = (ImageView) findViewById(R.id.clockwise);
//		anticlockwise = (ImageView) findViewById(R.id.anticlockwise);
//		carView = (CarView) findViewById(R.id.car_view);
//	}
//
//}
