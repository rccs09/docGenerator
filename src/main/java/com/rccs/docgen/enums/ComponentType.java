package com.rccs.docgen.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Define la estructura de un componente
 * componente | esquema de base de datos | Accion en la BDD | Estructura de carpetas 
 */
public enum ComponentType implements FsNode{
	// Componente que solo contiene un zip para la instalacion y una guia de instalacion
	COMPONENTE1("Componente1", "N/A", "N/A", Arrays.asList(ScopeDir.GUIDE, ScopeDir.INSTALLER)),
	// Compoenente que solo recibe un war para la instalacion
	COMPONENTE2("Componente2", "N/A", "N/A", Arrays.asList(ScopeDir.INSTALLER)), 
	// Componente que tiene un war, y dos SQLs uno para instalacion y otro para reverso
	COMPONENTE3("Componente3", "SCH_DB1", "Ejecutar el script, luego reiniciar el módulo de Componente3", Arrays.asList(ScopeDir.INSTALLER, ScopeDir.REVERSER)),
	// Compoenente usado para la instalacion de APPs mobiles
	COMPONENTE4("Componente4", "N/A", "N/A", Arrays.asList(MobilePlatform.ANDROID, MobilePlatform.IOS)),
	// Compoenente con carpetas variables y SQLs para instalacion y para reverso
	COMPONENTE5("Componente5", "SCH_DB2", "Ejecutar el script, antes de las instalación del Componente5", Arrays.asList(ScopeDir.INSTALLER, ScopeDir.REVERSER)); 
	
	private String type;
	private String schema;
	private String dbAction;
	private final List<FsNode> children;

	private ComponentType(String type, String schema, String dbAction, List<FsNode> children) {
		this.type = type;
		this.schema = schema;
		this.dbAction = dbAction;
		this.children = children;
	}

	public String getType() {
		return type;
	}
	
	public String getSchema() {
		return schema;
	}
	
	public String getDbAction() {
		return dbAction;
	}

	public List<FsNode> getChildren() {
		return children;
	}
	
	@Override
	public String dir() {
		return type;
	}
	
	@Override
	public List<FsNode> children() {
		return Collections.emptyList();
	}

	/**
	 * Genera un path a partir del scope recibido
	 * @param scopeDir -
	 * @return
	 */
	public String getPathByScope(ScopeDir scopeDir) {
		List<FsNode> list = this.children;
		StringBuilder sb = new StringBuilder();
		sb.append(this.dir());
		
		for (FsNode fsNode : list) {
			if(fsNode.dir().equals(scopeDir.dir())) {
				sb.append("/").append(fsNode.dir());
			}
		}
		return sb.toString();
	}
	
	
	
	//Valida si texto enviado coincide con algun enumerado
	public boolean matches(String componentFolder) {
		return componentFolder.equalsIgnoreCase(type);
	}

		
	//busca la lista de enumerados que coincida componentFolder
    public static Optional<ComponentType> fromFolder(String componentFolder) {
        return Arrays.stream(values())
                .filter(a -> a.matches(componentFolder))
                .findFirst();
    }
	
	
}
