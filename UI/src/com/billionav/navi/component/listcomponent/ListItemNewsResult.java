package com.billionav.navi.component.listcomponent;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.billionav.ui.R;
public class ListItemNewsResult extends ListItemInterface {
	private TextView textview01;
	private TextView textview02;
	
	public ListItemNewsResult(Context context,  String title,
			String descripiton) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_item_news, this);
		findViews();
		textview01.setText(title);
		textview02.setText(descripiton);		
		setBackgroundResource(R.drawable.list_selector_background);
		}
	
	private void findViews() {
		textview01 = (TextView) findViewById(R.id.list_Item_News_Title);
		textview02 = (TextView) findViewById(R.id.list_Item_News_Detail);
		}
	
	public void setInfo(String name, String address, String distance) {
		textview01.setText(name);
		textview02.setText(address);
		}
	
}
