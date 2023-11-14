package com.anchors.database;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Temp {
    public String rootPath = "D:\\project\\anchors\\src\\main\\java\\com\\resource\\";
    
    
    public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
       
        Temp temp = new Temp();
        temp.Process();
        
        System.out.println("------------------------------ "+className+" ------------------- Ed.");
    }
    
    public void Process() {
    	System.out.println("Temp ....");
    	
    	String str = " response = ObjectUtils.isNotEmpty(resultDto) ? defaultMapper.map(resultDto, IFHMBSAPEAICDPP0003Payload.Response.class) : null; ";
    	
    	 Pattern pattern = Pattern.compile(".*Payload.*");
    	  Matcher matcher = pattern.matcher(str);
    	  if(matcher.find()){    // 정규식과 매칭되는 값이 있으면
//    	      return matcher.group(2).trim();        // 특정 단어 사이의 값을 추출한다
    		  System.out.println(matcher.group(0).trim());
    	  }
//    	  return null;
    	  
    	  String temp = "D:\\workspace\\cdpp-app\\src\\main\\java\\com\\hyundaimotors\\hmb\\cdppapp\\controller\\foundation";
    	  String repl = "resources\\com\\hyundaimotors\\hmb\\cdppapp\\mapper";
    	  
    			  
    			  
    }
}
