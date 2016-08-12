package com.billionav.navi.menucontrol;

import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.billionav.DRIR.DRIRUICommon;
import com.billionav.navi.component.mapcomponent.MapOverwriteLayer;
import com.billionav.navi.datasynccontrol.DataSyncControl_TriggerListener;
import com.billionav.navi.naviscreen.mef.MapView;
import com.billionav.navi.uicommon.UIC_IntentCommon;
import com.billionav.navi.versioncontrol.VersionControl_TriggerListener;
import com.billionav.ui.R;
import com.billionav.voicerecogJP.VRJPManager;

public class MenuControlIF implements IMenuControl{
	private static final String TAG = "MenuControlIF"; 
	private static final String SUBTAG = "MenuControlIF: "; 
	private static int s_iInstanceCount = 0;
	@SuppressWarnings({ "rawtypes" })
	private static HashMap<String, Class> replaceActivityList = new HashMap<String, Class>();
	
	
	public static void Create(Context c){
		if(null==instance){
			instance = new MenuControlIF();
			instance.RegisterBroadcastReceiver(c);
			
			Intent setActivityReplaceIntent = new Intent();
			setActivityReplaceIntent.setAction("setReplaceActivitys");
//			c.sendBroadcast(setActivityReplaceIntent);
		}
		s_iInstanceCount++;
		if(null!=instance){
			instance.init();
			instance.UnlockAll();
		}
	}
			
	public static void Destroy(Context c){
		s_iInstanceCount--;
		if(instance!=null && s_iInstanceCount<=0){
			instance.init();
			instance.UnregisterBroadcastReceiver(c);			
			instance = null;
		}
	}	
	public  static MenuControlIF Instance(){ return instance; }
	
	private void init()
	{
		Log.d(TAG,SUBTAG+"**initialize["+s_iInstanceCount+"]**");
		m_cTriggerStack.clear();			
		m_cScreenInfoStack.PopAll();	
	}
	
	private static int s_iWinInstanceCount = 0;
	final public static int GetWinInstanceCount(){return s_iWinInstanceCount;}
	final public static void IncreaseWinInstance(){s_iWinInstanceCount++;}
	final public static void DecreaseWinInstance(){s_iWinInstanceCount--;}
	
	public  IViewManager GetViewManager() {return NSViewManager.GetViewManager();};
	//--------------------------------------------------------------//
	
	@Override
	protected void finalize() throws Throwable {
		m_cTriggerStack.clear();			
		m_cScreenInfoStack.PopAll();	
		super.finalize();
	}
	
	private static MenuControlIF instance = null;
		
	private class MenuControl_Message{
		private static final int						MN_MSG_TriggerInfo 				= 1;
		private static final int						MN_MSG_MapDrawDoneTimeout  		= 2;
		private static final int						MN_MSG_TestTrigger  			= 3;
		private static final int						MN_MSG_ContinueWinchange  		= 4;
		private static final int						MN_MSG_DelayWinchange  			= 5;
		private static final int						MN_MSG_UnlockInput				= 8;
		private static final int						MN_MSG_WinchangeEnd			= 9;
	}

	@SuppressWarnings("rawtypes")
	public boolean addChileReplaceParentPair(Class newcls, Class oldcls) {

		if(newcls == null || oldcls == null) {
			return false;
		}
//		if(!newcls.isInstance(Activity.class) || !oldcls.isInstance(Activity.class)) {
//			return false;
//		}
		replaceActivityList.put(oldcls +"", newcls);
		return true;

	}
	

	@SuppressWarnings("rawtypes")
	private Class getChildClass(Class parent) {
		if(null == parent) {
			return parent;
		}
		if(replaceActivityList.containsKey(parent+"")) {
			return replaceActivityList.get(parent+"");
		}
		return parent;
	}
	
	private class ScreenInfoStack {
		private static final String SUBTAG = "ScreenInfoStack: ";  
		
		class ScreenInfoObj extends Object{	
			private UUID        id;
			@SuppressWarnings("rawtypes")
			private Class		m_cWinCls 	 = null;
			private int 		m_iWinChangeType = NSWinchangeType.MN_WINCHANGE_TYPE_UNKNOWN;			
			
			public ScreenInfoObj()
			{
				super();
				id =UUID.randomUUID();
			}
			
			@SuppressWarnings("rawtypes")
			public void Set(Class wincls,int winchangtype,Object extraInfo)
			{
				if(null!=wincls){
					m_cWinCls = wincls;
				}
				m_iWinChangeType = winchangtype;
			}
		
			
			public void Clr(){
				m_cWinCls 	 	= null;
				m_iWinChangeType= NSWinchangeType.MN_WINCHANGE_TYPE_UNKNOWN;
			}
			
			public UUID GetId(){
				return id;
			}
			
			@SuppressWarnings("rawtypes")
			public Class GetWinClass(){				
				return m_cWinCls;				
			}
			
			public int GetWinChangeType(){ 
				return m_iWinChangeType; 
			}			
		}
		
		private Stack<ScreenInfoObj> m_cScreenInfoStack = new Stack<ScreenInfoObj>();
		private Stack<ScreenInfoObj> m_cPopedScreenInfoStack = new Stack<ScreenInfoObj>();
		
		public boolean Empty(){
			if(null == m_cScreenInfoStack){
				return true;
			}else{
				return m_cScreenInfoStack.empty();
			}
		}
		
		public int GetDepth()
		{
			if( null == m_cScreenInfoStack ){
				return 0;
			}		
			return m_cScreenInfoStack.size();
		}
		
