package com.gyc.dto;

import java.io.Serializable;

public class category implements Serializable{
    /**
	 * 
	 */
	private Integer id;
    private Integer parent_id;
    private String name;
    private String description;
    private String meta_keywords;
    private String meta_description;
    private String image_main;
    private String image_node;
	private String banner;
    private String sort_val;
    
    //0失效  10有效
    private Integer status;
    private String type;
    private Integer created_at;
	private Integer updated_at;

    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParent_id() {
		return parent_id;
	}
	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
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
	public String getMeta_keywords() {
		return meta_keywords;
	}
	public void setMeta_keywords(String meta_keywords) {
		this.meta_keywords = meta_keywords;
	}
	public String getMeta_description() {
		return meta_description;
	}
	public void setMeta_description(String meta_description) {
		this.meta_description = meta_description;
	}
	public String getImage_main() {
		return image_main;
	}
	public void setImage_main(String image_main) {
		this.image_main = image_main;
	}
	public String getImage_node() {
		return image_node;
	}
	public void setImage_node(String image_node) {
		this.image_node = image_node;
	}
	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner = banner;
	}
	public String getSort_val() {
		return sort_val;
	}
	public void setSort_val(String sort_val) {
		this.sort_val = sort_val;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Integer created_at) {
		this.created_at = created_at;
	}
	public Integer getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Integer updated_at) {
		this.updated_at = updated_at;
	}
	

	
}
