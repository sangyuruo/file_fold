package com.billionav.navi.usercontrol.Response;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerID;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PLogin;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.system.SystemInfo;
import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserInfo;
import com.billionav.navi.usercontrol.Request.UserControl_RequestBase;
import com.billionav.navi.usercontrol.Request.UserControl_RequestDownLoadUserInfo;
import com.billionav.navi.usercontrol.Request.UserControl_RequestFactory;
import com.billionav.navi.usercontrol.Request.UserControl_RequestId;

public class UserControl_ResponseUserLogin extends UserControl_ResponseBase {	
	private UserControl_UserInfo m_cUserInfo = new UserControl_UserInfo();	
	
	private int USER_NO_AUTH = 401;
	private int m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
	private int m_iErrCodeNum = 0;
	private int[] m_aiErrCode = new int[UC_MAX_ERR_CODE_NUM];
	private TimerTask  cReLoginTimeTask;
	private static Timer cTimer = new Timer();
	
	private int INVALIDATE_TOKEN_PERIOD = 0;
	private int m_iTokenValidTime = INVALIDATE_TOKEN_PERIOD;
	
	public UserControl_ResponseUserLogin(int iRequestId) {
		super(iRequestId);
		cReLoginTimeTask = new TimerTask() {
			@Override
			public void run() {
				Log.d("RefreshTheToken", "[RefreshTheToken]Timer Run ");
				//if login renew the Token 
				if (UserControl_ManagerIF.Instance().HasLogin()) {
					Log.d("RefreshTheToken", "[RefreshTheToken]Timer Run LoginStatus == true");
					RefreshTheToken();
				} else {
					//doNothing
					Log.d("RefreshTheToken", "[RefreshTheToken]Timer Run LoginStatus == false");
				}
			}
		};
	}
	
	public void doResponse() {
		NSTriggerInfo cTrigger = new NSTriggerInfo();
		cTrigger.m_iTriggerID = NSTriggerID.UIC_MN_TRG_UC_USER_LOGIN;
		int errCode = getResCode();		
		if (PResponse.RES_CODE_NO_ERROR != errCode) {
			UserControl_ManagerIF.Instance().setLoginStatus(false);
			int iErrorType = UserControl_ResponseBase.UC_RESPONES_LOC_FAIL;
			m_aiErrCode[0] = errCode;
			
			if (USER_NO_AUTH == errCode) {
				iErrorType = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
				byte[] receiveData = getReceivebuf();
				ParseResultInfo(receiveData);
			}
			cTrigger.SetlParam1(iErrorType);
			cTrigger.SetlParam2(m_aiErrCode[0]);
		}
		else {
			byte[] receiveData = getReceivebuf();			
			if (ParseResultInfo(receiveData)) {
				UserControl_ManagerIF.Instance().notifyLoginSucceed();
				UserControl_RequestBase cReq = (UserControl_RequestFactory
						.Instance())
						.CreateRequest(UserControl_RequestId.UC_REQ_DOWNLOAD_USERINFO);

				if ((null != m_cUserInfo.m_strPhoneNum)
						&& (!"".equals(m_cUserInfo.m_strPhoneNum))) {
					m_cUserInfo.m_iLoginType = UserControl_UserInfo.USER_LOGIN_TYPE_PHONE;
				} else if ((null != m_cUserInfo.m_strEmail)
						&& (!"".equals(m_cUserInfo.m_strEmail))) {
					m_cUserInfo.m_iLoginType = UserControl_UserInfo.USER_LOGIN_TYPE_EMAIL;
				} else if ((null != m_cUserInfo.m_strNickName)
						&& (!"".equals(m_cUserInfo.m_strNickName))) {
					m_cUserInfo.m_iLoginType = UserControl_UserInfo.USER_LOGIN_TYPE_NANE;
				}
				PLogin.setLoginResult(receiveData);
				UserControl_ManagerIF.Instance().SetUserInfo(m_cUserInfo);
				UserControl_ManagerIF.Instance().SaveLoginInfoToFile(m_cUserInfo);	
				UserControl_ManagerIF.Instance().setM_cLoginStoredInfo(m_cUserInfo);
				UserControl_ManagerIF.Instance().setLoginIdentityTag(getM_iPassedParam());
				UserControl_ManagerIF.Instance().setLoginStatus(true);
				
				if (UserControl_UserInfo.USER_LOGIN_TYPE_PHONE == m_cUserInfo.m_iLoginType) {
					((UserControl_RequestDownLoadUserInfo) cReq)
							.DownloadByPhone(m_cUserInfo.m_strPhoneNum, true);

				} else if (UserControl_UserInfo.USER_LOGIN_TYPE_EMAIL == m_cUserInfo.m_iLoginType) {
					((UserControl_RequestDownLoadUserInfo) cReq)
							.DownloadByMail(m_cUserInfo.m_strEmail, true);
				} else if (UserControl_UserInfo.USER_LOGIN_TYPE_NANE == m_cUserInfo.m_iLoginType) {
					((UserControl_RequestDownLoadUserInfo) cReq)
							.DownloadByName(m_cUserInfo.m_strNickName, true);
				}
				
				
			} else{
				UserControl_ManagerIF.Instance().setLoginStatus(false);
			}

			cTrigger.SetlParam1(m_iResultCode);
			cTrigger.SetlParam2(m_aiErrCode[0]);
		}
		
		cTrigger.SetString1(getM_iPassedParam() + "");
		MenuControlIF.Instance().TriggerForScreen(cTrigger);
	}

