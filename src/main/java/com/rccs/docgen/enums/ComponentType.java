package com.rccs.docgen.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ComponentType {
	//componente | esuqema BDD | Accion de BDD
	TIPO1("Componente1", "N/A", "N/A"),//COMPONENTE TIPO 1 -> 
	TIPO2("Componente2", "N/A", "N/A"),//COMPONENTE TIPO 2 -> 
	TIPO3("Componente3", "DB_C3", "Ejecutar el script, luego reiniciar el módulo de Componente3"),//COMPONENTE TIPO 3 -> 
	TIPO4("Componente4", "N/A", "N/A"),//COMPONENTE TIPO 4 -> 
	TIPO5("Componente5", "DB_C4", "Ejecutar el script, antede las instalación del Componente5");//COMPONENTE TIPO 5 -> 
	
	private String type;
	private String schema;
	private String dbAction;

	private ComponentType(String type, String schema, String dbAction) {
		this.type = type;
		this.schema = schema;
		this.dbAction = dbAction;
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
