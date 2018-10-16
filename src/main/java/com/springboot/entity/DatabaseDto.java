package com.springboot.entity;

/**
 * DB连接参数
 * @author 96257
 *
 */
public class DatabaseDto {
	private String connName; //显示名
	private String connType; //连接类型
	private String accessType; //连接方式
	private String hostName; //主机名称
	private String dbName; //数据库名称
	private String dbPort; //数据库端口
	private String userName; //数据库登录名
	private String password; //登陆密码
	private String dbEncodingKey; //设置数据库编码命名参数
	private String dbEncodingValue; //设置数据库编码值
	public String getConnName() {
		return connName;
	}
	public void setConnName(String connName) {
		this.connName = connName;
	}
	public String getConnType() {
		return connType;
	}
	public void setConnType(String connType) {
		this.connType = connType;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getDbPort() {
		return dbPort;
	}
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
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
	public String getDbEncodingKey() {
		return dbEncodingKey;
	}
	public void setDbEncodingKey(String dbEncodingKey) {
		this.dbEncodingKey = dbEncodingKey;
	}
	public String getDbEncodingValue() {
		return dbEncodingValue;
	}
	public void setDbEncodingValue(String dbEncodingValue) {
		this.dbEncodingValue = dbEncodingValue;
	}
	
}
