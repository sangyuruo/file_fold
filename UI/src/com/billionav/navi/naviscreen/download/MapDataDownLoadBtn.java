package com.billionav.navi.naviscreen.download;

import com.billionav.ui.R;
import com.billionav.navi.download.OfflinePackageInfo;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class MapDataDownLoadBtn extends TextView{
	public MapDataDownLoadBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTextColor(Color.WHITE);
		if(!isInEditMode()){
			this.setGravity(Gravity.CENTER);
		}
	}
	public void setMybtnEnabled(boolean enabled) {
		if(!enabled){
			setBackgroundResource(R.drawable.navicloud_and_757a);
		}
	}

	public void setDownloadstate(int state) {
		setVisibility(View.VISIBLE);
		switch (state) {
		default:
		case OfflinePackageInfo.W3_US_NO_PACKAGE:
			setText(R.string.STR_MM_01_04_02_86);						//download
			setBackgroundResource(R.drawable.navicloud_and_756a);
			break;
		case OfflinePackageInfo.W3_US_COMPLETED:
			setVisibility(View.INVISIBLE);
			break;
		case OfflinePackageInfo.W3_US_CAN_UPDATE :			
			setText(R.string.STR_MM_01_04_02_85);						//updating infomation
			setBackgroundResource(R.drawable.navicloud_and_759a);
			break;
		case OfflinePackageInfo.W3_US_PAUSE:
			setText(R.string.STR_MM_01_04_02_86);						//download
			setBackgroundResource(R.drawable.navicloud_and_756a);
			break;
		case OfflinePackageInfo.W3_US_DOWNLOADING:	
			setText(R.string.STR_MM_01_04_01_100);						//Cancel
			setBackgroundResource(R.drawable.navicloud_and_757a);
			break;
		case OfflinePackageInfo.W3_US_WAITING_FOR_DOWNLOAD:
			setText(R.string.STR_MM_01_04_01_100);						//Cancel
			setBackgroundResource(R.drawable.navicloud_and_757a);
			break;
		case OfflinePackageInfo.W3_US_UNPACK:							//unpack
			setText(R.string.STR_MM_01_04_01_104);	
			setBackgroundResource(R.drawable.navicloud_and_757a);
			break;
		}
	}
}
