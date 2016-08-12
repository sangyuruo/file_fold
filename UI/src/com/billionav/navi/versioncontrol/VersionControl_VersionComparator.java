package com.billionav.navi.versioncontrol;

import android.util.Log;

public class VersionControl_VersionComparator {
	public static int VERSION_LOCAL_EXPIRED = 2;
	public static int VERSION_LOCAL_HIGHER 	= 1;
	public static int VERSION_LOCAL_EQUAL 	= 0;
	public static int VERSION_LOCAL_LOWER 	= -1;
	public static int VERSION_FORMAT_ERROR 	= -2;
	
	public static int SORTING_NO_NEED_REVERSE_POS = -1;
	public static int SORTING_REVERSE_POS = 1;
	public static int SORTING_MAIN_DIFFERENT = 2;
	public static int SORTING_KEEP_POS = 0;
	
	public static int VersionCompare(String strLocalVersion, String strLatestVersion) {
		try {
			if(!VersionCompatible(strLocalVersion,strLatestVersion)) {
				return VERSION_LOCAL_EXPIRED;
			}
			int iTempValue = VersionSort(strLocalVersion,strLatestVersion);
			if (SORTING_NO_NEED_REVERSE_POS == iTempValue) {
				return VERSION_LOCAL_HIGHER;
			} 
			if (SORTING_REVERSE_POS == iTempValue) {
				return VERSION_LOCAL_LOWER;
			}
			return VERSION_LOCAL_EQUAL;
		}catch(Exception ex) {
			ex.printStackTrace();
			return VERSION_FORMAT_ERROR;
		}
	}
	
