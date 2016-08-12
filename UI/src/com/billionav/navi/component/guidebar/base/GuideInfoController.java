package com.billionav.navi.component.guidebar.base;

import java.util.ArrayList;

import android.text.TextUtils;
import android.util.Log;

import com.billionav.jni.UIGuideControlJNI;
import com.billionav.navi.app.AndroidNaviAPP;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.naviscreen.NaviViewManager;
import com.billionav.ui.R;
import android.util.Log;
import com.billionav.navi.sync.AppLinkService;

public class GuideInfoController {

	private final GuideInfoDataManager guideData = GuideInfoDataManager.Instance();
	private final UIGuideControlJNI guideControl = UIGuideControlJNI.getInstance();
	
	private boolean needRefreshTurningInfo;
	private boolean needRefreshArrowInfo;
	private boolean needRefreshDistanceInfo;
	private boolean needRefreshStreetInfo;
	private boolean needRefreshRouteDeatilInfo;
	private boolean needRefreshLaneInfo;
	private boolean needRefreshPointKind;
	
	public static final int GIB_TURNING_INFO = 0;
	public static final int GIB_ARROW_INFO = 1;
	public static final int GIB_DISTANCE_INFO = 2;
	public static final int GIB_STREET_INFO = 3;
	public static final int GIB_ROUTE_INFO = 4;
	public static final int GIB_LANE_INFO = 5;
	public static final int GIB_IR_TURNING_INFO = 6;
	public static final int GIB_IR_STREET_INFO = 7;
	public static final int GIB_IR_LANE_INFO = 8;
	
	public static final int GIB_INFO_ON_MAP = 9;
	
	//*************************Guide Lane Image Resource*************************************//
	static final int RECOMEND_IMAGE_HIGHWAY[] =
		{
			R.drawable.navicloud_and_072a_41,
			R.drawable.navicloud_and_072a_42,
			R.drawable.navicloud_and_072a_43,
			R.drawable.navicloud_and_072a_44,
			R.drawable.navicloud_and_072a_45,
			R.drawable.navicloud_and_072a_46,
			R.drawable.navicloud_and_072a_47,
			R.drawable.navicloud_and_072a_48,
			R.drawable.navicloud_and_072a_49,
			R.drawable.navicloud_and_072a_50,
			R.drawable.navicloud_and_072a_51,
			R.drawable.navicloud_and_072a_52,
			R.drawable.navicloud_and_072a_53,
			R.drawable.navicloud_and_072a_54,
			R.drawable.navicloud_and_072a_55,
			R.drawable.navicloud_and_072a_56,
			R.drawable.navicloud_and_072a_57,
			R.drawable.navicloud_and_072a_58,
			R.drawable.navicloud_and_072a_59,
			R.drawable.navicloud_and_072a_60,
			R.drawable.navicloud_and_072a_61,
			R.drawable.navicloud_and_072a_62,
			R.drawable.navicloud_and_072a_63,
			R.drawable.navicloud_and_072a_64,
			R.drawable.navicloud_and_072a_65,
			R.drawable.navicloud_and_072a_66,
			R.drawable.navicloud_and_072a_67,
			R.drawable.navicloud_and_072a_68
		};
		
		static final int RECOMEND_IMAGE_NORMAL[] =
		{
			R.drawable.navicloud_and_072b_41,
			R.drawable.navicloud_and_072b_42,
			R.drawable.navicloud_and_072b_43,
			R.drawable.navicloud_and_072b_44,
			R.drawable.navicloud_and_072b_45,
			R.drawable.navicloud_and_072b_46,
			R.drawable.navicloud_and_072b_47,
			R.drawable.navicloud_and_072b_48,
			R.drawable.navicloud_and_072b_49,
			R.drawable.navicloud_and_072b_50,
			R.drawable.navicloud_and_072b_51,
			R.drawable.navicloud_and_072b_52,
			R.drawable.navicloud_and_072b_53,
			R.drawable.navicloud_and_072b_54,
			R.drawable.navicloud_and_072b_55,
			R.drawable.navicloud_and_072b_56,
			R.drawable.navicloud_and_072b_57,
			R.drawable.navicloud_and_072b_58,
			R.drawable.navicloud_and_072b_59,
			R.drawable.navicloud_and_072b_60,
			R.drawable.navicloud_and_072b_61,
			R.drawable.navicloud_and_072b_62,
			R.drawable.navicloud_and_072b_63,
			R.drawable.navicloud_and_072b_64,
			R.drawable.navicloud_and_072b_65,
			R.drawable.navicloud_and_072b_66,
			R.drawable.navicloud_and_072b_67,
			R.drawable.navicloud_and_072b_68
		};
		
