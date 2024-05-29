package com.openhtmltopdf.openhtmltopdfService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
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

	    // Initialize PdfRendererBuilder
	    PdfRendererBuilder builder = new PdfRendererBuilder();
		builder.useSVGDrawer(new BatikSVGDrawer());

	    // Set HTML file
	    builder.withFile(htmlFile);

	    // Setup output stream for the PDF
	    try (OutputStream outputStream = new FileOutputStream("target/" + fileName + ".pdf")) {
	        builder.toStream(outputStream);

	        // Create PDF
	        builder.run();

	        try (PDDocument doc = PDDocument.load(new File("target/" + fileName + ".pdf"))) {
	            AccessPermission accessPermission = new AccessPermission();
	            accessPermission.setCanExtractContent(false);
	            accessPermission.setCanModify(false);
	            accessPermission.setCanModifyAnnotations(false);
	            accessPermission.setCanPrint(false);
	            accessPermission.setCanPrintDegraded(false);
	            accessPermission.setCanAssembleDocument(false);
	            accessPermission.setCanExtractForAccessibility(false);
	            accessPermission.setCanFillInForm(false);

	            StandardProtectionPolicy spp = new StandardProtectionPolicy("123", "123", accessPermission);
	            doc.protect(spp);

	            doc.save("target/" + fileName + ".pdf");
	        }
	    }
	}
}
