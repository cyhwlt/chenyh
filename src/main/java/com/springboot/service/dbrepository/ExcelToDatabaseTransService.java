package com.springboot.service.dbrepository;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.excelinput.ExcelInputField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.database.DatabaseDto;
import com.springboot.entity.database.ElasticsearchDto;
import com.springboot.entity.database.SQLDto;
import com.springboot.entity.excel.ExcelInputDto;
import com.springboot.entity.excel.ExcelToDBDto;
import com.springboot.entity.excel.SheetDto;
import com.springboot.service.common.CommonService;
import com.springboot.service.excel.ExcelService;

@Service
public class ExcelToDatabaseTransService {
	
	private static Logger logger = LogManager.getLogger(ExcelToDatabaseTransService.class);
	private static PreparedStatement ps;
	private static Connection conn;
	private static ResultSetMetaData metaData;
	public String RES_DIR = "res";
	
	@Autowired
	private CommonService comService;
	
	@Autowired
	private ExcelService xlsService;
	
	public Map<String, Object> excelToDatabase(ExcelToDBDto dto){
		Map<String, Object> returnValue = new HashMap();
		try {
			TransMeta generateTrans = this.generateKtr(dto);
			String xml = generateTrans.getXML();
			String transName = dto.getTransName();
			File file = new File(transName);
			FileUtils.writeStringToFile(file, xml, "UTF-8");
			returnValue.put("code", 0);
			returnValue.put("message", "");
			returnValue.put("data", xml);
		} catch (Exception e) {
			returnValue.put("code", -1);
			returnValue.put("message", e.getMessage());
			returnValue.put("data", null);
			logger.error(e.getMessage());
		}
		return returnValue;
	}
	
	public Map<String, Object> exceltoES(ExcelToDBDto dto) {
		Map<String, Object> returnValue = new HashMap();
		try {
			TransMeta generateTrans = this.generateExceltoESKtr(dto);
			String xml = generateTrans.getXML();
			String transName = dto.getTransName();
			File file = new File(transName);
			FileUtils.writeStringToFile(file, xml, "UTF-8");
			returnValue.put("code", 0);
			returnValue.put("message", "");
			returnValue.put("data", xml);
		} catch (Exception e) {
			returnValue.put("code", -1);
			returnValue.put("message", e.getMessage());
			returnValue.put("data", null);
			logger.error(e.getMessage());
		}
		return returnValue;
	}
	
	public TransMeta generateExceltoESKtr(ExcelToDBDto dto){
		TransMeta transMeta = new TransMeta();
		transMeta.setName(dto.getTransName());
		
		PluginRegistry registryID = PluginRegistry.getInstance();
		StepMeta excelInputStep = this.comService.excelInput(dto, registryID);
		StepMeta esBulkInsertStep = this.comService.esBulkInsert(dto.getEsDB(), registryID);
		
		transMeta.addStep(excelInputStep);
		transMeta.addStep(esBulkInsertStep);
		
		transMeta.addTransHop(new TransHopMeta(excelInputStep, esBulkInsertStep));
		logger.info("***********the end*************");
		return transMeta;
	}
	
	
	/**
	 * 1.excel输入
	 * 2.表输出
	 * 注：二维excel使用数据字典设计
	 * @throws Exception 
	 */
	public TransMeta generateKtr(ExcelToDBDto dto) throws Exception {
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
		StepMeta dbOutputStep = this.comService.tableOutput(transMeta, dto, registryID);
		StepMeta excelInputStep = this.comService.excelInput(dto, registryID);
		//添加到转换步骤
		transMeta.addStep(dbOutputStep);
		transMeta.addStep(excelInputStep); 
		
		//添加hop将两个步骤关联起来
		transMeta.addTransHop(new TransHopMeta(excelInputStep, dbOutputStep));
		logger.info("**********the end***********");
		return transMeta;
		
	}
	
