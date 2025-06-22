package com.somesh.Project1.controller;

import com.somesh.Project1.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private WhatsAppService whatsAppService;

    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        try {
            String messageText = extractMessage(payload);
            String senderPhone = extractSender(payload);

            // Do something useful
            whatsAppService.sendMessage(senderPhone, "Reply: " + messageText);

            return ResponseEntity.ok("Message received");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        }
    }

    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam(name = "hub.mode") String mode,
            @RequestParam(name = "hub.verify_token") String token,
            @RequestParam(name = "hub.challenge") String challenge
    ) {
        if ("subscribe".equals(mode) && "whatsapp123".equals(token)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Verification failed");
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("🔍 Incoming payload: " + payload);
        String message = extractMessage(payload);
        String sender = extractSender(payload);
        System.out.println("📩 From: " + sender + " | Message: " + message);
        return ResponseEntity.ok("✅ Webhook received");
    }

    private String extractMessage(Map<String, Object> payload) {
        try {
            List<Map<String, Object>> entries = (List<Map<String, Object>>) payload.get("entry");
            if (entries == null || entries.isEmpty()) return "No entries";

            Map<String, Object> entry = entries.get(0);
            List<Map<String, Object>> changes = (List<Map<String, Object>>) entry.get("changes");
            if (changes == null || changes.isEmpty()) return "No changes";

            Map<String, Object> change = changes.get(0);
            Map<String, Object> value = (Map<String, Object>) change.get("value");
            if (value == null) return "No value";

            List<Map<String, Object>> messages = (List<Map<String, Object>>) value.get("messages");
            if (messages == null || messages.isEmpty()) return "No messages";

            Map<String, Object> message = messages.get(0);
            Map<String, Object> text = (Map<String, Object>) message.get("text");
            if (text == null) return "No text";

            return (String) text.get("body");

        } catch (Exception e) {
            e.printStackTrace(); // Log error
            return "❌ Invalid payload format";
        }
    }


    private String extractSender(Map<String, Object> payload) {
        try {
            List<Map<String, Object>> entries = (List<Map<String, Object>>) payload.get("entry");
            if (entries == null || entries.isEmpty()) return "unknown";

            Map<String, Object> entry = entries.get(0);
            List<Map<String, Object>> changes = (List<Map<String, Object>>) entry.get("changes");
            if (changes == null || changes.isEmpty()) return "unknown";

            Map<String, Object> change = changes.get(0);
            Map<String, Object> value = (Map<String, Object>) change.get("value");
            if (value == null) return "unknown";

            List<Map<String, Object>> messages = (List<Map<String, Object>>) value.get("messages");
            if (messages == null || messages.isEmpty()) return "unknown";

            Map<String, Object> message = messages.get(0);
            return (String) message.get("from");
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }

}
