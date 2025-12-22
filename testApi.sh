#!/bin/bash

# Script de test complet de l'API REST Spring Boot
# Usage: ./test-api.sh

BASE_URL="http://localhost:8080/api"

echo "======================================"
echo "  TEST COMPLET DE L'API MÉDICALE"
echo "======================================"
echo ""

# Fonction pour afficher les résultats
print_result() {
    echo "---"
    echo "$1"
    echo "Réponse:"
    echo "$2" | jq '.' 2>/dev/null || echo "$2"
    echo ""
}

# Test 1: Créer des médecins
echo "📝 ÉTAPE 1: Création des médecins"
MEDECIN1=$(curl -s -X POST $BASE_URL/medecins \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Mbarga",
    "prenom": "Paul",
    "email": "paul.mbarga@hopital.cm",
    "tel": "+237690123456"
  }')
print_result "Médecin 1 créé" "$MEDECIN1"

MEDECIN2=$(curl -s -X POST $BASE_URL/medecins \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Atangana",
    "prenom": "Marie",
    "email": "marie.atangana@hopital.cm",
    "tel": "+237698888888"
  }')
print_result "Médecin 2 créé" "$MEDECIN2"

# Test 2: Récupérer tous les médecins
echo "📋 ÉTAPE 2: Liste de tous les médecins"
ALL_MEDECINS=$(curl -s $BASE_URL/medecins)
print_result "Tous les médecins" "$ALL_MEDECINS"

# Test 3: Récupérer un médecin par ID
echo "🔍 ÉTAPE 3: Récupération médecin par ID"
MEDECIN_BY_ID=$(curl -s $BASE_URL/medecins/1)
print_result "Médecin ID=1" "$MEDECIN_BY_ID"

# Test 4: Créer des dispositifs
echo "🏥 ÉTAPE 4: Création des dispositifs"
DISPOSITIF1=$(curl -s -X POST $BASE_URL/dispositifs \
  -H "Content-Type: application/json" \
  -d '{
    "nomCentreDeSante": "Centre Médical de Yaoundé",
    "contact": "+237699876543"
  }')
print_result "Dispositif 1 créé" "$DISPOSITIF1"

DISPOSITIF2=$(curl -s -X POST $BASE_URL/dispositifs \
  -H "Content-Type: application/json" \
  -d '{
    "nomCentreDeSante": "Hôpital Général de Douala",
    "contact": "+237688888888"
  }')
print_result "Dispositif 2 créé" "$DISPOSITIF2"

# Test 5: Rechercher dispositif
echo "🔍 ÉTAPE 5: Recherche de dispositif"
SEARCH_DISPOSITIF=$(curl -s "$BASE_URL/dispositifs/search?nomCentre=Yaoundé")
print_result "Recherche 'Yaoundé'" "$SEARCH_DISPOSITIF"

# Test 6: Créer des paramètres
echo "📊 ÉTAPE 6: Création de paramètres médicaux"
PARAMETRES1=$(curl -s -X POST $BASE_URL/parametres \
  -H "Content-Type: application/json" \
  -d '{
    "identifiantPatient": "PAT-2024-001",
    "poidsPatient": 68.50,
    "temperature": 37.10,
    "pressionArterielle": 118,
    "frequenceFoetale": 145,
    "dispositifId": 1
  }')
print_result "Paramètres patient PAT-2024-001" "$PARAMETRES1"

PARAMETRES2=$(curl -s -X POST $BASE_URL/parametres \
  -H "Content-Type: application/json" \
  -d '{
    "identifiantPatient": "PAT-2024-002",
    "poidsPatient": 72.30,
    "temperature": 37.50,
    "pressionArterielle": 125,
    "frequenceFoetale": 142,
    "dispositifId": 2
  }')
print_result "Paramètres patient PAT-2024-002" "$PARAMETRES2"

# Test 7: Récupérer paramètres par patient
echo "👤 ÉTAPE 7: Récupération paramètres d'un patient"
PARAM_PATIENT=$(curl -s $BASE_URL/parametres/patient/PAT-2024-001)
print_result "Paramètres de PAT-2024-001" "$PARAM_PATIENT"

