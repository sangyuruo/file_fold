package com.billionav.navi.usercontrol.Response;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

import com.billionav.jni.UIBaseConnJNI;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PLogin;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;


public class UserControl_ResponseDownLoadUserInfo extends UserControl_ResponseBase {	
	private boolean isDownloadMyself;
	private int m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
	
	
	private UserControl_UserInfo userInfo = new UserControl_UserInfo();
	
	public UserControl_UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserControl_UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public boolean isDownloadMyself() {
		return isDownloadMyself;
	}

	public void setDownloadMyself(boolean isDownloadMyself) {
		this.isDownloadMyself = isDownloadMyself;
	}

	public UserControl_ResponseDownLoadUserInfo(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		
		NSTriggerInfo cTrigger = new NSTriggerInfo();
		cTrigger.m_iTriggerID = NSTriggerID.UIC_MN_TRG_UC_DOWNLOAD_USERINFO;
		//if when server is responsing,error happens ,then getResCode() will return error code
		int errCode = getResCode();	
		if (PResponse.RES_CODE_NO_ERROR != errCode) {
			cTrigger.SetlParam1(UserControl_ResponseBase.UC_RESPONES_LOC_FAIL);
			cTrigger.SetlParam2(errCode);
		}
		else {
			byte[] receiveData = getReceivebuf();
			if (true==isDownloadMyself) {
				userInfo = UserControl_ManagerIF.Instance().GetLogInUserInfo();
			}
			//receivedata was parsed,and then if recieve userinfo,then save to instance of UserControl_ManagerIF
			if (ParseResultInfo(receiveData)) {
				if(true==isDownloadMyself) {
					
				}
				else {
					UserControl_ManagerIF .Instance().SetOtherUserInfo(userInfo);
				}
			}
			cTrigger.SetlParam1(m_iResultCode);

		}
		UIBaseConnJNI.notifyLogin(userInfo.m_strUserId, PLogin.getSessionToken());
		//throw trigger to UI
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
				items = root.getElementsByTagName("detail");
				
			//sava userinfo to native variable
				element = (Element) items.item(0);
				if (null != userInfo) {
					userInfo.m_strUserId= element.getAttribute("userid");
					userInfo.m_strNickName= element.getAttribute("nickname");
					userInfo.m_strEmail= element.getAttribute("email");
					userInfo.m_strPhoneNum= element.getAttribute("cellphone");
					userInfo.m_strBirthday= element.getAttribute("birthday");
					userInfo.m_strHomeAddress= element.getAttribute("address");
					userInfo.m_strSex= element.getAttribute("gender");
					userInfo.m_strOccupation= element.getAttribute("occupation");
					String strPhoto = element.getAttribute("photo");
					if (null != strPhoto && !"".equals(strPhoto)) {
						userInfo.m_byPhoto = strPhoto.getBytes();
					}
					userInfo.m_strMicroBlogURL= element.getAttribute("blog");
					userInfo.m_strRealname= element.getAttribute("realname");
					userInfo.m_strDescription= element.getAttribute("description");
					userInfo.m_strEducation= element.getAttribute("education");
					userInfo.m_strSchool= element.getAttribute("school");
					userInfo.m_strCompany= element.getAttribute("company");
					userInfo.m_strQQ= element.getAttribute("QQ");
					userInfo.m_strMSN= element.getAttribute("MSN");
					userInfo.m_strMicroBlogURL= element.getAttribute("blog");
				}
				
			}
			else {
				m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
//				SetErrorCode(root);
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
