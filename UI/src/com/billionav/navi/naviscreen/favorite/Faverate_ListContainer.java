package com.billionav.navi.naviscreen.favorite;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.jni.UIPointControlJNI;
import com.billionav.jni.UIPointData;
import com.billionav.navi.component.listcomponent.Favorite_Item;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.navi.naviscreen.download.TabHostListContainer;
import com.billionav.navi.naviscreen.route.ADT_Route_Main;
import com.billionav.navi.uitools.RouteCalcController;
import com.billionav.ui.R;

public class Faverate_ListContainer extends TabHostListContainer{
	public final static int MY_TAB_NUM= 2;
	
	private TextView noneItemText = null;
	
	private UIPointData[] favorites = new UIPointData[0];
	private UIPointData[] histroys = new UIPointData[0];
	
	private ArrayList<UIPointData> disPlaysPoints = new ArrayList<UIPointData>();
	
	public Faverate_ListContainer(Context context,boolean isexpandable) {
		super(context,isexpandable);
		adaptData();
		noneItemText = new TextView(getContext());
		noneItemText.setText(R.string.MSG_02_01_01_01);
		noneItemText.setTextSize(25);
		noneItemText.setTextColor(0xFF336699);
		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.setMargins(0, 45, 0, 0);
		addView(noneItemText,layoutParams);
		noneItemText.setVisibility(View.GONE);
	}
	
	public void refreshNoResultHint(boolean isSearchState){
		if(getListcount() == 0 && isSearchState){
			noneItemText.setVisibility(View.VISIBLE);
		}else{
			noneItemText.setVisibility(View.GONE);
		}
	}
	
	public void setFavorites(UIPointData[] favorites) {
		this.favorites = favorites;
	}

	public void setHistroys(UIPointData[] histroys) {
		this.histroys = histroys;
	}
	
	public ArrayList<UIPointData> getDisplayLists() {
		return disPlaysPoints;
	}

	@Override
	protected void listviewOnItemClick(AdapterView<?> arg0, View arg1,
			int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		UIPointData pointdata = disPlaysPoints.get(arg2);
		BundleNavi.getInstance().putInt("record", pointdata.getID());
		final boolean isTabOne = (getCurrentListState()==MY_TAB_ONE);
		BundleNavi.getInstance().putBoolean("favorited", isTabOne);
		BundleNavi.getInstance().put("pointdata", pointdata);
		if(!isTabOne){
			UIPointControlJNI.Instance().reqFindPoint(pointdata.getName(),pointdata.getLon(),pointdata.getLat());
		}
		MenuControlIF.Instance().ForwardWinChange(ADT_Favorite_Detail.class);
		if(OnItemClicklistener != null){
			OnItemClicklistener.onClick(null);
		}
	}
	
	private void setFilter(UIPointData[] points, String searchFiler) {
		disPlaysPoints.clear();
		if(TextUtils.isEmpty(searchFiler)) {
			for(UIPointData point: points) {
				disPlaysPoints.add(point);
			}
		} else {
			for(UIPointData point: points) {
				if(point.getName()!=null && point.getName().contains(searchFiler)
						|| point.getAddress()!=null && point.getAddress().contains(searchFiler)
						|| point.getTelNo()!=null && point.getTelNo().contains(searchFiler)) {
					disPlaysPoints.add(point);
				}
			}
		}
	}

	@Override
	public int getListcount() {
		// TODO Auto-generated method stub
		return disPlaysPoints.size();
	}

	

	@Override
	protected void refreshTab2(String searchFiler) {
		setFilter(histroys, searchFiler);
	}

	@Override
	protected void refreshTab1(String searchFiler) {
		setFilter(favorites, searchFiler);
	}

	@Override
	public View getListView(int position, View convertView, ViewGroup parent) {
		
		final UIPointData pointdata = disPlaysPoints.get(position);
		
		OnClickListener disposalListener  = new View.OnClickListener() {
			UIPointData point = pointdata;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = point.getName();
				String tel = point.getTelNo();
				String address = point.getAddress();
				long lon = point.getLon();
				long lat = point.getLat();
				if(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE != RouteCalcController.instance().getRoutePointFindPurpose()){
					RouteCalcController.instance().pointDataToPOI(name,lon,lat,address,tel);
					if(!MenuControlIF.Instance().BackSearchWinChange(ADT_Route_Main.class)){
						MenuControlIF.Instance().ForwardKeepDepthWinChange(ADT_Route_Main.class);		
					}
				}
				else{
					RouteCalcController.instance().rapidRouteCalculateWithData(name,lon,lat,address,tel);
				    if(OnItemClicklistener != null) {
				    	OnItemClicklistener.onClick(null);
                    }
				}
			}
		};
		if(pointdata != null){
			if(convertView == null)
			{
				if(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE != RouteCalcController.instance().getRoutePointFindPurpose())
				{
					switch (RouteCalcController.instance().getRoutePointFindPurpose()) {
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_START:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_START:
					convertView = new Favorite_Item(getContext(), pointdata.getName(),pointdata.getAddress(),pointdata.getTelNo(),position,R.drawable.navicloud_and_556a,disposalListener);
						break;
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA:
					convertView = new Favorite_Item(getContext(), pointdata.getName(),pointdata.getAddress(),pointdata.getTelNo(),position,R.drawable.navicloud_and_555a,disposalListener);
						break;
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST:
					convertView = new Favorite_Item(getContext(), pointdata.getName(),pointdata.getAddress(),pointdata.getTelNo(),position,R.drawable.navicloud_and_557a,disposalListener);
						break;
					default:
						break;
					}
				}else{
					convertView = new Favorite_Item(getContext(), 
							pointdata.getName(),
							pointdata.getAddress(),
							pointdata.getTelNo(),position,R.drawable.navicloud_and_569a,disposalListener); 
				}
				if(convertView != null){
					convertView.setBackgroundResource(R.drawable.list_selector_background);
				}
			}
			else
			{
				if(RouteCalcController.ROUTE_POINT_FIND_PURPOSE_NONE != RouteCalcController.instance().getRoutePointFindPurpose())
				{
					switch (RouteCalcController.instance().getRoutePointFindPurpose()) {
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_START:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_START:
						((Favorite_Item)convertView).setImage(R.drawable.navicloud_and_556a);
						break;
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_VIA:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_VIA:
						((Favorite_Item)convertView).setImage(R.drawable.navicloud_and_555a);
						break;
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_UPDATE_DEST:
					case RouteCalcController.ROUTE_POINT_FIND_PURPOSE_ADD_DEST:
						((Favorite_Item)convertView).setImage(R.drawable.navicloud_and_557a);
						break;
					default:
						break;
					}
				}else{
					((Favorite_Item)convertView).setImage(R.drawable.navicloud_and_569a);
				}
				((Favorite_Item)convertView).setIndex(position);
				((Favorite_Item)convertView).setTextview01(pointdata.getName());
				((Favorite_Item)convertView).setTextview02(pointdata.getAddress());
				((Favorite_Item)convertView).setTextview03(pointdata.getTelNo());
				((Favorite_Item)convertView).setComputeBtnOnClickListener(disposalListener);
				convertView.setBackgroundResource(R.drawable.list_selector_background);
			}
		}
		return convertView;
	}

}
