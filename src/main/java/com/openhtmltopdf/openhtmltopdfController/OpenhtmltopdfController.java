package com.openhtmltopdf.openhtmltopdfController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openhtmltopdf.openhtmltopdfService.OpenhtmltopdfService;


@RestController
@RequestMapping("/rest/api/v1")
public class OpenhtmltopdfController {

	@Autowired
	private OpenhtmltopdfService openhtmltopdfService;

	/**
	 * Generate pdf.
	 *
	 * @param fileName the file name
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@PostMapping("/generate-pdf/{fileName}")
	public ResponseEntity<String> generatePdf(@PathVariable("fileName") String fileName) throws Exception {

		openhtmltopdfService.generatePdf(fileName);

		return ResponseEntity.ok("The HTML file successfully converted to a PDF file.");

	}
}
