package com.billionav.navi.app.ext.log;

import java.util.Comparator;

public class LogMessageComparitor implements Comparator<NaviLogBean> {

	@Override
	public int compare(NaviLogBean arg0, NaviLogBean arg1) {
		// Always return 0, turning the priority queue into a FIFO queue. 
		return 0;
	}
}
