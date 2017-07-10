package com.gyc.dto;

import java.io.Serializable;

public class baojian implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3819635057693532374L;
	private Integer id;
	private Integer user_id;
	private String  name;
    private String  desc;
    private String reply_desc;
    private String  area_id;
    //报建宝状态(0审核中1审核完成2计划规划3施工主体确认4用地审批5施工验收)
    private Integer  status;
    private Integer admin_id;
    //1 未读 0 已读
    private String read_status;
   
    
    public String getReply_desc() {
		return reply_desc;
	}
	public void setReply_desc(String reply_desc) {
		this.reply_desc = reply_desc;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
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
