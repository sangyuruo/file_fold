package com.billionav.navi.usercontrol.Response;

import java.util.HashMap;

import android.util.Log;

import com.billionav.navi.usercontrol.Request.UserControl_RequestId;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseGRGetResource;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseGroupBuildGroup;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseGroupDelGroup;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseGroupEditGroup;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseGroupQueryGroups;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseInfoBlogSharing;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseInfoGroupSharing;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseMemberAddToBlackList;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseMemberApprovalJoinGroup;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseMemberExitGroup;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseMemberInvitePerson;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseMemberJoinGroup;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseMemberKickOffPerson;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseMemberMoveOutBlackList;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseMemberQueryBlackList;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseMyDataQueryRankList;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponseMyDataQueryUserStyle;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterDelCollection;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterGetPhoto;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterQueryColList;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterQueryPosterDetails;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterQueryPosterList;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterReportPos;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterUploadData;
import com.billionav.navi.usercontrol.Response.SNS.UserControl_ResponsePosterUserFeedback;

public class UserControl_ResponseFactory {
	private static UserControl_ResponseFactory m_sInstance = new UserControl_ResponseFactory();
	private static long m_sRequestID = 0;
	private static HashMap m_lUserRequestMap = new HashMap<String,UserControl_ResponseBase>();
	private UserControl_ResponseFactory(){
	}
	
	public static UserControl_ResponseFactory Instance() {
		return m_sInstance;
	}

