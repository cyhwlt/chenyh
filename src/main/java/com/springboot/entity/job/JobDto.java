package com.springboot.entity.job;

public class JobDto {
	private String jobName;
	private String fileName;
	private boolean serial;
	private SerialJobDto serialDto;
	private ParallelJobDto parallelDto;
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public boolean isSerial() {
		return serial;
	}
	public void setSerial(boolean serial) {
		this.serial = serial;
	}
	public SerialJobDto getSerialDto() {
		return serialDto;
	}
	public void setSerialDto(SerialJobDto serialDto) {
		this.serialDto = serialDto;
	}
	public ParallelJobDto getParallelDto() {
		return parallelDto;
	}
	public void setParallelDto(ParallelJobDto parallelDto) {
		this.parallelDto = parallelDto;
	}
	
}

//,
//"parallelDto":{
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
//	"transTosuccess":{
//			"{\"successName\":\"success1\",\"drawn\":true}":[
//				{
//					"transName":"TRANS1",
//					"fileName":"res\\paneller01.ktr",
//					"description":"",
//					"drawn":true
//				}
//			]
//		}
//	}
