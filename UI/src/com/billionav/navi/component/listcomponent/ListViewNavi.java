package com.billionav.navi.component.listcomponent;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.billionav.navi.uitools.ReflectTools;
import com.billionav.ui.R;

public class ListViewNavi extends ListView {

	public ListViewNavi(Context context) {
		super(context);
		setCacheColorHint(0);
		setBackgroundColor(0);
	}

	public ListViewNavi(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCacheColorHint(0);
		setBackgroundColor(0);
	}

	public ListViewNavi(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCacheColorHint(0);
		setBackgroundColor(0);
	}

	/**
	 * return arrays of the position of checked Items
	 * */
	public List<Integer> getCheckedItems() {
		SparseBooleanArray checkstaus = getCheckedItemPositions();
		if (checkstaus == null) {
			return null;
		}

		ArrayList<Integer> checkedPostions = new ArrayList<Integer>();
		for (int i = 0; i < checkstaus.size(); i++) {
			if (checkstaus.valueAt(i)) {
				checkedPostions.add(checkstaus.keyAt(i));
			}
		}
		return checkedPostions;
	}

	public void setAllItemChecked(boolean checked) {
		int count = getCount();
		for (int i = 0; i < count; i++) {
			setItemChecked(i, checked);
		}
	}

	public int getCheckedCount() {
		return getCheckedItems().size();
	}

	public static class SimpleAdapterBuilder {
		//view layout id(view resource)
		int resource;
		//to control id in view 
		int[] ids;
		//from data resource
		String[] names;
		Object[] defaultValue;
		//data resource
		List<Map<String, Object>> listMap;
		SimpleAdapter adapter;

		private SparseArray<OnItemClickListener> itemElementClick;;

		public SimpleAdapterBuilder(int resource, int... ids) {
			super();
			this.resource = resource;
			this.ids = ids.clone();
			names = new String[ids.length];
			for (int i = 0; i < names.length; i++) {
				names[i] = "name_" + i;
			}

			listMap = new ArrayList<Map<String, Object>>();
		}

		public void setDefaultValues(Object... obj) {
			if (obj.length != names.length) {
				throw new RuntimeException("arguments number wrong, must be "
						+ names.length + ", now " + obj.length);
			}
			defaultValue = obj;
		}
		
		public Object[] getData(int index) {
			if(index >= listMap.size()) {
				throw new IndexOutOfBoundsException("list size = "+ listMap.size()+", index = "+ index);
			}
			Map<String, Object> map =  listMap.get(index);
			Object[] data = new Object[names.length];
			for (int i = 0; i < names.length; i++) {
				data[i] = map.get(names[i]);
			}

			return data;
		}

		public void set(int index, Object... obj) {
			if (obj.length != names.length) {
				throw new RuntimeException("arguments number wrong, must be "
						+ names.length + ", now " + obj.length);
			}

			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < names.length; i++) {
				if (obj[i] == null && defaultValue != null) {
					map.put(names[i], defaultValue[i]);
				} else {
					map.put(names[i], obj[i]);
				}
			}
			listMap.set(index, map);

		}