		static final int NONGUIDE_IMAGE[] =
		{
			R.drawable.navicloud_and_072c_41,
			R.drawable.navicloud_and_072c_42,
			R.drawable.navicloud_and_072c_43,
			R.drawable.navicloud_and_072c_44,
			R.drawable.navicloud_and_072c_45,
			R.drawable.navicloud_and_072c_46,
			R.drawable.navicloud_and_072c_47,
			R.drawable.navicloud_and_072c_48,
			R.drawable.navicloud_and_072c_49,
			R.drawable.navicloud_and_072c_50,
			R.drawable.navicloud_and_072c_51,
			R.drawable.navicloud_and_072c_52,
			R.drawable.navicloud_and_072c_53,
			R.drawable.navicloud_and_072c_54,
			R.drawable.navicloud_and_072c_55,
			R.drawable.navicloud_and_072c_56,
			R.drawable.navicloud_and_072c_57,
			R.drawable.navicloud_and_072c_58,
			R.drawable.navicloud_and_072c_59,
			R.drawable.navicloud_and_072c_60,
			R.drawable.navicloud_and_072c_61,
			R.drawable.navicloud_and_072c_62,
			R.drawable.navicloud_and_072c_63,
			R.drawable.navicloud_and_072c_64,
			R.drawable.navicloud_and_072c_65,
			R.drawable.navicloud_and_072c_66,
			R.drawable.navicloud_and_072c_67,
			R.drawable.navicloud_and_072c_68
		};
	//*************************Guide Lane Image Resource*************************************//
	
