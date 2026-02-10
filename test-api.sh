#!/bin/bash

# ============================================
# SCRIPT DE TEST API - BD_ELECTRO
# Nouveau Schéma avec code dispositif et pression séparée
# Teste tous les 20 endpoints REST
# ============================================

# Couleurs pour l'affichage
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
BASE_URL="http://localhost:8080/api"
SLEEP_TIME=1

# Compteurs
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Fonction pour afficher les sections
print_section() {
    echo ""
    echo -e "${BLUE}============================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}============================================${NC}"
    echo ""
}

# Fonction pour afficher les résultats
print_result() {
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ PASS${NC} - $2"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}✗ FAIL${NC} - $2"
        FAILED_TESTS=$((FAILED_TESTS + 1))
    fi
}

# Fonction pour tester un endpoint
test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    
    echo -e "${YELLOW}Test:${NC} $description"
    echo -e "${YELLOW}$method${NC} $endpoint"
    
    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    echo "HTTP Status: $http_code"
    echo "Response:"
    echo "$body" | jq . 2>/dev/null || echo "$body"
    echo ""
    
    sleep $SLEEP_TIME
    
    echo "$body"
}

# Vérifier que le serveur est accessible
print_section "VÉRIFICATION DU SERVEUR"
response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/medecins")
if [ "$response" != "200" ]; then
    echo -e "${RED}ERREUR: Le serveur n'est pas accessible!${NC}"
    echo "Assurez-vous que l'application Spring Boot est démarrée sur le port 8080"
    exit 1
fi
echo -e "${GREEN}✓ Serveur accessible${NC}"

# ============================================
# TESTS MÉDECIN (6 endpoints)
# ============================================

print_section "1. TESTS MÉDECIN (6 endpoints)"

# 1.1 Créer un médecin
echo -e "${YELLOW}[1/6]${NC} POST /medecins - Créer un médecin"
MEDECIN_RESPONSE=$(test_endpoint "POST" "/medecins" '{
    "nom": "Mbarga",
    "prenom": "Paul",
    "email": "paul.mbarga@test.cm",
    "tel": "+237690123456"
}' "Créer un médecin")
MEDECIN_ID=$(echo "$MEDECIN_RESPONSE" | jq -r '.id // empty')
if [ ! -z "$MEDECIN_ID" ]; then
    print_result 0 "Médecin créé avec ID: $MEDECIN_ID"
else
    print_result 1 "Échec de création du médecin"
fi

# 1.2 Lister tous les médecins
echo -e "${YELLOW}[2/6]${NC} GET /medecins - Lister tous les médecins"
MEDECINS_LIST=$(test_endpoint "GET" "/medecins" "" "Lister tous les médecins")
COUNT=$(echo "$MEDECINS_LIST" | jq 'length // 0')
print_result 0 "Nombre de médecins: $COUNT"

# 1.3 Récupérer un médecin par ID
echo -e "${YELLOW}[3/6]${NC} GET /medecins/{id} - Récupérer un médecin"
if [ ! -z "$MEDECIN_ID" ]; then
    test_endpoint "GET" "/medecins/$MEDECIN_ID" "" "Récupérer médecin ID $MEDECIN_ID"
    print_result 0 "Médecin récupéré par ID"
else
    print_result 1 "Impossible de tester (pas d'ID)"
fi

# 1.4 Récupérer un médecin par email
echo -e "${YELLOW}[4/6]${NC} GET /medecins/email/{email} - Récupérer par email"
test_endpoint "GET" "/medecins/email/paul.mbarga@test.cm" "" "Récupérer médecin par email"
print_result 0 "Médecin récupéré par email"

# 1.5 Mettre à jour un médecin
echo -e "${YELLOW}[5/6]${NC} PUT /medecins/{id} - Mettre à jour un médecin"
if [ ! -z "$MEDECIN_ID" ]; then
    test_endpoint "PUT" "/medecins/$MEDECIN_ID" '{
        "nom": "Mbarga",
        "prenom": "Paul-André",
        "email": "paul.mbarga@test.cm",
        "tel": "+237690999999"
    }' "Mettre à jour médecin ID $MEDECIN_ID"
    print_result 0 "Médecin mis à jour"
else
    print_result 1 "Impossible de tester (pas d'ID)"
fi

