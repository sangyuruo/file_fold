package com.billionav.navi.component.bottombar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;

public class BottomBarRoutePreview extends RelativeLayout {
	private ImageView btnleft = null;
	private ImageView btnright = null;
	private RelativeLayout listdisplay_btn = null;
	private RelativeLayout roadedit_btn = null;
	private RelativeLayout startnavigation_btn = null;
	private ImageView simulatenavigation_btn = null;
	private RelativeLayout avoid_btn = null;

	public BottomBarRoutePreview(Context context) {
		super(context);
		init();
	}

	public BottomBarRoutePreview(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.bottombar_route_preview, this);
		
		btnleft = (ImageView)findViewById(R.id.btnleft);
		btnright = (ImageView)findViewById(R.id.btnright);
		listdisplay_btn = (RelativeLayout)findViewById(R.id.listdisplay_btn);
		roadedit_btn = (RelativeLayout)findViewById(R.id.roadedit_btn);
		startnavigation_btn = (RelativeLayout)findViewById(R.id.startnavigation_btn);
		simulatenavigation_btn = (ImageView)findViewById(R.id.simulatenavigation_btn);
		avoid_btn = (RelativeLayout)findViewById(R.id.avoid_btn);

		post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int w = 0;
				if(ScreenMeasure.isPortrait()){
					w = ScreenMeasure.getWidth();
				}else{
					w = ScreenMeasure.getHeight();
				}
				LayoutParams lp = (LayoutParams)((RelativeLayout)findViewById(R.id.bottom_route_layout)).getLayoutParams();
				lp.width = w;
				w /=4;
				lp = (LayoutParams)listdisplay_btn.getLayoutParams();
				lp.width = w;
				lp = (LayoutParams)roadedit_btn.getLayoutParams();
				lp.width = w;
				lp = (LayoutParams)startnavigation_btn.getLayoutParams();
				lp.width = w;
				lp = (LayoutParams)avoid_btn.getLayoutParams();
				lp.width = w;
				requestLayout();
			}
			
		});
			
		
	}

	public interface btnItemClick {
		void click(View view, int index);
	}

	public ImageView getBtnLeft() {
		return btnleft;
	}

	public ImageView getBtnRight() {
		return btnright;
	}

	public RelativeLayout getListdisplayBtn() {
		return listdisplay_btn;
	}

	public RelativeLayout getRoadeditBtn() {
		return roadedit_btn;
	}
	
	public RelativeLayout getStartnavigationBtn(){
		return startnavigation_btn;
	}

	public ImageView getSimulatenavigationBtn(){
		return simulatenavigation_btn;
	}
	public RelativeLayout getavoidbtn(){
		return avoid_btn;
	}
	public void modifyPrevAndNextPosition(){
		RelativeLayout rl = (RelativeLayout)findViewById(R.id.bar_one);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)rl.getLayoutParams();
		//portrait 
		if(ScreenMeasure.isPortrait()){
			lp.addRule(RelativeLayout.ABOVE, R.id.bar_two);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
			MapView.getInstance().resizeScaleBarForOutsideAct(0, -20);
		}else{
			lp.addRule(RelativeLayout.ABOVE, 0);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
			MapView.getInstance().resizeScaleBarForOutsideAct(0, -80);
		}
		rl.setLayoutParams(lp);
		
	}
}
