package com.rccs.docgen.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum ArtifactsType {
	ZIP(".zip", ScopeType.EXECUTION, HeaderType.FILE_VALIDATION, HeaderType.FILE_LOCATION, HeaderType.ARTIFACT_INSTALLATION),
	ZIP_REVERSO(".zip", ScopeType.REVERSE, HeaderType.FILE_VALIDATION, HeaderType.FILE_LOCATION, HeaderType.ARTIFACT_INSTALLATION),
	PDF(".pdf", ScopeType.GENERAL, HeaderType.FILE_VALIDATION, HeaderType.FILE_LOCATION),
	WAR(".war", ScopeType.EXECUTION, HeaderType.FILE_VALIDATION, HeaderType.FILE_LOCATION, HeaderType.ARTIFACT_INSTALLATION),
	SCRIPT_EJECUCION(".sql", ScopeType.EXECUTION, HeaderType.FILE_VALIDATION, HeaderType.DB_SCRIPT),
    SCRIPT_REVERSO(".sql", ScopeType.REVERSE, HeaderType.FILE_VALIDATION, HeaderType.DB_REVERSE_SCRIPT),
    BAR(".bar", ScopeType.EXECUTION, HeaderType.FILE_VALIDATION, HeaderType.FILE_LOCATION, HeaderType.ARTIFACT_INSTALLATION),
    BAR_REVERSO(".bar", ScopeType.REVERSE, HeaderType.FILE_VALIDATION, HeaderType.FILE_LOCATION, HeaderType.ARTIFACT_INSTALLATION),
    SH(".sh", ScopeType.EXECUTION, HeaderType.FILE_VALIDATION, HeaderType.FILE_LOCATION, HeaderType.ARTIFACT_INSTALLATION),
    SH_REVERSO(".sh", ScopeType.REVERSE, HeaderType.FILE_VALIDATION, HeaderType.FILE_LOCATION, HeaderType.ARTIFACT_INSTALLATION),
    APK(".zip", ScopeType.EXECUTION, HeaderType.FILE_VALIDATION, HeaderType.FILE_LOCATION),
    IPA(".zip", ScopeType.EXECUTION, HeaderType.FILE_VALIDATION, HeaderType.FILE_LOCATION);
    
   
	private final String extension;
	private final ScopeType scope;
	private final List<HeaderType> headerTypes;
	
	private ArtifactsType(String extension, ScopeType scope, HeaderType... headerTypes) {
		this.extension = extension;
		this.scope = scope;
		this.headerTypes = Arrays.asList(headerTypes);
	}

	public String getExtension() {
		return extension;
	}

	public ScopeType getScope() {
		return scope;
	}

	public List<HeaderType> getHeaderTypes() {
		return headerTypes;
	}
	
	//Valida si el scope hace match con algun archivo
	public boolean matches(String fileName, ScopeType scopeFilter) {
        return fileName.toLowerCase().endsWith(extension) && this.scope == scopeFilter;
    }

	//busca la lista de enumerados que coincida con el archivo y el scope
    public static Optional<ArtifactsType> fromFileName(String fileName, ScopeType scopeFilter) {
        return Arrays.stream(values())
                .filter(a -> a.matches(fileName, scopeFilter))
                .findFirst();
    }
	
	
}
