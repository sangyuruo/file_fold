package com.billionav.navi.datasynccontrol.Request;

import com.billionav.navi.datasynccontrol.DataSyncControl_AmebaParam;

/*	M---Must	N----Not Must    T------Together 
 *  CAR_ID							M
	MODEL_ID						M
	DRIVER_ID						M
	LON								T1
	LAT								T1
	ROAD_TYPE						T1
	DEPARTURE_TIME					N
	CONDITION						M
	VEHICLE_TYPE					N
	REMAIN_BATTERY					N
	REMAIN_GAS						N
	TIME_REQUIRED					-
	AIRCON							N
	AIRCON_STEP						WHEN AIRCON=MANUAL  M
	GEO_SYSTEM						T1
	FORMAT							M
	PAINT							M
	RESOLUTION						M
	REMAIN_RATE						M

*/

public class DataSyncControl_RequestAmeba extends DataSyncControl_RequestBase {
	private DataSyncControl_AmebaParam cAmebaParam;	
	private String strMiddleSymbol = "&";
	private String strEqualSymbol = "=";
	
	public DataSyncControl_RequestAmeba(int iRequestId) {
		super(iRequestId);
	}
	
	public boolean SendAmebaRequest() {
		String strURL = "";
		String strURLDomain = "https://spiral-web.incrementp.co.jp/";
		String strURLGate = "~yito/public_html/GetAmeba.php";
		
		StringBuffer urlTail = new StringBuffer();
		String strParamItem = "";
		String strParamItemName = "";
		
		//CAR_ID
		strParamItem = cAmebaParam.getStrCarID();
		strParamItemName = "CAR_ID";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		
		//MODEL_ID
		strParamItem = cAmebaParam.getStrModelID();
		strParamItemName = "MODEL_ID";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
			
		}
		
		//DRIVER_ID
		strParamItem = cAmebaParam.getStrDriveID();
		strParamItemName = "DRIVER_ID";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
	
		}
		
		//LON
		strParamItem = cAmebaParam.getStrLon();
		strParamItemName = "LON";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);

		}
		//LAT
		strParamItem = cAmebaParam.getStrLat();
		strParamItemName = "LAT";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//ROAD_TYPE
		strParamItem = cAmebaParam.getStrRoadType();
		strParamItemName = "ROAD_TYPE";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//DEPARTURE_TIME
		strParamItem = cAmebaParam.getStrDepartureTime();
		strParamItemName = "DEPARTURE_TIME";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//CONDITION
		strParamItem = cAmebaParam.getStrConition();
		strParamItemName = "CONDITION";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//VEHICLE_TYPE
		strParamItem = cAmebaParam.getStrVehicleType();
		strParamItemName = "VEHICLE_TYPE";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//REMAIN_BATTERY
		strParamItem = cAmebaParam.getStrRemainBattery();
		strParamItemName = "REMAIN_BATTERY";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//REMAIN_GAS
		strParamItem = cAmebaParam.getStrRemainGas();
		strParamItemName = "REMAIN_GAS";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//TIME_REQUIRED
		strParamItem = cAmebaParam.getStrTimeRequired();
		strParamItemName = "TIME_REQUIRED";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//AIRCON
		strParamItem = cAmebaParam.getStrAirCon();
		strParamItemName = "AIRCON";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//AIRCON_STEP
		strParamItem = cAmebaParam.getStrAtrConStep();
		strParamItemName = "AIRCON_STEP";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//GEO_SYSTEM
		strParamItem = cAmebaParam.getStrGeoSystem();
		strParamItemName = "GEO_SYSTEM";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//FORMAT
		strParamItem = cAmebaParam.getStrFormat();
		strParamItemName = "FORMAT";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//PAINT
		strParamItem = cAmebaParam.getStrPaint();
		strParamItemName = "PAINT";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//RESOLUTION
		strParamItem = cAmebaParam.getStrResolution();
		strParamItemName = "RESOLUTION";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		//REMAIN_RATE
		strParamItem = cAmebaParam.getStrRemainRate();
		strParamItemName = "REMAIN_RATE";
		if ( null !=  strParamItem && !"".equals(strParamItem) ) {
			urlTail.append(strMiddleSymbol);
			urlTail.append(strParamItemName);
			urlTail.append(strEqualSymbol);
			urlTail.append(strParamItem);
		}
		
		strURL = strURLDomain + strURLGate + "?" + urlTail.toString();
		SendRequestByGet(strURL);
		return true;
	}

	public DataSyncControl_AmebaParam getcAmebaParam() {
		return cAmebaParam;
	}

	public void setcAmebaParam(DataSyncControl_AmebaParam cAmebaParam) {
		this.cAmebaParam = cAmebaParam;
	}

}
