package com.billionav.navi.usercontrol.Request;

import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestGRGetResource;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestGroupBuildGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestGroupDelGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestGroupEditGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestGroupQueryGroups;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestInfoBlogSharing;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestInfoGroupSharing;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberAddToBlackList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberApprovalJoinGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberExitGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberInvitePerson;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberJoinGroup;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberKickOffPerson;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberMoveOutBlackList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMemberQueryBlackList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMyDataQueryRankList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestMyDataQueryUserStyle;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterDelCollection;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterGetPhoto;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterQueryColList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterQueryPosterDetails;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterQueryPosterList;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterReportPos;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterUploadData;
import com.billionav.navi.usercontrol.Request.SNS.UserControl_RequestPosterUserFeedback;

public class UserControl_RequestFactory {
	private static UserControl_RequestFactory m_sInstance = new UserControl_RequestFactory();;

	private UserControl_RequestFactory(){
	}
	
	public static UserControl_RequestFactory Instance() {
		return m_sInstance;
	}
	
	public UserControl_RequestBase CreateRequest(int iRequestId) {
		UserControl_RequestBase cRequestBase;
		
		switch (iRequestId) {
		case UserControl_RequestId.UC_REQ_REG_BY_PHONE:
			cRequestBase = new UserControl_RequestRegByPhone(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_CHECK_REGISTED:
			cRequestBase = new UserControl_RequestCheckRegisted(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_VALIDATECODE:
			cRequestBase = new UserControl_RequestReqValidateCode(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_USER_LOGIN:
			cRequestBase = new UserControl_RequestUserLogin(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_USER_LOGOUT:
			cRequestBase = new UserControl_RequestUserLogout(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_GET_PASS_BY_PHONE:
			cRequestBase = new UserControl_RequestGetPassByPhone(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_CHANGE_PASS:
			cRequestBase = new UserControl_RequestChangePass(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_UPLOAD_USERINFO:
			cRequestBase = new UserControl_RequestUpLoadUserInfo(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_DOWNLOAD_USERINFO:
			cRequestBase = new UserControl_RequestDownLoadUserInfo(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_REG_BY_MAIL:
			cRequestBase = new UserControl_RequestRegByMail(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_GET_PASS_BY_MAIL:
			cRequestBase = new UserControl_RequestGetPassByMail(iRequestId);
			break;

		case UserControl_RequestId.UC_REQ_DELETE_USER:
			cRequestBase = new UserControl_RequestDeleteUser(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_ASK_USER_IN_TO_CIRCLE:
			cRequestBase = new UserControl_RequestInviteUserIntoCircle(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SET_ACCESS_TOKEN:
			cRequestBase = new UserControl_RequestSetAccessToken(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_GET_ACCESS_TOKEN:
			cRequestBase = new UserControl_RequestGetAccessTokenStatus(iRequestId);
			break;
			

		case UserControl_RequestId.UC_REQ_GET_PHOTO:
			cRequestBase = new UserControl_RequestGetPhoto(iRequestId);
			break;
		
		case UserControl_RequestId.UC_REQ_SNS_GET_RESOURCE:
			cRequestBase = new UserControl_RequestGetResource(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_GET_VERIFICATION_CODE:
			cRequestBase = new UserControl_RequestGetCode(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SET_NEW_PASSWORD:
			cRequestBase = new UserControl_RequestForgetPass(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GM_BUILD_GROUP:
			cRequestBase = new UserControl_RequestGroupBuildGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GM_DEL_GROUP:
			cRequestBase = new UserControl_RequestGroupDelGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GM_EDIT_GROUP:
			cRequestBase = new UserControl_RequestGroupEditGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GM_QUERY_GROUPS:
			cRequestBase = new UserControl_RequestGroupQueryGroups(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_JOIN_GROUP:
			cRequestBase = new UserControl_RequestMemberJoinGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_ADD_TO_BLACKLIST:
			cRequestBase = new UserControl_RequestMemberAddToBlackList(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_EXIT_GROUP:
			cRequestBase = new UserControl_RequestMemberExitGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_INVITE_PERSON:
			cRequestBase = new UserControl_RequestMemberInvitePerson(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_APPROVAL_PERSON:
			cRequestBase = new UserControl_RequestMemberApprovalJoinGroup(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_KICKOFF_PERSON:
			cRequestBase = new UserControl_RequestMemberKickOffPerson(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_MOVEOUT_BLACKLIST:
			cRequestBase = new UserControl_RequestMemberMoveOutBlackList(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_GMM_QUERY_BLACKLIST:
			cRequestBase = new UserControl_RequestMemberQueryBlackList(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_IM_BLOG_SHARING:
			cRequestBase = new UserControl_RequestInfoBlogSharing(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_IM_GROUP_SHARING:
			cRequestBase = new UserControl_RequestInfoGroupSharing(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_UPLOAD_DATA:
			cRequestBase = new UserControl_RequestPosterUploadData(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_REPORT_POS:	
			cRequestBase = new UserControl_RequestPosterReportPos(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_USER_FEEDBACK:
			cRequestBase = new UserControl_RequestPosterUserFeedback(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_GET_PHOTO:
			cRequestBase = new UserControl_RequestPosterGetPhoto(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_QUERY_COL_LIST:
			cRequestBase = new UserControl_RequestPosterQueryColList(iRequestId);
			break;

		case UserControl_RequestId.UC_REQ_SNS_PM_DEL_COL:
			cRequestBase = new UserControl_RequestPosterDelCollection(iRequestId);
			break; 	
		
		case UserControl_RequestId.UC_REQ_SNS_PM_QUERY_POST_LIST:
			cRequestBase = new UserControl_RequestPosterQueryPosterList(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_SNS_PM_QUERY_POST_DETAILS:
			cRequestBase = new UserControl_RequestPosterQueryPosterDetails(iRequestId);
			break;
		
		case UserControl_RequestId.UC_REQ_SNS_GR_GET_RESOURCE:
			cRequestBase = new UserControl_RequestGRGetResource(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_MYDATA_GET_RANKLIST:
			cRequestBase = new UserControl_RequestMyDataQueryRankList(iRequestId);
			break;
			
		case UserControl_RequestId.UC_REQ_MYDATA_GET_USER_STYLE:
			cRequestBase = new UserControl_RequestMyDataQueryUserStyle(iRequestId);
			break;
			
		default:
			return null;
		}

		return cRequestBase;
	}
}
