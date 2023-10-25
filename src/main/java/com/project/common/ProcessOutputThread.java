package com.project.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/** ProessOutputTream class **/

public class ProcessOutputThread extends Thread {

    private InputStream is;
    private StringBuffer msg;
    
    public ProcessOutputThread(InputStream is, StringBuffer msg) {
        this.is = is;
        this.msg = msg;
    }
    
    public void run() {
        try {
        	String characterSet = "EUC-KR";
            msg.append(getStreamString(is,characterSet));
            
            System.out.println(msg.toString());
//            CharacterSet.getFindSet(msg.toString());
            
//            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
//            String str = buffer.readLine();
//            
//            System.out.println("입력값 : " + str);
//            buffer.close();

//	    
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally { 
	    	if (is != null) {try {is.close();} catch (IOException e) {e.printStackTrace();}}
	    }
    }
    
    private String getStreamString(InputStream is,String characterSet) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is,characterSet));
            StringBuffer out = new StringBuffer();
            String stdLine;
            while ((stdLine = reader.readLine()) != null) {
                out.append(stdLine);
            }
            return out.toString().trim();
       } catch (Exception e) {
          e.printStackTrace();
          return "";
       } finally {
    	   if (reader != null) { try { reader.close(); } catch (IOException e) { e.printStackTrace(); }}
       }
    }
}
