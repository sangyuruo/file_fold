package com.billionav.navi.naviscreen.photo_edit;

import java.io.ByteArrayOutputStream;

import com.billionav.navi.naviscreen.base.BundleNavi;
import com.billionav.sdkspecial.MiniThumbFile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.util.Log;

public class BitmapTransportHelper {
	public static final int COMMAND_GET_PICTURE = 1;
	public static final int COMMAND_DEL_PICTURE = 2;
	public static final int COMMAND_NONE = 0;
	
	private static BitmapTransportHelper instance;
	private final MiniThumbFile mMiniThumbFile;
	private byte[] sMiniThumbData = null;
	private  Uri EXT_URI = Images.Media.EXTERNAL_CONTENT_URI;
	
	private BitmapTransportHelper(){
		EXT_URI = Images.Media.EXTERNAL_CONTENT_URI;
		mMiniThumbFile = new MiniThumbFile(EXT_URI);
		sMiniThumbData = new byte[MiniThumbFile.BYTES_PER_MINTHUMB];
	}
	public static BitmapTransportHelper getInstance(){
		if(instance == null){
			instance = new BitmapTransportHelper();
		}
		return instance;
	}
	public Bitmap decodeBytes(byte[] buff){
		return BitmapFactory.decodeByteArray(buff, 0, buff.length);
	}
	public byte[] compressBitmap(Bitmap pic){
		int buffLen = pic.getWidth()*pic.getHeight();
		byte[] transbp = new byte[buffLen];
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();       
	    pic.compress(Bitmap.CompressFormat.PNG, 100, baos);       
	    transbp = baos.toByteArray(); 
	    return transbp;
	}
	public Bitmap getSuitableSizeBitmap(String path){
		BitmapFactory.Options opts = new BitmapFactory.Options(); 
		opts.inJustDecodeBounds = true; 
		BitmapFactory.decodeFile(path, opts);
		opts.inJustDecodeBounds = false; 
		Log.d("show", "optValue"+opts.outWidth+" "+opts.outHeight);
		if(opts.outWidth > (CameraLayout.BASIC_WIDTH<<3)||opts.outHeight>(CameraLayout.BASIC_WIDTH<<3)){
			opts.inSampleSize =((opts.outWidth > opts.outHeight)?opts.outWidth:opts.outHeight)/(CameraLayout.BASIC_WIDTH<<3);
			Log.d("show","inSampleSize"+opts.inSampleSize);
		} 

		return BitmapFactory.decodeFile(path, opts);
	}
	public Bitmap drawBitMap(long magic, long id, Context context) {
		return mMiniThumbFile.getThumbnail(context.getContentResolver(), id, Images.Thumbnails.MICRO_KIND, magic, sMiniThumbData);
	}
	public Bitmap getSuitableSizeBitmap(Bitmap bm){
		return Bitmap.createBitmap(bm
				, (int)((bm.getWidth() - CameraLayout.BASIC_WIDTH) / 2)
				, (int)((bm.getHeight() - CameraLayout.BASIC_WIDTH) / 2)
				, (int)(CameraLayout.BASIC_WIDTH)
				, (int)(CameraLayout.BASIC_WIDTH));
	}
	public void putBitmapIntoBundle(Bitmap bp){
		if(bp.getHeight()>116){
			bp = BitmapTransportHelper.getInstance().getSuitableSizeBitmap(bp);
		}
		int buffLen = bp.getWidth()*bp.getHeight();
		byte[] transbp = new byte[buffLen];
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();       
	    bp.compress(Bitmap.CompressFormat.PNG, 100, baos);       
	    transbp = baos.toByteArray(); 
		BundleNavi.getInstance().put("image", transbp);
	}
	public Bitmap getBitmapFromBundle(){
		byte buff[]=(byte[])BundleNavi.getInstance().get("cutPic"); 
		if (null != buff){
			return BitmapFactory.decodeByteArray(buff, 0, buff.length);			
		}
		return null;
	}
	public int getCommand(){
		return BundleNavi.getInstance().getInt("command");
	}
}
