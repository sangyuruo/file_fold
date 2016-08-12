package com.billionav.navi.app.ext.util;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLSurface;

import junit.framework.Assert;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import com.billionav.navi.app.ext.log.NaviLogUtil;
import com.billionav.navi.naviscreen.mef.EXInputSurface;

public class MediaCoderHelper {
	private MediaCodec.BufferInfo mBufferInfo;
	private MediaCodec mEncoder;
	// p7
	public static int bitRate = 4000000;
	public static int videoWidth = 800;
	public static int videoHeight = 480;
	// public static int FRAME_RATE = 4;
	// public static int IFRAME_INTERVAL = 5;
//	public static String MIME_TYPE = "video/avc";
	public static String MIME_TYPE = "video/3gpp";

	// default
	public static int FRAME_RATE = 15;
	public static int IFRAME_INTERVAL = 10;
	// public static String MIME_TYPE = "video/avc";
	// public static int videoWidth = 800;
	// public static int videoHeight = 480;
	// public static int bitRate = 3000000;

	private static boolean is420SP = false;
	Surface surface = null;
	EGLSurface eglSurface = null;

	EXInputSurface mInputSurface;
	private long mFakePts = 0;

	public EXInputSurface getmInputSurface() {
		return mInputSurface;
	}

	public void setmInputSurface(EXInputSurface mInputSurface) {
		this.mInputSurface = mInputSurface;
	}

	public interface EncoderHandler {
		public void execute(byte[] dataToWrite, int dataSize);
	}

	public void releaseEncoder() {
		if (null != mEncoder) {
			mEncoder.stop();
			mEncoder.release();
			mEncoder = null;
		}
	}

