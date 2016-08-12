package com.billionav.navi.naviscreen.base;

import java.util.LinkedList;

public class OfflineOperateIDManager {
	private static OfflineOperateIDManager instance = null;
	private LinkedList<Integer> operateIdList =  new LinkedList<Integer>();
	private int MAX_OPERATE_NUM = 10;
	public static OfflineOperateIDManager getInstance(){
		if(instance == null){
			instance = new OfflineOperateIDManager();
		}
		return instance;
	}
	public void setOperateTag(int operateId){
		 if(operateIdList.size() > MAX_OPERATE_NUM){
			 operateIdList.removeFirst();
		 }
		 operateIdList.add(operateId);
	}
	public int createOperateId(){
		int operateId =  (int) (Math.random()*10000);
		setOperateTag(operateId);
		return operateId;
		
	}
	public boolean isCurrentOperate(int operateId){
		if(operateId == operateIdList.getLast()){
			return true;
		}else{
			return false;
		}
	}
}
