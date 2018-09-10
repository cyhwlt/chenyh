package com.springboot.entity;

public class RepositoryTreeDto {
	private String id;
	private String parent;
	private String text;
	private String icon;
	private Object state;
	private String type;
	private boolean isLasted;
	private String path;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Object getState() {
		return state;
	}
	public void setState(Object state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isLasted() {
		return isLasted;
	}
	public void setLasted(boolean isLasted) {
		this.isLasted = isLasted;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
