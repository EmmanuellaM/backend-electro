package com.polytechnique.backend.controller;

import com.polytechnique.backend.entity.UplinkMessage;
import com.polytechnique.backend.repository.UplinkMessageRepository;
import com.polytechnique.backend.service.UplinkMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/uplink-messages")
@RequiredArgsConstructor
@Tag(name = "Uplink Messages", description = "Messages LoRaWAN reçus de ChirpStack")
public class UplinkMessageController {

    private final UplinkMessageRepository uplinkMessageRepository;
    private final UplinkMessageService uplinkMessageService;

    @GetMapping
    @Operation(summary = "Liste tous les messages uplink", description = "Retourne tous les messages LoRaWAN reçus")
    public ResponseEntity<List<UplinkMessage>> getAllMessages() {
        return ResponseEntity.ok(uplinkMessageRepository.findAll());
    }

    @GetMapping("/device/{devEui}")
    @Operation(summary = "Messages par DevEUI", description = "Retourne les messages d'un dispositif spécifique")
    public ResponseEntity<List<UplinkMessage>> getMessagesByDevice(@PathVariable String devEui) {
        return ResponseEntity.ok(uplinkMessageService.getMessagesByDevEui(devEui));
    }

    @GetMapping("/unprocessed")
    @Operation(summary = "Messages non traités", description = "Retourne les messages en attente de traitement")
    public ResponseEntity<List<UplinkMessage>> getUnprocessedMessages() {
        return ResponseEntity.ok(uplinkMessageRepository.findByProcessedFalseOrderByCreatedAtAsc());
    }

    @GetMapping("/unprocessed/count")
    @Operation(summary = "Compte des messages non traités")
    public ResponseEntity<Map<String, Long>> getUnprocessedCount() {
        long count = uplinkMessageService.getUnprocessedCount();
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PostMapping("/process")
    @Operation(summary = "Forcer le traitement des messages", description = "Déclenche manuellement le traitement de tous les messages non traités")
    public ResponseEntity<Map<String, String>> triggerProcessing() {
        uplinkMessageService.processUnprocessedMessages();
        return ResponseEntity.ok(Map.of("status", "success", "message", "Traitement déclenché"));
    }
}
