package com.jonathan.integracaohs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.jonathan.integracaohs.dto.ContactDTO;
import com.jonathan.integracaohs.service.ContactService;

@RestController
@RequestMapping(value = "/contact")
public class ContactController {
	
	@Autowired
	private ContactService contactService;

	@RequestMapping(value="/create", method=RequestMethod.POST)
	public ResponseEntity<?> processCallbackCode(@RequestHeader("Authorization") String authorizationHeader, 
            @RequestBody ContactDTO request){
		
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(contactService.createContact(authorizationHeader, request));
		} catch (WebClientResponseException e) {
			return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());
		}

	}
	
}
