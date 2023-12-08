package com.anchors.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.project.common.CommUtil;
import com.project.common.PostgreSql;

public class 소스분석 {
	
//	public final String IF_ID            = "IF007";//=>머지 확인중
//	public final String IF_ID            = "IF013";
	public final String IF_ID            = "IF044";
//	public final String swaggerURL       = "\\";
    public final String resourceRoot     = "D:\\project\\anchors\\src\\main\\java\\com\\resource\\";
    public final String sourceFolderPath = "D:\\workspace\\cdpp-app\\";
    
    public Map<String,String> controllerInterface = new HashMap<String,String>();
    public Map<String,String> mapperInterface     = new HashMap<String,String>();
    
    public Map<String,String> interfaceController = new HashMap<String,String>();
    public Map<String,String> interfaceMapping    = new HashMap<String,String>();
    public Map<String,String> interfaceMapper     = new HashMap<String,String>();
    public Map<String,String> transferProcess     = new HashMap<String,String>();
    public Map<String,String> transferReplica     = new HashMap<String,String>();
    
    public Map<String,String> interfacePayload    = new HashMap<String,String>();
    
    public Map<String,String> definition    = new HashMap<String,String>();
    
    public Map<String,String> dto     = new HashMap<String,String>();
    public Map<String,String> payload = new HashMap<String,String>();
    
    public Map<String,String> procedure = new HashMap<String,String>();
    
    public String mpperRootPath = "";
    
    public boolean isExistInterface = false;
    
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
            소스분석 process = new 소스분석();
            if(process.isExistInterface) process.Process(conn,serverType);
            else System.err.println(process.IF_ID+"가 존재하지 않습니다.");
            
//            process.useTable();
//            
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
             try{if(conn != null) {conn.close();}}catch(Exception e) {e.printStackTrace();} 
        }
        System.out.println("------------------------------ "+className+" ------------------- Ed.");
    }
    
