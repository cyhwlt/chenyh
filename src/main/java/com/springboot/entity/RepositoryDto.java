package com.springboot.entity;

public class RepositoryDto {
	private String name;
	private String decrible;
	private String repositoryType;
	private KettleDatabaseRepositoryDto kdrDto;
	
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
	public KettleDatabaseRepositoryDto getKdrDto() {
		return kdrDto;
	}
	public void setKdrDto(KettleDatabaseRepositoryDto kdrDto) {
		this.kdrDto = kdrDto;
	}
}
