package com.jonathan.integracaohs.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class HubspotSignatureValidator {
	
	private final String clientSecret;
	private final String webhookUrl;

	public HubspotSignatureValidator(Environment environment) {
		this.clientSecret = environment.getProperty("CLIENT_SECRET");
		this.webhookUrl = environment.getProperty("hubspot.webhook.url");
	}
	
    public boolean isValidSignature(String signature, String timestamp, String body) {
        try {
            String data = "POST" + webhookUrl + body + timestamp;
            String calculatedSignature = calculateHMACSHA256(data, clientSecret);
            return calculatedSignature.equals(signature);
        } catch (Exception e) {
            System.out.println("Erro ao calcular assinatura do Hubspot - " + e.getMessage());
            return false;
        }
    }

    private String calculateHMACSHA256(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);

        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}
