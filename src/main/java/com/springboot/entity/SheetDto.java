package com.springboot.entity;

import java.util.List;

public class SheetDto {
	private List<ExcelInputDto> dtos;
	private String sheetName;
	private String primaryKey;
	
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public List<ExcelInputDto> getDtos() {
		return dtos;
	}
	public void setDtos(List<ExcelInputDto> dtos) {
		this.dtos = dtos;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
}
