package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.NotificationSMSRequestDTO;
import com.polytechnique.backend.dto.response.NotificationSMSResponseDTO;
import com.polytechnique.backend.service.NotificationSMSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications SMS", description = "Historique et gestion des SMS envoyés aux infirmiers")
public class NotificationSMSController {

    private final NotificationSMSService notificationService;

    @PostMapping
    public ResponseEntity<NotificationSMSResponseDTO> createNotification(
            @Valid @RequestBody NotificationSMSRequestDTO requestDTO) {
        NotificationSMSResponseDTO response = notificationService.createNotification(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détails d'une notification", description = "Retourne le contenu et le statut d'un SMS par son ID.")
    public ResponseEntity<NotificationSMSResponseDTO> getNotificationById(@PathVariable int id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping
    @Operation(summary = "Historique des notifications", description = "Liste toutes les notifications SMS envoyées par le système.")
    public ResponseEntity<List<NotificationSMSResponseDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
}
