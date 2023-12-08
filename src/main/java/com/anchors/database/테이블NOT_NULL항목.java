package com.anchors.database;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.project.common.CommUtil;
import com.project.common.PostgreSql;

public class 테이블NOT_NULL항목 {
    public String rootPath = "D:\\project\\anchors\\src\\main\\java\\com\\resource\\";
    
    
    public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
//        String serviceName = "local";//
//        String serverType  = "local";
        
//        String serviceName = "prod";//
//        String serverType  = "dev";
        
        String serviceName = "prod";//
        String serverType  = "server";
        
        Connection conn = PostgreSql.Connection(serviceName, serverType,true);
                
        try {
            테이블NOT_NULL항목 process = new 테이블NOT_NULL항목();
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
    	getReplica(conn);
    }
    
    /**
     * 
     * @param conn
     */
    public void getLanding(Connection conn) {
    	StringBuilder result = new StringBuilder();
    	 /**
         * landing
         */
        String schema = "landing";
        List<String> lendingList = CommUtil.getFileReadListComent(rootPath+schema+".txt");
        for(int index=0;index<lendingList.size();index++) {
            System.out.println(lendingList.get(index));
            if(index != 0) result.append("\r\n");
            result.append(printColumnNotNullQuery(conn,schema,lendingList.get(index)));
        }
        
        CommUtil.writeFile(rootPath+"테이블NOT_NULL항목.txt", result);
    }
    
    
    /**
     * 
     * @param conn
     */
    public void getProcess(Connection conn) {
    	StringBuilder result = new StringBuilder();
    	 /**
         * landing
         */
        String schema = "process";
        List<String> lendingList = CommUtil.getFileReadListComent(rootPath+schema+".txt");
        for(int index=0;index<lendingList.size();index++) {
            System.out.println(lendingList.get(index));
            if(index != 0) result.append("\r\n");
            result.append(printColumnNotNullQuery(conn,schema,lendingList.get(index)));
        }
        
        CommUtil.writeFile(rootPath+"테이블NOT_NULL항목.txt", result);
    }
    
    /**
     * 
     * @param conn
     */
    public void getReplica(Connection conn) {
    	StringBuilder result = new StringBuilder();
    	 /**
         * landing
         */
        String schema = "replica";
        List<String> lendingList = CommUtil.getFileReadListComent(rootPath+schema+".txt");
        for(int index=0;index<lendingList.size();index++) {
            System.out.println(lendingList.get(index));
            if(index != 0) result.append("\r\n");
            result.append(printColumnNotNullQuery(conn,schema,lendingList.get(index)));
        }
        
        CommUtil.writeFile(rootPath+"테이블NOT_NULL항목.txt", result);
    }
    
    
    /**
     * 
     * @param conn
     * @param schema
     * @param table
     * @return
     */
    public String printColumnNotNullQuery(Connection conn, String schema, String table) {
        StringBuilder result = new StringBuilder();
        List<LinkedHashMap<String,String>> listData = PostgreSql.getSelect(conn, getColumnNotNullQuery(schema,table));
        
        for(int index=0;index<listData.size();index++) {
            LinkedHashMap<String,String> data = listData.get(index);
            
//            table_schema
//               , table_name 
//               , column_name 
//               , data_type 
//               , character_maximum_length
               
            String str = "";
            switch(data.get("data_type")) {
            case "integer":
            	str = data.get("table_schema") + "." + data.get("table_name") +"\t"+ data.get("column_name") + "\t" + data.get("data_type")+"\r\n";
            	result.append(str);
                System.out.println(str);
                break;
                default:
                	str = data.get("table_schema") + "." + data.get("table_name") +"\t"+ data.get("column_name") + "\t" + data.get("data_type") + "\t" + data.get("character_maximum_length")+"\r\n";
                	result.append(str);
                    System.out.println(str);
                    break;
            }
        }
        
        return result.toString();
    }
    
    /**
     * 
     * @param schema
     * @param table
     * @return
     */
    public String getColumnNotNullQuery(String schema, String table) {
        return getColumnInfoQuery(schema, table, "NO");
    }
    
    /**
     * 
     * @param schema
     * @param table
     * @return
     */
    public String getColumnNullQuery(String schema, String table) {
        return getColumnInfoQuery(schema, table, "YES");
    }
    
    /**
     * replica
     * synergy_action_audit__c
     * NO
     * @return
     */
    public String getColumnInfoQuery(String schema, String table, String nullable) {
        StringBuilder result = new StringBuilder();
        
        result.append(" select table_schema ");
        result.append("      , table_name ");
        result.append("      , column_name ");
        result.append("      , data_type ");
        result.append("      , character_maximum_length ");
        result.append(" from INFORMATION_SCHEMA.columns ");
        result.append(" where table_schema = '"+schema+"' ");
        result.append("  and table_name= '"+table+"' ");
        result.append("  and is_nullable = '"+nullable+"' ");
        result.append(" order by ordinal_position asc ");
    
        return result.toString();
    }
}
