package com.billionav.navi.sysMsgControl.Response;


import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.sysMsgControl.SysMsgControl;
import com.billionav.navi.uitools.SystemTools;

public class SysMsgControl_ResponseGetSysMsg extends SysMsgControl_ResponseBase{

	private int iStatus = -1;
	public SysMsgControl_ResponseGetSysMsg(int iRequestId) {
		super(iRequestId);
	}

	public void doResponse(){
		int iResCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			byte[] receiveData = getReceivebuf();
			if (null != receiveData) {
				boolean success = SysMsgControl.getInstance().XMLParser(receiveData);
				if(success){
					iStatus = UC_RESPONES_SUC;
					
					SysMsgControl.getInstance().sendSystemNotification();
				}else {
					iStatus = UC_DETAILS_XML_PRASE_ERROR;
				}
			} else {
				iStatus = UC_RESPONES_LOC_FAIL;
			}
		} else {
			iStatus = UC_RESPONES_SRV_FAIL;
		}
		
//		NSTriggerInfo cInfo = new NSTriggerInfo();
//		cInfo.SetTriggerID(0);
//		cInfo.SetlParam1(iStatus);
//		
//		MenuControlIF.Instance().TriggerForScreen(cInfo);
	}
}
