package com.gyc.yanpan.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gyc.dto.yanpan;
import com.gyc.dto.yanpanFile;

@Mapper
public interface YanPanMapper {

	public void saveYanPanProgram(yanpan yanpan);

	public List<yanpan> getYanPanProgramList(Map<String, Object> paramMap);
	
	public void saveYanPanFile(yanpanFile yanpanFile);

	public void updateYanPan(yanpan yanpan);

	public List<yanpanFile> getYanPanFileList(Integer yanpan_id);
}
