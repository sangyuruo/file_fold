package com.billionav.navi.uicommon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.bluetooth.BluetoothAdapter;
import android.os.Environment;

import com.billionav.jni.FileSystemJNI;
import com.billionav.navi.system.PLog;
import com.billionav.navi.uicommon.btevent.UIC_BTEventAdapter;
import com.billionav.navi.uicommon.btevent.impls.UIC_BTEventForMap;

public class UIC_CradleCommon {
	public static final String FILE_PATH = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_USER_PATH)
				+"/RW/BTBackup/";
	public static final String FILE_NAME = "BT_Backup_List.xml";
	public static boolean hasConnected = true;

	private static boolean isManualConnectCradle = false;

	private UIC_CradleCommon() {
	}

	public static UIC_BTEventAdapter btInstance = UIC_BTEventForMap
			.getInstance();

	public static void onBluetoothStateChanged() {
		int state = BluetoothAdapter.getDefaultAdapter().getState();

		switch (state) {
		case BluetoothAdapter.STATE_ON: {
//			jniVP_SystemPlayerJava.BluetoothChange(jniVP_SystemPlayerJava.BLUETOOTH_ON);
			btInstance.onBluetooth();
			break;
		}
		case BluetoothAdapter.STATE_OFF: {
//			jniVP_SystemPlayerJava.BluetoothChange(jniVP_SystemPlayerJava.BLUETOOTH_OFF);
			btInstance.offBluetooth();
			break;
		}
		case BluetoothAdapter.STATE_TURNING_OFF: {
//			jniVP_SystemPlayerJava.BluetoothChange(jniVP_SystemPlayerJava.BLUETOOTH_TURNING_OFF);
//			if (new jniSetupControl()
//					.GetInitialStatus(jniSetupControl.STUPDM_LAST_GPS_SETTING) == jniSetupControl.STUPDM_COMMON_OFF
//					|| CLocationListener.GPS_CONNECT_STATUS_DISABLE == CLocationListener
//							.Instance().GetCurrentGpsConnectStatus()) {
//				PLog.v("LOCATION_UI", "not use gps");
//				CLocationListener.Instance().SwitchToDisable(true);
//			} else {
//				PLog.v("LOCATION_UI", "SwitchToInterGPS99");
//				CLocationListener.Instance().SwitchToInterGPS(true);
//			}
			btInstance.turningOffBluetooth();
			break;
		}
		case BluetoothAdapter.STATE_TURNING_ON:
			btInstance.turningOnBluetooth();
			break;
		}
	}

	public static boolean isManualConnectCradle() {
		return isManualConnectCradle;
	}

	public static void setIsManualConnectCradle(boolean isManualConnectCradle) {
		UIC_CradleCommon.isManualConnectCradle = isManualConnectCradle;
	}

//	public static ArrayList<BTDevice_Info> getBackupDevices() {
//		ArrayList<BTDevice_Info> list = new ArrayList<BTDevice_Info>();
//		DocumentBuilder builder;
//		Document doc;
//
//		try {
//			File f = new File(FILE_PATH + FILE_NAME);
//			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			doc = builder.parse(f);
//			NodeList nl = doc.getElementsByTagName("device");
//			for (int i = 0; i < nl.getLength(); i++) {
//				Element dev = (Element) nl.item(i);
//
//				BTDevice_Info d = new BTDevice_Info();
//				d.Name = dev.getAttribute("name");
//				d.Address = dev.getAttribute("address");
//				list.add(d);
//			}
//		} catch (Exception e) {
//			list.clear();
//			e.printStackTrace();
//		} finally {
//			builder = null;
//			doc = null;
//		}
//		return list;
//	}

//	public static BTDevice_Info getLastDeviceOfBackup() {
//		ArrayList<BTDevice_Info> backupList = UIC_CradleCommon
//				.getBackupDevices();
//		if (backupList == null || backupList.isEmpty()) {
//			return null;
//		} else {
//			return backupList.get(backupList.size() - 1);
//		}
//	}

//	public static boolean deleteDeviceFromBackup(BTDevice_Info device) {
//		ArrayList<BTDevice_Info> list = getBackupDevices();
//		for (int i = 0; i < list.size(); i++) {
//			if (compareDevice(list.get(i), device)) {
//				list.remove(i);
//				return writeXML(FILE_PATH, FILE_NAME, list);
//			}
//		}
//		return false;
//	}

	public static int getNumOfBackupList() {
		DocumentBuilder builder;
		Document doc;

		try {
			File f = new File(FILE_PATH + FILE_NAME);
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.parse(f);
			NodeList nl = doc.getElementsByTagName("device");
			return nl.getLength();
		} catch (Exception e) {
			return -1;
		} finally {
			builder = null;
			doc = null;
		}
	}

