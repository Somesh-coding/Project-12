package com.somesh.Project1.controller;

import com.somesh.Project1.service.MessageService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<Map<String, Object>> getAllMessages() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("messages")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get();

        List<Map<String, Object>> messages = new ArrayList<>();
        for (DocumentSnapshot doc : future.get().getDocuments()) {
            messages.add(doc.getData());
        }

        return messages;
    }

    @PostMapping("/save")
    public String saveTestMessage(@RequestParam String sender, @RequestParam String message) {
        messageService.saveMessage(sender, message);
        return "Message saved to Firebase!";
    }
}
