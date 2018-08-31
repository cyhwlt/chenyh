package com.springboot.initresources;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 随项目启动而加载的方法可放在该类
 * @author 96257
 *
 */
@Component
public class MystartupRunner implements CommandLineRunner{

	@Override
	public void run(String... args) throws Exception {
		System.out.println("<<<<<<<<<<<<<<服务启动执行加载数据等操作<<<<<<<<<<<<");	
	}

}
