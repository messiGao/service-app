package com.gyc.dto;

import java.io.Serializable;

/** 
	 * Created by gaoyinchuan on 2017/07/05. 
*/  
public class expert implements Serializable{  
	/**
	 * 
	 */
	private static final long serialVersionUID = -97665101744362702L;
	/**
	 * 
	 */
	private Integer id;
	private String name;
	private String description;
	private String phone;
	private String work_time;
	private long created_at;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWork_time() {
		return work_time;
	}
	public void setWork_time(String work_time) {
		this.work_time = work_time;
	}
	private long updated_at;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public long getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(long updated_at) {
		this.updated_at = updated_at;
	}

	
}  


