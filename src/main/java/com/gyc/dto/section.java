package com.gyc.dto;

import java.io.Serializable;
import java.util.List;

public class section implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2910398320774463191L;
	private Integer id;
	private Integer baojian_id;
	private String display_order;
	private String  name;
    public String getDisplay_order() {
		return display_order;
	}
	public void setDisplay_order(String display_order) {
		this.display_order = display_order;
	}
	private String  desc;
    //提交阶段(0审核中1审核完成2计划规划3施工主体确认4用地审批5施工验收)
    private Integer  stage;
    private long created_at;
    private long updated_at;
    private List<baojianFile> fileList;  
    public List<baojianFile> getFileList() {
		return fileList;
	}
	public void setFileList(List<baojianFile> fileList) {
		this.fileList = fileList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBaojian_id() {
		return baojian_id;
	}
	public void setBaojian_id(Integer baojian_id) {
		this.baojian_id = baojian_id;
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
	public Integer getStage() {
		return stage;
	}
	public void setStage(Integer stage) {
		this.stage = stage;
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
