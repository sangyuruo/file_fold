package com.billionav.navi.app.ext;

import com.billionav.jni.UIPathControlJNI;
import com.billionav.navi.app.ext.log.NaviLogUtil;
import com.billionav.navi.sync.AppLinkService;

public final class NaviUtil {
	public static void stopDemoDriving(){
		NaviLogUtil.debug(NaviConstant.TAG_HMI, "demo stop...");
		new UIPathControlJNI().StopDemoDriving();
		AppLinkService service = AppLinkService.getInstance();
		if( null != service ){
			service.updateProgressFlag(false);
			service.sendDemoStatus(false);
		}
	}
	
	
	public static void startDemoDriving(){
		NaviLogUtil.debug(NaviConstant.TAG_HMI, "demo run...");
		new UIPathControlJNI().StartDemoDriving(1);
		AppLinkService service = AppLinkService.getInstance();
		if( null != service ){
			service.updateProgressFlag(false);
			service.sendDemoStatus(true);
		}
	}
	
}
