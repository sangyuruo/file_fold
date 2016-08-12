package com.billionav.navi.usercontrol.Response.SNS;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.billionav.navi.usercontrol.UserControl_CommonVar;
import com.billionav.navi.usercontrol.UserControl_ManagerIF;
import com.billionav.navi.usercontrol.UserControl_UserStyleInfo;

public class UserControl_ResponseMyDataQueryUserStyle  extends UserControl_ResponseSNSBase{

	public UserControl_ResponseMyDataQueryUserStyle(int iRequestId) {
		super(iRequestId);
	}
	
	public void setTriggleID(){
		setM_iTriggleID(UserControl_CommonVar.UIC_MN_TRG_UC_MYDATA_GET_USERDATA);
	}
	
	protected void OnResponseSuccess(Element element) {
		if (null == element) {
			return;
		}
		
		UserControl_UserStyleInfo info = new UserControl_UserStyleInfo();
		
		NodeList reportList = element.getElementsByTagName("report");
		if (null != reportList && reportList.getLength() > 0) {
			Element report = (Element)reportList.item(0);
			if (null != report) {
				
				info.setM_strPeriodType(report.getAttribute("period_type"));
				info.setM_strPeriodStartData(report.getAttribute("period_start"));
				info.setM_strPeriodEndData(report.getAttribute("period_end"));
				info.setM_strUserID(report.getAttribute("user_id"));
				info.setM_strUserRankAtPeriodEnd(report.getAttribute("user_rank_at_period_end"));
				
				info.setM_strUserRankInPeriod(report.getAttribute("user_rank_in_period"));
				info.setM_strUserRankTillNow(report.getAttribute("user_rank_till_now"));
				info.setM_strUserRankTillPeriodEnd(report.getAttribute("user_rank_till_period_end"));
				
				info.setM_strOpenScore(report.getAttribute("open_score"));
				info.setM_strOpenRapidSpeedUp(report.getAttribute("open_rapid_speed_up"));
				info.setM_strOpenRapidSpeedDown(report.getAttribute("open_rapid_speed_down"));
				info.setM_strOpenRapidTurn(report.getAttribute("open_rapid_turn"));
				info.setM_strOpenStar(report.getAttribute("open_star"));
				info.setM_strOpenDriveOnline(report.getAttribute("open_drive_on_line"));
				info.setM_strOpenDriveClose(report.getAttribute("open_drive_too_close"));
				
				info.setM_StrEarnScore(report.getAttribute("earn_score"));
				info.setM_StrEarnRapidSpeedUp(report.getAttribute("earn_rapid_speed_up"));
				info.setM_StrEarnRapidSpeedDown(report.getAttribute("earn_rapid_speed_down"));
				info.setM_StrEarnRapidTurn(report.getAttribute("earn_rapid_turn"));
				info.setM_strEarnDriveOnline(report.getAttribute("earn_drive_on_line"));
				info.setM_strEarnDriveClose(report.getAttribute("earn_drive_too_close"));
				
				info.setM_StrCloseScore(report.getAttribute("close_score"));
				info.setM_StrCloseRapidSpeedUp(report.getAttribute("close_rapid_speed_up"));
				info.setM_StrCloseRapidSpeedDown(report.getAttribute("close_rapid_speed_down"));
				info.setM_StrCloseRapidTurn(report.getAttribute("close_rapid_turn"));
				info.setM_StrCloseStar(report.getAttribute("close_star"));
				info.setM_strCloseDriveOnline(report.getAttribute("close_drive_on_line"));
				info.setM_strCloseDriveClose(report.getAttribute("close_drive_too_close"));
			}
		}
		
		UserControl_ManagerIF.Instance().setM_cUserStyleInfo(info);
		
	}
	
}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

