package com.springboot.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@RequestMapping("/hive")
public class ConnectHive {
	
	@RequestMapping(path="/test", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public void test() {
		String driverName = "org.apache.hive.jdbc.HiveDriver";
		try {
			Class.forName(driverName).newInstance();
			Connection con = DriverManager.getConnection("jdbc:hive2://172.16.101.29:10000/default", "root", "0");
			Statement stmt = con.createStatement();
			String sql = "show tables";
			System.out.println("Running:" + sql);
			ResultSet res = stmt.executeQuery(sql);
			System.out.println("执行结果：");
			if (res.next()) {
				System.out.println(res.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
}
