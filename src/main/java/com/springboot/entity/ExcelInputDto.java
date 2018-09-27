package com.springboot.entity;

public class ExcelInputDto {
	private String name;
	private int type;
	private int length;
	private int precision;
	private String format;
	private String currencySymbol;
	private String decimalSymbol;
	private String groupSymbol;
	private boolean repeated;
	private String typeDesc;
	private String trimTypeDesc;
	private int trimType;
	private String trimTypeCode;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getDecimalSymbol() {
		return decimalSymbol;
	}
	public void setDecimalSymbol(String decimalSymbol) {
		this.decimalSymbol = decimalSymbol;
	}
	public String getGroupSymbol() {
		return groupSymbol;
	}
	public void setGroupSymbol(String groupSymbol) {
		this.groupSymbol = groupSymbol;
	}
	public boolean isRepeated() {
		return repeated;
	}
	public void setRepeated(boolean repeated) {
		this.repeated = repeated;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public String getTrimTypeDesc() {
		return trimTypeDesc;
	}
	public void setTrimTypeDesc(String trimTypeDesc) {
		this.trimTypeDesc = trimTypeDesc;
	}
	public int getTrimType() {
		return trimType;
	}
	public void setTrimType(int trimType) {
		this.trimType = trimType;
	}
	public String getTrimTypeCode() {
		return trimTypeCode;
	}
	public void setTrimTypeCode(String trimTypeCode) {
		this.trimTypeCode = trimTypeCode;
	}
	
}
