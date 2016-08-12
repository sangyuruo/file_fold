package com.billionav.navi.versioncontrol.Response;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.versioncontrol.VersionControl_CommonVar;
import com.billionav.navi.versioncontrol.VersionControl_ManagerIF;
import com.billionav.navi.versioncontrol.VersionControl_MetaDataFormat;

public class VersionControl_ResponseGetMetaDataInfo extends VersionControl_ResponseBase{
	private int iStatus = UC_RESPONES_SUC;
	private int iDetails = 0;
	private static String XML_ROOT_KEY = "app-info";
	private static String XML_ITEM_KEY = "item";
	private static String XML_PARAM01_KEY = "key";
	private static String XML_PARAM02_KEY = "value";
	
	public VersionControl_ResponseGetMetaDataInfo(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		int iResCode = getResCode();
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			try{
				byte[] receiveData = getReceivebuf();
				String strData = new String(receiveData);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				builder = factory.newDocumentBuilder();
				Document dom = builder.parse(new InputSource(new StringReader(strData)));
				NodeList roots = dom.getElementsByTagName(XML_ROOT_KEY);
				VersionControl_ManagerIF.Instance().getM_cMetaDataList().clear();
				if (roots.getLength() > 0) {
					Element root = (Element)roots.item(0);
					NodeList items = root.getElementsByTagName(XML_ITEM_KEY);
					for(int index = 0; index > items.getLength(); index++) {
						VersionControl_MetaDataFormat data = new VersionControl_MetaDataFormat();
						Element item = (Element)items.item(index);
						String key = item.getAttribute(XML_PARAM01_KEY);
						String value = item.getAttribute(XML_PARAM02_KEY);
						data.setStrKey(key);
						data.setStrValue(value);
						VersionControl_ManagerIF.Instance().getM_cMetaDataList().add(data);
					}
					DebugListInfo(VersionControl_ManagerIF.Instance().getM_cMetaDataList());
				} else {
					Log.d("[VersionControl]", "[VersionControl]: GetMetaDataInfo Response Format Error ");
					iStatus = UC_RESPONES_LOC_FAIL;
					iDetails = UC_DETAILS_XML_PRASE_ERROR;
				}
			}catch (Exception ex) {
				ex.printStackTrace();
				Log.d("[VersionControl]", "[VersionControl]: GetMetaDataInfo Response Prase XML Error ");
				iStatus = UC_RESPONES_LOC_FAIL;
				iDetails = UC_DETAILS_XML_PRASE_ERROR;
			}
		} else {
			iStatus = UC_RESPONES_SRV_FAIL;
			iDetails = iResCode;
		}
		
		Log.d("[VersionControl]", "[VersionControl]: GetMetaDataInfo Response iStatus = " + iStatus + " iDetails=" +iDetails);
		NSTriggerInfo cInfo = new NSTriggerInfo();
		cInfo.SetTriggerID(VersionControl_CommonVar.UIC_MN_TRG_UC_GET_METADATA_INFO);
		cInfo.SetlParam1(iStatus);
		cInfo.SetlParam2(iDetails);
		MenuControlIF.Instance().TriggerForScreen(cInfo);
		
	}
	
	private void DebugListInfo(ArrayList<VersionControl_MetaDataFormat> list) {
		if(null == list) {
			return;
		}
		for (int index = 0; index < list.size(); index++) {
			VersionControl_MetaDataFormat item = list.get(index);
			Log.d("[VersionControl]", "[VersionControl]: GetMetaDataInfo ITEM: Index: " + index + " " +item.toString());
		}
	}

}
