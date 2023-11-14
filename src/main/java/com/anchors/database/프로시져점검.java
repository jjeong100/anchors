package com.anchors.database;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.project.common.CommUtil;
import com.project.common.PostgreSql;

public class 프로시져점검 {
    public String rootPath = "D:\\project\\anchors\\src\\main\\java\\com\\resource\\";
    
    
    public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
        String serviceName = "local";//
        String serverType  = "local";
        
//        String serviceName = "dev_prod";//
//        String serverType  = "dev";
        
//        String serviceName = "prod_prod";//
//        String serverType  = "server";
        
        Connection conn = PostgreSql.Connection(serviceName, serverType,false,serviceName);
                
        try {
            프로시져점검 process = new 프로시져점검();
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
    	
        /**
         * landing
         */
//    	getLanding(conn);
    	/**
    	 * process
    	 */
//    	getProcess(conn);
    	
    	/**
    	 * replica
    	 */
//    	getReplica(conn);
    }
    
    
    
   
}
