package com.springboot.entity.job;

import java.util.List;
import java.util.Map;

public class ParallelJobDto {
	private JobStartDto startDto;
	private Map<String, List<JobTransDto>> transTosuccess;
	public JobStartDto getStartDto() {
		return startDto;
	}
	public void setStartDto(JobStartDto startDto) {
		this.startDto = startDto;
	}
	public Map<String, List<JobTransDto>> getTransTosuccess() {
		return transTosuccess;
	}
	public void setTransTosuccess(Map<String, List<JobTransDto>> transTosuccess) {
		this.transTosuccess = transTosuccess;
	}
	
}
