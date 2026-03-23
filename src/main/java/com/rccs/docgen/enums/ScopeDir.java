package com.rccs.docgen.enums;

import java.util.Collections;
import java.util.List;

public enum ScopeDir implements FsNode{
	INSTALLER("Installer"),
	REVERSER("Reverser"),
	GUIDE("Guide");
	
	private final String dir;
	
	ScopeDir(String dir) {
		this.dir = dir;
	}
	
	@Override
	public String dir() {
		return dir;
	}
	
	@Override
	public List<FsNode> children() {
		return Collections.emptyList();
	}
}
