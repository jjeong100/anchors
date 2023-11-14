package com.anchors.database;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.common.CommUtil;

public class JSon정렬 {
	
	public Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
//        System.out.println("------------------------------ "+className+" ------------------- St.");
      
                
        try {
        	JSon정렬 process = new JSon정렬();
              process.Process();
//            
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
        }
//        System.out.println("------------------------------ "+className+" ------------------- Ed.");
    }
	
	 /**
     * 
     */
    public void Process() {
    	StringBuilder result = new StringBuilder();
    	List<String> list = CommUtil.getFileReadListComent("D:\\project\\anchors\\src\\main\\java\\com\\resource\\JSon정렬.txt");
    	
    	for(int index=0;index<list.size();index++) {
    		result.append(list.get(index));
    	}
    	
    	Map<String,Object> map = gson.fromJson(result.toString(), Map.class);
    	System.out.println(gson.toJson(map));
    }
}
