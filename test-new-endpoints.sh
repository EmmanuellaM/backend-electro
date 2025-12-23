#!/bin/bash

# Configuration
BASE_URL="http://localhost:8080/api"
MEDECIN_USER="dr.house@electro.com"
MEDECIN_PASS="password123" # This should match the password created or default
# For testing hashing, we might need to create a new doctor or use login endpoint.

echo "==============================================="
echo "TESTING NEW ENDPOINTS FOR BACKEND ELECTRO"
echo "==============================================="

# 1. AUTHENTICATION TEST
echo "1. Testing Authentication..."

# Create a doctor first (if not exists) or use existing.
# Since we hash password on creation/update, let's create a new doc to test login immediately.
# We skip auth if not implemented in this script, but user wants to test endpoints.
# Assuming AuthController works.

# Login and get Token
echo "Logging in as $MEDECIN_USER..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$MEDECIN_USER\", \"motDePasse\":\"$MEDECIN_PASS\"}")

TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo "Login failed or Token not found. Response: $LOGIN_RESPONSE"
    # Proceeding without token might fail for secured endpoints
else
    echo "Login successful. Token acquired."
fi

AUTH_HEADER="Authorization: Bearer $TOKEN"

# 2. DIAGNOSTIC ENDPOINTS
echo -e "\n2. Testing Diagnostic Endpoints..."
echo "Simulating SMS Diagnostic..."
curl -s -X POST "$BASE_URL/diagnostics/avec-notification" \
  -H "Content-Type: application/json" \
  -H "$AUTH_HEADER" \
  -d '{
    "medecinId": 1,
    "parametresId": 1,
    "message": "Alerte urgente: patient en danger",
    "niveauUrgence": "HAUTE"
  }' | cat
echo ""

echo "Getting Recent Diagnostics..."
curl -s -X GET "$BASE_URL/diagnostics/recents" -H "$AUTH_HEADER" | cat
echo ""

# 3. ANALYTICS (STATISTIQUES) ENDPOINTS
echo -e "\n3. Testing Statistics Endpoints..."
echo "Dashboard Stats..."
curl -s -X GET "$BASE_URL/statistiques/dashboard" -H "$AUTH_HEADER" | cat
echo ""

echo "Detailed Stats..."
curl -s -X GET "$BASE_URL/statistiques/detaillees" -H "$AUTH_HEADER" | cat
echo ""

echo "Global Search (query='med')..."
curl -s -X GET "$BASE_URL/search?query=med" -H "$AUTH_HEADER" | cat
echo ""

echo "Fever Alerts..."
curl -s -X GET "$BASE_URL/statistiques/alerts/fever" -H "$AUTH_HEADER" | cat
echo ""

# 4. MEDECIN STATS & STATUS
echo -e "\n4. Testing Medecin Stats & Status..."
MEDECIN_ID=1
echo "Updating Status to 'EN_CONSULTATION'..."
curl -s -X PATCH "$BASE_URL/medecins/$MEDECIN_ID/statut?statut=EN_CONSULTATION" -H "$AUTH_HEADER" | cat
echo ""

echo "Get Medecin Stats..."
curl -s -X GET "$BASE_URL/medecins/$MEDECIN_ID/statistiques" -H "$AUTH_HEADER" | cat
echo ""

# 5. DISPOSITIF STATUS & STATS
echo -e "\n5. Testing Dispositif Stats & Status..."
DISPOSITIF_ID=1
echo "Updating Status to 'ACTIF'..."
curl -s -X PATCH "$BASE_URL/dispositifs/$DISPOSITIF_ID/statut?statut=ACTIF" -H "$AUTH_HEADER" | cat
echo ""

echo "Get Dispositif Stats..."
curl -s -X GET "$BASE_URL/dispositifs/$DISPOSITIF_ID/statistiques" -H "$AUTH_HEADER" | cat
echo ""

# 6. PARAMETRES HISTORY
echo -e "\n6. Testing Parametres History..."
PATIENT_ID="P001" # Ensure variable matches data
echo "History for patient $PATIENT_ID with date range..."
curl -s -X GET "$BASE_URL/parametres/patient/$PATIENT_ID/historique?dateDebut=2023-01-01&dateFin=2025-12-31" -H "$AUTH_HEADER" | cat
echo ""

echo "==============================================="
echo "TESTS COMPLETED"
echo "==============================================="
