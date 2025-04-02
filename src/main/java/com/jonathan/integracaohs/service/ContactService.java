package com.jonathan.integracaohs.service;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.jonathan.integracaohs.dto.ContactDTO;
import com.jonathan.integracaohs.dto.HubspotContactRequest;
import com.jonathan.integracaohs.dto.HubspotContactResponse;

@Service
public class ContactService {
	
    private final WebClient webClient;
    
    private final String hubspotApiBaseUrl;
    private final String hubspotContactUri;
    
    public ContactService(WebClient.Builder webClientBuilder, Environment environment) {
    	
    	this.hubspotApiBaseUrl = environment.getProperty("hubspot.api-base-url");
    	this.hubspotContactUri = environment.getProperty("hubspot.contact-uri");
    	
        if (hubspotApiBaseUrl == null ||
        		hubspotContactUri == null) {
            throw new IllegalArgumentException("Confira suas configurações do arquivo application.yml conforme o readme do projeto");
        }
        
        this.webClient = webClientBuilder.baseUrl(hubspotApiBaseUrl).build();

    }

	public HubspotContactResponse createContact(String bearerToken, ContactDTO contact) {
		
		HubspotContactRequest hubspotContactRequest = new HubspotContactRequest(contact);
		
		HubspotContactResponse hubspotContactResponse = hubspotCreateContact(bearerToken, hubspotContactRequest);
		
		return hubspotContactResponse;
	}
	
    private HubspotContactResponse hubspotCreateContact(String bearerToken, HubspotContactRequest hubspotContactRequest) {

            return webClient.post()
                    .uri(hubspotContactUri)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken) 
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(hubspotContactRequest)
                    .retrieve()
                    .bodyToMono(HubspotContactResponse.class)
                    .block();
            
    }
	
	
}
