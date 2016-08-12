package com.billionav.navi.app.ext;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLSurface;

import com.billionav.navi.app.ext.log.NaviLogUtil;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

public class EncoderService {
	static String TAG = "MCH";

	private MediaCodec.BufferInfo mBufferInfo;
	private MediaCodec mEncoder;
	// p7
	public static int bitRate = 4000000;
//	 public static int bitRate = 6000000; 
//	 public static int videoWidth = 1080;
//	 public static int videoHeight = 1701;
	// 4000000;

	public static int videoWidth = 800;
	public static int videoHeight = 480;
	public static int FRAME_RATE = 5;
	public static int IFRAME_INTERVAL = 1;
	public static String MIME_TYPE = "video/avc";

	private static boolean is420SP = false;
	Surface surface = null;
	EGLSurface eglSurface = null;

	private long mFakePts = 0;

	ByteBuffer[] encoderOutputBuffers = null;
	MediaCodec.BufferInfo info = null;
	static final int TIMEOUT_USEC = 10000;
	boolean loopFlag = false;
	boolean isStartLoop = false;
	static boolean VERBOSE = true;
	
	private EncoderThread mEncoderThread;
	
	static EncoderService instance = null;

	public static EncoderService getInstance() {
		if (null == instance) {
			instance = new EncoderService();
		}
		return instance;
	}
	
	private EncoderService() {
	}
	
	public Surface getSurface() {
		return surface;
	}
	
