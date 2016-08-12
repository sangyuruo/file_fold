package com.billionav.navi.naviscreen.base;


import com.billionav.jni.UIMenuControlJNI;
import com.billionav.navi.download.PackageDownloadListener;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;

public class OfflinePackageListener extends PackageDownloadListener{
	private int index;
	public void SetDLNo(int index){
		this.index = index;
	}


	@Override
	protected void onDownloadOfflinePackageSize(String packageName,
			long downloadSize, int returnCode) {
		// TODO Auto-generated method stub
		int AreadCode = Integer.parseInt(packageName.substring( 0, packageName.indexOf("_") ));
		NSTriggerInfo nsTriggerInfo = new NSTriggerInfo();
		nsTriggerInfo.m_iTriggerID =NSTriggerID.UIC_MN_TRG_DL_MAP_PACK_UPDATESIZE;
		nsTriggerInfo.m_lParam1 = returnCode;
		nsTriggerInfo.m_lParam2 = downloadSize;
 		nsTriggerInfo.m_lParam3 = index;
		nsTriggerInfo.m_lParam4 = AreadCode;
		MenuControlIF.Instance().TriggerForScreen(nsTriggerInfo);
	}

}
