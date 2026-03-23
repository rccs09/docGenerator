package com.rccs.docgen.enums;

/**
 * Define los tipos de entregas o tipos de documentos por entrega
 */
public enum DeliveryType {
	VERSION("VERSION"),
	FIX("FIX");
	
	private final String type;
	
	private DeliveryType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
