package com.project.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

public class GlobalProperties {

//	public static void main(String[] args) {
//		
//		String path = "D:\\workspace\\Project\\sample\\src\\main\\java\\com\\project\\properties\\globals.properties";
//        Properties prop = new Properties();
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        InputStream is = classLoader.getResourceAsStream(path);
//        
//        try {
////            Reader reader = Resources.getResourceAsReader(resource);
////            InputStream reader = Temp.getClass().getResourceAsStream(resource);
////            properties.load(reader);
////            System.out.println(properties.getProperty("driver"));
////            System.out.println(properties.getProperty("username"));
////            System.out.println(properties.getProperty("password"));
////            System.out.println(properties.getProperty("url"));
//        	prop.load(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//       
//        
//        for(Entry<Object,Object> val:prop.entrySet()) {
//        	System.out.println(val);
//        }
//	}
	
	public static String getProperties(String name) {
		String default_path = "D:\\workspace\\anchors\\sample\\src\\main\\java\\com\\project\\properties\\globals.properties";
		return getProperties(default_path,name);
	}
	/*
	 * 
	 */
	public static String getProperties(String properties_path,String name) {
		String result = "";
		String path = properties_path;
        Properties prop = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream(path);
        
        try {
	        FileInputStream fis = new FileInputStream(path);
	        prop.load(fis);
	        
	        result = prop.getProperty(name);
//	        System.out.println("properties result : "+result);
        
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        return result;
	}
}
