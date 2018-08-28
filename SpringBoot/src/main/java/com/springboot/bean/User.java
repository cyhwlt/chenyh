package com.springboot.bean;

import java.util.Date;

public class User {
	private int id;
	private String username;
	private int age;
	private Date ctm;
 
	public User() {
	}
 
	public User(String username, int age) {
		this.username = username;
		this.age = age;
		this.ctm = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getCtm() {
		return ctm;
	}

	public void setCtm(Date ctm) {
		this.ctm = ctm;
	}
	
	

}
