/**
 * 
 */
package com.billionav.navi.uicommon;

import com.billionav.jni.UIPathControlJNI;
/**
 * @author yehen
 *
 */
public class UIC_RouteCommon{

	public static UIC_RouteCommon Instance() {
		if(null == m_pInstance){
			m_pInstance = new UIC_RouteCommon();
		}
		return m_pInstance; 
	}
	
	private static UIC_RouteCommon m_pInstance;
	private UIC_RouteCommon() {
	}
	
	public static final int ROUTE_STATUS_INVALID = 0;
    public static final int ROUTE_STATUS_EXIST_AND_GUIDE_ON = 1;
    public static final int ROUTE_STATUS_EXIST_AND_GUIDE_OFF = 2;
    public static final int ROUTE_STATUS_NOT_EXIST = 3;
    public static final int ROUTE_STATUS_HALF = 4;

	///Routing Criteria
	public static final int ROUTE_CONDITION  = 0;	
	///Expressways
	public static final int HIGHWAY = 1;
	///Ferry Conditions
	public static final int FERRY = 2;	
	//save route condition	
	private int RouteCondition = 0;
	private int Highway = 0;
	private int Ferry = 0;
	private boolean isRouteConditionModified = false;
	
	public static final int ROUTE_CAL_INVALID = 0;
	public static final int ROUTE_CAL_SUCCESS = 1;
	public static final int ROUTE_CAL_FAIL = 2;
	public static final int ROUTE_CAL_CANCEL = 3;
	public static final int ROUTE_CAL_TIME_OUT = 4;
	
	
	public int GetRouteOptionAttr(int dwID)
	{
		int attr = -1; 
		if( dwID == ROUTE_CONDITION )
		{
			attr = RouteCondition;			
		}else if ( dwID == HIGHWAY )
		{
			attr =   Highway;			
		}else if ( dwID == FERRY )
		{
			attr =  Ferry;			
		}		
		return attr;		
	}
		
	public boolean isRouteOptionModified()
	{		
		return isRouteConditionModified;
	}
	
	public void setRouteOptionModifiedFlag(boolean bModified)
	{
		isRouteConditionModified = bModified;
	}	
	
	public int getRouteStatus() {

		int status;

		UIPathControlJNI jniPathCtl = new UIPathControlJNI();
		int routeStatus = jniPathCtl.GetRouteStatus();
		int guideStatus = jniPathCtl.GetCurrentGuide();
		if (routeStatus == UIPathControlJNI.UIC_PT_ROUTE_STATUS_COMP) {
//			if (guideStatus == UIPathControlJNI.UIC_PT_CURRENT_GUIDE_ON) {
				status = ROUTE_STATUS_EXIST_AND_GUIDE_ON;
//			} else {
//				status = ROUTE_STATUS_EXIST_AND_GUIDE_OFF;
//			}
		} else if (routeStatus == UIPathControlJNI.UIC_PT_ROUTE_STATUS_NONE) {
			status = ROUTE_STATUS_NOT_EXIST;
		} else if (routeStatus == UIPathControlJNI.UIC_PT_ROUTE_STATUS_HALF) {
			status = ROUTE_STATUS_HALF;
		} else {
			status = ROUTE_STATUS_INVALID;
		}
		jniPathCtl = null;

		return status;

	}

	public boolean isRouteExistAndGuideOn() {

		return (getRouteStatus() == ROUTE_STATUS_EXIST_AND_GUIDE_ON);

	}
	
}
