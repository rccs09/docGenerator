package com.rccs.docgen.enums;

public enum TableType {
	//Tipo1: Tabla sin cabecera, tienen filas y columnas definidas en las que se ingresan campos definidos.
	WITHOUT_HEADER("SIN_CABECERA_CAMPOS_DEFINIDOS"),
	//Tipo2: Tablas que tienen cabecera y además filas y columnas definidas en las que se ingresan campos definidos
	HEADER_DEFINED_FIELDS("CON_CABECERA_CAMPOS_DEFINIDOS"),
	//Tipo3: Tablas que tienen cabecera, tienen columnas definidas pero las filas se van agregando en base a lo que se necesite.
	HEADER_ADD_ROWS("CON_CABECERA_PARA_AGREGAR_FILAS"),
	//Tipo4: Tablas que tienen cabecera, tienen una sola columna y fila en la que se agregarán textos de procedimientos a ejecutar.
	HEADER_UNIQUE_ROW("CON_CABECERA_UNICA_FILAS"),
	//Tipo5: Tablas que tienen cabecera, tienen una sola columna y fila en la que se agregarán textos de procedimientos a ejecutar. igual que las tipo4 con la diferencia que estas se usarán como plantilla, es decir a partir de esta tabla se crearán N tablas para agregar instrucciones distintas.
	HEADER_TEMPLATE_UNIQUE_ROW("CON_CABECERA_PLANTILLA");
	
	private final String type;
	
	private TableType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	
}