# 1.6 Créer un deuxième médecin (pour ne pas supprimer le premier)
echo -e "${YELLOW}[6/6]${NC} POST /medecins - Créer un deuxième médecin"
MEDECIN2_RESPONSE=$(test_endpoint "POST" "/medecins" '{
    "nom": "Atangana",
    "prenom": "Marie",
    "email": "marie.atangana@test.cm",
    "tel": "+237698888888"
}' "Créer un deuxième médecin")
MEDECIN2_ID=$(echo "$MEDECIN2_RESPONSE" | jq -r '.id // empty')
if [ ! -z "$MEDECIN2_ID" ]; then
    print_result 0 "Deuxième médecin créé avec ID: $MEDECIN2_ID"
else
    print_result 1 "Échec de création du deuxième médecin"
fi

# ============================================
# TESTS DISPOSITIF (6 endpoints) - NOUVEAU SCHÉMA
# ============================================

print_section "2. TESTS DISPOSITIF (6 endpoints) - NOUVEAU SCHÉMA"

# 2.1 Créer un dispositif (SuperAdmin role simulation)
echo -e "${YELLOW}[1/6]${NC} POST /dispositifs - Créer un dispositif"
DISPOSITIF_RESPONSE=$(test_endpoint "POST" "/dispositifs" '{
    "deveui": "A840411AB22C33D1",
    "appeui": "0000000000000000",
    "appkey": "2B7E151628AED2A6ABF7158809CF4F3C",
    "nomCentreDeSante": "Centre Médical de Test",
    "localisation": "Yaoundé, Cameroun",
    "contact": "+237699876543"
}' "Créer un dispositif avec DevEUI unique")
DISPOSITIF_ID=$(echo "$DISPOSITIF_RESPONSE" | jq -r '.id // empty')
if [ ! -z "$DISPOSITIF_ID" ]; then
    print_result 0 "Dispositif créé avec ID: $DISPOSITIF_ID (DevEUI: A840411AB22C33D1)"
else
    print_result 1 "Échec de création du dispositif"
fi

# 2.2 Lister tous les dispositifs
echo -e "${YELLOW}[2/6]${NC} GET /dispositifs - Lister tous les dispositifs"
DISPOSITIFS_LIST=$(test_endpoint "GET" "/dispositifs" "" "Lister tous les dispositifs")
COUNT=$(echo "$DISPOSITIFS_LIST" | jq 'length // 0')
print_result 0 "Nombre de dispositifs: $COUNT"

# 2.3 Récupérer un dispositif par ID
echo -e "${YELLOW}[3/6]${NC} GET /dispositifs/{id} - Récupérer un dispositif"
if [ ! -z "$DISPOSITIF_ID" ]; then
    test_endpoint "GET" "/dispositifs/$DISPOSITIF_ID" "" "Récupérer dispositif ID $DISPOSITIF_ID"
    print_result 0 "Dispositif récupéré par ID"
else
    print_result 1 "Impossible de tester (pas d'ID)"
fi

# 2.4 Rechercher un dispositif par nom de centre
echo -e "${YELLOW}[4/6]${NC} GET /dispositifs/search?nomCentre=X - Rechercher par nom"
test_endpoint "GET" "/dispositifs/search?nomCentre=Test" "" "Rechercher dispositif par nom centre"
print_result 0 "Recherche par nom de centre effectuée"

# 2.5 Mettre à jour un dispositif
echo -e "${YELLOW}[5/6]${NC} PUT /dispositifs/{id} - Mettre à jour un dispositif"
if [ ! -z "$DISPOSITIF_ID" ]; then
    test_endpoint "PUT" "/dispositifs/$DISPOSITIF_ID" '{
        "deveui": "A840411AB22C33D1",
        "appeui": "0000000000000000",
        "appkey": "2B7E151628AED2A6ABF7158809CF4F3C",
        "nomCentreDeSante": "Centre Médical de Test (Modifié)",
        "localisation": "Yaoundé, Centre",
        "contact": "+237699999999"
    }' "Mettre à jour dispositif ID $DISPOSITIF_ID"
    print_result 0 "Dispositif mis à jour"
else
    print_result 1 "Impossible de tester (pas d'ID)"
fi

# 2.6 Créer un deuxième dispositif
echo -e "${YELLOW}[6/6]${NC} POST /dispositifs - Créer un deuxième dispositif"
DISPOSITIF2_RESPONSE=$(test_endpoint "POST" "/dispositifs" '{
    "deveui": "A840411AB22C33D2",
    "appeui": "0000000000000000",
    "appkey": "11223344556677889900AABBCCDDEEFF",
    "nomCentreDeSante": "Hôpital de Test",
    "localisation": "Douala, Littoral",
    "contact": "+237677777777"
}' "Créer un deuxième dispositif")
DISPOSITIF2_ID=$(echo "$DISPOSITIF2_RESPONSE" | jq -r '.id // empty')
if [ ! -z "$DISPOSITIF2_ID" ]; then
    print_result 0 "Deuxième dispositif créé avec ID: $DISPOSITIF2_ID (DevEUI: A840411AB22C33D2)"