	public void prepareEncoder() {
		NaviLogUtil.debugEglStep("start prepareEncoder...");
		mBufferInfo = new MediaCodec.BufferInfo();

		MediaCodecInfo codecInfo = selectCodec(MIME_TYPE);
		if (codecInfo == null) {
			// Don't fail CTS if they don't have an AVC codec (not here,
			// anyway).
			NaviLogUtil.debugMediacodec("codecInfo is null");
			return;
		}else{
			NaviLogUtil.debugMediacodec("codecInfo is not null, name is " + codecInfo.getName() );
		}
		// int colorFormat = selectColorFormat(codecInfo,
		// NaviConstant.MIME_TYPE);

		// byte[] pps = {0,0,0,1,104,-18,60,-128};
		// byte[] header_sps = { 0, 0, 0, 1, 103, 100, 0, 40, -84, 52, -59, 1,
		// -32, 17, 31, 120, 11, 80, 16, 16, 31, 0, 0, 3, 3, -23, 0, 0, -22, 96,
		// -108 };
		// byte[] header_pps = { 0, 0, 0, 1, 104, -18, 60, -128 };

		byte[] header_sps = { 0, 0, 0, 1, 40, -50, 2, 114, 72 };
		byte[] header_pps = { 0, 0, 0, 1, 39, 66, -32, 22, -115, 104, 10, 3,
				-38, 108, -128, 0, 0, 3, 0, -128, 0, 0, 3, 2, 7, -118, 17, 80 };
		MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE,
				videoWidth, videoHeight);
		// format.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
		// format.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
		// format.setByteBuffer("csd-1", ByteBuffer.wrap(pps));
		format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
				MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
//		format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
		// format.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
		format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
		format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
		// format.setInteger(MediaFormat.KEY_FRAME_RATE, null);
		format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);

		try {
			// mEncoder = MediaCodec.createByCodecName(codecInfo.getName());
			mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
			NaviLogUtil.debugMediacodec("mEncoder name is " + mEncoder.getName() );
			mEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
		} catch (Throwable ex) {
			NaviLogUtil.debugMediacodec("mediacodec configure ex : "
					+ ex.getMessage());
		}
		try {
			surface = mEncoder.createInputSurface();
			NaviLogUtil.debugEglStep("create inpute surface success.");
		} catch (Throwable ex) {
			NaviLogUtil.debugMediacodec("create surface ex : "
					+ ex.getMessage());
		}
		// mInputSurface = new EXInputSurface(surface);
		// mInputSurface = new CodecInputSurface(mEncoder.createInputSurface());
		mEncoder.start();
		NaviLogUtil.debugEglStep("mEncoder start success.");
	}

	public void start() {
		mEncoder.start();
	}

	public void reset() {
		NaviLogUtil.debugEglStep("mediacodec reset");
		mEncoder.stop();
		mEncoder.start();
	}

	public void drainEncoder(boolean isEnd, EncoderHandler handler) {
		NaviLogUtil.debugEglStep("handle queue data success...");
		if (null == mEncoder) {
			NaviLogUtil.debugEglStep("mEncoder is null");
			return;
		}
		ByteBuffer[] encoderOutputBuffers = mEncoder.getOutputBuffers();
		final int TIMEOUT_USEC = 10000;
		while (true) {
			int encoderStatus = mEncoder.dequeueOutputBuffer(mBufferInfo,
					TIMEOUT_USEC);

			NaviLogUtil
					.debugEglStep("encoderStatus(in while):" + encoderStatus);

			if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
				// no output available yet
				if (!isEnd) {
					Log.i("syxtest", "!!! INFO_TRY_AGAIN_LATER");
					// TODO 0323-1348
					break; // out of while
				} else {
				}
			} else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
				NaviLogUtil.debugEglStep("INFO_OUTPUT_BUFFERS_CHANGED");
				// not expected for an encoder
				encoderOutputBuffers = mEncoder.getOutputBuffers();
			} else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
				NaviLogUtil.debugEglStep("INFO_OUTPUT_FORMAT_CHANGED");
				MediaFormat format = mEncoder.getOutputFormat();
				// if( null == format ){
				// NaviLogUtil.debugEglStep("format is null");
				// }else{
				// debugInfo(format,format.KEY_BIT_RATE);
				// debugInfo(format,format.KEY_CHANNEL_COUNT);
				// debugInfo(format,format.KEY_CHANNEL_MASK);
				// debugLongInfo(format,format.KEY_DURATION);
				// debugInfo(format,format.KEY_FLAC_COMPRESSION_LEVEL);
				// debugLongInfo(format,format.KEY_HEIGHT);
				// debugInfo(format,format.KEY_I_FRAME_INTERVAL);
				// debugInfo(format,format.KEY_MAX_INPUT_SIZE);
				// debugInfo(format,format.KEY_MAX_WIDTH);
				// debugInfo(format,format.KEY_MAX_HEIGHT);
				// debugInfo(format,format.KEY_SAMPLE_RATE);
				// debugStringInfo(format,format.KEY_MIME);
				// debugFloatInfo(format,format.KEY_FRAME_RATE);
				// debugInfo(format,format.KEY_PUSH_BLANK_BUFFERS_ON_STOP);
				// debugLongInfo(format,format.KEY_REPEAT_PREVIOUS_FRAME_AFTER);
				// }
			} else if (encoderStatus < 0) {
				// let's ignore it
				NaviLogUtil
						.debugEglStep("unexpected result from encoder.dequeueOutputBuffer: "
								+ encoderStatus);
			} else {
				
				try {
					NaviLogUtil.debugEglStep("dequeue data , encoderStatus is "
							+ encoderStatus);
//					if (mBufferInfo.size != 0) {
//						mFakePts = getPTSUs();
//						mBufferInfo.presentationTimeUs = mFakePts;
//						// mFakePts += 1000000L / FRAME_RATE;
//						byte[] dataToWrite = new byte[mBufferInfo.size];
//						encoderOutputBuffers[encoderStatus].get(dataToWrite,
//								mBufferInfo.offset, mBufferInfo.size);
//						handler.execute(dataToWrite, mBufferInfo.size);
//					}
					ByteBuffer outputData = encoderOutputBuffers[encoderStatus];
					if (mBufferInfo.size != 0) {
						outputData.position(mBufferInfo.offset);
						outputData.limit(mBufferInfo.offset + mBufferInfo.size);
						byte[] dataToWrite = new byte[mBufferInfo.size];
						outputData.get(dataToWrite);
						handler.execute(dataToWrite, mBufferInfo.size);
					}
					
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				} finally {
					mEncoder.releaseOutputBuffer(encoderStatus, false);
				}

				// mEncoder.releaseOutputBuffer(encoderStatus, false);
				if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
					if (!isEnd) {
					} else {
					}
					break; // out of while
				}

			}
		}
	}

	private void debugInfo(MediaFormat format, String key) {
		try {
			NaviLogUtil.debugEglStep(String.format("%s is %d", key,
					format.getInteger(key)));
		} catch (Throwable ex) {
			NaviLogUtil.debugEglStep(String.format(
					"debug is error ,key is %s, error is %s", key,
					ex.getMessage()));
		}
	}

	private void debugFloatInfo(MediaFormat format, String key) {
		try {
			NaviLogUtil.debugEglStep(String.format("%s is %f", key,
					format.getFloat(key)));
		} catch (Throwable ex) {
			NaviLogUtil.debugEglStep(String.format(
					"debug is error ,key is %s , error is %s", key,
					ex.getMessage()));
		}
	}

	private void debugLongInfo(MediaFormat format, String key) {
		try {
			NaviLogUtil.debugEglStep(String.format("%s is %d", key,
					format.getLong(key)));
		} catch (Throwable ex) {
			NaviLogUtil.debugEglStep(String.format(
					"debug is error ,key is %s , error is %s", key,
					ex.getMessage()));
		}
	}

	private void debugStringInfo(MediaFormat format, String key) {
		try {
			NaviLogUtil.debugEglStep(String.format("%s is %s", key,
					format.getString(key)));
		} catch (Throwable ex) {
			NaviLogUtil.debugEglStep(String.format(
					"debug is error ,key is %s , error is %s", key,
					ex.getMessage()));
		}
	}

	static MediaCoderHelper instance = null;

	public static MediaCoderHelper getInstance() {
		if (null == instance) {
			instance = new MediaCoderHelper();
		}
		return instance;
	}

	private MediaCoderHelper() {
	}

	private static MediaCodecInfo selectCodec(String mimeType) {
		int numCodecs = MediaCodecList.getCodecCount();
		for (int i = 0; i < numCodecs; i++) {
			MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

			if (!codecInfo.isEncoder()) {
				continue;
			}

			String[] types = codecInfo.getSupportedTypes();
			for (int j = 0; j < types.length; j++) {
				if (types[j].equalsIgnoreCase(mimeType)) {
					return codecInfo;
				}
			}
		}
		return null;
	}

	/**
	 * Returns a color format that is supported by the codec and by this test
	 * code. If no match is found, this throws a test failure -- the set of
	 * formats known to the test should be expanded for new platforms.
	 */
	private static int selectColorFormat(MediaCodecInfo codecInfo,
			String mimeType) {
		MediaCodecInfo.CodecCapabilities capabilities = codecInfo
				.getCapabilitiesForType(mimeType);
		for (int i = 0; i < capabilities.colorFormats.length; i++) {
			int colorFormat = capabilities.colorFormats[i];
			if (isRecognizedFormat(colorFormat)) {
				return colorFormat;
			}
		}
		return 0; // not reached
	}

	/**
	 * Returns true if this is a color format that this test code understands
	 * (i.e. we know how to read and generate frames in this format).
	 */
	private static boolean isRecognizedFormat(int colorFormat) {
		switch (colorFormat) {
		// these are the formats we know how to handle for this test
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
			is420SP = false;
			return true;
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
		case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
			is420SP = true;
			return true;
		default:
			return false;
		}
	}

	public MediaCodec.BufferInfo getmBufferInfo() {
		return mBufferInfo;
	}

	public void setmBufferInfo(MediaCodec.BufferInfo mBufferInfo) {
		this.mBufferInfo = mBufferInfo;
	}

	public MediaCodec getmEncoder() {
		return mEncoder;
	}

	public void setmEncoder(MediaCodec mEncoder) {
		this.mEncoder = mEncoder;
	}

	public static int getVideoWidth() {
		return videoWidth;
	}

	public static void setVideoWidth(int videoWidth) {
		MediaCoderHelper.videoWidth = videoWidth;
	}

	public static int getVideoHeight() {
		return videoHeight;
	}

	public static void setVideoHeight(int videoHeight) {
		MediaCoderHelper.videoHeight = videoHeight;
	}

	public Surface getSurface() {
		return surface;
	}

	public void makeCurrent() {
		mInputSurface.makeCurrent();
		;
	}

	public void setupEXInput() {
		mInputSurface.eglSetup();
	}

	public void setSurface(Surface surface) {
		this.surface = surface;
	}

	public void swap() {

		mInputSurface.setPresentationTime(System.nanoTime());
		mInputSurface.swapBuffers();
		// restoreRenderState();
	}

	// private void restoreRenderState() {
	// // switch back to previous state
	// if (!EGL14.eglMakeCurrent(mSavedEglDisplay, mSavedEglDrawSurface,
	// mSavedEglReadSurface,
	// mSavedEglContext)) {
	// throw new RuntimeException("eglMakeCurrent failed");
	// }
	// System.arraycopy(mSavedMatrix, 0, mProjectionMatrix, 0,
	// mProjectionMatrix.length);
	// }

	public EGLSurface getEglSurface() {
		// if( null == eglSurface ){
		// eglSurface = EglHelper.getInstance().createSurface(surface);
		// }
		return eglSurface;
	}

	protected long getPTSUs() {
		long result = System.nanoTime() / 1000L;
		// presentationTimeUs should be monotonic
		// otherwise muxer fail to write
		if (result < mFakePts)
			result = (mFakePts - result) + result;
		NaviLogUtil.debugSendSuccess("pt sus time is :" + result);
		return result;
	}
}
