package com.anchors.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.common.CommUtil;

public class 쿼리컬럼위치 {
	public String rootPath = "D:\\project\\anchors\\src\\main\\java\\com\\resource\\";
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
                
        String findStr = "immobilization_date__c";
        try {
        	쿼리컬럼위치 process = new 쿼리컬럼위치();
              process.Process(findStr);
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
    public void Process(String findStr) {
//    	List<LinkedHashMap<String,String>> listData = PostgreSql.getSelect(conn, query);
    	//PostgreSql.setSave(conn, insertStr(conn,str,tableName));
    	Map<String,String> insMap = new HashMap<String,String>();
    	Map<String,String> selMap = new HashMap<String,String>();
    	
    	List<String> ins = CommUtil.getFileReadListComent(rootPath+"쿼리컬럼위치_ins");
    	List<String> sel = CommUtil.getFileReadListComent(rootPath+"쿼리컬럼위치_sel");
    	
    	if(ins.size() != sel.size()) {
    		System.err.println("컬럼 갯수가 같지 않습니다.!");
    	}else {
    		for(int index=0;index<ins.size();index++) {
    			insMap.put(ins.get(index).replace(",", "").trim(), sel.get(index).replace(",", "").trim());
    			selMap.put(sel.get(index).replace(",", "").trim(), ins.get(index).replace(",", "").trim());
    		}
    	}
    	
    	if(insMap != null && selMap != null) {
    		if(insMap.get(findStr) != null) {
    			System.out.println("■"+insMap.get(findStr));
    		}
    		
    		if(selMap.get(findStr) != null) {
    			System.out.println("■"+selMap.get(findStr));
    		}
    	}
    }
}
