package com.billionav.navi.datasynccontrol.Response;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

import com.billionav.navi.datasynccontrol.DataSyncControl_AmebaData;
import com.billionav.navi.datasynccontrol.DataSyncControl_CommonVar;
import com.billionav.navi.datasynccontrol.DataSyncControl_ManagerIF;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;
import com.billionav.navi.usercontrol.Response.UserControl_ResponseBase;

public class DataSyncControl_ResponseAmeba extends DataSyncControl_ResponseFileBase{
	
	private int m_iResultCode = UC_RESPONES_SRV_FAIL;
	private DataSyncControl_AmebaData data;
	
	public DataSyncControl_ResponseAmeba(int iRequestId) {
		super(iRequestId);
	}
	
	public void doResponse() {
		NSTriggerInfo cTrigger = new NSTriggerInfo();
		
		cTrigger.m_iTriggerID = DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_GETAMEBA;
		int errCode = getResCode();	
		if (PResponse.RES_CODE_NO_ERROR != errCode) {
			cTrigger.SetlParam1(UserControl_ResponseBase.UC_RESPONES_SRV_FAIL);
			cTrigger.SetlParam2(errCode);
		}
		else {
			byte[] receiveData = getReceivebuf();
			if (ParseResultInfo(receiveData)) {
				m_iResultCode = UC_RESPONES_SUC;
			}
			cTrigger.SetlParam1(m_iResultCode);
		}
		MenuControlIF.Instance().TriggerForScreen(cTrigger);
	}
	
	
	public boolean ParseResultInfo(byte[] inputData) {
		String strData = new String(inputData);
		
		return PraseXML(strData);
		
	}
	
	public  boolean PraseXML(String xml)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new InputSource(new StringReader(xml)));
			NodeList responseList = dom.getElementsByTagName("Response");
			if ( null == responseList || 0 == responseList.getLength() ) {
				return false;
			}
			Element response = (Element)responseList.item(0);
			
			
			data = new DataSyncControl_AmebaData();
			String str = "";
			//car_id
			String car_id =  GetNodeContext(response,"car_id");
			data.setStrCar_id(car_id);
			//model_id
			String model_id =  GetNodeContext(response,"model_id");
			data.setStrModel_id(model_id);
			//driver_id
			String driver_id =  GetNodeContext(response,"driver_id");
			data.setStrDriver_id(driver_id);
			//timestamp_response
			String timestamp_response =  GetNodeContext(response,"timestamp_response");
			data.setStrTimestamp_response(timestamp_response);
			//timestamp_car_status
			String timestamp_car_status =  GetNodeContext(response,"timestamp_car_status");
			data.setStrTimestamp_car_status(timestamp_car_status);
			//KML Part
			PraseKML(response);
			
			DataSyncControl_ManagerIF.Instance().setM_cAmebaData(data);
		} 
		catch (Exception  e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public  boolean PraseKML(Element root) {
		NodeList kmlList = root.getElementsByTagName("kml");
		if (null == kmlList || 0 == kmlList.getLength() ) {
			return false;
		}
		
		Element kmlNode = (Element)kmlList.item(0);
		if (null == kmlNode) {
			return false;
		}
		NodeList DocumentList = kmlNode.getElementsByTagName("Document");
		if (null == DocumentList || 0 == DocumentList.getLength()) {
			return false;
		}
		Element Doc = (Element)DocumentList.item(0);
		if (null == Doc) {
			return false;
		}
/*		
		String name = GetNodeContext(Doc,"name");
		String open = GetNodeContext(Doc, "open");
		
		//Prase Tag <Style>
		NodeList styleList = Doc.getElementsByTagName("Style");
		if (null != styleList && styleList.getLength() > 0) {
			Element styleNode = (Element) styleList.item(0);
			//PolyStyle
			NodeList polyStyleList = styleNode.getElementsByTagName("PolyStyle");
			if (null != polyStyleList && polyStyleList.getLength() > 0) {
				Element polyStyleNode = (Element) polyStyleList.item(0);
				String color = GetNodeContext(polyStyleNode, "color");
				String colorMode = GetNodeContext(polyStyleNode, "colorMode");
			}
			
			//LineStyle
			NodeList lineStyleList = styleNode.getElementsByTagName("LineStyle");
			if (null != lineStyleList && lineStyleList.getLength() > 0) {
				Element lineStyleNode = (Element) lineStyleList.item(0);
				String color = GetNodeContext(lineStyleNode, "color");
				String colorMode = GetNodeContext(lineStyleNode, "width");
			}
		}
*/		
		//Prase Tag mutiple <Placemark> only prase the polygon
		NodeList placemarkList = Doc.getElementsByTagName("Placemark");
		if (null == placemarkList || 0 == placemarkList.getLength()) {
			return false;
		}
		StringBuffer stb = new StringBuffer();
//		for (int i = 0; i < placemarkList.getLength(); i ++) {
			Element placemarkNode = (Element)placemarkList.item(0);
			NodeList ploygonList = placemarkNode.getElementsByTagName("Polygon");
			if (null != ploygonList && ploygonList.getLength() > 0) {
				for (int index = 0; index < ploygonList.getLength(); index ++) {
					Element ploygonNode = (Element)ploygonList.item(index); 
					if (null != ploygonNode) {
						
						NodeList outerBoundaryIsList = ploygonNode.getElementsByTagName("outerBoundaryIs");
						if ( null != outerBoundaryIsList && outerBoundaryIsList.getLength() > 0) {
							Element outerBoundaryIsNode = (Element)outerBoundaryIsList.item(0);
							if (null != outerBoundaryIsNode) {
								NodeList LinearRingList = outerBoundaryIsNode.getElementsByTagName("LinearRing");
								if (null != LinearRingList && LinearRingList.getLength() > 0) {
									Element LinearRingNode = (Element) LinearRingList.item(0);
									if (null != LinearRingNode) {
										String lonLatList = GetNodeContext(LinearRingNode, "coordinates");
										stb.append(lonLatList.trim());
										if (index != ploygonList.getLength() - 1){
											stb.append(DataSyncControl_ManagerIF.m_strMutiplePolyGonSplit);
										}
									}
								}
							}
						}
//						
//						String lonLatList = GetNodeContext(ploygonNode, "coordinates");
//						stb.append(lonLatList);
//						if (i != placemarkList.getLength() - 1){
//							stb.append(DataSyncControl_ManagerIF.m_strMutiplePolyGonSplit);
//						}
					}
				}
			}
//		}
		data.setStrMutiplePolygonsLonLat(stb.toString());
		Log.v("LonLat", "AmebaLonLat:" + data.getStrMutiplePolygonsLonLat());
		return true;
	}
	
	public  String GetNodeContext(Element startNode, String subNodeName) {
		if (null != startNode) {
			NodeList nodeList = startNode.getElementsByTagName(subNodeName);
			if (null != nodeList && nodeList.getLength() > 0) {
				Element node = (Element)nodeList.item(0);
				//
				String nodeText = node.getTextContent();
				return nodeText;
			}
		}
		return "";
	}

}
