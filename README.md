# 🚀 Backend MaterniCare - Guide de Compilation et d'Installation

Ce projet est le backend de l'application MaterniCare, une solution IoT pour le suivi médical des femmes enceintes. Il est construit avec **Spring Boot 3.4**, **Java 17** et **PostgreSQL**.

---

## 🛠️ Guide d'Installation Rapide

### 1. Prérequis

Assurez-vous d'avoir installé sur votre machine :

- **JDK 17** (indispensable)
- **PostgreSQL** (v14 ou supérieure recommandée)
- **Git** (pour cloner le projet)

### 2. Configuration de la Base de Données

1. Lancez PostgreSQL et créez une base de données nommée `bd_electro` :

   ```sql
   CREATE DATABASE bd_electro;
   ```

2. Créez un utilisateur ou utilisez `postgres`. Par défaut, le projet cherche l'utilisateur `admin` avec le mot de passe `admin123`. Vous pouvez modifier cela dans le fichier `src/main/resources/application.properties`.

### 3. Compilation et Build

Utilisez le wrapper Maven (`mvnw`) inclus dans le projet pour compiler :

```bash
# Donner les permissions d'exécution (Linux/macOS)
chmod +x mvnw

# Compiler et installer les dépendances
./mvnw clean install
```

### 4. Lancement de l'Application

Pour lancer le serveur en mode développement :

```bash
./mvnw spring-boot:run
```

L'API sera accessible sur `http://localhost:8080/api`.

### 5. Documentation API (Swagger/OpenAPI)

Une fois l'application lancée, vous pouvez visualiser et tester l'API via Swagger UI :
👉 [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)

---

## 📋 Table des Matières (Documentation API)

