package com.billionav.navi.component.dialog;

import java.io.UnsupportedEncodingException;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.jni.UIVoiceControlJNI;
import com.billionav.navi.component.ViewHelp;
import com.billionav.navi.component.dialog.ClutchView.OnChangedListener;
import com.billionav.ui.R;

public class CustomDialog extends Dialog{
	private TextView title;
	private RelativeLayout contextLayout;
	private TextView message;
	
	private LinearLayout buttonLayout;
	private Button button1;
	private Button button2;
	
	private EditText input;
	private TextView input_max_length_tip;
	
	private LinearLayout checkboxLayout;
	private TextView checkTextView;
	private CheckBox checkbox;
	private ClutchView clutchView;

	public AlertParams P;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(P.backAllowClose){
				dismiss();
			}
			return true;
		} else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
			UIVoiceControlJNI.getInstance().volumeDown();
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
			UIVoiceControlJNI.getInstance().volumeUP();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public CustomDialog(Context context) {
		this(context, R.style.Theme_CustomDialog);
		// TODO Auto-generated constructor stub
//		P = new AlertParams(getContext());
//		this.setContentView(R.layout.dialog_view);
//		findViews();
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		P = new AlertParams(getContext());
		this.setContentView(R.layout.dialog_view);
		findViews();


	}
	private void findViews() {
		title = (TextView) findViewById(R.id.title_view);
		contextLayout = (RelativeLayout) findViewById(R.id.content_layout);
		message = (TextView) findViewById(R.id.message_view);
		buttonLayout = (LinearLayout) findViewById(R.id.button_layout);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		
		input = (EditText) findViewById(R.id.input);
		input_max_length_tip = (TextView) findViewById(R.id.input_max_length_tip);
		
		checkboxLayout = (LinearLayout) findViewById(R.id.checkboxLayout);
		checkTextView = (TextView) findViewById(R.id.checkbox_textview);
		checkbox = (CheckBox) findViewById(R.id.checkbox_imageview);
		clutchView = (ClutchView) findViewById(R.id.clutch_view);
		
	}
	@Override
	public boolean onSearchRequested() {
		// TODO Auto-generated method stub
		return true;
	}
	private void apply(final AlertParams p) {
		if(TextUtils.isEmpty(p.mTitle)) {
			title.setVisibility(View.GONE);
		} else {
			title.setText(p.mTitle);
		}
		
		if(TextUtils.isEmpty(p.mMessage)) {
			message.setVisibility(View.GONE);
		} else {
			message.setText(p.mMessage);
		}
		
		if(TextUtils.isEmpty(p.mPositiveButtonText) && TextUtils.isEmpty(p.mNegativeButtonText)) {
			buttonLayout.setVisibility(View.GONE);
		} else {
			if(TextUtils.isEmpty(p.mPositiveButtonText)) {
				button2.setVisibility(View.GONE);
			} else {
				button2.setVisibility(View.VISIBLE);
				button2.setText(p.mPositiveButtonText);
				button2.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dismiss();
						if(p.mPositiveButtonListener != null) {
							p.mPositiveButtonListener.onClick(CustomDialog.this, 0);
						}
					}

				});
			}
			
			
			if(TextUtils.isEmpty(p.mNegativeButtonText)) {
				button1.setVisibility(View.GONE);
			} else {
				button1.setText(p.mNegativeButtonText);
				button1.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dismiss();
						if(p.mNegativeButtonListener != null) {
						   p.mNegativeButtonListener.onClick(CustomDialog.this, 0);
						}
						if(!TextUtils.isEmpty(p.mPositiveButtonText)) {
							p.recovery();
						}
					}

				});
			}
		}
		
		if(p.mEditText == null) {
			input.setVisibility(View.GONE);
		} else {
			input.setText(p.mEditText);
			input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(p.edit_text_max_length + 1)});
			input.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					String tempString = "";
					p.mEditText = s;
					try {
						tempString = new String(s.toString().trim().getBytes("GBK"), "ISO8859_1");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if(tempString.length() > p.edit_text_max_length) {
						p.mEditText = s.subSequence(0, s.length()-1);
						input_max_length_tip.setText(getContext().getString(R.string.MSG_02_03_01_02));
						input_max_length_tip.setVisibility(View.VISIBLE);
						input.setText(p.mEditText);
						input.setSelection(p.mEditText.length());
					} else if(tempString.length() < p.edit_text_max_length){
						input_max_length_tip.setVisibility(View.GONE);
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			input.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					ViewHelp.showInputSoftKeyBoard(input);
					
				}
			}, 300);
		}
		
		if(p.mCheckText == null) {
			checkboxLayout.setVisibility(View.GONE);
		} else {
			checkTextView.setText(p.mCheckText);
			checkbox.setChecked(p.mCheckboxChecked);
			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					p.mCheckboxChecked = isChecked;
					
				}
			});
		}
		
		if(p.mClutchStep <= 0) {
			clutchView.setVisibility(View.GONE);
		} else {
			clutchView.setCommonInfo(p.mClutchValue, p.mClutchStep, p.mClutchMax, p.mClutchMin, p.mClutchUnit);
			clutchView.setOnChangedListener(new OnChangedListener() {
				
				@Override
				public void onChange(float value) {
					p.mClutchValue = value;
					
				}
			});
		}
		
		if(p.mItems != null) {
			View listView = createListView(p);
			if(listView != null) {
				contextLayout.addView(listView);
			}
		}
		
