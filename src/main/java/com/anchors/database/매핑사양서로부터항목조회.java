package com.anchors.database;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class 매핑사양서로부터항목조회 {
    public 매핑사양서로부터항목조회() {
    	
    }
	
	public static void main(String[] args) {
		String ext = ".xlsx";
		String interfaceName = "Inbound Contact Interface Workflow";
		String root = "D:\\interface_mapping_spec\\HMB Connex Implementation_Interface Spec_IF003_HMB ";
		String path = root + interfaceName + ext;
		
		System.out.println(path);
       
		매핑사양서로부터항목조회 proc = new 매핑사양서로부터항목조회();
		proc.process(path);
    }
	
	
	/**
	 * 
	 */
	public void process(String path) {
		int[] position = getTypePositon(path);
		
		System.out.println("Type 첫번째 위치 : "+position[0]+"번 행 : "+position[1]+"번 열 ");
		
		typeColumnName(path,position[0],position[1]);
	}
	
	public int[] getTypePositon(String path) {
		int[] result = {0,0};
		
		 try {
	            FileInputStream file = new FileInputStream(path);
	            XSSFWorkbook workbook = new XSSFWorkbook(file);
	 
	            int rowindex=0;
	            int columnindex=0;
	            //시트 수 (첫번째에만 존재하므로 0을 준다)
	            //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
//	            XSSFSheet sheet=workbook.getSheetAt(3);
	            XSSFSheet sheet = workbook.getSheet("Mapping Spec.");
	            
	            int typeColumnindex = 0;
	            //행의 수
	            int rows=sheet.getPhysicalNumberOfRows();
	            for(rowindex=0;rowindex<rows;rowindex++){
	                //행을읽는다
	                XSSFRow row=sheet.getRow(rowindex);
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
	                        
	                        if("Type".equals(value.trim())){
	                        	if(typeColumnindex == 0) {
	                        		typeColumnindex = columnindex;
	                        		result[0] = rowindex;
	                        		result[1] = columnindex;
	                        	}
	                        }
	                    }
	                }
	            }
	        }catch(Exception e) {
	            e.printStackTrace();
	        }
		return result;
	}
	
	/**
	 * 
	 */
	public void typeColumnName(String path,int startRowNum, int startColNum) {
		 try {
	            FileInputStream file = new FileInputStream(path);
	            XSSFWorkbook workbook = new XSSFWorkbook(file);
	 
	            int rowindex=0;
	            int columnindex=0;
	            //시트 수 (첫번째에만 존재하므로 0을 준다)
	            //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
//	            XSSFSheet sheet=workbook.getSheetAt(3);
	            XSSFSheet sheet = workbook.getSheet("Mapping Spec.");
	            
	            int typeColumnindex = 0;
	            //행의 수
	            int rows=sheet.getPhysicalNumberOfRows();
	            for(rowindex=0;rowindex<rows;rowindex++){
	                //행을읽는다
	                XSSFRow row=sheet.getRow(rowindex);
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
	                        	if(rowindex > startRowNum && columnindex == startColNum) {
	                        		columnInfo(columnindex,row);
	                        	}
//	                        	System.out.println(validationValue(cell));
	                        }
	                    }
	                }
	            }
	        }catch(Exception e) {
	            e.printStackTrace();
	        }
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

