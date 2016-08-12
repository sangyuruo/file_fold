package com.billionav.navi.component.actionbar;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView.OnEditorActionListener;

import com.billionav.ui.R;

public class ActionBarInputItem extends RelativeLayout {
	
	private ImageView searchButton;
	private EditText editText;
//	private ImageView deleteButton;
	
	private OnSearchButtonListener searchlistener;
	
	
	public ActionBarInputItem(Context context) {
		super(context);
		initialize();
	}

	public ActionBarInputItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
		
	}
	
	private void initialize() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.actionbar_input_item, this);
	    findViews();
		
	    setListener();
	    
	}

	private void findViews() {
		searchButton = (ImageView) findViewById(R.id.actionbar_input_searsh);
	    editText = (EditText) findViewById(R.id.actionbar_input_edit_text);
//	    deleteButton = (ImageView) findViewById(R.id.actionbar_input_delete);
	}
	
	private void setListener() {
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(searchlistener != null){
					searchlistener.onSearchButtonClicked(v, editText.getText().toString());
				}
			}
		});
		editText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
					closeInputKeyBoard();
				}
				return false;
			}
		});
	}
	public void setSearchButtonOnclickListener(OnClickListener l){
		searchButton.setOnClickListener(l);
	}
	public void setEditTextOnTextChangedListener(TextWatcher l){
		editText.addTextChangedListener(l);
	}
	public void clearEditText()
	{
		editText.setText("");
	}
	public void setSearchButtonListener(OnSearchButtonListener l){
		searchlistener = l;
	}
	
	public void addTextChangedListener(TextWatcher tw){
		editText.addTextChangedListener(new EditTextWatcher(tw));
	}
	
	public void setOnEditorActionListener(OnEditorActionListener l) {
		editText.setOnEditorActionListener(l);
	}
	
	public void setEditText(String name){
		editText.setText(name);
	}
	
	public void setEidtSelection(int index){
		editText.setSelection(index);
	}
	
	public String getText(){
		return editText.getText().toString();
	}

	
	public final void showInputSoftKeyBoard() {
	    //show soft key board
		editText.setFocusable(true);
		editText.requestFocus();
	    ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
	}
	
	public final void closeInputKeyBoard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	public static interface OnSearchButtonListener{
		void onSearchButtonClicked(View view, String inputText);
	}
	
	
	private class EditTextWatcher implements TextWatcher{
		private final TextWatcher t;
		
		private EditTextWatcher(TextWatcher t){
			this.t = t;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			t.beforeTextChanged(s, start, count, after);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			t.onTextChanged(s, start, before, count);
		}

		@Override
		public void afterTextChanged(Editable s) {
			t.afterTextChanged(s);
//			
//			if(TextUtils.isEmpty(s)){
//				searchButton.setEnabled(false);
//			} else {
//				searchButton.setEnabled(true);
//			}
		}
		
	}
	public void setDropdownInputbox(boolean iseject)
	{
		editText.setFocusable(iseject);
		editText.setFocusableInTouchMode(iseject);
	}

	public void setSearchButtonEnabled(boolean b) {
		searchButton.setEnabled(b);
		
	}
}
