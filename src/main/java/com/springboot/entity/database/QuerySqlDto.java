package com.springboot.entity.database;

public class QuerySqlDto {
	private DatabaseDto dbDto;
	private boolean containFields;
	private String tableName;
	public DatabaseDto getDbDto() {
		return dbDto;
	}
	public void setDbDto(DatabaseDto dbDto) {
		this.dbDto = dbDto;
	}
	public boolean isContainFields() {
		return containFields;
	}
	public void setContainFields(boolean containFields) {
		this.containFields = containFields;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}
