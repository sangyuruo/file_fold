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
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;


public class UserControl_ResponseChangePass extends UserControl_ResponseBase {	
	private int m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
	private int m_iErrCodeNum = 0;
	private int[] m_aiErrCode = new int[UC_MAX_ERR_CODE_NUM];
	private String temp_passWD = null;
	
	
	public UserControl_ResponseChangePass(int iRequestId)
	{
		super(iRequestId);
	}
	
	public void doResponse() {
		
		NSTriggerInfo cTrigger = new NSTriggerInfo();
		cTrigger.m_iTriggerID = NSTriggerID.UIC_MN_TRG_UC_CHANGE_PASS  ;
		
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
		
//parse result infomation,the result is  xml ,so according an agreement to parse
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
			Element root = element;
			String value = element.getAttribute("code");
			
			//0: successful
			//1: fail
			if(0 == value.compareTo("0")) {
				m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SUC;
				UserControl_UserInfo userInfo = UserControl_ManagerIF.Instance().GetLogInUserInfo();
				userInfo.m_strUserPassWord = temp_passWD;
				UserControl_ManagerIF.Instance().SaveLoginInfoToFile(userInfo);
				UserControl_ManagerIF.Instance().setM_cLoginStoredInfo(userInfo);
			}
			else {
				//if server error ,then the result code value is 1.
				//then m_iResultCode should be set with UC_RESPONES_SRV_FAIL
				m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
				//there are a lot of error,so throw out error root
				SetErrorCode(root);
			}
		} 
		catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void SetErrorCode(Element root) {
		NodeList items = root.getElementsByTagName("error");
		m_iErrCodeNum = items.getLength() < m_aiErrCode.length ? items.getLength() : m_aiErrCode.length;
		for(int i = 0; i < m_iErrCodeNum; i++) {
			Element element = (Element) items.item(0);
			String value = element.getAttribute("code");
			m_aiErrCode[i] = (new Integer(value)).intValue();
		}
	}
	

public void SetTempPasWord(String passwd){
		temp_passWD = passwd;
	}
}
