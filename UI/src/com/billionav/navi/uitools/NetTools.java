package com.billionav.navi.uitools;

import com.billionav.navi.net.PConnectReceiver;

public class NetTools {
	public static final boolean isNetEnable() {
		return  (PConnectReceiver.getConnectType() != PConnectReceiver.CONNECT_TYPE_NONE);
	}
}
