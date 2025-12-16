package com.rccs.docgen.beans;

import java.util.List;
import java.util.Map;

import com.rccs.docgen.enums.HeaderType;

public class PlaceHolderBean {
	private Map<String, String> params;
	private List<ArtfactsFileBean> especificArtifacts;
	private HeaderType headerType;
	private DeliveryConfigBean deliveryConfig;
	

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

	public List<ArtfactsFileBean> getEspecificArtifacts() {
		return especificArtifacts;
	}

	public void setEspecificArtifacts(List<ArtfactsFileBean> especificArtifacts) {
		this.especificArtifacts = especificArtifacts;
	}

	public DeliveryConfigBean getDeliveryConfig() {
		return deliveryConfig;
	}

	public void setDeliveryConfig(DeliveryConfigBean deliveryConfig) {
		this.deliveryConfig = deliveryConfig;
	}


}
