package com.billionav.navi.menucontrol;
import java.lang.reflect.Field;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.billionav.ui.R;

public abstract class NSViewManager extends ActivityGroup
									implements IViewManager
{
	
	abstract protected void OnCreate(Bundle savedInstanceState);
	abstract protected void OnStart();
	abstract protected void OnRestart();
	abstract protected void OnResume();	
	abstract protected void OnPause();	
	abstract protected void OnStop();	
	abstract protected void OnDestroy();
	
	protected static final String TAG = "MenuControlIF"; 
	protected static final String SUBTAG="NSViewManager: ";
	
	static public  NSViewManager GetViewManager(){return s_cThiz;}	
	static private NSViewManager s_cThiz = null;
	
	public static class MAIN_STATUS{
		public static final int UNKNOWN = 0;
		public static final int CREATE 	= 1;
		public static final int START 	= 2;
		public static final int RESUME 	= 3;
		public static final int PAUSE 	= 4;
		public static final int STOP 	= 5;
		public static final int DESTROY = 6;
	}
	static public int CURSTATUS = MAIN_STATUS.UNKNOWN;
	
	protected IMenuControl GetMenuControl()  {return MenuControlIF.Instance();}
	public FrameLayout 	   GetGlobalUpLayer(){return m_cContainer_UpLayer;}
	public FrameLayout 	   GetGlobalMenuLayer(){return m_cContainer_Menu;}
	public FrameLayout 	   GetGlobalDnLayer(){return m_cContainer_DnLayer;}
	
	protected FrameLayout 			m_cContainer	  	 = null; 	
	//{
	protected FrameLayout 			m_cContainer_UpLayer = null;
	protected FrameLayout 			m_cContainer_Menu 	 = null;	
	protected FrameLayout 			m_cContainer_DnLayer = null; 
	
	public Button sync = null;
	public TextView bgd;
	public FrameLayout applinkLogo;

	//}
	
	//--------------------Container_Menu------------------------
	private SafeViewFlipper			m_cFlipper_Menu = null;
	private View 					m_cView_Menu 	= null; 
	//--------------------Container_Menu------------------------
	
    /** Called when the activity is first created. */
    @Override
    public final void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);   
 
        Log.d(TAG, SUBTAG+"onCreate "+this);        
        
        s_cThiz = this;       
        
        setContentView(R.layout.nsviewmanager);   
        m_cContainer = (FrameLayout)(findViewById(R.id.ViewManager_Container_All));
        
        m_cContainer_Menu 	 = (FrameLayout)(findViewById(R.id.ViewManager_Container_Winscapse));
        m_cFlipper_Menu = new SafeViewFlipper(this);
        m_cContainer_Menu.addView(m_cFlipper_Menu);
        
        m_cContainer_UpLayer = (FrameLayout)(findViewById(R.id.ViewManager_Container_UpLayer));
        m_cContainer_DnLayer = (FrameLayout)(findViewById(R.id.ViewManager_Container_DnLayer));
        
        sync = (Button) (findViewById(R.id.syncButton));
        bgd = (TextView) findViewById(R.id.applinkText);
        applinkLogo = (FrameLayout) findViewById(R.id.applinkLogo);
        applinkLogo.setOnTouchListener(new OnTouchListener() {
        	public boolean onTouch(View v, MotionEvent event) {
        		return true;
        	}
        });
        
      	CURSTATUS = MAIN_STATUS.CREATE; 
		OnCreate(savedInstanceState);       
    }
    
    
    @Override
	protected final void onStart(){
    	super.onStart();
    	Log.d(TAG, SUBTAG+"onStart "+this);  
    	CURSTATUS = MAIN_STATUS.START;	
    	OnStart();		
    }
	
    @Override
	protected final void onRestart(){
    	super.onRestart();
    	Log.d(TAG, SUBTAG+"onRestart "+this);  
    	CURSTATUS = MAIN_STATUS.START;
    	OnRestart();			
    }
    
	@Override
	protected final void onResume() {		
		super.onResume();	
		Log.d(TAG, SUBTAG+"onResume "+this);  
		CURSTATUS = MAIN_STATUS.RESUME;	
		OnResume();					
	}
	
	@Override
	protected final void onPause(){
		Log.d(TAG, SUBTAG+"onPause "+this);  
		CURSTATUS = MAIN_STATUS.PAUSE;	
		OnPause();			
		super.onPause();
	}

	@Override
	protected final void onStop(){	
		Log.d(TAG, SUBTAG+"onStop "+this);  
		CURSTATUS = MAIN_STATUS.STOP;		
		OnStop();	
		super.onStop();
	}
	
	@Override
	protected final void onDestroy(){	
		Log.d(TAG, SUBTAG+"onDestroy "+this);  
		//PLog.d(TAG, "onDestroy");	
		CURSTATUS = MAIN_STATUS.DESTROY;
		getLocalActivityManager().dispatchDestroy(true);
		OnDestroy();	
		super.onDestroy();
		Log.d(TAG, SUBTAG+"onDestroy Finished "+this);  
	}
	
	@SuppressWarnings("static-access")
	@Override
	public final boolean dispatchKeyEvent(KeyEvent ev){	
		if(!MenuControlIF.CheckUserInteractionEnabled()) 
		{
			return true;
		}
		
		if ( (ev.getKeyCode() == KeyEvent.KEYCODE_BACK) && 
				( (ev.getFlags() & ev.FLAG_LONG_PRESS) != 0) ) {
			return true;
		}

		Activity activity = getLocalActivityManager().getCurrentActivity();
		if(null!=activity){
			activity.dispatchKeyEvent(ev);
		}
		
		if(m_cContainer_DnLayer.dispatchKeyEvent(ev)){
			return true;	
		}	
		
		return true;	
	}
	
	@Override
	public final boolean dispatchTouchEvent(MotionEvent ev) {	
		//PLog.d(TAG,"dispatchTouchEvent["+this+" "+ev.getAction()+"]");
		if(!MenuControlIF.CheckUserInteractionEnabled()) 
		{
			return true;
		}
		return super.dispatchTouchEvent(ev);	
	}	
	
	
	@Override
	public final boolean dispatchTrackballEvent(MotionEvent ev)
	{
		//PLog.d(TAG,"dispatchTrackballEvent["+this+" "+ev.getAction()+"]");
		if(!MenuControlIF.CheckUserInteractionEnabled()) 
		{
			return true;
		}
		return super.dispatchTrackballEvent(ev);
	}
	
	@Override
	public final IMenuBase GetCurMenuBase() {
		return (IMenuBase)getLocalActivityManager().getCurrentActivity();
	}
	
	@Override
	public IMenuBase GetMenuBaseById(String id)
	{
		return (IMenuBase)getLocalActivityManager().getActivity(id);
	}	
	
	@SuppressWarnings("rawtypes")
	public final void CreateViewMenuBase(Class cls,String id)
	{
		Log.d(TAG, SUBTAG+"Start new Activity,Current MenuBase:"+GetCurMenuBase()+" to "+cls);
		Intent intent  = new Intent(this,cls); 

		Window win_view = getLocalActivityManager().startActivity(id, intent);
		if(null==win_view){
			Log.e(TAG,SUBTAG+"create Activity:c"+ cls +" failed");
		}
		
		View view_activity = win_view.getDecorView();
		ViewGroup vGroup = (ViewGroup)view_activity.getParent();
		if(null!=vGroup){
			vGroup.removeView(view_activity);
		}
		
		m_cView_Menu = OnCreateViewMenuBase(view_activity,cls);		
	}
	
	@SuppressWarnings("rawtypes")
	public View OnCreateViewMenuBase(View activityview,Class activitycls)
	{
		return activityview;
	}
	
    public final void InsertViewMenuBase(boolean bUponLast,int iAnimIn,int iAnimOut)
    {  	
    	
    	Log.d(TAG, SUBTAG+"InsertViewMenuBase");
    	if(null == m_cFlipper_Menu || null == m_cView_Menu){
    		return;
    	}
    	int childCount = m_cFlipper_Menu.getChildCount();
    	if(childCount > 0){
    		for(int i=0; i < childCount; i++){
    			if(m_cFlipper_Menu.getChildAt(i)!= m_cFlipper_Menu.getCurrentView()){
        			m_cFlipper_Menu.removeViewAt(i);
        			childCount--;
        			i--;
    			}
    		}
    	}

    	try {  
       		if(-1!=iAnimIn && -1!=iAnimOut){
       			if(bUponLast){
       				m_cFlipper_Menu.setInAnimation(AnimationUtils.loadAnimation(this,iAnimIn));
       				m_cFlipper_Menu.setOutAnimation(AnimationUtils.loadAnimation(this,iAnimOut));
       				m_cFlipper_Menu.addView(m_cView_Menu);  
       				m_cFlipper_Menu.showNext();  
       			}else{
       				
       				int childCount2 = m_cFlipper_Menu.getChildCount();
       		    	
       		    	for(int i=0; i < childCount2; i++){
       		    		Log.d(TAG, SUBTAG+"Insert Down Layer pre"+m_cFlipper_Menu.getChildAt(i));       		             		    		
       		    	}	    	
       		    	
       				try{
       					m_cFlipper_Menu.addView(m_cView_Menu,0);  
	       				Class<?> classtype = android.widget.ViewAnimator.class;
	       				Field name = classtype.getDeclaredField("mWhichChild");
	       				name.setAccessible(true);
	       				name.set(m_cFlipper_Menu,1);
       				}catch( Exception e)
       				{
       					Log.d(TAG, SUBTAG+"Insert Down Layer classtype get failed");
       				}
       				
       				m_cFlipper_Menu.setInAnimation(AnimationUtils.loadAnimation(this,iAnimIn));
       				m_cFlipper_Menu.setOutAnimation(AnimationUtils.loadAnimation(this,iAnimOut));
       				
       				int childCount3 = m_cFlipper_Menu.getChildCount();
       				Log.d(TAG, SUBTAG+"Insert Down Layer after count"+childCount3);
       		    	for(int i=0; i < childCount3; i++){
       		    		Log.d(TAG, SUBTAG+"Insert Down Layer after"+m_cFlipper_Menu.getChildAt(i));       		             		    		
       		    	}
       		    	//m_cFlipper_Menu.showNext();
       				m_cFlipper_Menu.showPrevious();  
       				
       			}
       			
    		}else{
    			if(bUponLast){
    				m_cFlipper_Menu.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_right));
        			m_cFlipper_Menu.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_out_left));
    			}else{
    				m_cFlipper_Menu.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_left));
        			m_cFlipper_Menu.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_out_right));
        		
    			}
    			m_cFlipper_Menu.addView(m_cView_Menu);  
    	      	m_cFlipper_Menu.showNext();    
    		}
    		
		}catch (NotFoundException e) {
			//e.printStackTrace();
		}
		
    	  	
    }
    
    
    final public void DestroyViewMenuBase(String id){
    	Log.d(TAG,SUBTAG+"DestroyViewMenuBase:"+id);
    	if(null==id){
    		Log.w(TAG,"error bad input");
    		return;
    	}
    	Activity activity = getLocalActivityManager().getActivity(id);
    	if(null==activity){
    		Log.w(TAG,SUBTAG+"there is no Activity taged with "+id);
    		return;
    	}
    	
    	Log.d(TAG,SUBTAG+"destroy "+id);
    	getLocalActivityManager().destroyActivity(id,true);
   
    	Log.d(TAG,SUBTAG+"DestroyViewMenuBase End");
    }
    
    @Override
	final public void onConfigurationChanged(Configuration newConfig) 
    {
    	Log.d(TAG, SUBTAG+"onConfigurationChanged");
    	OnConfigurationChanged(newConfig);

    	super.onConfigurationChanged(newConfig);
	}
    
    public void OnConfigurationChanged(Configuration newConfig)
    {
    	if (null != GetMenuControl()){
        	GetMenuControl().OnConfigurationChanged(newConfig);    	
    	}
    }     
    
    class SafeViewFlipper extends ViewFlipper { 
    	private int m_iAnimCount = 0;
	    public SafeViewFlipper(Context context) { 
	        super(context); 
	        
	    } 

	    @Override 
	    protected void onDetachedFromWindow() { 
	        try{ 
	            super.onDetachedFromWindow(); 
	        }catch(Exception e) { 
	        	//e.printStackTrace();
	            stopFlipping(); 
	        } 
	    } 
	    
	    private AnimationListener m_ViewFlipperListener = new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation animation) {
				Log.w(TAG,SUBTAG+"ViewFlipper Animation Listener:"+animation+" End");
				m_iAnimCount--;
				if(m_iAnimCount<=0){
					MenuControlIF.Instance().NotifyWinchangeEnd();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}

			@Override
			public void onAnimationStart(Animation animation) {
				
			}    	
	    };
	 	    
	    @Override
	    public void showNext(){
    		try{    			
    			m_iAnimCount = getChildCount()<=1?1:2;
	    		getInAnimation().setAnimationListener(m_ViewFlipperListener);
	    		getOutAnimation().setAnimationListener(m_ViewFlipperListener);
	    		
    		}catch (NullPointerException e) {
				e.printStackTrace();
				m_iAnimCount = 0;
				MenuControlIF.Instance().NotifyWinchangeEnd();
	    	}
    		super.showNext();	    	
	    }
	    
	    
	    @Override
	    public void showPrevious(){
    		try{    			
    			m_iAnimCount = getChildCount()<=1?1:2;
	    		getInAnimation().setAnimationListener(m_ViewFlipperListener);
	    		getOutAnimation().setAnimationListener(m_ViewFlipperListener);
	    		
    		}catch (NullPointerException e) {
				e.printStackTrace();
				m_iAnimCount = 0;
				MenuControlIF.Instance().NotifyWinchangeEnd();
	    	}
    		super.showPrevious();	    	
	    }
	    
    }

}
