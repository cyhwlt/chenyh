package com.springboot.service.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.pentaho.di.trans.steps.excelinput.ExcelInputField;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService {
	
	private static Logger logger = LogManager.getLogger(ExcelService.class);

	public String upload(HttpServletRequest request, MultipartFile file, Model model) throws IOException {
		String msg = "";
	    int count = 1;
	    byte[] bytes = file.getBytes();
	    Long currentTimeMillis = System.currentTimeMillis();
	    Path path = Paths.get("E:\\" + currentTimeMillis + "_" + file.getOriginalFilename());
	    Files.write(path,bytes);
	    File f = new File(path.toString());
	    FileInputStream fileInputStream = new FileInputStream(f);
	    Workbook workbook = new HSSFWorkbook(fileInputStream);
	    fileInputStream.close();
	    Sheet sheet = workbook.getSheetAt(0);
	    byte[] b = new byte[1024];
	    Iterator<Row> iterator = sheet.rowIterator();
	    while (iterator.hasNext()){
	        try {
	            Row row = iterator.next();
	            count ++;
	        }catch (Exception e){
	            e.printStackTrace();
	            msg = "在导入第"+count+"行数据时报错;";
	            break;
	        }
	    }
	    if ("".equals(msg)){
	        msg = "导入成功！共导入"+count+"条试题";
	    }
	    model.addAttribute("msg",msg);
	    return "system/import_result";
		
	}
	
	public ExcelInputField[] analysisFile(String path, int sheetNumber) throws Exception{
		File excelFile = new File(path);
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
		HSSFSheet sheet = wb.getSheetAt(sheetNumber);
		sheet.getSheetName();
		int length = sheet.getRow(0).getPhysicalNumberOfCells();
		ExcelInputField[] array = new ExcelInputField[length]; //excel文件列数
		int count = 0; //数组项
		for (Row row : sheet) {
            for (Cell cell : row) {
            	array[count] = new ExcelInputField();
                switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                	logger.info(cell.getRichStringCellValue().getString()); 
                	array[count].setName(cell.getRichStringCellValue().getString());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                    	logger.info(String.valueOf(cell.getDateCellValue()));
                    	array[count].setName(String.valueOf(String.valueOf(cell.getDateCellValue())));
                    } else {
                    	logger.info(cell.getNumericCellValue());
                    	array[count].setName(String.valueOf(cell.getNumericCellValue()));
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                	logger.info(cell.getBooleanCellValue());
                	array[count].setName(String.valueOf(cell.getBooleanCellValue()));
                    break;
                default:
                }
                count++;
            }
            break;
		}
		return array;
	}

	public List<String> getSheetNames(String filePath) throws Exception {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(new File(filePath)));
		List<String> names = new ArrayList();
		for(int i=0; i<hssfWorkbook.getNumberOfSheets(); i++){
			HSSFSheet sheetAt = hssfWorkbook.getSheetAt(i);
			names.add(sheetAt.getSheetName());
		}
		return names;
	}
	
}