1. [Medecins](#medecins)
2. [Dispositifs](#dispositifs)
3. [Parametres](#parametres)
4. [Diagnostics](#diagnostics)
5. [Codes de Reponse HTTP](#codes-de-reponse-http)
6. [Gestion des Erreurs](#gestion-des-erreurs)

---

## Medecins

### Créer un médecin

**Endpoint:** `POST /api/medecins`

**Body:**

```json
{
  "nom": "Mbarga",
  "prenom": "Paul",
  "email": "paul.mbarga@hopital.cm",
  "tel": "+237690123456"
}
```

**Réponse:** `201 Created`

```json
{
  "id": 1,
  "nom": "Mbarga",
  "prenom": "Paul",
  "email": "paul.mbarga@hopital.cm",
  "tel": "+237690123456",
  "nombreDiagnostics": 0
}
```

**Curl:**

```bash
curl -X POST http://localhost:8080/api/medecins \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Mbarga",
    "prenom": "Paul",
    "email": "paul.mbarga@hopital.cm",
    "tel": "+237690123456"
  }'
```

---

### Récupérer tous les médecins

**Endpoint:** `GET /api/medecins`

**Réponse:** `200 OK`

```json
[
  {
    "id": 1,
    "nom": "Mbarga",
    "prenom": "Paul",
    "email": "paul.mbarga@hopital.cm",
    "tel": "+237690123456",
    "nombreDiagnostics": 5
  },
  {
    "id": 2,
    "nom": "Atangana",
    "prenom": "Marie",
    "email": "marie.atangana@hopital.cm",
    "tel": "+237698888888",
    "nombreDiagnostics": 3
  }
]
```

**Curl:**

```bash
curl http://localhost:8080/api/medecins
```

---

### Récupérer un médecin par ID

**Endpoint:** `GET /api/medecins/{id}`

**Réponse:** `200 OK`

```json
{
  "id": 1,
  "nom": "Mbarga",
  "prenom": "Paul",
  "email": "paul.mbarga@hopital.cm",
  "tel": "+237690123456",
  "nombreDiagnostics": 5
}
```

**Curl:**

```bash
curl http://localhost:8080/api/medecins/1
```

**Erreur si non trouvé:** `404 Not Found`

---

### Récupérer un médecin par email

**Endpoint:** `GET /api/medecins/email/{email}`

**Réponse:** `200 OK`

```json
{
  "id": 1,
  "nom": "Mbarga",
  "prenom": "Paul",
  "email": "paul.mbarga@hopital.cm",
  "tel": "+237690123456",
  "nombreDiagnostics": 5
}
```

**Curl:**

```bash
curl http://localhost:8080/api/medecins/email/paul.mbarga@hopital.cm
```

---

### Mettre à jour un médecin

**Endpoint:** `PUT /api/medecins/{id}`

**Body:**

```json
{
  "nom": "Mbarga",
  "prenom": "Paul",
  "email": "nouveau.email@hopital.cm",
  "tel": "+237699999999"
}
```

**Réponse:** `200 OK`

```json
{
  "id": 1,
  "nom": "Mbarga",
  "prenom": "Paul",
  "email": "nouveau.email@hopital.cm",
  "tel": "+237699999999",
  "nombreDiagnostics": 5
}
```

**Curl:**

```bash
curl -X PUT http://localhost:8080/api/medecins/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Mbarga",
    "prenom": "Paul",
    "email": "nouveau.email@hopital.cm",
    "tel": "+237699999999"
  }'
```

---

### Supprimer un médecin

**Endpoint:** `DELETE /api/medecins/{id}`

**Réponse:** `204 No Content`

**Curl:**

```bash
curl -X DELETE http://localhost:8080/api/medecins/1
```

---

## Dispositifs

### Créer un dispositif

**Endpoint:** `POST /api/dispositifs`

**Body:**

```json
{
  "nomCentreDeSante": "Centre Médical de Yaoundé",
  "contact": "+237699876543"
}
```

**Réponse:** `201 Created`

```json
{
  "id": 1,
  "nomCentreDeSante": "Centre Médical de Yaoundé",
  "contact": "+237699876543",
  "nombreParametres": 0
}
```

**Curl:**

```bash
curl -X POST http://localhost:8080/api/dispositifs \
  -H "Content-Type: application/json" \
  -d '{
    "nomCentreDeSante": "Centre Médical de Yaoundé",
    "contact": "+237699876543"
  }'
```

---

### Récupérer tous les dispositifs

**Endpoint:** `GET /api/dispositifs`

**Réponse:** `200 OK`

**Curl:**

```bash
curl http://localhost:8080/api/dispositifs
```

---

### Récupérer un dispositif par ID

**Endpoint:** `GET /api/dispositifs/{id}`

**Réponse:** `200 OK`

**Curl:**

```bash
curl http://localhost:8080/api/dispositifs/1
```

---

### Rechercher des dispositifs par nom de centre

**Endpoint:** `GET /api/dispositifs/search?nomCentre=Yaoundé`

**Réponse:** `200 OK`

```json
[
  {
    "id": 1,
    "nomCentreDeSante": "Centre Médical de Yaoundé",
    "contact": "+237699876543",
    "nombreParametres": 10
  }
]
```

**Curl:**

```bash
curl "http://localhost:8080/api/dispositifs/search?nomCentre=Yaoundé"
```

---

### Mettre à jour un dispositif

**Endpoint:** `PUT /api/dispositifs/{id}`

**Body:**

```json
{
  "nomCentreDeSante": "Centre de Santé Mvog-Ada",
  "contact": "+237677777777"
}
```

**Réponse:** `200 OK`

**Curl:**

```bash
curl -X PUT http://localhost:8080/api/dispositifs/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nomCentreDeSante": "Centre de Santé Mvog-Ada",
    "contact": "+237677777777"
  }'
```

---

### Supprimer un dispositif

**Endpoint:** `DELETE /api/dispositifs/{id}`

**Réponse:** `204 No Content`

**Curl:**

```bash
curl -X DELETE http://localhost:8080/api/dispositifs/1
```

---

## Parametres

### Créer des paramètres

**Endpoint:** `POST /api/parametres`

**Body:**

```json
{
  "identifiantPatient": "PAT-2024-001",
  "poidsPatient": 68.50,
  "temperature": 37.10,
  "pressionArterielle": 118,
  "frequenceFoetale": 145,
  "dispositifId": 1
}
```

**Réponse:** `201 Created`

```json
{
  "id": 1,
  "identifiantPatient": "PAT-2024-001",
  "poidsPatient": 68.50,
  "temperature": 37.10,
  "pressionArterielle": 118,
  "frequenceFoetale": 145,
  "dispositifId": 1,
  "nomCentreDeSante": "Centre Médical de Yaoundé",
  "nombreDiagnostics": 0
}
```

**Curl:**

```bash
curl -X POST http://localhost:8080/api/parametres \
  -H "Content-Type: application/json" \
  -d '{
    "identifiantPatient": "PAT-2024-001",
    "poidsPatient": 68.50,
    "temperature": 37.10,
    "pressionArterielle": 118,
    "frequenceFoetale": 145,
    "dispositifId": 1
  }'
```

---

### Récupérer tous les paramètres

**Endpoint:** `GET /api/parametres`

**Réponse:** `200 OK`

**Curl:**

```bash
curl http://localhost:8080/api/parametres
```

---

### Récupérer des paramètres par ID

**Endpoint:** `GET /api/parametres/{id}`

**Réponse:** `200 OK`

**Curl:**

```bash
curl http://localhost:8080/api/parametres/1
```

---

### Récupérer les paramètres d'un patient

**Endpoint:** `GET /api/parametres/patient/{identifiantPatient}`

**Réponse:** `200 OK`

```json
[
  {
    "id": 1,
    "identifiantPatient": "PAT-2024-001",
    "poidsPatient": 68.50,
    "temperature": 37.10,
    "pressionArterielle": 118,
    "frequenceFoetale": 145,
    "dispositifId": 1,
    "nomCentreDeSante": "Centre Médical de Yaoundé",
    "nombreDiagnostics": 2
  }
]
```

**Curl:**

```bash
curl http://localhost:8080/api/parametres/patient/PAT-2024-001
```

---

### Récupérer les paramètres par dispositif

**Endpoint:** `GET /api/parametres/dispositif/{dispositifId}`

**Réponse:** `200 OK`

**Curl:**

```bash
curl http://localhost:8080/api/parametres/dispositif/1
```

---

### Mettre à jour des paramètres

**Endpoint:** `PUT /api/parametres/{id}`

**Body:**

```json
{
  "identifiantPatient": "PAT-2024-001",
  "poidsPatient": 69.00,
  "temperature": 36.80,
  "pressionArterielle": 120,
  "frequenceFoetale": 142,
  "dispositifId": 1
}
```

**Réponse:** `200 OK`

**Curl:**

```bash
curl -X PUT http://localhost:8080/api/parametres/1 \
  -H "Content-Type: application/json" \
  -d '{
    "identifiantPatient": "PAT-2024-001",
    "poidsPatient": 69.00,
    "temperature": 36.80,
    "pressionArterielle": 120,
    "frequenceFoetale": 142,
    "dispositifId": 1
  }'
```

---

### Supprimer des paramètres

**Endpoint:** `DELETE /api/parametres/{id}`

**Réponse:** `204 No Content`

**Curl:**

```bash
curl -X DELETE http://localhost:8080/api/parametres/1
```

---

## Diagnostics

### Créer un diagnostic

**Endpoint:** `POST /api/diagnostics`

**Body:**

```json
{
  "contenu": "Patient en bonne santé générale. Poids: 68.5 kg - Dans la norme. Température: 37.1°C - Normale. Pression artérielle: 118 - Optimale. Fréquence fœtale: 145 bpm - Normale. Recommandation: Continuer le suivi prénatal régulier.",
  "medecinId": 1,
  "parametresId": 1
}
```

**Réponse:** `201 Created`

```json
{
  "id": 1,
  "contenu": "Patient en bonne santé...",
  "medecinId": 1,
  "medecinNom": "Mbarga",
  "medecinPrenom": "Paul",
  "medecinEmail": "paul.mbarga@hopital.cm",
  "parametresId": 1,
  "identifiantPatient": "PAT-2024-001",
  "poidsPatient": 68.50,
  "temperature": 37.10,
  "pressionArterielle": 118,
  "frequenceFoetale": 145
}
```

**Curl:**

```bash
curl -X POST http://localhost:8080/api/diagnostics \
  -H "Content-Type: application/json" \
  -d '{
    "contenu": "Patient en bonne santé...",
    "medecinId": 1,
    "parametresId": 1
  }'
```

---

### Récupérer tous les diagnostics

**Endpoint:** `GET /api/diagnostics`

**Réponse:** `200 OK`

**Curl:**

```bash
curl http://localhost:8080/api/diagnostics
```

---

### Récupérer un diagnostic par ID

**Endpoint:** `GET /api/diagnostics/{id}`

**Réponse:** `200 OK`

**Curl:**

```bash
curl http://localhost:8080/api/diagnostics/1
```

---

### Récupérer les diagnostics d'un médecin

**Endpoint:** `GET /api/diagnostics/medecin/{medecinId}`

**Réponse:** `200 OK`

**Curl:**

```bash
curl http://localhost:8080/api/diagnostics/medecin/1
```

---

### Récupérer les diagnostics basés sur des paramètres

**Endpoint:** `GET /api/diagnostics/parametres/{parametresId}`

**Réponse:** `200 OK`

**Curl:**

```bash
curl http://localhost:8080/api/diagnostics/parametres/1
```

---

### Mettre à jour un diagnostic

**Endpoint:** `PUT /api/diagnostics/{id}`

**Body:**

```json
{
  "contenu": "Diagnostic mis à jour...",
  "medecinId": 1,
  "parametresId": 1
}
```

**Réponse:** `200 OK`

**Curl:**

```bash
curl -X PUT http://localhost:8080/api/diagnostics/1 \
  -H "Content-Type: application/json" \
  -d '{
    "contenu": "Diagnostic mis à jour...",
    "medecinId": 1,
    "parametresId": 1
  }'
```

---

### Supprimer un diagnostic

**Endpoint:** `DELETE /api/diagnostics/{id}`

**Réponse:** `204 No Content`

**Curl:**

```bash
curl -X DELETE http://localhost:8080/api/diagnostics/1
```

---

## Codes de Reponse HTTP

| Code | Signification | Description |
| :--- | :--- | :--- |
| **200** | OK | Requête réussie |
| **201** | Created | Ressource créée avec succès |
| **204** | No Content | Suppression réussie (pas de contenu) |
| **400** | Bad Request | Erreur de validation des données |
| **404** | Not Found | Ressource non trouvée |
| **409** | Conflict | Conflit (ex: email déjà existant) |
| **500** | Internal Server Error | Erreur serveur |

---

## Gestion des Erreurs

Toutes les erreurs retournent une structure JSON uniforme:

### Ressource non trouvée (404)

**Requête:**

```bash
curl http://localhost:8080/api/medecins/999
```

**Réponse:** `404 Not Found`

```json
{
  "timestamp": "2024-12-03T10:30:00",
  "status": 404,
  "message": "Médecin non trouvé(e) avec id : '999'",
  "path": "/api/medecins/999"
}
```

---

### Email déjà existant (409)

**Requête:**

```bash
curl -X POST http://localhost:8080/api/medecins \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test",
    "prenom": "Test",
    "email": "paul.mbarga@hopital.cm",
    "tel": "1234"
  }'
```

**Réponse:** `409 Conflict`

```json
{
  "timestamp": "2024-12-03T10:31:00",
  "status": 409,
  "message": "Un médecin avec l'email 'paul.mbarga@hopital.cm' existe déjà",
  "path": "/api/medecins"
}
```

---

### Erreur de validation (400)

**Requête:**

```bash
curl -X POST http://localhost:8080/api/medecins \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "",
    "prenom": "Test",
    "email": "email-invalide",
    "tel": "1234"
  }'
```

**Réponse:** `400 Bad Request`

```json
{
  "timestamp": "2024-12-03T10:32:00",
  "status": 400,
  "message": "Erreur de validation des données",
  "path": "/api/medecins",
  "details": [
    "nom: Le nom du médecin est obligatoire",
    "email: L'email doit être valide"
  ]
}
```

---

## 🔄 Workflow Complet

### Exemple: Créer un diagnostic complet

```bash
# 1. Créer un médecin
MEDECIN=$(curl -s -X POST http://localhost:8080/api/medecins \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Mbarga",
    "prenom": "Paul",
    "email": "paul@hopital.cm",
    "tel": "+237690123456"
  }')
MEDECIN_ID=$(echo $MEDECIN | jq -r '.id')

# 2. Créer un dispositif
DISPOSITIF=$(curl -s -X POST http://localhost:8080/api/dispositifs \
  -H "Content-Type: application/json" \
  -d '{
    "nomCentreDeSante": "Centre Médical de Yaoundé",
    "contact": "+237699876543"
  }')
DISPOSITIF_ID=$(echo $DISPOSITIF | jq -r '.id')

# 3. Créer des paramètres
PARAMETRES=$(curl -s -X POST http://localhost:8080/api/parametres \
  -H "Content-Type: application/json" \
  -d "{
    \"identifiantPatient\": \"PAT-2024-001\",
    \"poidsPatient\": 68.50,
    \"temperature\": 37.10,
    \"pressionArterielle\": 118,
    \"frequenceFoetale\": 145,
    \"dispositifId\": $DISPOSITIF_ID
  }")
PARAMETRES_ID=$(echo $PARAMETRES | jq -r '.id')

# 4. Créer le diagnostic
curl -X POST http://localhost:8080/api/diagnostics \
  -H "Content-Type: application/json" \
  -d "{
    \"contenu\": \"Patient en bonne santé générale.\",
    \"medecinId\": $MEDECIN_ID,
    \"parametresId\": $PARAMETRES_ID
  }"
```

---

## 📚 Résumé des Endpoints

### Liste des Endpoints Médecins

- `POST /api/medecins` - Créer
- `GET /api/medecins` - Liste tous
- `GET /api/medecins/{id}` - Un par ID
- `GET /api/medecins/email/{email}` - Un par email
- `PUT /api/medecins/{id}` - Modifier
- `DELETE /api/medecins/{id}` - Supprimer

### Liste des Endpoints Dispositifs

- `POST /api/dispositifs` - Créer
- `GET /api/dispositifs` - Liste tous
- `GET /api/dispositifs/{id}` - Un par ID
- `GET /api/dispositifs/search?nomCentre=X` - Rechercher
- `PUT /api/dispositifs/{id}` - Modifier
- `DELETE /api/dispositifs/{id}` - Supprimer

### Liste des Endpoints Paramètres

- `POST /api/parametres` - Créer
- `GET /api/parametres` - Liste tous
- `GET /api/parametres/{id}` - Un par ID
- `GET /api/parametres/patient/{id}` - Par patient
- `GET /api/parametres/dispositif/{id}` - Par dispositif
- `PUT /api/parametres/{id}` - Modifier
- `DELETE /api/parametres/{id}` - Supprimer

### Liste des Endpoints Diagnostics

- `POST /api/diagnostics` - Créer
- `GET /api/diagnostics` - Liste tous
- `GET /api/diagnostics/{id}` - Un par ID
- `GET /api/diagnostics/medecin/{id}` - Par médecin
- `GET /api/diagnostics/parametres/{id}` - Par paramètres
- `PUT /api/diagnostics/{id}` - Modifier
- `DELETE /api/diagnostics/{id}` - Supprimer

---

**API complète et prête à l'emploi!**