		public void put(Object... obj) {
			if (obj.length != names.length) {
				throw new RuntimeException("arguments number wrong, must be "
						+ names.length + ", now " + obj.length);
			}

			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < names.length; i++) {
				if (obj[i] == null && defaultValue != null) {
					map.put(names[i], defaultValue[i]);
				} else {
					map.put(names[i], obj[i]);
				}
			}
			listMap.add(map);

		}

		public void applyData(Object obj, String getCountName,
				String getNoteName, String... getdataNames) {
			Object o = ReflectTools.invokeMethod(obj, getCountName);
			if (o == null) {
				throw new NullPointerException("please check count name.");
			}

			int count = Integer.parseInt(o.toString());
			Method getNoteMethod = ReflectTools.invokeMethod(obj, getNoteName,
					int.class);

			for (int i = 0; i < count; i++) {
				Object note = ReflectTools.invokeMethod(obj, getNoteMethod, i);
				Object[] noteDatas = new Object[getdataNames.length];
				for (int j = 0; j < getdataNames.length; j++) {
					noteDatas[j] = ReflectTools.invokeMethod(note,
							getdataNames[j]);
				}
				put(noteDatas);
			}
		};

		public List<Map<String, Object>> getDataList() {
			return listMap;
		}

		public void notifyDataSetChanged() {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
		}
		
		public void setItemElementClickListener(int resId, OnItemClickListener l){
			ensureItemElementListener();
			itemElementClick.put(resId, l);
		}
		
		private void ensureItemElementListener() {
			if(itemElementClick == null) {
				itemElementClick = new SparseArray<AdapterView.OnItemClickListener>(3);
			}
		}

		public SimpleAdapter createSimpleAdapter(Context context) {
			if (this.adapter != null) {
				return this.adapter;
			}	
			SimpleAdapter adapter = new SimpleAdapter(context, listMap,
					resource, names, ids) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view = super.getView(position, convertView, parent);
					if (convertView == null) {
						view.setBackgroundResource(R.drawable.list_selector_background);
					}
					
					setPositionForClickElements(view, position, convertView == null);
					return view;
				}
				
			};
			adapter.setViewBinder(new MyViewBinder());
			return this.adapter = adapter;
		}
		
		public SimpleAdapter createPartcularSimpleAdapter(Context context) {
			if (this.adapter != null) {
				return this.adapter;
			}	
			SimpleAdapter adapter = new SimpleAdapter(context, listMap,
					resource, names, ids) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view = super.getView(position, convertView, parent);
					if (convertView == null) {
						view.setBackgroundResource(R.drawable.list_selector_background);
					}
					String name =view.getContext().getResources().getString(R.string.STR_1200000000);
					boolean isVisableCheckedBox = listMap.get(position).get("name_0").equals(name);
					if(isVisableCheckedBox){
						((MultiChoiceLayout)view).setVisableCheckedBox(false);
						//get rid of checked state for nationwide package.
						((ListView)parent).setItemChecked(position, false);
					}else{
						((MultiChoiceLayout)view).setVisableCheckedBox(true);
					}
					setPositionForClickElements(view, position, convertView == null);
					return view;
				}
				
			};
			adapter.setViewBinder(new MyViewBinder());
			return this.adapter = adapter;
		}

		private void createClickListener(View v) {
			if (itemElementClick != null) {
				int count = itemElementClick.size();
				for (int i = 0; i < count; i++) {
					int id = itemElementClick.keyAt(i);
					v.findViewById(id).setOnClickListener(
							new ItemElementClickListener(i));
				}
			}
		}

		private void setPositionForClickElements(View view, int position,
				boolean isFirst) {
			createClickListener(view);
			if (itemElementClick != null) {
				int count = itemElementClick.size();
				for (int i = 0; i < count; i++) {
					int id = itemElementClick.keyAt(i);
					ItemElementClickListener elementListener = (ItemElementClickListener) ReflectTools
							.getFiledValue(view.findViewById(id), "mOnClickListener");
					elementListener.setPosition(position);
				}
			}
		}

		private static class MyViewBinder implements ViewBinder {
			SimpleDateFormat sdf = null;

			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {

				if (view instanceof TextView) {
					if (data instanceof Integer) {
						((TextView) view).setText((Integer) data);
					} else if (data instanceof Date) {
						if (sdf == null) {
							sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
						}
						((TextView) view).setText(sdf.format((Date) data));
					} else {
						((TextView) view).setText(textRepresentation);
					}
					return true;
				} else if (view instanceof ImageView) {
					if (data instanceof Integer) {
						((ImageView) view).setImageResource((Integer) data);
						return true;
					} else if (data instanceof Bitmap) {
						((ImageView) view).setImageBitmap((Bitmap) data);
						return true;
					}
				}
				return false;
			}

		}

		private class ItemElementClickListener implements OnClickListener {
			private final int id;
			private int position;

			ItemElementClickListener(int id) {
				this.id = id;
			}

			@Override
			public void onClick(View v) {
				if (itemElementClick.get(id) != null) {
					itemElementClick.get(id).onItemClick(null, v, position, id);
				}
			}

			void setPosition(int position) {
				this.position = position;
			}

		}
	}

}
