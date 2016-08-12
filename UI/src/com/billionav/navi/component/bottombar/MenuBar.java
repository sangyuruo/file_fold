package com.billionav.navi.component.bottombar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.DRIR.DRIRAppMain;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.dest.ADT_Overflow_Menu;
import com.billionav.navi.naviscreen.map.ForwardARscreenControl;
import com.billionav.navi.naviscreen.report.ADT_report_main;
import com.billionav.navi.naviscreen.srch.ADT_POI_Main;
import com.billionav.ui.R;

public class MenuBar extends RelativeLayout {

	private ImageView report;
	private ImageView mapLayer;
	private ImageView nearbySearch;
	private ImageView more;

	public MenuBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		findViews(context);
		setDefaultClickEvent();
	}

	public MenuBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		findViews(context);
		setDefaultClickEvent();
	}

	public MenuBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		findViews(context);
		setDefaultClickEvent();
	}

	private void findViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layer_menu_bar, this);
		report = (ImageView) findViewById(R.id.layer_menu_bar_report);
		mapLayer = (ImageView) findViewById(R.id.layer_menu_bar_layers);
		nearbySearch = (ImageView) findViewById(R.id.layer_menu_bar_nearby_search);
		more = (ImageView) findViewById(R.id.layer_menu_bar_more);
	}

	private void setDefaultClickEvent() {
		((View) report.getParent()).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ForwardARscreenControl.getinstance().setDRIRFunChangeState(DRIRAppMain.DRIRAPP_INTERNAL_OTHER_MODE,
						DRIRAppMain.DRIRAPP_CAMEEA_MODE);
				MenuControlIF.Instance()
						.ForwardWinChange(ADT_report_main.class);
			}
		});

		((View) nearbySearch.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						MenuControlIF.Instance().ForwardWinChange(
								ADT_POI_Main.class);
					}
				});
		((View) more.getParent()).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MenuControlIF.Instance().ForwardWinChange(
						ADT_Overflow_Menu.class);
			}
		});

	}

	public void setReportClickListener(OnClickListener l) {
		((View) report.getParent()).setOnClickListener(l);

	}

	public void setLayerClickListener(OnClickListener l) {
		((View) mapLayer.getParent()).setOnClickListener(l);
	}

	public void setItemEnable(int index, boolean enable) {
		int alpha = enable ? 255 : 100;
		switch (index) {
		case 0:
			report.getDrawable().setAlpha(alpha);
			int textColor = ((TextView) findViewById(R.id.layer_menu_bar_textView2))
					.getCurrentTextColor();
			textColor = textColor & 0x00ffffff | (alpha << 24);
			((TextView) findViewById(R.id.layer_menu_bar_textView2))
					.setTextColor(textColor);
			((View) report.getParent()).setEnabled(enable);
			break;
		case 1:
			mapLayer.getDrawable().setAlpha(alpha);
			int textColor1 = ((TextView) findViewById(R.id.layer_menu_bar_textView3))
					.getCurrentTextColor();
			textColor1 = textColor1 & 0x00ffffff | (alpha << 24);
			((TextView) findViewById(R.id.layer_menu_bar_textView3))
					.setTextColor(textColor1);
			((View) mapLayer.getParent()).setEnabled(enable);
			break;
		case 2:
			nearbySearch.getDrawable().setAlpha(alpha);
			int textColor2 = ((TextView) findViewById(R.id.layer_menu_bar_textView4))
					.getCurrentTextColor();
			textColor2 = textColor2 & 0x00ffffff | (alpha << 24);
			((TextView) findViewById(R.id.layer_menu_bar_textView4))
					.setTextColor(textColor2);
			((View) nearbySearch.getParent()).setEnabled(enable);
			break;
		case 3:
			more.getDrawable().setAlpha(alpha);
			int textColor3 = ((TextView) findViewById(R.id.layer_menu_bar_textView5))
					.getCurrentTextColor();
			textColor3 = textColor3 & 0x00ffffff | (alpha << 24);
			((TextView) findViewById(R.id.layer_menu_bar_textView5))
					.setTextColor(textColor3);
			((View) more.getParent()).setEnabled(enable);
			break;
		}
	}

	public void setEnableForAR() {
		setItemEnable(1, false);
		setItemEnable(2, false);
		setItemEnable(3, false);
	}

	public void setImageAlphaForAR() {
		mapLayer.getDrawable().setAlpha(255);
		nearbySearch.getDrawable().setAlpha(255);
		more.getDrawable().setAlpha(255);
	}
}
