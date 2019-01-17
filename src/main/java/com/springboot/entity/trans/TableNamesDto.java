package com.springboot.entity.trans;

public class TableNamesDto {
	//字段选择
	private String stepName; //步骤名称
	private String[] fieldName; //字段名称
	
	//表输入
	private TableInputDto tiDto;
	
	//复制记录到结果
	private String resultStepName; //步骤名称

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String[] getFieldName() {
		return fieldName;
	}

	public void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}

	public TableInputDto getTiDto() {
		return tiDto;
	}

	public void setTiDto(TableInputDto tiDto) {
		this.tiDto = tiDto;
	}

	public String getResultStepName() {
		return resultStepName;
	}

	public void setResultStepName(String resultStepName) {
		this.resultStepName = resultStepName;
	}
	
}
