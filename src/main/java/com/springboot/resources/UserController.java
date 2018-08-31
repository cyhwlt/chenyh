package com.springboot.resources;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.bean.User;
import com.springboot.service.UserService;
import com.springboot.util.JsonUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	private static Logger logger = LogManager.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(path="/getAllUser", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String getAllUser(){
        List<User> userList = userService.findAll();
        return JsonUtil.objectToJson(userList);
    }
	
	@RequestMapping(path="/addUser", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	@ResponseBody
	public String addUser(@RequestBody User u) {
		User user = new User();
		user.setUsername(u.getUsername());
		user.setUserage(u.getUserage());
		this.userService.addUser(user);
		return JsonUtil.objectToJson(user);
	}
	
	@RequestMapping(path="/removeUser/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public void deleteUser(@PathVariable("id") Integer id) {
		this.userService.deleteUser(id);
	}
}
