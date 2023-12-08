package com.anchors.database;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.common.CommUtil;
import com.project.common.PostgreSql;

public class 매핑항목정리_swagger {
	
	
	LinkedHashMap<String,String> resultMap = new LinkedHashMap<String,String>();
	LinkedHashMap<String,String> propertiesMap = new LinkedHashMap<String,String>();

	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
      
        try {
        	매핑항목정리_swagger process = new 매핑항목정리_swagger();
              process.Process();
//            
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
        }
        System.out.println("------------------------------ "+className+" ------------------- Ed.");
    }
	
	/**
	 * 
	 */
    public void Process() {
    	List<String> list01 = CommUtil.getFileReadListComent("D:\\project\\anchors\\src\\main\\java\\com\\resource\\mapping\\매핑항목정리_01.txt");
    	List<String> list02 = CommUtil.getFileReadListComent("D:\\project\\anchors\\src\\main\\java\\com\\resource\\mapping\\swagger\\매핑항목정리_02.txt");
    	
    	for(int index=0;index<list01.size();index++) {
//    		System.out.println(list01.get(index));
    		
    		String data =list01.get(index).replaceAll("\\s+", " ").trim();
//    		System.out.println(data);
    		String[] str = data.split("\\s");
    		resultMap.put(str[0].trim(), "true");
    	}
    	
    	
//    	Set<String> keys = resultMap.keySet(); // 해쉬맵의 키의 집합.
//    	Iterator<String> iter = keys.iterator();
//        while(iter.hasNext()) {
//            String key = iter.next().split("\\s")[0];
////            System.out.println("■"+key + ":" +key);
//        }
        
        /**
         * 
         */
        getDtoJsonProperties();
        
        /**
         * 
         */
        compareResult();
    }
    
    /**
     * 
     * @param path
     * @return
     */
    public LinkedHashMap<String,String> getSwaggerList(String path) {
    	LinkedHashMap<String,String> resultMap = new LinkedHashMap<String,String>();
    	
    	List<String> list = CommUtil.getFileReadListComent(path);

    	
    	for(int index=0;index<list.size();index++) {
    		String data =list.get(index).replaceAll("\\s+", " ");
    		if(data.indexOf("Object") == -1 && data.indexOf("Array") == -1) {
    			String[] str = data.split("\\s");
    			resultMap.put(str[0].trim(), "true");
    		}
    	}
    	
    	return resultMap;
    }
    
    /**
     * 
     * @param list01
     * @param list02
     * @return
     */
    public void getDtoJsonProperties() {
    	List<String> list = CommUtil.getFileReadListComent("D:\\project\\anchors\\src\\main\\java\\com\\resource\\mapping\\swagger\\PayLoad정리.txt");
    	
    	Map<String,String> properties = new HashMap<String,String>();
    	Map<String,String> params = new HashMap<String,String>();
    	for(int index=0;index<list.size();index++) {
//    		System.out.println("■■ ["+index+"] : " + list.get(index));
    		String lineStr = list.get(index).replaceAll("\\s+", " ").trim();
    		if(lineStr.indexOf("@JsonProperty") != -1) {
    			String str = lineStr.replace("@JsonProperty(\"", "").replace("\")", "");
//    			System.out.println("■" + str);
    			properties.put(str.toLowerCase(), str);
    			propertiesMap.put(str, "true");
    		}
    		
    		if(lineStr.toLowerCase().indexOf("private ") != -1) {
    			String str = lineStr.split("\\s")[2].replace(";","").trim();
//    			System.out.println("■■" + str);
    			params.put(str.toLowerCase(), str);
    		}
    	}
    	//propertiesMap put
    	{
	    	Set<String> keys = params.keySet(); // 해쉬맵의 키의 집합.
	    	Iterator<String> iter = keys.iterator();
	        while(iter.hasNext()) {
	            String key = iter.next();
//	            System.out.println("■"+key + ":" +key);
	            
	            if(properties.get(key) != null) {
	            	propertiesMap.put(properties.get(key), "true");
	            }else {
	            	propertiesMap.put(params.get(key), "true");
	            }
	        }
	    }
        
    	//propertiesMap 조회
//        {
//        	Set<String> keys = propertiesMap.keySet(); // 해쉬맵의 키의 집합.
//        	Iterator<String> iter = keys.iterator();
//            while(iter.hasNext()) {
//                String key = iter.next();
//                System.out.println("■>"+key);
//            }
//        }
    }
    
    /**
     * 
     */
    public void compareResult() {
    	List<String> list = CommUtil.getFileReadListComent("D:\\project\\anchors\\src\\main\\java\\com\\resource\\mapping\\매핑항목정리_01.txt");
    	int count =0;
    	for(int index=0;index<list.size();index++) {
    		String data =list.get(index).replaceAll("\\s+", " ").trim();
//    		if(data.indexOf("Object") == -1 && data.indexOf("Array") == -1) {
    			String[] str = data.split("\\s");
//    			resultMap.put(str[0].trim(), "true");
//    			System.out.println("■>"+str[0]);

    			if(propertiesMap.get(str[0]) != null) {
    				System.out.println("■"+str[0] + "|" + propertiesMap.get(str[0]));
    			}else {
    				System.err.println("■"+str[0] + "|" + propertiesMap.get(str[0]));
    			}
//    		}
    			count++;
    	}
    	System.out.println("■■ 갯수 :"+count);
    }
   
}
