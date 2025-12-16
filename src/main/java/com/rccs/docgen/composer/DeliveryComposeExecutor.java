package com.rccs.docgen.composer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.rccs.docgen.beans.ArtfactsFileBean;
import com.rccs.docgen.beans.DeliveryConfigBean;
import com.rccs.docgen.enums.DeliveryType;
import com.rccs.docgen.enums.DeployType;
import com.rccs.docgen.enums.RequestType;
import com.rccs.docgen.utils.ProcessDocUtils;

public class DeliveryComposeExecutor {
	private static final String TEMPLATE_PATH = "src/main/resources/template/template.docx";
	
	public void execute(String deliveryOutPath, String resourcesPath) {
		try(FileInputStream fis = new FileInputStream(TEMPLATE_PATH)){
			XWPFDocument doc = new XWPFDocument(fis);
			//TODO: configurara el map de los artefactos
			
			List<ArtfactsFileBean> artifacts = ProcessDocUtils.readAllArtifacts(resourcesPath);
			
			DeliveryConfigBean deliveryConfig = new DeliveryConfigBean("DEMANDA 58", 
					"25/12/2025", 
					"Galileo", 
					"", 
					"./FTP", 
					DeliveryType.VERSION, 
					RequestType.REQUEST, 
					DeployType.NEW_VERSION);
					
			DeliveryComposer composer = new DeliveryComposer(doc, artifacts, deliveryConfig);
			composer.process();
			
			FileOutputStream out = new FileOutputStream(deliveryOutPath);
			doc.write(out);
			out.close();
			doc.close();
			fis.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
