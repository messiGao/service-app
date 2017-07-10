package com.gyc.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gyc.dto.feedback;
import com.gyc.dto.user;

@Mapper
public interface UserMapper {
	public user getUserInfo(user user);
	
	public void userRegister(user user);
	
	public void updateUserInfo(user user);
	
	public String getUUID();

	public void updateUserInfoByToken(user user);

	public List<String> getAllUserInfo();

	public void submitFeedBack(feedback feedback);
}
