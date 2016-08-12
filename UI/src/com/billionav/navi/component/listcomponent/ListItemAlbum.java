package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.billionav.navi.naviscreen.photo_edit.CatalogThumbItem;
import com.billionav.ui.R;

public class ListItemAlbum extends ListItemInterface{

	private ImageView AlbumView;
	private TextView FileName;
	private TextView picCountTextView;
	public TextView getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName.setText(fileName);
	}


	public void setPicCount(int count){
		picCountTextView.setText("("+count+")");
	}


	public ListItemAlbum(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public ListItemAlbum(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ListItemAlbum(Context context, CatalogThumbItem ci){
		super(context);
		if(null == ci){
			return;
		}
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_album, this);
		findView();
		AlbumView.setImageBitmap(ci.getBitmap());
		picCountTextView.setText("("+ci.getPicCount()+")");
		FileName.setText(ci.getTextView());
	}

	private void findView() {
		AlbumView = (ImageView)findViewById(R.id.list_Item_Album_ImageView);
		picCountTextView = (TextView)findViewById(R.id.list_Item_Album_text);
		FileName = (TextView)findViewById(R.id.list_Item_Album_pic_count);
	}
	public String getText(){
		return FileName.getText().toString();
	}
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		super.setOnClickListener(l);
	}
	public ImageView getPic(){
		return null;
	}
	public void setpic(Bitmap bm){
		AlbumView.setImageBitmap(bm);
	}
	
	
	public void setImageResource(int resId){
		AlbumView.setImageResource(resId);
	}

}
