package com.billionav.navi.naviscreen.base;

import com.billionav.ui.R;
import com.billionav.navi.component.listcomponent.ListContainer2;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

public class ListActivityBase extends ActivityBase implements OnItemClickListener{
	private RelativeLayout topArea;
	private RelativeLayout bottomArea;
	
	private ListContainer2 listView;
	
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.scr_list_base);
		
		findViews();
		setListener();
	}

	private void setListener() {
		listView.setOnItemClickListener(this);
		
	}

	private void findViews() {
		topArea = (RelativeLayout) findViewById(R.id.top_area);
		bottomArea = (RelativeLayout) findViewById(R.id.bottom_area);
		listView = (ListContainer2) findViewById(R.id.list_view_navi);
	}
	
	public void setBottomBar(View view){
		bottomArea.removeAllViews();
		bottomArea.addView(view);
	}
	
	
	public void setTopBar(View view) {
		topArea.removeAllViews();
		topArea.addView(view);
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
	
}
