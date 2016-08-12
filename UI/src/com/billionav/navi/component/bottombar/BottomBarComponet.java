package com.billionav.navi.component.bottombar;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class BottomBarComponet extends BaseBottomBar {
	private onItemClickListener listener;
	private int index;
	public BottomBarComponet(Context context){
		super(context);
		index = 0;
		createControl();

		setClickable(true);
	}
	public BottomBarComponet(Context context,AttributeSet attr){
		super(context,attr);
		createControl();
		
		setClickable(true);
	}
	
	protected void createControl(){
		
	}
	
	public void addChild(View view){
		BaseBottomBar.LayoutParams lp = new BaseBottomBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		addChild(view, lp);

	}
	
	public void addChild(View view,BaseBottomBar.LayoutParams lp){
		addView(view, lp);
		if(lp.comline || lp.rowline ){
			return;
		}
		view.setId(index++);
		
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onClick(v, v.getId());
				}
			}
		});
	}
	
	public void addChildOnly(View view){
		BaseBottomBar.LayoutParams lp = new BaseBottomBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		addView(view, lp);
	}
	
	public static interface onItemClickListener{
		void onClick(View view,int index);
	}
	
	
	public void setItemClickListener(onItemClickListener listener){
		this.listener = listener;
	}
	
	
}


 