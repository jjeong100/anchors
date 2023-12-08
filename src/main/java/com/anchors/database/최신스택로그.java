package com.anchors.database;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.project.common.CommUtil;
import com.project.common.PostgreSql;

public class 최신스택로그 {
	public String if_id = "IF007";
	
	
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
//        String serviceName = "local";//
//        String serverType  = "local";
        
//        String serviceName = "dev_prod";//
//        String serverType  = "dev";
        
//        
        String serviceName = "prod";//
        String serverType  = "server";
        
        
        Connection conn = PostgreSql.Connection(serviceName, serverType,true);
                
        try {
        	최신스택로그 process = new 최신스택로그();
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
//    	
    	StringBuilder result = new StringBuilder();
    	List<LinkedHashMap<String,String>> listData = PostgreSql.getSelect(conn, getQuery());
    	for(int index=0;index<listData.size();index++) {
    		LinkedHashMap<String,String> data = listData.get(index);
    		System.out.println("■ id : "+data.get("if_id"));
    		result.append(data.get("if_err_msg"));
    	}
        
    	System.out.println("로그 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ st.");
    	System.out.println(result);
    	CommUtil.writeFile("D:\\project\\anchors\\src\\main\\java\\com\\resource\\최신스택로그", result, false);
    	System.out.println("로그 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ ed.");
    }
    
    /**
     * 
     * @return
     */
    public String getQuery() {
    	StringBuilder result = new StringBuilder();
    	
    	result.append(" SELECT if_id,if_err_msg FROM process.if_info_logs ");
    	if(if_id != null && !"".equals(if_id)) result.append(" WHERE if_id = '"+if_id+"' ");
    	result.append(" order by created_at desc ");
    	result.append(" limit 1 ");
    	return result.toString();
    }
}
