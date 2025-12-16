package com.rccs.docgen.enums;

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