	//*************************Guide Arrow Image Resource*************************************//
	public int getArrowIconByKind(int arrowKind) {
		int resID = -1;
		if(guideControl.GetGuidePointInfo_PointKind() != UIGuideControlJNI.DPGUDEF_CMN_ROAD_HWY){
			switch (arrowKind) {
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_STRAIGHT:
				resID = R.drawable.navicloud_and_077a_01;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_R:
				resID = R.drawable.navicloud_and_077a_03;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_L:
				resID = R.drawable.navicloud_and_077a_02;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_FR:
				resID = R.drawable.navicloud_and_077a_05;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_FL:
				resID = R.drawable.navicloud_and_077a_04;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_RR:
				resID = R.drawable.navicloud_and_077a_07;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_RL:
				resID = R.drawable.navicloud_and_077a_06;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_BST_R:
				resID = R.drawable.navicloud_and_077a_11;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_BST_L:
				resID = R.drawable.navicloud_and_077a_10;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN_R:
				resID = R.drawable.navicloud_and_077a_09;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN_L:
				resID = R.drawable.navicloud_and_077a_08;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN:
				resID = R.drawable.navicloud_and_077a_08;
				break;
	
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R45DEG_OUT:
				resID = R.drawable.navicloud_and_077a_12;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R90DEG_OUT:
				resID = R.drawable.navicloud_and_077a_13;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R135DEG_OUT:
				resID = R.drawable.navicloud_and_077a_14;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R180DEG_OUT:
				resID = R.drawable.navicloud_and_077a_15;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R225DEG_OUT:
				resID = R.drawable.navicloud_and_077a_16;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R270DEG_OUT:
				resID = R.drawable.navicloud_and_077a_17;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R315DEG_OUT:
				resID = R.drawable.navicloud_and_077a_18;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R360DEG_OUT:
				resID = R.drawable.navicloud_and_077a_19;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R45DEG_IN:
				resID = R.drawable.navicloud_and_077a_20;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R90DEG_IN:
				resID = R.drawable.navicloud_and_077a_21;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R135DEG_IN:
				resID = R.drawable.navicloud_and_077a_22;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R180DEG_IN:
				resID = R.drawable.navicloud_and_077a_23;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R225DEG_IN:
				resID = R.drawable.navicloud_and_077a_24;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R270DEG_IN:
				resID = R.drawable.navicloud_and_077a_25;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R315DEG_IN:
				resID = R.drawable.navicloud_and_077a_26;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R360DEG_IN:
				resID = R.drawable.navicloud_and_077a_27;
				break;
	
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L45DEG_OUT:
				resID = R.drawable.navicloud_and_077a_28;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L90DEG_OUT:
				resID = R.drawable.navicloud_and_077a_29;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L135DEG_OUT:
				resID = R.drawable.navicloud_and_077a_30;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L180DEG_OUT:
				resID = R.drawable.navicloud_and_077a_31;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L225DEG_OUT:
				resID = R.drawable.navicloud_and_077a_32;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L270DEG_OUT:
				resID = R.drawable.navicloud_and_077a_33;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L315DEG_OUT:
				resID = R.drawable.navicloud_and_077a_34;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L360DEG_OUT:
				resID = R.drawable.navicloud_and_077a_35;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L45DEG_IN:
				resID = R.drawable.navicloud_and_077a_36;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L90DEG_IN:
				resID = R.drawable.navicloud_and_077a_37;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L135DEG_IN:
				resID = R.drawable.navicloud_and_077a_38;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L180DEG_IN:
				resID = R.drawable.navicloud_and_077a_39;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L225DEG_IN:
				resID = R.drawable.navicloud_and_077a_40;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L270DEG_IN:
				resID = R.drawable.navicloud_and_077a_41;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L315DEG_IN:
				resID = R.drawable.navicloud_and_077a_42;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L360DEG_IN:
				resID = R.drawable.navicloud_and_077a_43;
				break;
			default:
				resID = 0;
				break;
		}
		}else{
			switch (arrowKind) {
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_STRAIGHT:
				resID = R.drawable.navicloud_and_074a_01;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_R:
				resID = R.drawable.navicloud_and_074a_03;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_L:
				resID = R.drawable.navicloud_and_074a_02;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_FR:
				resID = R.drawable.navicloud_and_074a_05;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_FL:
				resID = R.drawable.navicloud_and_074a_04;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_RR:
				resID = R.drawable.navicloud_and_074a_07;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_RL:
				resID = R.drawable.navicloud_and_074a_06;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_BST_R:
				resID = R.drawable.navicloud_and_074a_11;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_BST_L:
				resID = R.drawable.navicloud_and_074a_10;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN_R:
				resID = R.drawable.navicloud_and_074a_09;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN_L:
				resID = R.drawable.navicloud_and_074a_08;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN:
				resID = R.drawable.navicloud_and_074a_08;
				break;

			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R45DEG_OUT:
				resID = R.drawable.navicloud_and_074a_12;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R90DEG_OUT:
				resID = R.drawable.navicloud_and_074a_13;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R135DEG_OUT:
				resID = R.drawable.navicloud_and_074a_14;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R180DEG_OUT:
				resID = R.drawable.navicloud_and_074a_15;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R225DEG_OUT:
				resID = R.drawable.navicloud_and_074a_16;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R270DEG_OUT:
				resID = R.drawable.navicloud_and_074a_17;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R315DEG_OUT:
				resID = R.drawable.navicloud_and_074a_18;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R360DEG_OUT:
				resID = R.drawable.navicloud_and_074a_19;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R45DEG_IN:
				resID = R.drawable.navicloud_and_074a_20;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R90DEG_IN:
				resID = R.drawable.navicloud_and_074a_21;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R135DEG_IN:
				resID = R.drawable.navicloud_and_074a_22;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R180DEG_IN:
				resID = R.drawable.navicloud_and_074a_23;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R225DEG_IN:
				resID = R.drawable.navicloud_and_074a_24;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R270DEG_IN:
				resID = R.drawable.navicloud_and_074a_25;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R315DEG_IN:
				resID = R.drawable.navicloud_and_074a_26;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R360DEG_IN:
				resID = R.drawable.navicloud_and_074a_27;
				break;

			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L45DEG_OUT:
				resID = R.drawable.navicloud_and_074a_28;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L90DEG_OUT:
				resID = R.drawable.navicloud_and_074a_29;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L135DEG_OUT:
				resID = R.drawable.navicloud_and_074a_30;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L180DEG_OUT:
				resID = R.drawable.navicloud_and_074a_31;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L225DEG_OUT:
				resID = R.drawable.navicloud_and_074a_32;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L270DEG_OUT:
				resID = R.drawable.navicloud_and_074a_33;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L315DEG_OUT:
				resID = R.drawable.navicloud_and_074a_34;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L360DEG_OUT:
				resID = R.drawable.navicloud_and_074a_35;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L45DEG_IN:
				resID = R.drawable.navicloud_and_074a_36;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L90DEG_IN:
				resID = R.drawable.navicloud_and_074a_37;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L135DEG_IN:
				resID = R.drawable.navicloud_and_074a_38;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L180DEG_IN:
				resID = R.drawable.navicloud_and_074a_39;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L225DEG_IN:
				resID = R.drawable.navicloud_and_074a_40;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L270DEG_IN:
				resID = R.drawable.navicloud_and_074a_41;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L315DEG_IN:
				resID = R.drawable.navicloud_and_074a_42;
				break;
			case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L360DEG_IN:
				resID = R.drawable.navicloud_and_074a_43;
				break;
			default:
				resID = 0;
				break;
			}
		}

		return resID;
	}
	
