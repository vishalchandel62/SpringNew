package com.credentek.msme.loginmodel;

public class StandAloneOdInFcc {
	

	String lineId;
	LineIdParentLine lineIdParentLine;
	String accountNumber; 
	
	public LineIdParentLine getLineIdParentLine() {
		return lineIdParentLine;
	}
	public void setLineIdParentLine(LineIdParentLine lineIdParentLine) {
		this.lineIdParentLine = lineIdParentLine;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
	

}
