package com.billionav.navi.component.tab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.billionav.ui.R;

public class TabHostNavi extends RelativeLayout {
	
	private ImageView		leftIcon;
	private ImageView		midIcon;
	private ImageView		rightIcon;
	
	private final int[] leftIconImage = new int[2];
	private final int[] midIconImage = new int[2];
	private final int[] rightIconImage = new int[2];
	
	private OnTabItemClickListener listener;
	
	private RelativeLayout contextLayout;
	
	private int 			selectIndex;
	
	public TabHostNavi(Context context) {
		super(context);
		initialize();
	}
	
	public TabHostNavi(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
		
	}
	
	public void initialize(){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tab_host_navi, this);
		findViews();
		
	}

	public void addItem(int imageId, int nameId, View contextView){
		TabHostItem item = new TabHostItem(getContext(), imageId, nameId);
		addItem(item,contextView);
	}
	
	public void addItem(int imageId, View contextView){
		TabHostItem item = new TabHostItem(getContext(), imageId);
		addItem(item,contextView);
	}
	
	public void addContentView(View contentView){
		contextLayout.addView(contentView);
	}
	
	private void findViews(){
		leftIcon = (ImageView) findViewById(R.id.reb_left_icon);
		midIcon = (ImageView) findViewById(R.id.reb_mid_icon);
		rightIcon = (ImageView) findViewById(R.id.reb_right_icon);
		
		leftIcon.setOnClickListener(itemClickListener);
		midIcon.setOnClickListener(itemClickListener);
		rightIcon.setOnClickListener(itemClickListener);
		
		contextLayout = (RelativeLayout)findViewById(R.id.tab_host_navi_context_layout);
	}
	
	public void setSelection(int index){
		selectIndex = index;
		leftIcon.setImageResource(selectIndex == 0 ? leftIconImage[1]:leftIconImage[0]);
//		leftIcon.setBackgroundResource(selectIndex == 0 ? R.drawable.jaguar_and_761b : 0);
		midIcon.setImageResource(selectIndex == 1 ? midIconImage[1]:midIconImage[0]);
//		midIcon.setBackgroundResource(selectIndex == 1 ? R.drawable.jaguar_and_761b : 0);
		rightIcon.setImageResource(selectIndex == 2 ? rightIconImage[1]:rightIconImage[0]);
//		rightIcon.setBackgroundResource(selectIndex == 2 ? R.drawable.jaguar_and_761b : 0);
	}
	
	public void addItem(TabHostItem item, View contextView){
		contextLayout.addView(contextView);
	}
	
	public void setItemEnable(int index, boolean enabled){
		if(index == 0){
			leftIcon.setEnabled(enabled);
		}else if(index == 1){
			midIcon.setEnabled(enabled);
		}else if(index == 2){
			rightIcon.setEnabled(enabled);
		}
	}
	
	public void setTabItemImage(int index, int normalId, int selectId){
		if(index == 0){
			leftIconImage[0] = normalId;
			leftIconImage[1] = selectId;
		}else if(index == 1){
			midIconImage[0] = normalId;
			midIconImage[1] = selectId;
		}else if(index == 2){
			rightIconImage[0] = normalId;
			rightIconImage[1] = selectId;
		}
	}
	
	public void setTabItemImage(int index, Drawable d){
		if(index == 0){
			leftIcon.setImageDrawable(d);
		}else if(index == 1){
			midIcon.setImageDrawable(d);
		}else if(index == 2){
			rightIcon.setImageDrawable(d);
		}
	}
	
	public void setOnTabItemClickListener(OnTabItemClickListener l){
		listener = l;
	}
	
	private OnClickListener itemClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int index = 0;
			if(v == leftIcon){
				index = 0;
			}else if(v == midIcon){
				index = 1;
			}else if(v == rightIcon){
				index = 2;
			}
			setSelection(index);
			if(listener != null){
				listener.onTabItemClicked(index);
			}
			
		}
	};
	
	public interface OnTabItemClickListener{
		public void onTabItemClicked(int index); 
	}
}
