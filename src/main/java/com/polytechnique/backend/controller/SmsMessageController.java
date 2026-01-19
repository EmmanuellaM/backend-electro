package com.polytechnique.backend.controller;

import com.polytechnique.backend.dto.request.SmsMessageRequestDTO;
import com.polytechnique.backend.dto.response.SmsMessageResponseDTO;
import com.polytechnique.backend.service.SmsMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller pour gérer les SMS envoyés aux infirmiers
 */
@RestController
@RequestMapping("/sms-messages")
@RequiredArgsConstructor
@Tag(name = "SMS Messages", description = "API pour l'envoi et l'historique des SMS aux infirmiers")
@CrossOrigin(origins = "*")
public class SmsMessageController {

    private final SmsMessageService smsMessageService;

    @PostMapping
    @Operation(summary = "Envoyer un SMS", description = "Envoie un SMS à un infirmier (simulé) et l'enregistre dans l'historique")
    public ResponseEntity<SmsMessageResponseDTO> sendSms(@Valid @RequestBody SmsMessageRequestDTO requestDTO) {
        SmsMessageResponseDTO response = smsMessageService.sendSms(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/infirmier/{infirmierId}")
    @Operation(summary = "Historique SMS", description = "Récupère l'historique des SMS envoyés à un infirmier")
    public ResponseEntity<List<SmsMessageResponseDTO>> getHistory(@PathVariable int infirmierId) {
        List<SmsMessageResponseDTO> history = smsMessageService.getHistoryByInfirmier(infirmierId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/infirmier/{infirmierId}/count")
    @Operation(summary = "Nombre de SMS", description = "Récupère le nombre de SMS envoyés à un infirmier")
    public ResponseEntity<Long> getCount(@PathVariable int infirmierId) {
        long count = smsMessageService.countByInfirmier(infirmierId);
        return ResponseEntity.ok(count);
    }
}
