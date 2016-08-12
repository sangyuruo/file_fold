package com.billionav.navi.menucontrol;

import com.billionav.jni.UIVoiceControlJNI;
import com.billionav.jni.jniVoicePlayIF;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class ADT_ListMenuBase extends ListActivity 
						  implements ITriggerListener,IMenuBase 
{
	private static final String TAG = "MenuControlIF";  
	private static final String SUBTAG = "MenuBase: ";
	protected class MENUBASE_STATUS{
		public static final int UNKNOWN = 0;
		public static final int CREATE 	= 1;
		public static final int START 	= 2;
		public static final int RESUME 	= 3;
		public static final int PAUSE 	= 4;
		public static final int STOP 	= 5;
		public static final int DESTROY = 6;
	}	
	protected int 		m_iCurStatus = MENUBASE_STATUS.UNKNOWN;
	protected boolean 	m_bMapScreen = false;
	
	public ADT_ListMenuBase() {		
		super();
		MenuControlIF.IncreaseWinInstance();
		m_bMapScreen = MenuControlIF.IsMapScreen(this.getClass());
		Log.e(TAG,SUBTAG+"constructor["+this+"] Total:"+MenuControlIF.GetWinInstanceCount());		
	}
	
	@Override
	protected void finalize() throws Throwable {
		MenuControlIF.DecreaseWinInstance();
		Log.e(TAG,SUBTAG+"finalize["+this+"] Total:"+MenuControlIF.GetWinInstanceCount());	
		super.finalize();
	}
	
	protected void OnCreate(Bundle savedInstanceState){
    	//
    }
	
	protected void OnResume() {
		
	}	

	protected void OnPause() {
		
	}
	
	protected void OnStop() {
		
	}
		
	protected void OnDestroy(){
		
	}
	
	public boolean DispatchKeyEvent(KeyEvent ev){
		return false;
	}
	
	public boolean OnKeyDown(int keyCode, KeyEvent event){
		return false;
	}
	
	public boolean DispatchTouchEvent(MotionEvent ev){
		return false;
	}
	
	public boolean OnTouchEvent(MotionEvent event){
		return false;
	}
	
	public boolean DispatchTrackballEvent(MotionEvent ev){
		return false;
	}
	
	@Override
	public void OnSetScreenInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnInitialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final boolean IsProhibited() {
		// TODO Auto-generated method stub
		return MenuControlIF.Instance().IsProhibited();
	}
	
	@Override
	public void OnProhibition(boolean bProhibited) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void OnShow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnExit(int eWinChangeInterruptOption) {
		// TODO Auto-generated method stub
		
	}
	
    @Override
    final protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);   
        Log.d(TAG,SUBTAG+"onCreate:"+this);
        m_iCurStatus = MENUBASE_STATUS.CREATE;
        OnCreate(savedInstanceState);
    }
  
    @Override
	final protected void onResume() {
		super.onResume();
		Log.d(TAG,SUBTAG+"onResume:"+this);		
		m_iCurStatus = MENUBASE_STATUS.RESUME;
		OnResume();
	}
    
    @Override
	final protected void onPause() {
		Log.d(TAG,SUBTAG+"onPause:"+this);
		m_iCurStatus = MENUBASE_STATUS.PAUSE;
		OnPause();
		super.onPause();
	}
    
	@Override
	final protected void onStop() {
		Log.d(TAG,SUBTAG+"onStop:"+this);
		m_iCurStatus = MENUBASE_STATUS.STOP;
		OnStop();
		super.onStop();
	}
	
	@Override
	final protected void onDestroy(){
		Log.d(TAG,SUBTAG+"onDestroy:"+this);
		m_iCurStatus = MENUBASE_STATUS.DESTROY;
		OnDestroy();
		super.onDestroy();
	}
		
	@Override
	final public boolean dispatchKeyEvent(KeyEvent ev) {
		//Log.d(TAG,"dispatchKeyEvent["+this+" "+ev.getKeyCode()+"]");
		if ((!MenuControlIF.CheckUserInteractionEnabled())||
			(m_iCurStatus>=MENUBASE_STATUS.PAUSE))
		{
			return true;
		} 
		
		switch(ev.getKeyCode()){
		case KeyEvent.KEYCODE_VOLUME_UP:
//			if(jniVoicePlayIF.Instance().volumeKeyEvent(true)){
//				return true;
//			}
			UIVoiceControlJNI.getInstance().volumeUP();
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
//			if(jniVoicePlayIF.Instance().volumeKeyEvent(false)){
//				return true;
//			}
			UIVoiceControlJNI.getInstance().volumeDown();
			break;
		default:
			break;		
		}
		
		if(DispatchKeyEvent(ev)){
			return true;
		}else{
			return super.dispatchKeyEvent(ev);
		}
	}

	@Override
	final public boolean dispatchTouchEvent(MotionEvent ev){
		//Log.d(TAG,"dispatchTouchEvent["+this+" "+ev.getAction()+"]");
		if ((!MenuControlIF.CheckUserInteractionEnabled())||
			(m_iCurStatus>=MENUBASE_STATUS.PAUSE))
		{
			return true;
		} 		
		
		if(DispatchTouchEvent(ev)){
			return true;
		}else{
			return super.dispatchTouchEvent(ev);
		}
	}

	@Override
	final public boolean dispatchTrackballEvent(MotionEvent ev){	
		//Log.d(TAG,"dispatchTrackballEvent["+this+" "+ev.getAction()+"]");
		if ((!MenuControlIF.CheckUserInteractionEnabled())||
			(m_iCurStatus>=MENUBASE_STATUS.PAUSE))
		{
			return true;
		} 
		
		if(DispatchTrackballEvent(ev)){
			return true;
		}else{
			boolean bRet = super.dispatchTouchEvent(ev);
			//Log.e(TAG,SUBTAG+"DispatchTouchEvent:"+(m_bMapScreen?bRet:true));		
			return m_bMapScreen?bRet:true;			
		}
	}

	@Override
	final public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((!MenuControlIF.CheckUserInteractionEnabled())||
			(m_iCurStatus>=MENUBASE_STATUS.PAUSE))
		{
			return true;
		} 
				
		Log.d(TAG,SUBTAG+" onKeyDown["+keyCode+" "+event.getAction()+"]"+this);
   	    if(OnKeyDown(keyCode, event)){
			 return true;
   	    }
   	    
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:	
//			if (MenuControlIF.Instance().IsNaviLoaded()){
//	  			jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE2);
//	  		}
			if (event.getRepeatCount() == 0) {
				MenuControlIF.Instance().BackWinChange();
			}
			return true;
	  	default:	  	
