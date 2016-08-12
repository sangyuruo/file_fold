package com.billionav.jni;

public class UIUpdateControlJNI {
	private UIUpdateControlJNI() {
	}
	
	// ClearSearchDataType
	public static final int UI_UDC_CLEAR_SEARCH_DATA_FORMAT_VERSION		= 1;
	public static final int UI_UDC_CLEAR_SEARCH_DATA_DATA_VERSION		= 2;
	public static final int UI_UDC_CLEAR_SEARCH_DATA_TYPE_MAX			= 3;
	
	// RequestClearSearchData
	public static native void RequestClearSearchData(int eClearSearchDataType);
}
