package com.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.springboot.bean.User;

@Mapper
public interface UserMapper {
	User getById(Integer id);
	
	Integer addUser(User user);
	
	List<User> findAll();
}
