package com.anchors.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.project.common.CommUtil;

public class 매핑사양서로부터항목여부파일 {
    public final String findStr = "DateTime";
    public final String DATA_DIRECTORY = "D:"+File.separator+"interface_mapping_spec"+File.separator;
    public 매핑사양서로부터항목여부파일() {
        
    }
    
    public static void main(String[] args) {
        String ext = ".xlsx";
//        String interfaceName = "Inbound Contact Interface Workflow";
//        String interfaceName = "";
//        String root = "D:\\interface_mapping_spec\\HMB Connex Implementation_Interface Spec_IF048_HMB Update Lead WS";
//        String path = root + interfaceName + ext;
        
        try {
            매핑사양서로부터항목여부파일 proc = new 매핑사양서로부터항목여부파일();
            
            File dir = new File(proc.DATA_DIRECTORY);
    //
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File f, String name) {
                    //파일 이름에 "mine"가 붙은것들만 필터링
//                    return name.contains("IF048");
                    return name.contains("Interface");
                }
            };
    
//            String[] filenames = dir.list(filter);
    //        for (String filename : filenames) {
//            for(int index =0;index<filenames.length;index++) {
//                System.out.println("filename["+(index+1)+"] : " + proc.DATA_DIRECTORY+filenames[index]);
//                proc.process(proc.DATA_DIRECTORY+filenames[index]);
//            }
            
            StringBuilder result = new StringBuilder();
            String[] filenames = dir.list(filter);
            for(int index =0;index<filenames.length;index++) {
                System.out.println("filename["+(index+1)+"] : " + proc.DATA_DIRECTORY+filenames[index]);
//                proc.process(proc.DATA_DIRECTORY+filenames[index]);
                result.append(proc.typeColumnName(proc.DATA_DIRECTORY+filenames[index]));
            }
            CommUtil.writeFile("D:\\project\\anchors\\src\\main\\java\\com\\resource\\매핑DateTime정보", result);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 
     */
//    public void process(String path) {
//        StringBuilder result = new StringBuilder();
//        
//        result.append(typeColumnName(path));
//        CommUtil.writeFile("D:\\project\\anchors\\src\\main\\java\\com\\resource\\매핑DateTime정보", result);
//    }
    
  
    
    /**
     * 
     */
    public String typeColumnName(String path) {
        StringBuilder result = new StringBuilder();
         try {
                FileInputStream file = new FileInputStream(path);
                XSSFWorkbook workbook = new XSSFWorkbook(file);
     
                int rowindex=0;
                int columnindex=0;
                int typeColumnindex = 0;
                boolean headerOneFlag = true;
                //시트 수 (첫번째에만 존재하므로 0을 준다)
                //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
//                XSSFSheet sheet=workbook.getSheetAt(3);
                XSSFSheet sheet = workbook.getSheet("Mapping Spec.");
                
                //행의 수
                int rows = sheet.getPhysicalNumberOfRows();
                
                result.append(path+"\r\n");
                for(rowindex=0;rowindex<rows;rowindex++){
                    //행을읽는다
                    XSSFRow row = sheet.getRow(rowindex);
                    if(row !=null){
                        
                        
                        //셀의 수
                        int cells=row.getPhysicalNumberOfCells();
                        for(columnindex=0; columnindex<=cells; columnindex++){
                            //셀값을 읽는다
                            XSSFCell cell=row.getCell(columnindex);
                            String value="";
                          //셀이 빈값일경우를 위한 널체크
                            if(cell==null){
                                continue;
                            }else{
                                value = validationValue(cell);
                            }
                           
                            
                            if("M/O".toLowerCase().equals(value.toLowerCase().trim())) {
                                if(headerOneFlag) {
                                    result.append(rowInfo(row)+"\r\n");
                                    headerOneFlag = false;
                                }
                            }
                            
                            if("Type".toLowerCase().equals(value.toLowerCase().trim())){
                                if(typeColumnindex == 0) {
                                    typeColumnindex = columnindex;
                                }
                            }
                            
                            if(typeColumnindex != 0 && typeColumnindex == columnindex) {
                                if(findStr.toLowerCase().equals(value.toLowerCase().trim())) {
//                                System.out.println(value.trim());
                                    System.out.println(rowInfo(row));
                                    result.append(rowInfo(row)+"\r\n");
                                }
                            }
                        }
                    }
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
         return result.toString();
    }
    
    
    public String rowInfo(XSSFRow row) {
        StringBuilder result = new StringBuilder();
        int cells=row.getPhysicalNumberOfCells();
        for(int columnindex=0; columnindex<=cells; columnindex++){
            //셀값을 읽는다
            XSSFCell cell=row.getCell(columnindex);
            String value="";
            //셀이 빈값일경우를 위한 널체크
            if(cell==null){
                continue;
            }else{
                String message = validationValue(cell);
//                System.out.println(message);
                result.append(message.replaceAll("false", ""));
//                result.append(message);
                result.append("\t");
            }
        }
        
        return result.toString();
    }
    
    public void columnInfo(int columnindex, XSSFRow row) {
        XSSFCell cell = row.getCell(columnindex);
        System.out.println("["+columnindex+"] cell Type : "+validationValue(cell));
        XSSFCell cell2 = row.getCell(columnindex-3);
        System.out.println("["+columnindex+"] : "+validationValue(cell));
        
    }
    
    /**
     * 
     * @param cell
     * @return
     */
    public String validationValue(XSSFCell cell) {
        String result = "";
           //타입별로 내용 읽기
        switch (cell.getCellType()){
        case XSSFCell.CELL_TYPE_FORMULA:
            result = String.valueOf(cell.getCellFormula());
            break;
        case XSSFCell.CELL_TYPE_NUMERIC:
            result = String.valueOf(cell.getNumericCellValue());
            break;
        case XSSFCell.CELL_TYPE_STRING:
            result = String.valueOf(cell.getStringCellValue());
            break;
        case XSSFCell.CELL_TYPE_BLANK:
            result = String.valueOf(cell.getBooleanCellValue());
            break;
        case XSSFCell.CELL_TYPE_ERROR:
            result = String.valueOf(cell.getErrorCellValue());
            break;
        }
        
        return result;
    }
 
}

