package com.springboot.service.dbrepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.springframework.stereotype.Service;

import com.springboot.entity.DatabaseDto;
import com.springboot.entity.KtrDto;

@Service
public class KettleDatabaseRepositoryService {
	
	private static Logger logger = LogManager.getLogger(KettleDatabaseRepositoryService.class); 
	public String RES_DIR = "res";
	
	public void runTrans(String fileName) throws KettleException{
		String fullFileName = null;
		String property = System.getProperty("user.dir");
		String separator = File.separator;
		fullFileName = property + separator + RES_DIR;
		fullFileName += "\\" + fileName;
		TransMeta meta = new TransMeta(fullFileName);
		Trans trans = new Trans(meta);
		trans.execute(null);
		trans.waitUntilFinished();
		if(trans.getErrors()>0){
			logger.error("有异常");
		}
	}
	
	public void generateKtr(KtrDto dto) throws KettleException{
		try {
			TransMeta generateTrans = this.generateTrans(dto);
			String xml = generateTrans.getXML();
//			String transName = "generatektr_cyh.ktr";
			String transName = dto.getTransName();
			File file = new File(transName);
			FileUtils.writeStringToFile(file, xml, "UTF-8");
		} catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	//数据库表数据之间的转换
	public TransMeta generateTrans(KtrDto dto) throws KettleXMLException{
		TransMeta transMeta = new TransMeta();
		// 生成databasesXML
		String[] databasesXML = generateDBxml(dto.getInputDB(), dto.getOutputDB());
		// 设置转换的名称
		transMeta.setName(dto.getTransName());
		// 添加转换的数据库连接
		for(int i=0; i<databasesXML.length; i++){
			DatabaseMeta dbMeta = new DatabaseMeta(databasesXML[i]);
			transMeta.addDatabase(dbMeta);
		}
		// registry是给每个步骤生成一个标识ID
		PluginRegistry registryID = PluginRegistry.getInstance();
		//第一步表输入
		TableInputMeta tiMeta = new TableInputMeta();
		String tiPluginId = registryID.getPluginId(StepPluginType.class, tiMeta);
		//给表输入添加一个DatabaseMeta连接数据库
//		DatabaseMeta db_chenyh00 = transMeta.findDatabase("chenyh001");
		DatabaseMeta db_chenyh00 = transMeta.findDatabase(dto.getInputDB().getConnName());
		tiMeta.setDatabaseMeta(db_chenyh00);
//		String inSql = "SELECT id, name, program, grade FROM teachar";
		String inSql = "SELECT + " + dto.getFields() + " FROM " + dto.getInputTableName();
		tiMeta.setSQL(inSql);
		// 添加tiMeta到转换中
		StepMeta tims = new StepMeta(tiPluginId, "表输入", tiMeta);
		tims.setDraw(true);
		tims.setLocation(100, 100);
		transMeta.addStep(tims);
		
		//第二步插入与更新
		InsertUpdateMeta insertMeta = new InsertUpdateMeta();
		String iuPluginId = registryID.getPluginId(StepPluginType.class, insertMeta);
		//添加数据库连接
//		DatabaseMeta db_chenyh = transMeta.findDatabase("chenyh002");
		DatabaseMeta db_chenyh = transMeta.findDatabase(dto.getOutputDB().getConnName());
		insertMeta.setDatabaseMeta(db_chenyh);
		//设置操作的表
//		insertMeta.setTableName("teachar1");
		insertMeta.setTableName(dto.getOutputTableName());
		
		//设置用来查询的关键字
		insertMeta.setKeyLookup(new String[]{"ID"});
		insertMeta.setKeyStream(new String[]{"ID"});
		insertMeta.setKeyStream2(new String[]{""});
		insertMeta.setKeyCondition(new String[]{"="});
		
		// 生成要更新的字段
		String[] update = generateField(dto.getFields());
		//设置要更新的字段
//		String[] updateLookup = {"id","name","program","grade"};
//		String[] updateStream = {"id","name","program","grade"};
		Boolean[] updateOrNot = {false, true, true, true, true, true, true};
		insertMeta.setUpdateLookup(update);
		insertMeta.setUpdateStream(update);
		insertMeta.setUpdate(updateOrNot);
//		String[] lookup = insertMeta.getUpdateLookup();
		insertMeta.getUpdateLookup();
		
		//添加到转换中
		StepMeta insertStep = new StepMeta(iuPluginId, "表输出", insertMeta);
		insertStep.setDraw(true);
		insertStep.setLocation(250, 100);
		transMeta.addStep(insertStep);
		
		//添加hop将两个步骤关联起来
		transMeta.addTransHop(new TransHopMeta(tims, insertStep));
		logger.info("**********the end***********");
		return transMeta;
	}
	
	public String[] generateDBxml(DatabaseDto inputDB, DatabaseDto outputDB){
		String[] databasesXML = {
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	            "<connection>" +
	            "<name>"+inputDB.getConnName()+"</name>" +
	            "<server>"+inputDB.getHostName()+"</server>" +
	            "<type>"+inputDB.getConnType()+"</type>" +
	            "<access>"+inputDB.getAccessType()+"</access>" + 
	            "<database>"+inputDB.getDbName()+"</database>" +
	            "<port>"+inputDB.getDbPort()+"</port>" +
	            "<username>"+inputDB.getUserName()+"</username>" +
	            "<password>"+inputDB.getPassword()+"</password>" +
	            "</connection>",
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
}
