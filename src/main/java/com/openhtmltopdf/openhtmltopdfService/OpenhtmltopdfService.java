package com.openhtmltopdf.openhtmltopdfService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void generatePdf(String fileName) throws IOException, FileNotFoundException {

		String htmlContent = new String(
				Files.readAllBytes(Paths.get("src/main/resources/templates/BicReminderCL01.html")));

		Bank bank = new Bank();
		bank.setCardNum("2683");
		bank.setAccNum("00000000000007878");
		bank.setAccBalance("1119.34");

		Map<String, Object> data = new HashMap<>();
		data.put("cardNum", bank.getCardNum());
		data.put("accNum", bank.getAccNum());
		data.put("accBalance", bank.getAccBalance());

		String processedHtml = processTemplate(htmlContent, data);

		PdfRendererBuilder builder = new PdfRendererBuilder();
		builder.useSVGDrawer(new BatikSVGDrawer());

		builder.withHtmlContent(processedHtml, "classpath:/static/");

		try (OutputStream outputStream = new FileOutputStream("target/" + fileName + ".pdf")) {

			builder.toStream(outputStream);
			builder.run();

			try (PDDocument doc = PDDocument.load(new File("target/" + fileName + ".pdf"))) {
				AccessPermission accessPermission = new AccessPermission();
				accessPermission.setCanExtractContent(true);
				accessPermission.setCanModify(true);
				accessPermission.setCanModifyAnnotations(true);
				accessPermission.setCanPrint(true);
				accessPermission.setCanPrintDegraded(true);
				accessPermission.setCanAssembleDocument(true);
				accessPermission.setCanExtractForAccessibility(true);
				accessPermission.setCanFillInForm(true);

				StandardProtectionPolicy spp = new StandardProtectionPolicy("123", "123", accessPermission);
				doc.protect(spp);
				PDDocumentCatalog catalog = doc.getDocumentCatalog();
				
                 System.out.println("catalog----------------"+catalog.toString());
				PDAcroForm form = catalog.getAcroForm();
                System.out.println("form----------------"+form.toString());

				List<PDField> acroFormFields = form.getFields();
                System.out.println("acroFormFields----------------"+acroFormFields.toString());

				for (PDField field : acroFormFields) {
	                System.out.println("field----------------"+field);
					if (field.getFullyQualifiedName().equals("name")) {
						field.setReadOnly(true);
						field.setValue("Pin code :- 517849");
						field.getAcroForm().flatten();

					}
					if (field.getFullyQualifiedName().equals("lastname")) {
						field.setReadOnly(true);
						field.setValue("shubham");
						field.getAcroForm().flatten();

					}
					

				}

				doc.save("target/" + fileName + ".pdf");
			}
		}
	}

	private String processTemplate(String html, Map<String, Object> data) {

		for (Map.Entry<String, Object> entry : data.entrySet()) {
			html = html.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
		}
		return html;
	}
}
