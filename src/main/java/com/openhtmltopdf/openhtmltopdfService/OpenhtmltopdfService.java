package com.openhtmltopdf.openhtmltopdfService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.stereotype.Service;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;


@Service
public class OpenhtmltopdfService {

	/**
	 * Generate pdf.
	 *
	 * @param fileName the file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void generatePdf(String fileName) throws IOException {

		File htmlFile = new File("src/main/resources/static/BicReminderCL01.html");

		if (!htmlFile.exists()) {
			System.out.println("========= HTML file not found ===========");
			throw new FileNotFoundException("HTML file not found: " + htmlFile.getAbsolutePath());
		}

		 //  handle the HTML to PDF conversion
		PdfRendererBuilder builder = new PdfRendererBuilder();
		builder.useSVGDrawer(new BatikSVGDrawer());

		// specify the HTML file that you want to convert to a PDF
		builder.withFile(htmlFile);
		
		OutputStream outputStream = new FileOutputStream("target/" + fileName + ".pdf");
		builder.toStream(outputStream);

		builder.run();
		outputStream.close();
     
	}
}
