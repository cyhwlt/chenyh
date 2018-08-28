package com.springboot.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.bean.User;
import com.springboot.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/getAllUser")
    @ResponseBody
    public String getAllUser(){
        List<User> userList = userService.findAll();
        System.out.println("获取成功");
        return "获取成功";
    }
}
