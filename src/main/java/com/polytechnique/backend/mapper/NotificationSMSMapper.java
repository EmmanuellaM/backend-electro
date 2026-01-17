package com.polytechnique.backend.mapper;

import com.polytechnique.backend.dto.request.NotificationSMSRequestDTO;
import com.polytechnique.backend.dto.response.NotificationSMSResponseDTO;
import com.polytechnique.backend.entity.NotificationSMS;
import org.springframework.stereotype.Component;

@Component
public class NotificationSMSMapper {

    public NotificationSMS toEntity(NotificationSMSRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        NotificationSMS notification = new NotificationSMS();
        notification.setContenuMessage(dto.getContenuMessage());
        notification.setNumeroDestinataire(dto.getNumeroDestinataire());
        notification.setSucces(dto.getSucces() != null ? dto.getSucces() : false);
        return notification;
    }

    public NotificationSMSResponseDTO toResponseDTO(NotificationSMS notification) {
        if (notification == null) {
            return null;
        }
        NotificationSMSResponseDTO dto = new NotificationSMSResponseDTO();
        dto.setId(notification.getId());
        dto.setContenuMessage(notification.getContenuMessage());
        dto.setNumeroDestinataire(notification.getNumeroDestinataire());
        dto.setDateEnvoi(notification.getDateEnvoi());
        dto.setSucces(notification.getSucces());

        if (notification.getInfirmierLocal() != null) {
            dto.setInfirmierId(notification.getInfirmierLocal().getId());
        }
        if (notification.getDiagnostic() != null) {
            dto.setDiagnosticId(notification.getDiagnostic().getId());
        }
        return dto;
    }
}