	public String[] generateDBxml(DatabaseDto outputDB){
		String databaseXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<connection>" +
			"<name>"+outputDB.getConnName()+"</name>" +
			"<server>"+outputDB.getHostName()+"</server>" +
			"<type>"+outputDB.getConnType()+"</type>" +
			"<access>"+outputDB.getAccessType()+"</access>" + 
			"<database>"+outputDB.getDbName()+"</database>" +
			"<port>"+outputDB.getDbPort()+"</port>" +
			"<username>"+outputDB.getUserName()+"</username>" +
			"<password>"+outputDB.getPassword()+"</password>";
		StringBuilder sb = new StringBuilder(databaseXML);
		if(outputDB.getDbEncodingKey() != null && outputDB.getDbEncodingValue() != null){
            sb.append("<attributes>" + "<attribute>" +
            "<code>EXTRA_OPTION_"+outputDB.getDbEncodingKey()+".characterEncoding</code>" +
            "<attribute>" + outputDB.getDbEncodingValue() + "</attribute>" +
            "</attribute>" + "</attributes>" + "</connection>");
		}
		String[] databasesXML = {
	            sb.toString()
		};
		return databasesXML;
	}
	
	// excel——>数据库表
	public Map<String, Object> sql(SQLDto sqlDto){
		Connection conn = getConnection(sqlDto.getOutdbDto());
		Map<String, Object> returnValue = new HashMap();
		try{
			String sql = null;
			//判断表是否存在
			boolean exitTable = exitTable(sqlDto.getOutputTabName());
			if(exitTable){ //表已存在
				logger.info("没有SQL需要执行");
			}else{ //表不存在 从excel里获取字段，拼接createsql
				switch(sqlDto.getTransType()){
				case dbTodb:
					sql = dbTodb(sqlDto);
					break;
				case excelTodb:
					sql = excelTodb(sqlDto);
					break;
				}
				// 设置字符集
				sql += ");";
				logger.info("建表语句：" + sql);
				ps = conn.prepareStatement(sql);
			    ps.executeUpdate(sql);
			}
			returnValue.put("code", 0);
			returnValue.put("message", "");
			returnValue.put("data", sql);
		} catch(Exception e){
			returnValue.put("code", -1);
			returnValue.put("message", e.getMessage());
			returnValue.put("data", null);
			logger.error(e.getMessage());
		} finally{
			  try {
				ps.close();
				conn.close();  //关闭数据库连接
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}
	
	//获取数据库连接
	public Connection getConnection(DatabaseDto dto){
		String dbName = dto.getDbName();
		String dbPort = dto.getDbPort();
		String hostName = dto.getHostName();
		String userName = dto.getUserName();
		String password = dto.getPassword();
		try {
			switch(dto.getConnType()){
			case "MySQL":
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://"+hostName+":"+dbPort+"/"+dbName+"", userName, password);
				break;
			case "HIVE2":
				Class.forName("org.apache.hive.jdbc.HiveDriver");
				conn = DriverManager.getConnection("jdbc:hive2://"+hostName+":"+dbPort+"/"+dbName+"", userName, password);
				break;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return conn;
	}
	
	private boolean exitTable(String tableName){
		 boolean flag = false;
//	     conn = getConnection();  // 首先要获取连接，即连接到数据库
	     try {
	       String sql = "select * from "+tableName+";";
	       //预处理SQL 防止注入
	       ps = conn.prepareStatement(sql);
	       //执行
	       flag = ps.execute();
	       //关闭流
	       ps.close();
//	       conn.close();  //关闭数据库连接
	     } catch (SQLException e) {
	    	 logger.info("ExcelToDatabaseException:" + e.getMessage());
	     }
	    return flag;
	}
	
	public List<Map<String, String>> getColumn(String tableName){
		List<Map<String, String>> list = new ArrayList();
		String sql = "select * from " + tableName;
		try {
			ps = conn.prepareStatement(sql);
			metaData = ps.getMetaData();
			int columnCount = metaData.getColumnCount();
			for(int i=0; i<columnCount; i++){
				Map<String, String> map = new HashMap();
				map.put(metaData.getColumnName(i+1), metaData.getColumnTypeName(i+1));
				list.add(map);
			}
		} catch (SQLException e) {
			logger.error("获取表字段类型异常：" + e.getMessage());
		} finally {
			if (ps != null) {
				try {
					ps.close();
					conn.close();
				} catch (SQLException e) {
					logger.error("getColumnNames close pstem and connection failure", e.getMessage());
				}
			}
		}
		return list;
	}
	
	//excel入库时表不存在生成表
	public String excelTodb(SQLDto dto) throws Exception{
		String sql = "create table " + dto.getOutputTabName() + "(";
//		ExcelInputField[] excelFields = this.xlsService.analysisFile(dto.getExcelDto().getFilePath(), dto.getExcelDto().getSheetNumber());
		ExcelInputField[] excelFields = null;
		int length = excelFields.length;
		if(excelFields != null && length > 0){
			for(int i=0; i<length; i++){
				sql += excelFields[i].getName();
				switch(excelFields[i].getTypeDesc()){
				case "String":
					sql += " varchar(256)";
					break;
				default:
					sql += " TINYTEXT";
				}
				if (i == 0) {
					sql += " primary key";
				}
				if (i < length-1) {
					sql +=",";
				}
			}
		}
		return sql;
	}
	
	//表到表入库时表不存在生成表
	public String dbTodb(SQLDto dto){
		String sql = "create table " + dto.getOutputTabName() + "(";
		// 连接输入数据库获取表字段
		conn = getConnection(dto.getIndbDto());
		List<Map<String, String>> column = getColumn(dto.getInputTabName());
		//创建语句
		if(!column.isEmpty() && column != null){
			for(int i=0; i<column.size(); i++){
				for(String key: column.get(i).keySet()){
					sql += key; 
					if(i == 0){
						if(column.get(i).get(key).equalsIgnoreCase("varchar")){
							sql +=  " varchar(255) primary key";
						} else {
							sql += " " + column.get(i).get(key) + " primary key";
						}
					} else {
						if(column.get(i).get(key).equalsIgnoreCase("varchar")){
							sql += " varchar(255)";
						}else{
							sql += " " + column.get(i).get(key);
						}
					}
					if(i < column.size()-1){
						sql += ",";
					}
				}
			}
		}
		return sql;
	}

	public void excelBulktoES(ExcelToDBDto dto, List<String> sheetNames) throws Exception {
		//遍历excel的sheet
		int count = 0;
		for (String sheetName : sheetNames) {
			//重组excel的数据
			ExcelToDBDto excelDto = new ExcelToDBDto();
			SheetDto sheet = new SheetDto();
			sheet.setSheetName(sheetName);
			Map<String, String> fields = (Map<String, String>) this.xlsService.analysisFile(dto.getFilePath(), count).get("data");
			Set<String> keySet = fields.keySet();
			Iterator<String> iterator = keySet.iterator();
			List<ExcelInputDto> eiDtos = new ArrayList();
			while(iterator.hasNext()){
				String next = iterator.next();
				ExcelInputDto eiDto = new ExcelInputDto();
				eiDto.setName(next);
				eiDto.setTypeDesc(fields.get(next));
				eiDtos.add(eiDto);
			}
			sheet.setDtos(eiDtos);
			excelDto.setSheets(sheet);
			excelDto.setTransName("res/exceltoes_" + count + ".ktr");
			// 修改
			ElasticsearchDto esDto = dto.getEsDB();
			esDto.setType(sheetName);
			excelDto.setEsDB(dto.getEsDB());
			excelDto.setFilePath(dto.getFilePath());
			if(count > 0){
				int count1 = count;
				File file = new File("res/exceltoes_" + --count1 + ".ktr");
				file.delete();
			}
			//生成转换文件
			TransMeta generateTrans = this.generateExceltoESKtr(excelDto);
			String xml = generateTrans.getXML();
			String transName = excelDto.getTransName();
			File file = new File(transName);
			FileUtils.writeStringToFile(file, xml, "UTF-8");
			
			// 执行转换文件
			String fullFileName = null;
			String property = System.getProperty("user.dir");
			String separator = File.separator;
			fullFileName = property + separator + RES_DIR;
			fullFileName += "\\" + "exceltoes_" + count + ".ktr";
			TransMeta meta = new TransMeta(fullFileName);
			Trans trans = new Trans(meta);
			trans.execute(null);
			trans.waitUntilFinished();
			count++;
		}
		File file = new File("res/exceltoes_" + --count + ".ktr");
		file.delete();
	}
}
