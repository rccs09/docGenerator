package com.rccs.docgen.beans;

import com.rccs.docgen.enums.ArtifactsType;
import com.rccs.docgen.enums.ComponentType;
import com.rccs.docgen.enums.ScopeType;

public class ArtfactsFileBean {
	private String completePath;
	private String fileName;
	private ComponentType componentType;
	private ArtifactsType artifactsType;
	private ScopeType scopeType;
	private String md5;
	private String action;
	private String comment;
	private String schemadb;
	private String ftpPath;

	
	
	public ArtfactsFileBean() {
	}

	public ArtfactsFileBean(String completePath, String fileName, ComponentType componentType,
			ArtifactsType artifactsType, ScopeType scopeType, String md5, String action, String comment,
			String schemadb, String ftpPath) {
		this.completePath = completePath;
		this.fileName = fileName;
		this.componentType = componentType;
		this.artifactsType = artifactsType;
		this.scopeType = scopeType;
		this.md5 = md5;
		this.action = action;
		this.comment = comment;
		this.schemadb = schemadb;
		this.ftpPath = ftpPath;
	}

	public String getCompletePath() {
		return completePath;
	}

	public void setCompletePath(String completePath) {
		this.completePath = completePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ComponentType getComponentType() {
		return componentType;
	}

	public void setComponentType(ComponentType componentType) {
		this.componentType = componentType;
	}

	public ArtifactsType getArtifactsType() {
		return artifactsType;
	}

	public void setArtifactsType(ArtifactsType artifactsType) {
		this.artifactsType = artifactsType;
	}

	public ScopeType getScopeType() {
		return scopeType;
	}

	public void setScopeType(ScopeType scopeType) {
		this.scopeType = scopeType;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSchemadb() {
		return schemadb;
	}

	public void setSchemadb(String schemadb) {
		this.schemadb = schemadb;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	@Override
	public String toString() {
		return "ArtfactsFileBean [completePath=" + completePath + ", fileName=" + fileName + ", componentType="
				+ componentType + ", artifactsType=" + artifactsType + ", scopeType=" + scopeType + ", md5=" + md5
				+ ", action=" + action + ", comment=" + comment + ", schemadb=" + schemadb + ", ftpPath=" + ftpPath
				+ "]";
	}

//	private String absolutePath;
//	private String relativePath;
//	private String md5;
//	private String action;
//	private String comment;
//	private ArtifactsType type;
//	private String extension;
//	private String schemadb;

}
