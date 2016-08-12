package com.billionav.ttsengine;

import android.app.Activity;
import android.os.Bundle;

/*
 * The Java portion of this TTS plugin engine app does nothing.
 * This activity is only here so that the native code can be
 * wrapped up inside an apk file.
 *
 * The file path structure convention is that the native library
 * implementing TTS must be a file placed here:
 * /data/data/<PACKAGE_NAME>/lib/libtts<ACTIVITY_NAME_LOWERCASED>.so
 * Example:
 * /data/data/com.pset.ttsengine/lib/libttskeda.so
 */

public class Keda extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The Java portion of this does nothing.
        // This activity is only here so that everything
        // can be wrapped up inside an apk file.
        finish();
    }
}
