package com.springboot.entity.job;

import java.util.List;

public class SerialJobDto {
	//start
	private JobStartDto startDto;
	//trans
	private List<JobTransDto> transDto;
	//success
	private List<JobSuccessDto> successDto;
	public JobStartDto getStartDto() {
		return startDto;
	}
	public void setStartDto(JobStartDto startDto) {
		this.startDto = startDto;
	}
	public List<JobTransDto> getTransDto() {
		return transDto;
	}
	public void setTransDto(List<JobTransDto> transDto) {
		this.transDto = transDto;
	}
	public List<JobSuccessDto> getSuccessDto() {
		return successDto;
	}
	public void setSuccessDto(List<JobSuccessDto> successDto) {
		this.successDto = successDto;
	}
}

//{
//	"jobName":"job024",
//	"fileName":"res\\job024.kjb",
//	"startDto":{
//		"startName":"START",
//		"start":true,
//		"repeat":false,
//		"schedulerType":1,
//		"intervalSeconds":10,
//		"intervalMinutes":0,
//		"hour":12,
//		"minutes":0,
//		"weekDay":1,
//		"dayofMonth":1,
//		"drawn":true,
//		"parallel":true
//	},
//	"transDto":[
//		{
//		"transName":"TRANS1",
//		"fileName":"res\\paneller01.ktr",
//		"description":"",
//		"drawn":true
//		},
//		{
//		"transName":"TRANS2",
//		"fileName":"res\\paneller02.ktr",
//		"description":"",
//		"drawn":true
//		}
//	],
//	"successDto":{
//		"successName":"SUCCESS",
//		"drawn":true
//	}
//}