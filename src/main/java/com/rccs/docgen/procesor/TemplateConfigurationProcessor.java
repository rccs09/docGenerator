package com.rccs.docgen.procesor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rccs.docgen.beans.ArtfactsFileBean;
import com.rccs.docgen.beans.DeliveryConfigBean;
import com.rccs.docgen.beans.HeaderConfigBean;
import com.rccs.docgen.beans.PlaceHolderBean;
import com.rccs.docgen.enums.DateFormatPatter;
import com.rccs.docgen.enums.DeliveryType;
import com.rccs.docgen.enums.DeployType;
import com.rccs.docgen.enums.HeaderType;
import com.rccs.docgen.enums.TableType;
import com.rccs.docgen.utils.ProcessDocUtils;

public class TemplateConfigurationProcessor {

	/**
	 * Configura las cabeceras de las plantillas asignandole un tipo
	 * @return placeholder de las cabeceras
	 */
	public static Map<String, HeaderConfigBean> getTableHeadersKey(){
		Map<String, HeaderConfigBean> placeHolders = new HashMap<>();
		placeHolders.put("${requestOrigin}", new HeaderConfigBean(HeaderType.REQUEST_ORIGIN, TableType.HEADER_DEFINED_FIELDS));
		placeHolders.put("${deliveryInfo}", new HeaderConfigBean(HeaderType.DELIVERY_INFO, TableType.HEADER_DEFINED_FIELDS));
		placeHolders.put("${fileValidation}", new HeaderConfigBean(HeaderType.FILE_VALIDATION, TableType.HEADER_UNIQUE_ROW));
		placeHolders.put("${prerequisites}", new HeaderConfigBean(HeaderType.PREREQUISITES, TableType.HEADER_UNIQUE_ROW));
		placeHolders.put("${fileLocation}", new HeaderConfigBean(HeaderType.FILE_LOCATION, TableType.HEADER_ADD_ROWS));
		placeHolders.put("${environmentConfig}", new HeaderConfigBean(HeaderType.ENVIRONMENT_CONFIG, TableType.HEADER_UNIQUE_ROW));
		placeHolders.put("${artifactInstallation}", new HeaderConfigBean(HeaderType.ARTIFACT_INSTALLATION, TableType.HEADER_TEMPLATE_UNIQUE_ROW));
		placeHolders.put("${dbScripts}", new HeaderConfigBean(HeaderType.DB_SCRIPT, TableType.HEADER_ADD_ROWS));
		placeHolders.put("${dbReverseScripts}", new HeaderConfigBean(HeaderType.DB_REVERSE_SCRIPT, TableType.HEADER_ADD_ROWS));
		placeHolders.put("${docVersion}", new HeaderConfigBean(HeaderType.DOC_VERSION, TableType.HEADER_ADD_ROWS));
		return placeHolders;
	}
	
	/**
	 * Tipo1: Tabla sin cabecera, tienen filas y columnas definidas en las que se ingresan campos definidos.
	 * Procesa los campos de las tablas que no tiene cabecera
	 * @param deliveryConfig
	 * @return
	 */
	public static PlaceHolderBean getPlaceHolderTablesWithOutHeaders(DeliveryConfigBean deliveryConfig){
		PlaceHolderBean result = new PlaceHolderBean();
		Map<String, String> placeHolders = new HashMap<String, String>();
		placeHolders.put("${requestCode}", deliveryConfig.getDeliveryType()==DeliveryType.VERSION?"Liberación Homologación/Producción":"Liberación Producción");
		placeHolders.put("${generalDate}", ProcessDocUtils.changeDateStringFormat(deliveryConfig.getDeliveryDate(), DateFormatPatter.GENERAL.getPattern(), DateFormatPatter.DOCUMENT.getPattern()));
		placeHolders.put("${developer}", deliveryConfig.getDeveloper());
		placeHolders.put("${process}", deliveryConfig.getProcess());
		placeHolders.put("${ftpLocation}", deliveryConfig.getFtpBasePath());

		result.setParams(placeHolders);
		result.setDeliveryConfig(deliveryConfig);
		return result;
	}
	
