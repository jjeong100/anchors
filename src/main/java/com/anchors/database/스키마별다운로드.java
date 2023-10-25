package com.anchors.database;

import java.sql.Connection;

import com.project.common.PostgreSql;

public class 스키마별다운로드 {
	
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
        String serviceName     = "anchors";//
        String serverType = "local";
        
        Connection conn = PostgreSql.Connection(serviceName, serverType);
                
        try {
        	스키마별다운로드 process = new 스키마별다운로드();
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
        
    }
	/**
	 * 
	 * @return
	 */
	public String getTableLstQuery() {
		String query = "SELECT SCHEMANAME,TABLENAME FROM PG_TABLES WHERE SCHEMANAME NOT IN ('pg_catalog','information_schema')";
		
		return query;
	}
}
