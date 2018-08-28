package com.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.bean.User;
import com.springboot.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserMapper userMapper;
 
	@Override
	public User getUserById(Integer id) {
		return userMapper.getUserById(id);
	}
 
	@Override
	public List<User> getUserList() {
		return userMapper.getUserList();
	}
 
	@Override
	public int add(User user) {
		return userMapper.add(user);
	}
 
	@Override
	public int update(Integer id, User user) {
		return userMapper.update(id, user);
	}
 
	@Override
	public int delete(Integer id) {
		return userMapper.delete(id);
	}

}
