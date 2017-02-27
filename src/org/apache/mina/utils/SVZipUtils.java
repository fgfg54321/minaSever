package org.apache.mina.utils;


import java.io.ByteArrayInputStream;  
import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;
import java.io.InputStream;  
import java.io.OutputStream;  
import java.util.zip.GZIPInputStream;  
import java.util.zip.GZIPOutputStream;  
  
 
public abstract class SVZipUtils {  
  
	public enum CompressionMode
	{
		Compress(0),
		Decompress(1);
		
		private int value;
		CompressionMode(int v)
		{
			value = v;
		}
	}

    public static byte[] ConvertBytesGZip(byte[] data,CompressionMode mode) 
    {  
    	byte[] result = null;
    	try
		{
    		ByteArrayOutputStream bOut         = new ByteArrayOutputStream();
    		if(mode == CompressionMode.Compress)
    		{
    	        GZIPOutputStream gZipOutStream  = new GZIPOutputStream(bOut);
    	        gZipOutStream.write(data);  
    	        gZipOutStream.finish();  
    	        gZipOutStream.flush();  
    	        result                          = bOut.toByteArray();
    			bOut.close();
    		    gZipOutStream.close();  
    		}
    		else
    		{
    			ByteArrayInputStream bIn      = new ByteArrayInputStream(data);
    	        GZIPInputStream gZipInStream  = new GZIPInputStream(bIn);
    	        int len                       = gZipInStream.available();
    	        result                        = new byte[len];
    	        gZipInStream.read(result, 0, len);
    		}
		} 
    	catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return result;
    }  
  
  
}  