//		if(p.mViewAdder != null) {
//			contextLayout.addView(p.mViewAdder.createView(this));
//		}
		
	}
	
	private View createListView(final AlertParams p) {
		final ListView listView = new ListView(getContext());
		ArrayAdapter<CharSequence> adapter;
		if (p.mIsMultiChoice) {
			adapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.dialog_item_select_multichoice, R.id.text1,  p.mItems){
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view = super.getView(position, convertView, parent);
					if(p.mCheckedItems != null) {
						if(p.mCheckedItems[position]) {
							listView.setItemChecked(position, true);
						}
					}
					return view;
				}
			};
		} else {
            final int layout = p.mIsSingleChoice 
                    ? R.layout.dialog_item_select_singlechoice : R.layout.dialog_item_select;
            adapter = new ArrayAdapter<CharSequence>(getContext(), layout, R.id.text1, p.mItems) ;
            new Handler(){
            	@Override
            	public void handleMessage(Message msg) {
            		if (p.mCheckedItem > -1) {
            			listView.setItemChecked(p.mCheckedItem, true);
            			listView.setSelection(p.mCheckedItem);
                    }
            	}
            }.sendEmptyMessage(1);
		}
		
		listView.setAdapter(adapter);
		
		
		if (p.mOnClickListener != null) {
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    p.mOnClickListener.onClick(CustomDialog.this, position);
                    if (!p.mIsSingleChoice) {
                        dismiss();
                    } else {
                    	p.mCheckedItem = position;
                    }
                }
            });
        } else if (p.mOnCheckboxClickListener != null) {
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    if (p.mCheckedItems != null) {
                        p.mCheckedItems[position] = listView.isItemChecked(position);
                    }
                    p.mOnCheckboxClickListener.onClick(
                    		CustomDialog.this, position, listView.isItemChecked(position));
                }
            });
        }

        if (p.mIsSingleChoice) {
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        } else if (p.mIsMultiChoice) {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }
        listView.setDividerHeight(0);
		return listView;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		apply(P);
	}
	
	public boolean[] getCheckedItems() {
		return P.mCheckedItems;
	}
	
	/**
	 * for multichoices list;
	 * */
	public void setCheckedItems(boolean[] checkItems) {
		P.mCheckedItems = checkItems.clone();
	}

	public CharSequence getTitle() {
		return P.mTitle;
	}

	public void setTitle(String title) {
		P.mTitle = title;
	}

	public void setTitle(int titleId) {
		P.mTitle = P.mContext.getText(titleId);
	}

	public CharSequence getMessage() {
		return P.mMessage;
	}

	public void setMessage(String message) {
		P.mMessage = message;
	}
	public void setMessage(CharSequence message) {
		P.mMessage = message;
	}
	public void setMessage(int messageId) {
		P.mMessage = P.mContext.getText(messageId);
	}

	public void setOnKeyListener(OnKeyListener mOnKeyListener) {
		P.mOnKeyListener = mOnKeyListener;
		super.setOnKeyListener(mOnKeyListener);
	}

	public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
		P.mOnDismissListener = dismissListener;
		super.setOnDismissListener(dismissListener);
	}

	public void setOnCancelListener(DialogInterface.OnCancelListener cancelListener) {
		P.mOnCancelListener = cancelListener;
		super.setOnCancelListener(cancelListener);
	}
	
    public void setPositiveButton(int textId, final OnClickListener listener) {
    	P.mPositiveButtonText = P.mContext.getText(textId);
        P.mPositiveButtonListener = listener;
    }
    
    public void setPositiveButton(CharSequence text, final OnClickListener listener) {
    	P.mPositiveButtonText = text;
        P.mPositiveButtonListener = listener;
    }
    
    public void setNegativeButton(int textId, final OnClickListener listener) {
    	P. mNegativeButtonText = P.mContext.getText(textId);
    	P.mNegativeButtonListener = listener;
    }
    
    public void setNegativeButton(CharSequence text, final OnClickListener listener) {
        P.mNegativeButtonText = text;
        P.mNegativeButtonListener = listener;
    }
    
    public void setItems(CharSequence[] items, final OnClickListener listener) {
        P.mItems = items;
        P.mOnClickListener = listener;
    }
    
    public void setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, 
            final OnMultiChoiceClickListener listener) {
        P.mItems = items;
        P.mOnCheckboxClickListener = listener;
        P.mCheckedItems = checkedItems;
        P.mIsMultiChoice = true;
    }

    public void setSingleChoiceItems(CharSequence[] items, int checkedItem, final OnClickListener listener) {
        P.mItems = items;
        P.mOnClickListener = listener;
        P.mCheckedItem = checkedItem;
        P.mIsSingleChoice = true;
    } 
    
    public void setEditText(int textId) {
    	P.mEditText = P.mContext.getText(textId);
    }
    
    public void setEditText(CharSequence text) {
    	P.mEditText = text;
    }
    
    public void setEditTextMaxLength(int maxLength) {
    	P.edit_text_max_length = maxLength;
    }
    
    public CharSequence getEditText() {
    	return P.mEditText;
    }
    
    public void setCheckBox(CharSequence text, boolean isChecked) {
    	P.mCheckText = text;
    	P.mCheckboxChecked = isChecked;
    }
    
    public void setCheckBox(int textid, boolean isChecked) {
    	P.mCheckText = P.mContext.getText(textid);
    	P.mCheckboxChecked = isChecked;
    }
    
    public boolean isChecked(){
    	return P.mCheckboxChecked;
    }
    
    public void setEnterBackKeyAllowClose(boolean allowClose){
    	this.P.backAllowClose = allowClose;
    }
    
    public void setClutchInfo(float value, float step, float max, float min, String unit) {
    	P.mClutchValue = value;
    	P.mClutchStep = step;
    	P.mClutchMax = max;
    	P.mClutchMin = min;
    	P.mClutchUnit = unit;
    }
    
    public float getClutchValue() {
    	return P.mClutchValue;
    }
    
    public void addView(View view) {
//        P.mViewAdder = view;
    	contextLayout.addView(view);
    }
    
    /*package*/ static class AlertParams{
    	public boolean isShowing;
		public final Context mContext;
        public DialogInterface mDialog;
        
        public CharSequence mTitle;
        public CharSequence mMessage;
        public CharSequence mPositiveButtonText;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public CharSequence[] mItems;
        public DialogInterface.OnClickListener mOnClickListener;
        public boolean[] mCheckedItems;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public int mCheckedItem = -1;
        public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        
        public CharSequence mEditText;
        public int edit_text_max_length = 60;
        
        public boolean mCheckboxChecked;
        public CharSequence mCheckText;
        private boolean backAllowClose = false;
        
        public String mClutchUnit;
        public float mClutchValue;
        public float mClutchStep;
        public float mClutchMax;
        public float mClutchMin;

        
        public AlertParams(Context context) {
        	isShowing = false;
            mContext = context;
//            mCancelable = true;
            mClutchStep = -1;
        }
        
        private boolean[] mCheckedItemsForTemp;
        private int mCheckedItemForTemp = -1;
        private boolean mCheckboxCheckedForTemp;
        private CharSequence mEditTextForTemp;
        private float mClutchValueForTemp;
        
        public void store() {
        	if(mCheckedItems != null) {
            	mCheckedItemsForTemp = mCheckedItems.clone();
        	}
        	mCheckedItemForTemp = mCheckedItem;
        	mCheckboxCheckedForTemp = mCheckboxChecked;
        	mEditTextForTemp = mEditText;
        	mClutchValueForTemp = mClutchValue;
        }
        
        public void recovery() {
        	if(mCheckedItemsForTemp != null) {
        		mCheckedItems = mCheckedItemsForTemp.clone();
        	}
        	mCheckedItem = mCheckedItemForTemp;
        	mCheckboxChecked = mCheckboxCheckedForTemp;
        	mEditText = mEditTextForTemp;
        	mClutchValue = mClutchValueForTemp;
        }
            
    }
    
    public void requestSoftKeyboard(Context c) {
		InputMethodManager im = ((InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE));
		im.showSoftInput( input, InputMethodManager.SHOW_FORCED);
    }
}


