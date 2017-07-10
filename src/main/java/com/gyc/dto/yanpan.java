package com.gyc.dto;

import java.io.Serializable;

public class yanpan implements Serializable{
	private Integer id;
	private Integer user_id;
	private String  name;
    private String  desc;
    private String  area_id;
    //0 评估中 1评估完成
    private String  status;
    private Integer admin_id;
    public String getReply_desc() {
		return reply_desc;
	}
	public void setReply_desc(String reply_desc) {
		this.reply_desc = reply_desc;
	}
	private String read_status;
    private String reply_desc;
    private long reply_at;
    public long getReply_at() {
		return reply_at;
	}
	public void setReply_at(long reply_at) {
		this.reply_at = reply_at;
	}
	public String getRead_status() {
		return read_status;
	}
	public void setRead_status(String read_status) {
		this.read_status = read_status;
	}
	private long created_at;
    private long updated_at;
    public Integer getUser_id() {
  		return user_id;
  	}
  	public void setUser_id(Integer user_id) {
  		this.user_id = user_id;
  	}
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(Integer admin_id) {
		this.admin_id = admin_id;
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
