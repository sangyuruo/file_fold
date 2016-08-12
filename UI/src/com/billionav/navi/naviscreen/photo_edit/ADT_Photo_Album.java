package com.billionav.navi.naviscreen.photo_edit;


import android.os.Bundle;

import com.billionav.ui.R;
import com.billionav.navi.naviscreen.base.ActivityBase;

public class ADT_Photo_Album extends ActivityBase{
	private AlbumListContainer list;
	@Override
	protected void OnCreate(Bundle savedInstanceState) {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.adt_photo_edit_album);
		initialize();
		findViews();
	}
	private void findViews() {
		list = (AlbumListContainer)findViewById(R.id.adt_photo_edit_album_listview);
	}

	@Override
	protected void OnResume() {
		refreshList();
		super.OnResume();
	}
	private void refreshList() {
	}

	

	private void initialize() {
		setDefaultBackground();
		//blue star
		//string Album edit in Chinese
		setTitleText(R.string.STR_MM_02_02_08_01);
		
		
	}

	@Override
	protected void OnDestroy() {
		list.destory();
		super.OnDestroy();
	}
	
	@Override
	protected void OnStop() {
		super.OnStop();
	}
	

}
