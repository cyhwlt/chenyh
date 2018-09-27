package com.springboot.entity;

import java.util.List;

public class ExcelToDBDto {
	private String transName; //转换名称
	private String outputTableName; //输出源表名
	private DatabaseDto outputDB; // 作为输出源的数据库
	private String filePath; // excel文件路径
	private String fields; //表字段
	private SheetDto sheets; // excel文件的列名
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	public String getOutputTableName() {
		return outputTableName;
	}
	public void setOutputTableName(String outputTableName) {
		this.outputTableName = outputTableName;
	}
	public DatabaseDto getOutputDB() {
		return outputDB;
	}
	public void setOutputDB(DatabaseDto outputDB) {
		this.outputDB = outputDB;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	public SheetDto getSheets() {
		return sheets;
	}
	public void setSheets(SheetDto sheets) {
		this.sheets = sheets;
	}
		
}
