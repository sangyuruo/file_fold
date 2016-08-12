package com.billionav.navi.naviscreen.photo_edit;


import android.os.Bundle;
import android.widget.GridView;

import com.billionav.navi.naviscreen.base.ActivityBase;
import com.billionav.ui.R;


public class ADT_Photo_Browser extends ActivityBase{
//	public static final int BASIC_LIST_NUM = 9;
	private PhotosGridContainer gridView;
	
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_photo_edit_photos);
		findViews();
		initialize();
		gridView.setNumColumns(GridView.AUTO_FIT);
		gridView.setColumnWidth((int)(100*getResources().getDisplayMetrics().density+0.5f));
	}

	@Override
	protected void OnResume() {

		super.OnResume();
	}
	@Override
	public void OnShow() {
		super.OnShow();
	}
	

	private void initialize() {
		setDefaultBackground();
		//string Album edit in Chinese
		setTitleText(R.string.STR_MM_02_02_07_02);
	}

	private void findViews() {
		gridView = (PhotosGridContainer)findViewById(R.id.adt_photo_edit_photos_gridView);
	}
	long start;
	

	@Override
	protected void OnDestroy() {
		gridView.destory();
		super.OnDestroy();
	}
}
