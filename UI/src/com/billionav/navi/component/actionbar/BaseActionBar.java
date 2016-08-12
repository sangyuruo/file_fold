package com.billionav.navi.component.actionbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;

public class BaseActionBar extends RelativeLayout{
	
	private TextView titleText;
	private TextView titleTextRight;
	
	private RelativeLayout contextLayout;
	
	private LinearLayout actionItemLayout;
	private LinearLayout actionLayout;
	
	private ProgressBar progress;
	
	private LinearLayout actionbarLayout;
	private RelativeLayout belowArea;
	
	private BackgroudSetting backgroud;

	public BaseActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	public BaseActionBar(Context context) {
		super(context);
		initialize();
	}
	
	private void initialize(){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.actionbar_base, this);
	    
	    findViews();
	    
	    actionLayout.setVisibility(View.GONE);

	    onPrepareContextView(contextLayout);
	    onPrepareActionView(actionbarLayout);
	    
	    
	}

	
	public int getActionbarHeight() {
		return DensityUtil.dp2px(getContext(), 49);
	}
	
	
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if(backgroud != null) {
			backgroud.setbackgroud();
		}
	}
	
	public final void setDefaultBackground() {
		setBackgroundResource(R.drawable.navicloud_and_749a);
	}
	
	public final void setHasBackgroud(boolean has) {
		if(has) {
			setDefaultBackground();
		} else {
			backgroud = null;
			setBackgroundDrawable(null);
		}
	}
	
	public final void setBackgroundResource(int portImage, int LandImage) {
		backgroud = new BackgroudSetting(portImage, LandImage);
		backgroud.setbackgroud();
	}

	private void findViews() {
		titleText = (TextView) findViewById(R.id.title_text);
		titleTextRight = (TextView) findViewById(R.id.title_text_right);
		contextLayout = (RelativeLayout) findViewById(R.id.context_layout);
		
		actionItemLayout = (LinearLayout) findViewById(R.id.action_item_layout);
		actionLayout = (LinearLayout) findViewById(R.id.action_layout);
		
		progress = (ProgressBar) findViewById(R.id.actionbar_progressbar);
		
		actionbarLayout = (LinearLayout) findViewById(R.id.area_acition);
		belowArea = (RelativeLayout) findViewById(R.id.area_below);
	}
	
	public boolean notifyTriggerReceived(NSTriggerInfo triggerInfo){
		return false;
	}

	
	/**
	 * set text
	 * */
	public final void setText(int resId){		
		titleText.setText(resId);
	}
	
	/**
	 * set text
	 * */
	public final void setText(CharSequence text){
		titleText.setText(text);
	}
	public final void setTextRight(int resId){
		titleTextRight.setText(resId);
	}
	public final void setTextRight(CharSequence text){
		titleTextRight.setText(text);
	}
	
	private void setActionLayoutVisible() {
		if(actionLayout.getVisibility() != View.VISIBLE) {
			actionLayout.setVisibility(View.VISIBLE);
		}
	}
	private void setActionLayoutInVisible(){
		if(actionLayout.getVisibility() != View.GONE) {
			actionLayout.setVisibility(View.GONE);
		}
	}
	
	public final void showProgress() {
		setActionLayoutVisible();
		actionItemLayout.setVisibility(GONE);
		progress.setVisibility(VISIBLE);
	}
	
	public final void dismissProgress() {
		if(actionItemLayout.getChildCount() == 0){
			setActionLayoutInVisible();
		}
		progress.setVisibility(GONE);
		progress.setBackgroundResource(0);
		actionItemLayout.setVisibility(VISIBLE);
	}
	
	
	/**
	 * add item for action bar
	 * */
	public final void addActionItem(int imageId, OnClickListener l){
		ActionbarItem item = new ActionbarItem(getContext(), imageId, l);
		item.addToLinearLayout(actionItemLayout);
		setActionLayoutVisible();
	}
	
	/** set action bar enable */
	public final void setActionItemEnable(int index, boolean enabled) {
		getActionItem(index).setEnabled(enabled);
	}
	
	/** for more advanced use*/
	public final void addActionItem(StateListDrawable drawable, OnClickListener l) {
		ActionbarItem item = new ActionbarItem(getContext(), drawable, l);
		item.addToLinearLayout(actionItemLayout);
		setActionLayoutVisible();
	}
	/** for more advanced use*/
	@SuppressWarnings("unchecked")
	public final <T extends View> T getActionItem(int index) {
		if(index <0 || index > actionItemLayout.getChildCount()){
			return null;
		}
		return (T) actionItemLayout.getChildAt(index);
	}
	
	public final void addActionItem2(int imageId, String text, OnClickListener l){
		ActionBarItem2 item = new ActionBarItem2(getContext(), imageId, text, l);
		item.addToLinearLayout(actionItemLayout);
		setActionLayoutVisible();
	}
	
	public final void addActionItem2(int imageId, int textid, OnClickListener l){
		ActionBarItem2 item = new ActionBarItem2(getContext(), imageId, textid, l);
		item.addToLinearLayout(actionItemLayout);
		setActionLayoutVisible();
	}
	
	public final void addActionItem3(int textid, OnClickListener l){
		ActionBarItem3 item = new ActionBarItem3(getContext(), textid, l);
		item.addToLinearLayout(actionItemLayout);
		setActionLayoutVisible();
	}

	public final void addActionItem3(String text, OnClickListener l){
		ActionBarItem3 item = new ActionBarItem3(getContext(), text, l);
		item.addToLinearLayout(actionItemLayout);
		setActionLayoutVisible();
	}

	
	
	/**
	 * set no action bar
	 * */
	public final void setNoTitle(){
		actionbarLayout.setVisibility(View.GONE);
	}
		
	protected void onPrepareContextView(ViewGroup decView){
	}
	protected void onPrepareActionView(ViewGroup decView) {
	}
	public final void addViewToBelowView(int layoutResID) {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(layoutResID, belowArea);
	}
	
	public final void addViewToBelowViewForPreferemce(int layoutResID) {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = inflater.inflate(layoutResID, belowArea);
	    
	    //for pioneer phone
	    try{
		    view = ((ViewGroup)view).getChildAt(0);
		    view = (((ViewGroup)view).getChildAt(0));
		    view = (((ViewGroup)view).getChildAt(0));
		    view.setPadding(0, 0, 0, 0);
	    } catch (Exception e) {
	    	Log.d("PHONE", "Prefence layout no match.");
	    	e.printStackTrace();
		}
	    
	}
	
	public final void addViewToBelowView(View v) {
	    belowArea.addView(v);
	}
	
	public final RelativeLayout getBelowLayout(){
		return belowArea;
	}
	
	
	private class BackgroudSetting{
		final int portrait;
		final int landscape;
		
		public BackgroudSetting(int portrait, int landscape) {
			super();
			this.portrait = portrait;
			this.landscape = landscape;
		}

		void setbackgroud() {
			setBackgroundResource(ScreenMeasure.isPortrait() ? portrait : landscape);
		}
	}

	public void setTitleBackgroundBlack() {
		findViewById(R.id.area_acition).setBackgroundResource(R.drawable.navicloud_and_702a);
		LinearLayout ll = (LinearLayout)findViewById(R.id.area_acition);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)ll.getLayoutParams();
		lp.height = DensityUtil.dp2px(getContext(), 33);
		findViewById(R.id.area_acition).setLayoutParams(lp);
	}
	public void addViewToContextView(View v){
		contextLayout.addView(v);
	}


	
}
