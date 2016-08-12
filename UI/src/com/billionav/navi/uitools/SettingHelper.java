package com.billionav.navi.uitools;

import com.billionav.jni.UIMapControlJNI;
import com.billionav.jni.UISearchControlJNI;

public class SettingHelper {

	public static void setCameraTip(boolean value) {
		UISearchControlJNI.SetOrbisSwtch(value);
		UIMapControlJNI.SetOrbisSwitch(value);
		SharedPreferenceData.setValue(SharedPreferenceData.CAMERA_TIP, value);
	}
}
