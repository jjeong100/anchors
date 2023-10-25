package com.project.common;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class CharacterSet {

	/**
	 * 
	 * @param content
	 */
    public static void getFindSet(String content) {
        try {
            System.out.println("utf-8(1) : " + new String(content.getBytes("utf-8"), "euc-kr"));
            System.out.println("utf-8(2) : " + new String(content.getBytes("utf-8"), "ksc5601"));
            System.out.println("utf-8(3) : " + new String(content.getBytes("utf-8"), "x-windows-949"));
            System.out.println("utf-8(4) : " + new String(content.getBytes("utf-8"), "iso-8859-1"));
             
            System.out.println("iso-8859-1(1) : " + new String(content.getBytes("iso-8859-1"), "euc-kr"));
            System.out.println("iso-8859-1(2) : " + new String(content.getBytes("iso-8859-1"), "ksc5601"));
            System.out.println("iso-8859-1(3) : " + new String(content.getBytes("iso-8859-1"), "x-windows-949"));
            System.out.println("iso-8859-1(4) : " + new String(content.getBytes("iso-8859-1"), "utf-8"));
             
            System.out.println("euc-kr(1) : " + new String(content.getBytes("euc-kr"), "ksc5601"));
            System.out.println("euc-kr(2) : " + new String(content.getBytes("euc-kr"), "utf-8"));
            System.out.println("euc-kr(3) : " + new String(content.getBytes("euc-kr"), "x-windows-949"));
            System.out.println("euc-kr(4) : " + new String(content.getBytes("euc-kr"), "iso-8859-1"));
             
            System.out.println("ksc5601(1) : " + new String(content.getBytes("ksc5601"), "euc-kr"));
            System.out.println("ksc5601(2) : " + new String(content.getBytes("ksc5601"), "utf-8"));
            System.out.println("ksc5601(3) : " + new String(content.getBytes("ksc5601"), "x-windows-949"));
            System.out.println("ksc5601(4) : " + new String(content.getBytes("ksc5601"), "iso-8859-1"));
             
            System.out.println("x-windows-949(1) : " + new String(content.getBytes("x-windows-949"), "euc-kr"));
            System.out.println("x-windows-949(2) : " + new String(content.getBytes("x-windows-949"), "utf-8"));
            System.out.println("x-windows-949(3) : " + new String(content.getBytes("x-windows-949"), "ksc5601"));
            System.out.println("x-windows-949(4) : " + new String(content.getBytes("x-windows-949"), "iso-8859-1"));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
        
    /**
     * 
     */
    public static String setChartcter(String message) {
    	 
	      Charset charset = Charset.forName("UTF-8");
          CharsetDecoder decoder = charset.newDecoder();
          decoder.reset();
	      identify(message.getBytes(),decoder);
	      
	      return null;
    }
    
    /**
     * 
     */
    public static boolean identify ( byte[] bytes, CharsetDecoder decoder ) {
        try {
               decoder.decode(ByteBuffer.wrap(bytes));
                           // UTF-8 인코딩이 아닐경우 Exception 을 발생 시킨다
        }catch ( Exception e ){
               System.out.println("identify Error : " + e.getMessage());
               //System.out.println("리턴 false");
              return false;
        }
        //System.out.println("리턴 true");
       return true;
    
    } //identify()

}
