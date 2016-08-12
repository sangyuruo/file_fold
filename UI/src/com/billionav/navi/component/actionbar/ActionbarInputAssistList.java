package com.billionav.navi.component.actionbar;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.component.listcomponent.ListItemTextWithoutImg;

public class ActionbarInputAssistList extends RelativeLayout{
	private ListView listView;
	private ArrayList<String> list = new ArrayList<String>();
	private BaseAdapter adapter = null;
	private ActionbarInputAssistList(Context context) {
		super(context);
		
		setPadding(DensityUtil.dp2px(context, 12), DensityUtil.dp2px(context, 70), DensityUtil.dp2px(context, 12), 0);
		listView = new ListView(context);
		addView(listView);
		listView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
		listView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		listView.requestLayout();
		listView.setDivider(new ColorDrawable(Color.BLUE));
		adapter = new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = new ListItemTextWithoutImg(getContext(), list.get(position));
				}
				else{
					((ListItemTextWithoutImg)convertView).setText(list.get(position));
				}
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};
		listView.setAdapter(adapter);
		hide();
	}
	
	public static ActionbarInputAssistList inflate(ViewGroup vg){
		ActionbarInputAssistList assistList = new ActionbarInputAssistList(vg.getContext());
		vg.addView(assistList, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		return assistList;
	}
	
	public void setOnItemClickListener(OnItemClickListener l){
		listView.setOnItemClickListener(l);
	}
	
	public void clear(){
		list.clear();
		adapter.notifyDataSetChanged();
	}
	
	public void setItems(String[] names) {
		list.clear();
		
		if(names == null){
			return;
		}
		
		for(String name: names){
			list.add(name);
		}
		adapter.notifyDataSetChanged();
	}
	
	public String getString(int index){
		return list.get(index);
	}
	
	public void setOnScrollListener(OnScrollListener l) {
		listView.setOnScrollListener(l);

	}
	
	public void show(){
		if(getVisibility() != View.VISIBLE){
			setVisibility(View.VISIBLE);
		}
	}
	
	public void hide(){
		setVisibility(View.GONE);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getY() > DensityUtil.dp2px(getContext(), 45)){
			setVisibility(View.GONE);
		}
		return super.onTouchEvent(event);
	}

}
