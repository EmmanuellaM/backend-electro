package com.polytechnique.backend.service.impl;

import com.polytechnique.backend.service.InfobipSmsService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class InfobipSmsServiceImpl implements InfobipSmsService {

    @Value("${infobip.api.key}")
    private String apiKey;

    @Value("${infobip.api.base-url}")
    private String baseUrl;

    @Value("${infobip.sender.id}")
    private String senderId;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public void sendSms(String recipientNumber, String messageText) {
        MediaType mediaType = MediaType.parse("application/json");

        // Escape content
        String escapedText = escapeJson(messageText);

        // Build JSON body manually
        String jsonBody = String.format(
                "{\"messages\":[{\"from\":\"%s\",\"destinations\":[{\"to\":\"%s\"}],\"text\":\"%s\"}]}",
                senderId, recipientNumber, escapedText);

        RequestBody body = RequestBody.create(jsonBody, mediaType);
        Request request = new Request.Builder()
                .url(baseUrl + "/sms/2/text/advanced")
                .method("POST", body)
                .addHeader("Authorization", "App " + apiKey)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "null";
                log.error("Failed to send SMS via Infobip: Code={}, Body={}", response.code(), errorBody);
                throw new RuntimeException("Failed to send SMS: " + response.message());
            }
            log.info("SMS sent successfully to " + recipientNumber);
            if (response.body() != null)
                response.body().close();
        } catch (IOException e) {
            log.error("Error sending SMS", e);
            throw new RuntimeException("Error sending SMS", e);
        }
    }

    private String escapeJson(String raw) {
        if (raw == null)
            return "";
        return raw.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