	public static boolean VersionCompatible(String strVersion01, String strVersion02) {
		String[] VersionArray01 = strVersion01
				.split(VersionControl_VersionDataFormat.SPLIT_STRING);
		String[] VersionArray02 = strVersion02
				.split(VersionControl_VersionDataFormat.SPLIT_STRING);
		int iVersionArraySize01 = VersionArray01.length;
		int iVersionArraySize02 = VersionArray02.length;

		// Main Version Number
		if (iVersionArraySize01 > 0 && iVersionArraySize02 > 0) {
			int iMainVersion01 = Integer.parseInt(VersionArray01[0]);
			int iMainVerSion02 = Integer.parseInt(VersionArray02[0]);


			if (iMainVersion01 == iMainVerSion02) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
	
	
	public static int VersionSort(String strVersion01, String strVersion02) {
		
    	String[] VersionArray01 = strVersion01.split(VersionControl_VersionDataFormat.SPLIT_STRING);
    	String[] VersionArray02 = strVersion02.split(VersionControl_VersionDataFormat.SPLIT_STRING);
    	int iVersionArraySize01 = VersionArray01.length;
    	int iVersionArraySize02 = VersionArray02.length;
    	
    	//Main Version Number
    	if (iVersionArraySize01 > 0 && iVersionArraySize02 > 0) {
    		int iMainVersion01 = Integer.parseInt(VersionArray01[0]);
    		int iMainVerSion02 = Integer.parseInt(VersionArray02[0]);
    		
    		
    		if (iMainVersion01 > iMainVerSion02) {
    			return SORTING_NO_NEED_REVERSE_POS;
    		} else if (iMainVersion01 < iMainVerSion02) {
    			return SORTING_REVERSE_POS;
    		}
    	} 
    	
    	//Second Version Number
    	if (iVersionArraySize01 > 1 && iVersionArraySize02 > 1) {
    		int iSecondeVersion01 = Integer.parseInt(VersionArray01[1]);
    		int iSecondeVersion02 = Integer.parseInt(VersionArray02[1]);
    		if (iSecondeVersion01 > iSecondeVersion02) {
    			return SORTING_NO_NEED_REVERSE_POS;
    		} else if (iSecondeVersion01 < iSecondeVersion02) {
    			return SORTING_REVERSE_POS;
    		}
    	}
    	
    	//WeekRelease Version Number
    	if (iVersionArraySize01 > 2 && iVersionArraySize02 > 2) {
    		int iWeeklyReleaseVersion01 = Integer.parseInt(VersionArray01[2]);
    		int iWeeklyReleaseVersion02 = Integer.parseInt(VersionArray02[2]);
    		if(iWeeklyReleaseVersion01 > iWeeklyReleaseVersion02) {
    			return SORTING_NO_NEED_REVERSE_POS;
    		} else if (iWeeklyReleaseVersion01 < iWeeklyReleaseVersion02) {
    			return SORTING_REVERSE_POS;
    		}
    	}
    	
    	//flow Version Number  obj01 : 0.1.0.build2 obj02: 0.1.0.build3
    	if (iVersionArraySize01 > 3 && iVersionArraySize02 > 3) {
    		String strFlowVersion01 = VersionArray01[3];
    		String strFlowVersion02 = VersionArray02[3];
    		
    		//flow Version Number  obj01 : 0.1.0.build2 obj02: 0.1.0.build3
    		if (strFlowVersion01.contains(VersionControl_VersionDataFormat.WEEKLY_BUILD_STRING)) {
    			if (strFlowVersion02.contains(VersionControl_VersionDataFormat.WEEKLY_BUILD_STRING)) {
    				int iStart01 = strFlowVersion01.indexOf(VersionControl_VersionDataFormat.WEEKLY_BUILD_STRING);
    				int iStart02 = strFlowVersion02.indexOf(VersionControl_VersionDataFormat.WEEKLY_BUILD_STRING);
    				String strData01 = strFlowVersion01.substring(iStart01 + VersionControl_VersionDataFormat.WEEKLY_BUILD_STRING.length());
    				String strData02 = strFlowVersion02.substring(iStart02 + VersionControl_VersionDataFormat.WEEKLY_BUILD_STRING.length());
    				
    				int iData01 = Integer.parseInt(strData01);
    				int iData02 = Integer.parseInt(strData02);
    				if (iData01 > iData02) {
    					return SORTING_NO_NEED_REVERSE_POS;
    				} else if (iData01 < iData02) {
    					return SORTING_REVERSE_POS;
    				}
	    		}
    		}
    		
    		//flow Version Number  obj01 : 0.1.0.daily2 obj02: 0.1.0.build3
    		if (strFlowVersion01.contains(VersionControl_VersionDataFormat.DAILY_BUILD_STRING)) {
    			if (strFlowVersion02.contains(VersionControl_VersionDataFormat.WEEKLY_BUILD_STRING)) {
    				return SORTING_REVERSE_POS;
    			}
    		}
    		
    		//flow Version Number  obj01 : 0.1.0.build3 obj02: 0.1.0.daily2
    		if (strFlowVersion01.contains(VersionControl_VersionDataFormat.WEEKLY_BUILD_STRING)) {
    			if (strFlowVersion02.contains(VersionControl_VersionDataFormat.DAILY_BUILD_STRING)) {
    				return SORTING_NO_NEED_REVERSE_POS;
    			}
    		}
    		
    		//flow Version Number  obj01 : 0.1.0.daily20120708 obj02: 0.1.0.daily0.1.0.daily20120709
    		if (strFlowVersion01.contains(VersionControl_VersionDataFormat.DAILY_BUILD_STRING)) {
    			if (strFlowVersion02.contains(VersionControl_VersionDataFormat.DAILY_BUILD_STRING)) {
    				int iStart01 = strFlowVersion01.indexOf(VersionControl_VersionDataFormat.DAILY_BUILD_STRING);
    				int iStart02 = strFlowVersion02.indexOf(VersionControl_VersionDataFormat.DAILY_BUILD_STRING);
    				String strData01 = strFlowVersion01.substring(iStart01 + VersionControl_VersionDataFormat.DAILY_BUILD_STRING.length());
    				String strData02 = strFlowVersion02.substring(iStart02 + VersionControl_VersionDataFormat.DAILY_BUILD_STRING.length());
    				
    				int iData01 = Integer.parseInt(strData01);
    				int iData02 = Integer.parseInt(strData02);
    				if (iData01 > iData02) {
    					return SORTING_NO_NEED_REVERSE_POS;
    				} else if (iData01 < iData02) {
    					return SORTING_REVERSE_POS;
    				}
    			}
    		}
    		
    	}
    	
    	//flow Version Number  obj01 : 0.1.1 obj02: 0.1.1.build3
    	if (iVersionArraySize01 == 3 && iVersionArraySize02 == 4) {
    		String strFlowVersion02 = VersionArray02[3];
    		//flow Version Number  obj01 : 0.1.1 obj02: 0.1.1.build3
    		if (strFlowVersion02.contains(VersionControl_VersionDataFormat.WEEKLY_BUILD_STRING)) {
    			return SORTING_REVERSE_POS;
    		}
    		
    		//flow Version Number  obj01 : 0.1.1 obj02: 0.1.1.daily20120710
    		if (strFlowVersion02.contains(VersionControl_VersionDataFormat.DAILY_BUILD_STRING)) {
    			return SORTING_REVERSE_POS;
    		}
    	}
    	
    	if (iVersionArraySize01 == 4 && iVersionArraySize02 == 3) {
    		String strFlowVersion01 = VersionArray01[3];
    		//flow Version Number  obj01 : 0.1.1.build3 obj02: 0.1.1
    		if (strFlowVersion01.contains(VersionControl_VersionDataFormat.WEEKLY_BUILD_STRING)) {
    			return SORTING_NO_NEED_REVERSE_POS;
    		}
    		
    		//flow Version Number  obj01 : 0.1.1.daily20120710 obj02: 0.1.1
    		if (strFlowVersion01.contains(VersionControl_VersionDataFormat.DAILY_BUILD_STRING)) {
    			return SORTING_NO_NEED_REVERSE_POS;
    		}
    	}
    	
    	
		return SORTING_KEEP_POS;
	}
}
