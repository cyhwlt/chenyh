package com.springboot.service.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.springboot.util.JsonUtil;

@Service
public class ExcelService {
	
	private static Logger logger = LogManager.getLogger(ExcelService.class);

	public Map<String, Object> upload(HttpServletRequest request, MultipartFile file, Model model) throws IOException {
		Map<String, Object> returnValue = new HashMap();
		String msg = "";
		int count = 1;
		try {
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
		            Row row = iterator.next();
		            count ++;
		    	}
		    returnValue.put("code", 0);
		    returnValue.put("message", "上传excel文件成功");
		    returnValue.put("data", null);
		}catch (Exception e){
			returnValue.put("code", -1);
			returnValue.put("message", "上传excel文件失败");
			returnValue.put("data", null);
			msg = "在导入第"+count+"行数据时报错;";
			logger.error(msg + e.getMessage());
		}
//	    model.addAttribute("msg",msg);
	    return returnValue;
		
	}
	
	public Map<String, Object> analysisFile(String path, int sheetNumber){
		Map<String, Object> returnValue = new HashMap();
		File excelFile = new File(path);
		HSSFWorkbook wb = null;
		ExcelInputField[] array = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(excelFile));
			HSSFSheet sheet = wb.getSheetAt(sheetNumber);
			sheet.getSheetName();
			int length = sheet.getRow(0).getPhysicalNumberOfCells();
			array = new ExcelInputField[length]; //excel文件列数
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
			returnValue.put("code", 0);
			returnValue.put("message", "excel解析成功");
			returnValue.put("data", array);
		} catch (Exception e) {
			returnValue.put("code", -1);
			returnValue.put("message", "excel解析失败");
			returnValue.put("data", null);
			logger.error("excel解析失败：" + e.getMessage());
		}
		return returnValue;
	}

	public Map<String, Object> getSheetNames(String filePath){
		Map<String, Object> returnValue = new HashMap();
		HSSFWorkbook hssfWorkbook;
		List<String> names = new ArrayList();
		try {
			hssfWorkbook = new HSSFWorkbook(new FileInputStream(new File(filePath)));
			for(int i=0; i<hssfWorkbook.getNumberOfSheets(); i++){
				HSSFSheet sheetAt = hssfWorkbook.getSheetAt(i);
				names.add(sheetAt.getSheetName());
			}
			returnValue.put("code", 0);
			returnValue.put("message", "");
			returnValue.put("data", names);
		} catch (Exception e) {
			returnValue.put("code", -1);
			returnValue.put("message", "");
			returnValue.put("data", null);
			logger.error("获取excel文件的sheet名：" + e.getMessage());
		}
		return returnValue;
	}
	
}
