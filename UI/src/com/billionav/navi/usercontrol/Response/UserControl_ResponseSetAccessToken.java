package com.billionav.navi.usercontrol.Response;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;

public class UserControl_ResponseSetAccessToken  extends UserControl_ResponseBase{

	private int m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
	private int[] m_aiErrCode = new int[UC_MAX_ERR_CODE_NUM];
	
	public UserControl_ResponseSetAccessToken(int iRequestId)
	{
		super(iRequestId);
	}
	
	public void doResponse() {		
		NSTriggerInfo cTrigger = new NSTriggerInfo();
		cTrigger.m_iTriggerID = NSTriggerID.UIC_MN_TRG_UC_SET_ACCESS_TOKEN  ;		
		int errCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR != errCode) {
			cTrigger.SetlParam1(UserControl_ResponseBase.UC_RESPONES_LOC_FAIL);
			cTrigger.SetlParam2(errCode);
		}
		else {
			byte[] receiveData = getReceivebuf();			
			ParseResultInfo(receiveData);
			cTrigger.SetlParam1(m_iResultCode);
			cTrigger.SetlParam2(m_aiErrCode[0]);
		}
		MenuControlIF.Instance().TriggerForScreen(cTrigger);	
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
			if (0 == items.getLength()) {
				return false;
			}			
			Element element = (Element) items.item(0);
			String value = element.getAttribute("code");			
			//0: successful
			//1: fail
			if(0 == value.compareTo("0")) {
				m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SUC;
				
			}
			else {
				//if server error ,then the result code value is 1.
				//then m_iResultCode should be set with UC_RESPONES_SRV_FAIL
				m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
				//there are a lot of error,so throw out error root
				
			}
		} 
		catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
}
