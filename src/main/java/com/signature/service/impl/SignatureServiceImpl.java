package com.signature.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.signature.model.Signature;
import com.signature.model.UploadRequest;
import com.signature.service.SignatureService;

@Service
public class SignatureServiceImpl implements SignatureService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Value("${upload.path}")
	private String uploadDir;

	@Value("${font.path}")
	private String fontPath;

	@Override
	public ResponseEntity<?> upload(UploadRequest request) throws IOException {
		MultipartFile file = request.getFile();
		String name = request.getName();
		String email = request.getEmail();
		String message = request.getMessage();

		// Check if the uploaded file is a PDF document
		if (!file.getContentType().equals(MediaType.APPLICATION_PDF_VALUE)) {
			return new ResponseEntity<>("Uploaded file is not a PDF document", HttpStatus.BAD_REQUEST);
		}

		// Generate a unique file name for the uploaded file
		String fileName = file.getOriginalFilename();
		String uniqueId = UUID.randomUUID().toString();
		String filePath = uploadDir + File.separator + uniqueId + File.separator + fileName;

		// Create a directory with the unique ID
		File directory = new File(uploadDir + File.separator + uniqueId);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// Save the uploaded file to the file system
		File uploadedFile = new File(filePath);
		Files.write(uploadedFile.toPath(), file.getBytes());

		// Store the file path in the MongoDB collection
		Signature signature = new Signature();
		signature.setName(name);
		signature.setEmail(email);
		signature.setPath(filePath);
		this.mongoTemplate.save(signature);

		// Return a success response
		return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getSignatureImage(String name) throws IOException, FontFormatException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(60f);

		// Create a BufferedImage to hold the signature image
		BufferedImage signatureImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = signatureImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setFont(font);

		// Calculate the size of the signature image based on the length of the name
		int textWidth = graphics.getFontMetrics().stringWidth(name);
		int padding = 100;
		int width = textWidth + 2 * padding;
		int height = graphics.getFontMetrics().getHeight();
		signatureImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		graphics = signatureImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setFont(font);

		String signatureName = this.capitalize(name);
		// Calculate the x-coordinate of the starting point of the name to center it in
		// the image
		int startX = (width - textWidth) / 2;
		// Draw the name onto the signature image
		graphics.setColor(Color.BLACK);
		graphics.drawString(signatureName, startX, graphics.getFontMetrics().getAscent());

		// Convert the signature image to a byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(signatureImage, "png", baos);
		byte[] imageData = baos.toByteArray();

		// Create a Resource object from the byte array
		ByteArrayResource resource = new ByteArrayResource(imageData);

		// Return the signature image as a file resource
		return ResponseEntity.ok().contentLength(resource.contentLength()).contentType(MediaType.IMAGE_PNG)
				.body(resource);
	}

	private String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}

		String[] words = str.split("\\s+");
		StringBuilder sb = new StringBuilder();

		for (String word : words) {
			sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
		}

		return sb.toString().trim();
	}

}
