package com.springboot.service;

import java.util.List;

import com.springboot.bean.User;

public interface UserService {
	List<User> findAll();
	
	void addUser(User user);
	
	void deleteUser(Integer id);
}
