package com.springboot.resources.dbrepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.entity.DatabaseDto;
import com.springboot.resources.repository.RepositoryController;
import com.springboot.util.JsonUtil;

@Controller
@RequestMapping("/database")
public class KettleDatabaseRepositoryController {
	
	private static Logger logger = LogManager.getLogger(RepositoryController.class);
	
	/**
	 * 测试连接数据库
	 * @param dto
	 * @return
	 */
	@RequestMapping(path="/connect", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public String testDatabaseConnection(@RequestBody DatabaseDto dto){
		DatabaseMeta database = transDatabaseMetaFromObject(dto);
		String testConnection = database.testConnection();
		return JsonUtil.objectToJson(testConnection);
	}
	
	/**
	 * 连接成功以后获取库里的表
	 * @param dto
	 * @return
	 * @throws KettleDatabaseException
	 * @throws SQLException
	 */
	@RequestMapping(path="/confirm", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public String confirm(@RequestBody DatabaseDto dto) throws KettleDatabaseException, SQLException{
		DatabaseMeta dbMeta = transDatabaseMetaFromObject(dto);
		Database db = new Database(null, dbMeta);
		db.connect();
		String sql = "select TABLE_NAME from information_schema.tables where TABLE_SCHEMA='" + dto.getDbName() + "'";
		ResultSet resultSet = db.openQuery(sql);
		List<String> list = new ArrayList<String>();
		while(resultSet.next()){
			String tableName = resultSet.getString("TABLE_NAME");
			list.add(tableName);
		}
		return JsonUtil.objectToJson(list);
	}
	
	private DatabaseMeta transDatabaseMetaFromObject(DatabaseDto dto){
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