		//last screen
		public ScreenInfoObj GetTopInfo()
		{
			//Get the item at the top of the stack 
			if( null == m_cScreenInfoStack || m_cScreenInfoStack.empty()){
				return null;
			}
			return m_cScreenInfoStack.lastElement();
		}
	
		public ScreenInfoObj GetPenultInfo()
		{
			if( null == m_cScreenInfoStack ){
				return null;
			}	
			
			int size = m_cScreenInfoStack.size();
			if(size<=1){
				return null;
			}
			return m_cScreenInfoStack.get(size-2);			 
		}
		
		//default screen info
		public ScreenInfoObj GetBottomInfo()
		{
			//Get the item at the bottom of the stack 
			if( null == m_cScreenInfoStack || m_cScreenInfoStack.empty()){
				return null;
			}		
			return m_cScreenInfoStack.firstElement();
		}
	
		final public Stack<ScreenInfoObj> GetPopedScreenInfoObjects()
		{
			return m_cPopedScreenInfoStack;
		}
		
		final public Stack<ScreenInfoObj> GetScreenInfoObjects()
		{
			return m_cScreenInfoStack;
		}
		
		//
		@SuppressWarnings("rawtypes")
		public void Push(Class wincls,int winchangtype,Object extraInfo)
		{
			ScreenInfoObj cObj = new ScreenInfoObj();
			cObj.Set(wincls, winchangtype,extraInfo);
			Push(cObj);
		}
		
		//
		public void Push(ScreenInfoObj obj)
		{
			if( null == m_cScreenInfoStack){
				return;
			}		
			m_cScreenInfoStack.push(obj);
		}
		
		//
		public ScreenInfoObj Pop()
		{
			if( null == m_cScreenInfoStack || m_cScreenInfoStack.empty()){
				return null;
			}
			
			ScreenInfoObj obj= m_cScreenInfoStack.pop();
			
			if(null!=m_cPopedScreenInfoStack && null!=obj){
				m_cPopedScreenInfoStack.push(obj);
			}
			return obj;
		}
		
		public void PopAll(){
			if( null == m_cScreenInfoStack){
				return;
			}
			while(!m_cScreenInfoStack.empty()){
				ScreenInfoObj obj= m_cScreenInfoStack.pop();
				if(null!=m_cPopedScreenInfoStack){
					m_cPopedScreenInfoStack.push(obj);
				}else{					
					obj.Clr();
				}	
			}		
		}
			
		
		@SuppressWarnings("rawtypes")
		public boolean PopUntilByWinCls(Class wincls)
		{
			//not include winID
			if(false == SearchByWinCls(wincls))
			{
				return false;
			}
					
			while(!wincls.equals(GetTopInfo().GetWinClass()))
			{
				ScreenInfoObj obj= m_cScreenInfoStack.pop();
				if(null!=m_cPopedScreenInfoStack){
					m_cPopedScreenInfoStack.push(obj);
				}else{					
					obj.Clr();
				}	
			}
			return true;
		}
		
		public boolean PopUntilByDepth(int iDepth)
		{
			if( null == m_cScreenInfoStack
				||m_cScreenInfoStack.size()<iDepth){
				return false;
			}
			
			while (m_cScreenInfoStack.size()>iDepth)
			{
				ScreenInfoObj obj= m_cScreenInfoStack.pop();
				if(null!=m_cPopedScreenInfoStack){
					m_cPopedScreenInfoStack.push(obj);
				}else{					
					obj.Clr();
				}	
			}
			return true;
		}
		
		//
		@SuppressWarnings("rawtypes")
		public boolean SearchByWinCls(Class wincls)
		{			
			if( null == m_cScreenInfoStack || m_cScreenInfoStack.empty()){
				return false;
			}
			
			 for (ScreenInfoObj i : m_cScreenInfoStack){
				   if(wincls.equals(i.GetWinClass())){
					   return true;
				   }
			 }
			 return false;	
		}
		
		@SuppressWarnings("rawtypes")
		public boolean SearchByWinClsExceptTop(Class wincls)
		{
			if( null == m_cScreenInfoStack || m_cScreenInfoStack.empty()){
				return false;
			}
					
			int size = m_cScreenInfoStack.size();
			for(int i=size-2;i>=0;--i){
				ScreenInfoObj obj = m_cScreenInfoStack.get(i);	
				 if(obj!=null && wincls.equals(obj.GetWinClass())){
					 return true;
				 }				
			}			
			 return false;	
		}
		
		//
		public void LogoutStackInfo()
		{			
			Log.d(TAG,SUBTAG+"-----------Screen Info Stack Begin-------------");
			if( null == m_cScreenInfoStack || m_cScreenInfoStack.isEmpty()){    		
				Log.d(TAG,SUBTAG+"-----------Empty-----------------");
				Log.d(TAG,SUBTAG+"-----------Screen Info Stack End-----------------");
	    		return;
	    	}			
			Log.d(TAG,SUBTAG+"--------Stack Size:"+m_cScreenInfoStack.size()+"------------");
			 for (ScreenInfoObj i : m_cScreenInfoStack)
			 {
				 Log.d(TAG,SUBTAG+"-----------[" + i.GetWinClass()+"][Type:"+i.GetWinChangeType()+"]");
			 }
			
			Log.d(TAG,SUBTAG+"-----------Screen Info Stack End-----------------");
			
		}
	};		
	private ScreenInfoStack m_cScreenInfoStack =  new ScreenInfoStack();		
	
	
	
	private class MenuControlHandler extends Handler {
		public MenuControlHandler() 
		{
			super(Looper.getMainLooper());
		}	
		
