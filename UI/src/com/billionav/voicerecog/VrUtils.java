package com.billionav.voicerecog;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VrUtils {
	
	@SuppressWarnings({"unchecked"})
    public static <T> T uncheckedCast(Object obj) {
        return (T) obj;
    }
	
	public static boolean isSameFirstBit4(int a, int b) {
		while((a >> 4) > 0) {
			a >>= 4;
		}
		while((b >> 4) > 0) {
			b >>= 4;
		}
		return (a == b);
	}
	
	public static boolean writeWaveHeader(OutputStream output, int sampeRate, int sampleBits, short channel) {
		if (null == output) {
			return false;
		}
		
		try {
			// write the wav file per the wav file format
			output.write(stringToBytes("RIFF"));					// 00 - RIFF
			output.write(intToByteArray(0), 0, 4);					// 04 - how big is the rest of this file?
			output.write(stringToBytes("WAVE"));					// 08 - WAVE
			output.write(stringToBytes("fmt "));					// 12 - fmt 
			output.write(intToByteArray(16), 0, 4);					// 16 - size of this chunk
			output.write(shortToByteArray((short)1), 0, 2);			// 20 - what is the audio format? 1 for PCM = Pulse Code Modulation
			output.write(shortToByteArray((short)channel), 0, 2);	// 22 - mono or stereo? 1 or 2?  (or 5 or ???)
			output.write(intToByteArray(sampeRate), 0, 4);		// 24 - samples per second (numbers per second)
			output.write(intToByteArray(sampeRate*channel*sampleBits >> 3), 0, 4);		// 28 - bytes per second
			output.write(shortToByteArray((short)(channel*sampleBits >> 3)), 0, 2);	// 32 - # of bytes in one sample, for all channels
			output.write(shortToByteArray((short)sampleBits), 0, 2);// 34 - how many bits in a sample(number)?  usually 16 or 24
			output.write(stringToBytes("data"));					// 36 - data
			output.write(intToByteArray(0), 0, 4);					// 40 - how big is this data chunk
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean FillHeader(File file, int len) {
		try {
			RandomAccessFile randFile = new RandomAccessFile(file, "rw");
			
			randFile.seek(4);
			randFile.write(intToByteArray(44+len));
			randFile.seek(40);
			randFile.write(intToByteArray(len));
			
			randFile.close();
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	// these two routines convert a byte array to a unsigned short
	public static int byteArrayToInt(byte[] b) {
		int start = 0;
		int low = b[start] & 0xff;
		int high = b[start+1] & 0xff;
		return high << 8 | low ;
	}

	// these two routines convert a byte array to an unsigned integer
	public static long byteArrayToLong(byte[] b) {
		int start = 0;
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = b[i];
			cnt++;
		}
		
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return accum;
	}

	// returns a byte array of length 4
	private static byte[] intToByteArray(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) (i & 0x00FF);
		b[1] = (byte) ((i >> 8) & 0x000000FF);
		b[2] = (byte) ((i >> 16) & 0x000000FF);
		b[3] = (byte) ((i >> 24) & 0x000000FF);
		return b;
	}

	// convert a short to a byte array
	public static byte[] shortToByteArray(short data) {
		return new byte[]{(byte)(data & 0xff),(byte)((data >>> 8) & 0xff)};
	}
	
	public static byte[] stringToBytes(String str) throws IOException {
		byte[] bytes = new byte[str.length()];
		for (int index = 0; index < str.length(); index++) {
		    bytes[index] = (byte) str.charAt(index);
		}
		return bytes;
	}
	
	public static boolean isInteger(String str) {
		if (null == str || 0 == str.length()) {
			return false;
		}
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
    public static String hashSHA1(String text) { 
    	try {
	    	MessageDigest md = MessageDigest.getInstance("SHA-1");
		    byte[] data = text.getBytes("iso-8859-1");
		    md.update(data, 0, data.length);
		    byte[] sha1Hash = md.digest();
		    return convertToHex(sha1Hash);
    	}catch (UnsupportedEncodingException e) {
    		VrLog.e("VR", "Unsupported encoding");
    		return "";
    	}catch (NoSuchAlgorithmException e) {
    		VrLog.e("VR", "No such algorithm");
    		return "";
    	}
    }
    
    private static String convertToHex(byte[] data) { 
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int twoHalfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(twoHalfs++ < 1);
        } 
        return buf.toString();
    } 
}