	/**
	 * Tipo2: Tablas que tienen cabecera y además filas y columnas definidas en las que se ingresan campos definidos
	 * @param deliveryConfig
	 * @return
	 */
	public static PlaceHolderBean getPlaceHolderTablesWithHeaderDefinedFields(HeaderConfigBean headerConfig, DeliveryConfigBean deliveryConfig){
		PlaceHolderBean result = new PlaceHolderBean();
		Map<String, String> placeHolders = new HashMap<String, String>();
		switch (headerConfig.getHeaderType()) {
			case REQUEST_ORIGIN:
				placeHolders.put("${requestOrigin}", "Origen de la solicitud");
				placeHolders.put("${reqTypeReq}", DeployType.NEW_VERSION == deliveryConfig.getDeployType()? "\u2611":"\u2610");
				placeHolders.put("${reqTypeInc}", DeployType.PROCESS_UPGRADE == deliveryConfig.getDeployType()? "\u2611":"\u2610");
				placeHolders.put("${reqTypeOther}", DeployType.FIELS == deliveryConfig.getDeployType()? "\u2611":"\u2610");
				break;
				
			case DELIVERY_INFO:
				placeHolders.put("${deliveryInfo}", "Datos de la liberación");
				placeHolders.put("${rdNewVersion}", DeployType.NEW_VERSION == deliveryConfig.getDeployType()? "\u2611":"\u2610");
				placeHolders.put("${rdUpgrade}", DeployType.PROCESS_UPGRADE == deliveryConfig.getDeployType()? "\u2611":"\u2610");
				placeHolders.put("${rdFiles}", DeployType.FIELS == deliveryConfig.getDeployType()? "\u2611":"\u2610");
				break;
			default:
				break;
		}
		result.setParams(placeHolders);
		result.setHeaderType(headerConfig.getHeaderType());
		result.setDeliveryConfig(deliveryConfig);
		return result;
	}
	
	/**
	 * Tipo3: Tablas que tienen cabecera, tienen columnas definidas pero las filas se van agregando en base a lo que se necesite.
	 * @param deliveryConfig
	 * @return
	 */
	public static PlaceHolderBean getPlaceHolderTablesWithHeadersUniqueRow(HeaderConfigBean headerConfig, DeliveryConfigBean deliveryConfig, List<ArtfactsFileBean> artifacts){
		PlaceHolderBean result = new PlaceHolderBean();
		Map<String, String> placeHolders = new HashMap<String, String>();
		List<ArtfactsFileBean> especificArtifacts = ProcessDocUtils.filterArtifactByHeader(artifacts, headerConfig.getHeaderType());
		StringBuilder sb = new StringBuilder();
		switch (headerConfig.getHeaderType()) {
			case FILE_VALIDATION:
				sb.append("**NOTA**: Se recomienda validar cada uno de los archivos incluidos en la liberación a fin de determinar si han sido copiados correctamente.").append("\n")
				.append("A continuación de detalla el md5sum de cada archivo: ")
				.append("\n").append("\n")
				.append(especificArtifacts.stream().map(l -> l.getMd5() + "          " + l.getFtpPath()+ "/" + l.getFileName()).collect(Collectors.joining("\n")));
				
				placeHolders.put("${fileValidation}", "Tareas Adicionales");
				placeHolders.put("${fileValidationValues}", sb.toString());
				break;
				
			case PREREQUISITES:
				sb.append("**NOTA**: Para ejecutar se debe tener instalada la version XYZ (aun esta quemado)").append("\n")
				.append("\n").append("\n");
				
				placeHolders.put("${prerequisites}", "Prerequisitos");
				placeHolders.put("${prerequicitesValues}", sb.toString());
				break;
				
			case ENVIRONMENT_CONFIG:
				sb.append("**NOTA**: Aqui van las configuraciones de ambuientes XYZ (aun esta quemado)").append("\n")
				.append("\n").append("\n");
				
				placeHolders.put("${environmentConfig}", "Configuración de Ambientes");
				placeHolders.put("${environmentConfigValues}", sb.toString());
				break;
			
			default:
				new IllegalArgumentException("No se pudo identificar el header " + headerConfig.getHeaderType().getType() + " para el tipo de tabla: " + headerConfig.getTableType().getType());
				break;
		}
		result.setParams(placeHolders);
		result.setEspecificArtifacts(especificArtifacts);
		result.setHeaderType(headerConfig.getHeaderType());
		result.setDeliveryConfig(deliveryConfig);
		return result;
	}
	
