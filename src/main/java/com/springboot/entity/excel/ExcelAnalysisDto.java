package com.springboot.entity.excel;

/**
 * 解析excel文件的实体
 * @author 96257
 *
 */
public class ExcelAnalysisDto {
	private String filePath; // excel文件路径
	private int sheetNumber; // sheet下标
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getSheetNumber() {
		return sheetNumber;
	}
	public void setSheetNumber(int sheetNumber) {
		this.sheetNumber = sheetNumber;
	}
	
	
}
