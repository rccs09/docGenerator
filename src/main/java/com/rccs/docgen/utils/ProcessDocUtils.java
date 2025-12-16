package com.rccs.docgen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.rccs.docgen.beans.ArtfactsFileBean;
import com.rccs.docgen.beans.HeaderConfigBean;
import com.rccs.docgen.enums.ArtifactsType;
import com.rccs.docgen.enums.ComponentType;
import com.rccs.docgen.enums.HeaderType;
import com.rccs.docgen.enums.ScopeType;

public class ProcessDocUtils {

	/**
	 * Cambia el formato de la fecha al patron enviado
	 * 
	 * @param date            - fecha como string
	 * @param originalPattern - patron de la fecha
	 * @param newPattern      - nuevo patron para la fecha
	 * @return
	 */
	public static String changeDateStringFormat(String date, String originalPattern, String newPattern) {
		DateTimeFormatter inDf = DateTimeFormatter.ofPattern(originalPattern);
		DateTimeFormatter outDf = DateTimeFormatter.ofPattern(newPattern, new Locale("es", "ES"));
		LocalDate localDate = LocalDate.parse(date, inDf);
		return localDate.format(outDf);
	}

	/**
	 * Identifica si una tabla tiene en su primera fila y columna uno de los header
	 * definidos
	 * 
	 * @param table         - Tabla que se va a analizar
	 * @param headersConfig - Configuracion de los headers del documento
	 * @return Configuracion del header de la tabla si se encuentra, si no retorna
	 *         null
	 */
	public static HeaderConfigBean getTableHeader(XWPFTable table, Map<String, HeaderConfigBean> headersConfig) {
		XWPFTableRow firstRow = table.getRow(0);
		XWPFTableCell firstCell = firstRow.getCell(0);
		XWPFParagraph paragraph = firstCell.getParagraphArray(0);

		String paragraphText = getCompleteRunFromParagraph(paragraph);
		if (paragraphText != null) {
			for (Map.Entry<String, HeaderConfigBean> entry : headersConfig.entrySet()) {
				if (paragraphText.contains(entry.getKey())) {
					return entry.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Un parrafo puede tener muchos RUN y estos el texto de la celda, este metodo
	 * retorna el texto acumulado de todos los RUN de un parrafo
	 * 
	 * @param paragraph - Parrafo del que se desea extraer el texto
	 * @return texto completo del parrafo
	 */
	public static String getCompleteRunFromParagraph(XWPFParagraph paragraph) {
		String paragraphText = null;
		List<XWPFRun> runs = paragraph.getRuns();
		if (runs != null && !runs.isEmpty()) {
			StringBuilder fullText = new StringBuilder();
			for (XWPFRun run : runs) {
				String text = run.getText(0);
				if (text != null) {
					fullText.append(run.getText(0));
				}
			}
			paragraphText = fullText.toString();

		}
		return paragraphText;
	}

	/**
	 * Remplaza el run de un parrafo con el texto nuevo
	 * 
	 * @param p       - parrafo dentro de una celda
	 * @param newText - nuevo texto
	 */
	public static void replaceRun(XWPFParagraph p, String newText) {
		List<XWPFRun> runs = p.getRuns();

		// almaceno el estilo del run actual
		XWPFRun firstRun = runs.get(0);
		String fontFamily = firstRun.getFontFamily();
		Double fontSize = firstRun.getFontSizeAsDouble();
		boolean bold = firstRun.isBold();
		boolean italic = firstRun.isItalic();
		String color = firstRun.getColor();
		UnderlinePatterns underline = firstRun.getUnderline();

		// elimino los runs del parrafo
		for (int i = runs.size() - 1; i >= 0; i--) {
			p.removeRun(i);
		}

		String[] lines = newText.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (i > 0)
				p.createRun().addBreak();

			String[] boldTexts = lines[i].split("(?<=\\*\\*)|(?=\\*\\*)");
			boolean inBold = false;
			for (String boldText : boldTexts) {
				if (boldText.equals("**")) {
					inBold = !inBold;
					continue;
				}

				XWPFRun newRun = p.createRun();
				// copio el estilo
				newRun.setBold(inBold || bold);
				newRun.setItalic(italic);
				newRun.setUnderline(underline);
				if (fontFamily != null)
					newRun.setFontFamily(fontFamily);
				if (fontSize != null)
					newRun.setFontSize(fontSize);
				if (color != null)
					newRun.setColor(color);
				newRun.setText(boldText);

			}
		}
	}

	
	
	
	
	public static List<ArtfactsFileBean> readAllArtifacts(String pathDelivery) throws IOException {
		Path pathRoot = Paths.get(pathDelivery);
		List<ArtfactsFileBean> result = new ArrayList<>();

		Files.walk(pathRoot)
			.filter(p -> !p.getFileName().toString().equalsIgnoreCase(".DS_Store"))
			.filter(Files::isRegularFile).forEach(path -> {
			try {
				ArtfactsFileBean bean = createBeanFromPath(path, pathRoot);
				if (bean != null) {
					result.add(bean);
				}
			} catch (Exception e) {
				System.err.println("Error procesando archivo: " + path + ", " + e.getMessage());
			}
		});

		return result;
	}

	private static ArtfactsFileBean createBeanFromPath(Path path, Path ROOT) throws IOException, NoSuchAlgorithmException {
		String completePath = path.toString();
		String fileName = path.getFileName().toString();
		Path relative = ROOT.relativize(path);

		String componentFolder = relative.getName(0).toString();
		ComponentType componentType = ComponentType.fromFolder(componentFolder)
				.orElseThrow(() -> new IllegalArgumentException("No se pudo identificar el tipo de componente para: " + fileName));
		if (componentType == null)
			return null;

		ScopeType scopeType = determineScope(relative);
		ArtifactsType artifactsType = ArtifactsType.fromFileName(fileName, scopeType)
				.orElseThrow(() -> new IllegalArgumentException("No se pudo identificar el tipo de artefacto para: " + fileName));
		if (artifactsType == null)
			return null;

		String md5 = generateMd5(path.toFile());
		String ftpPath = buildFtpPath(relative);

		ArtfactsFileBean bean = new ArtfactsFileBean();
		bean.setCompletePath(completePath);
		bean.setFileName(fileName);
		bean.setComponentType(componentType);
		bean.setArtifactsType(artifactsType);
		bean.setScopeType(scopeType);
		bean.setMd5(md5);
		bean.setFtpPath(ftpPath);

		return bean;
	}

	private static ScopeType determineScope(Path relative) {
		for (Path part : relative) {
			String value = part.toString().toLowerCase();
			if (value.equals("reverso"))
				return ScopeType.REVERSE;
			if (value.equals("ejecucion"))
				return ScopeType.EXECUTION;
		}
		return ScopeType.GENERAL; // default
	}

	private static String buildFtpPath(Path relative) {
		Path parent = relative.getParent();
		if (parent == null)
			return "";
		return "/ftp/" + parent.toString().replace("\\", "/");
	}

	private static String generateMd5(File file) throws IOException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		try(InputStream is = new FileInputStream(file)){
			byte[] buffer = new byte[8192];
			int read;
			while((read = is.read(buffer)) != -1 ) {
				digest.update(buffer, 0, read);
			}
		}
		
		byte[] hasBytes = digest.digest();
		StringBuilder sb = new StringBuilder();
		for (byte b : hasBytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	//obtiene una lista de artefcatos en base a un header o que pertenecen a ese header
	public static List<ArtfactsFileBean> filterArtifactByHeader(List<ArtfactsFileBean> artefactos, HeaderType header) {
	    return artefactos.stream()
	        .filter(a -> a.getArtifactsType() != null)
	        .filter(a -> a.getArtifactsType().getHeaderTypes().contains(header))
	        .collect(Collectors.toList());
	}

}
