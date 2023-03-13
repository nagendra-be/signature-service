package com.signature.service;

public interface EmailService {

	void triggerEmail();

	void sendEmail(String subject, String to, String message);

}
