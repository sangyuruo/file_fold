package com.billionav.voicerecogJP.VR;



public class VrAnalysisResult {
	public static final int DOCOMO_ANALYSIS_ERR_NOERROR = 0;
	public static final int DOCOMO_ANALYSIS_ERR_COMMONERROR = 1;
	public static final int DOCOMO_ANALYSIS_ERR_GETTAG = 2;

	private int analysis_result;
	private int command_id;
	private double use_lat;
	private double use_lot;
	private String keyword1;
	private String keyword2;
	private String keyword3;
	private String keyword4;
	private String keyword5;
	private String result_type;
	private String result_encode;
	private String result_string;

	public void Init() {
		analysis_result = DOCOMO_ANALYSIS_ERR_COMMONERROR;
		command_id = 0;
		use_lat = 0;
		use_lot = 0;
		keyword1 = "";
		keyword2 = "";
		keyword3 = "";
		keyword4 = "";
		keyword5 = "";
		result_type = "";
		result_encode = "";
		result_string = "";
	}

	public int getAnalysis_result() {
		return analysis_result;
	}

	public int getCommand_id() {
		return command_id;
	}

	public double getUse_lat() {
		return use_lat;
	}

	public double getUse_lot() {
		return use_lot;
	}

	public String getKeyword1() {
		return keyword1;
	}

	public String getKeyword2() {
		return keyword2;
	}

	public String getKeyword3() {
		return keyword3;
	}

	public String getKeyword4() {
		return keyword4;
	}

	public String getKeyword5() {
		return keyword5;
	}

	public String getResult_type() {
		return result_type;
	}

	public String getResult_encode() {
		return result_encode;
	}

	public String getResult_string() {
		return result_string;
	}

	public void setAnalysis_result(int analysis_result) {
		this.analysis_result = analysis_result;
	}

	public void setCommand_id(int command_id) {
		this.command_id = command_id;
	}

	public void setUse_lat(double use_lat) {
		this.use_lat = use_lat;
	}

	public void setUse_lot(double use_lot) {
		this.use_lot = use_lot;
	}

	public void setKeyword1(String keyword1) {
		this.keyword1 = keyword1;
	}

	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}

	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}

	public void setKeyword4(String keyword4) {
		this.keyword4 = keyword4;
	}

	public void setKeyword5(String keyword5) {
		this.keyword5 = keyword5;
	}

	public void setResult_type(String result_type) {
		this.result_type = result_type;
	}

	public void setResult_encode(String result_encode) {
		this.result_encode = result_encode;
	}

	public void setResult_string(String result_string) {
		this.result_string = result_string;
	}
	
	
}