		public void handleMessage(Message msg) 
		{
			if(CheckQuit()){
				return;
			}
			
			if(NSViewManager.CURSTATUS>=NSViewManager.MAIN_STATUS.DESTROY){
				return;
			}
			
			switch(msg.what)
			{
			case MenuControl_Message.MN_MSG_ContinueWinchange:
				OnMsg_ContinueWinchange();
				break;			
			case MenuControl_Message.MN_MSG_TriggerInfo:
				OnMsg_TriggerForScreen((NSTriggerInfo)(msg.obj));
				break;	
//			case MenuControl_Message.MN_MSG_Prohibition:
//				OnMsg_Prohibition(msg.arg1==0?false:true);
//				break;
			case MenuControl_Message.MN_MSG_TestTrigger:
				OnMsg_TestTrigger((NSTriggerInfo)msg.obj);
				break;	
			case MenuControl_Message.MN_MSG_DelayWinchange:
				DoWinchange();
				break;	
			case MenuControl_Message.MN_MSG_UnlockInput:
				LockInput(false);
				break;
			case MenuControl_Message.MN_MSG_WinchangeEnd:
				OnMsg_WinchangeEnd();
				break;
			default:
				break;
		    }
		}		
	};	

	private  MenuControlHandler	m_cMenuControlHandler   = new MenuControlHandler();	
		
	//save undealed trigger
	private Stack<NSTriggerInfo>m_cTriggerStack 		= new Stack<NSTriggerInfo>(); 
	
	private boolean				m_bJustPush				= false;
	private boolean				m_bSetMapInVisiable		= false;
	
	@SuppressWarnings("rawtypes")
	private Class 				m_cCurWinscapeCls 		= null;
	@SuppressWarnings("rawtypes")
	private Class 				m_cNextWinscapeCls 		= null;
	@SuppressWarnings("rawtypes")
	private Class 				m_cLastWinscapeCls 		= null;
	private int					m_iAnimaID				= NSAnimationID.ANIMATION_ID_DEFAULT;				

	@SuppressWarnings("rawtypes")
	private void OnMsg_ContinueWinchange()
	{		
		LockWinChange(true);
		LockInput(true);
		
		Class nextWinCls = null;	
		UUID id = null;
		ScreenInfoStack.ScreenInfoObj obj= m_cScreenInfoStack.GetTopInfo();
		if(null != obj){
			nextWinCls = obj.GetWinClass();
			id = obj.GetId();
		}	
		
		Object curobj = GetViewManager().GetCurMenuBase();
		IMenuBase curMenuBase = null;
		if(curobj instanceof IMenuBase){
			curMenuBase = (IMenuBase)curobj;
		}
		
		if(null!=curMenuBase){
			//CurrentScreen:OnExit
			curMenuBase.Exit();
		}				
		
		if(null != nextWinCls)
		{
			m_cNextWinscapeCls = nextWinCls;
			
			if(!SetActiveWinscape(m_cNextWinscapeCls)){
				Destroy(NSViewManager.GetViewManager().getBaseContext());		
				UnlockAll();		    	
	            System.exit(0);  
	            return;
			}

			LockScreen(false);
			Log.d(TAG,SUBTAG+"-----Dst["+nextWinCls+"]");		
			while(null!= m_cScreenInfoStack.GetPopedScreenInfoObjects() && 
				  m_cScreenInfoStack.GetPopedScreenInfoObjects().size()>1){
				ScreenInfoStack.ScreenInfoObj cScreenInfoObj =m_cScreenInfoStack.GetPopedScreenInfoObjects().pop();
				GetViewManager().DestroyViewMenuBase(cScreenInfoObj.GetId().toString());
				cScreenInfoObj.Clr();
			}
			GetViewManager().CreateViewMenuBase(nextWinCls,id.toString());	

			/* set map view Visibility */
			if (IsMapScreen(nextWinCls))
			{
				if ((null == m_cCurWinscapeCls) || (!IsMapScreen(m_cCurWinscapeCls)))
				{
					MapView mapviewtemp = MapView.getInstance();
					if (null != mapviewtemp)
					{
						
						mapviewtemp.setVisibility(View.VISIBLE);
					}
				}
			}
			else
			{
//				if ((null != m_cCurWinscapeCls) && (IsMapScreen(m_cCurWinscapeCls)))
				{
					m_bSetMapInVisiable = true;
				}
			}

			m_cCurWinscapeCls  = m_cNextWinscapeCls;
			m_cNextWinscapeCls = null;
			
			//newScreen:OnSetMapInfo
			IMenuBase newMenuBase= GetViewManager().GetCurMenuBase();
			
			if(null!=newMenuBase){
				ResetSyncID();			
			}else{
				Log.e(TAG,SUBTAG+"-----Failed to create new view menu-----");
			}
		}else{
			Log.e(TAG,SUBTAG+"-----Bad Next Wincls-----");
			UnlockAll();
	    	return;
		}
			
		//Wait Sync
		if(false == m_bWaitForSync){
			DoWinchange();
		}else{
			//start wait map draw done
			m_cMenuControlHandler.sendMessageDelayed(m_cMenuControlHandler.obtainMessage(MenuControl_Message.MN_MSG_MapDrawDoneTimeout), 13000);	
		}
	}
	
	
	private boolean				m_bWaitForSync			= false;
	
	private void ResetSyncID()
	{
		m_bWaitForSync = false;
	}
	
