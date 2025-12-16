package com.rccs.docgen.composer;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.rccs.docgen.beans.ArtfactsFileBean;
import com.rccs.docgen.utils.ProcessDocUtils;

public class DeliveryComposeExecutorTest {
	private static final String OUT_PATH = "src/test/resources/out/Liberacion.docx";
	private static final String ARTIFACTS_PATH = "src/main/resources/entrega";
	
	@Test
	public void testExecute() {
		System.out.println("******************** INICIA EL PROCESO ********************");
		DeliveryComposeExecutor executor = new DeliveryComposeExecutor();
		executor.execute(OUT_PATH,ARTIFACTS_PATH);
		
		System.out.println("******************** FINALIZA EL PROCESO ********************");
	}
	
	
	@Test
	public void testBorrar() {
		System.out.println("******************** INICIA EL PROCESO ********************");
		try {
			List<ArtfactsFileBean> lista = ProcessDocUtils.readAllArtifacts(ARTIFACTS_PATH);
			for (ArtfactsFileBean bean : lista) {
				System.out.println(bean);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("******************** FINALIZA EL PROCESO ********************");
	}

}
