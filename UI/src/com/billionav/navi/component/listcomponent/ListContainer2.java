package com.billionav.navi.component.listcomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

public class ListContainer2 extends ListContainer {
	public ListContainer2(Context context) {
		super(context);
		initialize();
	}
	public ListContainer2(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	private void initialize() {
		setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		setOnItemClickListener(null);
	}

	public void setOnItemClickListener(OnItemClickListener l) {
		listView.setOnItemClickListener(l);
	}

	public void removeItem(int index) {
		layersItemList.remove(index);
		myAdapter.notifyDataSetChanged();
	}

}