//	public static String getStringOfXML(ArrayList<BTDevice_Info> list) {
//		StringBuffer str = new StringBuffer();
//
//		str.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
//		str.append("\n");
//		str.append("<devices>");
//		str.append("\n");
//		for (int i = 0; i < list.size(); i++) {
//			str.append("  <device name=\"" + list.get(i).Name + "\" address=\""
//					+ list.get(i).Address + "\"/>");
//			str.append("\n");
//		}
//		str.append("</devices>");
//		str.append("\n");
//		return str.toString();
//	}

//	public static boolean writeXML(String path, String name,
//			ArrayList<BTDevice_Info> list) {
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//			File filePath = new File(path);
//
//			File saveFile = new File(filePath, name);
//			FileOutputStream outStream;
//			try {
//
//				if (!filePath.exists()) {
//					filePath.mkdirs();
//				}
//				if (!saveFile.exists()) {
//					saveFile.createNewFile();
//				}
//
//				outStream = new FileOutputStream(saveFile);
//				outStream.write(getStringOfXML(list).getBytes());
//				outStream.close();
//				return true;
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				outStream = null;
//				saveFile = null;
//				filePath = null;
//			}
//		}
//		return false;
//	}
//
//	public static boolean addDevice(BTDevice_Info device) {
//		if (device == null) {
//			return false;
//		}
//		ArrayList<BTDevice_Info> list = getBackupDevices();
//		for (int i = 0; i < list.size(); i++) {
//			if (compareDevice(list.get(i), device)) {
//				list.remove(i);
//				break;
//			}
//		}
//		list.add(device);
//		return writeXML(FILE_PATH, FILE_NAME, list);
//	}

//	public static ArrayList<BTDevice_Info> toMixList(
//			ArrayList<BTDevice_Info> list0, ArrayList<BTDevice_Info> list1) {
//		if (list0 == null || list1 == null) {
//			return null;
//		} else {
//			ArrayList<BTDevice_Info> list = new ArrayList<BTDevice_Info>();
//			for (BTDevice_Info o : list0) {
//				if (containsDevice(list1, o)) {
//					list.add(o);
//                }
//			}
//			return list;
//		}
//	}
//
//	public static int getNumOfMixDevices() {
//		ArrayList<BTDevice_Info> list = toMixList(getBackupDevices(),
//				getBondedDevices());
//		if (list == null || list.isEmpty()) {
//			return -1;
//		} else {
//			return list.size();
//		}
//	}
//
//	public static boolean autoConnect() {
//		ArrayList<BTDevice_Info> list = toMixList(getBackupDevices(),
//				getBondedDevices());
//		if (list == null || list.isEmpty()) {
//			hasConnected = false;
//			return false;
//		}
//		hasConnected = true;
//		BTDevice_Info d = list.get(list.size() - 1);
//		return CLocationListener.Instance()
//				.SwitchToCradleGPS(d);
//	}
//
//	public static void hasConnected(BTDevice_Info device) {
//		hasConnected = containsDevice(getBackupDevices(), device);
//	}
//
//	public static ArrayList<BTDevice_Info> getBondedDevices() {
//		ArrayList<HashMap<String, Object>> PairedList = CLocationListener
//				.Instance().GetBondedDevice();
//		if (PairedList == null) {
//			return null;
//		}
//
//		ArrayList<BTDevice_Info> list = new ArrayList<BTDevice_Info>();
//		for (int i = 0; i < PairedList.size(); i++) {
//			BTDevice_Info mBondedStr = (BTDevice_Info) PairedList.get(i).get(
//					"" + i);
//			list.add(mBondedStr);
//		}
//		return list;
//	}
//
//	public static boolean compareDevice(BTDevice_Info b1, BTDevice_Info b2) {
//		if (b1 == null || b2 == null) {
//			return false;
//		}
//
//		boolean isEqu = b1.Address.equals(b2.Address);
//		if (isEqu) {
//			if (b1.Name == null) {
//				b1.Name = b2.Name;
//			} else if (b2.Name == null) {
//				b2.Name = b1.Name;
//			}
//		}
//
//		return isEqu;
//	}
//
//	public static boolean containsDevice(ArrayList<BTDevice_Info> list,
//			BTDevice_Info b) {
//		if (list == null || list.isEmpty()) {
//			return false;
//		}
//
//		int listSize = list.size();
//		for (int i = 0; i < listSize; i++) {
//			if (compareDevice(b, list.get(i))) {
//				return true;
//			}
//		}
//		return false;
//	}

}
