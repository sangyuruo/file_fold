package com.billionav.navi.component.mapcomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billionav.ui.R;

public class SingleBottomButton extends RelativeLayout{
	public static final int BUTTON_STYLE_START_POINT = 1;
	public static final int BUTTON_STYLE_WAY_POINT_1 = 2;
	public static final int BUTTON_STYLE_WAY_POINT_2 = 3;
	public static final int BUTTON_STYLE_WAY_POINT_3 = 4;
	public static final int BUTTON_STYLE_WAY_POINT_4 = 5;
	public static final int BUTTON_STYLE_WAY_POINT_5 = 6;
	public static final int BUTTON_STYLE_WAY_POINT_6 = 7;
	public static final int BUTTON_STYLE_WAY_POINT_7 = 8;
	public static final int BUTTON_STYLE_WAY_POINT_8 = 9;
	public static final int BUTTON_STYLE_WAY_POINT_9 = 10;
	public static final int BUTTON_STYLE_WAY_POINT_10 = 11;
	public static final int BUTTON_STYLE_END_POINT = 12;
	public static final int BUTTON_STYLE_REPORT = 13;
	public static final int BUTTON_STYLE_LIST = 14;
	public static final int BUTTON_STYLE_SCHEDULE = 15;
	private TextView buttonText;
	private ImageView buttonImage;
	public SingleBottomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.single_bottom_button, this);
		findView();
		
	}
	private void findView(){
		buttonImage = (ImageView)findViewById(R.id.single_bottom_button_image);
		buttonText = (TextView)findViewById(R.id.single_bottom_button_text);
	}
	public void setButtonShowStyle(int iStyle){
		int textValue = 0;
		int imageValue = 0;
		switch(iStyle){
		case BUTTON_STYLE_START_POINT:
			textValue = R.string.STR_MM_02_02_04_12;
			imageValue = R.xml.single_bottom_button_start;
			break;
		case BUTTON_STYLE_WAY_POINT_1:
			textValue = R.string.STR_MM_03_04_04_10;
			imageValue = R.xml.single_bottom_button_way_point1;
			break;
		case BUTTON_STYLE_WAY_POINT_2:
			textValue = R.string.STR_MM_03_04_04_10;
			imageValue = R.xml.single_bottom_button_way_point2;
			break;
		case BUTTON_STYLE_WAY_POINT_3:
			textValue = R.string.STR_MM_03_04_04_10;
			imageValue = R.xml.single_bottom_button_way_point3;
			break;
		case BUTTON_STYLE_WAY_POINT_4:
			textValue = R.string.STR_MM_03_04_04_10;
			imageValue = R.xml.single_bottom_button_way_point4;
			break;
		case BUTTON_STYLE_WAY_POINT_5:
			textValue = R.string.STR_MM_03_04_04_10;
			imageValue = R.xml.single_bottom_button_way_point5;
			break;
		case BUTTON_STYLE_WAY_POINT_6:
			textValue = R.string.STR_MM_03_04_04_10;
			imageValue = R.xml.single_bottom_button_way_point6;
			break;
		case BUTTON_STYLE_WAY_POINT_7:
			textValue = R.string.STR_MM_03_04_04_10;
			imageValue = R.xml.single_bottom_button_way_point7;
			break;
		case BUTTON_STYLE_WAY_POINT_8:
			textValue = R.string.STR_MM_03_04_04_10;
			imageValue = R.xml.single_bottom_button_way_point8;
			break;
		case BUTTON_STYLE_WAY_POINT_9:
			textValue = R.string.STR_MM_03_04_04_10;
			imageValue = R.xml.single_bottom_button_way_point9;
			break;
		case BUTTON_STYLE_WAY_POINT_10:
			textValue = R.string.STR_MM_03_04_04_10;
			imageValue = R.xml.single_bottom_button_way_point10;
			break;
		case BUTTON_STYLE_END_POINT:
			textValue = R.string.STR_MM_02_02_04_13;
			imageValue = R.xml.single_bottom_button_end;
			break;
		case BUTTON_STYLE_REPORT:
			//need to modify by shaokai
			textValue = R.string.STR_MM_02_02_04_13;
			imageValue = R.xml.single_bottom_button_report;
			break;
		case BUTTON_STYLE_LIST:
			//need to modify by huangli
			textValue = R.string.STR_MM_02_02_04_13;
			imageValue = R.xml.single_bottom_button_list;
			break;
		case BUTTON_STYLE_SCHEDULE:
			textValue = R.string.STR_COM_041;
			imageValue = R.xml.single_bottom_button_end;
			break;
		default:
			textValue = R.string.STR_MM_02_02_04_13;
			imageValue = R.xml.single_bottom_button_list;
			break;
		}
		
		buttonImage.setImageResource(imageValue);
		buttonText.setText(textValue);
	}
	@Override
	public void setOnClickListener(OnClickListener l) {
		buttonImage.setOnClickListener(l);
	}

}
