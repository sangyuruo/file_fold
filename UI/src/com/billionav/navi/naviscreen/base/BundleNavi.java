package com.billionav.navi.naviscreen.base;


import java.io.Serializable;

import android.app.Activity;
import android.os.Bundle;

public class BundleNavi {
	
	private BundleUnit bundle;
	private BundleUnit preBundle;
	
	private static final BundleNavi instance = new BundleNavi();
	
	private BundleNavi() {
		bundle = BundleUnit.nullBundleUnit;
		preBundle = BundleUnit.nullBundleUnit;
	}
	
	public static BundleNavi getInstance(){
		return instance;
	}
	
	/*package*/ void updateBundle(Class<? extends Activity> cls) {
		if(bundle.activityClass.equals(cls)){
			return;
		}
		preBundle = bundle;
		bundle = BundleUnit.create(cls);
	}
	
	/*package*/ void forceUpdateBundle(Class<? extends Activity> cls) {
		preBundle = bundle;
		bundle = BundleUnit.create(cls);
	}
	
	public String getString(String key){
		String temp = preBundle.bundle.getString(key);
		preBundle.bundle.remove(key);
		return temp;
	}
	
	public int getInt(String key){
		int temp = preBundle.bundle.getInt(key);
		preBundle.bundle.remove(key);
		return temp;
	}
	
	public int getInt(String key, int defaultValue){
		int temp = preBundle.bundle.getInt(key, defaultValue);
		preBundle.bundle.remove(key);
		return temp;
	}
	
	public float getFloat(String key){
		float temp = preBundle.bundle.getFloat(key);
		preBundle.bundle.remove(key);
		return temp;
	}
	
	public boolean getBoolean(String key){
		boolean temp = preBundle.bundle.getBoolean(key);
		preBundle.bundle.remove(key);
		return temp;
	}
	
	public Object get(String key){
		Object temp = preBundle.bundle.get(key);
		preBundle.bundle.remove(key);
		return temp;
	}
	
	public void putString(String key, String value){
		bundle.bundle.putString(key, value);
	}
	
	public void putInt(String key, int value){
		bundle.bundle.putInt(key, value);
	}
	
	public void putFloat(String key, float value) {
		bundle.bundle.putFloat(key, value);
	}
	
	public void putBoolean(String key, boolean value){
		bundle.bundle.putBoolean(key, value);
	}
	
	public void put(String key, Serializable value){
		bundle.bundle.putSerializable(key, value);
	}
	
	public Class<? extends Activity> getPreviousActivityClass(){
		return preBundle.activityClass;
	}
	
	public Class<? extends Activity> getCurrentActivityClass(){
		return bundle.activityClass;
	}
	
	private static class BundleUnit{
		private static final BundleUnit nullBundleUnit = new BundleUnit(Activity.class);
		
		private final Bundle bundle;
		private final Class<? extends Activity> activityClass;
		private BundleUnit(Class<? extends Activity> activityClass){
			this.activityClass = activityClass;
			bundle = new Bundle();
		}
		
		private static BundleUnit create(Class<? extends Activity> activityClass){
			return new BundleUnit(activityClass);
		}
	}

}
