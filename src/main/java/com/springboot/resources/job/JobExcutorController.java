package com.springboot.resources.job;

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
	 */
	@RequestMapping(path="/trans", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public void runJob(@RequestBody JobScheduleDto dto){
		this.jobService.runJob(dto);
	}
	
	/**
	 * 生成作业调度文件
	 */
	@RequestMapping(path="/schedulefile", method=RequestMethod.POST, consumes="application/json;charset=UTF-8", produces="application/json;charset=UTF-8")
	@ResponseBody
	public void jobSchedulefile(@RequestBody JobDto dto){
		this.jobService.generateJobFile(dto);
	}
}