//    public String fileBufferRead() {
//        char[] c = new char[(int)files[i].length()];
//        br = new BufferedReader (new FileReader(files[i]));                      
//        br.read(c);
//        str.append(c);
//    }
    public 소스분석() {
        
        try (Stream<Path> paths = Files.walk(Paths.get(sourceFolderPath))) {
            paths
            .filter(Files::isRegularFile)
            .forEach(a -> {
                 /** dto */
                 if(String.valueOf(a).toLowerCase().indexOf("dto.java") != -1) dto.put(String.valueOf(a.getFileName()).replace(".java", ""), "true");
                  
                 /** payLoad */
                 if(String.valueOf(a).toLowerCase().indexOf("payload.java") != -1) payload.put(String.valueOf(a.getFileName()).replace(".java", ""), "true");
                 
                 /** mapper root path **/
                 if(String.valueOf(a).toLowerCase().indexOf("mapper.xml") != -1 && String.valueOf(a).toLowerCase().indexOf("classes") == -1)  mpperRootPath = String.valueOf(a.getParent())+"\\";
                 
                 
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
        
         try (Stream<Path> paths = Files.walk(Paths.get(sourceFolderPath))) {
             paths
             .filter(Files::isRegularFile)
             .forEach(a -> {
                  /**
                   * controller
                   */
                  if(String.valueOf(a).toLowerCase().indexOf("controller.java") != -1) {
                      
                      try{
                            File file = new File(String.valueOf(a));
                            if(file.exists()){
                                BufferedReader inFile = new BufferedReader(new FileReader(file));
                                String sLine = null;
                                
                                while( (sLine = inFile.readLine()) != null ){
                                    if(sLine.indexOf("String IF_ID") != -1) {
                                          String result = sLine.split("=")[1];
                                          result = result.replaceAll("\"", "");
                                          result = result.replaceAll(";", "");
                                          result = result.trim();
                                          result = result.toUpperCase();
                                          controllerInterface.put(file.getPath(), result);
                                          
                                          interfaceController.put(result, file.getPath());
                                          
                                          String[] split = String.valueOf(a.getParent()).split("\\\\");
//                                          System.out.println(split[split.length-1]);
                                          definition.put(result, split[split.length-1]);
                                    }
                                    
                                    //mapper
                                    if(sLine.indexOf("defaultMapper.map(") != -1) {
                                        Set<String> keys = dto.keySet(); // 해쉬맵의 키의 집합.
                                        Iterator<String> iter = keys.iterator();
                                        while(iter.hasNext()) {
                                            String key = iter.next();
                                            if(sLine.indexOf(key) != -1) {
//                                                System.out.println(mpperRootPath+key.replace("Dto", "").replace("Payload", "")+"Mapper.xml("+controllerInterface.get(file.getPath())+")");
                                                mapperInterface.put(mpperRootPath+key.replace("Dto", "").replace("Payload", "")+"Mapper.xml",controllerInterface.get(file.getPath()));
                                            }
                                        }
                                    }
                                }
                            }else {
                                System.err.println("[getFileReadList] 파일이 존재하지 않습니다. : "+a);
                            }
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                  }
             });
         } catch(Exception e) {
             e.printStackTrace();
         }
         
         isExistInterface = interfaceController.get(IF_ID)==null?false:true;
    }
    
     /**
     * 
     */
    public void Process(Connection conn,String serverType) {
       StringBuilder controller = new StringBuilder();
       StringBuilder impl       = new StringBuilder();
       StringBuilder xml        = new StringBuilder();
       
       try (Stream<Path> paths = Files.walk(Paths.get(sourceFolderPath))) {
           paths
           .filter(Files::isRegularFile)
           .forEach(a -> {
               /**
                * controller
                */
               if(String.valueOf(a).toLowerCase().indexOf("controller.java") != -1) {
                   controller.append(a+"\r\n");
                   
                   
                   try{
                         File file = new File(String.valueOf(a));
                         if(file.exists()){
                             BufferedReader inFile = new BufferedReader(new FileReader(file));
                             String sLine = null;
                             
                             while( (sLine = inFile.readLine()) != null ){
                                 
//                                 String IF_ID = "";
                                 /** if id**/
//                                 if(sLine.indexOf("String IF_ID") != -1) {
//                                       String result = sLine.split("=")[1];
//                                       result = result.replaceAll("\"", "");
//                                       result = result.replaceAll(";", "");
//                                       result = result.trim();
//                                       result = result.toUpperCase();
//                                       
//                                       interfaceController.put(result, file.getPath());
////                                       controllerInterface.put(file.getPath(), result);
//                                 }
                                 
                                 //mapping url
                                 if(sLine.indexOf("@PostMapping") != -1) {
                                       String result = sLine.split("=")[1];
                                       result = result.replaceAll("\"", "");
                                       result = result.replace("(", "");
                                       result = result.replace(")", "");
                                       result = result.trim();
                                       
                                       interfaceMapping.put(controllerInterface.get(file.getPath()), result);
                                 }
                                 
                                 //mapper
                                 if(sLine.indexOf("defaultMapper.map(") != -1) {
                                     Set<String> keys = dto.keySet(); // 해쉬맵의 키의 집합.
                                     Iterator<String> iter = keys.iterator();
                                     while(iter.hasNext()) {
                                         String key = iter.next();
                                         if(sLine.indexOf(key) != -1) {
                                             interfaceMapper.put(controllerInterface.get(file.getPath()), mpperRootPath+key.replace("Dto", "").replace("Payload", "")+"Mapper.xml");
//                                             mapperInterface.put(controllerInterface.get(file.getPath()), key.replace("Dto", "").replace("Payload", "")+"Mapper.xml");
                                             
                                             interfacePayload.put(controllerInterface.get(file.getPath()), mpperRootPath+key.replace("Dto", "").replace("Payload", "")+"Payload.java");
                                         }
//                                         System.out.printf("map.get(\"%s\")=%s\n",key,map.get(key));
                                     }
                                 }
                             }
                         }else {
                             System.err.println("[getFileReadList] 파일이 존재하지 않습니다. : "+a);
                         }
                     }catch(Exception e) {
                         e.printStackTrace();
                     }
               }
               
               /**
                * xml
                */
               if(String.valueOf(a).toLowerCase().indexOf(".xml") != -1 
                       && String.valueOf(a).toLowerCase().indexOf("classes") == -1) {
                   
                   xml.append(a+"\r\n");
                   
                   try{
                       File file = new File(String.valueOf(a));
                       if(file.exists()){
                           BufferedReader inFile = new BufferedReader(new FileReader(file));
                           String sLine = null;
                           
                           while( (sLine = inFile.readLine()) != null ){
                               
//                               System.out.println(sLine);
                               
//                               String IF_ID = "";
                               /** procedure **/
                               if(sLine.toLowerCase().indexOf("call ") != -1) {
                                  
                                   if(sLine.toLowerCase().indexOf("process.") != -1 || sLine.toLowerCase().indexOf("\"process\".") != -1) {
                                       String result = sLine.replace("call", "");
                                       result = result.replace("CALL", "");
                                       result = result.replace(";", "");
                                       result = result.replaceAll("//s", "");
                                       result = result.trim();
                                       
                                       transferProcess.put(mapperInterface.get(file.getPath()), result);
                                       procedure.putAll(getProcedureMap(result));
                                       /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                   }
                                   if(sLine.toLowerCase().indexOf("replica.") != -1 || sLine.toLowerCase().indexOf("\"replica\".") != -1) {
                                	   String result = sLine.replace("call", "");
                                       result = result.replace("CALL", "");
                                       result = result.replace(";", "");
                                       result = result.replaceAll("//s", "");
                                       result = result.trim();
                                       transferReplica.put(mapperInterface.get(file.getPath()), result);
                                       procedure.putAll(getProcedureMap(result));
                                       /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                   }
                               }
                           }
                       }else {
                           System.err.println("[getFileReadList] 파일이 존재하지 않습니다. : "+a);
                       }
                   }catch(Exception e) {
                       e.printStackTrace();
                   }
               }
           });
       } catch(Exception e) {
           e.printStackTrace();
       }
       
       
       /** Out Put **/
       StringBuilder result = new StringBuilder();
       
       
       System.out.println("■ "+IF_ID+" definition : "+definition.get(IF_ID));
       result.append("■ "+IF_ID+" definition : "+definition.get(IF_ID)); result.append("\r\n");
       System.out.println("■ "+IF_ID+" interfaceController : "+interfaceController.get(IF_ID));
       result.append("■ "+IF_ID+" interfaceController : "+interfaceController.get(IF_ID)); result.append("\r\n");
       System.out.println("■ "+IF_ID+" interfaceMapping    : "+interfaceMapping.get(IF_ID));
       result.append("■ "+IF_ID+" interfaceMapping    : "+interfaceMapping.get(IF_ID)); result.append("\r\n");
       System.out.println("■ "+IF_ID+" mapper xml          : "+interfaceMapper.get(IF_ID));
       result.append("■ "+IF_ID+" mapper xml          : "+interfaceMapper.get(IF_ID)); result.append("\r\n");
       System.out.println("■ "+IF_ID+" transferProcess     : "+transferProcess.get(IF_ID));
       result.append("■ "+IF_ID+" transferProcess     : "+transferProcess.get(IF_ID)); result.append("\r\n");
       System.out.println("■ "+IF_ID+" transferReplica     : "+transferReplica.get(IF_ID));
       result.append("■ "+IF_ID+" transferReplica     : "+transferReplica.get(IF_ID)); result.append("\r\n");
       System.out.println("■ "+IF_ID+" interfacePayload     : "+interfacePayload.get(IF_ID));
       result.append("■ "+IF_ID+" interfacePayload     : "+interfacePayload.get(IF_ID)); result.append("\r\n");
       
//       System.out.println("■ "+IF_ID+" procedure     : "+procedure.get(transferReplica.get(IF_ID)));
       String procProcessName = (transferProcess.get(IF_ID) != null)?transferProcess.get(IF_ID).split("\\(")[0]:"";
       String procReplicaName = (transferReplica.get(IF_ID) != null)?transferReplica.get(IF_ID).split("\\(")[0]:"";
//       System.out.println(procName);
       
       //쿼리 파일
//       System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ query ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
       result.append("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ query ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\r\n");
       System.out.println(interfaceMapper.get(IF_ID));
       List<String> list = CommUtil.getFileReadList(interfaceMapper.get(IF_ID));
       for(int index=0;index<list.size();index++) {
//    	   System.out.println(list.get(index));
    	   result.append(list.get(index)); result.append("\r\n");
       }
       
       프로시져점검 proc = new 프로시져점검();
       if(procProcessName != null && !"".equals(procProcessName)) {
//    	   System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ process ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
    	   result.append("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ process ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\r\n");
    	   result.append(proc.Process(conn, procProcessName, procedure.get(transferProcess.get(IF_ID)))); result.append("\r\n");
       }
       if(procReplicaName != null && !"".equals(procReplicaName)) {
//    	   System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ replica ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
    	   result.append("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ replica ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\r\n");
    	   result.append(proc.Process(conn, procReplicaName, procedure.get(transferReplica.get(IF_ID)))); result.append("\r\n");
       }
       
       //컨트롤 위치
       CommUtil.writeFile(resourceRoot+"소스분서_Controller.txt", controller);
       CommUtil.writeFile(resourceRoot+"소스분서_xml.txt", xml);
       
       System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 사용테이블 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
       result.append("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 테스트 로컬 사용테이블 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\r\n");
       List<String> useList = useTable(result.toString(),serverType);
       for(String use:useList) result.append(use+"\r\n");
    	   
       /** 인터페이스별 소스분석 정보 **/
       CommUtil.writeFile(resourceRoot+"interface\\"+IF_ID+".txt",result.toString());
       
    }
    
    /**
     * 
     */
    public Map<String,String> getProcedureMap(String value) {
    	Map<String,String> result = new HashMap<String,String>();
    	  //////////////////////////////////////////////////////////////////////////////////////////////////////// procedure pattern
        StringBuilder param = new StringBuilder();
        param.append("{");
        Pattern pattern = Pattern.compile("[#{](.*)[}]");
        Matcher matcher = pattern.matcher(value);
            while (matcher.find()) {
//                                        System.out.println(matcher.group(0));
             String m = matcher.group(0);
             String[] split = m.replaceAll("\\s", "").split("\\}\\,\\#\\{");
             for(int i=0;i<split.length;i++) {
                 if(i!=0) param.append(",");
//                 System.out.println(split[i].split("\\,")[0].replace("#{","").replace("}","").toLowerCase());
                 param.append(split[i].split("\\,")[0].replace("#{","").replace("}","").toLowerCase());
             }
        }
        param.append("}");
        result.put(value.trim().replace(";", ""), param.toString());
        
        return result;
    }
    
    /** 
     * 
     */
    public List<String> useTable(String value,String serverType){
    	List<String> result = new ArrayList<String>();
    	result.addAll(useTable(value.toLowerCase(),"landing",serverType));
    	result.addAll(useTable(value.toLowerCase(),"\"landing\"",serverType));
    	result.addAll(useTable(value.toLowerCase(),"process",serverType));
    	result.addAll(useTable(value.toLowerCase(),"\"process\"",serverType));
    	result.addAll(useTable(value.toLowerCase(),"replica",serverType));
    	result.addAll(useTable(value.toLowerCase(),"\"replica\"",serverType));
    	
    	return result;
    }
    public List<String> useTable(String value,String schema,String serverType) {
    	List<String> result = new ArrayList<String>();
    	Map<String,String> data = new HashMap<String,String>();
    	Pattern pattern = Pattern.compile(schema+"\\." + ".*");
    	Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
        	String m = matcher.group(0);
        	if(m.indexOf("#") == -1) {
        		String[] split = m.split("\\s");
        		data.put(split[0].trim(), "true");
        	}
        }
        
        Set<String> keys = data.keySet(); // 해쉬맵의 키의 집합.
        Iterator<String> iter = keys.iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            if("local".equals(serverType)) {
            	 System.out.println("delete from "+key + ";");
            }else {
            	 System.out.println("delete from "+key + "       where not exists (select 1);");
            }
            	
           
            result.add(key);
        }
    	return result;
    }
    
    /**
     * 
     */
    public void test() {
    	String value = "";
    	getProcedureMap(value);
    }
}
