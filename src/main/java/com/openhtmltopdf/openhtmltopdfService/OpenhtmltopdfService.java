package com.openhtmltopdf.openhtmltopdfService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Service;

import com.openhtmltopdf.model.Bank;
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
               Bank bank =new Bank();
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

				PDDocumentCatalog catalog = doc.getDocumentCatalog();

				PDAcroForm form = catalog.getAcroForm();

				List<PDField> acroFormFields = form.getFields();

				System.out.println("acroFormFields" +acroFormFields);
				
				System.out.println(String.format("found %d acroFrom fields", acroFormFields.size()));

				for (PDField field : acroFormFields) {
					if (field.getFullyQualifiedName().equals("name")) {
						field.setReadOnly(true);
						field.setValue("shubham");
					}
					
					
					doc.save("target/" + fileName + ".pdf");
				}

				
				
			}
		}

	}
}
