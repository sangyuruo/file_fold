package com.billionav.navi.uicommon;

import android.content.DialogInterface;
import android.util.Log;

import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.naviscreen.dest.ADT_Top_Menu;
import com.billionav.navi.naviscreen.download.ADT_Download_Map;
import com.billionav.navi.uitools.SharedPreferenceData;
import com.billionav.ui.R;


public class UIC_AuthCommon {

	private static UIC_AuthCommon instance = null;
	private Class enterClass = null;
	
	public static  UIC_AuthCommon getInstance(){
		if(instance == null){
			instance = new UIC_AuthCommon();
		}

		return instance;
	}
	private UIC_AuthCommon(){
		
	}
	public void setEnterClass(Class temp){
		 this.enterClass = temp;
	}
	public Class getEnterClass(){
		return enterClass;
	}
	private boolean isFirstEnterLogin(){
		if(enterClass==null){
			return true;
		}
		return false;
	}
	public void leaveAuthWinscape(){
	if (!isFirstEnterLogin()) {
		MenuControlIF.Instance().BackSearchWinChange(enterClass);
	} else {
		if(!judgeDownloadOflData()){
			MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Top_Menu.class);
		}
	}
	}
	/**
	 * judgeDownloadOflData
	 * judge whether pop up download offline package hint
	 *
	 * @return  true mean pop up hint , false mean don't pop up hint.
	 */
	private boolean judgeDownloadOflData() {
		boolean oflautotip = SharedPreferenceData.getBoolean(SharedPreferenceData.OFFLINE_AUTO_TIP);
		if(oflautotip){
			CustomDialog customdialog = new CustomDialog(MenuControlIF.Instance().GetCurrentActivity());
			customdialog.setTitle(R.string.STR_MM_01_00_01_09);
			customdialog.setMessage(R.string.MSG_01_00_01_08);
			customdialog.setPositiveButton(R.string.MSG_00_00_00_09, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					MenuControlIF.Instance().ForwardWinChangeByInsertWinsToStackBelowTopDeleteCurWin(new Class[]{ADT_Top_Menu.class},ADT_Download_Map.class);
				}
			});
			customdialog.setNegativeButton(R.string.MSG_02_02_01_02, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					MenuControlIF.Instance().ForwardDefaultWinChange(ADT_Top_Menu.class);
				}
			});
			customdialog.show();
		}else{
			return false;
		}
		SharedPreferenceData.setValue(SharedPreferenceData.OFFLINE_AUTO_TIP, false);
		return true;
	}
}
