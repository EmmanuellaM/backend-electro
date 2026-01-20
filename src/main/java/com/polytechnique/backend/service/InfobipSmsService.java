package com.polytechnique.backend.service;

public interface InfobipSmsService {
    void sendSms(String recipientNumber, String messageText);
}
