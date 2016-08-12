package com.billionav.voicerecogJP.UI;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Base64;

import com.billionav.navi.menucontrol.MenuControlIF;
import com.billionav.navi.menucontrol.NSViewManager;
import com.billionav.navi.uitools.SystemTools;

public class ContactControl{
	
    private static ContactControl instance = null;
    
    public static ContactControl getContactControlInstance(){
    	if (instance == null) {
    		instance = new ContactControl();
		}
    	return instance;
    }
    
    private static final String[] PHONES_PROJECTION = new String[] {
    	Phone.DISPLAY_NAME, 
    	Phone.NUMBER, 
    	};
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    
    private static final int PHONES_NUMBER_INDEX = 1;
    
    private ArrayList<ContactInfo> numberList = new ArrayList<ContactInfo>();


    
	private String[] splitNameByWord(String name) {
		int length = name.length();
		String [] subname = new String[length];
		for (int i = 0; i < name.length(); i++) {
			subname[i] = name.charAt(i)+"";
		}
		return subname;
	}
    
    
    
    
    public ArrayList<ContactInfo> getPhoneContactsByName(String name){
		
		numberList.clear();
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,  
				                ContactsContract.CommonDataKinds.Phone.NUMBER};  
        Cursor cursor =NSViewManager.GetViewManager().getCurrentActivity().getBaseContext().getContentResolver().query(  
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                projection,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = '" + name + "'",  
                null,        
                null); 
  
        if( cursor == null ) {  
            return null;  
        }  
        while(cursor.moveToNext()){  
            
            String number2 = cursor.getString(PHONES_NUMBER_INDEX);
			String number3 = number2.trim();
			String number4 = dealWithNumber(number3);
            String nameResult = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
            ContactInfo one = new ContactInfo();
            one.setName(nameResult);
            one.setNumber(number4);
            numberList.add(one);
        }
        cursor.close();
        
        if (numberList.size() == 0) {
        	
        	String newName = getResultName(name);
        	String [] subName = splitNameByWord(newName);
        	numberList = getPhoneContacts(subName);
		}else {
		}
        return numberList;
	}
    
    
    public ArrayList<ContactInfo> getPhoneContactsByCompare(ContactInfo[] contacts){
		
		numberList.clear();
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,  
				                ContactsContract.CommonDataKinds.Phone.NUMBER};  
        Cursor cursor =NSViewManager.GetViewManager().getCurrentActivity().getBaseContext().getContentResolver().query(  
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                projection,
                null,  
                null,        
                null); 
  
        if( cursor == null ) {  
            return null;  
        }  
        while(cursor.moveToNext()){  
            
            String number2 = cursor.getString(PHONES_NUMBER_INDEX);
			String number3 = number2.trim();
			String number4 = dealWithNumber(number3);
            String nameResult = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
            for (int i = 0; i < contacts.length; i++) {
            	if (contacts[i].getName().equals(nameResult) && contacts[i].getNumber().equals(number4)) {
            		
            		ContactInfo one = new ContactInfo();
            		one.setName(nameResult);
            		one.setNumber(number4);
            		numberList.add(one);
            	}
			}
        }
        cursor.close();
        
        return numberList;
	}

    private String getResultName(String name){
    	String newName = "";
    	if (SystemTools.isCH()) {
			if (name.contains("����")) {
				String [] subName = name.split("����");
				for (int i = 0; i < subName.length; i++) {
					newName = newName + subName[i];
				}
			}else {
				newName = name;
			}
		}else if(SystemTools.isJP()){
			if (name.contains("����")) {
				String [] subName = name.split("����");
				for (int i = 0; i < subName.length; i++) {
					newName = newName + subName[i];
				}
			}else {
				newName = name;
			}
		}
    	
    	return newName;
    } 
    
    public ArrayList<ContactInfo> getPhoneContacts(String[] subName) {
    	
    	ArrayList<ContactInfo> finalName = new ArrayList<ContactInfo>();
    	int length = subName.length;
    	String selection = "display_name like ?" ;
    	for (int i = 0; i < length - 1; i++) {
			selection = selection +"or display_name like ?";
		}
    	
    	String [] selectionArgs = new String[length];
    	
    	for (int i = 0; i < length; i++) {
			selectionArgs[i] = "%" + subName[i] + "%";
		}
    	
		ContentResolver resolver = NSViewManager.GetViewManager().getCurrentActivity().getBaseContext().getContentResolver();
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
											PHONES_PROJECTION,
											selection,
											selectionArgs,
											null);
	
		if (phoneCursor != null) {
			
			int count1 = 0;
			int count2 = 0;
			
		    while (phoneCursor.moveToNext()) {
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				String finalNumber = dealWithNumber(phoneNumber);
				if (TextUtils.isEmpty(finalNumber)){
					continue;
				}
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				//�Ƚϲ��ҳ��Ľ���϶���ߵ�String
				count1 = 0;
				
				for(int i = 0; i < subName.length; i++){
					for(int j = 0; j < contactName.length(); j++){
						if (subName[i].equals(contactName.charAt(j)+"")) {
							count1++;
							break;
						}
					}
				}
				if (count1 > count2) {
					count2 = count1;
					finalName.clear();
					ContactInfo data = new ContactInfo();
					data.setName(contactName);
					data.setNumber(finalNumber);
					finalName.add(data);
				}else if(count1 == count2){
					ContactInfo data = new ContactInfo();
					data.setName(contactName);
					data.setNumber(finalNumber);
					finalName.add(data);
				}else {
				}
		    }
		    
		    for (int i = 0; i < finalName.size(); i++) {
			}
		    phoneCursor.close();
		}
		return finalName;
    }
    
    
    
	public ContactInfo getPhoneContactsByNumber(String number){
		
		ArrayList<ContactInfo> numberList = new ArrayList<ContactInfo>();
		ContactInfo contact = new ContactInfo();
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,  
	            ContactsContract.CommonDataKinds.Phone.NUMBER,
	            Photo.PHOTO_ID,
	            Phone.CONTACT_ID};  
	
		
		Cursor cursor = NSViewManager.GetViewManager().getCurrentActivity().getBaseContext().getContentResolver().query(  
		ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
		projection,
//		ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + number + "'",
		null,
		null,           
		null);
		
		if( cursor == null ) {  
		return null;  
		}  
		
		while(cursor.moveToNext()){  
		
			
			String name = cursor.getString(0);
			String number2 = cursor.getString(1);
			String number3 = number2.trim();
			String number4 = dealWithNumber(number3);
			Long photoid = cursor.getLong(2);
			Long contactid = cursor.getLong(3);
			Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
			ContactInfo data = new ContactInfo();
			data.setName(name);
			data.setNumber(number4);
			data.setUri(uri);
			data.setPhotoID(photoid);
			data.setContactid(contactid);
			numberList.add(data);
			
		}
		
		for (int i = 0; i < numberList.size(); i++) {
			if (number.contains(numberList.get(i).getNumber())) {
				Long photoID = numberList.get(i).getPhotoID();
				Long contactid = numberList.get(i).getContactid();
				contact.setName(numberList.get(i).getName());
				if(photoID > 0 ) {
					Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
					InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(NSViewManager.GetViewManager().getCurrentActivity().getBaseContext().getContentResolver(), uri);
					Bitmap contactPhoto = BitmapFactory.decodeStream(input);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					contactPhoto.compress(CompressFormat.PNG, 100, bos);
					byte[] datas = bos.toByteArray();
					String photostr = Base64.encodeToString(datas, Base64.DEFAULT);
					contact.setPhoto(photostr);
			    } 
			}
		}
		
		cursor.close();
		
		
		return contact;
	}
	
