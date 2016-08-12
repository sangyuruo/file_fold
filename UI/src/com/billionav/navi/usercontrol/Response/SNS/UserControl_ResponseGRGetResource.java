package com.billionav.navi.usercontrol.Response.SNS;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;


public class UserControl_ResponseGRGetResource  extends UserControl_ResponseSNSBase{

	public UserControl_ResponseGRGetResource(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_GR_GET_RESOURCE);
	}
	
	public boolean ParseResultInfo(byte[] inputData) {
		if ("text/html; charset=utf-8".equals(getRecvContentType())) {
			
			String strData = new String(inputData);		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
				Document dom = builder.parse(new InputSource(new StringReader(strData)));
//				Element root = dom.getDocumentElement();
//				NodeList items = root.getElementsByTagName("result");
				
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
					OnResponseSuccess(element);
				}
				else {
					//if server error ,then the result code value is 1.
					//then m_iResultCode should be set with UC_RESPONES_SRV_FAIL
					m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
					OnReponseFailed(root);
				}
			} 
			catch (Exception  e) {
				e.printStackTrace();
				return false;
			}
	
			return true;
			
		}else {
			m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SUC;
			if (UserControl_CommonVar.GET_RESOURCE_TYPE_POSTER_VOICE == getM_iPassParam01()) {
				UserControl_ManagerIF.Instance().setM_bytePosterVoiceInfo(inputData);
			} else if (UserControl_CommonVar.GET_RESOURCE_TYPE_USER_PICTURE == getM_iPassParam01()) {
				UserControl_ManagerIF.Instance().setM_bytePosterUserHeaderPicInfo(inputData);
			} else if (UserControl_CommonVar.GET_RESOURCE_TYPE_POSTER_PHOTO == getM_iPassParam01()) {
				UserControl_ManagerIF.Instance().setM_bytePosterPictureInfo(inputData);
			}
			return true;
		}
	}

}
