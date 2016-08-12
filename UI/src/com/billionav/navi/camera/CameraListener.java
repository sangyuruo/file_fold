package com.billionav.navi.camera;

/**
 * An interface which contains a callback for the picture ready after taking a picture.
 */

public interface CameraListener {
	void onImageCompleted(byte[] data);
}
