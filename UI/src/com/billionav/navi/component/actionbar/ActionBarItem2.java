package com.billionav.navi.component.actionbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.ui.R;
import com.billionav.navi.component.ViewHelp;

public class ActionBarItem2 extends RelativeLayout  implements ActionItemInterface{

	public ActionBarItem2(Context context, int imageId, int textId, OnClickListener l) {
		this(context, imageId, context.getString(textId), l);
		setLayoutParam();
	}

	public ActionBarItem2(Context context, int imageId, String text, OnClickListener l) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.actionbar_item, this);
	    setBackgroundDrawable(getActionItemPressSelector());
	    setOnClickListener(l);
	    ImageView iv = (ImageView) findViewById(R.id.actionbar_item_image);
	    TextView tv = (TextView) findViewById(R.id.actionbar_item_text);
	    
	    iv.setImageResource(imageId);
	    tv.setText(text);
	    setLayoutParam();
	}
	private void setLayoutParam() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.weight = 10;
		lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
		setLayoutParams(lp);
	}
	public void addToLinearLayout(LinearLayout l) {
//		if(l.getChildCount() != 0){
//			l.addView(getDividerImageView());
//		}
		l.addView(this);
	}
	private Drawable getActionItemPressSelector() {
		return ViewHelp.createDrawableListByImageID(getContext(), -1, R.drawable.navicloud_and_726_02b);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		final int alpha = enabled ? 255 : 100;
		((ImageView)findViewById(R.id.actionbar_item_image)).getDrawable().setAlpha(alpha);
		int textColor = ((TextView) findViewById(R.id.actionbar_item_text)).getCurrentTextColor();
		textColor = textColor & 0x00ffffff | (alpha<<24);
		((TextView) findViewById(R.id.actionbar_item_text)).setTextColor(textColor);
	}

}
