package com.signature.service;

import org.springframework.http.ResponseEntity;

import com.signature.model.CreateCustomerRequest;
import com.signature.model.UpdateCustomerRequest;

public interface CustomerService {

	ResponseEntity<?> createCustomer(CreateCustomerRequest request);

	ResponseEntity<?> getCustomers(String searchInput);
	
	ResponseEntity<?> getCustomer(String customerId);

	ResponseEntity<?> updateCustomer(UpdateCustomerRequest request);

	ResponseEntity<?> deleteCustomer(String customerId);
}
