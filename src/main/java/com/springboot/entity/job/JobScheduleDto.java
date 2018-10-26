package com.springboot.entity.job;

public class JobScheduleDto {
	private String logPath;
	private String kjbPath;
	public String getLogPath() {
		return logPath;
	}
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	public String getKjbPath() {
		return kjbPath;
	}
	public void setKjbPath(String kjbPath) {
		this.kjbPath = kjbPath;
	}
}
