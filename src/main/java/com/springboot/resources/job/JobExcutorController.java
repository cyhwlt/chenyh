package com.springboot.resources.job;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.entity.job.JobDto;
import com.springboot.entity.job.JobScheduleDto;
import com.springboot.service.job.JobService;

@Controller
@RequestMapping("/job")
public class JobExcutorController {

	@Autowired
	private JobService jobService;
	
	/**
	 * 启动作业调度
	 * @param dto
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(path="/trans", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public Map<String, Object> runJob(@RequestBody JobScheduleDto dto) throws Exception{
		return this.jobService.runJob(dto);
	}
	
	/**
	 * 生成作业调度文件
	 * @return 
	 */
	@RequestMapping(path="/schedulefile", method=RequestMethod.POST, consumes="application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> jobSchedulefile(@RequestBody JobDto dto){
		return this.jobService.generateJobFile(dto);
	}
	
	/**
	 * 1.获取所有表名称（out .ktr）
	 * 2.设置变量 （in .ktr1）
	 * 3.抽取数据（in .ktr2）
	 * 4.抽取数据作业（包含2.3）
	 * 5.库迁移作业（1,4）
	 * @throws Exception 
	 */
	@RequestMapping(path="/dbmigrate", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public void dbMigrate() throws Exception{
		this.jobService.dbMigrate();
	}
	
}
