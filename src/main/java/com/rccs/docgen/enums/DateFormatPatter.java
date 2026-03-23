package com.rccs.docgen.enums;

/**
 * Define los formatos de fecha para los distintos espacios de fecha del documento
 */
public enum DateFormatPatter {
	GENERAL("dd/MM/yyyy"),
	HEADER("dd-MM-yy"),
	DOCUMENT("yyyyMMdd"),
	FTP("dd_MM_yy");
	
	private final String pattern;
	
	private DateFormatPatter(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}
	
	
}
