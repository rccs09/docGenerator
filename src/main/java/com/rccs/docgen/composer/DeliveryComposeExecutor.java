package com.rccs.docgen.composer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.rccs.docgen.beans.ArtifactsFileBean;
import com.rccs.docgen.beans.DeliveryConfigBean;
import com.rccs.docgen.enums.ComponentType;
import com.rccs.docgen.enums.DeliveryType;
import com.rccs.docgen.enums.FsNode;
import com.rccs.docgen.utils.ProcessDocUtils;

public class DeliveryComposeExecutor {
	private static final String TEMPLATE_PATH = "src/main/resources/template/template.docx";
	
	public void execute(String deliveryOutPath, String resourcesPath, DeliveryConfigBean deliveryConfig) {
		try(FileInputStream fis = new FileInputStream(TEMPLATE_PATH)){
			XWPFDocument doc = new XWPFDocument(fis);
			List<ArtifactsFileBean> artifacts = ProcessDocUtils.readAllArtifacts(resourcesPath, deliveryConfig);
			
			validateStructureResourcePath(resourcesPath, deliveryConfig.getDeliveryType());
			
			DeliveryComposer composer = new DeliveryComposer(doc, artifacts, deliveryConfig);
			composer.process();
			
			FileOutputStream out = new FileOutputStream(deliveryOutPath);
			doc.write(out);
			out.close();
			doc.close();
			fis.close();
			
//			DeliveryConfigBean deliveryConfig = new DeliveryConfigBean("DEMANDA 58", 
//					"25/12/2025", 
//					"Galileo", 
//					"", 
//					"./FTP", 
//					DeliveryType.VERSION, 
//					RequestType.REQUEST, 
//					DeployType.NEW_VERSION);
					
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void validateStructureResourcePath(String path, DeliveryType deliveryType) {
		ComponentType[] components = ComponentType.values();
		validateStructureResourcePath(path, deliveryType, components);
	}
	
	
	private void validateStructureResourcePath(String path, DeliveryType deliveryType, ComponentType[] components) {
		Path root = Paths.get(path);
		
		for(ComponentType component : components) {
			if(deliveryType == DeliveryType.VERSION) {
				validatePath(root, component);
			}
			
			if(deliveryType == DeliveryType.FIX && component == ComponentType.COMPONENTE2) {
				validatePath(root, component);
			}
		}
	}
	
	
	private void validatePath(Path base, FsNode node) {
		Path current = base.resolve(node.dir());
		if(!Files.isDirectory(current)) {
			throw new IllegalStateException("Directorio '" + current + "' no encontrado");
		}
		
		for(FsNode child : node.children()) {
			validatePath(current, child);
		}
		
	}
	
	
}
