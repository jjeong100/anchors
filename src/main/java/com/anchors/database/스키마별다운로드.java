package com.anchors.database;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

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
    	String schemas = "process";
    	String tableName = "SMSSendLog";
    	
    	String query = getCreateTableScript(schemas,tableName);
    	
//    	System.out.println(query);
    	
    	List<LinkedHashMap<String,String>> listData = PostgreSql.getSelect(conn, query);
    	
    	LinkedHashMap<String,String> data = listData.get(0);
    	
    	System.out.println("-- Table: "+schemas+"."+tableName+"");
    	System.out.println();
    	System.out.println("-- DROP TABLE IF EXISTS landing.\""+tableName+"\";");
    	System.out.println();
    	System.out.println(data.get("string_agg"));
    	
        
    }
	/**
	 * 
	 * @return
	 */
	public String getTableLstQuery() {
		String query = "SELECT SCHEMANAME,TABLENAME FROM PG_TABLES WHERE SCHEMANAME NOT IN ('pg_catalog','information_schema')";
		
		return query;
	}
	
	
	public String getCreateTableScript(String schemas,String tableName) {
		StringBuilder result = new StringBuilder();
		
		result.append("SELECT string_agg(ddl_txt::text, E'\n')");
		result.append("FROM (");
		result.append(" 	(");
		result.append(" 		SELECT 'CREATE TABLE ' || '"+schemas+"."+tableName+"' || '(' ||");
		result.append(" 			string_agg(pa.attname || ' ' || pg_catalog.format_type(pa.atttypid, pa.atttypmod) ||");
		result.append(" 		        coalesce((SELECT ' DEFAULT '|| substring(pg_get_expr(paf.adbin, paf.adrelid) for 128)");
		result.append(" 		            FROM pg_attrdef paf ");
		result.append(" 		            WHERE paf.adrelid = pa.attrelid AND paf.adnum = pa.attnum AND pa.atthasdef), '')");
		result.append(" 				||");
		result.append(" 		        CASE WHEN pa.attnotnull = true THEN ");
		result.append(" 		            ' NOT NULL'");
		result.append(" 		        ELSE");
		result.append(" 		            '' END,E'\n, ') ");
		result.append("		        || ");
		result.append(" 		 		coalesce((SELECT E'\n, ' || 'CONSTRAINT' || ' ' || conindid::regclass::varchar || ' ' || pg_get_constraintdef(oid)"); 
		result.append("					FROM pg_constraint");
		result.append("					WHERE connamespace::regnamespace::varchar = '"+schemas+"'");
		result.append("					AND conrelid::regclass::varchar = '"+tableName+"'");
		result.append("					AND contype = 'p'), '')");
		result.append("		        || E'\n);' AS ddl_txt");
		result.append("		FROM pg_attribute pa");
		result.append(" 		JOIN pg_class pc ON pa.attrelid=pc.oid");
		result.append(" 		WHERE pc.relnamespace::regnamespace::varchar='"+schemas+"'");
		result.append("			AND pc.relname::varchar = '"+tableName+"' ");
		result.append("			AND pa.attnum > 0 AND NOT pa.attisdropped AND pc.relkind='r'");
		result.append("		GROUP BY pa.attrelid");
		result.append(" 	)");
		result.append(" 	UNION ALL");
		result.append(" 	(");
		result.append(" 		SELECT string_agg(indexdef || ';'  ::text, E'\n') AS ddl_txt");
		result.append("		FROM pg_indexes");
		result.append(" 		WHERE schemaname='"+schemas+"' AND tablename='"+tableName+"'");
		result.append(" 	)");
		result.append(") AS t");
		
		return result.toString();
	}
}
