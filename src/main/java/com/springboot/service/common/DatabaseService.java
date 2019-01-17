package com.springboot.service.common;

import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.springframework.stereotype.Service;

import com.springboot.entity.database.DatabaseDto;
import com.springboot.entity.trans.TableInputDto;

@Service
public class DatabaseService {
	
	public String[] generateDBxml(DatabaseDto dbDto){
		String[] databasesXML = {
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	            "<connection>" +
	            "<name>"+dbDto.getConnName()+"</name>" +
	            "<server>"+dbDto.getHostName()+"</server>" +
	            "<type>"+dbDto.getConnType()+"</type>" +
	            "<access>"+dbDto.getAccessType()+"</access>" + 
	            "<database>"+dbDto.getDbName()+"</database>" +
	            "<port>"+dbDto.getDbPort()+"</port>" +
	            "<username>"+dbDto.getUserName()+"</username>" +
	            "<password>"+dbDto.getPassword()+"</password>" +
	            "</connection>"
		};
		return databasesXML;
	}

	public StepMeta tableInput(TransMeta transMeta, TableInputDto dto, PluginRegistry registryID) {
		TableInputMeta tiMeta = new TableInputMeta();
		String tiPluginId = registryID.getPluginId(StepPluginType.class, tiMeta);
		//给表输入添加一个DatabaseMeta连接数据库
		DatabaseMeta db_chenyh00 = transMeta.findDatabase(dto.getDbDto().getConnName());
		tiMeta.setDatabaseMeta(db_chenyh00);
//		String inSql = "SELECT + " + dto.getFields() + " FROM " + dto.getInputTableName();
		String sql = dto.getSql();
		tiMeta.setSQL(sql);
		// 添加tiMeta到转换中
		StepMeta tims = new StepMeta(tiPluginId, "表输入", tiMeta);
		tims.setDraw(true); 
		tims.setLocation(100, 100);
		return tims;
		
	}
}
