package com.springboot.util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;

import com.springboot.entity.database.DatabaseDto;

public class DBUtil {
	
	public String getTableInDB(DatabaseDto dto){
		DatabaseMeta dbMeta = transDatabaseMetaFromObject(dto);
		Database db = new Database(null, dbMeta);
		List<String> list = new ArrayList<String>();
		try {
			db.connect();
			String sql = "select TABLE_NAME from information_schema.tables where TABLE_SCHEMA='" + dto.getDbName() + "'";
			ResultSet resultSet = db.openQuery(sql);
			while(resultSet.next()){
				String tableName = resultSet.getString("TABLE_NAME");
				list.add(tableName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JsonUtil.objectToJson(list);
	}
	
	public DatabaseMeta transDatabaseMetaFromObject(DatabaseDto dto){
		DatabaseMeta database = new DatabaseMeta();
		database.setName(dto.getConnName());
		database.setDisplayName(dto.getConnName());
		database.setDatabaseType(dto.getConnType());
		database.setAccessType(DatabaseMeta.getAccessType( dto.getAccessType() ) );
		database.setHostname(dto.getHostName());
		database.setDBName(dto.getDbName());
		database.setDBPort(dto.getDbPort());
		database.setUsername(dto.getUserName());
		database.setPassword(dto.getPassword());
		return database;
	}

}
