package com.springboot.entity.job;

public class JobTransDto {
	private String transName;
	private String fileName;
	private String description;
	private boolean drawn;
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isDrawn() {
		return drawn;
	}
	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
	}
}
