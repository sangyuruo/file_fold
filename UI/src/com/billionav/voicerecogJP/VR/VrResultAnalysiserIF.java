package com.billionav.voicerecogJP.VR;

import org.json.JSONObject;

import android.util.Log;


public class VrResultAnalysiserIF {
	private static String TAG  = "VrResultAnalysiserIF";
	private static VrResultAnalysiserIF m_cInstance;
	private VrAnalysisResult m_cResult;
	
	public void AnalysisResult(JSONObject jObject) 
	{
		VrResultAnalysiser Analysiser = new VrResultAnalysiser();
		m_cResult = Analysiser.AnalysisResult(jObject);
		Log.i(TAG, "resultAnaly.getAnalysis_result() = " + m_cResult.getAnalysis_result());	
		Log.i(TAG, "resultAnaly.getCommand_id() = " + m_cResult.getCommand_id());
		Log.i(TAG, "resultAnaly.getUse_lat() = " + m_cResult.getUse_lat());
		Log.i(TAG, "resultAnaly.getUse_lot() = " + m_cResult.getUse_lot());
		Log.i(TAG, "resultAnaly.getKeyword1() = " + m_cResult.getKeyword1());
		Log.i(TAG, "resultAnaly.getKeyword2() = " + m_cResult.getKeyword2());	
		Log.i(TAG, "resultAnaly.getResult_type() = " + m_cResult.getResult_type());
		Log.i(TAG, "resultAnaly.getResult_encode() = " + m_cResult.getResult_encode());
		Log.i(TAG, "resultAnaly.getResult_string() = " + m_cResult.getResult_string());	
	}
	
	/**
	 * @return analysis result
	 */
	public VrAnalysisResult getResult() {
		return m_cResult;
	}
	
	/**
	 * @return hash map for post param
	 */
//	public HashMap<String, ?> getParams() {
//		VrResultAnalysiser Analysiser = new VrResultAnalysiser();
//		return Analysiser.getParams();
//	}
	
	/**
	 * @return class Instance
	 */
	public static VrResultAnalysiserIF Instance() {
		if(m_cInstance == null)
		{
			m_cInstance = new VrResultAnalysiserIF();
		}
		return m_cInstance;
	}
}
