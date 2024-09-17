package com.credentek.msme.loginmodel;

import java.util.ArrayList;

public class LineIdParentLine {
	

	//LIMIT_AMT, UTILISATION, AVAILABLE_AMOUNT
	String limitAmount;
	String utilization;
	String availableAmount;
	ArrayList<ChildOdInFcc> childOd ;
	String description;
	String parentUtil;
	
	public String getParentUtil() {
		return parentUtil;
	}
	public void setParentUtil(String parentUtil) {
		this.parentUtil = parentUtil;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public ArrayList<ChildOdInFcc> getChildOd() {
		return childOd;
	}
	public void setChildOd(ArrayList<ChildOdInFcc> childOd) {
		this.childOd = childOd;
	}
	public String getLimitAmount() {
		return limitAmount;
	}
	public void setLimitAmount(String limitAmount) {
		this.limitAmount = limitAmount;
	}
	public String getUtilization() {
		return utilization;
	}
	public void setUtilization(String utilization) {
		this.utilization = utilization;
	}
	public String getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(String availableAmount) {
		this.availableAmount = availableAmount;
	}
	
	

}
