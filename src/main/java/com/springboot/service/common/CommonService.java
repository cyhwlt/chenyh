package com.springboot.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.elasticsearchbulk.ElasticSearchBulkMeta;
import org.pentaho.di.trans.steps.elasticsearchbulk.ElasticSearchBulkMeta.Server;
import org.pentaho.di.trans.steps.excelinput.ExcelInputField;
import org.pentaho.di.trans.steps.excelinput.ExcelInputMeta;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.springframework.stereotype.Service;

import com.springboot.entity.database.DBToDBDto;
import com.springboot.entity.database.ElasticsearchDto;
import com.springboot.entity.excel.ExcelInputDto;
import com.springboot.entity.excel.ExcelToDBDto;

@Service
public class CommonService {

	private static Logger logger = LogManager.getLogger(CommonService.class);
	
	//表输入
	public StepMeta tableInput(TransMeta transMeta, DBToDBDto dto, PluginRegistry registryID){
		// 表输入
		TableInputMeta tiMeta = new TableInputMeta();
		String tiPluginId = registryID.getPluginId(StepPluginType.class, tiMeta);
		//给表输入添加一个DatabaseMeta连接数据库
		DatabaseMeta db_chenyh00 = transMeta.findDatabase(dto.getInputDB().getConnName());
		tiMeta.setDatabaseMeta(db_chenyh00);
		String inSql = "SELECT " + dto.getFields() + " FROM " + dto.getInputTableName();
		tiMeta.setSQL(inSql);
		// 添加tiMeta到转换中
		StepMeta tims = new StepMeta(tiPluginId, "表输入", tiMeta);
		tims.setDraw(true); 
		tims.setLocation(100, 100);
		return tims;
	}
	
	//表输出
	public StepMeta tableOutput(TransMeta transMeta, ExcelToDBDto dto, PluginRegistry registryID){
		//1.设置表输出参数
		InsertUpdateMeta insertMeta = new InsertUpdateMeta();
		String iuPluginId = registryID.getPluginId(StepPluginType.class, insertMeta);
		
		//添加数据库连接
		DatabaseMeta db_chenyh = transMeta.findDatabase(dto.getOutputDB().getConnName());
		insertMeta.setDatabaseMeta(db_chenyh);
		//设置操作的表
		insertMeta.setTableName(dto.getOutputTableName());
		
		//设置用来查询的关键字
		insertMeta.setKeyLookup(new String[]{dto.getSheets().getPrimaryKey()});
		insertMeta.setKeyStream(new String[]{dto.getSheets().getPrimaryKey()});
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
		
		//生成转换步骤
		StepMeta insertStep = new StepMeta(iuPluginId, "表输出", insertMeta);
		insertStep.setDraw(true);
		insertStep.setLocation(250, 100);
		return insertStep;
	}
	
	//excel输入
	public StepMeta excelInput(ExcelToDBDto dto, PluginRegistry registryID){
		//2.设置excel输入参数
		ExcelInputMeta eiMeta = new ExcelInputMeta();
		// 读取excel文件，获取字段
		List<ExcelInputDto> sheets = dto.getSheets().getDtos();
		int size = sheets.size();
//				ExcelInputField[] fields = analysisFile(dto.getFilePath(),2);
		ExcelInputField[] fields = new ExcelInputField[size];
		int i = 0;
		for (ExcelInputDto sheet : sheets) {
			fields[i] = new ExcelInputField();
			fields[i].setName(sheet.getName());
			i++;
		}
		eiMeta.setField(fields);
		String[] fileName  = new String[]{dto.getFilePath()};
		String[] fileMask = {};
		String[] fileRequired = {"N"};
		eiMeta.setFileName(fileName);
//				eiMeta.setFileMask(fileMask);
//				eiMeta.setExcludeFileMask(fileMask);
		eiMeta.setFileRequired(fileRequired);
		eiMeta.setIncludeSubFolders(fileRequired);
//				eiMeta.setFileField("");
//				eiMeta.setSheetField("");
//				eiMeta.setSheetRowNumberField("");
//				eiMeta.setRowNumberField("");
		eiMeta.setAddResultFile(true);
		eiMeta.setStartsWithHeader(true);
		eiMeta.setIgnoreEmptyRows(true);
		eiMeta.setStopOnEmpty(false);
		eiMeta.setAcceptingFilenames(false);
//				eiMeta.setAcceptingField("");
//				eiMeta.setAcceptingStepName("");
//				eiMeta.allocate(1, 1, 1);
		String name = dto.getSheets().getSheetName();
		String[] sheetName = {name};
		eiMeta.setSheetName(sheetName);
		int[] row = {0};
		int[] col = {0};
		eiMeta.setStartColumn(col);
		eiMeta.setStartRow(row);
		eiMeta.setStrictTypes(false);
		eiMeta.setErrorIgnored(false);
		eiMeta.setErrorLineSkipped(false);
//				eiMeta.setWarningFilesDestinationDirectory("");
		eiMeta.setBadLineFilesExtension("waring");
//				eiMeta.setErrorFilesDestinationDirectory("");
		eiMeta.setErrorFilesExtension("error");
//				eiMeta.setLineNumberFilesDestinationDirectory("");
		eiMeta.setLineNumberFilesExtension("line");
//				eiMeta.setShortFileNameField("");
//				eiMeta.setPathField("");
//				eiMeta.setIsHiddenField("");
//				eiMeta.setLastModificationDateField("");
//				eiMeta.setUriField("");
//				eiMeta.setRootUriField("");
//				eiMeta.setSizeField("");
		
		String eiPluginId = registryID.getPluginId(StepPluginType.class, eiMeta); 
		return new StepMeta(eiPluginId,"excel输入",eiMeta);
	}
	//批量加载输出
	public StepMeta esBulkInsert(ElasticsearchDto dto, PluginRegistry registryID){
		// elasticsearch bulk insert
		ElasticSearchBulkMeta esbMeta = new ElasticSearchBulkMeta();
		esbMeta.setDefault();
		// general
		esbMeta.setIndex(dto.getIndex()); // 设置索引名称
		esbMeta.setType(dto.getType()); // 设置类型名
		// servers
		Server server = new Server();
		List<Server> servers = new ArrayList();
		server.address = dto.getServerAddress();
		server.port = dto.getServerPort();
		servers.add(server);
		esbMeta.setServers(servers);
		// fields
		Map<String, String> fields = new HashMap();
		esbMeta.setFieldsMap(fields);
		// settings
		Map<String, String> map = new HashMap();
		map.put("cluster.name", dto.getClusterName());
		esbMeta.setSettingsMap(map);
		
		esbMeta.setBatchSize("50000");
		esbMeta.setStopOnError(true);
		
		String esPluginId = registryID.getPluginId(StepPluginType.class, esbMeta);
		// 添加esbMeta到转换中
		StepMeta sm = new StepMeta(esPluginId, "批量加载ES", esbMeta);
		sm.setDraw(true);
		sm.setLocation(100, 100);
		return sm;
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
}
