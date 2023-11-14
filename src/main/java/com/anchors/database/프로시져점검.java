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
        	String schema    = "replica";
//        	String procedure = "if_in_channel_partner_inf_wf";
        	String procedure = "if_product_in_wf";
//        	String param     = "{proc_acc_id,proc_con_id,checkcu}";
        	String param     = "{param_id}";
        	
        	
            프로시져점검 process = new 프로시져점검();
            process.Process(conn,schema,procedure,param);
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
     * @param conn
     * @param procedure
     * @param param
     * @return
     */
    public String Process(Connection conn,String procedure,String param) {
    	String[] split = procedure.split("\\.");
    	
    	System.out.println("■schema : " + split[0]);
    	System.out.println("■procedure : " + split[1]);
    	System.out.println("■param : " + param);
    	
    	return Process( conn, split[0], split[1], param);
    }
    
     /**
     * 
     */
    public String Process(Connection conn,String schema,String procedure,String param) {
    	StringBuilder result = new StringBuilder();
    	String query = getProcedure(schema,procedure,param);
    	System.out.println(query);
    	List<LinkedHashMap<String,String>> listData = PostgreSql.getSelect(conn, query);
    	
    	for(int index=0;index<listData.size();index++) {
    		LinkedHashMap<String,String> data = listData.get(index);
    		System.out.println(data.get("prosrc"));
    		result.append(data.get("prosrc"));
    	}
    	
    	return result.toString();
    }
    
    /**
     * select pg.prosrc, pg.proargnames
    	from pg_catalog.pg_proc pg
    	where 1=1
    	and pg.pronamespace = (select relnamespace from pg_catalog.pg_class
    	                     where relnamespace::regnamespace::varchar='replica'
    	                    limit 1)
    	and pg.proname = 'if_in_channel_partner_inf_wf'
    	and pg.proargnames = '{proc_acc_id,proc_con_id,checkcu}'
     * @return
     */
    public String getProcedure(String schema,String procedure,String param) {
    	StringBuilder result = new StringBuilder();
    	
    	result.append(" select pg.prosrc \r\n");
    	result.append(" from pg_catalog.pg_proc pg \r\n");
    	result.append(" where 1=1 \r\n");
    	result.append(" and pg.pronamespace = (select relnamespace from pg_catalog.pg_class \r\n");
    	result.append("                         where relnamespace::regnamespace::varchar='"+schema+"' \r\n");
    	result.append("                         limit 1) ");
    	result.append(" and pg.proname = '"+procedure+"' \r\n");
    	result.append(" and pg.proargnames = '"+param+"' \r\n");
    	
    	return result.toString();
    }
    
    
   
}
