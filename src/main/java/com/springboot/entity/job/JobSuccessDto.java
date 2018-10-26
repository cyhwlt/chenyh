package com.springboot.entity.job;

public class JobSuccessDto {
	private String successName;
	private boolean drawn;
	public String getSuccessName() {
		return successName;
	}
	public void setSuccessName(String successName) {
		this.successName = successName;
	}
	public boolean isDrawn() {
		return drawn;
	}
	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
	}
}
