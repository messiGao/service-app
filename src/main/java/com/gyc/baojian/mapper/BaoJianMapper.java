package com.gyc.baojian.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gyc.dto.baojian;
import com.gyc.dto.baojianFile;
import com.gyc.dto.section;
import com.gyc.dto.yanpan;

@Mapper
public interface BaoJianMapper {

	public void saveBaojianProgram(baojian baojian);

	public baojian getBaoJianProgram(baojian baojian);

	public void saveSection(section section);

	public List<section> getSection(section section);

	public void saveBaojianFile(baojianFile baojianFile);

	public List<yanpan> getBaoJianProgramList(Map<String, Object> paramMap);

	public List<baojianFile> getBaojianFileList(Integer section_id);

	public void deleteSection(section paramSection);

	public void updateBaoJian(baojian baojian);

	public String getBaojianTips(String section_id);

	public void updateBaoJianStatus(baojian baojian);
}