	private static boolean isTtt = false;
	public synchronized boolean TriggerForScreen(NSTriggerInfo cTriggerInfo)
	{
		
		m_cMenuControlHandler.sendMessage(m_cMenuControlHandler.obtainMessage(MenuControl_Message.MN_MSG_TriggerInfo,cTriggerInfo));		

		if (cTriggerInfo.m_iTriggerID == 202) {
			if (isTtt) {
				Log.i("SDL","cTriggerInfo:" + cTriggerInfo.toString());
				m_cMenuControlHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						Log.i("SDL","m_cMenuControlHandler run");
						m_cMenuControlHandler.sendEmptyMessage(MenuControl_Message.MN_MSG_WinchangeEnd);
					}
				}, 500);
			}

			if (isTtt) {
				isTtt = false;
			}
			else {
				isTtt = true;
			}
		}
		return true;
	}	
	
	private final int[] m_arrayTriggers_DRIR = {
		NSTriggerID.UIC_MN_TRG_DRIR_TAKEPIC,
		NSTriggerID.UIC_MN_TRG_DRIR_STARTPREVIEW,
		NSTriggerID.UIC_MN_TRG_DRIR_STOPPREVIEW, 
	};
	
	private boolean CheckDRIRTrigger(int iTriggerID){
		for(int i=0;i<m_arrayTriggers_DRIR.length;++i)
		{
			if(iTriggerID == m_arrayTriggers_DRIR[i]){
				return true;
			}
		}
		return false;
	};
	
	private void OnMsg_TriggerForScreen(NSTriggerInfo cTriggerInfo)
	{

//		if(CheckDRIRTrigger(cTriggerInfo.m_iTriggerID)){
//			DRIRUICommon.OnTrigger(cTriggerInfo);
//		}
		
//		if(DataSyncControl_TriggerListener.OnTriggerForWinscape(cTriggerInfo)){
//			return;
//		}
		
		Object obj = GetViewManager().GetCurMenuBase();
		ITriggerListener cTriggerListener = null;
		if(obj instanceof ITriggerListener){
			cTriggerListener =(ITriggerListener)obj;
		}
//		if(null != MapOverwriteLayer.getInstance()) {
//			MapOverwriteLayer.getInstance().onTrigger(cTriggerInfo);
//		}
		if(null != cTriggerListener && !IsWinChangeLocked())
		{
			switch(cTriggerInfo.m_iTriggerID){
			case NSTriggerID.UIC_MN_TRG_MC_PROHIBITION_CHANGE:
				{
					m_bProhibited = (cTriggerInfo.m_lParam1==0)?false:true;
//					jniSetupControl setupCtl = new jniSetupControl();
//					if(jniSetupControl.STUPDM_COMMON_ON == 
//						setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DRIVE_MODE)){
						((IMenuBase)cTriggerListener).Prohibition(m_bProhibited);	
//					}					
				}
				break;	
			case NSTriggerID.UIC_MN_TRG_INTENT_CALL:
			{
				int type = (int) cTriggerInfo.GetlParam1();
				if(obj instanceof UIC_IntentCommon.IntentCall) {
					boolean receiveIntent = ((UIC_IntentCommon.IntentCall)obj).onIntentCall(type);
					UIC_IntentCommon.Instance().doAfterOnIntentCall(receiveIntent, type);
				} else {
					UIC_IntentCommon.Instance().doAfterOnIntentCall(true, type);
				}
				break;
			}

			default:	
//				jniPointControl_new.Instance().TriggerForWinscape(cTriggerInfo);
				cTriggerListener.TriggerForWinscape(cTriggerInfo);
//				SnsControl.Instance().TriggerForWinscape(cTriggerInfo);
				VRJPManager.Instance().onTrigger(cTriggerInfo);
				VersionControl_TriggerListener.OnTriggerForWinscape(cTriggerInfo);
				break;
			}
		}else{
			m_cTriggerStack.push(cTriggerInfo);		
		}
	}
	
	private void DoWinchange(){
		Log.d(TAG,SUBTAG+"-----DoWinchange------ m_iAnimaID = " + m_iAnimaID);
		final IMenuBase cMenuBase = GetViewManager().GetCurMenuBase();
		if(null != cMenuBase){
			cMenuBase.SetScreenInfo();
			cMenuBase.Initialize();			
			cMenuBase.Prohibition(IsProhibited());
			//to distinguish between forward or Back
			boolean bUponLast = (0!=(GetWinchangeType()&0x01000000));
			int iAnimaIn  = 0;
			int iAnimaOut = 0;
			switch(m_iAnimaID){
			case NSAnimationID.ANIMATION_ID_SLIDE_IN_RIGHT:	
				iAnimaIn  = R.anim.slide_in_right;
				iAnimaOut = R.anim.slide_out_left;				
				break;
			case NSAnimationID.ANIMATION_ID_SLIDE_IN_LEFT:
				iAnimaIn  = R.anim.slide_in_left;
				iAnimaOut = R.anim.slide_out_right;	
				break;
			case NSAnimationID.ANIMATION_ID_SLIDE_IN_BOTTOM:
				bUponLast = true;
				iAnimaIn  = R.anim.slide_in_bottom;
				iAnimaOut = R.anim.none;
				break;
			case NSAnimationID.ANIMATION_ID_SLIDE_IN_BOTTOM_DELAYED:
				bUponLast = true;
				iAnimaIn  = R.anim.slide_in_bottom;
				iAnimaOut = R.anim.long_anim_time;
				break;
			case NSAnimationID.ANIMATION_ID_OUT_DELAYED:
				bUponLast = true;
				iAnimaIn  = R.anim.none;
				iAnimaOut = R.anim.long_anim_time;
				break;
			case NSAnimationID.ANIMATION_ID_SLIDE_OUT_BOTTOM:
				bUponLast = false;
				iAnimaIn  = R.anim.none;
				iAnimaOut = R.anim.slide_out_bottom;
				break;
			case NSAnimationID.ANIMATION_ID_ALPHA_TRANSPARANT:
				iAnimaIn  = R.anim.alpha_trans_2_1;
				iAnimaOut = R.anim.alpha_trans_2_0;
				break;
			case NSAnimationID.ANIMATION_ID_NONE:
				iAnimaIn  = R.anim.none;
				iAnimaOut = R.anim.none;
				break;
			default:
				iAnimaIn  = -1;
				iAnimaOut = -1;
				break;				
			}
			GetViewManager().InsertViewMenuBase(bUponLast,iAnimaIn,iAnimaOut);
			cMenuBase.Show();				
		}else{
			Log.d(TAG,SUBTAG+"-----Failed to insert view menu------");
		}
		m_iAnimaID = NSAnimationID.ANIMATION_ID_DEFAULT;
		m_bJustPush	= false;		
		m_cMenuControlHandler.sendEmptyMessageDelayed(MenuControl_Message.MN_MSG_UnlockInput,50);
		m_cScreenInfoStack.LogoutStackInfo();
		
	}
			
	
	public  void NotifyWinchangeEnd(){
		if(!IsWinChangeLocked()){
			return;
		}		
		m_cMenuControlHandler.sendEmptyMessage(MenuControl_Message.MN_MSG_WinchangeEnd);
	}
	
	
	private void OnMsg_WinchangeEnd(){
		Log.d(TAG,SUBTAG+"-----OnWinchangeEnd------");
		while( null!=m_cScreenInfoStack.GetPopedScreenInfoObjects() &&
				   !m_cScreenInfoStack.GetPopedScreenInfoObjects().empty())
		{
			ScreenInfoStack.ScreenInfoObj cScreenInfoObj =m_cScreenInfoStack.GetPopedScreenInfoObjects().pop();
			GetViewManager().DestroyViewMenuBase(cScreenInfoObj.GetId().toString());

			cScreenInfoObj.Clr();
		}

		/* set map view Visibility */
		if (m_bSetMapInVisiable)
		{
			m_bSetMapInVisiable = false;
			MapView mapviewtemp = MapView.getInstance();
			if (null != mapviewtemp)
			{
				mapviewtemp.setVisibility(View.INVISIBLE);
			}
		}
		
		//Post delayed Trigger in the stack to screen
		while(!m_cTriggerStack.empty()){						
			NSTriggerInfo cTrigger = m_cTriggerStack.pop();
			cTrigger.m_bDelayed = true;
			Log.d(TAG,SUBTAG+"-----post Trigger["+ cTrigger.m_iTriggerID+"] in stack to Handler----" );
			m_cMenuControlHandler.sendMessageAtFrontOfQueue(m_cMenuControlHandler.obtainMessage(MenuControl_Message.MN_MSG_TriggerInfo,cTrigger));			
		}
		LockWinChange(false);
		UnlockAll();
	}
	
	@SuppressWarnings("rawtypes")
	private boolean SetActiveWinscape(Class cls)
	{		
		if( isSuperClass(cls, ADT_ListMenuBase.class) ||
			isSuperClass(cls, ADT_MenuBase.class) ||
			isSuperClass(cls, ADT_PreferenceMenuBase.class))
		{
			return true; //always return true
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	private static boolean isSuperClass(Class childClass, Class superClass){
		if(childClass.equals(Object.class)){
			return false;
		}
		
		if(childClass.getSuperclass().equals(superClass)){
			return true;
		} else {
			return isSuperClass(childClass.getSuperclass(), superClass);
		}
	}
	
	private static final int    LOCK_UNLOCKALL			= 0x00000000;
	private static final int    LOCK_WINCHANGE			= 0x00000001;
	private static final int    LOCK_SCREEN				= 0x00000010;
	private static final int    LOCK_INPUT				= 0x00000100;
	private int 				m_iLocks				= LOCK_UNLOCKALL;
	
	public synchronized void LockScreen(boolean bLock)
	{			
		if(bLock){
			m_iLocks |= LOCK_SCREEN;
		}else{
			m_iLocks &= ~LOCK_SCREEN;
		}
		Log.d(TAG,SUBTAG+"Lock Screen:"+bLock+" All Lock:"+m_iLocks);
	}
	
	final public boolean IsScreenLocked()
	{
		return (0!=(m_iLocks&LOCK_SCREEN));
	}	
	
	private void LockWinChange(boolean bLock){		
		if(bLock){
			m_iLocks |= LOCK_WINCHANGE;
		}else{
			m_iLocks &= ~LOCK_WINCHANGE;
		}
		Log.d(TAG,SUBTAG+"Lock WinChange:"+bLock+" All Lock:"+m_iLocks);
	}
	
	final public boolean IsWinChangeLocked(){
		return (0!=(m_iLocks&LOCK_WINCHANGE));
	}
	
	final private void LockInput(boolean bLock){
		if(bLock){
			m_iLocks |= LOCK_INPUT;
		}else{
			m_iLocks &= ~LOCK_INPUT;
		}
		Log.d(TAG,SUBTAG+"Lock Input:"+bLock+" All Lock:"+m_iLocks);
	}
	
	final private boolean IsInputLocked(){
		return (0!=(m_iLocks&LOCK_INPUT));
	}
	
	final private void UnlockAll(){
		m_iLocks = LOCK_UNLOCKALL;
		
		Log.d(TAG,SUBTAG+"!UnlockAll!  All Lock:"+m_iLocks);
	}
	
	public void setWinchangeWithoutAnimation(){
		SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_NONE);
	}
	
	@SuppressWarnings("rawtypes")
	private boolean WinChange(Class winCls,int winchangtype)
	{			
		Class modifiedWinCls = getChildClass(winCls);
		Log.d(TAG,SUBTAG+"Screen["+modifiedWinCls+"]ChangeType["+winchangtype+"]");
		if(IsWinChangeLocked()){
			Log.w(TAG,SUBTAG+"Win Change Failed["+modifiedWinCls+"]["+winchangtype+"]");
			return false;
		}
		
		if(!m_cScreenInfoStack.Empty()){
			m_cLastWinscapeCls = m_cScreenInfoStack.GetTopInfo().GetWinClass();
		}else{
			m_cLastWinscapeCls = null;
		}
		if(null != m_cLastWinscapeCls){
			boolean isNextScreenMap = false;
			boolean isCurrentScreenMap = false;
			if(null == modifiedWinCls){
				if(IsMapScreen(GetHierarchyBelowWinscapeClass())){
					isNextScreenMap = true;
				}
			}
			else{
				if(IsMapScreen(modifiedWinCls)){
					isNextScreenMap = true;
				}
			}
			
			if(IsMapScreen(m_cLastWinscapeCls)){
				isCurrentScreenMap = true;
			}
			if(m_iAnimaID == NSAnimationID.ANIMATION_ID_DEFAULT){
				if(isNextScreenMap && isCurrentScreenMap){
					MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_ALPHA_TRANSPARANT);
				}
				else if(isNextScreenMap){
					MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_SLIDE_OUT_BOTTOM);
				}
				else if(isCurrentScreenMap){
					MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_SLIDE_IN_BOTTOM);
				}
				else{
					MenuControlIF.Instance().SetWinchangeAnimation(NSAnimationID.ANIMATION_ID_DEFAULT);
				}
			}

		}else{
		}
		
		switch(winchangtype)
		{
		case NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD:
		{
			m_cScreenInfoStack.Push(modifiedWinCls, winchangtype,null);
		}
		break;
		case NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD_KEEPDEPTH:
		{	
			m_cScreenInfoStack.Pop();
			m_cScreenInfoStack.Push(modifiedWinCls, winchangtype,null);
		}	
		break;
		case NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD_DEFAULT:
		{	
			m_cScreenInfoStack.PopAll();
			m_cScreenInfoStack.Push(modifiedWinCls, winchangtype,null);
		}
		break;		
		case NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD_KEEPDEFAULT:
		{	
			m_cScreenInfoStack.PopUntilByDepth(1);
			m_cScreenInfoStack.Push(modifiedWinCls, winchangtype,null);
		}	
		break;
		case NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD_SEARCH:
		{	
			if(true == m_cScreenInfoStack.PopUntilByWinCls(modifiedWinCls)){
				m_cScreenInfoStack.Pop();
				m_cScreenInfoStack.Push(modifiedWinCls, winchangtype,null);
			}else{
				return false;
			}
		}
		break;
		case NSWinchangeType.MN_WINCHANGE_TYPE_BACK:
		{
			int iStackDepth = m_cScreenInfoStack.GetDepth();
			if(0==iStackDepth || 1==iStackDepth){
				return false;
			}else{
				m_cScreenInfoStack.Pop();
				m_cScreenInfoStack.GetTopInfo().Set(modifiedWinCls, winchangtype, null);
			}
		}
		break;			
		case NSWinchangeType.MN_WINCHANGE_TYPE_BACK_DEFAULT:
		{	
			int iStackDepth = m_cScreenInfoStack.GetDepth();
			if(0==iStackDepth || 1==iStackDepth){
				return false; //do nothing
			}else{
				m_cScreenInfoStack.PopUntilByDepth(1);
				m_cScreenInfoStack.GetTopInfo().Set(modifiedWinCls, winchangtype, null);
			}
		}	
		break;
		case NSWinchangeType.MN_WINCHANGE_TYPE_BACK_SEARCH:
		{	
			int iStackDepth = m_cScreenInfoStack.GetDepth();
			if(true == m_cScreenInfoStack.PopUntilByWinCls(modifiedWinCls)){
				if(iStackDepth == m_cScreenInfoStack.GetDepth()){
					return false;
				}
				m_cScreenInfoStack.GetTopInfo().Set(modifiedWinCls, winchangtype, null);
			}else{
				return false;
			}
		}	
		break;		
		//case WinchangeType.MN_WINCHANGE_TYPE_BACK_NAVIGATION:
		//{				
		//}	
		//break;
		default:
			return false;
		}
		
		if(m_bJustPush){
			m_bJustPush = false;
		}else{
			LockScreen(true);
			LockWinChange(true);
			LockInput(true);
			m_cMenuControlHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					OnMsg_ContinueWinchange();
				}
			}, 20);//wait 20ms to finish the change of image of button
			//m_cMenuControlHandler.sendEmptyMessage(MenuControl_Message.MN_MSG_ContinueWinchange);
			//m_cMenuControlHandler.sendMessageAtFrontOfQueue(m_cMenuControlHandler.obtainMessage(MenuControl_Message.MN_MSG_ContinueWinchange));
		}
		return true;
	}
	
	@Override
	public boolean BackWinChange()
	{
		return WinChange(null,NSWinchangeType.MN_WINCHANGE_TYPE_BACK);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean BackSearchWinChange(Class wincls)
	{
		return WinChange(wincls,NSWinchangeType.MN_WINCHANGE_TYPE_BACK_SEARCH);
	}
	
	@Override
	public boolean BackDefaultWinChange()
	{
		return WinChange(null,NSWinchangeType.MN_WINCHANGE_TYPE_BACK_DEFAULT);
	}
		
	@SuppressWarnings({ "rawtypes"})
	@Override
	public boolean ForwardWinChange(Class wincls)
	{
		return WinChange(wincls,NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean ForwardSearchWinChange(Class wincls)
	{
		return WinChange(wincls,NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD_SEARCH);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean ForwardKeepDepthWinChange(Class wincls)
	{
		return WinChange(wincls,NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD_KEEPDEPTH);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean ForwardDefaultWinChange(Class wincls)
	{
		return WinChange(wincls,NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD_DEFAULT);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean ForwardKeepDefaultWinChange(Class wincls)
	{
		return WinChange(wincls,NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD_KEEPDEFAULT);
	}
		
	public void SendTestTrigger(NSTriggerInfo triggerinfo ,long delaymillis)
	{	
		m_cMenuControlHandler.sendMessageDelayed(m_cMenuControlHandler.obtainMessage(MenuControl_Message.MN_MSG_TestTrigger,triggerinfo), delaymillis);
	}	
	
	private void  OnMsg_TestTrigger(NSTriggerInfo triggerinfo)
	{
		TriggerForScreen(triggerinfo);
	}
	
	
	
	@Override
	public boolean ForwardWinChangeByInsertWinsToStackBelowTop(Class<?>[] jumpwincls, Class<?> wincls) {
		// TODO Auto-generated method stub
		if(null != jumpwincls) {
			for(Class<?> c : jumpwincls){
				if(null != c) {
					m_cScreenInfoStack.Push(c ,NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD,null);
				}
			}
		}
		return WinChange(wincls,NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD);
	}
	
	@Override
	public boolean ForwardWinChangeByInsertWinsToStackBelowTopDeleteCurWin(
			Class<?>[] jumpwincls, Class<?> wincls) {
		// TODO Auto-generated method stub
		m_cScreenInfoStack.Pop();
		if(null != jumpwincls) {
			for(Class<?> c : jumpwincls){
				if(null != c) {
					m_cScreenInfoStack.Push(c ,NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD,null);
				}
			}
		}
		return WinChange(wincls,NSWinchangeType.MN_WINCHANGE_TYPE_FORWARD);
	}

	@Override
	public void SetWinchangeAnimation(int iAnimaID)
	{
		if(iAnimaID < NSAnimationID.ANIMATION_ID_NONE || iAnimaID > NSAnimationID.ANIMATION_ID_ALPHA_TRANSPARANT){
			Log.d(TAG,TAG+" AnimationID error, please see the reference");
			return;
		}
		m_iAnimaID = iAnimaID;
	}

	@Override
	public int GetWinchangeType()
	{
		ScreenInfoStack.ScreenInfoObj obj = m_cScreenInfoStack.GetTopInfo();
		if(null != obj){
			return obj.GetWinChangeType();
		}else{
			return NSWinchangeType.MN_WINCHANGE_TYPE_UNKNOWN;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	final public Class GetCurrentWinscapeClass()
	{
		return m_cCurWinscapeCls ;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	final public Class GetNextWinscapeClass()
	{
		return m_cNextWinscapeCls;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	final public Class GetLastWinscapeClass(){
		return m_cLastWinscapeCls;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class GetHierarchyBelowWinscapeClass()
	{
		ScreenInfoStack.ScreenInfoObj obj = m_cScreenInfoStack.GetPenultInfo();
		if(null != obj){
			return obj.GetWinClass();
		}else{
			return null;
		}
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class GetDefaultWinscapeClass(){
		ScreenInfoStack.ScreenInfoObj obj = m_cScreenInfoStack.GetBottomInfo();
		if(null != obj){
			return obj.GetWinClass();
		}else{
			return null;
		}
	}
	
//	@Override
//	public void JustPushWinscape(){
//		m_bJustPush = true;
//	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean SearchWinscape(Class wincls) {
		return m_cScreenInfoStack.SearchByWinCls(wincls);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean SearchWinscapeExceptCurrent(Class wincls) {
		return m_cScreenInfoStack.SearchByWinClsExceptTop(wincls);
	}
	
	private  boolean	m_bProhibited = false;
	
	public void DriveModeChanged()
	{
//		jniSetupControl setupCtl = new jniSetupControl();
//		if(jniSetupControl.STUPDM_COMMON_ON == setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DRIVE_MODE)){
//			NSTriggerInfo TriggerInfo = new NSTriggerInfo();
//			TriggerInfo.m_iTriggerID = NSTriggerID.UIC_MN_TRG_MC_PROHIBITION_CHANGE;
//			TriggerInfo.m_lParam1 = (m_bProhibited)?1:0;
//			TriggerForScreen(TriggerInfo);
//		}
	}	
	
	public boolean IsProhibited(){
//		if(true==IsNaviLoaded()){
//			jniSetupControl setupCtl = new jniSetupControl();
//			if(jniSetupControl.STUPDM_COMMON_ON == setupCtl.GetInitialStatus(jniSetupControl.STUPDM_DRIVE_MODE)){
				return m_bProhibited;
//			}else{
//				return false;
//			}			
//		}else{
//			return false;
//		}
	}
	
//	public void SendTestProibition(boolean bProhibited){
//		m_cMenuControlHandler.sendMessage(m_cMenuControlHandler.obtainMessage(MenuControl_Message.MN_MSG_Prohibition,(bProhibited?1:0),0));
//	}	
	
//	private void OnMsg_Prohibition(boolean bProhibited){		
//		Log.d(TAG,SUBTAG+ "OnMsg_Prohibition["+bProhibited+"]");		
//		Object curobj = GetViewManager().GetCurMenuBase();
//		IMenuBase curMenuBase = null;
//		if(curobj instanceof IMenuBase){
//			curMenuBase = (IMenuBase)curobj;
//		}
//		if(null != curMenuBase){
//			curMenuBase.Prohibition(bProhibited);
//		}
//	}
	
	public void RegisterBroadcastReceiver(Context c){
		Log.d(TAG,SUBTAG+ "RegisterBroadcastReceiver["+c+"]");	
	}
	
	public void UnregisterBroadcastReceiver(Context c){
		Log.d(TAG,SUBTAG+ "UnregisterBroadcastReceiver["+c+"]");	
	}

	
	private boolean m_bNaviLoaded = false;
	
	public final boolean IsNaviLoaded(){
		return m_bNaviLoaded;
	}
	
	public Activity GetCurrentActivity(){return NSViewManager.GetViewManager().getLocalActivityManager().getCurrentActivity();};
	
	private boolean m_bQuit = false;
	
	public final void Quit(){ m_bQuit = true;}
	
	@Override
	public final boolean CheckQuit(){return m_bQuit;}
	
	@Override
	public void OnConfigurationChanged(Configuration newConfig)
	{
		for(ScreenInfoStack.ScreenInfoObj i:m_cScreenInfoStack.GetScreenInfoObjects())
		{
			final Activity activity = (Activity)GetViewManager().GetMenuBaseById(i.id.toString());
			if(null!=activity){
				activity.onConfigurationChanged(newConfig);
			}		
		}
	}
	//-------------------------------JNI------------------------------

	public interface MapScreen{
	}
	
	public interface InitlizationScreen{
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean IsMapScreen(Class cls){
		if(null == cls){
			return false; 
		}
		Class[] ss = cls.getInterfaces();
		for(Class s : ss){
			if(s.equals(MapScreen.class)){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean IsInitlizationScreen (Class cls) {
		if(null == cls){
			return false; 
		}
		Class[] ss = cls.getInterfaces();
		for(Class s : ss){
			if(s.equals(InitlizationScreen.class)){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	private static final Class[] s_ArrayLightBGCls = {
	};
	
	@SuppressWarnings("rawtypes")
	private static boolean IsLightBgScreen(Class cls){
		for(int i=0;i<s_ArrayLightBGCls.length;i++){
			if(cls.equals(s_ArrayLightBGCls[i])){
				return true;
			}
		}
		return false;		
	}
	
	public class MN_WinAttr{
		public static final int BG_TYPE_NONE =  0;
		public static final int BG_TYPE_BLACK = 1;
		public static final int BG_TYPE_LIGHT = 2;
		public int    BackgroundType = BG_TYPE_NONE;
		
	};
	
	@SuppressWarnings("rawtypes")
	public MN_WinAttr GetWinAttribute(Class cls)
	{
		MN_WinAttr winAttr = new MN_WinAttr();
		
		if(IsMapScreen(cls) || cls.getSimpleName().startsWith("ADT_")){
			winAttr.BackgroundType = MN_WinAttr.BG_TYPE_NONE;			
		}else if(IsLightBgScreen(cls)){
			winAttr.BackgroundType = MN_WinAttr.BG_TYPE_LIGHT;	
		}else{
			winAttr.BackgroundType = MN_WinAttr.BG_TYPE_BLACK;	
		}
		
		return winAttr;
	}
	
	public boolean SetScreenOrientation(int screenOrientation) {
		Log.d(TAG,SUBTAG+"SetScreenOrientation:"+screenOrientation);
		Activity activity = NSViewManager.GetViewManager();
		if(null == activity){
			return false;
		}
		switch(screenOrientation){
		case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
		case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
		case ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED:
		case ActivityInfo.SCREEN_ORIENTATION_USER:
		case ActivityInfo.SCREEN_ORIENTATION_SENSOR:	
		case ActivityInfo.SCREEN_ORIENTATION_NOSENSOR:	
			activity.setRequestedOrientation(screenOrientation);
			return true;
		default:
			return false;
		}		
	}
	
    public  boolean SetFullScreen(boolean bFullScreen){
    	Log.d(TAG,SUBTAG+"SetFullScreen:" + bFullScreen);
    	Activity activity = NSViewManager.GetViewManager();
		if(null == activity){
			return false;
		}
		
    	if(bFullScreen){
    		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
    	}else{
    		activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
    	}
    	return true;
    }
    

	public static boolean IsRoutineTrigger( int iTriggerID)
	{
		switch(iTriggerID){
		case NSTriggerID.UIC_MN_TRG_GUIDE_ROUTE_INFO_CHANGE:
		case NSTriggerID.UIC_MN_TRG_LOC_FEEDBACK:
		case NSTriggerID.UIC_MN_TRG_DRIR_GSADJUST:
		case NSTriggerID.UIC_MN_TRG_MAP_DRAW_DONE:
		case NSTriggerID.UIC_MN_TRG_MAP_DIR_CHANGED:
		case NSTriggerID.UIC_MN_TRG_DRIR_IRDRAW:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean CheckUserInteractionEnabled(){
		do{
			if(null==instance){
				return false;
			}
			if(instance.CheckQuit()){
				return false;
			}
			if(instance.IsScreenLocked()){
				return false;
			}
			if(instance.IsWinChangeLocked()){
				return false;
			}
			if(instance.IsInputLocked()){
				return false;
			}
		}while(false);
		
		return true;
	}
}
