package com.billionav.voicerecog;

public interface VrEngineListener {
	void onCommandUploaded(String contentID, String externID);
	void onSpeechReady();
	void onSpeechBegin();
	void onSpeechEnd();
	void onSpeechError(int err);
	void onSpeechResult(String result, int confidence);
}
