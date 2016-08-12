package com.billionav.navi.menucontrol;

public class NSWinchangeType
{	
	//
	public static final int MN_WINCHANGE_TYPE_UNKNOWN					   = 0xFFFFFFFF;     
	
	//Back
	public static final int MN_WINCHANGE_TYPE_BACK                         = 0x00000001;
	public static final int MN_WINCHANGE_TYPE_BACK_DEFAULT                 = 0x00000002;        
	public static final int MN_WINCHANGE_TYPE_BACK_NAVIGATION              = 0x00000003;
	public static final int MN_WINCHANGE_TYPE_BACK_SEARCH                  = 0x00000004;

	//Forward
	public static final int MN_WINCHANGE_TYPE_FORWARD                      = 0x01000001;
	public static final int MN_WINCHANGE_TYPE_FORWARD_DEFAULT              = 0x01000002;
	public static final int MN_WINCHANGE_TYPE_FORWARD_KEEPDEPTH            = 0x01000003;
	public static final int MN_WINCHANGE_TYPE_FORWARD_SEARCH               = 0x01000004;
	public static final int MN_WINCHANGE_TYPE_FORWARD_KEEPDEFAULT          = 0x01000005;
}	