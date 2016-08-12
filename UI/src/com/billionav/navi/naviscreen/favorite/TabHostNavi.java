package com.billionav.navi.naviscreen.favorite;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.billionav.navi.component.actionbar.ActionbarInput;
import com.billionav.navi.component.tab.TabHostItem;
import com.billionav.navi.naviscreen.download.TabHostListContainer;
import com.billionav.ui.R;

public class TabHostNavi extends RelativeLayout {
	private LinearLayout itemLayout;
	private RelativeLayout contextLayout;
	private TabHostListContainer listContainer = null;
	private ActionbarInput searchinput = null;
	private final ArrayList<TabHostItem> items = new ArrayList<TabHostItem>();
	public TabHostNavi(Context context, TabHostListContainer fl, ActionbarInput searchinput) {
		super(context);
		listContainer = fl;
		this.searchinput = searchinput;
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.adt_favorite_tabhostnavi, this);
		findViews();
		contextLayout.addView(fl);
	}
	
	public TabHostNavi(Context context, TabHostListContainer fl) {
		super(context);
		listContainer = fl;
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.adt_favorite_tabhostnavi, this);
		findViews();
		contextLayout.addView(fl);
	}
	
	public void setSearchinput(ActionbarInput searchinput){
		this.searchinput = searchinput;
	}
	
	public void addItem(int imageId, int nameId,int normalbackgroundId ,int pressedbackgroundId){
		TabHostItem item = new TabHostItem(getContext(), imageId, nameId, normalbackgroundId, pressedbackgroundId);
		addItem(item);
	}
	
	public void addItem(int imageId,int normalbackgroundId ,int pressedbackgroundId){
		TabHostItem item = new TabHostItem(getContext(), imageId, normalbackgroundId, pressedbackgroundId);
		addItem(item);
	}

	private void findViews(){
		itemLayout = (LinearLayout)findViewById(R.id.favorite_tab_host_navi_item_layout);
		contextLayout = (RelativeLayout)findViewById(R.id.favorite_tab_host_navi_context_layout);
	}
	public void dismissTabhost(){
		itemLayout.setVisibility(View.GONE);
	}
	public void addItem(TabHostItem item){
		itemLayout.addView(item);
		((LinearLayout.LayoutParams)item.getLayoutParams()).weight = 1;
		
		items.add(item);
		item.setOnClickListener(new OnItemClickListener(items.size()-1));
//		contextViews.add(contextView);
//		contextView.setVisibility(View.GONE);
		if(items.size() == 1){
			selected(0);
		}
	}
	
	public void setItemEnable(int index, boolean enable){
		items.get(index).setEnabled(enable);
	}

	private void selected(int index){
		for(TabHostItem item: items){
			item.setItemSelected(false);
		}
		items.get(index).setItemSelected(true);
	}
	
	private boolean isEnabled = true;
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	private class OnItemClickListener implements OnClickListener{
		private final int index;
		private OnItemClickListener(int i){
			index = i;
		}
		//switch tab
		@Override
		public void onClick(View v) {
			if(!isEnabled){
				return;
			}
			listContainer.setCurrentListState(index);
//			faverate_listContainer.deleteSelect();
//			listContainer.refresh("");
			searchinput.clearEditText();
			selected(index);
		}
		
	}
}
