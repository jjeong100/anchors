package com.anchors.database;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.project.common.CommUtil;

public class 스택로그의쿼리정리 {
	
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");

        String path = "D:\\project\\anchors\\src\\main\\java\\com\\resource\\최신스택로그";
        
        try {
        	스택로그의쿼리정리 process = new 스택로그의쿼리정리();
              process.Process(path);
//            
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
        }
        System.out.println("------------------------------ "+className+" ------------------- Ed.");
    }
	
	 /**
     * 
     */
    public void Process(String path) {
    	StringBuilder result = new StringBuilder();
    	List<String> list = CommUtil.getFileReadList(path);
    	for(int index=0;index<list.size();index++) {
    		String data = list.get(index);
    		System.out.println(data);
    		result.append(data+"\r\n");
    	}
    	
    	String[] return_carrage = {" ,","select ","from ","where ","join ","on ","or "};
    	for(int index=0;index<list.size();index++) {
    		String data = list.get(index);
    		
    		if(data.indexOf("### SQL:") != -1) {
    			for(String rn : return_carrage) {
    				data = data.replaceAll(rn, "\r\n"+rn);
    			}
    			
    			System.out.println(data);
    			result.append(data);
    		}
    		if(data.indexOf("ERROR:") != -1) {
    			System.out.println(data);
    			result.append("\r\n"+data);
    		}
    	}
    	
    	CommUtil.writeFile("D:\\project\\anchors\\src\\main\\java\\com\\resource\\스택로그의쿼리정리", result, false);
    }
    
   
}
