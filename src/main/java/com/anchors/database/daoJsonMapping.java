package com.anchors.database;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.project.common.PostgreSql;

public class daoJsonMapping {
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
//        String serviceName = "prod_prod";//
//        String serverType  = "server";
        
        String serviceName = "prod";//
        String serverType  = "dev";
        
        String table   = "process.asset";
        String column  = "description";
        
        
        String schemas = "";
        String[] split = table.split("\\.");
        schemas = split[0];
        table   = split[1];
        
        table = table.replaceAll(schemas+".", "");
        
        Connection conn = PostgreSql.Connection(serviceName, serverType,true);
                
        try {
        	daoJsonMapping process = new daoJsonMapping();
              process.Process(conn,schemas,table,column);
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
    public void Process(Connection conn,String schemas,String table,String column) {
//    	System.out.println("■ 컬럼생성");
    	
//    	System.out.println("컬럼수정\r\n");
    	
    	String query = getColumnsName(schemas,table,column);
    	System.out.println(query);
    	List<LinkedHashMap<String,String>> listData = PostgreSql.getSelect(conn,query);
    	
    	for(int index=0;index<listData.size();index++) {
    		LinkedHashMap<String,String> data = listData.get(index);
    		
    		String is_nullable = data.get("is_nullable");
    		String isNull = is_nullable.equals("NO")?"NOT NULL":"NULL";
    		String data_type = data.get("data_type");
    		if("character varying".equals(data_type) ) data_type = "varchar";
    		String character_maximum_length = data.get("character_maximum_length");
    		System.out.println(data.get("column_name")+"\t"+isNull+"\t"+data.get("data_type")+"\t"+data.get("character_maximum_length"));
    		
    		if("Y".equals(is_nullable))
    			System.out.println("ALTER TABLE "+schemas+"."+table+" ADD "+column+" "+data_type+"("+character_maximum_length+") NOT NULL;");
    		else
    			System.out.println("ALTER TABLE "+schemas+"."+table+" ADD "+column+" "+data_type+"("+character_maximum_length+");");	
    		
    	}
    	
    	
    }
    
    /**
     * TABLE_SCHEMA : process
     * TABLE_NAME   : service_request
     * @return
     */
    public String getColumnsName(String schemas, String table,String column) {
    	StringBuilder result = new StringBuilder();
    	
    	result.append(" SELECT column_name,is_nullable,data_type, character_maximum_length\r\n");
    	result.append("   FROM INFORMATION_SCHEMA.COLUMNS\r\n");
    	result.append("  WHERE 1=1\r\n");
    	result.append("    AND TABLE_SCHEMA = '"+schemas+"'\r\n");
    	result.append(" --    and TABLE_CATALOG = '데이터베이스명'\r\n");
    	result.append("    AND TABLE_NAME    = '"+table+"'\r\n");
    	if(column != null && !"".equals(column)) result.append("    AND COLUMN_NAME   = '"+column+"'\r\n");
    	result.append("  ORDER BY ORDINAL_POSITION;\r\n");
    	
    	return result.toString();
    }
}
