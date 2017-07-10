package com.gyc.dto;

import java.io.Serializable;

public class verify implements Serializable{
    /**
	 * 
	 */
	private String verifyCode;
    private Integer verifyTimes;
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public Integer getVerifyTimes() {
		return verifyTimes;
	}
	public void setVerifyTimes(Integer verifyTimes) {
		this.verifyTimes = verifyTimes;
	}
    
}
