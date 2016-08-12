package com.billionav.navi.uitools;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.XmlResourceParser;
import android.view.KeyEvent;

import com.billionav.navi.component.dialog.CustomDialog;
import com.billionav.ui.R;

public class DialogTools {
	private DialogTools(){};
	
	/**
	 * create a dialog for exit application.
	 * 
	 * */
	public static CustomDialog createExitDialog(Context context){
		CustomDialog dialog = new CustomDialog(context);
		dialog.setTitle(R.string.MSG_01_02_01_01_04);
		dialog.setMessage(R.string.MSG_01_02_01_01_03);
		dialog.setNegativeButton(R.string.STR_COM_001, null);
		dialog.setPositiveButton(R.string.STR_COM_003, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SystemTools.exitSystem();
				
			}});
		return dialog;
	}
	
	/**
	 * get a instance of DialogInterface.OnClickListener, 
	 * that will play a beep voice.
	 * */
	public static OnClickListener getDefaultListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		};
	}
	
	/**
	 * create a dialog with single button.
	 * 
	 * */
	public static CustomDialog getDialogForSingleButton(Context context, String titleId, String msgId, int closeButtonId, OnClickListener onClickListener){
		CustomDialog dialog = new CustomDialog(context);
		dialog.setTitle(titleId);
		dialog.setMessage(msgId);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
	        		dialog.cancel();
	        		return true;
				}
				return false;
			}
		});
		
		if(onClickListener==null){
			onClickListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE2);
				}
			};
		}
		
		if(closeButtonId==0){
			dialog.setNegativeButton(R.string.STR_COM_003, onClickListener); //ok
		}else if(closeButtonId==-1){
			dialog.setNegativeButton(R.string.STR_COM_001, onClickListener); //cancel
		} else {
			dialog.setNegativeButton(closeButtonId, onClickListener); //closeButtonId
		}
		
		return dialog;
	}
	
	public static CustomDialog getDialogForSingleButton(Context context, int titleId, int msgId, int closeButtonId, OnClickListener onClickListener){
		String titleStr = "";
		String msgStr = "";
		if(titleId>0) {
			titleStr = context.getString(titleId);
		}
		if(msgId>0){
			msgStr = context.getString(msgId);
		}
		return getDialogForSingleButton(context, titleStr, msgStr, closeButtonId, onClickListener);
	}
	
	public static CustomDialog getDialogForSingleButton(Context context, String titleId, String msgId, int closeButtonId){
		return getDialogForSingleButton(context, titleId, msgId, closeButtonId, null);
	}
	
	public static CustomDialog getDialogForSingleButton(Context context, int titleId, int msgId, int closeButtonId){
		return getDialogForSingleButton(context, titleId, msgId, closeButtonId, null);
	}

	public static CustomDialog getDialogForSingleButton(Context context, String titleId, String msgId){
		return getDialogForSingleButton(context, titleId, msgId, -1, null);
	}
	
	public static CustomDialog getDialogForSingleButton(Context context, int titleId, int msgId){
		return getDialogForSingleButton(context, titleId, msgId, -1, null);
	}

	/**
	 * get error dialog
	 * 
	 * */
//	public static CustomDialog getErrorDialog(Context context, long errorCode) {
//		
//		String errCode = translateErrorCode(errorCode);
//		int stringId = getStringIdFromCode(context, errCode);
//		return getErrorDialog(context, stringId, errCode);
//	}
	
//	private static String translateErrorCode(long errorCode){
//		String errCode = Long.toHexString(errorCode);
//		int length = errCode.length();
//		if(length>8){
//			errCode = errCode.substring(length-8);
//		}else if(length<8){
//			for(int i=0; i<8-length; i++){
//				errCode = "0"+errCode;
//			}
//		}
//		
//		return errCode;
//	}
//	
//	private static int getStringIdFromCode(Context context, String errorCode) {
//		errorCode = errorCode.toUpperCase();
//		int stringId = -1;
//		try {
//			XmlResourceParser xpp = context.getResources().getXml(R.xml.errorcode);
//			
//			for (int eventType = xpp.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = xpp.next()) {
//				if ((eventType != XmlPullParser.START_TAG) || (xpp.getDepth() != 2)) {
//					continue;
//				}
//				
//				String code = xpp.getAttributeValue(null, "code").toUpperCase();
//				
//				if(!code.endsWith("XXX")){
//					if(code.equalsIgnoreCase(errorCode)){
//						return xpp.getAttributeResourceValue(null, "string-id", 0);
//					} else {
//						continue;
//					}
//				}else{
//					if(code.endsWith("XXXX") && errorCode.startsWith(code.substring(0, 4))){
//						String exceptions = xpp.getAttributeValue(null, "exception");
//						boolean isExceptionCode = (exceptions != null && exceptions.contains(errorCode.substring(4)));
//						boolean isStringIdNull = (stringId == -1);
//						if(isStringIdNull && !isExceptionCode){
//							stringId = xpp.getAttributeResourceValue(null, "string-id", 0);
//						}
//					} else if(code.endsWith("XXX") && errorCode.startsWith(code.substring(0, 5))){
//						stringId = xpp.getAttributeResourceValue(null, "string-id", 0);
//					} 
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (XmlPullParserException e) {
//			e.printStackTrace();
//		}
//		
//		return stringId;
//	}
//	
//	private static CustomDialog getErrorDialog(Context context, int errorText, String errorCode){
//		
//		errorCode = "("+errorCode.substring(0, 2)+"-"+errorCode.substring(2, 4)+"-"+errorCode.substring(4)+")";
//        
//        String msg;
//        if(errorText>0){
//        	msg = context.getString(errorText) + "\n" + errorCode;
//        } else {
//        	msg = errorCode;
//        }
//		String title = context.getString(R.string.STR_MM_01_01_04_29);
//		return getDialogForSingleButton(context, title, msg, 0);
//	}

}
