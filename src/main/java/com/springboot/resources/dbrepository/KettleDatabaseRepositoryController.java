package com.springboot.resources.dbrepository;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.entity.database.DatabaseDto;
import com.springboot.entity.database.QuerySqlDto;
import com.springboot.resources.repository.RepositoryController;
import com.springboot.service.dbrepository.KettleDatabaseRepositoryService;
import com.springboot.util.DBUtil;
import com.springboot.util.JsonUtil;

@Controller
@RequestMapping("/database")
public class KettleDatabaseRepositoryController {
	
	private static Logger logger = LogManager.getLogger(RepositoryController.class);
	
	@Autowired
	private KettleDatabaseRepositoryService kdrService;
	
	/**
	 * 测试连接数据库
	 * @param dto
	 * @return
	 */
	@RequestMapping(path="/connect", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public String testDatabaseConnection(@RequestBody DatabaseDto dto){
		DBUtil dbUtil = new DBUtil();
		DatabaseMeta database = dbUtil.transDatabaseMetaFromObject(dto);
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
	public String confirm(@RequestBody DatabaseDto dto) throws Exception{
		return this.kdrService.getTables(dto);
	}
	
	/**
	 * 获取SQL查询语句
	 */
	@RequestMapping(path="/querysql", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public String getSql(@RequestBody QuerySqlDto dto){
		String sql = this.kdrService.getSql(dto);
		return JsonUtil.objectToJson(sql);
	}
}
