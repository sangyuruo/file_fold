package com.billionav.navi.naviscreen.favorite;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.billionav.jni.UIPointData;
import com.billionav.navi.component.listcomponent.Favorite_Delete_Item;
import com.billionav.navi.naviscreen.download.TabHostListContainer;
import com.billionav.ui.R;

public class Faverate_ListContainer_Delete extends TabHostListContainer{
	private OnClickListener itemClickListener;
	
	private ArrayList<UIPointData> datas = new ArrayList<UIPointData>();
	
	private boolean[] selected;
	
	
	public Faverate_ListContainer_Delete(Context context,boolean isexpandable) {
		super(context,isexpandable);
		adaptData();
	}

	@Override
	protected void listviewOnItemClick(AdapterView<?> arg0, View arg1,
			int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(!selected[arg2]){
			((Favorite_Delete_Item)arg1).setCheaked(true);
			selected[arg2] = true;
		}
		else{
			((Favorite_Delete_Item)arg1).setCheaked(false);
			selected[arg2] = false;
		}
		if(itemClickListener != null) {
			itemClickListener.onClick(null);
		}
	}
	
	public void setData(ArrayList<UIPointData> datas) {
		this.datas = datas;
		selected = new boolean[datas.size()];
		Arrays.fill(selected, false);
	}
	
	public void setFilter(UIPointData[] points, String searchFiler) {
		datas.clear();
		selected = new boolean[points.length];
		if(TextUtils.isEmpty(searchFiler)) {
			for(UIPointData point: points) {
				datas.add(point);
			}
		} else {
			for(UIPointData point: points) {
				if(point.getName()!=null && point.getName().contains(searchFiler)
						|| point.getAddress()!=null && point.getAddress().contains(searchFiler)) {
					datas.add(point);
				}
			}
		}
		Arrays.fill(selected, false);
	}


	@Override
	public int getListcount() {
		// TODO Auto-generated method stub
		return datas.size();
	}
	
	public void allselect() {
		boolean isAllSelected = true;
		
		for(int i=0; i<getListcount();i++) {
			if(!selected[i]) {
				isAllSelected = false;
			}
		}
		
		Arrays.fill(selected, !isAllSelected);
	}
	
	public int[] getCheckedIds() {
		int count = 0;
		
		for(int i=0; i<getListcount();i++) {
			if(selected[i]) {
				count++;
			}
		}
		
		int[] checkedIds = new int[count];
		int index = 0;
		for(int i=0; i<getListcount();i++) {
			if(selected[i]) {
				checkedIds[index] = datas.get(i).getID();
				index++;
			}
		}
		
		return checkedIds;
	}

	@Override
	public View getListView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//save current index
		UIPointData pointdata = datas.get(position);
		
		if(convertView == null) {
			convertView = new Favorite_Delete_Item(getContext(), 
					pointdata.getName(),
					pointdata.getAddress(),
					pointdata.getTelNo(),
					selected[position],
					position); 
			convertView.setBackgroundResource(R.drawable.list_selector_background);
		} else {
			((Favorite_Delete_Item)convertView).setIndex(position);
			((Favorite_Delete_Item)convertView).setName(pointdata.getName());
			((Favorite_Delete_Item)convertView).setAddress(pointdata.getAddress());
			((Favorite_Delete_Item)convertView).setTelephoneNumber(pointdata.getTelNo());
			((Favorite_Delete_Item)convertView).setCheaked(selected[position]);
			convertView.setBackgroundResource(R.drawable.list_selector_background);
		}
		return convertView;
	}
	
}
