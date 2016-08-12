package com.billionav.navi.component.actionbar;

import com.billionav.navi.component.actionbar.ActionBarInputItem.OnSearchButtonListener;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class ActionbarInput extends BaseActionBar {

	protected ActionBarInputItem inputItem;
	protected ActionbarInputAssistList assistList;
	
	public ActionbarInput(Context context) {
		super(context);
		initialize();
	}
	
	public ActionbarInput(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}
	
	private void initialize() {
		assistList = ActionbarInputAssistList.inflate(this);
	}
	
	public void showKeyBoard() {
		inputItem.showInputSoftKeyBoard();
	}
	public String getText(){
		return inputItem.getText();
	}
	public void closeInputKeyBoard() {
		inputItem.closeInputKeyBoard();
	}
	@Override
	protected void onPrepareContextView(ViewGroup decView) {
		decView.addView(inputItem = new ActionBarInputItem(getContext()));
	}
	
	public void setEditText(String name){
		inputItem.setEditText(name);
	}
	
	public void setSearchButtonListener(OnSearchButtonListener l){
		inputItem.setSearchButtonListener(l);
	}
	
	public void addTextChangedListener(TextWatcher tw){
		inputItem.addTextChangedListener(tw);
	}
	
	public void setOnEditorActionListener(OnEditorActionListener l) {
		inputItem.setOnEditorActionListener(l);
	}
	
	public void setOnItemClickListener(OnItemClickListener l){
		assistList.setOnItemClickListener(l);
	}
	
	public void setOnScrollListener(OnScrollListener l) {
		assistList.setOnScrollListener(l);

	}
	public void clear(){
		assistList.clear();
	}
	
	public void setItems(String[] names) {
		assistList.setItems(names);
	}
	
	public void setDropdownInputbox(boolean iseject)
	{
		 inputItem.setDropdownInputbox(iseject);
	}
	public void clearEditText()
	{
		inputItem.clearEditText();
	}


}