//	public ArrayList<ContactInfo> getContactInfoByNumber(String number){
//		String selection = Phone.NUMBER+" like ?" ;
//		String[] selectionArgs = new String[1];
//		selectionArgs[0] = "%" + number + "%";
//		ArrayList<ContactInfo> contactName = new ArrayList<ContactInfo>();
////		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,  
////	            ContactsContract.CommonDataKinds.Phone.NUMBER};  
//	
//		Cursor cursor = NSViewManager.GetViewManager().getCurrentActivity().getBaseContext().getContentResolver().query(  
////		ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
//		Phone.CONTENT_URI,
//		PHONES_PROJECTION,
////		ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + number + "'",
//		
//		selection,
//		selectionArgs ,
//		null);
//		
//		if( cursor == null ) {  
//		return null;  
//		}  
//		
//		while(cursor.moveToNext()){  
//		
//			String name = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
//			String number2 = cursor.getString(PHONES_NUMBER_INDEX);
//			ContactInfo data = new ContactInfo();
//			data.setName(name);
//			data.setNumber(number2);
//			contactName.add(data);
//		}
//		
//		cursor.close();
//		
//		
//		return contactName;
//	}
	
	
	public ArrayList<ContactInfo> getContactInfoByNumber(String number){
		
		ArrayList<ContactInfo> contactName = new ArrayList<ContactInfo>();
		ArrayList<ContactInfo> finalName = new ArrayList<ContactInfo>();
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,  
	            ContactsContract.CommonDataKinds.Phone.NUMBER}; 
	
		Cursor cursor = NSViewManager.GetViewManager().getCurrentActivity().getBaseContext().getContentResolver().query(  
		ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
		projection,
		null,
		null,           
		null);
		
		if( cursor == null ) {  
			return null;  
		}  
		
		while(cursor.moveToNext()){  
		
			String name = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
			String number2 = cursor.getString(PHONES_NUMBER_INDEX);
			String number3 = number2.trim();
			String number4 = dealWithNumber(number3);
			ContactInfo data = new ContactInfo();
			data.setName(name);
			data.setNumber(number4);
			contactName.add(data);
		}
		
		for (int i = 0; i < contactName.size(); i++) {
			if (contactName.get(i).getNumber().contains(number)) {
				
				finalName.add(contactName.get(i));
			}
		}
		
		cursor.close();
		
		
		return finalName;
	}
	
	private String dealWithNumber(String number3) {
		String result = "";
		char res[] = number3.toCharArray();
		for (int i = 0; i < res.length; i++) {
			if((res[i]>=48) && (res[i]<=57)) {
				result = result + res[i] ;
			}
		}
		return result;
	}




	public void getPhoneContactByNumberAsynchrony(final String number){
		
//		ThreadEngine.Instance().post(new Runnable() {
//			
//			public void run() {
////				String name = getPhoneContactsByNumber(number);
//				
//				//send trigger
//			}
//		});
	}
	public void getPhoneContactsByNameAsynchrony(final String name){
		
//		ThreadEngine.Instance().post(new Runnable() {
//			
//			public void run() {
////				String nameList = getPhoneContactsByNumber(name);
//				
//				//send trigger
//			}
//		});
		
	}
	
	public boolean callOnePerson(String number){
			
		if (number ==null || number.equals("")) {
			return false;
		}else {
			
			Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
			dialIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			MenuControlIF.Instance().GetCurrentActivity().startActivity(dialIntent);
			return true;
		}
		
	}
	
}
