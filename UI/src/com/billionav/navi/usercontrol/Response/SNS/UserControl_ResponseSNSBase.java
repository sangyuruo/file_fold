package com.billionav.navi.usercontrol.Response.SNS;


import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;


public class UserControl_ResponseSNSBase extends UserControl_ResponseBase{
	protected int m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
	private static int DEFAULT_MAX_ERROR_COUNT = 3;
	protected int m_iParam02;
	protected int m_iParam03;
	protected int m_iParam04;
	protected String m_strParam01;
	
	protected String m_strPassParam01;
	protected int	 m_iPassParam01;
	
	protected int m_iTriggleID = UserControl_CommonVar.UIC_MN_TRG_UC_INVLIDATE;
	
	public UserControl_ResponseSNSBase(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		NSTriggerInfo cTrigger = new NSTriggerInfo();
		setTriggleID();
		cTrigger.m_iTriggerID = getM_iTriggleID();
		
		int errCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR != errCode) {
//			cTrigger.SetlParam1(UserControl_ResponseBase.UC_RESPONES_LOC_FAIL);
//			cTrigger.SetlParam2(errCode);
			setM_iResultCode(UserControl_ResponseBase.UC_RESPONES_LOC_FAIL);
			setM_iParam02(errCode);
		}
		else {
			byte[] receiveData = getReceivebuf();	
			
			if(ParseResultInfo(receiveData)) {
				
			}
		}
		SetTheStringParam();
		cTrigger.SetString1(m_strParam01);
		cTrigger.SetlParam1(m_iResultCode);
		cTrigger.SetlParam2(m_iParam02);
		cTrigger.SetlParam3(m_iParam03);
		cTrigger.SetlParam4(m_iParam04);
		MenuControlIF.Instance().TriggerForScreen(cTrigger);
		
		UserControl_CommonVar.SNSLog("SNSCommon", UserControl_CommonVar.SNS_LOG_STEP_TRIGGLE, cTrigger.toString());
		UserControl_CommonVar.SNSLog("SNSCommon", UserControl_CommonVar.SNS_LOG_STEP_END, "");
	}
	
	protected void SetTheStringParam() {
		setM_strParam01(getM_strPassParam01());
	}
	
	protected void SetErrorCode(Element root) {
		
		if (null == root) {
			return;
		}
		
		NodeList items = root.getElementsByTagName("error");
		if (null == items) {
			return ;
		}
		int iLenght = items.getLength() < DEFAULT_MAX_ERROR_COUNT ? items.getLength() : DEFAULT_MAX_ERROR_COUNT;
		for(int i = 0; i < iLenght; i++) {
			Element element = (Element) items.item(i);
			if (null == element) {
				return ;
			}
			String value = element.getAttribute("code");
			if (0 == i) {
				setM_iParam02(Integer.parseInt(value));
			} else if (1 == i) {
				setM_iParam03(Integer.parseInt(value));
			} else if (2 == i) {
				setM_iParam04(Integer.parseInt(value));
			} else {
				
			}
		}
	}
	

	public boolean ParseResultInfo(byte[] inputData) {
		String strData = new String(inputData);		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new InputSource(new StringReader(strData)));
//			Element root = dom.getDocumentElement();
//			NodeList items = root.getElementsByTagName("result");
			
			NodeList items = dom.getElementsByTagName("result");
			if (null == items) {
				return false;
			}
			if (0 == items.getLength()) {
				return false;
			}			
			Element element = (Element) items.item(0);
			Element root = element;
			String value = element.getAttribute("code");
			
			//0: successful
			//1: fail
			if(0 == value.compareTo("0")) {
				m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SUC;
				UserControl_CommonVar.SNSLog("SNSCommon", UserControl_CommonVar.SNS_LOG_STEP_RECEIVE, "SUCCESS");
				OnResponseSuccess(element);
			}
			else {
				//if server error ,then the result code value is 1.
				//then m_iResultCode should be set with UC_RESPONES_SRV_FAIL
				m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
				UserControl_CommonVar.SNSLog("SNSCommon", UserControl_CommonVar.SNS_LOG_STEP_RECEIVE, "Failed");
				OnReponseFailed(root);
			}
		} 
		catch (Exception  e) {
			UserControl_CommonVar.SNSLog("SNSCommon", UserControl_CommonVar.SNS_LOG_STEP_EXCEPTION, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	protected void OnResponseSuccess(Element element) {
		
	}
	
	protected void OnReponseFailed(Element root){
		
		SetErrorCode(root);
	}
	
	public void setTriggleID(){
		
	}
	
	public int getM_iTriggleID() {
		return m_iTriggleID;
	}

	public void setM_iTriggleID(int m_iTriggleID) {
		this.m_iTriggleID = m_iTriggleID;
	}
	
	public int getM_iResultCode() {
		return m_iResultCode;
	}

	public void setM_iResultCode(int m_iResultCode) {
		this.m_iResultCode = m_iResultCode;
	}

	public int getM_iParam02() {
		return m_iParam02;
	}

	public void setM_iParam02(int m_iParam02) {
		this.m_iParam02 = m_iParam02;
	}

	public int getM_iParam03() {
		return m_iParam03;
	}

	public void setM_iParam03(int m_iParam03) {
		this.m_iParam03 = m_iParam03;
	}

	public int getM_iParam04() {
		return m_iParam04;
	}

	public void setM_iParam04(int m_iParam04) {
		this.m_iParam04 = m_iParam04;
	}

	public String getM_strParam01() {
		return m_strParam01;
	}

	public void setM_strParam01(String m_strParam01) {
		this.m_strParam01 = m_strParam01;
	}

	public String getM_strPassParam01() {
		return m_strPassParam01;
	}

	public void setM_strPassParam01(String m_strPassParam01) {
		this.m_strPassParam01 = m_strPassParam01;
	}

	public int getM_iPassParam01() {
		return m_iPassParam01;
	}

	public void setM_iPassParam01(int m_iPassParam01) {
		this.m_iPassParam01 = m_iPassParam01;
	}
	
	

}
