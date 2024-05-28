package com.openhtmltopdf.openhtmltopdfService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.stereotype.Service;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;


@Service
public class OpenhtmltopdfService {

	/**
	 * Generate pdf.
	 *
	 * @param fileName the file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void generatePdf(String fileName) throws IOException {

		File htmlFile = new File("src/main/resources/static/html/demo.html");

		if (!htmlFile.exists()) {
			System.out.println("========= HTML file not found ===========");
			throw new FileNotFoundException("HTML file not found: " + htmlFile.getAbsolutePath());
		}

		PdfRendererBuilder builder = new PdfRendererBuilder();

		builder.withFile(htmlFile);
		OutputStream outputStream = new FileOutputStream("target/" + fileName + ".pdf");
		builder.toStream(outputStream);

		builder.run();
		outputStream.close();

	}
}
