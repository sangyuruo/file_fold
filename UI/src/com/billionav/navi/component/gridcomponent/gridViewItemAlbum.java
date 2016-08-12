package com.billionav.navi.component.gridcomponent;

import com.billionav.ui.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;


public class gridViewItemAlbum extends GridItemInterface{
	private ImageView AlbumView;
	public gridViewItemAlbum(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public gridViewItemAlbum(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.grid_item_photos, this);
		findView();
	}
	public gridViewItemAlbum(Context context,  Bitmap bm){
		this(context);
		AlbumView.setImageBitmap(bm);
	}

	private void findView() {
		AlbumView = (ImageView)findViewById(R.id.grid_Item_photo_ImageView);		
	}
	public void setPic(Bitmap bm){
		AlbumView.setImageBitmap(bm);
	}
	public void setPic(int resID){
		AlbumView.setImageResource(resID);
	}

}
