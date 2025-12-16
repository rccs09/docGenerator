package com.rccs.docgen.enums;

public enum DeployType {
	NEW_VERSION("NUEVA_VERSION"),
	PROCESS_UPGRADE("UPGRADE_DE_PROCESO"),
	FIELS("PASE_ARCHIVOS");
	
	private String type;

	private DeployType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	
}
