package com.anchors.database;

import java.util.List;

import com.project.common.CommUtil;

public class 매핑비교 {
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
        
        try {
        	매핑비교 process = new 매핑비교();
              process.Process();
//            
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
//             try{if(conn != null) {conn.close();}}catch(Exception e) {e.printStackTrace();} 
        }
        System.out.println("------------------------------ "+className+" ------------------- Ed.");
    }
	
	 /**
     * 
     */
    public void Process() {
        List<String> list01 = CommUtil.getFileReadList("D:\\project\\anchors\\src\\main\\java\\com\\resource\\매핑비교_json.txt");
        List<String> list02 = CommUtil.getFileReadList("D:\\project\\anchors\\src\\main\\java\\com\\resource\\매핑비교_format.txt");
        
        
        for(int index=0;index<list02.size();index++) {
        	System.out.println(list02.get(index));
        }
        
    }
    
    public void namecheck(String name) {
    	System.out.println(name);
    }
    
}
