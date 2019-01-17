package com.springboot.entity.trans;

import com.springboot.entity.database.DatabaseDto;

public class TableInputDto {
	private String stepName; //步骤名称
	private String sql; //sql语句
	private DatabaseDto dbDto; //数据库连接
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public DatabaseDto getDbDto() {
		return dbDto;
	}
	public void setDbDto(DatabaseDto dbDto) {
		this.dbDto = dbDto;
	}
	
	
}
