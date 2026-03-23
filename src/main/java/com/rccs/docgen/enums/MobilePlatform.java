package com.rccs.docgen.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Definicion de los artefactos para mobile
 */
public enum MobilePlatform implements FsNode{
	ANDROID("android", Arrays.asList(ScopeDir.GUIDE, ScopeDir.INSTALLER)),
	IOS("ios", Arrays.asList(ScopeDir.GUIDE, ScopeDir.INSTALLER));
	
	private final String dir;
	private final List<FsNode> children;
	
	private MobilePlatform(String dir, List<FsNode> children) {
		this.dir = dir;
		this.children = children;
	}
	
	@Override
	public String dir() {
		return dir;
	}
	
	@Override
	public List<FsNode> children() {
		return children;
	}
}
