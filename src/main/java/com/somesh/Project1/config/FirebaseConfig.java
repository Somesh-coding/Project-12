package com.somesh.Project1.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        InputStream serviceAccount = getClass()
                .getClassLoader()
                .getResourceAsStream("firebase/serviceAccountKey.json");

        if (serviceAccount == null) {
            throw new RuntimeException("🔥 Firebase serviceAccountKey.json not found in classpath!");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase initialized successfully.");
        }
    }
}
