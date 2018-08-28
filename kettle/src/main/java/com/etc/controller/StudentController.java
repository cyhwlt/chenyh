package com.etc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etc.po.Student;
import com.etc.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private StudentService studentService;
	
	@RequestMapping("/findAll")
	@ResponseBody
	public String findAll() {
		System.out.println("进入查询方法");
		List<Student> list = studentService.findAll();
		for (Student student : list) {
			System.out.println("学生：" + student.toString());
		}
		return "findAll";
	}
	
	@RequestMapping("/insertStudent")
	@ResponseBody
	public String insertStudent(Student student) {
		System.out.println("进入添加方法");
		int result = this.studentService.insertStudent(student);
		if (result == 1) {
			System.out.println("添加学生信息成功");
		} else {
			System.out.println("添加学生信息失败");
		}
		return "insert";
	}
}
