package com.billionav.navi.uitools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore.Audio;

import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.ui.R;

public class MessageTools {
	private static MessageTools instance;
	private NotificationManager mNotificationManager;
	private Notification notification;
	private Context context;
	private String contentTitle ;
	private final String TICKET_TITLE = "88Map";
	private final int icon = R.drawable.navicloud_and_002b;
	private PendingIntent onClickIntent ;
	
	private MessageTools(){
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) NSViewManager.GetViewManager().getSystemService(ns);
		long when = System.currentTimeMillis();
		notification = new Notification(icon,TICKET_TITLE, when);
		notification.flags|=Notification.FLAG_AUTO_CANCEL; 
	}
	
	private MessageTools(Context c){
		context = c;
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) c.getSystemService(ns);
		long when = System.currentTimeMillis();
		notification = new Notification(icon,TICKET_TITLE, when);
		notification.flags|=Notification.FLAG_AUTO_CANCEL; 
		
		notification.defaults |= Notification.DEFAULT_LIGHTS; 
		notification.ledARGB = 0xff00ff00; 
		notification.ledOnMS = 300; 
		notification.ledOffMS = 1000; 
		notification.flags |= Notification.FLAG_SHOW_LIGHTS; 
		
		notification.defaults |= Notification.DEFAULT_SOUND;
//		notification.sound = Uri.parse("android.resource://com.sun.alex/raw/dida"); 
//		notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6"); 
		
		notification.defaults |= Notification.DEFAULT_VIBRATE; 
		long[] vibrate = {0,100,200,300}; 
		notification.vibrate = vibrate ;
	}
	
	
	public static MessageTools getInstance(){
		if(null == instance){
			instance = new MessageTools();
		}
		return instance;
	}
	public static MessageTools Create(Context c) {
		if(null == instance){
			instance = new MessageTools(c);
		}
		return instance;
	}
	public void sendMessageToSystemBar(int id, String contentText){
		notification.setLatestEventInfo(context, contentTitle, contentText, onClickIntent);
		mNotificationManager.notify(id, notification);
	}
	
//	public Intent getOnApkUpdateFinishedIntent(){
//		Intent notificationIntent = new Intent(NSViewManager.GetViewManager(), NSViewManager.GetViewManager().getClass());
//		notificationIntent = new Intent(Intent.ACTION_VIEW);  
//		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//		notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
//		
//		final VersionControl_VersionDataFormat versionNode = VersionControl_ManagerIF.Instance().getM_cLatestVersionInfo();
//		
//		Uri uri = Uri.parse(versionNode.getStrAPKDownLoadURL()); 
//		
//		Log.d("test","versionNode.getStrAPKDownLoadURL() = "+versionNode.getStrAPKDownLoadURL());
//		final String filePath = FileSystemJNI.instance().getSystemPath(FileSystemJNI.FILE_NDATA_PATH)+"/AplUpdate/"+uri.getLastPathSegment();
//		notificationIntent.setDataAndType(Uri.parse("file://" +  filePath), "application/vnd.android.package-archive");  
//		
//		return notificationIntent;
//	}
	
//	public PendingIntent getOnApkUpdateFinishedPendingIntent(){
//		return PendingIntent.getActivity(NSViewManager.GetViewManager()
//				, 0, getOnApkUpdateFinishedIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
//	}
	
	public PendingIntent getOnApkUpdatingPendingIntent(){
		return PendingIntent.getActivity(context, 0, new Intent(), 0);
	}
	
	public void setPendingIntent(PendingIntent intent){
		this.onClickIntent = intent;
	}
	public void cancelMessage(int id){
		mNotificationManager.cancel(id);
	}
}