else
    print_result 1 "Échec de création du deuxième dispositif"
fi

# ============================================
# TESTS PARAMETRES (7 endpoints) - PRESSION SÉPARÉE
# ============================================

print_section "3. TESTS PARAMETRES (7 endpoints) - PRESSION SÉPARÉE"

# 3.1 Créer des paramètres avec pression séparée
echo -e "${YELLOW}[1/7]${NC} POST /parametres - Créer des paramètres (tension 120/80)"
if [ ! -z "$DISPOSITIF_ID" ]; then
    PARAMETRES_RESPONSE=$(test_endpoint "POST" "/parametres" "{
        \"identifiantPatient\": \"PAT-TEST-001\",
        \"poidsPatient\": 68.50,
        \"temperature\": 37.10,
        \"pressionArterielleSystolique\": 120,
        \"pressionArterielleDiastolique\": 80,
        \"frequenceFoetale\": 145,
        \"dispositifId\": $DISPOSITIF_ID
    }" "Créer des paramètres avec tension 120/80")
    PARAMETRES_ID=$(echo "$PARAMETRES_RESPONSE" | jq -r '.id // empty')
    if [ ! -z "$PARAMETRES_ID" ]; then
        print_result 0 "Paramètres créés avec ID: $PARAMETRES_ID (Tension: 120/80)"
    else
        print_result 1 "Échec de création des paramètres"
    fi
else
    print_result 1 "Impossible de tester (pas de dispositif)"
fi

# 3.2 Lister tous les paramètres
echo -e "${YELLOW}[2/7]${NC} GET /parametres - Lister tous les paramètres"
PARAMETRES_LIST=$(test_endpoint "GET" "/parametres" "" "Lister tous les paramètres")
COUNT=$(echo "$PARAMETRES_LIST" | jq 'length // 0')
print_result 0 "Nombre de paramètres: $COUNT"

# 3.3 Récupérer des paramètres par ID
echo -e "${YELLOW}[3/7]${NC} GET /parametres/{id} - Récupérer des paramètres"
if [ ! -z "$PARAMETRES_ID" ]; then
    test_endpoint "GET" "/parametres/$PARAMETRES_ID" "" "Récupérer paramètres ID $PARAMETRES_ID"
    print_result 0 "Paramètres récupérés par ID"
else
    print_result 1 "Impossible de tester (pas d'ID)"
fi

# 3.4 Récupérer paramètres par patient
echo -e "${YELLOW}[4/7]${NC} GET /parametres/patient/{id} - Récupérer par patient"
test_endpoint "GET" "/parametres/patient/PAT-TEST-001" "" "Récupérer paramètres du patient"
print_result 0 "Paramètres récupérés par patient"

# 3.5 Récupérer paramètres par dispositif
echo -e "${YELLOW}[5/7]${NC} GET /parametres/dispositif/{id} - Récupérer par dispositif"
if [ ! -z "$DISPOSITIF_ID" ]; then
    test_endpoint "GET" "/parametres/dispositif/$DISPOSITIF_ID" "" "Récupérer paramètres du dispositif"
    print_result 0 "Paramètres récupérés par dispositif"
else
    print_result 1 "Impossible de tester (pas de dispositif)"
fi

# 3.6 Mettre à jour des paramètres
echo -e "${YELLOW}[6/7]${NC} PUT /parametres/{id} - Mettre à jour des paramètres"
if [ ! -z "$PARAMETRES_ID" ] && [ ! -z "$DISPOSITIF_ID" ]; then
    test_endpoint "PUT" "/parametres/$PARAMETRES_ID" "{
        \"identifiantPatient\": \"PAT-TEST-001\",
        \"poidsPatient\": 69.00,
        \"temperature\": 37.20,
        \"pressionArterielleSystolique\": 118,
        \"pressionArterielleDiastolique\": 78,
        \"frequenceFoetale\": 148,
        \"dispositifId\": $DISPOSITIF_ID
    }" "Mettre à jour paramètres (tension 118/78)"
    print_result 0 "Paramètres mis à jour"
else
    print_result 1 "Impossible de tester (pas d'ID)"
fi