//	  		if (MenuControlIF.Instance().IsNaviLoaded()){
//	  			jniVoicePlayIF.Instance().PlayBeep(jniVoicePlayIF.BEEP_ID_TONE1);
//	  		}
	  		return super.onKeyDown(keyCode,event);		  
		}	
	}

	@Override
	final public boolean onTouchEvent(MotionEvent event)
	{
		if ((!MenuControlIF.CheckUserInteractionEnabled())||
			(m_iCurStatus>=MENUBASE_STATUS.PAUSE))
		{
			return true;
		} 
		
		if(OnTouchEvent(event)){
			return true;
		}
		return super.onTouchEvent(event);		  
	}
	
	@Override
	public boolean TriggerForWinscape(NSTriggerInfo cTriggerInfo)
	{			
		if(!MenuControlIF.IsRoutineTrigger(cTriggerInfo.m_iTriggerID))
		{
			Log.d(TAG,SUBTAG+"OnTrigger["+this+"]["+cTriggerInfo+"]");
		}	
		return OnTrigger(cTriggerInfo);
	}
	
	public boolean OnTrigger(NSTriggerInfo cTriggerInfo){	
		return true;
	}
	
	public void LockScreen() {
		MenuControlIF.Instance().LockScreen(true);
	}

	public void UnlockScreen() {
		MenuControlIF.Instance().LockScreen(false);
	}

	public void SetWinchangeAnimation(int iAnimaID) {
		MenuControlIF.Instance().SetWinchangeAnimation(iAnimaID);
	}
	
	public int GetWinchangeType() {
		return MenuControlIF.Instance().GetWinchangeType();
	}
	
	//----------------------------------
	public boolean BackWinChange()
	{
		Log.d(TAG,SUBTAG+"BackWinChange:"+this);
		return MenuControlIF.Instance().BackWinChange() ;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean BackSearchWinChange(Class wincls)
	{
		Log.d(TAG,SUBTAG+"BackSearchWinChange:"+this+" to "+wincls);
		return MenuControlIF.Instance().BackSearchWinChange(wincls) ;
	}
	
	public boolean BackDefaultWinChange()
	{
		Log.d(TAG,SUBTAG+"BackDefaultWinChange:"+this);
		return MenuControlIF.Instance().BackDefaultWinChange() ;
	}
		
	@SuppressWarnings("rawtypes")
	public boolean ForwardWinChange(Class wincls)
	{
		Log.d(TAG,SUBTAG+"ForwardWinChange:"+this+" to "+wincls);
		return MenuControlIF.Instance().ForwardWinChange(wincls) ;
	}

	@SuppressWarnings("rawtypes")
	public boolean ForwardSearchWinChange(Class wincls)
	{
		Log.d(TAG,SUBTAG+"ForwardSearchWinChange:"+this+" to "+wincls);
		return MenuControlIF.Instance().ForwardSearchWinChange(wincls) ;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean ForwardKeepDepthWinChange(Class wincls)
	{
		Log.d(TAG,SUBTAG+"ForwardKeepDepthWinChange:"+this+" to "+wincls);
		return MenuControlIF.Instance().ForwardKeepDepthWinChange(wincls);
	}
	
	@SuppressWarnings("rawtypes")
	public boolean ForwardDefaultWinChange(Class wincls)
	{
		Log.d(TAG,SUBTAG+"ForwardDefaultWinChange:"+this+" to "+wincls);
		return MenuControlIF.Instance().ForwardDefaultWinChange(wincls) ;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean ForwardKeepDefaultWinChange(Class wincls)
	{
		Log.d(TAG,SUBTAG+"ForwardKeepDefaultWinChange:"+this+" to "+wincls);
		return MenuControlIF.Instance().ForwardKeepDefaultWinChange(wincls) ;
	}
	
	//------------------------------------------------------
	@Override
	final public void SetScreenInfo()	
	{	
		Log.d(TAG,SUBTAG+"SetScreenInfo["+this+"]");
		OnSetScreenInfo();	
	}
	
	@Override
	final public void Initialize()	
	{
		Log.d(TAG,SUBTAG+"Initialize["+this+"]");
		OnInitialize();		
	}
	
	@Override
	final public void Prohibition(boolean bProhibited) {
		Log.d(TAG,SUBTAG+"Prohibition["+bProhibited+"]["+this+"]");
	    OnProhibition(bProhibited);
	}
	
	@Override
	final public void Exit()	
	{
		Log.d(TAG,SUBTAG+"Exit["+this+"]");
		OnExit(0);
	}
	
	@Override
	final public void Show()
	{
		Log.d(TAG,SUBTAG+"Show["+this+"]");
		OnShow();
	}
}
