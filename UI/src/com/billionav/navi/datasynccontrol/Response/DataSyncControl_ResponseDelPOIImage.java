package com.billionav.navi.datasynccontrol.Response;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.billionav.navi.net.PResponse;

public class DataSyncControl_ResponseDelPOIImage extends DataSyncControl_ResponseFileBase{
	private String strTimestamp = "";
	private String strUUID = "";
	private String strRecordStatus = "";
	private String strRecordKind = "";
	private String strPOIImage = "";
	
	public DataSyncControl_ResponseDelPOIImage(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		
		int errCode = getResCode();	
		if (PResponse.RES_CODE_NO_ERROR != errCode) {
			//Update Error
//			jniPointControl_new.Instance().PrepareGetNextImageInfo(jniPointControl_new.PNT_SYNC_IMAGE_STATES_ERROR,getM_StrPassParam01());
		}
		else {
			byte[] receiveData = getReceivebuf();
			if (ParseResultInfo(receiveData)) {
				//success
//				jniPointControl_new.Instance().PrepareGetNextImageInfo(jniPointControl_new.PNT_SYNC_IMAGE_STATES_SUCESS,getM_StrPassParam01());
			} else {
				//error
//				jniPointControl_new.Instance().PrepareGetNextImageInfo(jniPointControl_new.PNT_SYNC_IMAGE_STATES_PARSE_ERROR,getM_StrPassParam01());
			}
		}
		
	}

	public boolean ParseResultInfo(byte[] inputData) {
		String strData = new String(inputData);		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new InputSource(new StringReader(strData)));
			
			NodeList items = dom.getElementsByTagName("pset_regpoint_server_root");
			if (0 == items.getLength()) {
				return false;
			}			
			Element element = (Element) items.item(0);
			String strResult = GetNodeValue(element, "result");
			strTimestamp = GetNodeValue(element, "server_timestamp");
			strUUID = GetNodeValue(element, "uuid");
			strRecordStatus = GetNodeValue(element, "recordstatus");
			strRecordKind = GetNodeValue(element, "recordkind");
			if ("0".equals(strResult)) {
				strPOIImage = GetNodeValue(element, "image");
				return true;
			} else {
				return false;
			}
		} 
		catch (Exception  e) {
			e.printStackTrace();
			return false;
		}
	}
	
	String GetNodeValue(Element ele, String tag)
	{
		NodeList list = ele.getElementsByTagName(tag);
		if (null == list) {
			return null;
		}
		if (0 == list.getLength()) {
			return null;
		}
		Element node = (Element) list.item(0);
		return node.getTextContent();
	}
	
	public String getStrTimestamp() {
		return strTimestamp;
	}

	public void setStrTimestamp(String strTimestamp) {
		this.strTimestamp = strTimestamp;
	}

	public String getStrUUID() {
		return strUUID;
	}

	public void setStrUUID(String strUUID) {
		this.strUUID = strUUID;
	}

	public String getStrRecordStatus() {
		return strRecordStatus;
	}

	public void setStrRecordStatus(String strRecordStatus) {
		this.strRecordStatus = strRecordStatus;
	}

	public String getStrRecordKind() {
		return strRecordKind;
	}

	public void setStrRecordKind(String strRecordKind) {
		this.strRecordKind = strRecordKind;
	}

	public String getStrPOIImage() {
		return strPOIImage;
	}

	public void setStrPOIImage(String strPOIImage) {
		this.strPOIImage = strPOIImage;
	}
	
}

