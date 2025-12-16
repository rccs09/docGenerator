package com.rccs.docgen.enums;

public enum DateFormatPatter {
	GENERAL("dd/MM/yyyy"),
	DOCUMENT("dd-MMM-yy"),
	FTP("dd_MM_yy");
	
	private final String pattern;
	
	private DateFormatPatter(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}
	
	
}