	public void releaseEncoder() {
		if (null != mEncoder) {
			loopFlag = false;
			isStartLoop = false;
			mEncoder.stop();
			mEncoder.release();
			mEncoder = null;
		}
	}
	public void prepareEncoder() {
		log("start prepareEncoder...");
		mBufferInfo = new MediaCodec.BufferInfo();

		MediaCodecInfo codecInfo = selectCodec(MIME_TYPE);
		if (codecInfo == null) {
			// Don't fail CTS if they don't have an AVC codec (not here,
			// anyway).
			log("codecInfo is null");
			return;
		} else {
			log("codecInfo is not null, name is " + codecInfo.getName());
		}
		// int colorFormat = selectColorFormat(codecInfo,
		// MIME_TYPE);
		CodecCapabilities codecCapabilities = codecInfo
				.getCapabilitiesForType(MIME_TYPE);
		if (null != codecCapabilities) {
			log("FEATURE_AdaptivePlayback: "
					+ codecCapabilities
							.isFeatureSupported(CodecCapabilities.FEATURE_AdaptivePlayback));
			// log("FEATURE_TunneledPlayback: " +
			// codecCapabilities.isFeatureSupported(
			// CodecCapabilities.FEATURE_TunneledPlayback));
			// log("FEATURE_SecurePlayback: " +
			// codecCapabilities.isFeatureSupported(
			// CodecCapabilities.FEATURE_SecurePlayback));
		}

		// byte[] pps = {0,0,0,1,104,-18,60,-128};
		// byte[] header_sps = { 0, 0, 0, 1, 103, 100, 0, 40, -84, 52, -59, 1,
		// -32, 17, 31, 120, 11, 80, 16, 16, 31, 0, 0, 3, 3, -23, 0, 0, -22, 96,
		// -108 };
		// byte[] header_pps = { 0, 0, 0, 1, 104, -18, 60, -128 };
		MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE,
				videoWidth, videoHeight);
		// format.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
		// format.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
		// format.setByteBuffer("csd-1", ByteBuffer.wrap(pps));
		format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
				MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
		// format.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
		format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
		format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
		// format.setInteger(MediaFormat.KEY_FRAME_RATE, null);
		format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);

		try {
			mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
			log("mEncoder name is " + mEncoder.getName());
			mEncoder.configure(format, null, null,
					MediaCodec.CONFIGURE_FLAG_ENCODE);
		} catch (Throwable ex) {
			log("mediacodec configure ex : " + ex.getMessage());
		}
		try {
			surface = mEncoder.createInputSurface();
			log("create inpute surface success.");
		} catch (Throwable ex) {
			log("create surface ex : " + ex.getMessage());
		}
		// mInputSurface = new EXInputSurface(surface);
		// mInputSurface = new CodecInputSurface(mEncoder.createInputSurface());
		mEncoder.start();
		// encoderOutputBuffers = mEncoder.getOutputBuffers();
		log("mEncoder start success.");
		
		mEncoderThread = new EncoderThread(mEncoder);
        mEncoderThread.start();
        mEncoderThread.waitUntilReady();
	}
	
	public static void log(String msg) {
		Log.i(TAG, msg);
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
	
	public void frameAvailableSoon() {
        Handler handler = mEncoderThread.getHandler();
        handler.sendMessage(handler.obtainMessage(
                EncoderThread.EncoderHandler.MSG_FRAME_AVAILABLE_SOON));
    }
	
	private static class EncoderThread extends Thread {
        private MediaCodec mEncoder;
        private MediaFormat mEncodedFormat;
        private MediaCodec.BufferInfo mBufferInfo;

        private EncoderHandler mHandler;
        private int mFrameNum;

        private final Object mLock = new Object();
        private volatile boolean mReady = false;

        public EncoderThread(MediaCodec mediaCodec ) {
            mEncoder = mediaCodec;
            mBufferInfo = new MediaCodec.BufferInfo();
        }

        /**
         * Thread entry point.
         * <p>
         * Prepares the Looper, Handler, and signals anybody watching that we're ready to go.
         */
        @Override
        public void run() {
            Looper.prepare();
            mHandler = new EncoderHandler(this);    // must create on encoder thread
            Log.d(TAG, "encoder thread ready");
            synchronized (mLock) {
                mReady = true;
                mLock.notify();    // signal waitUntilReady()
            }

            Looper.loop();

            synchronized (mLock) {
                mReady = false;
                mHandler = null;
            }
            Log.d(TAG, "looper quit");
        }

        /**
         * Waits until the encoder thread is ready to receive messages.
         * <p>
         * Call from non-encoder thread.
         */
        public void waitUntilReady() {
            synchronized (mLock) {
                while (!mReady) {
                    try {
                        mLock.wait();
                    } catch (InterruptedException ie) { /* not expected */ }
                }
            }
        }

        /**
         * Returns the Handler used to send messages to the encoder thread.
         */
        public EncoderHandler getHandler() {
            synchronized (mLock) {
                // Confirm ready state.
                if (!mReady) {
                    throw new RuntimeException("not ready");
                }
            }
            return mHandler;
        }

        /**
         * Drains all pending output from the decoder, and adds it to the circular buffer.
         */
        public void drainEncoder() {
            final int TIMEOUT_USEC = 0;     // no timeout -- check for buffers, bail if none

            ByteBuffer[] encoderOutputBuffers = mEncoder.getOutputBuffers();
            while (true) {
                int encoderStatus = mEncoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);
                if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    break;
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    // not expected for an encoder
                    encoderOutputBuffers = mEncoder.getOutputBuffers();
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    // Should happen before receiving buffers, and should only happen once.
                    // The MediaFormat contains the csd-0 and csd-1 keys, which we'll need
                    // for MediaMuxer.  It's unclear what else MediaMuxer might want, so
                    // rather than extract the codec-specific data and reconstruct a new
                    // MediaFormat later, we just grab it here and keep it around.
                    mEncodedFormat = mEncoder.getOutputFormat();
                    Log.d(TAG, "encoder output format changed: " + mEncodedFormat);
                } else if (encoderStatus < 0) {
                    Log.w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " +
                            encoderStatus);
                    // let's ignore it
                } else {
                    ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                    if (encodedData == null) {
                        throw new RuntimeException("encoderOutputBuffer " + encoderStatus +
                                " was null");
                    }

                    if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                        // The codec config data was pulled out when we got the
                        // INFO_OUTPUT_FORMAT_CHANGED status.  The MediaMuxer won't accept
                        // a single big blob -- it wants separate csd-0/csd-1 chunks --
                        // so simply saving this off won't work.
                        if (VERBOSE) Log.d(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                        mBufferInfo.size = 0;
                    }

                    if (mBufferInfo.size != 0) {
                        // adjust the ByteBuffer values to match BufferInfo (not needed?)
                        encodedData.position(mBufferInfo.offset);
                        encodedData.limit(mBufferInfo.offset + mBufferInfo.size);
                        byte[] dataToWrite = new byte[mBufferInfo.size];
                        encodedData.get( dataToWrite, mBufferInfo.offset, mBufferInfo.size);
                        log("mOutputStream write size : " + mBufferInfo.size);
//                        mEncBuffer.add(encodedData, mBufferInfo.flags,
//                                mBufferInfo.presentationTimeUs);

                        if (VERBOSE) {
                            Log.d(TAG, "sent " + mBufferInfo.size + " bytes to muxer, ts=" +
                                    mBufferInfo.presentationTimeUs);
                        }
                    }

                    mEncoder.releaseOutputBuffer(encoderStatus, false);

                    if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        Log.w(TAG, "reached end of stream unexpectedly");
                        break;      // out of while
                    }
                }
            }
        }

        /**
         * Drains the encoder output.
         * <p>
         * See notes for {@link CircularEncoder#frameAvailableSoon()}.
         */
        void frameAvailableSoon() {
            if (VERBOSE) Log.d(TAG, "frameAvailableSoon");
            drainEncoder();
            mFrameNum++;
        }

        /**
         * Tells the Looper to quit.
         */
        void shutdown() {
            if (VERBOSE) Log.d(TAG, "shutdown");
            Looper.myLooper().quit();
        }

        /**
         * Handler for EncoderThread.  Used for messages sent from the UI thread (or whatever
         * is driving the encoder) to the encoder thread.
         * <p>
         * The object is created on the encoder thread.
         */
        private static class EncoderHandler extends Handler {
            public static final int MSG_FRAME_AVAILABLE_SOON = 1;
            public static final int MSG_SAVE_VIDEO = 2;
            public static final int MSG_SHUTDOWN = 3;

            // This shouldn't need to be a weak ref, since we'll go away when the Looper quits,
            // but no real harm in it.
            private WeakReference<EncoderThread> mWeakEncoderThread;

            /**
             * Constructor.  Instantiate object from encoder thread.
             */
            public EncoderHandler(EncoderThread et) {
                mWeakEncoderThread = new WeakReference<EncoderThread>(et);
            }

            @Override  // runs on encoder thread
            public void handleMessage(Message msg) {
                int what = msg.what;
                Log.d(TAG, "EncoderHandler: what=" + what);

                EncoderThread encoderThread = mWeakEncoderThread.get();
                if (encoderThread == null) {
                    Log.w(TAG, "EncoderHandler.handleMessage: weak ref is null");
                    return;
                }

                switch (what) {
                    case MSG_FRAME_AVAILABLE_SOON:
                        encoderThread.frameAvailableSoon();
                        break;
                    case MSG_SHUTDOWN:
                        encoderThread.shutdown();
                        break;
                    default:
                        throw new RuntimeException("unknown message " + what);
                }
            }
        }
	}
}
