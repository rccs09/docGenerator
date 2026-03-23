package com.rccs.docgen.enums;

/**
 * Define los tipos de peticiones
 */
public enum RequestType {
	REQUEST("REQUERIMIENTO"),
	INCIDENT("INCIDENTE"),
	OTHERS("OTROS");
	
	private String type;

	private RequestType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	
}
