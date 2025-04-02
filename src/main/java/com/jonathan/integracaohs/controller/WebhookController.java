package com.jonathan.integracaohs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jonathan.integracaohs.service.WebhookService;

@RestController
@RequestMapping(value = "/webhook")
public class WebhookController {

	@Autowired
	private WebhookService webhookService;

	@RequestMapping(value="/events", method=RequestMethod.POST)
	public ResponseEntity<String> processEvents(@RequestHeader("X-HubSpot-Signature-V3") String signature,
				@RequestHeader("X-HubSpot-Request-Timestamp") String timestamp,
            	@RequestBody String body){
		
		if (webhookService.isTimestampExpired(timestamp)) {
	        return ResponseEntity
	                .status(HttpStatus.UNPROCESSABLE_ENTITY)
	                .body("A requisição deveria ter sido enviada dentro dos últimos 5 minutos.");
		}
		
		if (!webhookService.isHubspotSignatureValid(signature, timestamp, body)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Notificação sem assinatura válida do Hubspot.");
		}
		
		webhookService.processEvents(body);
		
		return ResponseEntity.ok().body("Notificação recebida");
	}
	
}
