package com.rccs.docgen.beans;

import com.rccs.docgen.enums.DeliveryType;
import com.rccs.docgen.enums.DeployType;
import com.rccs.docgen.enums.RequestType;

public class DeliveryConfigBean {
	private String lastVersion;
	private String deliveryDate;
	private String developer;
	private String process;
	private String ftpBasePath;
	private DeliveryType deliveryType;
	private RequestType requestType;
	private DeployType deployType;

	public DeliveryConfigBean(String lastVersion, String deliveryDate, String developer, String process,
			String ftpBasePath, DeliveryType deliveryType, RequestType requestType, DeployType deployType) {
		this.lastVersion = lastVersion;
		this.deliveryDate = deliveryDate;
		this.developer = developer;
		this.process = process;
		this.ftpBasePath = ftpBasePath;
		this.deliveryType = deliveryType;
		this.requestType = requestType;
		this.deployType = deployType;
	}

	public String getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getFtpBasePath() {
		return ftpBasePath;
	}

	public void setFtpBasePath(String ftpBasePath) {
		this.ftpBasePath = ftpBasePath;
	}

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public DeployType getDeployType() {
		return deployType;
	}

	public void setDeployType(DeployType deployType) {
		this.deployType = deployType;
	}

}
