package com.billionav.navi.component.mapcomponent;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.billionav.navi.component.DensityUtil;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.uicommon.IViewControl;
import com.billionav.navi.uitools.ScreenMeasure;
import com.billionav.ui.R;
import com.pioneer.PLocProviderKit.PLocProviderKit;
import com.pioneer.PLocProviderKit.interfaces.RequiredListener;

public class CradleState extends RelativeLayout implements IViewControl {

	private PLocProviderKit cradle = new PLocProviderKit();

	private ImageView cradleStateIcon;

	public CradleState(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public CradleState(Context context) {
		super(context);
		initialize();
	}

	private void initialize() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.cradle_state, this);

		findViews();
		adjustLayout();
		cradle.registerGpsStatusListener(new RequiredListener() {

			@Override
			public void onExtDeviceConnectStateChanged(int connectState) {
				cradleStateIcon
						.setBackgroundResource(connectState == PLocProviderKit.CONNECT_STATE_CONNECTED ? R.drawable.cradle_apk_logo
								: R.drawable.cradle_apk_logo2);
			}
		});
		cradle.getExtDeviceConnectionStatus();
	}

	private void findViews() {
		cradleStateIcon = (ImageView) findViewById(R.id.cradle_state);
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		adjustLayout();
		super.onConfigurationChanged(newConfig);
	}

	public void onResume() {
		setMarkValue();
	}

	private void setMarkValue() {
		cradleStateIcon
				.setBackgroundResource(cradle.getExtDeviceConnectionStatus() == PLocProviderKit.CONNECT_STATE_CONNECTED ? R.drawable.cradle_apk_logo
						: R.drawable.cradle_apk_logo2);
	}

	private void adjustLayout() {
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) cradleStateIcon
				.getLayoutParams();
		if (ScreenMeasure.isPortrait()) {
			lp.setMargins(0, 0, 0, DensityUtil.dp2px(getContext(), 56));
		} else {
			lp.setMargins(0, 0, 0, 0);
		}
	}

	@Override
	public boolean receiveTrigger(NSTriggerInfo trigger) {
		return false;
	}

}
