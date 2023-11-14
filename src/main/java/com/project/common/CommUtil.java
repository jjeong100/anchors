package com.project.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CommUtil {
    
    public static String getFileRead(String path) {
        
        StringBuilder buffer = new StringBuilder();
        
        try{
            File file = new File(path);
            if(file.exists()){
                BufferedReader inFile = new BufferedReader(new FileReader(file));
                String sLine = null;
                
                int index=0;
                while( (sLine = inFile.readLine()) != null ){
                    if(index != 0) buffer.append("\r\n");
                    buffer.append(sLine);
                    index++;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return buffer.toString();
    }
    
    /**
     * 
     * @param path
     * @return
     */
  public static List<String> getFileReadList(String path) {
       List<String> result = new ArrayList<String>();
        
        try{
            File file = new File(path);
            if(file.exists()){
                BufferedReader inFile = new BufferedReader(new FileReader(file));
                String sLine = null;
                
                while( (sLine = inFile.readLine()) != null ){
                    result.add(sLine);
                }
            }else {
                System.err.println("[getFileReadList] 파일이 존재하지 않습니다. : "+path);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
  
  /**
   * 
   * @param path
   * @return
   */
public static List<String> getFileReadListComent(String path) {
     List<String> result = new ArrayList<String>();
      
      try{
          File file = new File(path);
          if(file.exists()){
              BufferedReader inFile = new BufferedReader(new FileReader(file));
              String sLine = null;
              
              while( (sLine = inFile.readLine()) != null ){
                  
                  if(sLine.indexOf("--") != -1) sLine = sLine.substring(0,sLine.indexOf("--"));
                  if(!"".equals(sLine.trim())) result.add(sLine);
              }
          }else {
              System.err.println("[getFileReadList] 파일이 존재하지 않습니다. : "+path);
          }
      }catch(Exception e) {
          e.printStackTrace();
      }
      
      return result;
  }
 
    /**
     * isEmpty : true 이면 '' false 이면 null
     * default isEmpty true;
     * @param path
     * @return
     */
  
  public static String[] getStringData(String[] data) {
      return getStringData(data, true);
  }
  
    public static String[] getStringData(String[] data, boolean isEmpty) {
        
        String[] result = data;
        
        try{
            for(int index=0;index<result.length;index++) {
                //"20201116";
                //;
                //"''";
                
                if(result[index] == null || "".equals(result[index].trim())) result[index] = "null";
                result[index] = result[index].replaceAll("\"", "'");
                result[index] = result[index].replaceAll("''''", "''");
                
               
                if(!isEmpty) if(result[index] == null || "''".equals(result[index])) result[index] = "null";
                
//                System.out.println( result[index]);
            }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 
     * @param path
     * @return
     */
    public static List<String> getFileDataListRead(String path,String del) {
        
        List<String> list = new ArrayList<String>();
        
        try{
            File file = new File(path);
            if(file.exists()){
                BufferedReader inFile = new BufferedReader(new FileReader(file));
                String sLine = null;
                while( (sLine = inFile.readLine()) != null ){
                    //System.out.println(sLine); //읽어들인 문자열을 출력 합니다.} 
                    
                    if(del != null) {
                        list.add(sLine.replaceAll(del, ""));
                    } else {
                        list.add(sLine);
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    public static String getInsertString(List<String> columns, List<String[]> datas, String tableName,boolean isLogs){
        return  getInsertString(columns,  datas,  tableName, isLogs,false);
    }
    /**
     * 
     */
    public static String getInsertString(List<String> columns, List<String[]> datas, String tableName,boolean isLogs,boolean isEmpty){
        StringBuilder result = new StringBuilder();
//        if(columns.size() != data.length) return "데이타와 컬럼 갯수가 상이함니다..";
        
        tableName = tableName.toLowerCase();
        
        
        if(isLogs) {
            for(int index=0;index<datas.size();index++) {
                String[] data = getStringData(datas.get(index),isEmpty);
                
//                System.out.println("■■■■■■■■■■■■■■■■■■■ data count("+(index+1)+")");
                for(int i=0;i<columns.size();i++) {
                    System.out.println(columns.get(i)+" : "+data[i]);
                }
                
            }
        }
        
        
        for(int index=0;index<datas.size();index++) {
            result.append("INSERT INTO ");
            result.append(tableName);
            result.append(" (");
            for(int i=0;i<columns.size();i++) {
                if(i!=0) result.append(",");
                result.append(columns.get(i));
            }
            result.append(") VALUES (");
            String[] data = getStringData(datas.get(index));
            for(int i=0;i<data.length;i++) {
                if(i!=0) result.append(",");
                result.append(data[i]);
            }
            result.append(");");
        }
        
        return result.toString();
    }
    
    /**
     * 
     */
    public static String getDataMatchingString(List<String> columns, List<String[]> datas,boolean isLogs){
        StringBuilder result = new StringBuilder();
        
        if(isLogs) {
            for(int index=0;index<datas.size();index++) {
                String[] data = getStringData(datas.get(index));
                
                System.out.println("■■■■■■■■■■■■■■■■■■■ data count("+(index+1)+")");
                for(int i=0;i<columns.size();i++) {
                    System.out.println(columns.get(i)+" : "+data[i]);
                }
                
            }
        }
        
        
        for(int index=0;index<datas.size();index++) {
            String[] data = getStringData(datas.get(index));
            for(int i=0;i<columns.size();i++) {
                if(i!=0) result.append("\r\n");
                result.append(columns.get(i)+":"+data[i]);
            }
        }
        
        return result.toString();
    }
    
    /**
     * 
     */
    public static String getDataBasePrintLog(List<LinkedHashMap<String,String>> datas) {
        return getDataBasePrintLog(datas,false);
    }
    /**
     * 
     */
    public static String getDataBasePrintLog(List<LinkedHashMap<String,String>> datas,boolean isNull) {
        StringBuilder result = new StringBuilder();
        
        
        List<String> columns = new ArrayList<String>();
        if(datas.size() > 0 ) {
            for(String key : datas.get(0).keySet()){
    //            System.out.println("키 : " + key);
                if(!"".equals(result.toString())) result.append(",");
                columns.add(key);
                result.append(key);
            }
//            result.append("\r\n");
            
            System.out.println(datas.size());
            for(int index=0;index<datas.size();index++) {
                //entrySet().iterator()
                Map<String,String> data                 = datas.get(index);
//                Iterator<Entry<String, String>> entries = data.entrySet().iterator();
//                while(entries.hasNext()){
//                    Map.Entry<String, String> entry = entries.next();
//                   // System.out.println("[Key]:" + entry.getKey() + " [Value]:" +  entry.getValue());
////                    result.append(b);
//                }
                result.append("\r\n");
                for(int i=0;i<columns.size();i++) {
                    if(i!=0) {
                        result.append(",");
                    }
                    result.append("'");
                    if(isNull) {
                        if(data.get(columns.get(index)) == null) result.append("");
                        else result.append(data.get(columns.get(index)));
                    }else {
                        result.append(data.get(columns.get(index)));
                    }
                    result.append("'");
                }
            }
        }
        
        return result.toString();
    }
    
    /**
     * 
     * @param writefullPath
     * @param content
     */
    public static boolean writeAddFile(String writefullPath,StringBuilder content) {
        return writeAddFile(writefullPath, content.toString());
    }
    
    
    /**
     */
    public static boolean writeAddFile(String writefullPath,String content) {
        
        List<String> list = CommUtil.getFileReadList(writefullPath);
        StringBuilder temp = new StringBuilder();
        for(int index=0;index<list.size();index++) {
//            if(index != 0) 
            temp.append(list.get(index));
            temp.append("\r\n");
        }
        temp.append(content);
        content = temp.toString();
        ////////////////////////////////////////////////////////// 조회후 저장
                
        File file = new File(writefullPath);
        
        boolean result = false;
        try {
            if(file.exists()){
                System.err.println("[writeFile] 파일이 존재하지 않습니다. (기존 삭제후 생성합니다.): "+writefullPath);
            }else {
                System.err.println("[writeFile] 파일이 존재하지 않습니다. (생성 합니다.): "+writefullPath);
            }
            if(content != null && !"".equals(content)) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(content);
                writer.close();
                
                //파일 쓰기 성공
                result = true;
            }
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 
     * @param writefullPath
     * @param content
     */
    public static boolean writeFile(String writePath,String fileName,List<String> content) {
        String writefullPath = writePath + File.separator + fileName;
        return writeFile(writefullPath, content.toString());
    }
    /**
     * 
     * @param writefullPath
     * @param content
     */
    public static boolean writeFile(String writefullPath,List<String> content) {
        
        StringBuilder result = new StringBuilder();
        
        System.out.println("writeFile content size : "+content.size());
        for(int index=0;index<content.size();index++) {
            if(index != 0) result.append("\r\n");
            result.append(content.get(index));
        }
        return writeFile(writefullPath, result.toString());
    }
    
    /**
     * 
     * @param writefullPath
     * @param content
     */
    public static boolean writeFile(String writePath,String fileName,StringBuilder content) {
        String writefullPath = writePath + File.separator + fileName;
        return writeFile(writefullPath, content.toString());
    }
    /**
     * 
     * @param writefullPath
     * @param content
     */
    public static boolean writeFile(String writefullPath,StringBuilder content) {
        return writeFile(writefullPath, content.toString());
    }
    
    /**
     */
    public static boolean writeFile(String writefullPath,String content) {
        File file = new File(writefullPath);
        
        boolean result = false;
        try {
            if(file.exists()){
                System.err.println("[writeFile] 파일이 존재하지 않습니다. (기존 삭제후 생성합니다.): "+writefullPath);
            }else {
                System.err.println("[writeFile] 파일이 존재하지 않습니다. (생성 합니다.): "+writefullPath);
            }
            if(content != null && !"".equals(content)) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(content);
                writer.close();
                
                //파일 쓰기 성공
                result = true;
            }
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return result;
        
        //Try-with-resources      try가 마지막에 close()를 호출해주기 때문에 우리가 호출해주지 않아도 됩니다.
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            writer.write(content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
        //write()대신에 append()를 사용하여 여러번 text를 입력할 수 있습니다.
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            writer.append("Hello~\n");
//            writer.append("World!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    
    /**
     * 
     * @param writefullPath
     * @param content
     * @param isLog
     * @return
     */
    public static boolean writeFile(String writefullPath,StringBuilder content, boolean isLog) {
    	return writeFile(writefullPath, content.toString(),  isLog);
    }
    
    /**
     * 
     * @param writefullPath
     * @param content
     * @param isLog
     * @return
     */
    public static boolean writeFile(String writefullPath,String content, boolean isLog) {
        File file = new File(writefullPath);
        
        boolean result = false;
        try {
            if(file.exists()){
                if(isLog) System.err.println("[writeFile] 파일이 존재하지 않습니다. (기존 삭제후 생성합니다.): "+writefullPath);
            }else {
            	if(isLog) System.err.println("[writeFile] 파일이 존재하지 않습니다. (생성 합니다.): "+writefullPath);
            }
            if(content != null && !"".equals(content)) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(content);
                writer.close();
                
                //파일 쓰기 성공
                result = true;
            }
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
//    /**
//     outputs: Camel_Case_To_Something_Else_
//     desired output: Camel_Case_To_Something_Else
//     Camel_Case => CamelCase
//    **/
//    public static String camelCase(String value) {
//        String result = value;
//        String regex = "([A-Z][a-z]+)";
//        String replacement = "$1_";
//        result =  value.replaceAll(regex, replacement); 
//        //System.out.println(text.replaceAll("([^_A-Z])([A-Z])", "$1_$2"));
//        
//        
////        CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "SomeInput");
//        return result;
//    }
//    
//    /**
//     * underscore
//     * CamelCase => Camel_Case
//     * 
//     */
//    public static String snakeCase(String value) {
////        Matcher m = Pattern.compile("(?<=[a-z])[A-Z]").matcher(value);
////        Matcher m = Pattern.compile("(\\b[a-z]+|\\G(?!^))((?:[A-Z]|\\d+)[a-z]*)").matcher(value);
////
////        StringBuffer sb = new StringBuffer();
////        System.out.println("m.find() : ["+value+"] :"+m.find());
////        while (m.find()) {
////            m.appendReplacement(sb, "_"+m.group().toLowerCase());
////            System.out.println(sb);
////        }
////        m.appendTail(sb);
////        System.out.println(">> 정규식 테스트 : "+sb.toString());
////        return sb.toString();
//        
//        String regex = "([a-z])([A-Z]+)";
//        String replacement = "$1_$2";
//        System.out.println(value.replaceAll(regex, replacement).toLowerCase());
//        
//        return regex;
//
//    }
    /**
     * com.google.guava
     * stock_qty 변경 => stockQty
     * @param value
     * @return
     */
    public static String camelCase(String value) {
        String result = value;
        try {
//        result = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, value);
            result = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, result);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * <groupId>com.google.guava</groupId>
     * <artifactId>guava</artifactId>
     * <version>30.1-jre</version> 
     * stockQty 변경 => stock_qty
     * @param value
     * @return
     */
    public static String snakeCase(String value) {
        String result = value;
        try {
            result = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,result);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * com.google.guava
     * stock_qty 변경 => StockQty
     * @param value
     * @return
     */
    public static String pascalCase(String value) {
        String result = value;
        try {
            result = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, result);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * com.google.guava
     * stockQty 변경 => STOCK_QTY
     * @param value
     * @return
     */
    public static String snakeCaseToUpper(String value) {
        return snakeCase(value).toUpperCase();
    }
    
    /***
    *  1. Entry 에 For-Each Loop 사용
    *  Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    *  for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
    *      System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
    *  }
    *  
    *  2. Key Value 에 For-Each Loop사용
    *  Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    *   
    *  //iterating over keys only
    *  for (Integer key : map.keySet()) {
    *      System.out.println("Key = " + key);
    *  }
    *   
    *  //iterating over values only
    *  for (Integer value : map.values()) {
    *      System.out.println("Value = " + value);
    *  }
    *  
    *  3. Iterator 사용
    *  - Generic 사용
    *  Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    *  Iterator<Map.Entry<Integer, Integer>> entries = map.entrySet().iterator();
    *  while (entries.hasNext()) {
    *      Map.Entry<Integer, Integer> entry = entries.next();
    *      System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
    *  }
    *  
    *  - Generic 미사용
    *  Map map = new HashMap();
    *  Iterator entries = map.entrySet().iterator();
    *  while (entries.hasNext()) {
    *      Map.Entry entry = (Map.Entry) entries.next();
    *      Integer key = (Integer)entry.getKey();
    *      Integer value = (Integer)entry.getValue();
    *      System.out.println("Key = " + key + ", Value = " + value);
    *  }
    *  
    *  4. Key값으로 Value를 찾는 반복문
    *  Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    *  for (Integer key : map.keySet()) {
    *      Integer value = map.get(key);
    *      System.out.println("Key = " + key + ", Value = " + value);
    *  }
    ******/
    
    /**
     * 
     */
    public static void printOutMap(Map<String,String> map) {
        
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
         
        while (it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
//        
//        Iterator<Map<String,String>> entries = map.entrySet().iterator();
//        while (entries.hasNext()) {
//            String value = entries.next();
//             Map.Entry entry = (Map.Entry) entries.next();
//            String key = entry.getKey();
//            String value = entry.getValue();
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//        }
    }
    
    /**
     * 
     * @return
     */
    public static List<String> getMapFromKey(LinkedHashMap<String,String> mapData) {
        List<String> result = new ArrayList<String>();
        Iterator<Map.Entry<String, String>> h_it = mapData.entrySet().iterator();
        while (h_it.hasNext()) {
            Map.Entry<String, String> data = (Map.Entry<String, String>)h_it.next();
            result.add(data.getKey());
        }
        
        return result;
    }
    
    /**
     * 
     * @return
     */
    public static List<String> getMapFromKey(List<LinkedHashMap<String,String>> dataSet) {
        
        List<String> result = new ArrayList<String>();
        if(dataSet.size() > 0) {
            LinkedHashMap<String,String> map = dataSet.get(0);
            Iterator<Map.Entry<String, String>> h_it = map.entrySet().iterator();
            while (h_it.hasNext()) {
                Map.Entry<String, String> data = (Map.Entry<String, String>)h_it.next();
                result.add(data.getKey());
            }
        }
        
        return result;
    }
    
    public static void SysOutTableView(List<LinkedHashMap<String,String>> dataSet) {
    	SysOutTableView(dataSet,null,null);
    }
    /**
     * 
     */
    public static void SysOutTableView(List<LinkedHashMap<String,String>> dataSet,String token,String filePath) {
        Logger logger = Logger.getLogger(CommUtil.class.getName());
        StringBuilder result = new StringBuilder();
        List<String> keyList = new ArrayList<String>();
        if(dataSet.size() > 0) {
            LinkedHashMap<String,Integer> dataLength = new LinkedHashMap<String,Integer>();
            //////////////////////////////////////////////////////////////////////
            LinkedHashMap<String,String> header      = new LinkedHashMap<String,String>();
            header.putAll(dataSet.get(0));
            
            Iterator<Map.Entry<String, String>> h_it = header.entrySet().iterator();
            while (h_it.hasNext()) {
                Map.Entry<String, String> data = (Map.Entry<String, String>)h_it.next();
                keyList.add(data.getKey());
            }
//            
            //length 초기화
            for(int i=0;i<keyList.size();i++) {
                dataLength.put(keyList.get(i),keyList.get(i).length());
            }
            
            //
            for(int index=0;index<dataSet.size();index++) {
                Map<String,String> map = dataSet.get(index);
                for(int i=0;i<keyList.size();i++) {
                    if(map.get(keyList.get(i)) != null && !"".equals(map.get(keyList.get(i)))) {
                        int source = map.get(keyList.get(i)).length();
                         int target = dataLength.get(keyList.get(i));
                         
                         if(source > target) {
                             dataLength.put(keyList.get(i), source);
                         }
                    }else {
                        dataLength.put(keyList.get(i), keyList.get(i).length());
                    }
                }
            }
            
            System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 데이타 조회 St.");
            for(int index=0;index<keyList.size();index++) {
                System.out.printf("%"+dataLength.get(keyList.get(index))+"s\t",keyList.get(index));
                result.append(keyList.get(index)+"\t");
            }
            System.out.println();//줄바꿈
            result.append("\r\n");
            
            for(int index=0;index<dataSet.size();index++) {
                Map<String,String> map = dataSet.get(index);
                for(int i=0;i<keyList.size();i++) {
                    System.out.printf("%"+dataLength.get(keyList.get(i))+"s\t",map.get(keyList.get(i)));
                    result.append(map.get(keyList.get(i))+"\t");
                }
                System.out.println();//줄바꿈
                result.append("\r\n");
            }
            System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 데이타 조회 Ed.");
            if(filePath != null && !"".equals(filePath)) CommUtil.writeFile(filePath, result);
//            tempSet.addAll(dataSet);
//            System.out.println("사이즈 : "+tempSet.size());
//            ////////////////////////////////////////////////////////////////////// 최대 데이타 길이?
//            for(int index=0;index<tempSet.size();index++) {
//                Map<String,String> map                 = tempSet.get(index);
//                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
////                
//                while(it.hasNext()) {
//                    Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
//                    System.out.println("2 ["+pair.getKey()+"]");
//                    int source = pair.getValue().length();
//                    int target = dataLength.get(pair.getKey());
//                    
//                    if(source > target) {
//                        dataLength.put(pair.getKey(), source);
//                    }
//                    it.remove(); // avoids a ConcurrentModificationException
//                }
//            }
//            
//           ////////////////////////////////////////////////////////////////////// 화면 표시
//            Map<String,String> header_map = new HashMap<String,String>();
//            header_map.putAll(dataSet.get(0));
//            Iterator<Map.Entry<String, String>> header_it = header_map.entrySet().iterator();
////            System.out.println(header_map.get("menu_lvl"));
//               
//            while (header_it.hasNext()) {
////                System.out.println("3");
//                Map.Entry<String, String> data = (Map.Entry<String, String>)header_it.next();
//                
//                System.out.printf("%"+dataLength.get(data.getKey())+"s\t",data.getKey());
//                header_it.remove(); // avoids a ConcurrentModificationException
//            }
//            
////            tempdata
//            //////////////////
//            System.out.println();//줄바꿈
            
//            for(int index=0;index<dataSet.size();index++) {
//                Map<String,String> map                 = dataSet.get(index);
//                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//                
//                while(it.hasNext()) {
//                    Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
//                    
////                    System.out.println()
//                    
//                    it.remove(); // avoids a ConcurrentModificationException
//                }
//            }
            
        }else {
            logger.info("■■■■ 데이타가 없음. ■■■■■");
        }
    }
    
    /**
     * channel Copy
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copyFile(File in, File out) throws IOException {
        
        System.out.println("■■■■ copy Folder 위치 : "+out.getParent());
        
        if(!new File(out.getParent()).isDirectory()) {
            new File(out.getParent()).mkdirs();
        }
        FileChannel inChannel = new FileInputStream(in).getChannel();
        FileChannel outChannel = new FileOutputStream(out).getChannel();
        try {
          // magic number for Windows, 64Mb - 32Kb)
          int maxCount = (64 * 1024 * 1024) - (32 * 1024);
          long size = inChannel.size();
          long position = 0;
          while (position < size) {
            position += inChannel
                .transferTo(position, maxCount, outChannel);
          }
        } catch (IOException e) {
          throw e;
        } finally {
          if (inChannel != null)
            inChannel.close();
          if (outChannel != null)
            outChannel.close();
        }
      }

      public static void makeFile(String Path, String content) {
        try {
          // Create file
          FileWriter fstream = new FileWriter(Path);
          BufferedWriter bf = new BufferedWriter(fstream);
          bf.write(content);
          // Close the output stream
          bf.close();
        } catch (Exception e) {// Catch exception if any
          System.err.println("Error: " + e.getMessage());
        }
      }
      
      /**
       * 
       */
    public static String getFirstLower(String value) {
        String result = value;
        if(result != null && !"".equals(result)) {
            //char ch = value.charAt(0);
            String ch = value.substring(0, 1).toLowerCase();
            String body = value.substring(1);
              
            result = ch + body;
        }
        return result;
    }
    
    /**
     * 
     */
    public static String getFirstUpper(String value) {
        String result = value;
        if(result != null && !"".equals(result)) {
            //char ch = value.charAt(0);
            String ch = value.substring(0, 1).toUpperCase();
            String body = value.substring(1);
            
            result = ch + body;
        }
        return result;
    }
    
    /**
     * 
     * @return
     */
    public static boolean isFunctionDue(List<String> list, String function) {
        boolean result = false;
        StringBuilder buffer = new StringBuilder();
        for(int index=0;index<list.size();index++) {
            buffer.append(list.get(index));
        }
        
//        String val = buffer.toString(); //대상문자열
//        result = Pattern.matches(function, val);
        
        if(buffer.toString().indexOf(function) != -1) result = true;
        System.out.println("같은 function 존재 여부 : "+result);
        
        return result;
    }
    
    /**
     * 
     * @param list
     * @return
     */
    public static String setListByCarriageReturn(List<String> list) {
        StringBuilder result = new StringBuilder();
        for(int index=0;index<list.size();index++) {
            if(index != 0) {
                result.append("\r\n");
            }
            result.append(list.get(index));
        }
        
        return result.toString();
    }
    
    /**
     * 입력받은 String 중 어떤위치의 어떤문자를 제거하라.
     * position : 0부터
     * @param str
     * @param position
     * @param token
     * @return
     */
    public static String getWordDel(String str,int position,String token) {
        String result = str;
        
        String temp = result.charAt(position)+"";
        if(token.equals(temp)) {
            result = str.substring(0, position);
            result = result + str.substring(position+1);
        }
        
        return result;
    }
    
    /**
     * 
     * @param dirPath
     * @return
     */
    public static List<String> getSubDirList(String dirPath) {
        List<String> result = new ArrayList<String>();
        result.addAll(getSubDirList(result,dirPath));
        return result;
    }
    
    /**
     * 하위폴더 읽기
     * @param dirPath
     */
    public static List<String> getSubDirList(List<String> list,String dirPath) {
        List<String> result = new ArrayList<String>();
        File dir = new File(dirPath);
        File files[] = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                result.addAll(getSubDirList(result,file.getPath()));
            } else {
//                System.out.println("file: " + file);
                result.add(file.getPath());
            }
        }
        
        return result;
    }
    
    
    /**
     * 
     */
    public static String getFileNameToFilePath(String filePath) {
        String result = "";
        if(filePath != null) {
            result = filePath;
//            System.out.println(result);
            String[] temp = result.split("\\\\");
//            for(int index=0;index<temp.length;index++) {
//                System.out.println(temp[index]);
//            }
            result = temp[temp.length-1];
        }
        return result;
    }
    
    /**
     *  (.*)는 0개 이상으로 이루어진 어떤 문자열을 의미
     */
    public static String getPatternData(String content,String start,String end) {
        String result = "";
        
//        System.out.println(content);
//        Pattern pattern = Pattern.compile("/(.*)\\.do"); 
        Pattern pattern = Pattern.compile(start+"(.*)"+end); 
        Matcher matcher = pattern.matcher(content);
        
        int index = 0;
        boolean isFind = false;
        while(matcher.find()) {
            //group() 메소드를 호출하고 정규 표현에 일치된 문자열을 꺼냄
//            System.out.println(matcher.group()); 
            if(index != 0) result += "|";
            
            result += matcher.group();
            
            isFind = true;
            index++;
        }
        
      //값이 없을때 들어온값 그대로 전달
        if(!isFind) {
            result = content.replaceAll(" +", "").replaceAll("\"", "").replace("varurl=", "");
//            System.out.println(result);
        }
        return result;
    }
    
    
    /*******************************************************************
     * 테스트
     * @param args
     ******************************************************************/
    public static void main(String[] args) {
        System.out.println("----------- 공통 테스트 ---------- St.");
//        String test = "abcde|xxs";
//        String result = getWordDel(test,5,"|");
//        System.out.println(result);
        String content = "companyApi.jsp  frm.action = \"<c:url value='/companyGetApi.do'/>\"";
        System.out.println(getPatternData(content,"/","\\.do"));
        System.out.println("----------- 공통 테스트 ---------- Ed.");
    }
    
    
    /**
     * split 초기화
     * @return
     */
    public static String[] init_split(String value,String token,int maxLen) {
        String[] result = new String[maxLen];
        String[] split = value.split(token);
        
        for(int index=0;index<maxLen;index++) {
            if(index >= split.length) {
                result[index] = "";
            }else {
                result[index] = split[index];
            }
        }
        return result;
    }
    
    
    /**
     * setter getter생성
     * String[0] = "private int" , String[1] = "out_price"
     * String[0] = "private String" , String[1] = "doc_name"
     * String[0] = "private java.util.Date" , String[1] ="write_dt"
     * @param dataList
     * @return
     */
    public static String setterAndgetter(List<String[]> dataList) {
        //"private String ";"factory_cd"
        StringBuilder result = new StringBuilder();
        for(int index=0;index<dataList.size();index++) {
            String[] array = dataList.get(index);
            
            String type = array[0].replaceAll("private", "").trim();
            String column = CommUtil.camelCase(array[1].trim());
            
            
            result.append("    public "+type+" get"+CommUtil.getFirstUpper(column)+"() {");result.append("\r\n");
            result.append("        return "+column+";");result.append("\r\n");
            result.append("    }");result.append("\r\n");
            
            result.append("\r\n");
            
            result.append("    public void set"+CommUtil.getFirstUpper(column)+"("+type+" "+column+") {");result.append("\r\n");
            result.append("        this."+column+" = "+column+";");result.append("\r\n");
            result.append("    }");result.append("\r\n");
        }
        
        return result.toString();
    }
    
    /**
     * 
     * @param content
     * @return
     */
    public static String doubleQuotesBetweenStringOne(String content) {
        String result = "";
        
        List<String> tempList = doubleQuotesBetweenString(content);
        if(tempList.size() == 1) result = tempList.get(0);
        
        return result;
    }
    
    /**
     * 
     * @return
     */
    public static List<String> doubleQuotesBetweenString(String content) {
        List<String> result = new ArrayList<String>();
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(content);

        while (m.find()) {
//            System.out.println(m.group(1) + "  " + m.group(2));
//            System.out.println(m.group(1));
            result.add(m.group(1));
        }
        
        return result;
    }
    
    public static String dateKorFormat(Date now) {
         String format = "yyyy/MM/dd HH:mm:ss";
        return dateKorFormat(now,format);
    }
    
    /**
     * yyyy/MM/dd HH:mm:ss";
     * 
     * @param now
     * @return
     */
    public static String dateKorFormat(Date now, String dateFormat) {
        String result = "";
        try {
            // 날짜 형식을 String으로 혹은 String을 날짜 형식으로 변환하기 위한 포맷형식이다.
            DateFormat format = new SimpleDateFormat(dateFormat);
            // Calendar형식에서 날짜를 가져온다. 특이점은 Calendar가 singleton 형식이다.
            // Date 값을 가져와서 String으로 변환한다.
            result = format.format(Calendar.getInstance().getTime());
//            System.out.println(datestr);
//             Date 객체를 선언해서 String으로 변환한다.
//            result = format.format(new Date());
            
            result = format.format(now);
//            System.out.println(datestr);
            // String 형식을 Date형식으로 변환한다.
//            Date date = format.parse("2019/06/29");
//            System.out.println(date);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static List<String[]> getListByJsonString(StringBuilder json){
        return getListByJsonString(json.toString());
    }
    
    public static List<String[]> getListByJsonString(String json){
        // 방법1 Member[] array = gson.fromJson(jsonString, Member[].class);
        //        List<Member> list = Arrays.asList(array); 
        // 방법2 List<Member> list2 = gson.fromJson(jsonString, new TypeToken<List<Member>>(){}.getType());
            
        List<String[]> result = new Gson().fromJson(json,new TypeToken<List<String[]>>(){}.getType());
        
        return result;
    }
    
    /**
     * 
     * @param infoList
     * @return
     */
    public static List<String[]> getJspColumnInfo(String[] infoList){
        List<String> result = new ArrayList<String>(Arrays.asList(infoList));
        return getJspColumnInfo(result);
    }
    
    /**
     * 
     * @param infoList
     * @return
     */
    public static List<String[]> getJspColumnInfo(List<String> infoList){
        String path = "../sample/src/main/java/com/project/sample/resource/항목사전.txt";
        List<String> list = getFileReadList(path);
        
        List<String[]> result = new ArrayList<String[]>();
        for(int index=0;index<infoList.size();index++) {
            boolean isValue = true;
            for(int i=0;i<list.size();i++) {
                if(list.get(i).replaceAll(" +","").indexOf(infoList.get(index).replaceAll(" +","")) != -1) {
                    String[] split = list.get(i).split("\\|");
                    String[] array = {split[1],infoList.get(index)};
                    result.add(array);
                    isValue = false;
                    break;
                }
            }
            //비슷한 값이 없다.
            if(isValue) {
                String[] array = {"",infoList.get(index)};
                result.add(array);
            }
        }
        
        return result;
    }
    
    /**
     * 
     * @param content
     * @return
     */
    public static List<String> getReadStringBufferToList(StringBuilder content) {
        return getReadStringBufferToList(content.toString());
    }
    
    /**
     * 
     * @return
     */
    public static List<String> getReadStringBufferToList(String content) {
        List<String> result = new ArrayList<String>();
        String[] split = content.split("\r\n");
        for(int index=0;index<split.length;index++) {
            result.add(split[index]);
        }
        
        return result;
    }
    
    
    
    private static StringBuffer buffer;
    private static Process process; 
    private static BufferedReader bufferedReader; 
    private static StringBuffer readBuffer; 
    
    public static String inputCommand(String cmd) { 
        buffer = new StringBuffer(); 
        buffer.append("cmd.exe "); 
        buffer.append("/c ");
        buffer.append(cmd); 
        return buffer.toString();
    } 
    
    public static void execCommand(String cmd) { 
          try {
            // 명령어 실행
            Process process = Runtime.getRuntime().exec(cmd);
            StringBuffer stdMsg = new StringBuffer();
        
            // 스레드로 inputStream 버퍼 비우기
            ProcessOutputThread o = new ProcessOutputThread(process.getInputStream(), stdMsg);
            o.start();
            StringBuffer errMsg = new StringBuffer();
        
            // 스레드로 errorStream 버퍼 비우기
            o = new ProcessOutputThread(process.getErrorStream(), errMsg);
            o.start();
            // 수행종료시까지 대기
            process.waitFor();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
 
    public static String execCommand01(String cmd) { 
        try { 
            process = Runtime.getRuntime().exec(cmd);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            readBuffer = new StringBuffer(); 
            while((line = bufferedReader.readLine()) != null) { 
                readBuffer.append(line); 
                readBuffer.append("\n");
            } 
//            process.waitFor();
            return readBuffer.toString(); 
        }catch (Exception e) { 
            e.printStackTrace(); 
            System.exit(1); 
        }finally {
            if(bufferedReader != null) try{bufferedReader.close();}catch(Exception e) {e.printStackTrace();}
            if(process != null) {
                try{
                    //그런데 만약 프로세스의 수행이 끝이 날때까지
                    process.getErrorStream().close();
                    process.getInputStream().close();
                    process.getOutputStream().close();
                    process.destroy();
                }catch(Exception e) {e.printStackTrace();}
            }
        }
        return null;
    }
    
    /**
     * 
     * @return
     */
    public static String getNowDate() {
        String format = "yyyyMMdd";
        return getNowDate(format);
    }
    
    /**
     * 
     * @return
     */
    public static String getNowTime() {
        String format = "HHmmss";
        return getNowDate(format);
    }
    
    /**
     * 
     * @param format
     * @return
     */
    public static String getNowDate(String format) {
         return dateKorFormat(new Date(),format);
    }
    
    /**
     * 
     * @param format
     * @return
     */
    public static String getNowTime(String format) {
       return dateKorFormat(new Date(),format);
    }
    
    /**
     * 
     * @return
     */
    public static Map<String,String> getPropertiesInfo(String key) {
//        globals.local.database.dep_mes.url=jdbc:postgresql://127.0.0.1:5432/DEP_MES
        Map<String,String> result = new HashMap<String,String>();
        
        if(key.toLowerCase().indexOf("database") != -1) {
            String value = GlobalProperties.getProperties(key);
            String[] split01 = value.split(":");
//            String[] split02 = split01[1].split(":");
            
            result.put("ip", split01[2].replace("//", ""));
            result.put("port", split01[3].split("/")[0]);
            result.put("db", split01[3].split("/")[1]);
        }
        
        return result;
    }
    
    /**
     * 
     */
    public static void xmlXPath(String path) {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(true);
         DocumentBuilder builder;
         Document doc = null;

        try {
            // xml 파싱하기
//            Reader reader = new Reader();
            Reader r = new FileReader(path);
            InputSource is = new InputSource(r);
            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            // XPathExpression expr = xpath.compile("/response/body/items/item");
            XPathExpression expr = xpath.compile("//mapper");
            
            NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                NodeList child = nodeList.item(i).getChildNodes();
                for (int j = 0; j < child.getLength(); j++) {
                    Node node = child.item(j);
                    System.out.println("현재 노드 이름 : " + node.getNodeName());
                    System.out.println("현재 노드 타입 : " + node.getNodeType());
//                    System.out.println("현재 노드 값 : " + node.getTextContent());
                    System.out.println("현재 노드 네임스페이스 : " + node.getPrefix());
                    System.out.println("현재 노드의 다음 노드 : " + node.getNextSibling());
                    System.out.println("");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * kd
     * - 
     * dp
     * -
     * 
     * @param workspace
     * @return
     */
    public static String getProjectDirName(String workspace) {
        String result = "";
        String root = "D:"+File.separator+"workspace"+File.separator;
        switch(workspace.toLowerCase()) {
        case "kd":
            result = root + workspace.toLowerCase()+File.separator+"kd-mes-1.0"+File.separator;
            break;
        case "dp":
            result = root + workspace.toLowerCase()+File.separator+"dp-mes-1.0"+File.separator;
            break;
            default:
                System.err.println("workspse가 없는 데이타....");
                return result;
        }
        
        return result;
    }
    
    /**
     * 
     * @param map
     */
    public static void getIteratorView(Map<String,Object> map) {
        Set<String> keys = map.keySet(); // 해쉬맵의 키의 집합.
        Iterator<String> iter = keys.iterator();
        while(iter.hasNext()) {
            String key = iter.next(); //*****
            System.out.printf("map.get(\"%s\")=%s\n",key,map.get(key));
        }
    }
    
    /**
     * 
     * @param map
     */
    public static void getStringIteratorView(Map<String,String> map) {
        Set<String> keys = map.keySet(); // 해쉬맵의 키의 집합.
        Iterator<String> iter = keys.iterator();
        while(iter.hasNext()) {
            String key = iter.next(); //*****
            System.out.printf("map.get(\"%s\")=%s\n",key,map.get(key));
        }
    }
    
    /**
     * 
     * @param sourceFolderPath
     */
    public static void getAllFolderFilesNameList(String sourceFolderPath) {

    	try (Stream<Path> paths = Files.walk(Paths.get(sourceFolderPath))) {
			paths
			.filter(Files::isRegularFile)
			.forEach(System.out::println);
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * String date = "20200801";
        
		//1년 후 날짜
		String addYear  = AddDate(date, 1, 0, 0);
        
		//1달 후 날짜
		String addMonth = AddDate(date, 0, 1, 0);
        
		//1일 후 날짜
		String addDay   = AddDate(date, 0, 0, 1);
        
		System.out.println(addYear);  //20210801
		System.out.println(addMonth); //20200901
		System.out.println(addDay);   //20200802
		
     * @param strDate
     * @param year
     * @param month
     * @param day
     * @return
     * @throws Exception
     */
    public static String AddDate(String strDate, int year, int month, int day) {
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
        
		try {
			Date dt = dtFormat.parse(strDate);
			cal.setTime(dt);
			cal.add(Calendar.YEAR,  year);
			cal.add(Calendar.MONTH, month);
			cal.add(Calendar.DATE,  day);
		}catch(Exception e) {
        	e.printStackTrace();
        }
        
		return dtFormat.format(cal.getTime());
	}
    
    /**
     * String date = "20200801";
        
		//1년 후 날짜
		String addYear  = AddDate(date, 1, 0, 0);
        
		//1달 후 날짜
		String addMonth = AddDate(date, 0, 1, 0);
        
		//1일 후 날짜
		String addDay   = AddDate(date, 0, 0, 1);
        
		System.out.println(addYear);  //20210801
		System.out.println(addMonth); //20200901
		System.out.println(addDay);   //20200802
		
     * @param strDate
     * @param year
     * @param month
     * @param day
     * @return
     * @throws Exception
     */
    public static String AddMonth(String strMonth, int month) {
		
    	//String strDate = strMonth+"01";
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMM");
		Calendar cal = Calendar.getInstance();
        try {
			Date dt = dtFormat.parse(strMonth);
			cal.setTime(dt);
	        
			cal.add(Calendar.MONTH, month);
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
		return dtFormat.format(cal.getTime());
	}
    
    /**
     * 
     * @return
     */
    public static String getProjectPath(Object object, String src_package, String fileName, String ext) {
    	String root   = object.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(); //GlobalProperties.class.;//"D:\\workspace\\Project\\sample\\src\\main\\java\\com\\project\\properties\\globals.properties";
    	String result = root.replace("target/classes", "src/main/java");
    	
//    	System.out.println("result : " + result+"|"+root);
    	String[] split = src_package.split("\\.");
    	String url = "";
    	for(int index=0;index<split.length;index++) {
    		url = url + split[index]+"/";
    	}
    	result = result + url + fileName + "."+ext;
    	
    	return result;
    }
    
//public static void callApi(JsonObject params, String type){
//        
//        HttpURLConnection conn = null;
//        JSONObject responseJson = null;
//        
//        try {
//            //URL 설정
//            URL url = new URL("http://localhost:8080/test/api/action");
// 
//            conn = (HttpURLConnection) url.openConnection();
//            
//            // type의 경우 POST, GET, PUT, DELETE 가능
//            conn.setRequestMethod(type);
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Transfer-Encoding", "chunked");
//            conn.setRequestProperty("Connection", "keep-alive");
//            conn.setDoOutput(true);
//            
//            
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            // JSON 형식의 데이터 셋팅
//            JsonObject commands = new JsonObject();
//            JsonArray jsonArray = new JsonArray();
//            
//            params.addProperty("key", 1);
//            params.addProperty("age", 20);
//            params.addProperty("userNm", "홍길동");
// 
//            commands.add("userInfo", params);
//             // JSON 형식의 데이터 셋팅 끝
//            
//            // 데이터를 STRING으로 변경
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String jsonOutput = gson.toJson(commands);
//                 
//            bw.write(commands.toString());
//            bw.flush();
//            bw.close();
//            
//            // 보내고 결과값 받기
//            int responseCode = conn.getResponseCode();
//            if (responseCode == 200) {
//                 BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                StringBuilder sb = new StringBuilder();
//                String line = "";
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//                responseJson = new JSONObject(sb.toString());
//                
//                // 응답 데이터
//                System.out.println("responseJson :: " + responseJson);
//            } 
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            System.out.println("not JSON Format response");
//            e.printStackTrace();
//        }
//    }
}
