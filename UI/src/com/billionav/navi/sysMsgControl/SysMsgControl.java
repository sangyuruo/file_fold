package com.billionav.navi.sysMsgControl;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.billionav.jni.FileSystemJNI;
import com.billionav.navi.component.dialog.CustomSelectableDialog;
import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.system.PLog;
import com.billionav.navi.uitools.MessageTools;
import com.billionav.navi.uitools.SystemTools;

public class SysMsgControl {
	private static final String TAG = "SysMsgControl";
	private static final String XML_ROOT_TAG = "result";
	private static final String XML_ERROR_TAG = "code";
	private static final String MSGCOUNT_TAG = "messages";
	private static final String MSG_TAG = "message";
	private static final String MSG_ID = "id";
	private static final String MSG_TITLE = "title";
	private static final String MSG_STARTDATE = "startDate";
	private static final String MSG_DUEDATE = "dueDate";
	
	private String receiveDataToWrite = null;
	
	public static final String SYS_NOTIFICATION_KEY = "notificationXML";
	private static final String path = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH)+"SYSMSG/sample.xml";
	
	private final int notifyID = 0x1001;
	
	private ArrayList<SysMessageFormat> msgList = new ArrayList<SysMessageFormat>();
	
	private static SysMsgControl instance;
	
	
	public static SysMsgControl getInstance(){
		if(null == instance){
			instance = new SysMsgControl();
		}
		return instance;
	}
	
	private SysMsgControl() {
	}
	
	

	//-------------------------XML Parser-----------------------------//
	/*
	 * parse XML files By Dom
	 */
	private boolean parseXML2List(String receiveData){
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.parse(new InputSource(new StringReader(receiveData)));
			
			Node nod = dom.getFirstChild();
			if(!isXMLVailed(nod)){
				return false;
			}
			
			NodeList items = dom.getElementsByTagName(MSG_TAG);
			
			if (0 == items.getLength()) {
				//no Data Download succeed
				return false;
			}
			
			msgList.clear();
			for(int i = 0; i < items.getLength(); ++i){
				if(null != items.item(i)){
					msgList.add(getMsgFromNode(items.item(i)));
				}
			}
		} catch (Exception e) {
			PLog.e(TAG, "["+TAG+"]"+"parseXML2List Error");
			e.printStackTrace();
			return false;
		} finally {
			builder = null;
			factory = null;
		}
		receiveDataToWrite = receiveData;
		return true;
	}
	/*
	 * convert XML nodes to SysMessageFormat
	 */
	private SysMessageFormat getMsgFromNode(Node node){
		if (null == node) {
			return null;
		}
		Element nodeElement = (Element) node;
		SysMessageFormat msgBody = new SysMessageFormat();
		int id = 0;
		try{
			id = Integer.parseInt(nodeElement.getAttribute(MSG_ID));
		} catch(Exception e){
		}
		msgBody.setM_intMsgID(id);
		msgBody.setM_StrTitle(nodeElement.getAttribute(MSG_TITLE));
		msgBody.setM_Strm_StrStartDate(nodeElement.getAttribute(MSG_STARTDATE));
		msgBody.setM_StrDueDate(nodeElement.getAttribute(MSG_DUEDATE));
		msgBody.setM_StrContent(node.getTextContent().trim());

//		System.out.println(nodeElement.getAttribute(MSG_TITLE) + nodeElement.getAttribute(MSG_STARTDATE)+
//				node.getTextContent().trim());
		return msgBody;
	}
	
	private boolean isXMLVailed(Node nod) {
		if(!XML_ROOT_TAG.equals(nod.getNodeName())){
			System.out.println("Error !!! Node name = "+nod.getNodeName());
			return false;
		} else  {
			String errorCode = ((Element)nod).getAttribute(XML_ERROR_TAG);
			if(!"0".equals(errorCode)){
				System.out.println("Error !!! error code = "+errorCode);
				return false;
			}
		}
		return true;
	}
	//-------------------------XML Parser end-----------------------------//
	
	//---------------------interfaces---------------------------//
	private long requestID = -1;
	public long requestSysMsgFromServer(){
		if(requestID < 0){
			requestID = SysMsgControl_ManagerIF.getInstance().requestSysMsgFromServer();
		}
		return requestID;
	}
	
	public boolean XMLParser(byte[] receiveData){
		String strData = new String(receiveData);
		return parseXML2List(strData);
	}
	
	public boolean XMLParser(String receiveData){
		return parseXML2List(receiveData);
	}
	
	public ArrayList<SysMessageFormat> getMsgList(){
		return msgList;
	}
	
	public void ListMsgContent(){
		for(SysMessageFormat msg:msgList){
			Log.d("test",msg.toString());
		}
	}
	
	public void sendSystemNotification(){
		SystemTools.writeFileContext(path, receiveDataToWrite);
		sendNotificationToNotificationBar();
	}
	
	//---------------------interfaces  end---------------------------//
	
	//---------------------notification---------------------------//
	private static final String NOTIFICATION_STRING = "System Maintain Information";
	private void sendNotificationToNotificationBar(){
//		NotificationManager mNotificationManager = (NotificationManager) NSViewManager.GetViewManager().getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = new Notification(NOTIFICATION_ICON, NOTIFICATION_STRING, System.currentTimeMillis());
////		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		
		Intent notificationIntent = new Intent(NSViewManager.GetViewManager(), CustomSelectableDialog.class);
		
		notificationIntent.putExtra(SYS_NOTIFICATION_KEY, path);
		PendingIntent contentIntent = PendingIntent.getActivity(MenuControlIF.Instance().GetCurrentActivity(), 0, notificationIntent,0);
//		notification.contentIntent = contentIntent;
//		mNotificationManager.notify(notifyID , notification);
//		MessageTools.getInstance().setPendingIntent(contentIntent);
		int size = msgList.size();
		String message = "";
		if(size > 0 && msgList.get(size-1) != null){
			
			message = msgList.get(size-1).getM_StrContent();
		}
		MessageTools.getInstance().sendMessageToSystemBar(notifyID, TextUtils.isEmpty(message)?NOTIFICATION_STRING:message);
	}
	//---------------------notification  end---------------------------//
	
}
