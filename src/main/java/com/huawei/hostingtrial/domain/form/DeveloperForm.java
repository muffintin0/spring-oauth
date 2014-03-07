package com.huawei.hostingtrial.domain.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class DeveloperForm {
	private String description;
	private Integer totalPhoneNums;
	
	@NotEmpty
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@NotEmpty
	@Min(value=0)
	@Max(value=5)
	public Integer getTotalPhoneNums() {
		return totalPhoneNums;
	}
	
	public void setTotalPhoneNums(Integer totalPhoneNums) {
		this.totalPhoneNums = totalPhoneNums;
	}
	
	

}
