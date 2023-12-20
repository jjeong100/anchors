package com.anchors.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class 소스파일정보 {
	public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
        
        try {
        	String folderPath = "";
        	소스파일정보 process = new 소스파일정보();
            process.Process("D:\\workspace\\cdpp-app\\");
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
	public void Process(String sourceFolderPath) {
		  try (Stream<Path> paths = Files.walk(Paths.get(sourceFolderPath))) {
	            paths
	            .filter(Files::isRegularFile)
	            .forEach(a -> {
	            	String sLine = String.valueOf(a);
            		if(sLine.indexOf(".xml") != -1 && sLine.indexOf("classes") == -1) {
            			System.out.println(sLine);
            		}
	            });
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	}
	
}
