package com.anchors.database;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.project.common.PostgreSql;

public class 인서트쿼리생성 {

	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
        String serviceName     = "anchors";//
        String serverType = "local";
        
        Connection conn = PostgreSql.Connection(serviceName, serverType);
                
        try {
        	인서트쿼리생성 process = new 인서트쿼리생성();
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
//    	List<LinkedHashMap<String,String>> listData = PostgreSql.getSelect(conn, query);
    	//PostgreSql.setSave(conn, insertStr(conn,str,tableName));
    	String schemas   = "process";
    	String tableName = "testdrivehistory__c";
    	
    	schemas = schemas.toLowerCase();
    	tableName = tableName.toLowerCase();
    	
    	List<LinkedHashMap<String,String>> listData = PostgreSql.getSelect(conn,getMakeQuery(schemas,tableName));
    	
    	StringBuilder result = new StringBuilder(); 
    	result.append(" update "+schemas+"."+tableName+" set ");
    	for(int index=0;index<listData.size();index++) {
    		LinkedHashMap<String,String> data = listData.get(index);
    		
    		// ", "+data.get("column_name").toLowerCase() + " = " 
    		
    	}
        
    }
    
    
	/**
	 * 
	 * @return
	 */
	public String getMakeQuery(String schemas,String tableName ) {
		StringBuilder result = new StringBuilder();
		
		result.append("select column_name, is_nullable from information_schema.columns");
		result.append(" where table_schema = 'process' ");
		result.append(" and table_name = 'testdrivehistory__c'");
		result.append(" order by DTD_IDENTIFIER::INT ASC");
		
		return result.toString();
	}
	
}
