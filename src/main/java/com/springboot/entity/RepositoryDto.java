package com.springboot.entity;

public class RepositoryDto {
	private String connName;
	private String hostName;
	private String databaseName;
	private String databasePort;
	private String userName;
	private String password;
	private String connType;
	private String connWay;
	public String getConnName() {
		return connName;
	}
	public void setConnName(String connName) {
		this.connName = connName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getDatabasePort() {
		return databasePort;
	}
	public void setDatabasePort(String databasePort) {
		this.databasePort = databasePort;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConnType() {
		return connType;
	}
	public void setConnType(String connType) {
		this.connType = connType;
	}
	public String getConnWay() {
		return connWay;
	}
	public void setConnWay(String connWay) {
		this.connWay = connWay;
	}
	
	
}
