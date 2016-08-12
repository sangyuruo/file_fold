package com.billionav.navi.system;


public class DevelopMode {
	// only temp for developing
		
		public static boolean isDevelopingAdvanced() {
			// if debugmode.pset.kawagoe exists, Developing is open
			// if not, Developing is close
			return false;//(jniConfigManager.STARTMODE_ADVANCED == jniConfigManager.Instance().GetStartMode());
		}
		
		public static boolean isDeveloping() {
			// if debugmode.pset.kawagoe exists, Developing is open
			// if not, Developing is close
			return false;//(jniConfigManager.STARTMODE_DEVELOP == jniConfigManager.Instance().GetStartMode());
		}
}