	//get a response acording to requestid
	public UserControl_ResponseBase CreateResponse(int iRequestId) {
		UserControl_ResponseBase cResponseBase;
		
		switch (iRequestId) {
		case UserControl_RequestId.UC_REQ_REG_BY_PHONE:
			cResponseBase = new UserControl_ResponseRegByPhone(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_CHECK_REGISTED:
			cResponseBase = new UserControl_ResponseCheckRegisted(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_VALIDATECODE:
			cResponseBase = new UserControl_ResponseReqValidateCode(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_USER_LOGIN:
			cResponseBase = new UserControl_ResponseUserLogin(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_USER_LOGOUT:
			cResponseBase = new UserControl_ResponseUserLogout(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_GET_PASS_BY_PHONE:
			cResponseBase = new UserControl_ResponseGetPassByPhone(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_CHANGE_PASS:
			cResponseBase = new UserControl_ResponseChangePass(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_UPLOAD_USERINFO:
			cResponseBase = new UserControl_ResponseUpLoadUserInfo(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_DOWNLOAD_USERINFO:
			cResponseBase = new UserControl_ResponseDownLoadUserInfo(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_REG_BY_MAIL:
			cResponseBase = new UserControl_ResponseRegByMail(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_GET_PASS_BY_MAIL:
			cResponseBase = new UserControl_ResponseGetPassByMail(iRequestId);
			break;

		case UserControl_RequestId.UC_REQ_DELETE_USER:
			cResponseBase = new UserControl_ResponseDeleteUser(iRequestId);
			break;

		case UserControl_RequestId.UC_REQ_ASK_USER_IN_TO_CIRCLE:
			cResponseBase = new UserControl_ResponseInviteUserIntoCircle(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SET_ACCESS_TOKEN:
			cResponseBase = new UserControl_ResponseSetAccessToken(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_GET_ACCESS_TOKEN:
			cResponseBase = new UserControl_ResponseGetAccessTokenStatus(iRequestId);
			break;	
			
		case UserControl_RequestId.UC_REQ_SNS_GM_BUILD_GROUP:
			cResponseBase = new UserControl_ResponseGroupBuildGroup(iRequestId);
			break;
		
		case UserControl_RequestId.UC_REQ_SNS_GET_RESOURCE:
			cResponseBase = new UserControl_ResponseGetResource(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_GET_VERIFICATION_CODE:
			cResponseBase = new UserControl_ResponseGetCode(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SET_NEW_PASSWORD:
			cResponseBase = new UserControl_ResponseForgetPass(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_GET_PHOTO:
			cResponseBase = new UserControl_ResponseGetPhoto(iRequestId);
			break;	
			
		case UserControl_RequestId.UC_REQ_SNS_GM_DEL_GROUP:
			cResponseBase = new UserControl_ResponseGroupDelGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GM_EDIT_GROUP:
			cResponseBase = new UserControl_ResponseGroupEditGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GM_QUERY_GROUPS:
			cResponseBase = new UserControl_ResponseGroupQueryGroups(iRequestId);
			break;
		
		case UserControl_RequestId.UC_REQ_SNS_GMM_JOIN_GROUP:
			cResponseBase = new UserControl_ResponseMemberJoinGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_ADD_TO_BLACKLIST:
			cResponseBase = new UserControl_ResponseMemberAddToBlackList(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_EXIT_GROUP:
			cResponseBase = new UserControl_ResponseMemberExitGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_INVITE_PERSON:
			cResponseBase = new UserControl_ResponseMemberInvitePerson(iRequestId);
			break;
		
		case UserControl_RequestId.UC_REQ_SNS_GMM_APPROVAL_PERSON:
			cResponseBase = new UserControl_ResponseMemberApprovalJoinGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_KICKOFF_PERSON:
			cResponseBase = new UserControl_ResponseMemberKickOffPerson(iRequestId);
			break;
		
		case UserControl_RequestId.UC_REQ_SNS_GMM_MOVEOUT_BLACKLIST:
			cResponseBase = new UserControl_ResponseMemberMoveOutBlackList(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_QUERY_BLACKLIST:
			cResponseBase = new UserControl_ResponseMemberQueryBlackList(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_IM_BLOG_SHARING:
			cResponseBase = new UserControl_ResponseInfoBlogSharing(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_IM_GROUP_SHARING:
			cResponseBase = new UserControl_ResponseInfoGroupSharing(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_UPLOAD_DATA:
			cResponseBase = new UserControl_ResponsePosterUploadData(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_REPORT_POS:	
			cResponseBase = new UserControl_ResponsePosterReportPos(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_USER_FEEDBACK:
			cResponseBase = new UserControl_ResponsePosterUserFeedback(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_GET_PHOTO:
			cResponseBase = new UserControl_ResponsePosterGetPhoto(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_QUERY_COL_LIST:
			cResponseBase = new UserControl_ResponsePosterQueryColList(iRequestId);
			break;

		case UserControl_RequestId.UC_REQ_SNS_PM_DEL_COL:
			cResponseBase = new UserControl_ResponsePosterDelCollection(iRequestId);
			break; 	
		
		case UserControl_RequestId.UC_REQ_SNS_PM_QUERY_POST_LIST:
			cResponseBase = new UserControl_ResponsePosterQueryPosterList(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_QUERY_POST_DETAILS:
			cResponseBase = new UserControl_ResponsePosterQueryPosterDetails(iRequestId);
			break;
		
		case UserControl_RequestId.UC_REQ_SNS_GR_GET_RESOURCE:
			cResponseBase = new UserControl_ResponseGRGetResource(iRequestId);
			break;
		
		case UserControl_RequestId.UC_REQ_MYDATA_GET_RANKLIST:
			cResponseBase = new UserControl_ResponseMyDataQueryRankList(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_MYDATA_GET_USER_STYLE:
			cResponseBase = new UserControl_ResponseMyDataQueryUserStyle(iRequestId);
			break;
			
		
		default:
			return null;
		}
		
		cResponseBase.setM_lUserRequestID(GetRequestID());
		m_lUserRequestMap.put(cResponseBase.getM_lUserRequestID() + "", cResponseBase);
		return cResponseBase;
	}
	
	private synchronized long GetRequestID() {
		if(m_sRequestID >= Long.MAX_VALUE) {
			m_sRequestID = 0;
		}
		return ++m_sRequestID;
	}
	
	private boolean CancleUserRequestBase(long iRequestID) {
		String key = iRequestID + "";
		if(m_lUserRequestMap.containsKey(key)) {
			UserControl_ResponseBase request = (UserControl_ResponseBase)m_lUserRequestMap.get(key);
			return request.cancelRequest();
		}
		return false;
	}
	
	public void RemoveUserRequest(long iRequestID) {
		String key = iRequestID + "";
		if(m_lUserRequestMap.containsKey(key)) {
			m_lUserRequestMap.remove(key);
			Log.d("[UserControl]", "[UserControl]--UserRequestID:RemoveUserRequest " + key);
		}
	}
	
	public boolean CancleUserRequest(long iRequestID){
		if(CancleUserRequestBase(iRequestID)) {
			Log.d("[UserControl]", "[UserControl]--UserRequestID:CancleUserRequest success" + iRequestID);
			RemoveUserRequest(iRequestID);
			return true;
		} else {
			Log.d("[UserControl]", "[UserControl]--UserRequestID:CancleUserRequest Failed" + iRequestID);
			return false;
		}
		
	}

}
