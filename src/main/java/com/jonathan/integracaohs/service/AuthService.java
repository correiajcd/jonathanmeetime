package com.jonathan.integracaohs.service;

import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.jonathan.integracaohs.dto.TokenResponse;

@Service
public class AuthService {
	
    private final String port;
    private final String hubspotAuthUrl;
    private final String hubspotRedirectUrl;
    private final String hubspotApiBaseUrl;
    private final String hubspotTokenUri;
    private final Environment environment;
    private final WebClient webClient;
    
    public AuthService(WebClient.Builder webClientBuilder, Environment environment) {
    	
    	this.environment = environment;
    	this.port = environment.getProperty("server.port");
    	this.hubspotAuthUrl = environment.getProperty("hubspot.auth-url");
    	this.hubspotRedirectUrl = environment.getProperty("hubspot.redirect-url");
    	this.hubspotApiBaseUrl = environment.getProperty("hubspot.api-base-url");
    	this.hubspotTokenUri = environment.getProperty("hubspot.token-uri");
    	
        if (port == null || 
        		hubspotAuthUrl == null || 
        		hubspotRedirectUrl == null || 
        		hubspotApiBaseUrl == null ||
        		hubspotTokenUri == null) {
            throw new IllegalArgumentException("Confira suas configurações do arquivo application.yml conforme o readme do projeto");
        }
    	
        this.webClient = webClientBuilder.baseUrl(hubspotApiBaseUrl).build();

    }
    
    private String getClientId() {
        return environment.getProperty("CLIENT_ID");
    }
    
    private String getClientSecret() {
        return environment.getProperty("CLIENT_SECRET");
    }
    
    private Boolean checkEnvironmentVariables() {
    	
    	if (getClientId() == null || getClientSecret() == null) {
    		return false;
    	}
    	
    	return true;
    	
    }
    
    public String createAuthUrl() {
    	
    	if (checkEnvironmentVariables()) {
    			
    		return hubspotAuthUrl +
    				"?client_id=" + getClientId() +
    				"&scope=crm.objects.contacts.read%20crm.objects.contacts.write" + //Scope fixo devido a única função do desafio ser criação de contato
    				"&redirect_uri=" + hubspotRedirectUrl;

    	} else {
    		return "Variáveis de ambiente CLIENT_ID ou CLIENT_SECRET não definidas";
    	}
    	
    	
    }

	public String processCallbackCode(String code) {
		TokenResponse token = new TokenResponse();
		try {
			token = getToken(code);
		} catch (Exception e) {
			return "Erro ao gerar o token - " + e.getMessage();
		}
		
		return "Bearer token de acesso: " + token.getAccess_token();
	}
	
    private TokenResponse getToken(String code) {
        return webClient.post()
        		.uri(hubspotTokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(
                    "grant_type=authorization_code" +
                            "&client_id=" + getClientId() +
                            "&client_secret=" + getClientSecret() +
                            "&redirect_uri=" + hubspotRedirectUrl +
                            "&code=" + code
                        )
            .retrieve()
            .bodyToMono(TokenResponse.class)
            .block();
    }
    
}
