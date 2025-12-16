package com.rccs.docgen.procesor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

import com.rccs.docgen.beans.ArtfactsFileBean;
import com.rccs.docgen.beans.PlaceHolderBean;
import com.rccs.docgen.enums.DeliveryType;
import com.rccs.docgen.utils.ProcessDocUtils;

public class TableProcesor {
	
	//case WITHOUT_HEADER:
	public static XWPFTable processTableWithoutHeader(XWPFTable original, PlaceHolderBean placeHolder) {
		XWPFTable newTable = new XWPFTable((CTTbl)original.getCTTbl().copy(), original.getBody());
		replaceValuesFromTable(newTable, placeHolder);
		return newTable;
	}
	
	
	//case HEADER_DEFINED_FIELDS:
	public static XWPFTable processTableWithHeaderDefinedFields(XWPFTable original, PlaceHolderBean placeHolder) {
		return processTableWithoutHeader(original, placeHolder);
	}
	
	
	//case HEADER_UNIQUE_ROW:
	public static XWPFTable processTableWithHeaderUniqueRow(XWPFTable original, PlaceHolderBean placeHolder) {
		return processTableWithoutHeader(original, placeHolder);
	}
	

	//case HEADER_ADD_ROWS:
	public static XWPFTable processTableWithHeaderAddRows(XWPFTable original, PlaceHolderBean placeHolder) {
		XWPFTable newTable = new XWPFTable((CTTbl)original.getCTTbl().copy(), original.getBody());
		processTableToAddRows(newTable, placeHolder);
		return newTable;
	}
	
