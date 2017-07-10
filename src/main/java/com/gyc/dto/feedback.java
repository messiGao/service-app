package com.gyc.dto;

import java.io.Serializable;

/** 
	 * Created by gaoyinchuan on 2017/07/05. 
*/  
public class feedback implements Serializable{  
	/**
	 * 
	 */
	private static final long serialVersionUID = 5064299336711701278L;
	private Integer id;
	private Integer user_id;
	private String content;
	private long created_at;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	private long updated_at;
	
	
}  