	public static int getArrowIconByKindForDetail(int arrowKind) {
		int resID = -1;

		switch (arrowKind) {
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_STRAIGHT:
			resID = R.drawable.navicloud_and_075a_01;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_R:
			resID = R.drawable.navicloud_and_075a_03;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_L:
			resID = R.drawable.navicloud_and_075a_02;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_FR:
			resID = R.drawable.navicloud_and_075a_05;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_FL:
			resID = R.drawable.navicloud_and_075a_04;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_RR:
			resID = R.drawable.navicloud_and_075a_07;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_TURN_RL:
			resID = R.drawable.navicloud_and_075a_06;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_BST_R:
			resID = R.drawable.navicloud_and_075a_11;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_BST_L:
			resID = R.drawable.navicloud_and_075a_10;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN_R:
			resID = R.drawable.navicloud_and_075a_09;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN_L:
			resID = R.drawable.navicloud_and_075a_08;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_UTERN:
			resID = R.drawable.navicloud_and_075a_08;
			break;

		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R45DEG_OUT:
			resID = R.drawable.navicloud_and_075a_12;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R90DEG_OUT:
			resID = R.drawable.navicloud_and_075a_13;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R135DEG_OUT:
			resID = R.drawable.navicloud_and_075a_14;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R180DEG_OUT:
			resID = R.drawable.navicloud_and_075a_15;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R225DEG_OUT:
			resID = R.drawable.navicloud_and_075a_16;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R270DEG_OUT:
			resID = R.drawable.navicloud_and_075a_17;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R315DEG_OUT:
			resID = R.drawable.navicloud_and_075a_18;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R360DEG_OUT:
			resID = R.drawable.navicloud_and_075a_19;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R45DEG_IN:
			resID = R.drawable.navicloud_and_075a_20;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R90DEG_IN:
			resID = R.drawable.navicloud_and_075a_21;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R135DEG_IN:
			resID = R.drawable.navicloud_and_075a_22;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R180DEG_IN:
			resID = R.drawable.navicloud_and_075a_23;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R225DEG_IN:
			resID = R.drawable.navicloud_and_075a_24;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R270DEG_IN:
			resID = R.drawable.navicloud_and_075a_25;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R315DEG_IN:
			resID = R.drawable.navicloud_and_075a_26;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_R360DEG_IN:
			resID = R.drawable.navicloud_and_075a_27;
			break;

		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L45DEG_OUT:
			resID = R.drawable.navicloud_and_075a_28;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L90DEG_OUT:
			resID = R.drawable.navicloud_and_075a_29;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L135DEG_OUT:
			resID = R.drawable.navicloud_and_075a_30;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L180DEG_OUT:
			resID = R.drawable.navicloud_and_075a_31;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L225DEG_OUT:
			resID = R.drawable.navicloud_and_075a_32;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L270DEG_OUT:
			resID = R.drawable.navicloud_and_075a_33;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L315DEG_OUT:
			resID = R.drawable.navicloud_and_075a_34;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L360DEG_OUT:
			resID = R.drawable.navicloud_and_075a_35;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L45DEG_IN:
			resID = R.drawable.navicloud_and_075a_36;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L90DEG_IN:
			resID = R.drawable.navicloud_and_075a_37;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L135DEG_IN:
			resID = R.drawable.navicloud_and_075a_38;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L180DEG_IN:
			resID = R.drawable.navicloud_and_075a_39;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L225DEG_IN:
			resID = R.drawable.navicloud_and_075a_40;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L270DEG_IN:
			resID = R.drawable.navicloud_and_075a_41;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L315DEG_IN:
			resID = R.drawable.navicloud_and_075a_42;
			break;
		case UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_L360DEG_IN:
			resID = R.drawable.navicloud_and_075a_43;
			break;
		default:
			resID = 0;
			break;
		}

		return resID;
	}
	//*************************Guide Arrow Image Resource*************************************//
	
	//*************************Data changed Listener *************************************//
	private final ArrayList<DataChangeListener> dataChangedListeners = new ArrayList<DataChangeListener>();
	public interface DataChangeListener{
		
		/**
		 * called when data changed
		 */
		public void onDataChanged();
		/**
		 * called when route kind changed
		 */
		public void onRoutePointKindChanged();
		/**
		 * the item's type
		 * @return {@link GuideInfoController.GIB_TURNING_INFO} etc.;
		 */
		public Object getTag();
	}
	
	/**
	 * Add view to guide
	 * 
	 * @param l class implement DataChangeListener.
	 */
	public void addDataChangeListener(DataChangeListener l){
		dataChangedListeners.add(l);
	}
	
	public void removeAllDataChangeListener(){
		dataChangedListeners.clear();
	}
	
	/**
	 * Remove view from data change listener
	 * @param l
	 */
	public void removeDataChangeListener(DataChangeListener l){
		dataChangedListeners.remove(l);
	}
	//*************************Data changed Listener *************************************//
	
	
	/**
	 * synchronize all guide info, called after guide-info-bar initialized
	 */
	public void syncAllGuideInfo(){
		
		updateGuideInfo();
		
		for(int i = 0; i < dataChangedListeners.size();i++){
			DataChangeListener l = dataChangedListeners.get(i);
			if(l == null){
				dataChangedListeners.remove(i);
			}else{
				l.onDataChanged();
				l.onRoutePointKindChanged();
			}
		}
	}
	
