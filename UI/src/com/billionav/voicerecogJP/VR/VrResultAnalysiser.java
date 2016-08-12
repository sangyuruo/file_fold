package com.billionav.voicerecogJP.VR;



import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

public class VrResultAnalysiser {
	// json tags
	private static final String JSON_TAG_command_id = "command_id";
	private static final String JSON_TAG_use_lat = "use_lat";
	private static final String JSON_TAG_use_lon = "use_lon";
	private static final String JSON_TAG_keyword1 = "search_key1";
	private static final String JSON_TAG_keyword2 = "search_key2";
	private static final String JSON_TAG_keyword3 = "search_key3";
	private static final String JSON_TAG_keyword4 = "search_key4";
	private static final String JSON_TAG_keyword5 = "search_key5";
	private static final String JSON_TAG_result = "result";

	// json common tag def
	private static final String JSON_TAG_VALUE = "val";
	private static final String JSON_TAG_TYPE = "type";
	private static final String JSON_TAG_TYPE_STRING = "string";
	private static final String JSON_TAG_TYPE_DOUBLE = "double";
	private static final String JSON_TAG_TYPE_XML = "xml";
	private static final String JSON_TAG_TYPE_KML = "kml";
	private static final String JSON_TAG_TYPE_TXT = "text";
	private static final String JSON_TAG_ENCODE = "enc";
	private static final String JSON_TAG_ENCODE_BASE64 = "base64";

	public VrAnalysisResult AnalysisResult(JSONObject jObject) {

		VrAnalysisResult result = new VrAnalysisResult();
		result.Init();
		
		try {
			// set command id
			JSONObject jidObj = jObject.getJSONObject(JSON_TAG_command_id);
			String commandidStr = GetValueByType(jidObj, JSON_TAG_TYPE_STRING);				
			if (commandidStr.equals("")) {
				return result;
			}
			if(!commandidStr.substring(0, 6).equals(IntentionClient.instance().getProjectName()))
			{
				return result;
			}
			commandidStr = commandidStr.substring(6);
			result.setCommand_id(Integer.parseInt(commandidStr, 16));

			// set lat
			if(jObject.has(JSON_TAG_use_lat))
			{
				JSONObject jlatObj = jObject.getJSONObject(JSON_TAG_use_lat);
				String jlatStr = GetValueByType(jlatObj, JSON_TAG_TYPE_DOUBLE);
				if (jlatStr.equals("")) {
					return result;
				}
				result.setUse_lat(Double.parseDouble(jlatStr));
			}
			else
			{
				result.setUse_lat(0.0);
			}

			// set lon
			if(jObject.has(JSON_TAG_use_lon))
			{
				JSONObject jlonObj = jObject.getJSONObject(JSON_TAG_use_lon);
				String jlonStr = GetValueByType(jlonObj, JSON_TAG_TYPE_DOUBLE);
				if (jlonStr.equals("")) {
					return result;
				}
				result.setUse_lot(Double.parseDouble(jlonStr));
			}
			else
			{
				result.setUse_lot(0.0);
			}
			
			// set keywords
			result.setKeyword1(GetKeyword(jObject, JSON_TAG_keyword1));
			result.setKeyword2(GetKeyword(jObject, JSON_TAG_keyword2));
			result.setKeyword3(GetKeyword(jObject, JSON_TAG_keyword3));
			result.setKeyword4(GetKeyword(jObject, JSON_TAG_keyword4));
			result.setKeyword5(GetKeyword(jObject, JSON_TAG_keyword5));
			
			// set result
			if (jObject.has(JSON_TAG_result)) {
				JSONObject searchresult = jObject.getJSONObject(JSON_TAG_result);
				if(searchresult != null)
				{
					String jsonType = searchresult.getString(JSON_TAG_TYPE);
					String jsonEncode = searchresult.getString(JSON_TAG_ENCODE);
					result.setResult_type(jsonType);
					result.setResult_encode(jsonEncode);
					
					if((jsonType.equals(JSON_TAG_TYPE_XML) || 
							jsonType.equals(JSON_TAG_TYPE_TXT) ||
								jsonType.equals(JSON_TAG_TYPE_KML)) &&
					   (jsonEncode.equals(JSON_TAG_ENCODE_BASE64)))
					{
						String val = searchresult.getString(JSON_TAG_VALUE);
						String values = new String(Base64.decode(val, Base64.DEFAULT));
						result.setResult_string(values);
					}
					else {
						return result;
					}
				}
			}
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
			return result;
		}
		
		result.setAnalysis_result(VrAnalysisResult.DOCOMO_ANALYSIS_ERR_NOERROR);
		return result;
	}

