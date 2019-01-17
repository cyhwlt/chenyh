package com.springboot.entity.trans;

public class SetVariableDto {
	// 从结果获取记录
	private String stepName;
	private String fieldName;
	private String fieldType;
	
	// 设置变量
	private String svStepName;
	private String svFieldName;
	private String variableName;
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getSvStepName() {
		return svStepName;
	}
	public void setSvStepName(String svStepName) {
		this.svStepName = svStepName;
	}
	public String getSvFieldName() {
		return svFieldName;
	}
	public void setSvFieldName(String svFieldName) {
		this.svFieldName = svFieldName;
	}
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
	
}
