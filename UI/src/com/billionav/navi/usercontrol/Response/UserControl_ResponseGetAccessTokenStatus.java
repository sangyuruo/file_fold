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
import com.billionav.navi.usercontrol.UserControl_WeiBoBindStatus;

public class UserControl_ResponseGetAccessTokenStatus extends UserControl_ResponseBase{

	private String m_strToken = "";
	private int m_iResultCode = UserControl_ResponseBase.UC_RESPONES_LOC_FAIL;
	private int m_iErrCodeNum = 0;
	private int sinaValue=0;
	private int tencentValue=0;
	
	public UserControl_ResponseGetAccessTokenStatus(int iRequestId)
	{
		super(iRequestId);
	}
	public void doResponse() {
		
		NSTriggerInfo cTrigger = new NSTriggerInfo();
		cTrigger.m_iTriggerID = NSTriggerID.UIC_MN_TRG_UC_GET_ACCESS_TOKEN_STATUS  ;
		
		int errCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR != errCode) {
			cTrigger.SetlParam1(UserControl_ResponseBase.UC_RESPONES_LOC_FAIL);
			cTrigger.SetlParam2(errCode);
		}
		else {
			byte[] receiveData = getReceivebuf();	
			ParseResultInfo(receiveData);
			cTrigger.SetlParam1(m_iResultCode);
			cTrigger.SetlParam3(sinaValue);
			cTrigger.SetlParam4(tencentValue);
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
			Element root = element;
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

			}
			 items = root.getElementsByTagName("app");
			 
			 UserControl_WeiBoBindStatus status = null;
			 if (null != items && items.getLength() > 0) {
				 status = new UserControl_WeiBoBindStatus();
				 for (int index = 0; index < items.getLength(); index++) {
					 element = (Element) items.item(index);
					 if(null != element && UserControl_WeiBoBindStatus.WEIBO_MAX_NUMBER >= items.getLength()) {
						 String strName = element.getAttribute("name");//sina  or  tencent
						 String strValue = element.getAttribute("value");
						 String strToken = element.getAttribute("token");
						 String strValidPeriod = element.getAttribute("valid-period");
						 
						 status.setStrWeiBoName(index, strName);
						 status.setStrWeiBoBindValue(index, strValue);
						 status.setStrWeiBoTokens(index, strToken);
						 status.setStrWeiBoValidPeriods(index, strValidPeriod);
					 }
				 }
			 }
			UserControl_ManagerIF.Instance().setM_cWeiBoBindStatus(status);
		} 
		catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
