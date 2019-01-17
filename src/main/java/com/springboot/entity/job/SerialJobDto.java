package com.springboot.entity.job;

import java.util.List;

public class SerialJobDto {
	//start
	private JobStartDto startDto;
	//trans
	private List<JobTransDto> transDto;
	//job
	private List<JobDto> jobDto;
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
	public List<JobDto> getJobDto() {
		return jobDto;
	}
	public void setJobDto(List<JobDto> jobDto) {
		this.jobDto = jobDto;
	}
	
}