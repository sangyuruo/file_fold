package com.billionav.voicerecogJP.VR;

import java.util.List;


public interface VrListener {
	void onStart(int engType);
	void onResults(List<String> list, int engType);
	void onError(int err, int engType);
}
