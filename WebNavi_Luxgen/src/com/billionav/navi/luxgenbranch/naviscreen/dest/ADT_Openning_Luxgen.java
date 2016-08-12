package com.billionav.navi.luxgenbranch.naviscreen.dest;

import android.content.res.Configuration;

import com.billionav.navi.cn.Luxgen.R;
import com.billionav.navi.naviscreen.open.ADT_Openning;

public class ADT_Openning_Luxgen extends ADT_Openning{
	
	@Override
	public void setBackgroud(Configuration newConfig) {
		if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			getImageView().setImageResource(R.drawable.navicloud_and_bg_517a);
			getImageView().setBackgroundResource(R.drawable.navicloud_and_bg_517a);
	  	}else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
	  		getImageView().setImageResource(R.drawable.navicloud_and_bg_517b);
	  		getImageView().setBackgroundResource(R.drawable.navicloud_and_bg_517b);
		}
	}
}
