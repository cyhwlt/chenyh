package com.springboot.entity;

public class DBToDBDto {
	private String transName; //转换名称
	private String inputTableName; //输入源表名
	private String outputTableName; //输出源表名
	private String fields; //表字段
	private DatabaseDto inputDB; // 作为输入源的数据库
	private DatabaseDto outputDB; // 作为输出源的数据库
//	private String filePath; // excel文件路径

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getInputTableName() {
		return inputTableName;
	}

	public void setInputTableName(String inputTableName) {
		this.inputTableName = inputTableName;
	}

	public String getOutputTableName() {
		return outputTableName;
	}

	public void setOutputTableName(String outputTableName) {
		this.outputTableName = outputTableName;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public DatabaseDto getInputDB() {
		return inputDB;
	}

	public void setInputDB(DatabaseDto inputDB) {
		this.inputDB = inputDB;
	}

	public DatabaseDto getOutputDB() {
		return outputDB;
	}

	public void setOutputDB(DatabaseDto outputDB) {
		this.outputDB = outputDB;
	}
}
