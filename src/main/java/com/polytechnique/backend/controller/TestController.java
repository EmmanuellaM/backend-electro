// package com.polytechnique.backend.controller;

// import com.polytechnique.backend.entity.Diagnostic;
// import com.polytechnique.backend.entity.Dispositif;
// import com.polytechnique.backend.entity.Medecin;
// import com.polytechnique.backend.entity.Parametres;
// import com.polytechnique.backend.repository.DiagnosticRepository;
// import com.polytechnique.backend.repository.DispositifRepository;
// import com.polytechnique.backend.repository.MedecinRepository;
// import com.polytechnique.backend.repository.ParametresRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// /**
//  * Controller de test pour vérifier le mapping des entités
//  * 
//  * ⚠️ CE CONTROLLER EST UNIQUEMENT POUR LES TESTS
//  * Il expose directement les entités, ce qui n'est pas recommandé en production
//  * En production, utilise des DTOs !
//  */
// @RestController
// @RequestMapping("/test")
// public class TestController {

//     @Autowired
//     private MedecinRepository medecinRepository;

//     @Autowired
//     private DispositifRepository dispositifRepository;

//     @Autowired
//     private ParametresRepository parametresRepository;

//     @Autowired
//     private DiagnosticRepository diagnosticRepository;

//     // ============================================
//     // ENDPOINTS DE TEST POUR MEDECIN
//     // ============================================

//     /**
//      * Tester la création d'un médecin
//      * POST /api/test/medecins
//      */
//     @PostMapping("/medecins")
//     public ResponseEntity<Medecin> createMedecin(@RequestBody Medecin medecin) {
//         Medecin saved = medecinRepository.save(medecin);
//         return ResponseEntity.status(HttpStatus.CREATED).body(saved);
//     }

//     /**
//      * Récupérer tous les médecins
//      * GET /api/test/medecins
//      */
//     @GetMapping("/medecins")
//     public ResponseEntity<List<Medecin>> getAllMedecins() {
//         List<Medecin> medecins = medecinRepository.findAll();
//         return ResponseEntity.ok(medecins);
//     }

//     /**
//      * Récupérer un médecin par ID
//      * GET /api/test/medecins/{id}
//      */
//     @GetMapping("/medecins/{id}")
//     public ResponseEntity<Medecin> getMedecinById(@PathVariable Long id) {
//         return medecinRepository.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     // ============================================
//     // ENDPOINTS DE TEST POUR DISPOSITIF
//     // ============================================

//     /**
//      * Tester la création d'un dispositif
//      * POST /api/test/dispositifs
//      */
//     @PostMapping("/dispositifs")
//     public ResponseEntity<Dispositif> createDispositif(@RequestBody Dispositif dispositif) {
//         Dispositif saved = dispositifRepository.save(dispositif);
//         return ResponseEntity.status(HttpStatus.CREATED).body(saved);
//     }

//     /**
//      * Récupérer tous les dispositifs
//      * GET /api/test/dispositifs
//      */
//     @GetMapping("/dispositifs")
//     public ResponseEntity<List<Dispositif>> getAllDispositifs() {
//         List<Dispositif> dispositifs = dispositifRepository.findAll();
//         return ResponseEntity.ok(dispositifs);
//     }

//     /**
//      * Récupérer un dispositif par ID
//      * GET /api/test/dispositifs/{id}
//      */
//     @GetMapping("/dispositifs/{id}")
//     public ResponseEntity<Dispositif> getDispositifById(@PathVariable Long id) {
//         return dispositifRepository.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     // ============================================
//     // ENDPOINTS DE TEST POUR PARAMETRES
//     // ============================================

//     /**
//      * Tester la création de paramètres
//      * POST /api/test/parametres
//      */
//     @PostMapping("/parametres")
//     public ResponseEntity<Parametres> createParametres(@RequestBody Parametres parametres) {
//         Parametres saved = parametresRepository.save(parametres);
//         return ResponseEntity.status(HttpStatus.CREATED).body(saved);
//     }

//     /**
//      * Récupérer tous les paramètres
//      * GET /api/test/parametres
//      */
//     @GetMapping("/parametres")
//     public ResponseEntity<List<Parametres>> getAllParametres() {
//         List<Parametres> parametres = parametresRepository.findAll();
//         return ResponseEntity.ok(parametres);
//     }

//     /**
//      * Récupérer des paramètres par ID
//      * GET /api/test/parametres/{id}
//      */
//     @GetMapping("/parametres/{id}")
//     public ResponseEntity<Parametres> getParametresById(@PathVariable Long id) {
//         return parametresRepository.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     // ============================================
//     // ENDPOINTS DE TEST POUR DIAGNOSTIC
//     // ============================================

//     /**
//      * Tester la création d'un diagnostic
//      * POST /api/test/diagnostics
//      */
//     @PostMapping("/diagnostics")
//     public ResponseEntity<Diagnostic> createDiagnostic(@RequestBody Diagnostic diagnostic) {
//         Diagnostic saved = diagnosticRepository.save(diagnostic);
//         return ResponseEntity.status(HttpStatus.CREATED).body(saved);
//     }

//     /**
//      * Récupérer tous les diagnostics
//      * GET /api/test/diagnostics
//      */
//     @GetMapping("/diagnostics")
//     public ResponseEntity<List<Diagnostic>> getAllDiagnostics() {
//         List<Diagnostic> diagnostics = diagnosticRepository.findAll();
//         return ResponseEntity.ok(diagnostics);
//     }

//     /**
//      * Récupérer un diagnostic par ID
//      * GET /api/test/diagnostics/{id}
//      */
//     @GetMapping("/diagnostics/{id}")
//     public ResponseEntity<Diagnostic> getDiagnosticById(@PathVariable Long id) {
//         return diagnosticRepository.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     // ============================================
//     // ENDPOINT DE SANTÉ
//     // ============================================

//     /**
//      * Tester si l'API fonctionne
//      * GET /api/test/health
//      */
//     @GetMapping("/health")
//     public ResponseEntity<String> health() {
//         return ResponseEntity.ok("✅ API is running! Test endpoints are available.");
//     }
// }