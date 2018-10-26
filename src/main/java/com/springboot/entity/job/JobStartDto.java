package com.springboot.entity.job;

public class JobStartDto {
	private String startName;
	private boolean start;
	private boolean repeat;
	private int schedulerType;
	private int intervalSeconds;
	private int intervalMinutes;
	private int hour;
	private int minutes;
	private int weekDay;
	private int dayofMonth;
	
	private boolean drawn;
	private boolean parallel;
	public String getStartName() {
		return startName;
	}
	public void setStartName(String startName) {
		this.startName = startName;
	}
	public int getSchedulerType() {
		return schedulerType;
	}
	public void setSchedulerType(int schedulerType) {
		this.schedulerType = schedulerType;
	}
	public int getIntervalSeconds() {
		return intervalSeconds;
	}
	public void setIntervalSeconds(int intervalSeconds) {
		this.intervalSeconds = intervalSeconds;
	}
	public int getIntervalMinutes() {
		return intervalMinutes;
	}
	public void setIntervalMinutes(int intervalMinutes) {
		this.intervalMinutes = intervalMinutes;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}
	public int getDayofMonth() {
		return dayofMonth;
	}
	public void setDayofMonth(int dayofMonth) {
		this.dayofMonth = dayofMonth;
	}
	public boolean isDrawn() {
		return drawn;
	}
	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
	}
	public boolean isStart() {
		return start;
	}
	public void setStart(boolean start) {
		this.start = start;
	}
	public boolean isRepeat() {
		return repeat;
	}
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	public boolean isParallel() {
		return parallel;
	}
	public void setParallel(boolean parallel) {
		this.parallel = parallel;
	}
	
}