	/**
	 * 
	 * Notify GuideInfoBar when UIC_MN_TRG_GUIDE_ROUTE_INFO_CHANGE received.
	 * 
	 * If any guide-data changed, it's view will be refreshed;
	 * 
	 * @param info
	 * @return
	 */
	public boolean notifyTrigger(NSTriggerInfo info){
		
		int id = info.GetTriggerID();
		if(id == NSTriggerID.UIC_MN_TRG_GUIDE_ROUTE_INFO_CHANGE){
			if(updateGuideInfo()){
				refreshGuideInfoBar();
			}
			return true;
		}else if(id == NSTriggerID.UIC_MN_TRG_MAGVIEW_SHOW){
//			for(DataChangeListener l : dataChangedListeners){
//				if(l.getViewTarget() == GIB_INFO_ON_MAP){
//					l.onDataChanged();
//					break;
//				}
//			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean notifyIRTrigger(NSTriggerInfo info){
		
		int id = info.GetTriggerID();
		if(id == NSTriggerID.UIC_MN_TRG_GUIDE_ROUTE_INFO_CHANGE){
			if(updateGuideInfo()){
				refreshIRGuideInfoBar();
			}
		}else if(id == NSTriggerID.UIC_MN_TRG_GUD_GUDFINISH) {
			NaviViewManager.GetViewManager().moveTaskToBack(true);
			return true;
		}
		return true;
	}
	
	/**
	 * Update all guide info.
	 * 
	 * @return True if any data has updated.
	 */
	public boolean updateGuideInfo(){
		int guideDisplayStatus = guideControl.GetDispStatus();
		if((UIGuideControlJNI.DPGUDEF_CMN_ROUTEINFO & guideDisplayStatus) == 0){
			return false;
		}
		updateGuidePointKind();
		updateStreetInfo();
		updateTurningInfo();
		updateETAInfo();
		updateLaneInfo();
		
		return needRefreshPointKind || needRefreshTurningInfo || needRefreshStreetInfo || needRefreshRouteDeatilInfo || needRefreshLaneInfo;
	}
	
	/**
	 * Update guidepoint
	 * highWay or nonHighWay is depended to decide guidebar's color
	 */
	private void updateGuidePointKind() {
		int guideDisplayStatus = guideControl.GetDispStatus();
		boolean isRouteInfoExist = (0 != (UIGuideControlJNI.DPGUDEF_CMN_ROUTEINFO & guideDisplayStatus));
		needRefreshPointKind = false;
		if(isRouteInfoExist){
			int kind = guideControl.GetGuidePointInfo_PointKind();
			if(kind != guideData.getGuidePointKind()){
				if(kind == UIGuideControlJNI.DPGUDEF_CMN_ROAD_HWY 
						|| guideData.getGuidePointKind() == UIGuideControlJNI.DPGUDEF_CMN_ROAD_HWY){
					needRefreshPointKind = true;
					Log.d("test","kind="+kind);
				}
				guideData.setGuidePointKind(kind);
			}
		}
		
	}

	/**
	 * Update Next street name
	 * 
	 * @return True if street name changed
	 */
	public boolean updateStreetInfo(){
		
		int guideDisplayStatus = guideControl.GetDispStatus();
		boolean isRouteInfoExist = (0 != (UIGuideControlJNI.DPGUDEF_CMN_ROUTEINFO & guideDisplayStatus));
		needRefreshStreetInfo = false;
		if(isRouteInfoExist){
			
			String nextStreetName;
			
			
			int kind = UIGuideControlJNI.getInstance()
					.GetGuidePointInfo_PointKind();

			if ((kind >= 1 && kind <= 6) || (kind >= 0x101 && kind <= 0x106)) {
				nextStreetName = NaviViewManager.GetViewManager().getString(
						R.string.STR_MM_01_01_04_13);
			} else if (kind == 15 || kind == 271) {
				nextStreetName = NaviViewManager.GetViewManager().getString(
						R.string.STR_MM_01_01_04_24);
			} else {
				if (AndroidNaviAPP.getInstance().getLanguageType() == AndroidNaviAPP.LANGUAGE_JP) {
					nextStreetName = guideControl.GetGuidePointInfo_CrossName();
				} else {
					nextStreetName = guideControl
							.GetGuidePointInfo_NextStreetName();

				}

			}
			
			if(TextUtils.isEmpty(nextStreetName)){
				nextStreetName = MenuControlIF.Instance().GetCurrentActivity().getString(R.string.STR_MM_01_01_04_26);
			}
			if (UIGuideControlJNI.getInstance().GetGuidePointInfo_ExistDistrictSignPost())
			{
				nextStreetName = guideControl.getDirctionSignSpotName();
				nextStreetName += " " + MenuControlIF.Instance().GetCurrentActivity().getString(R.string.STR_MM_01_01_04_23);
			}
			if(!nextStreetName.equals(guideData.getStreetName())){
				 // +text direction
					guideData.setStreetName(nextStreetName);
					needRefreshStreetInfo = true;
			}//judge if need refresh data
			
		}else{
			if(!"".equals(guideData.getStreetName()))
			{
				guideData.setStreetName("");
			
				//need disable info
				needRefreshStreetInfo = true;
			}
		}
		
		//don't need refresh
		return needRefreshStreetInfo;
	}
	
	
	/**
	 * Update next Turning's arrow mark and it's distance.
	 * 
	 * @return True if data changed.
	 */
	public boolean updateTurningInfo(){

		updateArrowInfo();
		updateNextDistance();
		
		needRefreshTurningInfo = needRefreshArrowInfo || needRefreshDistanceInfo;
		
		return needRefreshTurningInfo;
	}
	
	/**
	 * Update next Turning's arrow mark.
	 * 
	 * @return True if arrow changed.
	 */
	public boolean updateArrowInfo(){
		
		int guideDisplayStatus = guideControl.GetDispStatus();
		boolean isRouteInfoExist = (0 != (UIGuideControlJNI.DPGUDEF_CMN_ROUTEINFO & guideDisplayStatus));
		needRefreshArrowInfo = false;
		if(isRouteInfoExist){
//			if(UIGuideControlJNI.DPGUDEF_CMN_SIGN_DISTKIND_INVALID  != guideControl.GetGuidePointInfo_DistanceKind()){
				int arrowKind = guideControl.GetGuidePointInfo_ArrowKind();
				int turningImageID = getArrowIconByKind(arrowKind);
				
				int kind = UIGuideControlJNI.getInstance().GetGuidePointInfo_PointKind();
				if(arrowKind==0 && (kind == 15 || kind == 271)) {
					turningImageID = getArrowIconByKind(UIGuideControlJNI.DPGUDEF_CMN_GUIDEARROW_STRAIGHT);
				}
				
				if(turningImageID != guideData.getTurningImageID()){
					guideData.setTurningImageID(turningImageID);
					
					//need refresh info
					needRefreshArrowInfo = true;
				}
//			}
		}else{
			guideData.setTurningImageID(0);
			
			//need disable info
			needRefreshArrowInfo = true;
		}
		
		//don't need refresh
		return needRefreshArrowInfo;
	}
	
	
	/**
	 * Update next turning distance.
	 * 
	 * @return True if distance is changed.
	 */
	public boolean updateNextDistance(){
		
		int guideDisplayStatus = guideControl.GetDispStatus();
		
		boolean isNextDistanceExist = (0 != (UIGuideControlJNI.DPGUDEF_CMN_DISTANCE & guideDisplayStatus)) ;
		needRefreshDistanceInfo = false;
		if(isNextDistanceExist){
//			if(UIGuideControlJNI.DPGUDEF_CMN_SIGN_DISTKIND_INVALID  != guideControl.GetGuidePointInfo_DistanceKind()){
				String distance = guideControl.GetDistanceInfo_StrDistance();
				if(null == distance){
					guideData.setTurningDistance("--");
					needRefreshDistanceInfo = true;
					return needRefreshDistanceInfo;
				}
				String num = distance.replaceAll("[a-z]*", "");
				String unit = distance.replaceAll("[0-9]*\\.?[0-9]*", "");
				
				int indexOfdot = num.indexOf(".");
//				if (num != null && indexOfdot > 0){
//					if(indexOfdot == 2) {
//						num = num.substring(0, indexOfdot);
//					} else if (indexOfdot > 2) {
//						num = "**";
//					} else if (num.length() - (indexOfdot + 1) > 1) {
//						num = num.substring(0, indexOfdot + 2);
//					}
//				} else if (num != null && indexOfdot < 0
//						&& unit.length() == 2
//						&& num.length() > 3) {
//					num = "**";
//				}
				

				//abel add
//				Log.v("AbelDebugGuide","distance old:"+distance);
//				Float numNew = Float.parseFloat(num);
//				if(unit.equals("m")){
//				    distance = Math.round(numNew*1.0936133) + "yd";
//				}
//				else if(unit.equals("km")){
//				    distance = Math.round(numNew*0.6213712)  + "mi";
//				}
//				else {
//				    distance = num + unit;
//				}
//				Log.v("AbelDebugGuide","distance new:"+distance);
				distance = num + unit;



				if(!distance.equals(guideData.getTurningDistance())){
					guideData.setTurningDistance(distance);
					needRefreshDistanceInfo = true;
				}//judge if need refresh data
//			}
		}else{
			guideData.setTurningDistance("--");
			needRefreshDistanceInfo = true;
		}
		
		return needRefreshDistanceInfo;
	}
	
	
	/**
	 * Update route detail(ETA) info.
	 * 
	 * @return True if any ETA data changed.
	 */
	public boolean updateETAInfo(){
		
		int guideDisplayStatus = guideControl.GetDispStatus();
		boolean isETAInfoExist = (0 != (UIGuideControlJNI.DPGUDEF_CMN_ETAINFO & guideDisplayStatus));
		needRefreshRouteDeatilInfo = false;
		
		if(isETAInfoExist){
//			String remainingDistance = guideData.getRemainingDistanceString();
//			if(guideControl.GetETADistanceKind() != UIGuideControlJNI.DPGUDEF_CMN_SIGN_DISTKIND_INVALID ){
			String remainingDistance  = guideControl.GetETAInfo_StrDestDist();
		

			//abel add	
//			Log.v("AbelDebugGuide","remaining distance old:"+remainingDistance);
//			String num = remainingDistance.replaceAll("[a-z]*", "");
//			String unit = remainingDistance.replaceAll("[0-9]*\\.?[0-9]*", "");
//			Float numNew = Float.parseFloat(num);
//			if(unit.equals("m")){
//			    numNew = (float)(Math.round((numNew*1.0936133)*10))/10;
//			    remainingDistance = numNew + "yd";
//			}
//			else if(unit.equals("km")){
//			    numNew = (float)(Math.round((numNew*0.6213712)*10))/10;
//			    remainingDistance = numNew + "mi";
//			}
//			else {
//			    ;
//			}
//			Log.v("AbelDebugGuide","remaining distance new:"+remainingDistance);


//			}

			String remainingTime = guideData.getRemainingTimeString();
//			if(guideControl.GetETATimeKind() == UIGuideControlJNI.DPGUDEF_CMN_SIGN_TIMEKIND_OK ){
				remainingTime = guideControl.GetETAInfo_StrDestETR();
//			}
			String arriveTime = guideControl.GetETAInfo_StrDestETA();
			
			if(!remainingDistance.equals(guideData.getRemainingDistanceString())) {
				needRefreshRouteDeatilInfo = true;
				guideData.setRemainingDistanceString(remainingDistance);
				guideData.setRemainingTimeImageID(0);
			}
			if(!remainingTime.equals(guideData.getRemainingTimeString())) {
				needRefreshRouteDeatilInfo = true;
				guideData.setRemainingTimeString(remainingTime);
				guideData.setRemainingTimeImageID(0);
			}
			if(!arriveTime.equals(guideData.getArriveTimeString())) {
				needRefreshRouteDeatilInfo = true;
				guideData.setArriveTimeString(arriveTime);
				guideData.setRemainingTimeImageID(0);
			}
			//judge if need refresh data
			if(guideData.syncGuideProgress()){
				needRefreshRouteDeatilInfo = true;
			}
		}else{
			
			guideData.setRemainingDistanceString("**m");
			guideData.setRemainingTimeString("**h**m");
			guideData.setArriveTimeString("**h**m");
			guideData.setRemainingTimeImageID(0);
			
			needRefreshRouteDeatilInfo = true;
		}
		return needRefreshRouteDeatilInfo;
	}
	
	
	/**
	 * Update guide lane info.
	 * 
	 * @return True if lane info changed.
	 */
	public boolean updateLaneInfo(){
		int guideDisplayStatus = guideControl.GetDispStatus();
		boolean isLaneInfoExist = (0 != (UIGuideControlJNI.DPGUDEF_CMN_GUIDE_LANE & guideDisplayStatus));
		needRefreshLaneInfo = false;
		
		if(isLaneInfoExist){
			int iLaneNum = UIGuideControlJNI.getInstance().GetLaneInfo_LanNum();
			
			if(iLaneNum < 0){
				if(guideData.getGuideLaneImageID() != null)
				{
					needRefreshLaneInfo = true;
				}
				guideData.setGuideLaneImageID(null);
			}else{
				int laneImage[] = new int[iLaneNum];
				int lastLaneImage[] = guideData.getGuideLaneImageID();
				
				int iLaneArrowKind[] = UIGuideControlJNI.getInstance().GetLaneInfo_LaneArrowKind();
		    	int iLaneKind[] = UIGuideControlJNI.getInstance().GetLaneInfo_LaneKind();
		    	
		    	//read image id to array
		    	int iCount = 0;
	    		while (iLaneNum > 0) {
	    			int index = iLaneNum - 1;

	    			
	    			if(0 != iLaneArrowKind[index]) /*DPGUDEF_CMN_LANEARROW_NONE*/
	    			{
	    				if(1 == iLaneKind[index] || 2 == iLaneKind[index]) /*DPGUDEF_CMN_LANE_REC*/ /*DPGUDEF_CMN_LANE_GUIDEON*/
	    				{
	    					if(guideControl.GetGuidePointInfo_PointKind() == UIGuideControlJNI.DPGUDEF_CMN_ROAD_HWY){
	    						laneImage[iCount] = RECOMEND_IMAGE_HIGHWAY[iLaneArrowKind[index] - 1];
	    					}else{
	    						laneImage[iCount] = RECOMEND_IMAGE_NORMAL[iLaneArrowKind[index] - 1];
	    					}
	    				}
//	    				else if(2 == iLaneKind[index])/*DPGUDEF_CMN_LANE_GUIDEON*/
//	    				{
////	    					laneImage[iCount] = NORMAL_IMAGE[iLaneArrowKind[index] - 1];
//	    					laneImage[iCount] = NONGUIDE_IMAGE[iLaneArrowKind[index] - 1];
//	    				}
	    				else if(3 == iLaneKind[index])/*DPGUDEF_CMN_LANE_GUIDEOFF*/
	    				{
	    					laneImage[iCount] = NONGUIDE_IMAGE[iLaneArrowKind[index] - 1];
	    				}
	    				else{
	    					
	    				}
	    			}
	    			else
	    			{
	    				laneImage[iCount] = 0;
	    			}
	    			iLaneNum--;
	    			++iCount;
	    		}//read image id to array
	    		
	    		//judge if need refresh data
	    		if(lastLaneImage == null){
	    			guideData.setGuideLaneImageID(laneImage);
	    			needRefreshLaneInfo = true;
	    		}else{
	    			if(lastLaneImage.length == laneImage.length){
	    				for(int i = 0 ; i < laneImage.length; i++){
	    					
	    					if(lastLaneImage[i] != laneImage[i]){
	    						guideData.setGuideLaneImageID(laneImage);
	    						needRefreshLaneInfo = true;
	    						break;
	    					}
	    					needRefreshLaneInfo = false;
	    				}
	    			}else{
	    				guideData.setGuideLaneImageID(laneImage);
						needRefreshLaneInfo = true;
	    			}
	    		}//judge if need refresh data
			}
		}else{
			if(guideData.getGuideLaneImageID() != null)
			{
				needRefreshLaneInfo = true;
			}
			guideData.setGuideLaneImageID(null);
		}
		
		return needRefreshLaneInfo;
	}
	
	/**
	 * 
	 * Refresh the GuideInfoBar's Item those data has changed.
	 * 
	 */
	public void refreshGuideInfoBar(){
		int listenerNums = dataChangedListeners.size();
	        boolean needSendGuideInfoToFord = false;
		AppLinkService service = AppLinkService.getInstance();
		for(int i = listenerNums - 1; i >= 0 ;i--){
			DataChangeListener l = dataChangedListeners.get(i);
			if(l == null){
				dataChangedListeners.remove(i);
			}else{
				int tag = (Integer)l.getTag();
				if(needRefreshPointKind){
					l.onRoutePointKindChanged();
				}
				switch (tag) {
				case GIB_TURNING_INFO:
					if (needRefreshTurningInfo){
						l.onDataChanged();
						needRefreshTurningInfo = false;
						needSendGuideInfoToFord = true;
					}
					break;
					
				case GIB_STREET_INFO:
					l.onDataChanged();
					needSendGuideInfoToFord = true;
					break;
				case GIB_ROUTE_INFO:
					if (needRefreshRouteDeatilInfo){
						l.onDataChanged();
						needRefreshRouteDeatilInfo = false;
						needSendGuideInfoToFord = true;
					}
					break;
				case GIB_LANE_INFO:
					if (needRefreshLaneInfo){
						l.onDataChanged();
						needRefreshLaneInfo = false;
					}
					break;
				default:
					break;
				}
			}
		}
		if(needSendGuideInfoToFord == true && service != null){
		    service.onGuideInfoChanged();
		}
	}
	
	private void refreshIRGuideInfoBar(){
		int listenerNums = dataChangedListeners.size();
		for(int i = listenerNums - 1; i > 0 ;i--){
			DataChangeListener l = dataChangedListeners.get(i);
			if(l == null){
				dataChangedListeners.remove(i);
			}else{
				int tag = (Integer) l.getTag();
				switch (tag) {
				case GIB_IR_TURNING_INFO:
					if (needRefreshTurningInfo){
						l.onDataChanged();
						needRefreshTurningInfo = false;
					}
					break;
				case GIB_IR_STREET_INFO:
					if (needRefreshStreetInfo){
						l.onDataChanged();
						needRefreshStreetInfo = false;
					}
					break;
				case GIB_IR_LANE_INFO:
					if (needRefreshLaneInfo){
						l.onDataChanged();
						needRefreshLaneInfo = false;
					}
					break;
				default:
					break;
				}
			}
		}
		
	}
	
	/**
	 * play sound when Arrow clicked
	 */
	public void playNextTurningGuideVoice(){
		guideControl.ReqVoiceGuide(UIGuideControlJNI.NAVI_SRV_GUD_REQTYPE_SOUNDONLY);
	}
	
	/**
	 * 
	 * @return True if illust image exist.
	 */	
	public boolean isIllustExist(){
		return guideControl.isIllustExist();
	}

	public static GuideInfoController createInstance() {
		return new GuideInfoController();
	}
	
}

