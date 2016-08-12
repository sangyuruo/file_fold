package com.billionav.voicerecog;

public class VrEngineFactory {
	public static final String ENGINE_KEDA = "keda";
	public static final String ENGINE_GOOGLE = "google";
	public static VrEngine getVrEngine(String type) {
		 if (ENGINE_KEDA.equals(type)) {
		      return new VrEngineKeda();
		 }
		 return new VrEngineGoogle();
	}
}
