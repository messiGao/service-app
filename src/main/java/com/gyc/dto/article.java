package com.gyc.dto;

public class article {

	private Integer id;
	private Integer category_id;
	private String name;
	private String summary;
	private String content;
	private String meta_keywords;
	private String meta_description;
	private String image_main;
	private String image_node;
	private Integer view_count;
	private String status;
	private Integer sort_val;
	private Integer created_at;
	private Integer updated_at;
	private String type;
	private boolean collect_sign;
	public boolean isCollect_sign() {
		return collect_sign;
	}

	public void setCollect_sign(boolean collect_sign) {
		this.collect_sign = collect_sign;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	private String category_name;
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;

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

	public Integer getView_count() {
		return view_count;
	}

	public void setView_count(Integer view_count) {
		this.view_count = view_count;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSort_val() {
		return sort_val;
	}

	public void setSort_val(Integer sort_val) {
		this.sort_val = sort_val;
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

	@Override
	public boolean equals(Object object) {
		if (object == null)
			return this == null;
		return object.toString().equals(toString());

	}

	@Override
	public String toString() {
		return String.valueOf(id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