	public void SetUserInfo(UserControl_UserInfo userInfo) {
		if ((null != m_cUserInfo)
			&& (null != userInfo)) {
			m_cUserInfo = userInfo;
		}
	}

	public boolean ParseResultInfo(byte[] inputData) {
		String strData = new String(inputData);
		
		if ("".equals(strData)) {
			return false;
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new InputSource(new StringReader(strData)));
			//Element root = dom.getDocumentElement();
			//NodeList items = root.getElementsByTagName("result");
			NodeList items =  dom.getElementsByTagName("result");
			if (0 == items.getLength()) {
				return false;
			}
			
			Element element = (Element) items.item(0);
			String value = null;
			if(null != element){
				value = element.getAttribute("code");
			}
			
			//0: successful
			//1: fail
			if(null != value && 0 == value.compareTo("0")) {
				m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SUC;
				//Start Timer ---Auto login before Token unuseful 
				m_iTokenValidTime = getTokenValidPeriod(element);
				if (INVALIDATE_TOKEN_PERIOD != m_iTokenValidTime) {
					Timer cTimer = GetTimer();
					if (null != cTimer) {
						long lTime = (long)(m_iTokenValidTime * 60 * 1000 * 0.9);
						cTimer.schedule(cReLoginTimeTask, lTime);
						Log.d("RefreshTheToken", "[RefreshTheToken]TaskSetTime After " + lTime);
					} else {
						Log.d("RefreshTheToken", "[RefreshTheToken]NoTimer ");
					}
				}else {
					Log.d("RefreshTheToken", "[RefreshTheToken]m_iTokenValidTime ==  INVALIDATE_TOKEN_PERIOD");
				}
				
				praseThirdXMLParty(element);
			}
			else {
				m_iResultCode = UserControl_ResponseBase.UC_RESPONES_SRV_FAIL;
				if (null != element) {
					NodeList errorList =  element.getElementsByTagName("error");
					if (null != errorList && errorList.getLength() > 0) {
						Element errorCode = (Element) errorList.item(0);
						if (null != errorCode) {
							String strCode = errorCode.getAttribute("code");
							if (!"".equals(strCode)) {
								m_aiErrCode[0] = Integer.parseInt(strCode);
							}
						}
					}
				}
				return false;
			}
		} 
		catch (Exception  e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	private void RefreshTheToken() {
		UserControl_UserInfo[] cUserInfo = UserControl_ManagerIF.Instance().GetLoginObjectInfoFromFile();
		if(cUserInfo.length > 0 && null != cUserInfo[0]) {
			Log.d("RefreshTheToken", "[RefreshTheToken]Task Running ");
			Log.d("RefreshTheToken", "[RefreshTheToken]Login Type: " + cUserInfo[0].m_iLoginType + " param01: "+cUserInfo[0].m_strPhoneNum + " param02: "+cUserInfo[0].m_strUserPassWord);
			if ( UserControl_UserInfo.USER_LOGIN_TYPE_PHONE == cUserInfo[0].m_iLoginType) {
				UserControl_ManagerIF.Instance().LoginByPhone(cUserInfo[0].m_strPhoneNum,cUserInfo[0].m_strUserPassWord,SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_RELOGIN);
			} else if (UserControl_UserInfo.USER_LOGIN_TYPE_EMAIL == cUserInfo[0].m_iLoginType) {
				UserControl_ManagerIF.Instance().LoginByMail(cUserInfo[0].m_strEmail,cUserInfo[0].m_strUserPassWord ,SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_RELOGIN);
			} else if (UserControl_UserInfo.USER_LOGIN_TYPE_NANE == cUserInfo[0].m_iLoginType) {
				UserControl_ManagerIF.Instance().LoginByName(cUserInfo[0].m_strNickName,cUserInfo[0].m_strUserPassWord ,SystemInfo.getDeviceNo(),UserControl_CommonVar.LOGIN_INDETITY_OF_SOURCE_RELOGIN);
			} else {
				Log.d("RefreshTheToken", "[RefreshTheToken]Login Type Null");
			}
		} else {
			Log.d("RefreshTheToken", "[RefreshTheToken]Task Running Error lenght = " + cUserInfo.length);
		}
	}
	
	private void praseThirdXMLParty(Element element){
		if(null != element) {
			NodeList thirdpartyList = element.getElementsByTagName("thirdparty");
			if (null != thirdpartyList && thirdpartyList.getLength() > 0) {
				Element thirdpartyEle = (Element) thirdpartyList.item(0);
				NodeList appList = thirdpartyEle.getElementsByTagName("app");
				if (null != appList && appList.getLength() > 0) {
					for (int index = 0; index < appList.getLength(); index++) {
						Element appEle = (Element)appList.item(index);
						if (null != appEle) {
							String strName = appEle.getAttribute("name");
							String strToken = appEle.getAttribute("token");
							String strVaildPeriod = appEle.getAttribute("valid-period");
							int iWeiBoStatus = UserControl_UserInfo.WEIBO_STATUS_UNBINDED;
							if (null != strToken && !"".equals(strToken.trim())) {
								iWeiBoStatus = UserControl_UserInfo.WEIBO_STATUS_BINDED;
							}
							if (UserControl_CommonVar.APP_WEIBO_SINA.equals(strName)) {
								m_cUserInfo.m_strWeiBoToken[0] = strToken;
								m_cUserInfo.m_strWeiBoVaildPeriod[0] = strVaildPeriod;
								m_cUserInfo.m_iWeiBoStatus[0] = iWeiBoStatus;
							} else if (UserControl_CommonVar.APP_WEIBO_TECENT.equals(strName)) {
								m_cUserInfo.m_strWeiBoToken[1] = strToken;
								m_cUserInfo.m_strWeiBoVaildPeriod[1] = strVaildPeriod;
								m_cUserInfo.m_iWeiBoStatus[1] = iWeiBoStatus;
							} else {
								
							}
						}
					}
				}
			}
		}
	}
	
	private int getTokenValidPeriod(Element result) {
		try {
			NodeList tokenNodeList = result.getElementsByTagName("token");
			if (null != tokenNodeList && tokenNodeList.getLength() > 0) {
				Element tokenNode = (Element)tokenNodeList.item(0);
				if (null != tokenNode) {
					String period = tokenNode.getAttribute("valid-period");
					return Integer.parseInt(period);
				}
			}
		}catch(Exception e) {
			return INVALIDATE_TOKEN_PERIOD;
		}
		
		return INVALIDATE_TOKEN_PERIOD;
	}
	
	private Timer GetTimer() {
		return cTimer;
	}

}
