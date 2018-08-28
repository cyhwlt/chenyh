package com.etc.po;

public class Student {

	private int sno;
	private String sname;
	private int sage;
	
	public Student(){}
	public Student(String name, int sage) {
		this.sname = name;
		this.sage = sage;
	}
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public int getSage() {
		return sage;
	}
	public void setSage(int sage) {
		this.sage = sage;
	}
	
	@Override
	public String toString() {
		return "Student [sno=" + sno + ", sname=" + sname + ", sage=" + sage + "]";
	}
}
