package com.rccs.docgen.procesor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rccs.docgen.beans.ArtifactsFileBean;
import com.rccs.docgen.beans.DeliveryConfigBean;
import com.rccs.docgen.beans.HeaderConfigBean;
import com.rccs.docgen.beans.PlaceHolderBean;
import com.rccs.docgen.enums.ArtifactsType;
import com.rccs.docgen.enums.ComponentType;
import com.rccs.docgen.enums.DateFormatPatter;
import com.rccs.docgen.enums.DeliveryType;
import com.rccs.docgen.enums.DeployType;
import com.rccs.docgen.enums.HeaderType;
import com.rccs.docgen.enums.RequestType;
import com.rccs.docgen.enums.ScopeType;
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
		placeHolders.put("${requestCode}", deliveryConfig.getDeliveryType()==DeliveryType.VERSION?"Liberación UAT/Producción":"Liberación Producción");
		placeHolders.put("${generalDate}", ProcessDocUtils.changeDateStringFormat(deliveryConfig.getDeliveryDate(), DateFormatPatter.GENERAL.getPattern(), DateFormatPatter.HEADER.getPattern()));
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
				placeHolders.put("${reqTypeReq}", RequestType.REQUEST == deliveryConfig.getRequestType()? "\u2611":"\u2610");
				placeHolders.put("${reqTypeInc}", RequestType.INCIDENT == deliveryConfig.getRequestType()? "\u2611":"\u2610");
				placeHolders.put("${reqTypeOther}", RequestType.OTHERS == deliveryConfig.getRequestType()? "\u2611":"\u2610");
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
	public static PlaceHolderBean getPlaceHolderTablesWithHeadersUniqueRow(HeaderConfigBean headerConfig, DeliveryConfigBean deliveryConfig, List<ArtifactsFileBean> artifacts){
		PlaceHolderBean result = new PlaceHolderBean();
		Map<String, String> placeHolders = new HashMap<String, String>();
		List<ArtifactsFileBean> especificArtifacts = ProcessDocUtils.filterArtifactByHeader(artifacts, headerConfig.getHeaderType());
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
				sb.append("Tener en PRODUCCIóN la versión: ").append(deliveryConfig.getLastVersion()).append("\n")
				.append("\n").append("\n");
				
				placeHolders.put("${prerequisites}", "Prerequisitos");
				placeHolders.put("${prerequicitesValues}", sb.toString());
				break;
				
			case ENVIRONMENT_CONFIG:
				sb.append("✔ **UAT**")
				.append("\n").append("\t ✔ Componente1: N/A")
				.append("\n").append("\t ✔ Componente2: N/A")
				.append("\n").append("\t ✔ Componente3: N/A")
				.append("\n").append("\t ✔ Componente4: N/A")
				.append("\n").append("\t ✔ Componente5: N/A")
				.append("\n").append("\n")
				.append("✔ **PRODUCCION**")
				.append("\n").append("\t ✔ Componente1: N/A")
				.append("\n").append("\t ✔ Componente2: N/A")
				.append("\n").append("\t ✔ Componente3: N/A")
				.append("\n").append("\t ✔ Componente4: N/A")
				.append("\n").append("\t ✔ Componente5: N/A");
				
				placeHolders.put("${environmentConfig}", "Configuración de Ambientes");
				placeHolders.put("${environmentConfigValues}", deliveryConfig.getDeliveryType()==DeliveryType.VERSION? sb.toString():"");
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
	public static PlaceHolderBean getPlaceHolderTablesWithHeaderAddRows(HeaderConfigBean headerConfig, DeliveryConfigBean deliveryConfig, List<ArtifactsFileBean> artifacts){
		PlaceHolderBean result = new PlaceHolderBean();
		Map<String, String> placeHolders = new HashMap<String, String>();
		List<ArtifactsFileBean> especificArtifacts = ProcessDocUtils.filterArtifactByHeader(artifacts, headerConfig.getHeaderType());
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
	public static PlaceHolderBean processTableWithTemplateUniqueRow(HeaderConfigBean headerConfig, DeliveryConfigBean deliveryConfig, List<ArtifactsFileBean> artifacts){
		PlaceHolderBean result = new PlaceHolderBean();
		Map<String, String> placeHolders = new HashMap<String, String>();
		List<ArtifactsFileBean> especificArtifactsByHeader = ProcessDocUtils.filterArtifactByHeader(artifacts, headerConfig.getHeaderType());
		
		Map<ComponentType, List<ArtifactsFileBean>> artifactsByComponent = null;
		Map<ComponentType, Map<ScopeType, String>> installActions = null;
		Map<ComponentType, Map<String, String>> placeHolderByComponent = null;
		
		
		switch (headerConfig.getHeaderType()) {
			case ARTIFACT_INSTALLATION:
				placeHolders.put("${artifactInstallation}", "Instalación - %s");
				//artefactos por componente
				artifactsByComponent = especificArtifactsByHeader.stream()
						.filter(a -> a.getComponentType() != null)
						.collect(Collectors.groupingBy(ArtifactsFileBean::getComponentType));
				
				//define los pasos de instalacion para cada componente
				installActions = setInstallActionByComponente(artifactsByComponent, artifacts);
				
				//define los placeholder por compoenente
				placeHolderByComponent = generatePlaceHolderByActions(installActions);
				
				break;
				
			default:
				new IllegalArgumentException("No se pudo identificar el header " + headerConfig.getHeaderType().getType() + " para el tipo de tabla: " + headerConfig.getTableType().getType());
				break;
		}
		result.setParams(placeHolders);
		result.setEspecificArtifacts(especificArtifactsByHeader);
		result.setHeaderType(headerConfig.getHeaderType());
		result.setDeliveryConfig(deliveryConfig);
		result.setArtifactsByComponent(artifactsByComponent);
		result.setInstallActions(installActions);
		result.setParamsInstallActions(placeHolderByComponent);
		return result;
	}
	
	
	//Separa los placeholder por componente
	private static Map<ComponentType, Map<String, String>> generatePlaceHolderByActions(Map<ComponentType, Map<ScopeType, String>> installActions){
		Map<ComponentType, Map<String, String>> placeHolderByComponent = new HashMap<>();
		for (Map.Entry<ComponentType, Map<ScopeType, String>> entry : installActions.entrySet()) {
			Map<String, String> placeHolder = new HashMap<>();
			StringBuilder sb = new StringBuilder();
			ComponentType componentType = entry.getKey();
			Map<ScopeType, String> actions = entry.getValue();
			placeHolder.put("${artifactInstallation}", "Instalación - " + componentType.getType().toUpperCase());
			
			if(installActions != null && installActions.size() > 0 && installActions.get(componentType) != null) {
				for (Map.Entry<ScopeType, String> action : actions.entrySet()) {
					String val = action.getValue();
					sb.append(val).append("\n");
				}
				placeHolder.put("${artifactInstallationValues}", sb.toString());
			}else {
				String error = "No hay acciones definidas para el compoenente: " + componentType;
				System.out.println(error);
				placeHolder.put("${artifactInstallationValues}", error);
			}
			placeHolderByComponent.put(componentType, placeHolder);
		}
		return placeHolderByComponent;
	}
	
	private static Map<ComponentType, Map<ScopeType, String>> setInstallActionByComponente(Map<ComponentType, List<ArtifactsFileBean>> artifactsByComponent, List<ArtifactsFileBean> originalArtifacts){
		Map<ComponentType, Map<ScopeType, String>> installActions = new HashMap<>();
		
		for(Map.Entry<ComponentType, List<ArtifactsFileBean>> entry : artifactsByComponent.entrySet()) {
			ComponentType key = entry.getKey();
			List<ArtifactsFileBean> artifacts = entry.getValue();
			switch (key) {
				case COMPONENTE1:
					installActions.put(key, setActionsToComponent1(artifacts, originalArtifacts));
					break;
				case COMPONENTE2:
					installActions.put(key, setActionsToComponent2(artifacts));
					break;
				case COMPONENTE3:
					installActions.put(key, setActionsToComponent3(artifacts));
					break;
				case COMPONENTE4:
					installActions.put(key, setActionsToComponent4(originalArtifacts));
					break;
				case COMPONENTE5:
					installActions.put(key, setActionsToComponent5(artifacts));
					break;
				default:
					break;
			}
		}
		return installActions;
	}
	
	// configuracion de las acciones del Componente1
	private static Map<ScopeType, String> setActionsToComponent1(List<ArtifactsFileBean> artifacts, List<ArtifactsFileBean> originalArtifacts){
		//obtener guia
		ArtifactsFileBean pdfFile = originalArtifacts.stream()
				.filter(oa -> oa.getComponentType() == ComponentType.COMPONENTE1)
				.filter(oa -> oa.getArtifactsType() == ArtifactsType.PDF)
				.findFirst().orElse(null);
		
		Map<ScopeType, String> installActions = new HashMap<>();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" ✔ El archivo **").append(pdfFile.getOriginPath()).append(pdfFile.getFileName()).append("**")
		.append(" es el instructivo del Componente 1").append("\n");
		
		for (ArtifactsFileBean artifact : artifacts) {
			sb.append("\t✔ ").append(artifact.getOriginPath()).append(artifact.getFileName()).append("\n");
		}
		
		installActions.put(ScopeType.INSTALL, sb.toString());
		return installActions;
	}
	
	// configuracion de las acciones del Componente2
	private static Map<ScopeType, String> setActionsToComponent2(List<ArtifactsFileBean> artifacts){
		Map<ScopeType, String> installActions = new HashMap<>();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" ✔ Instalar war **").append(artifacts.get(0).getFileName()).append("**").append(" en el servidor de aplicaciones del componente1. ");
		
		installActions.put(ScopeType.INSTALL, sb.toString());
		return installActions;
	}
	
	
	// configuracion de las acciones del Componente3
	private static Map<ScopeType, String> setActionsToComponent3(List<ArtifactsFileBean> artifacts){
		Map<ScopeType, String> installActions = new HashMap<>();
		
		Map<ArtifactsType, List<ArtifactsFileBean>> artifactsByType = artifacts.stream()
				.filter(a -> a.getArtifactsType() != null)
				.collect(Collectors.groupingBy(
						ArtifactsFileBean::getArtifactsType,
						Collectors.collectingAndThen(Collectors.toList(), 
								list -> {
									list.sort(Comparator.comparing(ArtifactsFileBean::getFileName, String.CASE_INSENSITIVE_ORDER));
									return list;
								})
						));
		
		StringBuilder completeAction = new StringBuilder();
		StringBuilder warAction = new StringBuilder();
		StringBuilder zipAction = new StringBuilder();
		
		for (Map.Entry<ArtifactsType, List<ArtifactsFileBean>> artiMap : artifactsByType.entrySet()) {
			switch (artiMap.getKey()) {
				case WAR:
					warAction.append(" ✔ Instalar war **").append(artifacts.get(0).getFileName()).append("**").append(" en el servidor de aplicaciones del componente2. ");
					completeAction.append(warAction);
					break;
				case ZIP:
					zipAction.append(" ✔ Descomprimir el zip **").append(artifacts.get(0).getFileName()).append("**").append(" en el servidor de aplicaciones del componente2. ");
					completeAction.append(zipAction);
					break;
				default:
					break;
			}
		}
		
		
		installActions.put(ScopeType.INSTALL, completeAction.toString());
		return installActions;
	}
	
	
	// configuracion de las acciones del Componente4
	private static Map<ScopeType, String> setActionsToComponent4(List<ArtifactsFileBean> artifacts){
		Map<ScopeType, String> installActions = new HashMap<>();
		
		//obtener guia
		List<ArtifactsFileBean> guias = artifacts.stream()
				.filter(oa -> oa.getComponentType() == ComponentType.COMPONENTE4)
				.filter(oa -> oa.getArtifactsType() == ArtifactsType.PDF)
				.collect(Collectors.toList());
		
		StringBuilder sb = new StringBuilder();
		for (ArtifactsFileBean guia : guias) {
			if(guia.getFileName().toLowerCase().contains("android")) {
				sb.append(" ✔ Para **Android**, usar la guia: **").append(guia.getOriginPath()).append(guia.getFileName()).append("**\n");
			}else {
				sb.append(" ✔ Para **iOS**, usar la guia: **").append(guia.getOriginPath()).append(guia.getFileName()).append("**\n");
			}
		}
		
		installActions.put(ScopeType.INSTALL, sb.toString());
		return installActions;
	}
	
	// configuracion de las acciones del Componente5
	private static Map<ScopeType, String> setActionsToComponent5(List<ArtifactsFileBean> artifacts){
		Map<ScopeType, String> installActions = new HashMap<>();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" ✔ AUN NO SE IMPLEMENTA***********************");
		
		installActions.put(ScopeType.INSTALL, sb.toString());
		return installActions;
	}
	
	
}
