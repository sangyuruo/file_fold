package com.billionav.navi.uitools;


public class Base64 {
	
	private static Base64 m_sInstance = null;
	
	private final static byte[] encodingTable =
    {
        (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F', (byte)'G',
        (byte)'H', (byte)'I', (byte)'J', (byte)'K', (byte)'L', (byte)'M', (byte)'N',
        (byte)'O', (byte)'P', (byte)'Q', (byte)'R', (byte)'S', (byte)'T', (byte)'U',
        (byte)'V', (byte)'W', (byte)'X', (byte)'Y', (byte)'Z',
        (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f', (byte)'g',
        (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l', (byte)'m', (byte)'n',
        (byte)'o', (byte)'p', (byte)'q', (byte)'r', (byte)'s', (byte)'t', (byte)'u',
        (byte)'v', (byte)'w', (byte)'x', (byte)'y', (byte)'z',
        (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5', (byte)'6',
        (byte)'7', (byte)'8', (byte)'9',
        (byte)'+', (byte)'/'
    };

    private static byte    padding = (byte)'=';

	/*
	 * set up the decoding table.
	 */
    private final static byte[] decodingTable = new byte[128];
	
	private void initialiseDecodingTable()
	{
	    for (int i = 0; i < encodingTable.length; i++)
	    {
	        decodingTable[encodingTable[i]] = (byte)i;
	    }
	}

	private Base64() {
	    initialiseDecodingTable();
	}
	
	public static Base64 getInstance() {
		if (m_sInstance == null ) {
			m_sInstance = new Base64();
		}
		return m_sInstance;
	}
	
	 /**
     * encode the input data producing a base 64 output stream.
     *
     * @return the number of bytes produced.
     */
    public byte[] encode(byte[] data, int off, int length)
    {
        int modulus = length % 3;
        int dataLength = (length - modulus);
        int a1, a2, a3;
        
        byte[] temp = new byte[2048];
        int j = 0;
        
        for (int i = off; i < off + dataLength; i += 3)
        {
            a1 = data[i] & 0xff;
            a2 = data[i + 1] & 0xff;
            a3 = data[i + 2] & 0xff;
            temp[j] = encodingTable[(a1 >>> 2) & 0x3f]; 
            j++;
            temp[j] = encodingTable[((a1 << 4) | (a2 >>> 4)) & 0x3f];
            j++;
            temp[j] = encodingTable[((a2 << 2) | (a3 >>> 6)) & 0x3f];
            j++;
            temp[j] = encodingTable[a3 & 0x3f];
            j++;
            //Log.info(String.format("%x %x %x %x", temp[j-4],temp[j-3],temp[j-2],temp[j-1]));
            //out.write(encodingTable[(a1 >>> 2) & 0x3f]);
            //out.write(encodingTable[((a1 << 4) | (a2 >>> 4)) & 0x3f]);
           // out.write(encodingTable[((a2 << 2) | (a3 >>> 6)) & 0x3f]);
            //out.write(encodingTable[a3 & 0x3f]);
        }

        /*
         * process the tail end.
         */
        int    b1, b2, b3;
        int    d1, d2;

        switch (modulus)
        {
        case 0:        /* nothing left to do */
            break;
        case 1:
            d1 = data[off + dataLength] & 0xff;
            b1 = (d1 >>> 2) & 0x3f;
            b2 = (d1 << 4) & 0x3f;
            temp[j] = encodingTable[b1];
            j++;
            temp[j] = encodingTable[b2];
            j++;
            temp[j] = padding;
            j++;
            temp[j] = padding;
            j++;
            //out.write(encodingTable[b1]);
           // out.write(encodingTable[b2]);
           /// out.write(padding);
           // out.write(padding);
            //Log.info(String.format("%d %d %d %d", temp[j-4],temp[j-3],temp[j-2],temp[j-1]));
            break;
        case 2:
            d1 = data[off + dataLength] & 0xff;
            d2 = data[off + dataLength + 1] & 0xff;

            b1 = (d1 >>> 2) & 0x3f;
            b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;
            b3 = (d2 << 2) & 0x3f;
                        
            temp[j] = encodingTable[b1];
            j++;
            temp[j] = encodingTable[b2];
            j++;
            temp[j] = encodingTable[b3];
            j++;
            temp[j] = padding;
            j++;
            //out.write(encodingTable[b1]);
            //out.write(encodingTable[b2]);
            //out.write(encodingTable[b3]);
            //out.write(padding);
            //Log.info(String.format("%d %d %d %d", temp[j-4],temp[j-3],temp[j-2],temp[j-1]));
            break;
        }
        int len = (dataLength / 3) * 4 + ((modulus == 0) ? 0 : 4);
        byte[] output = new byte[len];
        for (int i = 0; i < len; i++) {
        	output[i] = temp[i];
        }
        return output;
    }

    private boolean ignore(char c) {
        return (c == '\n' || c =='\r' || c == '\t' || c == ' ');
    }
    
    /**
     * decode the base 64 encoded byte data writing it to the given output stream,
     * whitespace characters will be ignored.
     *
     * @return the number of bytes produced.
     */
    public byte[] decode( byte[] data, int off, int length )
    {
    	if (length == 0) {
    		return null;
    	}
        byte    b1, b2, b3, b4;
        int     outLen = 0;
        byte[] 	temp = new byte[2048];
        
        int     end = off + length;
        
        while (end > off)
        {
            if (!ignore((char)data[end - 1]))
            {
                break;
            }
            
            end--;
        }
        
        int  i = off;
        int  finish = end - 4;
        int  j = 0;
        
        i = nextI(data, i, finish);

        while (i < finish)
        {
            b1 = decodingTable[data[i++]];
            
            i = nextI(data, i, finish);
            
            b2 = decodingTable[data[i++]];
            
            i = nextI(data, i, finish);
            
            b3 = decodingTable[data[i++]];
            
            i = nextI(data, i, finish);
            
            b4 = decodingTable[data[i++]];
            
            temp[j] = (byte) ((b1 << 2) | (b2 >> 4));
            j++;
            temp[j] = (byte) ((b2 << 4) | (b3 >> 2));
            j++;
            temp[j] = (byte) ((b3 << 6) | b4);
            j++;

           // out.write((b1 << 2) | (b2 >> 4));
           // out.write((b2 << 4) | (b3 >> 2));
           // out.write((b3 << 6) | b4);
            outLen += 3;
            i = nextI(data, i, finish);
        }
        
        if (data[end - 2] == padding)
	    {
	        b1 = decodingTable[data[end - 4]];
	        b2 = decodingTable[data[end - 3]];
	
	       // out.write((b1 << 2) | (b2 >> 4));
	        temp[j] = (byte) ((byte) (b1 << 2) | (b2 >> 4));
	        outLen += 1;
	    }
	    else if (data[end - 1] == padding)
	    {
	        b1 = decodingTable[data[end - 4]];
	        b2 = decodingTable[data[end - 3]];
	        b3 = decodingTable[data[end - 2]];
	        temp[j] = (byte) ((b1 << 2) | (b2 >> 4));
            j++;
            temp[j] = (byte) ((b2 << 4) | (b3 >> 2));
	      //  out.write((b1 << 2) | (b2 >> 4));
	      //  out.write((b2 << 4) | (b3 >> 2));
	        
	        outLen += 2;
	    }
	    else
	    {
	        b1 = decodingTable[data[end - 4]];
	        b2 = decodingTable[data[end - 3]];
	        b3 = decodingTable[data[end - 2]];
	        b4 = decodingTable[data[end - 1]];
            temp[j] = (byte) ((b1 << 2) | (b2 >> 4));
            j++;
            temp[j] = (byte) ((b2 << 4) | (b3 >> 2));
            j++;
            temp[j] = (byte) ((b3 << 6) | b4);
	     //   out.write((b1 << 2) | (b2 >> 4));
	     //   out.write((b2 << 4) | (b3 >> 2));
	     //   out.write((b3 << 6) | b4);
	        
	        outLen += 3;
	    } 
        byte[] output = new byte[outLen];
        for (int index = 0; index < outLen; index++) {
        	output[index] = temp[index];
        }
        return output;
    }

    private int nextI(byte[] data, int i, int finish) {
        while ((i < finish) && ignore((char)data[i])) {
            i++;
        }
        return i;
    }

}
