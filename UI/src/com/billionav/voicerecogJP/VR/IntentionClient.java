package com.billionav.voicerecogJP.VR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.co.nttdocomo.ss.lib.adt.net.SSHttpClientFactory;
import jp.co.nttdocomo.ss.lib.adt.util.task.AsyncTaskLoader;
import jp.co.nttdocomo.ss.lib.adt.util.task.IAsyncCallback;

import org.json.JSONObject;

import com.billionav.navi.app.AndroidNaviAPP;

public class IntentionClient {
	private static final String URL_DEFAULT = "http://ec2-54-248-203-224.ap-northeast-1.compute.amazonaws.com/DDNInterfaceServerWeb/svc/jsonform";
	private static final String URL_SERVER = "http://ec2-54-248-203-224.ap-northeast-1.compute.amazonaws.com/DDNInterfaceServerWeb/svc/jsonform";
	public static final String PROJECT_NAME_EN = "iproEN";
	public static final String PROJECT_NAME_CH = "iproCH";
	private static final String PARAM_TYPE = "type";
	private static final String PARAM_VAL = "val";
	
	private static final int CONN_TIMEOUT = 10000;
	private static final int RECV_TIMEOUT = 10000;
	
	private List<ParamListener> paramListeners;
	private List<ResultListener> resultListeners;
	
	private AsyncTaskLoader asyncTask;
	private String voiceText;
	private JSONObject jsonResult;
	
	private static IntentionClient sInstance;
	
	/**
	 * Caller should implement it in order to pass parameters to intention request
	 * 
	 */
	public interface ParamListener {
		Map<String, ?> getParams();
	}
	
	/**
	 * Caller should implement it in order to receive intention server result
	 *
	 */
	public interface ResultListener {
		/**
		 * response from intention server
		 * @param result intention server return result
		 * @attention This method will be called by main thread(UI thread)
		 */
		void onResult(JSONObject result);
	}
	
	final IAsyncCallback iac = new IAsyncCallback() {
		@Override
		public void workToDo() {
			try {
				final JSONObject jsonReq = buildReqParams();
				System.out.println("machen url is + [" + URL_SERVER + "]");
				System.out.println("machen req json is [" + jsonReq + "]");
				jsonResult = SSHttpClientFactory.getInstance().post(jsonReq);
				System.out.println("machen  json result is ["+jsonResult+"]");
			} catch(Exception e ) {
			}
		}
		
		@Override
		public void onComplete() {
			asyncTask = null;
			Iterator<ResultListener> it = resultListeners.iterator();
		    while (it.hasNext()) {
		    	ResultListener item = it.next();
		    	item.onResult(jsonResult);
		    }
		}
	};
	
	public static IntentionClient instance() {
		if (null != sInstance) {
			return sInstance;
		}
		
		synchronized(IntentionClient.class) {
			if (null == sInstance) {
				sInstance = new IntentionClient();
			}
		}
		return sInstance;
	}
	
	protected IntentionClient() {
		asyncTask = null;
		paramListeners = new ArrayList<ParamListener>(2);
		resultListeners = new ArrayList<ResultListener>(2);
	}
	

	/**
	 * UI or Search caller should set it in order to pass parameters
	 *
	 */
	public boolean registerParamListener(ParamListener listener) {
		if (null == listener) return false;
		return paramListeners.add(listener);
	}
	
	public boolean unregisterParamListener(ParamListener listener) {
		if (null == listener) return false;
		return paramListeners.remove(listener);
	}

	/**
	 * Caller should set it in order to receive intention server result
	 *
	 */
	public boolean registerResultListener(ResultListener listener) {
		if (null == listener) return false;
		return resultListeners.add(listener);
	}


	public boolean init(String url) {
		final SSHttpClientFactory sscf = SSHttpClientFactory.getInstance();
		sscf.setUserAgent(getProjectName());
		sscf.setConnectionTimeout(CONN_TIMEOUT);
		sscf.setReceiveTimeout(RECV_TIMEOUT);
		sscf.setPostURL((null != url) ? url : URL_DEFAULT);
		return true;
	}
	
	public boolean init() {
		return init(URL_SERVER);
 	}
	
	public boolean request(String text) {
		if (null != asyncTask) {
			asyncTask.cancel(true);
		}
		jsonResult = null;
		voiceText = text;
		asyncTask = new AsyncTaskLoader();
		asyncTask.execute(iac);
		return true;
	}
	
	public void cancel() {
		if (null != asyncTask) {
			asyncTask.cancel(true);
		} else {
		}
	}
	
	private JSONObject buildReqParams() {
		final JSONObject joReq = new JSONObject();
	    try {
			    final JSONObject joParam1 = new JSONObject();
			    
			    
			    joParam1.put(PARAM_TYPE, "string");
				joParam1.put(PARAM_VAL, getProjectName());
			    joReq.put( "project_name" , joParam1 );
			    
			    final JSONObject joParam2 = new JSONObject();
			    joParam2.put(PARAM_TYPE, "string");
			    joParam2.put(PARAM_VAL, voiceText);
			    joReq.put("voice_text", joParam2);
	    	
	    } catch (Exception e) {
	    }
	    
		return joReq;
	}
	
	public String getProjectName(){
		int lanType = AndroidNaviAPP.getInstance().getLanguageType();
		if(lanType == AndroidNaviAPP.LANGUAGE_CH){
			return PROJECT_NAME_CH;
		}else if(lanType == AndroidNaviAPP.LANGUAGE_US){
			return PROJECT_NAME_EN;
		}
		return "";
	}
	
}