	//case HEADER_TEMPLATE_UNIQUE_ROW:
	public static List<XWPFTable> processTableWithTemplateUniqueRow(XWPFTable original, PlaceHolderBean placeHolder) {
		List<XWPFTable> tableList = new ArrayList<XWPFTable>();
		
		for (ArtfactsFileBean artifact : placeHolder.getEspecificArtifacts()) {
			XWPFTable newTable = new XWPFTable((CTTbl)original.getCTTbl().copy(), original.getBody());
			
			Map<String, String> placeHolders = new HashMap<String, String>();
			placeHolders.put("${artifactInstallation}", "Instalación - " + artifact.getComponentType().getType().toUpperCase());
			placeHolder.setParams(placeHolders);
			replaceValuesFromTable(newTable, placeHolder);
			tableList.add(newTable);
		}

		return tableList;
	}
	
	
	//remplaza los placeHolder predefinidos en una tabla
	private static void replaceValuesFromTable(XWPFTable table, PlaceHolderBean placeHolder) {
		//recorre filas
		for (XWPFTableRow row : table.getRows()) {
			//recorre celdas de la fila
			for (XWPFTableCell cell : row.getTableCells()) {
				//recorre parrafos de la celda
				for (XWPFParagraph p : cell.getParagraphs()) {
					String paragraphText = ProcessDocUtils.getCompleteRunFromParagraph(p);
					
					if(paragraphText != null && !paragraphText.isEmpty()) {
						boolean hasKey = false;
						//busca si alguna celda contiene alguna key del placeHolder
						for (Map.Entry<String, String> entry : placeHolder.getParams().entrySet()) {
							if(paragraphText.contains(entry.getKey())) {
								paragraphText = paragraphText.replace(entry.getKey(), entry.getValue());
								hasKey = true;
							}
						}
						
						if(hasKey) {
							ProcessDocUtils.replaceRun(p, paragraphText);
						}
					}
				}
			}
		}
	}
	
	
	private static void processTableToAddRows(XWPFTable table, PlaceHolderBean placeHolder) {
		replaceValuesFromTable(table, placeHolder);
		switch (placeHolder.getHeaderType()) {
			case FILE_LOCATION:
				for (ArtfactsFileBean artifact : placeHolder.getEspecificArtifacts()) {
					XWPFTableRow row = table.createRow();
					XWPFTableCell cell0 = row.getCell(0);
					cell0 = cell0==null? row.createCell(): cell0;
					fillCell(cell0, artifact.getFtpPath());
					
					XWPFTableCell cell1 = row.getCell(1);
					cell1 = cell1==null? row.createCell(): cell1;
					fillCell(cell1, artifact.getFileName());
					
					XWPFTableCell cell2 = row.getCell(2);
					cell2 = cell2==null? row.createCell(): cell2;
					fillCell(cell2, String.format("Seguir la instalación de %s", artifact.getComponentType().getType()) );
				}
				
				break;
				
			case DB_SCRIPT:
				for (ArtfactsFileBean artifact : placeHolder.getEspecificArtifacts()) {
					XWPFTableRow row = table.createRow();
					XWPFTableCell celldb0 = row.getCell(0);
					celldb0 = celldb0==null? row.createCell(): celldb0;
					fillCell(celldb0, artifact.getComponentType().getSchema());
					
					XWPFTableCell celldb1 = row.getCell(1);
					celldb1 = celldb1==null? row.createCell(): celldb1;
					fillCell(celldb1, artifact.getFtpPath());
					
					XWPFTableCell celldb2 = row.getCell(2);
					celldb2 = celldb2==null? row.createCell(): celldb2;
					fillCell(celldb2, artifact.getFileName());
					
					XWPFTableCell celldb3 = row.getCell(3);
					celldb3 = celldb3==null? row.createCell(): celldb3;
					fillCell(celldb3, artifact.getComponentType().getDbAction() );
					
					XWPFTableCell celldb4 = row.getCell(4);
					celldb4 = celldb4==null? row.createCell(): celldb4;
					if(placeHolder.getDeliveryConfig().getDeliveryType() == DeliveryType.VERSION) {
						fillCell(celldb4, "Aplicar en homologación y producción");
					}else if(placeHolder.getDeliveryConfig().getDeliveryType() == DeliveryType.FIX) {
						fillCell(celldb4, "Aplicar únicamente en producción");
					}
				}
				
				break;
				
			case DB_REVERSE_SCRIPT:
				for (ArtfactsFileBean artifact : placeHolder.getEspecificArtifacts()) {
					XWPFTableRow row = table.createRow();
					XWPFTableCell celldbRev0 = row.getCell(0);
					celldbRev0 = celldbRev0==null? row.createCell(): celldbRev0;
					fillCell(celldbRev0, artifact.getComponentType().getSchema());
					
					XWPFTableCell celldbRev1 = row.getCell(1);
					celldbRev1 = celldbRev1==null? row.createCell(): celldbRev1;
					fillCell(celldbRev1, artifact.getFtpPath());
					
					XWPFTableCell celldbRev2 = row.getCell(2);
					celldbRev2 = celldbRev2==null? row.createCell(): celldbRev2;
					fillCell(celldbRev2, artifact.getFileName());
					
					XWPFTableCell celldbRev3 = row.getCell(3);
					celldbRev3 = celldbRev3==null? row.createCell(): celldbRev3;
					fillCell(celldbRev3, artifact.getComponentType().getDbAction() );
					
					XWPFTableCell celldbRev4 = row.getCell(4);
					celldbRev4 = celldbRev4==null? row.createCell(): celldbRev4;
					if(placeHolder.getDeliveryConfig().getDeliveryType() == DeliveryType.VERSION) {
						fillCell(celldbRev4, "Aplicar en homologación y producción");
					}else if(placeHolder.getDeliveryConfig().getDeliveryType() == DeliveryType.FIX) {
						fillCell(celldbRev4, "Aplicar únicamente en producción");
					}
				}
				
				break;
				
			case DOC_VERSION:
				XWPFTableRow row = table.createRow();
				XWPFTableCell cellFecha = row.getCell(0);
				cellFecha = cellFecha==null? row.createCell(): cellFecha;
				fillCell(cellFecha, placeHolder.getDeliveryConfig().getDeliveryDate());
				
				XWPFTableCell cellRol = row.getCell(1);
				cellRol = cellRol==null? row.createCell(): cellRol;
				fillCell(cellRol, placeHolder.getDeliveryConfig().getDeveloper());
				
				XWPFTableCell cellVersion = row.getCell(2);
				cellVersion = cellVersion==null? row.createCell(): cellVersion;
				fillCell(cellVersion, "V1");//TODO: por ahora esta quemado
				break;
				
			default:
				new IllegalArgumentException("No se pudo identificar el header " + placeHolder.getHeaderType().getType() );
				break;
		}
		
		
		
	}
	
	
//	private static void processTableToAddRows(XWPFTable table, PlaceHolderBean placeHolder) {
//		replaceValuesFromTable(table, placeHolder);
//		
//		for (ArtfactsFileBean artifact : placeHolder.getEspecificArtifacts()) {
//			XWPFTableRow row = table.createRow();
//			
//			switch (placeHolder.getHeaderType()) {
//				case FILE_LOCATION:
//					XWPFTableCell cell0 = row.getCell(0);
//					cell0 = cell0==null? row.createCell(): cell0;
//					fillCell(cell0, artifact.getFtpPath());
//					
//					XWPFTableCell cell1 = row.getCell(1);
//					cell1 = cell1==null? row.createCell(): cell1;
//					fillCell(cell1, artifact.getFileName());
//					
//					XWPFTableCell cell2 = row.getCell(2);
//					cell2 = cell2==null? row.createCell(): cell2;
//					fillCell(cell2, String.format("Seguir la instalación de %s", artifact.getComponentType().getType()) );
//					break;
//					
//				case DB_SCRIPT:
//					XWPFTableCell celldb0 = row.getCell(0);
//					celldb0 = celldb0==null? row.createCell(): celldb0;
//					fillCell(celldb0, artifact.getComponentType().getSchema());
//					
//					XWPFTableCell celldb1 = row.getCell(1);
//					celldb1 = celldb1==null? row.createCell(): celldb1;
//					fillCell(celldb1, artifact.getFtpPath());
//					
//					XWPFTableCell celldb2 = row.getCell(2);
//					celldb2 = celldb2==null? row.createCell(): celldb2;
//					fillCell(celldb2, artifact.getFileName());
//					
//					XWPFTableCell celldb3 = row.getCell(3);
//					celldb3 = celldb3==null? row.createCell(): celldb3;
//					fillCell(celldb3, artifact.getComponentType().getDbAction() );
//					
//					XWPFTableCell celldb4 = row.getCell(4);
//					celldb4 = celldb4==null? row.createCell(): celldb4;
//					if(placeHolder.getDeliveryConfig().getDeliveryType() == DeliveryType.VERSION) {
//						fillCell(celldb4, "Aplicar en homologación y producción");
//					}else if(placeHolder.getDeliveryConfig().getDeliveryType() == DeliveryType.FIX) {
//						fillCell(celldb4, "Aplicar únicamente en producción");
//					}
//					break;
//					
//				case DB_REVERSE_SCRIPT:
//					XWPFTableCell celldbRev0 = row.getCell(0);
//					celldbRev0 = celldbRev0==null? row.createCell(): celldbRev0;
//					fillCell(celldbRev0, artifact.getComponentType().getSchema());
//					
//					XWPFTableCell celldbRev1 = row.getCell(1);
//					celldbRev1 = celldbRev1==null? row.createCell(): celldbRev1;
//					fillCell(celldbRev1, artifact.getFtpPath());
//					
//					XWPFTableCell celldbRev2 = row.getCell(2);
//					celldbRev2 = celldbRev2==null? row.createCell(): celldbRev2;
//					fillCell(celldbRev2, artifact.getFileName());
//					
//					XWPFTableCell celldbRev3 = row.getCell(3);
//					celldbRev3 = celldbRev3==null? row.createCell(): celldbRev3;
//					fillCell(celldbRev3, artifact.getComponentType().getDbAction() );
//					
//					XWPFTableCell celldbRev4 = row.getCell(4);
//					celldbRev4 = celldbRev4==null? row.createCell(): celldbRev4;
//					if(placeHolder.getDeliveryConfig().getDeliveryType() == DeliveryType.VERSION) {
//						fillCell(celldbRev4, "Aplicar en homologación y producción");
//					}else if(placeHolder.getDeliveryConfig().getDeliveryType() == DeliveryType.FIX) {
//						fillCell(celldbRev4, "Aplicar únicamente en producción");
//					}
//					break;
//					
//				case DOC_VERSION:
//					XWPFTableCell cellFecha = row.getCell(0);
//					cellFecha = cellFecha==null? row.createCell(): cellFecha;
//					fillCell(cellFecha, placeHolder.getDeliveryConfig().getDeliveryDate());
//					
//					XWPFTableCell cellRol = row.getCell(1);
//					cellRol = cellRol==null? row.createCell(): cellRol;
//					fillCell(cellRol, placeHolder.getDeliveryConfig().getDeveloper());
//					
//					XWPFTableCell cellVersion = row.getCell(2);
//					cellVersion = cellVersion==null? row.createCell(): cellVersion;
//					fillCell(cellVersion, "V1");//TODO: por ahora esta quemado
//					break;
//					
//				default:
//					new IllegalArgumentException("No se pudo identificar el header " + placeHolder.getHeaderType().getType() );
//					break;
//			}
//			
//		}
//		
//	}
	
	private static void fillCell(XWPFTableCell cell, String data) {
		XWPFParagraph p = cell.getParagraphs().get(0);
		XWPFRun run = p.createRun();
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText(data);
		p.setAlignment(ParagraphAlignment.LEFT);
		cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
	}
	

}
