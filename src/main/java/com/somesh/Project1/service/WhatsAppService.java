package com.somesh.Project1.service;

import org.springframework.beans.factory.annotation.Value; // ✅ Correct import
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    @Value("${whatsapp.token}")
    private String accessToken;

    @Value("${whatsapp.phone.id}")
    private String phoneNumberId;

    private final String BASE_URL = "https://graph.facebook.com/v17.0/";

    public void sendMessage(String toPhoneNumber, String message) {
        String url = BASE_URL + phoneNumberId + "/messages";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", toPhoneNumber);
        body.put("type", "text");
        body.put("text", Map.of("body", message));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); // ✅ Authorization

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("✅ Sent WhatsApp message: " + response.getBody());
    }
}
