package com.springboot.entity.excel;

import java.util.List;

/**
 * excel输入源实体
 * @author 96257
 *
 */
public class SheetDto {
	private List<ExcelInputDto> dtos;
	private String sheetName; // excel某个sheet的名称
	private String primaryKey; // 对应表的主键
	
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