	/**
	 * Tipo4: Tablas que tienen cabecera, tienen columnas definidas pero las filas se van agregando en base a lo que se necesite.
	 * @param deliveryConfig
	 * @return
	 */
	public static PlaceHolderBean getPlaceHolderTablesWithHeaderAddRows(HeaderConfigBean headerConfig, DeliveryConfigBean deliveryConfig, List<ArtfactsFileBean> artifacts){
		PlaceHolderBean result = new PlaceHolderBean();
		Map<String, String> placeHolders = new HashMap<String, String>();
		List<ArtfactsFileBean> especificArtifacts = ProcessDocUtils.filterArtifactByHeader(artifacts, headerConfig.getHeaderType());
		switch (headerConfig.getHeaderType()) {
			case FILE_LOCATION:
				placeHolders.put("${fileLocation}", "Ubicación de los Archivos");
				break;
				
			case DB_SCRIPT:
				placeHolders.put("${requestOrigin}", "Scripts de base de datos");
				break;
				
			case DB_REVERSE_SCRIPT:
				placeHolders.put("${dbReverseScripts}", "Scripts de base de datos - Solo en caso de reverso");
				break;
				
			case DOC_VERSION:
				placeHolders.put("${docVersion}", "Descripción del documento");
				break;
				
			default:
				new IllegalArgumentException("No se pudo identificar el header " + headerConfig.getHeaderType().getType() + " para el tipo de tabla: " + headerConfig.getTableType().getType());
				break;
		}
		result.setParams(placeHolders);
		result.setEspecificArtifacts(especificArtifacts);
		result.setHeaderType(headerConfig.getHeaderType());
		result.setDeliveryConfig(deliveryConfig);
		return result;
	}
	
	
	/**
	 * Tipo5: Tablas que tienen cabecera, tienen columnas definidas pero las filas se van agregando en base a lo que se necesite.
	 * @param deliveryConfig
	 * @return
	 */
	public static PlaceHolderBean processTableWithTemplateUniqueRow(HeaderConfigBean headerConfig, DeliveryConfigBean deliveryConfig, List<ArtfactsFileBean> artifacts){
		PlaceHolderBean result = new PlaceHolderBean();
		Map<String, String> placeHolders = new HashMap<String, String>();
		List<ArtfactsFileBean> especificArtifacts = ProcessDocUtils.filterArtifactByHeader(artifacts, headerConfig.getHeaderType());
		switch (headerConfig.getHeaderType()) {
			case ARTIFACT_INSTALLATION:
				placeHolders.put("${artifactInstallation}", "Instalación - %s");
				break;
				
			default:
				new IllegalArgumentException("No se pudo identificar el header " + headerConfig.getHeaderType().getType() + " para el tipo de tabla: " + headerConfig.getTableType().getType());
				break;
		}
		result.setParams(placeHolders);
		result.setEspecificArtifacts(especificArtifacts);
		result.setHeaderType(headerConfig.getHeaderType());
		result.setDeliveryConfig(deliveryConfig);
		return result;
	}
	
}
