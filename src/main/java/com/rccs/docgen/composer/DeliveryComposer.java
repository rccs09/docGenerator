package com.rccs.docgen.composer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import com.rccs.docgen.beans.ArtfactsFileBean;
import com.rccs.docgen.beans.DeliveryConfigBean;
import com.rccs.docgen.beans.HeaderConfigBean;
import com.rccs.docgen.enums.TableType;
import com.rccs.docgen.procesor.TableProcesor;
import com.rccs.docgen.procesor.TemplateConfigurationProcessor;
import com.rccs.docgen.utils.ProcessDocUtils;

public class DeliveryComposer {
	private final XWPFDocument template;
	private final List<ArtfactsFileBean> files;
	private final DeliveryConfigBean deliveryConfig;

	public DeliveryComposer(XWPFDocument template, List<ArtfactsFileBean> files, DeliveryConfigBean deliveryConfig) {
		this.template = template;
		this.files = files;
		this.deliveryConfig = deliveryConfig;
	}

	public void process() {
		Map<String, HeaderConfigBean>  headersConfig = TemplateConfigurationProcessor.getTableHeadersKey();
		
		List<IBodyElement> newTables = new ArrayList<IBodyElement>();
		
		for (XWPFTable table : template.getTables()) {
			HeaderConfigBean headerConfig = ProcessDocUtils.getTableHeader(table, headersConfig);
			TableType tableType = headerConfig == null? TableType.WITHOUT_HEADER : headerConfig.getTableType();
			
			switch (tableType) {
				case WITHOUT_HEADER:
					newTables.add(TableProcesor.processTableWithoutHeader(table, TemplateConfigurationProcessor.getPlaceHolderTablesWithOutHeaders(deliveryConfig)));
					break;
					
				case HEADER_DEFINED_FIELDS:
					newTables.add(TableProcesor.processTableWithHeaderDefinedFields(table, TemplateConfigurationProcessor.getPlaceHolderTablesWithHeaderDefinedFields(headerConfig, deliveryConfig)));
					break;
					
				case HEADER_UNIQUE_ROW:
					newTables.add(TableProcesor.processTableWithHeaderUniqueRow(table, TemplateConfigurationProcessor.getPlaceHolderTablesWithHeadersUniqueRow(headerConfig, deliveryConfig, files)));
					break;
					
				case HEADER_ADD_ROWS:
					newTables.add(TableProcesor.processTableWithHeaderAddRows(table, TemplateConfigurationProcessor.getPlaceHolderTablesWithHeaderAddRows(headerConfig, deliveryConfig, files)));
					break;
					
				case HEADER_TEMPLATE_UNIQUE_ROW:
					newTables.addAll(TableProcesor.processTableWithTemplateUniqueRow(table, TemplateConfigurationProcessor.processTableWithTemplateUniqueRow(headerConfig, deliveryConfig, files)));
					break;
			}
		}
		
		replaceDocument(newTables);
		
	}
	
	
	private void replaceDocument(List<IBodyElement> newTables) {
		int total = template.getBodyElements().size();
		//elimino las tablas actuales
		for (int i = total -1; i>=0; i--) {
			template.removeBodyElement(i);
		}
		
		//agrego las nuebas tablas
		for (int i = 0; i < newTables.size(); i++) {
			IBodyElement table = newTables.get(i);
			
			if(table instanceof XWPFParagraph) {
				template.createParagraph().getCTP().set(((XWPFTable)table).getCTTbl());
			}else {
				template.createTable().getCTTbl().set(((XWPFTable)table).getCTTbl());
				
				if(i+1 < newTables.size() && newTables.get(i+1) instanceof XWPFTable) {
					template.createParagraph();
				}
			}
		}
	}
	
	
}
