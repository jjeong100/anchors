package com.anchors.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.project.common.PostgreSql;

public class 테이블데이터존재컬럼 {
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
        
        String serviceName = "local";//
        String serverType  = "local";
        
//        String serviceName = "prod";//
//        String serverType  = "server";
        
        String table   = "process.account";
        String column  = "";
        String where   = "limit 1";
//        String where   = "where dealercode__c = '6'";
        
      
      String schemas = "";
      String[] split = table.split("\\.");
      schemas = split[0];
      table   = split[1];
      
      table = table.replaceAll(schemas+".", "");
      
      Connection conn = PostgreSql.Connection(serviceName, serverType,true);
                
        try {
        	테이블데이터존재컬럼 process = new 테이블데이터존재컬럼();
              process.Process(conn,schemas,table,where);
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
    public void Process(Connection conn,String schemas,String table,String where) {
//    
    	StringBuilder result = new StringBuilder();
//    	System.out.println(getAllColumnQuery( conn, schemas, table, where));
    	StringBuilder query = new StringBuilder();
    	
    	List<String> list = getAllColumnList( conn, schemas, table, where);
    	query.append(" select ");
    	
    	for(int index=0;index<list.size();index++) {
    		if(index != 0) query.append("\r\n     , "); 
    		query.append(list.get(index));
    	}
    	query.append("\r\n from "+schemas+"."+table);
    	query.append("\r\n " + where);
    	
//    	System.out.println(query);
    	List<LinkedHashMap<String,String>> listData = PostgreSql.getSelect(conn,query);//1건?
    	LinkedHashMap<String,String> data  = null;
    	if(listData.size() > 0) data = listData.get(0);
    	
    	if(data != null) {
	    	List<String> columns = new ArrayList<String>();
	    	for(int index=0;index<list.size();index++) {//컬럼 만큼 loop
	    		if(data.get(list.get(index)) != null) columns.add(list.get(index));
	    	}
	    	
	    	
	    	result.append(" select ");
	    	for(int index=0;index<columns.size();index++) {
	    		if(index != 0) result.append("\r\n     , ");
	    		result.append(columns.get(index));
	    	}
	    	result.append("\r\n from "+schemas+"."+table);
	    	result.append("\r\n "+where);
	    	
	    	System.out.println(result.toString());
    	}else {
    		System.err.println("데이터가 없습니다.");
    	}
    	
    }
    
    /**
     * 
     * @param conn
     * @param schemas
     * @param table
     * @param column
     * @return
     */
    public List<String> getAllColumnList(Connection conn,String schemas,String table,String where) {
    	List<String> result = new ArrayList<String>();
    	String query = getColumnsName(schemas,table);
    	List<LinkedHashMap<String,String>> listData = PostgreSql.getSelect(conn,query);
    	for(int index=0;index<listData.size();index++) {
    		LinkedHashMap<String,String> data = listData.get(index);
    		result.add(data.get("column_name"));
    	}
    	
    	return result;
    }
    
    /**
     * TABLE_SCHEMA : process
     * TABLE_NAME   : service_request
     * @return
     */
    public String getColumnsName(String schemas, String table) {
    	StringBuilder result = new StringBuilder();
    	
    	result.append(" SELECT column_name,is_nullable,data_type, character_maximum_length\r\n");
    	result.append("   FROM INFORMATION_SCHEMA.COLUMNS\r\n");
    	result.append("  WHERE 1=1\r\n");
    	result.append("    AND TABLE_SCHEMA = '"+schemas+"'\r\n");
    	result.append(" --    and TABLE_CATALOG = '데이터베이스명'\r\n");
    	result.append("    AND TABLE_NAME    = '"+table+"'\r\n");
    	result.append("  ORDER BY ORDINAL_POSITION;\r\n");
    	
    	return result.toString();
    }
}
