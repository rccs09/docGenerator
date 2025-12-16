package com.rccs.docgen.enums;

public enum ScopeType {
	GENERAL("GENERAL"),
	EXECUTION("EJECUCION"),
	REVERSE("REVERSO");
	
	private final String type;
	
	private ScopeType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	
}
