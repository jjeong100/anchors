package com.anchors.database;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Temp {
    public String rootPath = "D:\\project\\anchors\\src\\main\\java\\com\\resource\\";
    
    
    public static void main(String[] args) {
        final String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println("------------------------------ "+className+" ------------------- St.");
       
        Temp temp = new Temp();
        temp.Process();
        
        System.out.println("------------------------------ "+className+" ------------------- Ed.");
    }
    
    public void Process() {
    	test1();
    }
    public void test1() {
    	String str = " response = ObjectUtils.isNotEmpty(resultDto) ? defaultMapper.map(resultDto, IFHMBSAPEAICDPP0003Payload.Response.class) : null; ";
//    	String str = " process.if_product_in_wf(#{PARAM_ID,jdbcType=VARCHAR,mode=IN},#{PARAM_ID,jdbcType=VARCHAR,mode=IN}); ";
    	
    	   
//    	String str = "|마이크로소프트||애플||페이스북||네이버|";
//    	Pattern pattern = Pattern.compile("[#{](.*)[}]");
    	Pattern pattern = Pattern.compile("[(](.*)[)]");

    	Matcher matcher = pattern.matcher(str);

    	while (matcher.find()) {
//    	    System.out.println(matcher.group(0));
    	    String m = matcher.group(0);
    	    String[] split = m.split("\\}\\,\\#\\{");
    	    for(String fstr : split)
    	    System.out.println(fstr.split("\\,")[0].replace("#{","").replace("}","").toLowerCase());

//    	    if (matcher.group(1) == null) break;
    	}
    }
    
    public void test2() {
    	String str = " process.if_product_in_wf(#{PARAM_ID,jdbcType=VARCHAR,mode=IN}); ";
    	
    	Pattern pattern = Pattern.compile("(\\b#{\\b)(.*?)(\\b}\\b)");
    	Matcher matcher = pattern.matcher(str);
    	if(matcher.find()){    // 정규식과 매칭되는 값이 있으면
    	    System.out.println(matcher.group(2).trim());        // 특정 단어 사이의 값을 추출한다
    	}
    }
    
   
}
