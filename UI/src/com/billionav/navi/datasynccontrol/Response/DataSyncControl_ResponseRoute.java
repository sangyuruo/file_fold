package com.billionav.navi.datasynccontrol.Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.billionav.jni.FileSystemJNI;
import com.billionav.navi.datasynccontrol.DataSyncControl_CommonVar;
import com.billionav.navi.datasynccontrol.DataSyncControl_ManagerIF;
import com.billionav.navi.datasynccontrol.DataSyncControl_RouteInfo;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSTriggerInfo;
import com.billionav.navi.net.PResponse;

public class DataSyncControl_ResponseRoute extends DataSyncControl_ResponseBase{
	private int m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_SUC;
	private DataSyncControl_RouteInfo m_RouteInfo = new DataSyncControl_RouteInfo();
	int iResCode = 0;
	private String m_StrFilePath = "";
	private String m_StrFileName = "";
	
	public DataSyncControl_ResponseRoute(int iRequestId) {
		super(iRequestId);
		m_StrFilePath = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH);
		m_StrFilePath += DataSyncControl_CommonVar.DATASYNC_ROUTE_STORE_SUBPATH;
		m_StrFileName = DataSyncControl_CommonVar.DATASYNC_ROUTE_STORE_NAME;
	}
	
	public void doResponse() {
		if (DataSyncControl_CommonVar.SYNC_PATH_MEHTOD_GETSTATUS.equals(getM_StrPassParam01())) {
			doResponseOnQuery();
		} else if (DataSyncControl_CommonVar.SYNC_PATH_MEHTOD_GETPATH.equals(getM_StrPassParam01())) {
			doResponseOnGetPath();
		} else {
			
		}
	}
	
	public void doResponseOnQuery(){
		iResCode = getResCode();
		
		NSTriggerInfo cInfo = new NSTriggerInfo();
		cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_GETROUTESTATUS);
		
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			if (ParseResultInfoOnQuery(getReceivebuf())) {
				
			} else {
				m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_SRV_FAIL;
				iResCode = DataSyncControl_ResponseBase.UC_DETAILS_XML_PRASE_ERROR;
			}
		} else {
			m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_SRV_FAIL;
		}
		cInfo.SetlParam1(m_iResultCode);
		cInfo.SetlParam2(iResCode);
		
		MenuControlIF.Instance().TriggerForScreen(cInfo);
	}
	
	public void doResponseOnGetPath(){
		iResCode = getResCode();
		
		NSTriggerInfo cInfo = new NSTriggerInfo();
		cInfo.SetTriggerID(DataSyncControl_CommonVar.DATASYNC_TRIGGLEID_GETROUTE_SYNC_RESULT);
		
		if (PResponse.RES_CODE_NO_ERROR == iResCode) {
			if (ParseResultInfoOnGetRoute(getReceivebuf())) {
				
			} else {
				m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_SRV_FAIL;
				iResCode = DataSyncControl_ResponseBase.UC_DETAILS_XML_PRASE_ERROR;
			}
		} else {
			m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_SRV_FAIL;
		}
		
		cInfo.SetlParam1(m_iResultCode);
		cInfo.SetlParam2(iResCode);
		cInfo.SetString1(m_StrFilePath + m_StrFileName);
		
		MenuControlIF.Instance().TriggerForScreen(cInfo);
	}
	
	public boolean ParseResultInfoOnQuery(byte[] inputData) {
		
		if (null == inputData) {
			return false;
		}
		
		String strData = new String(inputData);
		
		if ("".equals(strData)) {
			return false;
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new InputSource(new StringReader(strData)));
//			Element root = dom.getDocumentElement();
//			NodeList items = root.getElementsByTagName("result");
			
			NodeList items = dom.getElementsByTagName("result");
			if (0 == items.getLength()) {
				return false;
			}
			
			Element element = (Element) items.item(0);
			Element root = element;
			String value = element.getAttribute("code");
			
			//0: successful
			//1: fail
			if(0 == value.compareTo("0")) {
				m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_SUC;
				
				NodeList remarkItems = root.getElementsByTagName("path");
				if (null != remarkItems && remarkItems.getLength() > 0) {
					Element remarkElement = (Element)remarkItems.item(0);
					if (null != remarkElement ) {
						String strName = remarkElement.getAttribute("name");    
						String strNewDownload = remarkElement.getAttribute("new-download");
						String strPath = remarkElement.getAttribute("path");
						
						m_RouteInfo.setStrName(strName);
						SetStatusOnQuery(strNewDownload);
					}
				} else {
					m_RouteInfo.setRouteStatus((int)DataSyncControl_CommonVar.SYNCROUTESTATUS_NO_ROUTE);
				}
				DataSyncControl_ManagerIF.Instance().setM_cRouteInfo(m_RouteInfo);
			}
			else {
				m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_SRV_FAIL;
			}
		} 
		catch (Exception  e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean ParseResultInfoOnGetRoute(byte[] inputData) {
		
		if (null == inputData) {
			return false;
		}
		
		String strData = new String(inputData);
		
		if ("".equals(strData)) {
			return false;
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new InputSource(new StringReader(strData)));
//			Element root = dom.getDocumentElement();
//			NodeList items = root.getElementsByTagName("result");
			NodeList items =  dom.getElementsByTagName("result");
			if (0 == items.getLength()) {
				return false;
			}
			
			Element element = (Element) items.item(0);
			
			if (null == element) {
				return false;
			}
			Element root = element;
			String value = element.getAttribute("code");
			
			//0: successful
			//1: fail
			if(0 == value.compareTo("0")) {
				m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_SUC;
				
				NodeList remarkItems = root.getElementsByTagName("path");
				if (null != remarkItems && remarkItems.getLength() > 0) {
					Element remarkElement = (Element)remarkItems.item(0);
					if (null != remarkElement ) {
						String strName = remarkElement.getAttribute("name");    
						String strNewDownload = remarkElement.getAttribute("new-download");
						String strPath = remarkElement.getAttribute("path");
						
						m_RouteInfo.setStrName(strName);
						SavePathFileOnGetPath(m_StrFilePath,m_StrFileName, strPath);
					}
				} else {
					m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_SRV_FAIL;
					iResCode = DataSyncControl_ResponseBase.UC_DETAILS_XML_PRASE_ERROR;
				}
				DataSyncControl_ManagerIF.Instance().setM_cRouteInfo(m_RouteInfo);
			}
			else {
				m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_SRV_FAIL;
				if (null != root) {
					NodeList errorItems = root.getElementsByTagName("error");
					if (null != errorItems && errorItems.getLength() > 0) {
						Element error = (Element)errorItems.item(0);
						if (null != error) {
							String code = error.getAttribute("code");
							if (null != code && !"".equals(code)) {
								iResCode = Integer.parseInt(code);
							}
						}
					}
				}
			}
		} 
		catch (Exception  e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void SetStatusOnQuery(String strNewDownload) {
		if (!"".equals(strNewDownload)) {
			int iNewDownload = Integer.parseInt(strNewDownload);
			if (0 == iNewDownload) {
				m_RouteInfo.setRouteStatus((int)DataSyncControl_CommonVar.SYNCROUTESTATUS_GETROUTE_NOT_FIRST_TIME);
			}else if (1 == iNewDownload) {
				m_RouteInfo.setRouteStatus((int)DataSyncControl_CommonVar.SYNCROUTESTATUS_GETROUTE_FIRST_TIME);
			}
			m_RouteInfo.setiNewDownload(iNewDownload);	
		}
	}

	private void SavePathFileOnGetPath(String strPath, String strFileName, String strXMLPath) {
		
		boolean bSaveStatus = true;
		if (null == strPath || "".equals(strPath) || null == strFileName || "".equals(strFileName)) {
			bSaveStatus = false;
			return ;
		}
		
		String strFileFullPath = strPath + strFileName;
		
		if (IsFileExist(strFileFullPath)) {
			if (DeleteFiles(strFileFullPath)){
				
			}else {
				bSaveStatus = false;
			}
			if (StoreFile(strFileFullPath, strXMLPath)){
				
			} else {
				bSaveStatus = false;
			}
		} else {
			CreateDirs(strPath);
			if (CreateFiles(strFileFullPath)) {
				
			} else {
				bSaveStatus = false;
				return ;
			}
			
			if (StoreFile(strFileFullPath, strXMLPath)){
				
			} else {
				bSaveStatus = false;
			}
		}
		
		if (!bSaveStatus) {
			m_iResultCode = DataSyncControl_ResponseBase.UC_RESPONES_LOC_FAIL;
			iResCode = DataSyncControl_CommonVar.SYNCGETPATH_DETAILS_ERROR_SAVEFILE_ERROR;
		}
		

	}
	
	private boolean IsFileExist(String strFileFullPath) {
		File file = new File(strFileFullPath);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean CreateFiles(String strFileFullPath) {
		File file = new File(strFileFullPath);
		if(!file.exists())    
		{    
		    try {    
		        file.createNewFile();
		    } catch (IOException e) {    
		        // TODO Auto-generated catch block    
		        e.printStackTrace(); 
		        return false;
		    }
		}    
		return true;
	}
	
	private boolean DeleteFiles(String strFileFullPath) {
		File file = new File(strFileFullPath);
		if(file.exists())    
		{    
		   return file.delete(); 
		}    
		return false;
	}
	
	private boolean CreateDirs(String fileDir) {
		File file = new File(fileDir);
		if  (!file.exists())      
		{       
		    file.mkdirs();    
		} else   
		{  
			return false;
		}  
		return true;
	}
	
	private boolean StoreFile(String fileFullPathName,String path) {
		try {
			FileWriter fw = new FileWriter(fileFullPathName);
			fw.write(path);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}