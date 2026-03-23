package com.rccs.docgen.enums;

public enum ScopeType {
	GENERAL("GENERAL"),
	INSTALL("INSTALACION"),
	REVERSE("REVERSO");
	
	private final String type;
	
	private ScopeType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	
}