	private String GetValueByType(JSONObject obj, String Type){
		String result = "";
		if ((null == obj) || (null == Type)) {
			return result;
		}

		String jsonType;
		try {
			jsonType = obj.getString(JSON_TAG_TYPE);
			if (jsonType.equals(Type)) {
				result = obj.getString(JSON_TAG_VALUE);
			}
		} catch (JSONException e) {
		}
		return result;
	}
	
	private String GetKeyword(JSONObject jObject, String key)
	{
		String keywordstr;
		try {
			keywordstr = jObject.getString(key);
			if(!keywordstr.equals(""))
			{
				JSONObject jkeywordObj = jObject.getJSONObject(key);
				return GetValueByType(jkeywordObj, JSON_TAG_TYPE_STRING);
			}
		} catch (JSONException e) {
		}
		
		return "";
	}

	public HashMap<String, ?> getParams() {
		HashMap<String, Object>  map = new HashMap<String, Object>();
//		jniSetupControl SetupDM = new jniSetupControl();
		
		// set ccid 
//		map.put("ccid", DocomoAuthDM.instance().getCCID());
		
		// set otp 
//		map.put("otp",DocomoAuthDM.instance().getOneTimePasswword());
		
		// set contract
//		map.put("contract", true ? 1: 0);
		
		// set gas brand
		String gs_value = "0";
//		switch(SetupDM.GetInitialStatus(jniSetupControl.STUPDM_GS_BRANDNAME)){
//            case jniSetupControl.STUPDM_GS_ALL:
//            	gs_value = "0";
//                break;
//            case jniSetupControl.STUPDM_GS_IDEMOTSU_KOUSAN:
//            	gs_value = "1";
//                break;
//            case jniSetupControl.STUPDM_GS_ESSO:
//            	gs_value = "2";
//                break;
//            case jniSetupControl.STUDPM_GS_ENEOS:
//            	gs_value = "3";
//                break;
//            case jniSetupControl.STUPDM_GS_KIGUNASU_SEKIYU:
//            	gs_value = "4";
//                break;
//            case jniSetupControl.STUPDM_GS_KOSKOSUMO_SEKIYU:
//            	gs_value = "6";
//                break;
//            case jniSetupControl.STUPDM_GS_SHOUWA_SHELL_SEKIYU:
//            	gs_value = "7";
//                break;
//            case jniSetupControl.STUPDM_GS_ZENERARU:
//            	gs_value = "9";
//                break;
//            case jniSetupControl.STUPDM_GS_MOBIL:
//            	gs_value = "11";
//                break;
//            case jniSetupControl.STUPDM_GS_MITSUI_SEKIYU:
//            	gs_value = "12";
//                break;
//            case jniSetupControl.STUPDM_GS_AUTOGAS:
//            	gs_value = "13";
//                break;
//            case jniSetupControl.STUPDM_GS_JA_SS:
//            	gs_value = "14";
//                break;
//            case jniSetupControl.STUPDM_GS_ITOUCHUU:
//            	gs_value = "15";
//                break;
//            case jniSetupControl.STUPDM_GS_MIKADOISHI:
//            	gs_value = "16";
//                break;
//            case jniSetupControl.STUPDM_GS_MARUBENIENERGY:
//            	gs_value = "17";
//                break;
//            case jniSetupControl.STUPDM_GS_MITSUBISHI_SHOUJI_SEKIYU:
//            	gs_value = "18";
//                break;
//            case jniSetupControl.STUPDM_GS_HOKUREN:
//            	gs_value = "19";
//                break;
//            case jniSetupControl.STUPDM_GS_SOLATO:
//            	gs_value = "20";
//                break;
//            default:
//            	gs_value = "0";
//                break;
//		}
		map.put("gs_value", gs_value);
		
		// set gas type
		int gs_type = 0;
//		switch(SetupDM.GetInitialStatus(jniSetupControl.STUPDM_GASOLINE_SETTING)){
//		case jniSetupControl.STUPDM_GASOLINE_REGULAR:
//			//regular
//			gs_type = 1;
//			break;
//		case jniSetupControl.STUPDM_GASOLINE_HIGH_OCTANE:
//			//highoku
//			gs_type = 2;
//			break;
//		default:
//			//all
//			gs_type = 0;
//			break;
//		}
		map.put("gs_type", gs_type);

		// set count
		map.put("count_dshop", 50);
		map.put("count_id", 50);
		map.put("count_gs", 50);
		map.put("count_pk", 50);
		map.put("count_spot", 50);
		map.put("count_fword", 50);
		map.put("count_tvspot", 50);
		
		// set range 
		map.put("range_dshop", 5000);//5000(m)
		map.put("range_id", 5000);//5000(m)
		map.put("range_gs", 8000);//8000(m)
		map.put("range_pk", 8000);// define in if is 16000
		map.put("range_spot", 5000);//5000(m)
		map.put("range_fword", 16000);//16000(m)
		map.put("range_tvspot", 5);//5(km)
		
		// set car info
//		byte status = SetupDM.GetCarLimitStatus();
//		byte ch[] = new byte[3];
//		ch[0] = 0;
//		ch[1] = 0;
//		ch[2] = 0;
//		
//		int i = 0;
//		if(0 != (status & 0x80)){//3Number : 1
//			ch[i] = '1';
//			i++;
//		}
//		if(0 != (status & 0x40)){//RV : 3
//			ch[i] = '3';
//			i++;
//		}
//		if(0 != (status & 0x20)){//1BOX : 2
//			ch[i] = '2';
//			i++;
//		}
//		String strCarLimit;
//		if(0 == i){//no type
//			strCarLimit = new String("0");
//		}
//		else{
//			strCarLimit = new String(ch, i);
//		}

//		int iLen = SetupDM.GetCarSizeStatus(jniSetupControl.CAR_SIZE_LENGTH);
//		int iWidth = SetupDM.GetCarSizeStatus(jniSetupControl.CAR_SIZE_WIDTH);
//		int iHeight = SetupDM.GetCarSizeStatus(jniSetupControl.CAR_SIZE_HEIGHT);
//
//		int CAR_DATA_SIZE_INVALID = 0xffff;
//		iLen = (CAR_DATA_SIZE_INVALID == iLen) ? 0 : (iLen/10);
//		iWidth = (CAR_DATA_SIZE_INVALID == iWidth) ? 0 : (iWidth/10);
//		iHeight = (CAR_DATA_SIZE_INVALID == iHeight) ? 0 : (iHeight/10);
//		String strCarSize = String.format("%d,%d,%d",iLen,iWidth,iHeight);
//
//		map.put("car_info", strCarLimit + "," + strCarSize);
		
		// set parking condition
		String pk_condition = "";
//        switch(SetupDM.GetInitialStatus(jniSetupControl.STUPDM_PARK_USE_TYPE)){
//            case jniSetupControl.STUPDM_PARK_ALL:
//            	pk_condition = "0129";
//                break;
//            case jniSetupControl.STUPDM_PARK_ONLY_EMPTY:
//            	pk_condition = "0";
//                break;
//            case jniSetupControl.STUPDM_PARK_EMPTY_AND_BUSY:
//            	pk_condition = "01";
//                break;
//            default:
            	pk_condition = "0129";
//                break;
//        }
		map.put("pk_condition", pk_condition);
		
		// set tv spot start date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		java.util.Date date = new java.util.Date();
		date.setYear(date.getYear() - 1);
		String cur_date = sdf.format(date);  
		map.put("tvspot_start_date", cur_date);

		return map;
	}
}
