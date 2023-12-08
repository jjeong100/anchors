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

import com.project.common.CommUtil;
import com.project.common.PostgreSql;

public class 매핑항목정리 {
	
	public Map<String,String> landingMap = new HashMap<String,String>();
	public Map<String,String> processMap = new HashMap<String,String>();
	public Map<String,String> replicaMap = new HashMap<String,String>();
	
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
      
        try {
        	매핑항목정리 process = new 매핑항목정리();
              process.Process();
//            
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
        }
        System.out.println("------------------------------ "+className+" ------------------- Ed.");
    }
	
	 /**
     * swagger(X)
     * procedure(X)
     * 
     * landing
     * process
     * replica
     * get
     */
	public String type = "landing";
	public String[] tableName = {"if_product_list_in_wf_product"};
	public String[] fileName01  = {"landing항목_파라메터.txt","landing항목_파라메터02.txt"};
	public String[] fileName02  = {"landing항목_쿼리.txt","landing항목_쿼리02.txt"};
	public String fileRoot      = "D:\\project\\anchors\\src\\main\\java\\com\\resource\\mapping\\";
	public String[] param_replace     = {"","item."};
    public void Process() {
    	StringBuilder result = new StringBuilder();
    	Map<String,String> swaggerMap = new HashMap<String,String>();
    	swaggerMap = getSwaggerList("D:\\project\\anchors\\src\\main\\java\\com\\resource\\mapping\\매핑항목정리_01.txt");
    	
    	
    	//landing map에 put
    	for(int index=0;index<tableName.length;index++) {
    		if("landing".equals(type)) {
    			getLandingList(tableName[index],fileRoot+fileName01[index],fileRoot+fileName02[index],index);
    		}else if("process".equals(type)) {
    			
    		}else if("replica".equals(type)) {
    			
    		}
    	}
    	
    	Set<String> keys = swaggerMap.keySet(); // 해쉬맵의 키의 집합.
    	Iterator<String> iter = keys.iterator();
        while(iter.hasNext()) {
            String key = iter.next().split("\\s")[0].toLowerCase();
            System.out.println("■"+key + ":" +landingMap.get(key.toLowerCase()));
            result.append("■"+key + ":" +landingMap.get(key.toLowerCase()));
        }
        
        if("landing".equals(type)) {
        	CommUtil.writeFile("D:\\project\\anchors\\src\\main\\java\\com\\resource\\mapping\\landing항목_결과.txt",result);
        }else if("process".equals(type)) {
        	CommUtil.writeFile("D:\\project\\anchors\\src\\main\\java\\com\\resource\\mapping\\process항목_결과.txt",result);
        }else if("replica".equals(type)) {
        	CommUtil.writeFile("D:\\project\\anchors\\src\\main\\java\\com\\resource\\mapping\\replica항목_결과.txt",result);
        }
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
     * @param path
     * @return
     */
    public String getLandingList(String tableName,String pathParams,String pathSelect,int position) {
    	String result = "";
    	
    	List<String> list01 = CommUtil.getFileReadListComent(pathParams);
    	List<String> list02 = CommUtil.getFileReadListComent(pathSelect);
    	for(int index=0;index<list02.size();index++) {
    		String str = list02.get(index);
    		if(str.indexOf("insert into ") != -1) {
    			
    		}
    	}
    	
    	for(int index=0;index<list01.size();index++) {
    		String changeData = "";
    		String data =list01.get(index).replaceAll("\\s+", " ");
//    		selectMap.put(data.trim(), "true");
    		
    		Pattern pattern = Pattern.compile("[#{](.*)[}]");
        	Matcher matcher = pattern.matcher(data);
    
        	while (matcher.find()) {
//        	    System.out.println(matcher.group(0));
        		changeData = matcher.group(0);
//        	    System.out.println(m);
        	}
        	
        	String paramStr  = changeData.replace("#{", "").replace("}","").trim().toLowerCase();
        	String selectStr = list02.get(index).replace(",", "").trim();
        	System.out.println( paramStr +"|"+ selectStr);
        	landingMap.put(paramStr.replace(param_replace[position],""), tableName + "\t" +selectStr);
    	}
    	return result;
    }
    
    
    /**
     * 
     * @param path
     * @return
     */
    public String getProcessList(String tableName,String pathParams,String pathSelect,int position) {
    	String result = "";
    	
    	List<String> list01 = CommUtil.getFileReadListComent(pathParams);
    	List<String> list02 = CommUtil.getFileReadListComent(pathSelect);
    	
    	for(int index=0;index<list01.size();index++) {
    		String paramStr  = list01.get(index).replace(",", "").trim().toLowerCase();
        	String selectStr = list02.get(index).replace(",", "").trim();
        	
    		processMap.put(paramStr.replace(param_replace[position],""), tableName + "\t" +selectStr);
    	}
    	return result;
    }
}
