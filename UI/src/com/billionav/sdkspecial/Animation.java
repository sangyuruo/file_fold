/**
 * 
 */
package com.billionav.sdkspecial;

import android.app.Activity;

/**
 * @author builder
 *
 */
public class Animation {

	/**
	 * 
	 */
	private Animation() {
		// TODO Auto-generated constructor stub
	}
	
	public static void playScreenChangeAnimation(Activity a, int animID1 ,int animID2) {
		if (a != null ) {
			a.overridePendingTransition(animID1, animID2);
		}
	}

}