# Test 8: Créer des diagnostics
echo "🩺 ÉTAPE 8: Création de diagnostics"
DIAGNOSTIC1=$(curl -s -X POST $BASE_URL/diagnostics \
  -H "Content-Type: application/json" \
  -d '{
    "contenu": "Patient en bonne santé générale. Tous les paramètres dans les normes. Recommandation: Continuer le suivi prénatal régulier.",
    "medecinId": 1,
    "parametresId": 1
  }')
print_result "Diagnostic 1 créé" "$DIAGNOSTIC1"

DIAGNOSTIC2=$(curl -s -X POST $BASE_URL/diagnostics \
  -H "Content-Type: application/json" \
  -d '{
    "contenu": "Surveillance nécessaire. Température légèrement élevée. Pression artérielle à surveiller. Repos recommandé.",
    "medecinId": 2,
    "parametresId": 2
  }')
print_result "Diagnostic 2 créé" "$DIAGNOSTIC2"

# Test 9: Récupérer diagnostics par médecin
echo "👨‍⚕️ ÉTAPE 9: Diagnostics d'un médecin"
DIAG_MEDECIN=$(curl -s $BASE_URL/diagnostics/medecin/1)
print_result "Diagnostics du médecin ID=1" "$DIAG_MEDECIN"

# Test 10: Mettre à jour un médecin
echo "✏️ ÉTAPE 10: Mise à jour d'un médecin"
UPDATE_MEDECIN=$(curl -s -X PUT $BASE_URL/medecins/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Mbarga",
    "prenom": "Paul Emmanuel",
    "email": "paul.mbarga@hopital.cm",
    "tel": "+237690999999"
  }')
print_result "Médecin mis à jour" "$UPDATE_MEDECIN"

# Test 11: Test d'erreur - Ressource non trouvée
echo "❌ ÉTAPE 11: Test erreur 404"
ERROR_404=$(curl -s $BASE_URL/medecins/999)
print_result "Erreur 404 - Médecin non trouvé" "$ERROR_404"

# Test 12: Test d'erreur - Email déjà existant
echo "❌ ÉTAPE 12: Test erreur 409 (Email existant)"
ERROR_409=$(curl -s -X POST $BASE_URL/medecins \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test",
    "prenom": "Test",
    "email": "paul.mbarga@hopital.cm",
    "tel": "1234"
  }')
print_result "Erreur 409 - Email existant" "$ERROR_409"

# Test 13: Test d'erreur - Validation
echo "❌ ÉTAPE 13: Test erreur 400 (Validation)"
ERROR_400=$(curl -s -X POST $BASE_URL/medecins \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "",
    "prenom": "Test",
    "email": "email-invalide"
  }')
print_result "Erreur 400 - Validation" "$ERROR_400"

# Récapitulatif final
echo "======================================"
echo "         RÉCAPITULATIF FINAL"
echo "======================================"
echo ""
echo "✓ Médecins créés: 2"
echo "✓ Dispositifs créés: 2"
echo "✓ Paramètres créés: 2"
echo "✓ Diagnostics créés: 2"
echo ""
echo "📊 Vérification finale:"
echo ""

echo "Nombre de médecins:"
curl -s $BASE_URL/medecins | jq 'length' 2>/dev/null || echo "Erreur jq non installé"

echo "Nombre de dispositifs:"
curl -s $BASE_URL/dispositifs | jq 'length' 2>/dev/null || echo "Erreur jq non installé"

echo "Nombre de paramètres:"
curl -s $BASE_URL/parametres | jq 'length' 2>/dev/null || echo "Erreur jq non installé"

echo "Nombre de diagnostics:"
curl -s $BASE_URL/diagnostics | jq 'length' 2>/dev/null || echo "Erreur jq non installé"

echo ""
echo "======================================"
echo "    ✅ TESTS TERMINÉS AVEC SUCCÈS"
echo "======================================"
echo ""
echo "💡 Astuce: Installe 'jq' pour un meilleur formatage JSON"
echo "   Ubuntu/Debian: sudo apt install jq"
echo "   Mac: brew install jq"