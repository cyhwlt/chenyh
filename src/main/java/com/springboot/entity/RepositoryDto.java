package com.springboot.entity;

/**
 * 创建资源库参数实体类
 * @author 96257
 *
 */
public class RepositoryDto {
	private String name;
	private String decrible;
	private String repositoryType;
	private DatabaseDto dbDto;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDecrible() {
		return decrible;
	}
	public void setDecrible(String decrible) {
		this.decrible = decrible;
	}
	public String getRepositoryType() {
		return repositoryType;
	}
	public void setRepositoryType(String repositoryType) {
		this.repositoryType = repositoryType;
	}
	public DatabaseDto getDbDto() {
		return dbDto;
	}
	public void setDbDto(DatabaseDto dbDto) {
		this.dbDto = dbDto;
	}
	
}
