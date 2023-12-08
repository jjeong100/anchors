package com.anchors.database;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.project.common.CommUtil;
import com.project.common.PostgreSql;

public class 로컬쿼리실행 {
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
        String serviceName = "local";//
        String serverType  = "local";
        
        Connection conn = PostgreSql.Connection(serviceName, serverType,true);
                
        try {
        	로컬쿼리실행 process = new 로컬쿼리실행();
              process.Process(conn);
//            
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
             try{if(conn != null) {conn.close();}}catch(Exception e) {e.printStackTrace();} 
        }
        System.out.println("------------------------------ "+className+" ------------------- Ed.");
    }
	
	 /**
     * 
     */
    public void Process(Connection conn) {
    	
    }
    
    public String getQery(String path) {
    	StringBuilder result = new StringBuilder();
    	
    	List<String> list = CommUtil.getFileReadList(path);
    	
    	for(int index=0;index<list.size();index++) {
    		result.append(false);
    	}
    	
    	
    	return result.toString();
    }
}
