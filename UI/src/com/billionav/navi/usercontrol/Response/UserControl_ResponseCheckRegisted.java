package com.billionav.navi.usercontrol.Response;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;


public class UserControl_ResponseCheckRegisted extends UserControl_ResponseBase {	
	private int m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
	private int m_iResultDetailCode = 0;
	
	
	public UserControl_ResponseCheckRegisted(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		
		NSTriggerInfo cTrigger = new NSTriggerInfo();
		cTrigger.m_iTriggerID = NSTriggerID.UIC_MN_TRG_UC_CHECK_REGISTED;

		
		int errCode = getResCode();

		
		//if response of server happen error,then should set params from trigger
		if (PResponse.RES_CODE_NO_ERROR != errCode) {
			cTrigger.SetlParam1(UserControl_ResponseBase.UC_RESPONES_LOC_FAIL);
			cTrigger.SetlParam2(errCode);
		}
		else {
			byte[] receiveData = getReceivebuf();	
			ParseResultInfo(receiveData);

			cTrigger.SetlParam1(m_iResultCode);
			cTrigger.SetlParam2(m_iResultDetailCode);
		}

		//send trigger to UI
		MenuControlIF.Instance().TriggerForScreen(cTrigger);
	}
		
	public boolean ParseResultInfo(byte[] inputData) {

		String strData = new String(inputData);	
		m_iResultDetailCode=new Integer(strData).intValue();
		if (0 == m_iResultDetailCode) {
			m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SUC;
		}
		return true;
	}
	
	


	
	
}
