package com.mash.data.service;

public class SecurityInfo {
	
	protected String userId;
	
	protected String processInstanceId;
	
	protected String stage;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public SecurityInfo(String userId, String processInstanceId, String stage) {
		
		this.userId = userId;
		this.processInstanceId = processInstanceId;
		this.stage = stage;
	}
	
	
	

}
