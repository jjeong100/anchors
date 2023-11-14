package com.anchors.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.project.common.CommUtil;
import com.project.common.PostgreSql;

public class 소스분석 {
    public final String resourceRoot = "D:\\project\\anchors\\src\\main\\java\\com\\resource\\";
    public final String sourceFolderPath = "D:\\workspace\\cdpp-app\\";
    
    public Map<String,String> controllerInterface = new HashMap<String,String>();
    public Map<String,String> mapperInterface     = new HashMap<String,String>();
    
    public Map<String,String> interfaceController = new HashMap<String,String>();
    public Map<String,String> interfaceMapping    = new HashMap<String,String>();
    public Map<String,String> interfaceMapper     = new HashMap<String,String>();
    public Map<String,String> transferProcess     = new HashMap<String,String>();
    public Map<String,String> transferReplica     = new HashMap<String,String>();
    
    public Map<String,String> dto     = new HashMap<String,String>();
    public Map<String,String> payload = new HashMap<String,String>();
    
    public Map<String,String> procedure = new HashMap<String,String>();
    
    public String mpperRootPath = "";
    
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
            소스분석 process = new 소스분석();
            process.Process(conn);
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
                                    }
                                    
                                    //mapper
                                    if(sLine.indexOf("defaultMapper.map(") != -1) {
                                        Set<String> keys = dto.keySet(); // 해쉬맵의 키의 집합.
                                        Iterator<String> iter = keys.iterator();
                                        while(iter.hasNext()) {
                                            String key = iter.next();
                                            if(sLine.indexOf(key) != -1) {
                                                System.out.println(mpperRootPath+key.replace("Dto", "").replace("Payload", "")+"Mapper.xml("+controllerInterface.get(file.getPath())+")");
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
    }
    
     /**
     * 
     */
    public void Process(Connection conn) {
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
                                 if(sLine.indexOf("String IF_ID") != -1) {
                                       String result = sLine.split("=")[1];
                                       result = result.replaceAll("\"", "");
                                       result = result.replaceAll(";", "");
                                       result = result.trim();
                                       result = result.toUpperCase();
                                       
                                       interfaceController.put(result, file.getPath());
//                                       controllerInterface.put(file.getPath(), result);
                                 }
                                 
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
                                             interfaceMapper.put(controllerInterface.get(file.getPath()), key.replace("Dto", "").replace("Payload", "")+"Mapper.xml");
//                                             mapperInterface.put(controllerInterface.get(file.getPath()), key.replace("Dto", "").replace("Payload", "")+"Mapper.xml");
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
                                       System.out.println("["+mapperInterface.get(file.getPath())+"]("+file.getPath()+") : "+sLine);
//                                       String result = sLine.trim().split("\\s")[1];
                                       String result = sLine.replace("call", "").replace("CALL", "").trim();
                                       transferProcess.put(mapperInterface.get(file.getPath()), result);
                                       
                                       //////////////////////////////////////////////////////////////////////////////////////////////////////// procedure pattern
                                       StringBuilder param = new StringBuilder();
                                       param.append("{");
                                       Pattern pattern = Pattern.compile("[#{](.*)[}]");
                                          Matcher matcher = pattern.matcher(result);
                                           while (matcher.find()) {
    //                                        System.out.println(matcher.group(0));
                                            String m = matcher.group(0);
                                            String[] split = m.split("\\}\\,\\#\\{");
                                            for(int i=0;i<split.length;i++) {
                                                if(i!=0) param.append(",");
//                                                System.out.println(split[i].split("\\,")[0].replace("#{","").replace("}","").toLowerCase());
                                                param.append(split[i].split("\\,")[0].replace("#{","").replace("}","").toLowerCase());
                                            }
                                        }
                                           param.append("}");
                                       procedure.put(result.trim().replace(";", ""), param.toString());
                                       /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                   }
                                   if(sLine.toLowerCase().indexOf("replica.") != -1 || sLine.toLowerCase().indexOf("\"replica\".") != -1) {
//                                       String result = sLine.trim().split("\\s")[1];
                                	   String result = sLine.replace("call", "").replace("CALL", "").trim();
                                       transferReplica.put(mapperInterface.get(file.getPath()), result);
                                       
                                       //////////////////////////////////////////////////////////////////////////////////////////////////////// procedure pattern
                                       StringBuilder param = new StringBuilder();
                                       param.append("{");
                                       Pattern pattern = Pattern.compile("[#{](.*)[}]");
                                       Matcher matcher = pattern.matcher(result);
                                           while (matcher.find()) {
    //                                        System.out.println(matcher.group(0));
                                            String m = matcher.group(0);
                                            String[] split = m.split("\\}\\,\\#\\{");
                                            for(int i=0;i<split.length;i++) {
                                                if(i!=0) param.append(",");
//                                                System.out.println(split[i].split("\\,")[0].replace("#{","").replace("}","").toLowerCase());
                                                param.append(split[i].split("\\,")[0].replace("#{","").replace("}","").toLowerCase());
                                            }
                                       }
                                       param.append("}");
                                       procedure.put(result.trim().replace(";", ""), param.toString());
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
       
       System.out.println("■ IF005 interfaceController : "+interfaceController.get("IF005"));
       System.out.println("■ IF005 interfaceMapping    : "+interfaceMapping.get("IF005"));
       System.out.println("■ IF005 mapper xml          : "+interfaceMapper.get("IF005"));
       System.out.println("■ IF005 transferProcess     : "+transferProcess.get("IF005"));
       System.out.println("■ IF005 transferReplica     : "+transferReplica.get("IF005"));
       
       System.out.println("■ IF005 procedure     : "+procedure.get(transferReplica.get("IF005")));
       String procName = transferReplica.get("IF005").split("\\(")[0];
       System.out.println(procName);
       프로시져점검 proc = new 프로시져점검();
       proc.Process(conn, procName, procedure.get(transferReplica.get("IF005")));
       
//       CommUtil.getFileReadListComent(sourceFolderPath)
       //컨트롤 위치
       CommUtil.writeFile(resourceRoot+"소스분서_Controller.txt", controller);
       CommUtil.writeFile(resourceRoot+"소스분서_xml.txt", xml);
    }
}
