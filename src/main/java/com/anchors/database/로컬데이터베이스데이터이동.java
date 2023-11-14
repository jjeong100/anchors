package com.anchors.database;

import java.util.List;

import com.project.common.CommUtil;

public class 로컬데이터베이스데이터이동 {
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
        
        try {
        	로컬데이터베이스데이터이동 process = new 로컬데이터베이스데이터이동();
              process.Process();
//            
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
//             try{if(conn != null) {conn.close();}}catch(Exception e) {e.printStackTrace();} 
        }
        System.out.println("------------------------------ "+className+" ------------------- Ed.");
    }
	
	 /**
     * 
     */
    public void Process() {
        
        
    }
    
    public void namecheck(String name) {
    	System.out.println(name);
    	
    	
    }
    
}
