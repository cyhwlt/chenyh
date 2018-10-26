package com.springboot.entity.database;

import com.springboot.entity.excel.ExcelAnalysisDto;
import com.springboot.enums.TransType;

public class SQLDto {
	private ExcelAnalysisDto excelDto;
	private DatabaseDto indbDto;
	private DatabaseDto outdbDto;
	private String inputTabName;
	private String outputTabName;
	private TransType transType;
	public ExcelAnalysisDto getExcelDto() {
		return excelDto;
	}
	public void setExcelDto(ExcelAnalysisDto excelDto) {
		this.excelDto = excelDto;
	}
	
	public DatabaseDto getIndbDto() {
		return indbDto;
	}
	public void setIndbDto(DatabaseDto indbDto) {
		this.indbDto = indbDto;
	}
	public DatabaseDto getOutdbDto() {
		return outdbDto;
	}
	public void setOutdbDto(DatabaseDto outdbDto) {
		this.outdbDto = outdbDto;
	}
	public String getInputTabName() {
		return inputTabName;
	}
	public void setInputTabName(String inputTabName) {
		this.inputTabName = inputTabName;
	}
	public String getOutputTabName() {
		return outputTabName;
	}
	public void setOutputTabName(String outputTabName) {
		this.outputTabName = outputTabName;
	}
	public TransType getTransType() {
		return transType;
	}
	public void setTransType(TransType transType) {
		this.transType = transType;
	}
	
	
}
