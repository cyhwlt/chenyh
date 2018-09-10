package com.springboot.resources;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.PreparedStatement;
import com.springboot.bean.Database;
import com.springboot.util.JsonUtil;

@Controller
@RequestMapping("/connection")
public class ConnectHiveController {

	private static Logger logger = LogManager.getLogger(ConnectHiveController.class);
	Connection conn;
	@RequestMapping("/hive")
	public void connectHive() {
		
	}
	
	@RequestMapping(path = "/mysql", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void connectMysql(@RequestBody Database database) {
		try {
			Class.forName(database.getDriver());
			conn = DriverManager.getConnection(database.getUrl(), database.getName(), database.getPassword());
			PreparedStatement pst = (PreparedStatement) conn.prepareCall("select * from user");
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				logger.info(rs.getString(1));
				logger.info(rs.getString(2));
				logger.info(rs.getString(3));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(path = "/confirm", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String ConfirmConnect() {
		StringBuffer sb = new StringBuffer();
		List<String> tables = new ArrayList<String>();
		try {
			DatabaseMetaData dbMetaData = conn.getMetaData();
			ResultSet rs = dbMetaData.getTables(null,  null, null, new String[] {"TABLE"});
			while(rs.next()) {
				sb.append("表名：" + rs.getString("TABLE_NAME"));
				sb.append("表所属数据库：" + rs.getString("TABLE_CAT"));
				sb.append("-------------------");
				tables.add(sb.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return JsonUtil.objectToJson(tables);
	}
}
