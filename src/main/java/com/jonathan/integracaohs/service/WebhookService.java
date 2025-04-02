package com.jonathan.integracaohs.service;

import java.time.Instant;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.integracaohs.dto.HubspotNotification;
import com.jonathan.integracaohs.util.HubspotSignatureValidator;

@Service
public class WebhookService {
	
	private final HubspotSignatureValidator hubspotSignatureValidator;
	private final ObjectMapper objectMapper;
	private final String webhookMaxTimestamp;
	
    public WebhookService(HubspotSignatureValidator signatureValidator, ObjectMapper objectMapper, Environment environment) {
        this.hubspotSignatureValidator = signatureValidator;
        this.objectMapper = objectMapper;
        this.webhookMaxTimestamp = environment.getProperty("hubspot.webhook.max-timestamp");
    }

	public Boolean isHubspotSignatureValid(String signature, String timestamp, String body) {

		return hubspotSignatureValidator.isValidSignature(signature, timestamp, body);
	}
	
	public Boolean isTimestampExpired(String timestamp) {
		
		long maxTimestamp = 0;
		
		try {
			maxTimestamp = Long.parseLong(webhookMaxTimestamp);
		} catch (NumberFormatException e) {
			System.out.println("Erro na configuração do valor hubspot.webhook.max-timestamp. Informe um tempo em milissegundos");
			return true;  // Considera expirado por segurança
		}
		
		long receivedTimestamp = Long.parseLong(timestamp);
        long currentTimestamp = Instant.now().toEpochMilli();
        
		return (currentTimestamp - receivedTimestamp) > maxTimestamp;
	}

	public void processEvents(String events) {
		
		List<HubspotNotification> eventsList = rawBodyToHubspotNotification(events);
		
		for (HubspotNotification hubspotNotification : eventsList) {
			if (hubspotNotification.getSubscriptionType().equalsIgnoreCase("contact.creation")) {
				// Como não há definição do que tratar ao receber o evento foi feito apenas um print na console para exibir recebimento do evento
				System.out.println("Um contato foi criado no CRM");
			}
		}
		
	}
	
	private List<HubspotNotification> rawBodyToHubspotNotification(String events) {
        try {
        	List<HubspotNotification> eventsList = objectMapper.readValue(events, objectMapper.getTypeFactory().constructCollectionType(List.class, HubspotNotification.class));
        	return eventsList;
        } catch (Exception e) {
           System.out.println("Erro ao converter lista de notificações do webhook do Hubspot - " + e.getMessage());
           throw new RuntimeException("Erro ao converter lista de notificações do webhook do Hubspot");
        }
		
	}

}
