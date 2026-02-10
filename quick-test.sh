#!/bin/bash

# ============================================
# TEST RAPIDE API - BD_ELECTRO
# Version simplifiée pour test rapide
# ============================================

GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

BASE_URL="http://localhost:8080/api"

echo -e "${BLUE}======================================${NC}"
echo -e "${BLUE}TEST RAPIDE API BD_ELECTRO${NC}"
echo -e "${BLUE}======================================${NC}"
echo ""

# 1. Vérifier que le serveur fonctionne
echo -e "${YELLOW}1. Vérification du serveur...${NC}"
response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/medecins")
if [ "$response" = "200" ]; then
    echo -e "${GREEN}✓ Serveur accessible${NC}"
else
    echo -e "${RED}✗ Serveur non accessible - Démarrez l'application${NC}"
    exit 1
fi
echo ""

# 2. Créer un médecin
echo -e "${YELLOW}2. Création d'un médecin...${NC}"
MEDECIN=$(curl -s -X POST "$BASE_URL/medecins" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test",
    "prenom": "Docteur",
    "email": "test.docteur@example.cm",
    "tel": "+237690000000"
  }')
MEDECIN_ID=$(echo "$MEDECIN" | jq -r '.id // empty')
if [ ! -z "$MEDECIN_ID" ]; then
    echo -e "${GREEN}✓ Médecin créé (ID: $MEDECIN_ID)${NC}"
    echo "$MEDECIN" | jq .
else
    echo -e "${RED}✗ Échec création médecin${NC}"
    echo "$MEDECIN"
fi
echo ""

# 3. Créer un dispositif
echo -e "${YELLOW}3. Création d'un dispositif...${NC}"
DISPOSITIF=$(curl -s -X POST "$BASE_URL/dispositifs" \
  -H "Content-Type: application/json" \
  -d '{
    "deveui": "B123411AB22C33D1",
    "appeui": "0000000000000000",
    "appkey": "12345678901234567890123456789012",
    "nomCentreDeSante": "Centre de Test Rapide",
    "localisation": "Yaoundé",
    "contact": "+237699000000"
  }')
DISPOSITIF_ID=$(echo "$DISPOSITIF" | jq -r '.id // empty')
if [ ! -z "$DISPOSITIF_ID" ]; then
    echo -e "${GREEN}✓ Dispositif créé (ID: $DISPOSITIF_ID)${NC}"
    echo "$DISPOSITIF" | jq .
else
    echo -e "${RED}✗ Échec création dispositif${NC}"
    echo "$DISPOSITIF"
fi
echo ""

# 4. Créer des paramètres avec pression séparée
echo -e "${YELLOW}4. Création de paramètres (tension 120/80)...${NC}"
if [ ! -z "$DISPOSITIF_ID" ]; then
    PARAMETRES=$(curl -s -X POST "$BASE_URL/parametres" \
      -H "Content-Type: application/json" \
      -d "{
        \"identifiantPatient\": \"PAT-QUICK-TEST\",
        \"poidsPatient\": 70.00,
        \"temperature\": 37.00,
        \"pressionArterielleSystolique\": 120,
        \"pressionArterielleDiastolique\": 80,
        \"frequenceFoetale\": 145,
        \"dispositifId\": $DISPOSITIF_ID
      }")
    PARAMETRES_ID=$(echo "$PARAMETRES" | jq -r '.id // empty')
    if [ ! -z "$PARAMETRES_ID" ]; then
        echo -e "${GREEN}✓ Paramètres créés (ID: $PARAMETRES_ID, Tension: 120/80)${NC}"
        echo "$PARAMETRES" | jq .
    else
        echo -e "${RED}✗ Échec création paramètres${NC}"
        echo "$PARAMETRES"
    fi
else
    echo -e "${RED}✗ Pas de dispositif, skip${NC}"
fi
echo ""

# 5. Créer un diagnostic
echo -e "${YELLOW}5. Création d'un diagnostic...${NC}"
if [ ! -z "$MEDECIN_ID" ] && [ ! -z "$PARAMETRES_ID" ]; then
    DIAGNOSTIC=$(curl -s -X POST "$BASE_URL/diagnostics" \
      -H "Content-Type: application/json" \
      -d "{
        \"contenu\": \"Test rapide OK. Tension normale (120/80).\",
        \"medecinId\": $MEDECIN_ID,
        \"parametresId\": $PARAMETRES_ID
      }")
    DIAGNOSTIC_ID=$(echo "$DIAGNOSTIC" | jq -r '.id // empty')
    if [ ! -z "$DIAGNOSTIC_ID" ]; then
        echo -e "${GREEN}✓ Diagnostic créé (ID: $DIAGNOSTIC_ID)${NC}"
        echo "$DIAGNOSTIC" | jq .
    else
        echo -e "${RED}✗ Échec création diagnostic${NC}"
        echo "$DIAGNOSTIC"
    fi
else
    echo -e "${RED}✗ Données manquantes, skip${NC}"
fi
echo ""

# Résumé
echo -e "${BLUE}======================================${NC}"
echo -e "${BLUE}RÉSUMÉ${NC}"
echo -e "${BLUE}======================================${NC}"
echo "Médecin ID:     $MEDECIN_ID"
echo "Dispositif ID:  $DISPOSITIF_ID (DevEUI: B123411AB22C33D1)"
echo "Paramètres ID:  $PARAMETRES_ID (Tension: 120/80)"
echo "Diagnostic ID:  $DIAGNOSTIC_ID"
echo ""

if [ ! -z "$DIAGNOSTIC_ID" ]; then
    echo -e "${GREEN}✓ TEST RAPIDE RÉUSSI!${NC}"
    echo "Tous les endpoints principaux fonctionnent correctement."
else
    echo -e "${YELLOW}⚠ TEST PARTIEL${NC}"
    echo "Certaines données n'ont pas pu être créées."
fi