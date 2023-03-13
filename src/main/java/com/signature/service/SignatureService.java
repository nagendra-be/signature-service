package com.signature.service;

import java.awt.FontFormatException;
import java.io.IOException;

import org.springframework.http.ResponseEntity;

import com.signature.model.UploadRequest;

public interface SignatureService {

	ResponseEntity<?> upload(UploadRequest request) throws IOException;

	ResponseEntity<?> getSignatureImage(String name) throws IOException, FontFormatException;

	ResponseEntity<?> download(String accessCode) throws IOException;

	ResponseEntity<?> uploadSigned(UploadRequest request) throws IOException;
	
	ResponseEntity<?> downloadSigned(String accessCode) throws IOException;
}
