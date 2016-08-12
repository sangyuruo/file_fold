package com.billionav.navi.component.dialog;

import android.content.DialogInterface;

public interface IDialog extends DialogInterface {
	boolean isShowing();
	void show();
}
