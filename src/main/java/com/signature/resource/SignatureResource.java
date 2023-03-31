package com.signature.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadFile(@ModelAttribute UploadRequest request) {
		try {
			return this.signatureService.upload(request);
		} catch (Exception e) {
			return new ResponseEntity<>("Exception occurred while uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/generateimage")
	public ResponseEntity<?> getSignatureImage(@RequestParam String name) {
		try {
			return this.signatureService.getSignatureImage(name);
		} catch (Exception e) {
			return new ResponseEntity<>("Exception occurred while generating signature image",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/download")
	public ResponseEntity<?> downloadFile(@RequestParam String accessCode) {
		try {
			return this.signatureService.download(accessCode);
		} catch (Exception e) {
			return new ResponseEntity<>("Exception occurred while downloading file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/uploadsigned", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadSignedFile(@ModelAttribute UploadRequest request) {
		try {
			return this.signatureService.uploadSigned(request);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Exception occurred while uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/downloadsigned")
	public ResponseEntity<?> downloadSignedFile(@RequestParam String accessCode) {
		try {
			return this.signatureService.downloadSigned(accessCode);
		} catch (Exception e) {
			return new ResponseEntity<>("Exception occurred while downloading file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/list")
	public ResponseEntity<?> getallFileDetails() {
		return this.signatureService.getAllFileDetails();
	}

	@GetMapping(value = "/approve")
	public ResponseEntity<?> approveFile(@RequestParam String accessCode, @RequestParam String status) {
		return this.signatureService.approveFile(accessCode, status);
	}
}
