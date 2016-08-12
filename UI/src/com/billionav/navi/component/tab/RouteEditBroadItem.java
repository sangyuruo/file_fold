package com.billionav.navi.component.tab;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.ui.R;
import com.billionav.navi.component.listcomponent.ListContainer;
import com.billionav.navi.component.listcomponent.ListItemInterface;
import com.billionav.navi.component.listcomponent.ListItemTextWithoutImg;
import com.billionav.navi.component.tab.RouteEditBroad.OnRouteEditItemClickListener;

public class RouteEditBroadItem extends RelativeLayout{
	public static final int start_Point = 100000;
	public static final int destination_Point = 100001;
	public static final int way_Point = 100002;
	
	private PopupWindow popupWindowStartPoint = null;
	
	private TextView explainText = null;
	private TextView locationText = null;
	
	private int			itemType = -1;
	private int			popupWidth = 0;
	
	private OnRouteEditItemClickListener clickListener;
	
	public RouteEditBroadItem(Context context,int type, String locationStr) {
		super(context);
		itemType = type;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.route_edit_broad_item, this);
		findViews();
		explainText.setText(pointTypeName(type));
		locationText.setText(locationStr);
		setListeners();
	}
	
	public RouteEditBroadItem(Context context, int type) {
		super(context);
		itemType = type;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.route_edit_broad_item, this);
		findViews();
		explainText.setText(pointTypeName(type));
		if(type == start_Point){
			locationText.setText(R.string.STR_MM_03_02_02_01);
		} else {
			locationText.setText("");
		}
		setListeners();
	}
	
	public RouteEditBroadItem(Context context, int type, OnRouteEditItemClickListener l){
		super(context);
		itemType = type;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.route_edit_broad_item, this);
		findViews();
		explainText.setText(pointTypeName(type));
		if(type == start_Point){
			locationText.setText(R.string.STR_MM_03_02_02_01);
		} else {
			locationText.setText("");
		}
		setListeners();
		clickListener = l;
	} 
	
	private int pointTypeName(int type){
		switch (type) {
		case start_Point:
			return R.string.STR_MM_03_01_01_02;
		case destination_Point:
			return R.string.STR_MM_03_01_01_03;
		case way_Point:
			return R.string.STR_MM_03_04_05_03;
		default:
			throw new RuntimeException("type is wrong");
		}
	}
	
	private void findViews() {
		explainText = (TextView)findViewById(R.id.routeeditbroaditem_explain_icon);
		locationText = (TextView)findViewById(R.id.routeeditbroaditem_location_icon);
	}
		
	public void setlocationText(String text){
		locationText.setText(text);
	}
	
	public void setlocationText(int textId){
		locationText.setText(textId);
	}
	
	public String getlocationText(){
		return locationText.getText().toString();
	}
	
	private void setListeners(){
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(clickListener != null){
					clickListener.onItemClick(RouteEditBroadItem.this ,0);
				}
			}
		});
	}
	
	private PopupWindow getPopupWindow() {
		if (popupWindowStartPoint != null) {
			popupWidth = getWidth();
			popupWindowStartPoint.setWidth(popupWidth);
			return popupWindowStartPoint;
		}
		listContainer = new ListContainer(getContext());
		ListItemTextWithoutImg listItemText = new ListItemTextWithoutImg(getContext(), R.string.STR_MM_03_02_02_01);
		listItemText.setOnClickListener(l);
		listContainer.addItem(listItemText);
		listItemText = new ListItemTextWithoutImg(getContext(), R.string.STR_MM_03_01_01_06);
		listItemText.setOnClickListener(l);
		listContainer.addItem(listItemText);
		listItemText = new ListItemTextWithoutImg(getContext(), R.string.STR_MM_03_01_02_07);
		listItemText.setOnClickListener(l);
		listItemText.setEnabled(false);
		listContainer.addItem(listItemText);
		listItemText = new ListItemTextWithoutImg(getContext(), R.string.STR_MM_03_01_02_08);
		listItemText.setOnClickListener(l);
		listItemText.setEnabled(false);
		listContainer.addItem(listItemText);
		if(itemType == way_Point){
			listItemText = new ListItemTextWithoutImg(getContext(), R.string.STR_MM_03_01_01_13);
			listItemText.setOnClickListener(l);
			listContainer.addItem(listItemText);
		}
		
		float density =  getResources().getDisplayMetrics().density;
		int height =  (listContainer.getCount() + 1)* (int)(40  *density +.5f);
		popupWindowStartPoint = new PopupWindow(listContainer, getWidth(), height);
		popupWindowStartPoint.setFocusable(true);
	
		return popupWindowStartPoint;
	}
	
	private ListContainer listContainer;
	private OnClickListener l = new OnClickListener() {
		public void onClick(View v) {
			int index = listContainer.getIndexOfItem((ListItemInterface) v);
			if(clickListener != null){
				clickListener.onItemClick(RouteEditBroadItem.this ,index);
			}
		}
	};
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		if (null != popupWindowStartPoint && popupWindowStartPoint.isShowing()) {
			// TODO Auto-generated method stub
			new Handler().postDelayed(new Runnable(){
				public void run() {
					// TODO Auto-generated method stub
					if(null == popupWindowStartPoint) { return;
                    }
					int screenHeight = getResources().getDisplayMetrics().heightPixels;
					int popupHeight = popupWindowStartPoint.getHeight();
					popupWindowStartPoint.dismiss();
					if (popupHeight > screenHeight / 2) {
						popupWindowStartPoint.showAtLocation(getRootView(),
								android.view.Gravity.CENTER, 0, 0);
					} else {
						popupWindowStartPoint.showAsDropDown(RouteEditBroadItem.this);
					}

					if (getWidth() != popupWidth) {
						popupWidth = getWidth();
					}

					popupWindowStartPoint.update(popupWidth,
							popupWindowStartPoint.getHeight());
				}
			}, 300);
			
		}
		super.onConfigurationChanged(newConfig);
	}

}
