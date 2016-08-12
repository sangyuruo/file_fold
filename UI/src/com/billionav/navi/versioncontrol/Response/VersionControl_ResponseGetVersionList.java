package com.billionav.navi.versioncontrol.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.versioncontrol.VersionControl_CommonVar;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;
import com.billionav.navi.versioncontrol.VersionControl_VersionComparator;
import com.billionav.navi.versioncontrol.VersionControl_VersionDataFormat;

public class VersionControl_ResponseGetVersionList extends VersionControl_ResponseBase{
	private int iStatus = UC_RESPONES_SUC;
	private int iDetails = 0;

	public VersionControl_ResponseGetVersionList(int iRequestId) {
		super(iRequestId);
		//test();
	}
	
	public void doResponse() {
		int iResCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			try {
				byte[] receiveData = getReceivebuf();
				String strData = new String(receiveData);
				JSONObject jObject;
				jObject = new JSONObject(strData);
				Iterator<String> keyIter = jObject.keys();
				String key = null;
				JSONObject obj = null;
				VersionControl_ManagerIF.Instance().getM_cVersionList().clear();
				
				while (keyIter.hasNext()) {
					VersionControl_VersionDataFormat data = new VersionControl_VersionDataFormat();
					
					key = (String) keyIter.next();
					obj = (JSONObject) jObject.get(key);
					
					data.setStrVerSion(key);
					if (obj.has(VersionControl_VersionDataFormat.APIKEY)) {
						String strAPKName = obj.getString(VersionControl_VersionDataFormat.APIKEY);
						data.setStrAPKName(strAPKName);
						data.setStrAPKDownLoadURL(VersionControl_ManagerIF.Instance().getM_strVersionServerUrl() + strAPKName);
					}
					if (obj.has(VersionControl_VersionDataFormat.METADATAKEY)) {
						String strMetaDataName = obj.getString(VersionControl_VersionDataFormat.METADATAKEY);
						data.setStrMetaData(strMetaDataName);
						data.setStrMetaDataDownLoadURL(VersionControl_ManagerIF.Instance().getM_strVersionServerUrl() +strMetaDataName);
					}
					VersionControl_ManagerIF.Instance().getM_cVersionList().add(data);
				}
				Comparator comparator = new VersionDataFormatcomparator();
				Collections.sort(VersionControl_ManagerIF.Instance().getM_cVersionList(), comparator);
				DebugSortedListInfo(VersionControl_ManagerIF.Instance().getM_cVersionList());
				
				if (VersionControl_ManagerIF.Instance().getM_cVersionList().size() > 0) {
					VersionControl_VersionDataFormat cLatestVersionInfo = VersionControl_ManagerIF.Instance().getM_cVersionList().get(0);
					VersionControl_ManagerIF.Instance().setM_cLatestVersionInfo(cLatestVersionInfo);
				} else {
					iDetails = UC_DETAILS_NO_VERSION;
				}
				
			} catch (JSONException e) {
				iStatus =  UC_RESPONES_LOC_FAIL;
				iDetails = UC_DETAILS_JSON_PRASE_ERROR;
				Log.d("[VersionControl]", "[VersionControl]: GetVersionInfoList Response Exception");
				e.printStackTrace();
			} catch (Exception ex) {
				iStatus =  UC_RESPONES_LOC_FAIL;
				iDetails = UC_DETAILS_SERVER_DATA_PRASE_ERROR;
				Log.d("[VersionControl]", "[VersionControl]: GetVersionInfoList Response Prase Data Exception");
				ex.printStackTrace();
			}
		} else {
			iStatus = UC_RESPONES_SRV_FAIL;
			iDetails = iResCode;
		}
		