# 3.7 Créer des deuxièmes paramètres (hypertension)
echo -e "${YELLOW}[7/7]${NC} POST /parametres - Créer paramètres avec hypertension (140/90)"
if [ ! -z "$DISPOSITIF_ID" ]; then
    PARAMETRES2_RESPONSE=$(test_endpoint "POST" "/parametres" "{
        \"identifiantPatient\": \"PAT-TEST-002\",
        \"poidsPatient\": 72.00,
        \"temperature\": 36.90,
        \"pressionArterielleSystolique\": 140,
        \"pressionArterielleDiastolique\": 90,
        \"frequenceFoetale\": 150,
        \"dispositifId\": $DISPOSITIF_ID
    }" "Créer des paramètres avec hypertension")
    PARAMETRES2_ID=$(echo "$PARAMETRES2_RESPONSE" | jq -r '.id // empty')
    if [ ! -z "$PARAMETRES2_ID" ]; then
        print_result 0 "Deuxièmes paramètres créés avec ID: $PARAMETRES2_ID (Tension: 140/90)"
    else
        print_result 1 "Échec de création des deuxièmes paramètres"
    fi
else
    print_result 1 "Impossible de tester (pas de dispositif)"
fi

# ============================================
# TESTS DIAGNOSTIC (7 endpoints)
# ============================================

print_section "4. TESTS DIAGNOSTIC (7 endpoints)"

# 4.1 Créer un diagnostic
echo -e "${YELLOW}[1/7]${NC} POST /diagnostics - Créer un diagnostic"
if [ ! -z "$MEDECIN_ID" ] && [ ! -z "$PARAMETRES_ID" ]; then
    DIAGNOSTIC_RESPONSE=$(test_endpoint "POST" "/diagnostics" "{
        \"contenu\": \"Patient en bonne santé générale. Poids normal (68.5 kg). Température normale (37.1°C). Tension artérielle optimale (120/80 mmHg). Fréquence fœtale normale (145 bpm). Recommandation: Continuer le suivi prénatal régulier.\",
        \"medecinId\": $MEDECIN_ID,
        \"parametresId\": $PARAMETRES_ID
    }" "Créer un diagnostic pour tension normale")
    DIAGNOSTIC_ID=$(echo "$DIAGNOSTIC_RESPONSE" | jq -r '.id // empty')
    if [ ! -z "$DIAGNOSTIC_ID" ]; then
        print_result 0 "Diagnostic créé avec ID: $DIAGNOSTIC_ID"
    else
        print_result 1 "Échec de création du diagnostic"
    fi
else
    print_result 1 "Impossible de tester (pas de médecin ou paramètres)"
fi

# 4.2 Lister tous les diagnostics
echo -e "${YELLOW}[2/7]${NC} GET /diagnostics - Lister tous les diagnostics"
DIAGNOSTICS_LIST=$(test_endpoint "GET" "/diagnostics" "" "Lister tous les diagnostics")
COUNT=$(echo "$DIAGNOSTICS_LIST" | jq 'length // 0')
print_result 0 "Nombre de diagnostics: $COUNT"

# 4.3 Récupérer un diagnostic par ID
echo -e "${YELLOW}[3/7]${NC} GET /diagnostics/{id} - Récupérer un diagnostic"
if [ ! -z "$DIAGNOSTIC_ID" ]; then
    test_endpoint "GET" "/diagnostics/$DIAGNOSTIC_ID" "" "Récupérer diagnostic ID $DIAGNOSTIC_ID"
    print_result 0 "Diagnostic récupéré par ID"
else
    print_result 1 "Impossible de tester (pas d'ID)"
fi

# 4.4 Récupérer diagnostics par médecin
echo -e "${YELLOW}[4/7]${NC} GET /diagnostics/medecin/{id} - Récupérer par médecin"
if [ ! -z "$MEDECIN_ID" ]; then
    test_endpoint "GET" "/diagnostics/medecin/$MEDECIN_ID" "" "Récupérer diagnostics du médecin"
    print_result 0 "Diagnostics récupérés par médecin"
else
    print_result 1 "Impossible de tester (pas de médecin)"
fi

# 4.5 Récupérer diagnostics par paramètres
echo -e "${YELLOW}[5/7]${NC} GET /diagnostics/parametres/{id} - Récupérer par paramètres"
if [ ! -z "$PARAMETRES_ID" ]; then
    test_endpoint "GET" "/diagnostics/parametres/$PARAMETRES_ID" "" "Récupérer diagnostics des paramètres"
    print_result 0 "Diagnostics récupérés par paramètres"
