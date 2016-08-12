package com.billionav.navi.component.guidebar;

import java.util.Arrays;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UIGuideControlJNI;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.guidebar.base.GuideInfoDataManager;
import com.billionav.navi.menucontrol.SCRMapID;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.navi.uitools.SystemTools;
import com.billionav.navi.uitools.UILog;
import com.billionav.ui.R;

public class IllustComponent extends RelativeLayout{
	
	private ImageView	illustImage;
	private ImageView	illustArrow;
	private LinearLayout progressLayout;
	private ProgressBar	progress;
	private TextView	progressText;
	private Bitmap		imgIllust;
	private Bitmap		imgIllustArrow;
	
	private int			distanceNum;
	
	private final UIGuideControlJNI guideControl = UIGuideControlJNI.getInstance();
	
	public static final int MAX_PROGRESS_DIP = 185;
	
	public static final int ILLUST_WIDTH_DIP = 320;
	public static final int ILLUST_HEIGHT_DIP = 230;
	private static final int GUIDE_BAR_HEIGHT_DIP = 87;
	private static final int LAYOUT_MARGIN_DIP = 3;
	
	public IllustComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.gi_illust, this);
	    
	    findViews();
	    post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				resizeLayout();
				
			}
		});
	    if(SystemTools.isCH()){
	    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)progressLayout.getLayoutParams();
	    	params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
	    	params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
	    	progressLayout.setLayoutParams(params);
	    }
	}
	
	private void findViews(){
		illustImage = (ImageView) findViewById(R.id.gii_illust_display);
		illustArrow = (ImageView) findViewById(R.id.gii_illust_arrow);
		progress = (ProgressBar) findViewById(R.id.gii_progress);
		progressText = (TextView) findViewById(R.id.gi_distance);
		progressLayout = (LinearLayout)findViewById(R.id.gii_illust_progress_layout);
	}
	
	private void updateLayout(){
		
		updateMapView();
	}
	@Override
	public void setOnClickListener(OnClickListener l) {
		illustArrow.setOnClickListener(l);
	}
	
	public void updateMapView(){
		boolean isPort = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
		float offsetX = 0;
		float offsetY = 0;
		android.view.ViewGroup.LayoutParams lp = getLayoutParams();
		if(isIllustShowing()){
			
			if(isPort){
				offsetY = (float)(lp.height / 2)/ScreenMeasure.getHeight();
			}else{
				if(SystemTools.isCH()){
					offsetX = (float)(0 + (lp.width / 2))/ScreenMeasure.getWidth();
				}
				else{
					offsetX = (float)(0 - (lp.width / 2))/ScreenMeasure.getWidth();
				}
			}
		}else{
		}
		
		
		UIMapControlJNI.SetScreenIDWithOffset(SCRMapID.ADT_ID_NavigationIllust, offsetX, offsetY);
	}
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		resizeLayout();
		updateLayout();
		if(isIllustShowing()){
			updateIllustImage();
		}
	}
	
	/**
	 * refresh road ,distance and progress 
	 */
	public void notifyTrigger(){
		
		if(isIllustShowing()){
			
			int num = guideControl.GetDistanceInfo_LDistance();
			
			int progressNum = (int)((num * 1.0f) / (distanceNum * 1.0f)*100);
		
			progress.setProgress(progressNum);
			String distance = GuideInfoDataManager.Instance().getTurningDistance();
			progressText.setText(distance);
		}
		
		
	}
	
	/**
	 * Decode illust bitmap
	 * @return true if the image is prepared
	 */
	public boolean prepareIllustImage(){
		
    	android.view.ViewGroup.LayoutParams lp = illustImage.getLayoutParams();
    	byte illustBufferVertical[] = null;
		int illustBufferSize = 0;

		illustBufferVertical = guideControl.GetIllustInfo_BKImageBuffer();
		illustBufferSize = guideControl.GetIllustInfo_BKImageSize();
		if (null == illustBufferVertical 
				|| 0 >= illustBufferVertical.length
				|| 0 >= illustBufferSize) 
		{
			return false;
		}

		Bitmap bm;
		if(null != imgIllust && !imgIllust.isRecycled()){
			imgIllust.recycle();
			imgIllust = null;
		}

			
		bm = BitmapFactory.decodeByteArray(illustBufferVertical, 0, illustBufferSize);
		if(bm == null){
			return false;
		}
		Matrix matrix = new Matrix();
		matrix.postScale((float) (lp.width -DensityUtil.dp2px(getContext(), LAYOUT_MARGIN_DIP * 2))/ bm.getWidth(), (float) (lp.height-DensityUtil.dp2px(getContext(), LAYOUT_MARGIN_DIP * 2)) / bm.getHeight());
		imgIllust = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		if(imgIllust == null){
			return false;
		}
		bm.recycle();
		bm = null;
    	
		
		return true;
	}
	public boolean prepareIllustArrowImage(){
		android.view.ViewGroup.LayoutParams lp = illustArrow.getLayoutParams();
    	byte[] arrowBuffer = UIGuideControlJNI.getInstance().GetIllustInfo_ArrowImageBuffer();
		int arrowSize = UIGuideControlJNI.getInstance().GetIllustInfo_ArrowImageSize();
		Bitmap bm;
//    	byte illustBufferVertical[] = null;
//		int illustBufferSize = 0;
//		illustBufferVertical = guideControl.GetIllustInfo_BKImageBuffer();
//		illustBufferSize = guideControl.GetIllustInfo_BKImageSize();
		if (null == arrowBuffer 
				|| 0 >= arrowBuffer.length
				|| 0 >= arrowSize) 
		{
			return false;
		}

		if(null != imgIllustArrow && !imgIllustArrow.isRecycled()){
			imgIllustArrow.recycle();
			imgIllustArrow = null;
		}
		bm = BitmapFactory.decodeByteArray(arrowBuffer, 0, arrowSize);
		if(bm == null){
			return false;
		}
		Matrix matrix = new Matrix();
		matrix.postScale((float) (lp.width -DensityUtil.dp2px(getContext(), LAYOUT_MARGIN_DIP * 2))/ bm.getWidth(), (float) (lp.height-DensityUtil.dp2px(getContext(), LAYOUT_MARGIN_DIP * 2)) / bm.getHeight());
		imgIllustArrow  = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		if(imgIllustArrow == null){
			return false;
		}
		bm.recycle();
		bm = null;
		
		return true;
	}
	private void refreshIllustImage(){
		illustImage.setImageBitmap(null);
		illustArrow.setImageBitmap(null);
		
		illustImage.setImageBitmap(imgIllust);
		illustArrow.setImageBitmap(imgIllustArrow);
	}
	
	/**
	 * 
	 * must call after {@link #prepareIllustImage} and the prepare return true;
	 */
	public void showIllust(){
		refreshIllustImage();
		distanceNum = guideControl.GetDistanceInfo_LDistance();
		this.setVisibility(View.VISIBLE);
		updateLayout();
	}
	
	
	public boolean updateIllustImage(){
		boolean isImageReady = prepareIllustImage();
		boolean isArrowReady = prepareIllustArrowImage();
		if(isImageReady || isArrowReady){
			refreshIllustImage();
			return true;
		}else{
			return false;
		}
	}
	private void resizeLayout(){
		android.view.ViewGroup.LayoutParams lp = illustImage.getLayoutParams();
		android.view.ViewGroup.LayoutParams lp2 = getLayoutParams();
		android.view.ViewGroup.LayoutParams lp3 = progress.getLayoutParams();
		if(ScreenMeasure.isPortrait()){
			lp2.width = ScreenMeasure.getWidth();
			lp2.height = (ScreenMeasure.getHeight() / 2) - DensityUtil.dp2px(getContext(), GUIDE_BAR_HEIGHT_DIP);		
			
			lp.width = ScreenMeasure.getWidth()-DensityUtil.dp2px(getContext(), LAYOUT_MARGIN_DIP * 2);
			lp.height = (ScreenMeasure.getHeight() / 2) - DensityUtil.dp2px(getContext(), GUIDE_BAR_HEIGHT_DIP)-DensityUtil.dp2px(getContext(), LAYOUT_MARGIN_DIP * 2);
		}else{
			lp2.width = (ScreenMeasure.getWidth() / 2);
			lp2.height = ScreenMeasure.getHeight() - DensityUtil.dp2px(getContext(), GUIDE_BAR_HEIGHT_DIP - 5);

			lp.width = (ScreenMeasure.getWidth() / 2)-DensityUtil.dp2px(getContext(), LAYOUT_MARGIN_DIP * 2);
			lp.height = ScreenMeasure.getHeight() - DensityUtil.dp2px(getContext(), GUIDE_BAR_HEIGHT_DIP - 5)-DensityUtil.dp2px(getContext(), LAYOUT_MARGIN_DIP * 2);
		}
		lp3.height = lp.height - DensityUtil.dp2px(getContext(), 50);
		illustImage.setLayoutParams(lp);
		illustArrow.setLayoutParams(lp);
		setLayoutParams(lp2);
		requestLayout();
	}
	
	public boolean isIllustShowing(){
		return this.getVisibility() == View.VISIBLE;
	}
	
	
	public void hideIllust(){
		illustImage.setImageBitmap(null);
		illustArrow.setImageBitmap(null);
		
		if(null != imgIllust && !imgIllust.isRecycled()){
			imgIllust.recycle();
			imgIllust = null;
		}
		if(null != imgIllustArrow && !imgIllustArrow.isRecycled()){
			imgIllustArrow.recycle();
			imgIllustArrow = null;
		}
		this.setVisibility(View.GONE);
		updateLayout();
	}
	
}
