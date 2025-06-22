package com.somesh.Project1.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MessageService {

    public void saveMessage(String from, String message) {
        Firestore db = FirestoreClient.getFirestore();

        Map<String, Object> data = new HashMap<>();
        data.put("sender", from);
        data.put("message", message);
        data.put("timestamp", new Date());

        ApiFuture<DocumentReference> result = db.collection("messages").add(data);
        System.out.println("✅ Message saved to Firestore");
    }
}