		Log.d("[VersionControl]", "[VersionControl]: GetVersionInfoList Response iStatus = " + iStatus + " iDetails=" +iDetails);
		NSTriggerInfo cInfo = new NSTriggerInfo();
		cInfo.SetTriggerID(VersionControl_CommonVar.UIC_MN_TRG_VC_GET_VERSION_LIST);
		cInfo.SetlParam1(iStatus);
		cInfo.SetlParam2(iDetails);
		MenuControlIF.Instance().TriggerForScreen(cInfo);
	}
	
	public void test(){
///////////////////test
		ArrayList<VersionControl_VersionDataFormat> testList = new ArrayList<VersionControl_VersionDataFormat>();
		VersionControl_VersionDataFormat data01 = new VersionControl_VersionDataFormat();
		data01.setStrVerSion("0.0.1");
		VersionControl_VersionDataFormat data02 = new VersionControl_VersionDataFormat();
		data02.setStrVerSion("0.0.1.build1");
		VersionControl_VersionDataFormat data03 = new VersionControl_VersionDataFormat();
		data03.setStrVerSion("0.0.1.build2");
		VersionControl_VersionDataFormat data04 = new VersionControl_VersionDataFormat();
		data04.setStrVerSion("0.0.2");
		VersionControl_VersionDataFormat data05 = new VersionControl_VersionDataFormat();
		data05.setStrVerSion("0.0.2.build3");
		VersionControl_VersionDataFormat data06 = new VersionControl_VersionDataFormat();
		data06.setStrVerSion("0.0.2.daily20120701");
		VersionControl_VersionDataFormat data07 = new VersionControl_VersionDataFormat();
		data07.setStrVerSion("0.0.2.daily20120702");
		VersionControl_VersionDataFormat data08 = new VersionControl_VersionDataFormat();
		data08.setStrVerSion("0.0.3");
		VersionControl_VersionDataFormat data09 = new VersionControl_VersionDataFormat();
		data09.setStrVerSion("0.0.3.daily20120708");
		VersionControl_VersionDataFormat data010 = new VersionControl_VersionDataFormat();
		data010.setStrVerSion("0.0.3.build7");
		VersionControl_VersionDataFormat data011 = new VersionControl_VersionDataFormat();
		data011.setStrVerSion("0.0.3.build4");
		VersionControl_VersionDataFormat data012 = new VersionControl_VersionDataFormat();
		data012.setStrVerSion("0.0.4");
		
		testList.add(data01);
		testList.add(data02);
		testList.add(data03);
		testList.add(data04);
		testList.add(data05);
		testList.add(data06);
		testList.add(data07);
		testList.add(data08);
		testList.add(data09);
		testList.add(data010);
		testList.add(data011);
		testList.add(data012);
		Comparator comparator = new VersionDataFormatcomparator();
		Collections.sort(testList,comparator);
		
		DebugSortedListInfo(testList);
	}
	
	/**
	 * 
	 * @author chenyong
	 * @sample 
	 * 	1.0 -> 初始release
 	 *	1.1 -> 新功能1release
 	 *	1.1.0.build1234 -> 内部weekly release build, build版本是流水号
 	 *	1.1.0.build1235 -> 内部weekly release build，外部公开
 	 *	1.1.1 -> bug修正，从1.1.0.build1235bump而来
 	 *	1.1.2 -> 再次bug修正
 	 *	1.2 -> 新功能2release
 	 *	1.2.1 -> bug修正
	 *
	 */
	public class VersionDataFormatcomparator implements Comparator{

	    public int compare(Object o1,Object o2) {
	    	
	    	VersionControl_VersionDataFormat p1=(VersionControl_VersionDataFormat)o1;
	    	VersionControl_VersionDataFormat p2=(VersionControl_VersionDataFormat)o2;
	    	String strVersion01 = p1.getStrVerSion();
	    	String strVersion02 = p2.getStrVerSion();
	    	
	    	return VersionControl_VersionComparator.VersionSort(strVersion01, strVersion02);
	       }

	}
	
	public void DebugSortedListInfo(ArrayList<VersionControl_VersionDataFormat> list) {
		for (int index = 0; index < list.size(); index++) {
			VersionControl_VersionDataFormat data = list.get(index);
			Log.d("[VersionControl]", "[VersionControl]: GetVersionInfoList SortedListInfo index:" + index + " Version:" + data.getStrVerSion());
		}
	}
	
	
}
