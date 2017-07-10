package com.gyc.dto;

import java.io.Serializable;

public class yanpanFile implements Serializable{
	private Integer id;
    private Integer  yanpan_id;
    private String  type;
    private String  src;
    private String  file_type;
    private long created_at;
    private long updated_at;
    public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getYanpan_id() {
		return yanpan_id;
	}
	public void setYanpan_id(Integer yanpan_id) {
		this.yanpan_id = yanpan_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
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
