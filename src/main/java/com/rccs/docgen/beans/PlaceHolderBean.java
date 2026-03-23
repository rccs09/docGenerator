package com.rccs.docgen.beans;

import java.util.List;
import java.util.Map;

import com.rccs.docgen.enums.ComponentType;
import com.rccs.docgen.enums.HeaderType;
import com.rccs.docgen.enums.ScopeType;

public class PlaceHolderBean {
	private Map<String, String> params;
	private List<ArtifactsFileBean> especificArtifacts;
	private HeaderType headerType;
	private DeliveryConfigBean deliveryConfig;
	private Map<ComponentType, List<ArtifactsFileBean>> artifactsByComponent;
	private Map<ComponentType, Map<ScopeType, String>> installActions;
	private Map<ComponentType, Map<String, String>> paramsInstallActions;;
	

	public HeaderType getHeaderType() {
		return headerType;
	}

	public void setHeaderType(HeaderType headerType) {
		this.headerType = headerType;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<ArtifactsFileBean> getEspecificArtifacts() {
		return especificArtifacts;
	}

	public void setEspecificArtifacts(List<ArtifactsFileBean> especificArtifacts) {
		this.especificArtifacts = especificArtifacts;
	}

	public DeliveryConfigBean getDeliveryConfig() {
		return deliveryConfig;
	}

	public void setDeliveryConfig(DeliveryConfigBean deliveryConfig) {
		this.deliveryConfig = deliveryConfig;
	}

	public Map<ComponentType, List<ArtifactsFileBean>> getArtifactsByComponent() {
		return artifactsByComponent;
	}

	public void setArtifactsByComponent(Map<ComponentType, List<ArtifactsFileBean>> artifactsByComponent) {
		this.artifactsByComponent = artifactsByComponent;
	}

	public Map<ComponentType, Map<ScopeType, String>> getInstallActions() {
		return installActions;
	}

	public void setInstallActions(Map<ComponentType, Map<ScopeType, String>> installActions) {
		this.installActions = installActions;
	}

	public Map<ComponentType, Map<String, String>> getParamsInstallActions() {
		return paramsInstallActions;
	}

	public void setParamsInstallActions(Map<ComponentType, Map<String, String>> paramsInstallActions) {
		this.paramsInstallActions = paramsInstallActions;
	}



}
