package com.etc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.etc.*")
@MapperScan(value = "com.etc.mapper") //找到mapper文件
public class SpringBootBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBaseApplication.class, args);
		System.out.println("启动完成");
	}

}