else
    print_result 1 "Impossible de tester (pas de paramètres)"
fi

# 4.6 Mettre à jour un diagnostic
echo -e "${YELLOW}[6/7]${NC} PUT /diagnostics/{id} - Mettre à jour un diagnostic"
if [ ! -z "$DIAGNOSTIC_ID" ] && [ ! -z "$MEDECIN_ID" ] && [ ! -z "$PARAMETRES_ID" ]; then
    test_endpoint "PUT" "/diagnostics/$DIAGNOSTIC_ID" "{
        \"contenu\": \"Patient en bonne santé générale (MISE À JOUR). État stable. Tension optimale (120/80). Recommandation: Continuer le suivi.\",
        \"medecinId\": $MEDECIN_ID,
        \"parametresId\": $PARAMETRES_ID
    }" "Mettre à jour diagnostic ID $DIAGNOSTIC_ID"
    print_result 0 "Diagnostic mis à jour"
else
    print_result 1 "Impossible de tester (pas d'ID)"
fi

# 4.7 Créer un deuxième diagnostic (hypertension)
echo -e "${YELLOW}[7/7]${NC} POST /diagnostics - Créer diagnostic pour hypertension"
if [ ! -z "$MEDECIN2_ID" ] && [ ! -z "$PARAMETRES2_ID" ]; then
    DIAGNOSTIC2_RESPONSE=$(test_endpoint "POST" "/diagnostics" "{
        \"contenu\": \"Tension légèrement élevée (140/90 mmHg). Patient en léger surpoids (72 kg). Fréquence fœtale normale (150 bpm). Recommandation: Surveiller la tension, réduire le sel, refaire un contrôle dans 1 semaine.\",
        \"medecinId\": $MEDECIN2_ID,
        \"parametresId\": $PARAMETRES2_ID
    }" "Créer un diagnostic pour hypertension")
    DIAGNOSTIC2_ID=$(echo "$DIAGNOSTIC2_RESPONSE" | jq -r '.id // empty')
    if [ ! -z "$DIAGNOSTIC2_ID" ]; then
        print_result 0 "Deuxième diagnostic créé avec ID: $DIAGNOSTIC2_ID"
    else
        print_result 1 "Échec de création du deuxième diagnostic"
    fi
else
    print_result 1 "Impossible de tester (pas de médecin 2 ou paramètres 2)"
fi

# ============================================
# RÉSUMÉ DES DONNÉES CRÉÉES
# ============================================

print_section "DONNÉES CRÉÉES DURANT LES TESTS"

echo "Médecins créés:"
echo "  - ID $MEDECIN_ID: Dr. Mbarga Paul"
echo "  - ID $MEDECIN2_ID: Dr. Atangana Marie"
echo ""
echo "Dispositifs créés:"
echo "  - ID $DISPOSITIF_ID: A840411AB22C33D1 (Centre Médical de Test)"
echo "  - ID $DISPOSITIF2_ID: A840411AB22C33D2 (Hôpital de Test)"
echo ""
echo "Paramètres créés:"
echo "  - ID $PARAMETRES_ID: PAT-TEST-001 (Tension: 120/80)"
echo "  - ID $PARAMETRES2_ID: PAT-TEST-002 (Tension: 140/90)"
echo ""
echo "Diagnostics créés:"
echo "  - ID $DIAGNOSTIC_ID: Diagnostic pour PAT-TEST-001"
echo "  - ID $DIAGNOSTIC2_ID: Diagnostic pour PAT-TEST-002"
echo ""

# ============================================
# RÉSUMÉ FINAL
# ============================================

print_section "RÉSUMÉ DES TESTS"

echo -e "Total de tests: ${BLUE}$TOTAL_TESTS${NC}"
echo -e "Tests réussis:  ${GREEN}$PASSED_TESTS${NC}"
echo -e "Tests échoués:  ${RED}$FAILED_TESTS${NC}"
echo ""

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}✓ TOUS LES TESTS ONT RÉUSSI!${NC}"
    echo ""
    echo "Points clés du nouveau schéma testés:"
    echo "  ✓ DevEUI unique obligatoire"
    echo "  ✓ Pression artérielle séparée (systolique/diastolique)"
    echo "  ✓ Timestamps automatiques (dateMesure, dateDiagnostic)"
    echo "  ✓ Statuts et localisations"
    exit 0
else
    echo -e "${RED}✗ CERTAINS TESTS ONT ÉCHOUÉ${NC}"
    exit 1
fi