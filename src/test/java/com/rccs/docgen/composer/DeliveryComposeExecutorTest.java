package com.rccs.docgen.composer;

import org.junit.Test;

import com.rccs.docgen.beans.DeliveryConfigBean;
import com.rccs.docgen.enums.DeliveryType;
import com.rccs.docgen.enums.DeployType;
import com.rccs.docgen.enums.RequestType;

public class DeliveryComposeExecutorTest {
	private static final String OUT_PATH = "src/test/resources/out/Liberacion.docx";
	private static final String ARTIFACTS_PATH = "src/test/resources/in/version";
	
	@Test
	public void testExecuteVersion() {
		System.out.println("******************** INICIA EL PROCESO ********************");
		
		String versionProductiva = "Verion 1.0.1";
		String fechaEntrega = "15/02/2026";
		String responsable = "Roberto C.";
		String proceso = "Proceso Inicial";
		String ftp = "fptProduccion";
		String ftpBasePath = "./produccion/";
		DeliveryType deliveryType = DeliveryType.VERSION;
		RequestType requestType = RequestType.REQUEST;
		DeployType deployType = DeployType.NEW_VERSION;
		
		DeliveryConfigBean deliveryConfig = new DeliveryConfigBean(versionProductiva, fechaEntrega, 
				responsable, proceso, ftp, ftpBasePath, 
				deliveryType, requestType, deployType);
		
		
		
		DeliveryComposeExecutor executor = new DeliveryComposeExecutor();
		executor.execute(OUT_PATH, ARTIFACTS_PATH, deliveryConfig);
		
		System.out.println("******************** FINALIZA EL PROCESO ********************");
	}
	
	
//	@Test
//	public void testBorrar() {
//		System.out.println("******************** INICIA EL PROCESO ********************");
//		try {
//			List<ArtfactsFileBean> lista = ProcessDocUtils.readAllArtifacts(ARTIFACTS_PATH);
//			for (ArtfactsFileBean bean : lista) {
//				System.out.println(bean);
//			}
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println("******************** FINALIZA EL PROCESO ********************");
//	}

}
