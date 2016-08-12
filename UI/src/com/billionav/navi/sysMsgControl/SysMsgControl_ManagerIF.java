package com.billionav.navi.sysMsgControl;



import com.billionav.jni.W3JNI;
import com.billionav.navi.net.PNetLog;
import com.billionav.navi.sysMsgControl.Request.SysMsgControl_RequestFactory;
import com.billionav.navi.sysMsgControl.Request.SysMsgControl_RequestGetSysMsg;

public class SysMsgControl_ManagerIF {


	
	private static SysMsgControl_ManagerIF instance;
	private String m_strMsgServerUrl = null;
	
	
	public static SysMsgControl_ManagerIF getInstance(){
		if(null == instance){
			instance = new SysMsgControl_ManagerIF();
		}
		return instance;
	}
	private SysMsgControl_ManagerIF() {
		Init();
	}
	
	public void Init(){
		InitUrlFromIni();
	}
	
	private void InitUrlFromIni() {
		String requestRootUrl = W3JNI.getConfigValue("SYSMSGURL");
		if (requestRootUrl.length() == 0) {
			m_strMsgServerUrl = null;
			PNetLog.e("SysMsgControl_ManagerIF:: InitUrl error requestRootUrl=["
					+ requestRootUrl + "]\n");
		} else {
			m_strMsgServerUrl = requestRootUrl;
		}

	}
	
	public String getM_strMsgServerUrl() {
		return m_strMsgServerUrl;
	}

	public void setM_strMsgServerUrl(String m_strMsgServerUrl) {
		this.m_strMsgServerUrl = m_strMsgServerUrl;
	}

	/*
	 * request SysMsg from server By interface supported by Shaokai in com.billionav.navi.sysMsgControl.Request 
	 */
	public long requestSysMsgFromServer(){
		SysMsgControl_RequestGetSysMsg request = (SysMsgControl_RequestGetSysMsg) SysMsgControl_RequestFactory.Instance().CreateRequest(SysMsgControl_RequestType.SM_REQ_MESSAGE_ID);
		request.sendSysMsgRequest();
		return request.getM_iUserRequestID();
	}

}
