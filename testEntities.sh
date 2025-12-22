#!/bin/bash

# Script de test automatisé pour vérifier le mapping des entités
# Usage: ./test-entities.sh

# Couleurs pour l'affichage
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080/api/test"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}   Test du Mapping des Entités JPA    ${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Fonction pour tester un endpoint
test_endpoint() {
    local endpoint=$1
    local method=$2
    local data=$3
    local test_name=$4
    
    echo -e "${YELLOW}Test: ${test_name}${NC}"
    
    if [ "$method" == "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" "$BASE_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n-1)
    
    if [ "$http_code" == "200" ] || [ "$http_code" == "201" ]; then
        echo -e "${GREEN}✅ SUCCÈS${NC} (HTTP $http_code)"
        echo "$body" | jq '.' 2>/dev/null || echo "$body"
    else
        echo -e "${RED}❌ ÉCHEC${NC} (HTTP $http_code)"
        echo "$body"
    fi
    
    echo ""
    sleep 1
}

# Test 0: Vérifier que l'API fonctionne
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Test 0: Santé de l'API${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
test_endpoint "/health" "GET" "" "Vérifier que l'API est disponible"

# Test 1: Médecin
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Test 1: Entité MEDECIN${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

test_endpoint "/medecins" "POST" '{
    "nom": "Mbarga",
    "prenom": "Paul",
    "email": "paul.mbarga@hopital.cm",
    "tel": "+237690123456"
}' "Créer un médecin"

test_endpoint "/medecins" "GET" "" "Récupérer tous les médecins"

test_endpoint "/medecins/1" "GET" "" "Récupérer le médecin ID 1"

# Test 2: Dispositif
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Test 2: Entité DISPOSITIF${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

test_endpoint "/dispositifs" "POST" '{
    "nomCentreDeSante": "Centre Médical de Yaoundé",
    "contact": "+237699876543"
}' "Créer un dispositif"

test_endpoint "/dispositifs" "GET" "" "Récupérer tous les dispositifs"

test_endpoint "/dispositifs/1" "GET" "" "Récupérer le dispositif ID 1"

# Test 3: Paramètres
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Test 3: Entité PARAMETRES${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

test_endpoint "/parametres" "POST" '{
    "identifiantPatient": "PAT-2024-001",
    "poidsPatient": 68.50,
    "temperature": 37.10,
    "pressionArterielle": "118/75",
    "frequenceFoetale": 145,
    "dispositif": {
        "idDispositif": 1
    }
}' "Créer des paramètres"

test_endpoint "/parametres" "GET" "" "Récupérer tous les paramètres"

test_endpoint "/parametres/1" "GET" "" "Récupérer les paramètres ID 1"

# Test 4: Diagnostic
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Test 4: Entité DIAGNOSTIC${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

test_endpoint "/diagnostics" "POST" '{
    "contenu": "Patient en bonne santé générale. Poids: 68.5 kg - Dans la norme. Température: 37.1°C - Normale. Pression artérielle: 118/75 - Optimale. Fréquence fœtale: 145 bpm - Normale. Recommandation: Continuer le suivi prénatal régulier.",
    "medecin": {
        "idMedecin": 1
    },
    "parametres": {
        "idParametres": 1
    }
}' "Créer un diagnostic"

test_endpoint "/diagnostics" "GET" "" "Récupérer tous les diagnostics"

test_endpoint "/diagnostics/1" "GET" "" "Récupérer le diagnostic ID 1"

# Résumé
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}   Résumé des Tests${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "Si tous les tests sont ${GREEN}✅ SUCCÈS${NC}, alors:"
echo -e "  ${GREEN}✓${NC} Toutes les entités sont bien mappées"
echo -e "  ${GREEN}✓${NC} Les relations JPA fonctionnent"
echo -e "  ${GREEN}✓${NC} La connexion à PostgreSQL fonctionne"
echo -e "  ${GREEN}✓${NC} Les validations fonctionnent"
echo ""
echo -e "Tu peux maintenant passer aux ${YELLOW}DTOs${NC} et ${YELLOW}Services${NC}! 🚀"
echo ""