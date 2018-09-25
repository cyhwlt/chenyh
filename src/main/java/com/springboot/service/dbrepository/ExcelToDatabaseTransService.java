package com.springboot.service.dbrepository;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.excelinput.ExcelInputField;
import org.pentaho.di.trans.steps.excelinput.ExcelInputMeta;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.springframework.stereotype.Service;

import com.springboot.entity.DatabaseDto;
import com.springboot.entity.KtrDto;

@Service
public class ExcelToDatabaseTransService {
	
	private static Logger logger = LogManager.getLogger(ExcelToDatabaseTransService.class); 

	public void excelToDatabase(KtrDto dto){
		try {
			TransMeta generateTrans = this.generateKtr(dto);
			String xml = generateTrans.getXML();
			String transName = dto.getTransName();
			File file = new File(transName);
			FileUtils.writeStringToFile(file, xml, "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 1.excel输入
	 * 2.表输出
	 * 注：二维excel使用数据字典设计
	 * @throws Exception 
	 */
	public TransMeta generateKtr(KtrDto dto) throws Exception {
		TransMeta transMeta = new TransMeta();
		//生成databaseXML——表输出信息
		String[] dbXml = generateDBxml(dto.getOutputDB());
		//设置转换名称
		transMeta.setName(dto.getTransName());
		//添加转换的数据库连接
		for(int i=0; i<dbXml.length; i++){
			DatabaseMeta dbMeta = new DatabaseMeta(dbXml[i]);
			transMeta.addDatabase(dbMeta);
		}
		// registry是给每个步骤生成一个标识ID
		PluginRegistry registryID = PluginRegistry.getInstance();
		
		//1.设置表输出参数
		InsertUpdateMeta insertMeta = new InsertUpdateMeta();
		String iuPluginId = registryID.getPluginId(StepPluginType.class, insertMeta);
		//添加数据库连接
		DatabaseMeta db_chenyh = transMeta.findDatabase(dto.getOutputDB().getConnName());
		insertMeta.setDatabaseMeta(db_chenyh);
		//设置操作的表
		insertMeta.setTableName(dto.getOutputTableName());
		
		//设置用来查询的关键字
		insertMeta.setKeyLookup(new String[]{"ID"});
		insertMeta.setKeyStream(new String[]{"ID"});
		insertMeta.setKeyStream2(new String[]{""});
		insertMeta.setKeyCondition(new String[]{"="});
		
		// 生成要更新的字段
		String[] update = generateField(dto.getFields());
		//设置要更新的字段
		Boolean[] updateOrNot = {false, true, true, true, true, true, true};
		insertMeta.setUpdateLookup(update);
		insertMeta.setUpdateStream(update);
		insertMeta.setUpdate(updateOrNot);
		insertMeta.getUpdateLookup();
		//添加到转换中
		StepMeta insertStep = new StepMeta(iuPluginId, "表输出", insertMeta);
		insertStep.setDraw(true);
		insertStep.setLocation(250, 100);
		transMeta.addStep(insertStep);
		
		//2.设置excel输入参数
		ExcelInputMeta eiMeta = new ExcelInputMeta();
		// 读取excel文件，获取字段
		ExcelInputField[] fields = anaylsisFile(dto.getFilePath());
		eiMeta.setField(fields);
		String[] fileName = new String[]{dto.getFilePath()};
		String[] fileMask = {};
		String[] fileRequired = {"N"};
		eiMeta.setFileName(fileName);
//		eiMeta.setFileMask(fileMask);
//		eiMeta.setExcludeFileMask(fileMask);
		eiMeta.setFileRequired(fileRequired);
		eiMeta.setIncludeSubFolders(fileRequired);
//		eiMeta.setFileField("");
//		eiMeta.setSheetField("");
//		eiMeta.setSheetRowNumberField("");
//		eiMeta.setRowNumberField("");
		eiMeta.setAddResultFile(true);
		eiMeta.setStartsWithHeader(true);
		eiMeta.setIgnoreEmptyRows(true);
		eiMeta.setStopOnEmpty(false);
		eiMeta.setAcceptingFilenames(false);
//		eiMeta.setAcceptingField("");
//		eiMeta.setAcceptingStepName("");
//		eiMeta.allocate(1, 1, 1);
		String[] sheetName = {"Sheet1"};
		eiMeta.setSheetName(sheetName);
		int[] row = {0};
		int[] col = {0};
		eiMeta.setStartColumn(col);
		eiMeta.setStartRow(row);
		eiMeta.setStrictTypes(false);
		eiMeta.setErrorIgnored(false);
		eiMeta.setErrorLineSkipped(false);
//		eiMeta.setWarningFilesDestinationDirectory("");
		eiMeta.setBadLineFilesExtension("waring");
//		eiMeta.setErrorFilesDestinationDirectory("");
		eiMeta.setErrorFilesExtension("error");
//		eiMeta.setLineNumberFilesDestinationDirectory("");
		eiMeta.setLineNumberFilesExtension("line");
//		eiMeta.setShortFileNameField("");
//		eiMeta.setPathField("");
//		eiMeta.setIsHiddenField("");
//		eiMeta.setLastModificationDateField("");
//		eiMeta.setUriField("");
//		eiMeta.setRootUriField("");
//		eiMeta.setSizeField("");
		
		String eiPluginId = registryID.getPluginId(StepPluginType.class, eiMeta); 
		StepMeta stepMeta = new StepMeta(eiPluginId,"excel输入",eiMeta);
		transMeta.addStep(stepMeta);
		
		//添加hop将两个步骤关联起来
		transMeta.addTransHop(new TransHopMeta(stepMeta, insertStep));
		logger.info("**********the end***********");
		return transMeta;
		
	}
	
	public String[] generateDBxml(DatabaseDto outputDB){
		String[] databasesXML = {
	            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	            "<connection>" +
	            "<name>"+outputDB.getConnName()+"</name>" +
	            "<server>"+outputDB.getHostName()+"</server>" +
	            "<type>"+outputDB.getConnType()+"</type>" +
	            "<access>"+outputDB.getAccessType()+"</access>" + 
	            "<database>"+outputDB.getDbName()+"</database>" +
	            "<port>"+outputDB.getDbPort()+"</port>" +
	            "<username>"+outputDB.getUserName()+"</username>" +
	            "<password>"+outputDB.getPassword()+"</password>" +
	            "</connection>"
		};
		return databasesXML;
	}
	
	public String[] generateField(String fields){
		String[] strs = fields.split(",");
		List<String> field = new ArrayList();
		for (String string : strs) {
			field.add(string);
		}
		String[] array = field.toArray(new String[field.size()]);
		return array;
	}
	
	public ExcelInputField[] anaylsisFile(String path) throws Exception{
		File excelFile = new File(path);
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
		HSSFSheet sheet = wb.getSheetAt(0);
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

}
