package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.NotificationSMSRequestDTO;
import com.polytechnique.backend.dto.response.NotificationSMSResponseDTO;
import com.polytechnique.backend.entity.Diagnostic;
import com.polytechnique.backend.entity.InfirmierLocal;

import java.util.List;

public interface NotificationSMSService {
    NotificationSMSResponseDTO createNotification(NotificationSMSRequestDTO requestDTO);

    void createAutomaticNotification(Diagnostic diagnostic, InfirmierLocal infirmier);

    List<NotificationSMSResponseDTO> getAllNotifications();

    NotificationSMSResponseDTO getNotificationById(int id);
}
