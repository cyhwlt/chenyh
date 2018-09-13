package com.springboot.entity;

import java.util.List;

import org.pentaho.di.repository.RepositoryMeta;

public class RepositoryModelDto {
	private String username;
	private String password;
	private List<RepositoryMeta> availableRepositories;
	private RepositoryMeta selectedRepository;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<RepositoryMeta> getAvailableRepositories() {
		return availableRepositories;
	}
	public void setAvailableRepositories(List<RepositoryMeta> availableRepositories) {
		this.availableRepositories = availableRepositories;
	}
	public RepositoryMeta getSelectedRepository() {
		return selectedRepository;
	}
	public void setSelectedRepository(RepositoryMeta selectedRepository) {
		this.selectedRepository = selectedRepository;
	}
	
	
}
