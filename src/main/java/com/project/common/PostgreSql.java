package com.project.common;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

 
public class PostgreSql {
 
     public static String xmlFile = "D:\\workspace\\Project\\sample\\src\\main\\java\\com\\project\\common\\postgresql_query.xml";
//    public Connection con = null;
//    public Statement  st  = null;
//    public ResultSet  rs  = null;
    
//    public static void main(String[] args) {
// 
//        Connection con = null;
//        Statement st = null;
//        ResultSet rs = null;
// 
//    
//        String url      = "jdbc:postgresql://127.0.0.1:5432/KD_MES";
//        String user     = "rntime";
//        String password = "rntime8630";
// 
//        try {
//            con = DriverManager.getConnection(url, user, password);
//            st = con.createStatement();
//            rs = st.executeQuery("SELECT VERSION()");
// 
//            if (rs.next()) {
//                System.out.println(rs.getString(1));
//            }
// 
//        } catch (SQLException ex) {
//            Logger lgr = Logger.getLogger(PostgreSql.class.getName());
//            lgr.log(Level.SEVERE, ex.getMessage(), ex);
// 
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (st != null) {
//                    st.close();
//                }
//                if (con != null) {
//                    con.close();
//                }
// 
//            } catch (SQLException ex) {
//                Logger lgr = Logger.getLogger(PostgreSql.class.getName());
//                lgr.log(Level.WARNING, ex.getMessage(), ex);
//            }
//        }
//    }
    
    /**
     * 
     */
    public static Connection Connection(String dbName,String serverType) {
        String urlName = "globals."+serverType+".database."+dbName+".url";
        
        return Connection(urlName);
    }
    public static Connection Connection(String urlName) {
         Logger logger = Logger.getLogger(PostgreSql.class.getName());
         Connection conn = null;
         
          try {
              if(GlobalProperties.getProperties(urlName) != null && !"".equals(GlobalProperties.getProperties(urlName))) {
                  String url      = GlobalProperties.getProperties(urlName);
                  String user     = GlobalProperties.getProperties("globals.database.username");
                  String password = GlobalProperties.getProperties("globals.database.password");
                  conn = DriverManager.getConnection(url, user, password);
                  if(conn != null) {
                      StringBuilder log = new StringBuilder();
                      
                      log.append("\r\n▶접속 URL   : "+url);
                      log.append("\r\n▶접속 아이디   : "+user);
                      log.append("\r\n▶접속 페스워드 : "+password);
                      
                      logger.info((new Date())+" : "+log.toString());
                  }
              }else {
                  logger.info((new Date())+" : globlas.properties 확인 필요..");
              }
          } catch (SQLException ex) {
              logger.log(Level.SEVERE, ex.getMessage(), ex);
          } 
//          finally {
//              try {if (conn != null) {conn.close();}} catch (SQLException ex) {Logger lgr = Logger.getLogger(PostgreSql.class.getName());lgr.log(Level.WARNING, ex.getMessage(), ex);}
//          }
          
         return conn;
    }
    
    /**
     * 
     */
    public static void connectionClose(Connection conn) {
        Logger logger = Logger.getLogger(PostgreSql.class.getName());
        try {
            logger.info(new Date()+" 접속 디비(해제) : "+conn.getCatalog());
            
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
    }
    
    public static Map<String,String> getSelectOne(Connection conn, StringBuilder query){
        return getSelectOne(conn, query.toString());
    }
    
    /**
     * 결과 값 1건조회
     * @param conn
     * @param query
     * @return
     */
    public static Map<String,String> getSelectOne(Connection conn, String query){
        Map<String,String> result = new HashMap<String,String>();
         
        Statement st = null;
        ResultSet rs = null;
         
         try {
              st = conn.createStatement();
              rs = st.executeQuery(query);
              
              ResultSetMetaData rsmd = rs.getMetaData();
              int columnCount = rsmd.getColumnCount();
              
              while (rs.next()) {
                 for(int index=0;index<columnCount;index++) {
                     String key = rsmd.getColumnName((index+1));
                     String value = rs.getString(key);
                     result.put(key, value);
                 }
              }
          } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(PostgreSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs  != null) {rs.close();}
                if (st  != null) {st.close();}
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(PostgreSql.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
         return result;
     }
    
    
    /**
     * 중복여부
     * 중복시 true
     * @param conn
     * @param query
     * @return
     */
    public static boolean getSelectDue(Connection conn, StringBuilder query){
        return getSelectDue(conn, query.toString());
    }
    
    public static boolean getSelectDue(Connection conn, String query){
        boolean result = false;
         
        Statement st = null;
        ResultSet rs = null;
         
         try {
              st = conn.createStatement();
              rs = st.executeQuery(query);
              
              while (rs.next()) {
                  String value = rs.getString(1);
                  if(value != null && !"".equals(value)) {
                      if(Integer.parseInt(value) > 0) result = true;
                  }
              }
          } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(PostgreSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs  != null) {rs.close();}
                if (st  != null) {st.close();}
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(PostgreSql.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
         return result;
     }
    
    /**
     * 조회
     * @param conn
     * @param query
     * @return
     */
    public static List<LinkedHashMap<String,String>> getSelect(Connection conn, StringBuilder query){
        return getSelect(conn, query.toString());
    }
    /**
     * 조회
     */
    public static List<LinkedHashMap<String,String>> getSelect(Connection conn, String query){
       List<LinkedHashMap<String,String>> result = new ArrayList<LinkedHashMap<String,String>>();
        
       Statement st = null;
       ResultSet rs = null;
        
        try {
             st = conn.createStatement();
             rs = st.executeQuery(query);
             
             ResultSetMetaData rsmd = rs.getMetaData();
             int columnCount = rsmd.getColumnCount();
             
//             System.out.println(rs.get)
             while (rs.next()) {
                 LinkedHashMap<String,String> data = new LinkedHashMap<String,String>();
                for(int index=0;index<columnCount;index++) {
                    String key = rsmd.getColumnName((index+1));
                    String value = rs.getString(key);
                    data.put(key, value);
                }
                
                result.add(data);
             }
//             System.out.println(result.size());
         } catch (SQLException ex) {
           Logger lgr = Logger.getLogger(PostgreSql.class.getName());
           lgr.log(Level.SEVERE, ex.getMessage(), ex);

       } finally {
           try {
               if (rs  != null) {rs.close();}
               if (st  != null) {st.close();}
           } catch (SQLException ex) {
               Logger lgr = Logger.getLogger(PostgreSql.class.getName());
               lgr.log(Level.WARNING, ex.getMessage(), ex);
           }
       }
        return result;
    }
    
    
    /**
     * 삭제
     */
    public static int setDelete(Connection conn, StringBuilder query){
        return setSave( conn,  query.toString());
    }
    
    /**
     * 삭제
     */
    public static int setDelete(Connection conn, String query){
        return setSave( conn,  query.toString());
    }
    
    /**
     * 저장
     */
    public static int setSave(Connection conn, StringBuilder query){
        return setSave( conn,  query.toString());
    }
    /**
     * 저장
     */
    public static int setSave(Connection conn, String query){
        
       Statement st = null;
       int rs = 0;
        
        try {
             st = conn.createStatement();
             rs = st.executeUpdate(query);
             
//             ResultSetMetaData rsmd = rs.getMetaData();
//             int columnCount = rsmd.getColumnCount();
             
         } catch (SQLException ex) {
           Logger lgr = Logger.getLogger(PostgreSql.class.getName());
           String exception_msg = ex.getMessage();
           lgr.log(Level.SEVERE, exception_msg, ex);
           
           if(exception_msg.indexOf("duplicate key value") != -1) {
               System.out.println("데이타 중복");
           }
           
           

       } finally {
           try {
               if (st  != null) {st.close();}
           } catch (SQLException ex) {
               Logger lgr = Logger.getLogger(PostgreSql.class.getName());
               lgr.log(Level.WARNING, ex.getMessage(), ex);
           }
       }
        
       return rs;
    }
    
    
    /**
     * 
     * @param conn
     * @param tableName
     * @param isUpper
     * @return
     */
    public static List<String[]> getTableTypeColumns(Connection conn, String tableName) {
    	 List<String[]> result = new ArrayList<String[]>();
    	StringBuilder query = new StringBuilder();
    	query.append(" SELECT COLUMN_NAME ");
    	query.append(" , DATA_TYPE ");
    	query.append(" , CASE WHEN DATA_TYPE = 'numeric' THEN NUMERIC_PRECISION || ',' || NUMERIC_SCALE ");
    	query.append("   WHEN DATA_TYPE IN ('text','timestamp without time zone') THEN ''");
    	query.append(" ELSE CHARACTER_MAXIMUM_LENGTH || '' END AS DATA_LEN");
    	query.append(" FROM INFORMATION_SCHEMA.columns WHERE TABLE_NAME = '"+tableName.toLowerCase()+"'");
    	
//    	System.out.println(query.toString());
    
    	List<LinkedHashMap<String,String>> data = PostgreSql.getSelect(conn,query);
	    for(int index=0;index<data.size();index++) {
            LinkedHashMap<String,String> map = data.get(index);
            
            String type = getDateType(map.get("data_type"))+"("+map.get("data_len")+")";
            String[] array = {map.get("column_name"),type};
            
            result.add(array);
        }
           
        return result;
    }
    
    /**
     * 
     * @param value
     * @return
     */
    public static String getDateType(String value) {
    	String result = value;
    	switch(value) {
    	case "character varying":
    		result = "VARCHAR";
    		break;
    	case "text":
    		result = "TEXT";
    		break;
		case "numeric":
			result = "NUMERIC";
    		break;
		case "timestamp without time zone":
			result = "TIMESTAMP";
    		break;
    	}
    	
    	return result;
    }
 
    /**
     * 
     * @param conn
     * @param tableName
     * @param isUpper
     * @return
     */
    public static List<String> getTableColumns(Connection conn, String tableName) {
        return getTableColumns( conn,  tableName, false);
    }
    
    /**
     * 
     * @param conn
     * @param tableName
     * @param isUpper
     * @return
     */
    public static List<String> getTableColumns(Connection conn, String tableName,boolean isUpper) {
        List<String> result = new ArrayList<String>();
        tableName = tableName != null?tableName.toLowerCase():"";
        String query = "SELECT * FROM INFORMATION_SCHEMA.columns WHERE TABLE_NAME = '"+tableName+"'  ORDER BY ORDINAL_POSITION ASC";
//        List<Hshmap> 
        List<LinkedHashMap<String,String>> data = PostgreSql.getSelect(conn,query);
        
        for(int index=0;index<data.size();index++) {
            LinkedHashMap<String,String> map = data.get(index);
            
            if(isUpper) result.add(map.get("column_name").toUpperCase());
            else result.add(map.get("column_name"));

        }
        
        return result;
    }
    
    public static List<String> getTableKey(Connection conn, String tableName) {
        List<String> result = new ArrayList<String>();

        tableName = tableName != null?tableName.toLowerCase():"";
        StringBuilder query = new StringBuilder();
        
        query.append(" SELECT CC.COLUMN_NAME AS COLUMN_NAME ");query.append("\r\n");
        query.append(" FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS       TC ");query.append("\r\n");
        query.append("     ,INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE CC ");query.append("\r\n");
        query.append(" WHERE 1=1 ");query.append("\r\n");
        query.append(" --    AND TC.TABLE_CATALOG   = '데이터베이스명' ");query.append("\r\n");
        query.append("  AND TC.TABLE_NAME      = '"+tableName+"' ");query.append("\r\n");
        query.append("  AND TC.CONSTRAINT_TYPE = 'PRIMARY KEY' ");query.append("\r\n");
        query.append("  AND TC.TABLE_CATALOG   = CC.TABLE_CATALOG ");query.append("\r\n");
        query.append("  AND TC.TABLE_SCHEMA    = CC.TABLE_SCHEMA ");query.append("\r\n");
        query.append("  AND TC.TABLE_NAME      = CC.TABLE_NAME ");query.append("\r\n");
        query.append("   AND TC.CONSTRAINT_NAME = CC.CONSTRAINT_NAME ");query.append("\r\n");
        
        System.out.println(query.toString());
        
//        List<Hshmap> 
        List<LinkedHashMap<String,String>> data = PostgreSql.getSelect(conn,query);
        
        for(int index=0;index<data.size();index++) {
            LinkedHashMap<String,String> map = data.get(index);
            
            result.add(map.get("column_name"));

        }
        
        return result;
    }
    
    /**
     * default tab 간격
     * columns 코멘트 출력
     */
    public static String getComments(Connection conn,String tableName, String column) {
           tableName = tableName != null?tableName.toLowerCase():"";
           StringBuilder query = new StringBuilder();
           query.append(" SELECT 'COMMENT ON COLUMN ' || UPPER(C.RELNAME) || '.' || UPPER(A.ATTNAME) || '    IS ' || '''' || (SELECT COL_DESCRIPTION(A.ATTRELID, A.ATTNUM)) || '''' || ';' AS COMMENT ");
           query.append(" FROM ");
		   query.append(" PG_CATALOG.PG_CLASS C ");
		   query.append(" INNER JOIN PG_CATALOG.PG_ATTRIBUTE A ");
		   query.append(" ON A.ATTRELID = C.OID ");
		   query.append(" WHERE 1=1 ");
		   query.append("   AND A.ATTNUM > 0 ");
		   query.append("   AND A.ATTISDROPPED IS FALSE ");
		   query.append("   AND PG_CATALOG.PG_TABLE_IS_VISIBLE(C.OID) ");
		   query.append("   AND C.RELNAME = '"+tableName+"' ");
		   query.append("   AND A.ATTNAME = '"+column+"' ");
		   query.append("   ORDER BY A.ATTRELID, A.ATTNUM ");

//           System.out.println(query.toString());
           
//           List<Hshmap> 
           Map<String,String> data = PostgreSql.getSelectOne(conn, query);
           return data.get("comment");
    }
    
    /**
     * 
     * @param conn
     * @param findCommdents
     * @return
     */
    public static List<String> getColumnsComments(Connection conn,String findCommdents) {
        String tableName = null;
        String token     = null;
        return getColumnsComments( conn, tableName,  findCommdents,  token);
    }
    
    public static List<String> getColumnsComments(Connection conn,String tableName, String findCommdents) {
        String token     = null;
        return getColumnsComments( conn, tableName,  findCommdents,  token);
    }
    
    
    /**
     * default tab 간격
     * columns 코멘트 출력
     */
    public static List<String> getColumnsComments(Connection conn,String tableName, String findCommdents, String token) {
           List<String> result = new ArrayList<String>();

           tableName = tableName != null?tableName.toLowerCase():"";
           StringBuilder query = new StringBuilder();
           
           query.append(" SELECT COMMENT,COLNAME,DATA_TYPE,DATA_LEN FROM ( ");query.append("\r\n");
           query.append(" SELECT COLNAME,COMMENT,DATA_TYPE,DATA_LEN FROM");query.append("\r\n");
           query.append(" (SELECT C.RELNAME");query.append("\r\n");
           query.append("      , A.ATTRELID AS TABLEOID");query.append("\r\n");
           query.append("      , A.ATTNAME  AS COLNAME");query.append("\r\n");
           query.append("      , A.ATTNUM   AS COLUMNOID");query.append("\r\n");
           query.append("      , (SELECT COL_DESCRIPTION(A.ATTRELID, A.ATTNUM)) AS COMMENT");query.append("\r\n");
           
           query.append("      , (SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = C.RELNAME AND COLUMN_NAME = A.ATTNAME) AS DATA_TYPE ");
           query.append("      , (SELECT CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = C.RELNAME AND COLUMN_NAME = A.ATTNAME) AS DATA_LEN ");
           
           query.append(" FROM");query.append("\r\n");
           query.append("    PG_CATALOG.PG_CLASS C");query.append("\r\n");
           query.append("    INNER JOIN PG_CATALOG.PG_ATTRIBUTE A ON A.ATTRELID = C.OID");query.append("\r\n");
           query.append(" WHERE 1=1");query.append("\r\n");
           if(tableName != null && !"".equals(tableName))  query.append("    AND C.RELNAME = '"+tableName+"'");query.append("\r\n");
           query.append("    AND A.ATTNUM > 0");query.append("\r\n");
           query.append("    AND A.ATTISDROPPED IS FALSE");query.append("\r\n");
           query.append("    AND PG_CATALOG.PG_TABLE_IS_VISIBLE(C.OID)");query.append("\r\n");
           query.append(" ORDER BY A.ATTRELID, A.ATTNUM) A");query.append("\r\n");
           if(findCommdents != null && !"".equals(findCommdents)) query.append(" WHERE REGEXP_REPLACE(COMMENT, ' +', '', 'g') LIKE '%"+findCommdents+"%'");query.append("\r\n");
           query.append(" ) A");query.append("\r\n");
           query.append(" GROUP BY COLNAME,COMMENT,DATA_TYPE,DATA_LEN ");

           
           System.out.println(query.toString());
           
//           List<Hshmap> 
           List<LinkedHashMap<String,String>> data = PostgreSql.getSelect(conn,query);
           
           
           for(int index=0;index<data.size();index++) {
               LinkedHashMap<String,String> map = data.get(index);
               String data_type = PostgreSqlToJavaDataType(map.get("data_type"));
               
               if(token == null) token = " ";
               
               if("VARCHAR".equals(data_type)) {
                   result.add(map.get("colname").toUpperCase()+token+data_type+"("+map.get("data_len")+")    --"+map.get("comment"));
               }else {
                   result.add(map.get("colname").toUpperCase()+token+data_type+"    --"+map.get("comment"));
               }
           }
           
           return result;
    }
    
    /**
     * default tab 간격
     * columns 코멘트 출력
     */
    public static List<String> getColumnsCommentsExcel(Connection conn,String findCommdents) {
           List<String> result = new ArrayList<String>();

           StringBuilder query = new StringBuilder();
           
           query.append(" SELECT COLNAME,COMMENT,DATA_TYPE,DATA_LEN FROM ( ");query.append("\r\n");
           query.append(" SELECT COLNAME,COMMENT,DATA_TYPE,DATA_LEN FROM");query.append("\r\n");
           query.append(" (SELECT C.RELNAME");query.append("\r\n");
           query.append("      , A.ATTRELID AS TABLEOID");query.append("\r\n");
           query.append("      , A.ATTNAME  AS COLNAME");query.append("\r\n");
           query.append("      , A.ATTNUM   AS COLUMNOID");query.append("\r\n");
           query.append("      , (SELECT COL_DESCRIPTION(A.ATTRELID, A.ATTNUM)) AS COMMENT");query.append("\r\n");
           
           query.append("      , (SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = C.RELNAME AND COLUMN_NAME = A.ATTNAME) AS DATA_TYPE ");
           query.append("      , (SELECT CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = C.RELNAME AND COLUMN_NAME = A.ATTNAME) AS DATA_LEN ");
           
           query.append(" FROM");query.append("\r\n");
           query.append("    PG_CATALOG.PG_CLASS C");query.append("\r\n");
           query.append("    INNER JOIN PG_CATALOG.PG_ATTRIBUTE A ON A.ATTRELID = C.OID");query.append("\r\n");
           query.append(" WHERE 1=1");query.append("\r\n");
           query.append("    AND A.ATTNUM > 0");query.append("\r\n");
           query.append("    AND A.ATTISDROPPED IS FALSE");query.append("\r\n");
           query.append("    AND PG_CATALOG.PG_TABLE_IS_VISIBLE(C.OID)");query.append("\r\n");
           query.append(" ORDER BY A.ATTRELID, A.ATTNUM) A");query.append("\r\n");
           query.append(" WHERE REGEXP_REPLACE(COMMENT, ' +', '', 'g') LIKE '%"+findCommdents+"%'");query.append("\r\n");
           query.append(" ) A");query.append("\r\n");
           query.append(" GROUP BY COLNAME,COMMENT,DATA_TYPE,DATA_LEN ");

           
           System.out.println(query.toString());
           
//           List<Hshmap> 
           List<LinkedHashMap<String,String>> data = PostgreSql.getSelect(conn,query);
           
           
           for(int index=0;index<data.size();index++) {
               LinkedHashMap<String,String> map = data.get(index);
               String data_type = PostgreSqlToJavaDataType(map.get("data_type"));
               String comment = map.get("comment");
               String colname = map.get("colname").toUpperCase();
               String colType = data_type.toUpperCase().replace("NUMERIC", "NUMBER").replace("VARCHAR", "VARCHAR2");
               String data_len = map.get("data_len")==null?"":map.get("data_len");
               
               
               result.add(comment+"\t"+colname+"\t"+colType+"\t"+data_len);
           }
           
           return result;
    }
    
    /**
     * 
     * @param value
     * @return
     */
    public static String PostgreSqlToJavaDataType(String value) {
        String result = value;
        
        switch(value) {
        case "character varying":
            result = "VARCHAR";
            break;
        case "integer":
            result = "INTEGER";
            break;
        case "timestamp without time zone":
            result = "TIMESTAMP";
            break;
            default:
                break;
        }
        
        return result;
    }
   
     
    /*
     * test
     */
//    public static void main(String[] args) {
//        String query = "SELECT * FROM information_schema.columns where table_name= 'mcc001'";
//        
//        try {
//            Connection conn = PostgreSql.Connection("globals.database.kd_mes.url");
//            List<LinkedHashMap<String,String>> result = PostgreSql.getSelect(conn, query);
//            
//            System.out.println(CommUtil.getDataBasePrintLog(result,true));
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    public static void xmlRead(String id, List<String> params) {
    	String[] array = new String[params.size()];
    	for(int index=0;index<array.length;index++) {
    		array[index] = new String(params.get(index));
    	}
    	
    	xmlRead(id,array);
    	
    }
    
    public static void xmlRead(String id, String[] params) {
        try {
            // XML 문서 파싱
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            
            // root 구하기
            Element root = document.getDocumentElement();
            
            // root의 속성
            System.out.println("class name: " + root.getAttribute("name"));
            
            NodeList childeren = root.getChildNodes(); // 자식 노드 목록 get
            for(int i = 0; i < childeren.getLength(); i++){
                Node node = childeren.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){ // 해당 노드의 종류 판정(Element일 때)
                    Element ele = (Element)node;
                    String nodeName = ele.getNodeName();
                    
                    if(id.equals(nodeName)) {
                    	System.out.println(ele.getAttribute(id));
                    }
                    
                    System.out.println("node name: " + nodeName);
//                    if(nodeName.equals("teacher")){
//                        System.out.println("node attribute: " + ele.getAttribute("name"));
//                    }
//                    else if(nodeName.equals("student")){
//                        // 이름이 student인 노드는 자식노드가 더 존재함
//                        NodeList childeren2 = ele.getChildNodes();
//                        for(int a = 0; a < childeren2.getLength(); a++){
//                            Node node2 = childeren2.item(a);
//                            if(node2.getNodeType() == Node.ELEMENT_NODE){
//                                Element ele2 = (Element)node2;
//                                String nodeName2 = ele2.getNodeName();
//                                System.out.println("node name2: " + nodeName2);
//                                System.out.println("node attribute2: " + ele2.getAttribute("num"));
//                            }
//                        }
//                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private static SqlMapClient sqlMap;
    
    public static  SqlMapClient getSqlMap(){
        Reader reader = null;
        if(sqlMap==null){
            try {
                reader = Resources.getResourceAsReader("config.xml");
                sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return sqlMap;
    }
    
    
    /**
     * @param conn01
     * @param conn02
     * @param table
     * @return
     */
    public static int copyData(Connection conn01,Connection conn02,String table) {
    	int result = 0;
    	String quey01 = "SELECT column_name FROM INFORMATION_SCHEMA.columns WHERE TABLE_NAME = '"+table+"'";
    	List<String> columns = new ArrayList<String>();
//    	List<LinkedHashMap<String,String>> data01 = PostgreSql.getSelect(conn01, quey01);
    	List<LinkedHashMap<String,String>> data01 = PostgreSql.getSelect(conn02, quey01);
    	for(int index=0;index<data01.size();index++) {
    		Map<String,String> temp = data01.get(index);
    		columns.add(temp.get("column_name"));
    	}
    	
    	StringBuilder query02 = new StringBuilder();
    	
    	query02.append("SELECT ");
    	for(int index=0;index<columns.size();index++ ) {
    		if(index != 0) query02.append(" , ");
    		query02.append(columns.get(index).toUpperCase());
    		
    	}
    	query02.append(" FROM ");
    	query02.append(table);
    	
    	
    	List<LinkedHashMap<String,String>> data02 = PostgreSql.getSelect(conn02, query02);
    	for(int index=0;index<data02.size();index++) {
    		Map<String,String> mapData = data02.get(index);
    		StringBuilder query03 = new StringBuilder();
    		query03.append("INSERT INTO ");
    		query03.append(table);
    		query03.append(" ( ");
        	for(int i=0;i<columns.size();i++ ) {
        		if(i != 0) query03.append(" , ");
        		query03.append(columns.get(i).toUpperCase());
        	}
        	query03.append(" ) VALUES ( ");
    		for(int i=0;i<columns.size();i++) {
    			
    			
    			if(i != 0) query03.append(" , ");
    			if(mapData.get(columns.get(i)) != null) {
    				String inData = mapData.get(columns.get(i)).replaceAll("'", "''");
	    			query03.append("'");
	    			query03.append(inData);
	    			query03.append("'");
    			}else {
    				query03.append("NULL");
    			}
    		}
    		query03.append(" )");
    		
//    		System.out.println("■ 쿼리 : "+query03.toString());
    		
    		PostgreSql.setSave(conn01, query03.toString());
    		result++;
    	}
    	
    	return result;
    }
    /**
     * 
     */
    public static void getXmlQuery() {
    	// iBATIS
//    	 public Document iBATISForMake(List result) throws Exception {
//    	   Element data = new Element(root_node);
//    	
//    	   for (int i = 0; i < result.size(); i++ ) {
//    	     Element element = new Element(child_node);
//    	     String xml = (String)result.get(i);
//    	     Document document = builder.build(new StringReader(xml));
//    	
//    	     Element root = document.getRootElement();
//    	     List child = root.getChildren();
//    	     for (Iterator iter = child.iterator();iter.hasNext();) {
//    	       Element node = (Element) iter.next();
//    	
//    	       String name = (String) node.getName();
//    	       String value = (String) node.getText();
//    	       addElement(element,name,value);
//    	     }
//    	
//    	     data.addContent(element);
//    	   }
//    	
//    	   Document document = new Document(data);
//    	
//    	   return document;
//    	 }
//    	
//    	 // 엘리먼트 생성
//    	 public Element addElement(Element parent, String name, String value) {
//    	   Element element = new Element(name);
//    	   element.setText(value);
//    	   parent.addContent(element);
//    	   return parent;
//    	 }
//    	
//    	 // 애트리뷰트 생성
//    	 public void addAttribute(Element element, String name, String value){
//    	   Attribute attribute = new Attribute(name,value);
//    	   element.setAttribute(attribute);
//    	 }
    }
    
    public class BoardVO {
        // NO SUB CONTENT SID REIP REGDATE
        private int no;
        private String sub, content, sid, reip, regdate;
    }
    
    
}