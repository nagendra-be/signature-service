package com.signature.resource;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.signature.model.UploadRequest;
import com.signature.service.SignatureService;

@RestController
@RequestMapping("/api/v1/signature")
public class SignatureResource {

	@Autowired
	private SignatureService signatureService;

	@CrossOrigin(value = "http://localhost:3000")
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadFile(@ModelAttribute UploadRequest request) {
		try {
			return this.signatureService.upload(request);
		} catch (Exception e) {
			return new ResponseEntity<>("Exception occurred while uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin(value = "http://localhost:3000")
	@GetMapping(value = "/generateimage")
	public ResponseEntity<?> getSignatureImage(@RequestParam String name) {
		try {
			return this.signatureService.getSignatureImage(name);
		} catch (Exception e) {
			return new ResponseEntity<>("Exception occurred while generating signature image",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
