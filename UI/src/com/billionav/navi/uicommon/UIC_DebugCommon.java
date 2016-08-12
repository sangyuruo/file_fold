package com.billionav.navi.uicommon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UIC_DebugCommon {
	private static UIC_DebugCommon m_pInstance;

	private String filename = "";

	private boolean logRunState = false;

//	private List<String> filelist = new ArrayList<String>();

	private int scrollTime = 500;

	private int scrollDistance = 6;

	private String aslFileName = "";

//	private List<String> aslFilelist = new ArrayList<String>();

	private boolean mapDragLineFlg = false;

	private boolean incomeFirst = true;

	private boolean useSmartloopDebugFile = true;

	private boolean exitAplforBackKey = false;
	
	//no use, will be remove
	private int type;
	
	private boolean speedadjustType = false;
	
	private boolean isOpenSensorGyro = false;
	
	public static final int TYPE_DEBUG = 1;
	public static final int TYPE_SERVICE = 2;
	private static final int HIDE_PREVIEWLAYOUT = 0;
	private static final int SHOW_PREVIEWLAYOUT = 1;
	private static final int SHOW_SPEEDLAYOUT = 2;
	private static final int HIDE_SPEEDLAYOUT = 3;

	
	public void setExitAplforBackKey(boolean exitAplforBackKey) {
		this.exitAplforBackKey = exitAplforBackKey;
	}

	public boolean isExitAplforBackKey() {
		return exitAplforBackKey;
	}

	public boolean isUseSmartloopDebugFile() {
		return useSmartloopDebugFile;
	}

	public void setUseSmartloopDebugFile(boolean useSmartloopDebugFile) {
		this.useSmartloopDebugFile = useSmartloopDebugFile;
	}

	public static UIC_DebugCommon Instance() {
		if (null == m_pInstance) {
			m_pInstance = new UIC_DebugCommon();
		}
		return m_pInstance;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setLogRunState(boolean logRunState) {
		this.logRunState = logRunState;
	}

	public boolean isLogRunState() {
		return logRunState;
	}


	public void setScrollTime(int scrollTime) {
		this.scrollTime = scrollTime;
	}

	public int getScrollTime() {
		return scrollTime;
	}

	public void setScrollDistance(int scrollDistance) {
		this.scrollDistance = scrollDistance;
	}

	public int getScrollDistance() {
		return scrollDistance;
	}

	public void setAslFileName(String aslFileName) {
		this.aslFileName = aslFileName;
	}

	public String getAslFileName() {
		return aslFileName;
	}

	public void setMapDragLineFlg(boolean mapDragLineFlg) {
		this.mapDragLineFlg = mapDragLineFlg;
	}

	public boolean isMapDragLineFlg() {
		return mapDragLineFlg;
	}

	public void setIncomeFirst(boolean incomeFirst) {
		this.incomeFirst = incomeFirst;
	}

	public boolean isIncomeFirst() {
		return incomeFirst;
	}

	public boolean isOpenSensorGyro() {
		return isOpenSensorGyro;
	}

	public void setOpenSensorGyro(boolean isOpenSensorGyro) {
		this.isOpenSensorGyro = isOpenSensorGyro;
	}


}
