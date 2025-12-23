# Endpoints Manquants - Backend MaterniCare

Ce document liste tous les endpoints qui doivent être implémentés dans le backend pour que le frontend fonctionne correctement.

---

## 🔐 1. Authentification & Autorisation

### 1.1 Login
- **Méthode**: `POST`
- **Route**: `/api/auth/login`
- **Body**:
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
- **Réponse**:
  ```json
  {
    "token": "string",
    "user": {
      "id": "number",
      "email": "string",
      "nom": "string",
      "prenom": "string",
      "role": "admin | medecin"
    }
  }
  ```
- **Priorité**: 🔴 **CRITIQUE**

### 1.2 Logout
- **Méthode**: `POST`
- **Route**: `/api/auth/logout`
- **Headers**: `Authorization: Bearer {token}`
- **Priorité**: 🟡 **MOYENNE**

### 1.3 Vérifier le token
- **Méthode**: `GET`
- **Route**: `/api/auth/verify`
- **Headers**: `Authorization: Bearer {token}`
- **Réponse**:
  ```json
  {
    "valid": "boolean",
    "user": { ... }
  }
  ```
- **Priorité**: 🟡 **MOYENNE**

---

## 👨‍⚕️ 2. Gestion des Médecins (Extensions)

### 2.1 Changer le statut d'un médecin
- **Méthode**: `PATCH`
- **Route**: `/api/medecins/{id}/statut`
- **Body**:
  ```json
  {
    "statut": "actif | inactif"
  }
  ```
- **Priorité**: 🔴 **CRITIQUE**

### 2.2 Obtenir les médecins par statut
- **Méthode**: `GET`
- **Route**: `/api/medecins/statut/{statut}`
- **Params**: `statut` = `actif` ou `inactif`
- **Réponse**: `Array<MedecinResponseDTO>`
- **Priorité**: 🟢 **BASSE**

### 2.3 Rechercher des médecins
- **Méthode**: `GET`
- **Route**: `/api/medecins/search`
- **Query params**: `?query=string`
- **Description**: Recherche par nom, prénom, email ou spécialité
- **Réponse**: `Array<MedecinResponseDTO>`
- **Priorité**: 🟡 **MOYENNE**

### 2.4 Obtenir les statistiques d'un médecin
- **Méthode**: `GET`
- **Route**: `/api/medecins/{id}/statistiques`
- **Réponse**:
  ```json
  {
    "nombreDiagnostics": "number",
    "derniereConnexion": "string (ISO date)",
    "diagnosticsAujourdHui": "number",
    "diagnosticsCetteSemaine": "number"
  }
  ```
- **Priorité**: 🟡 **MOYENNE**

---

## 📱 3. Gestion des Dispositifs (Extensions)

### 3.1 Changer le statut d'un dispositif
- **Méthode**: `PATCH`
- **Route**: `/api/dispositifs/{id}/statut`
- **Body**:
  ```json
  {
    "statut": "actif | inactif | maintenance"
  }
  ```
- **Priorité**: 🔴 **CRITIQUE**

### 3.2 Obtenir les dispositifs par statut
- **Méthode**: `GET`
- **Route**: `/api/dispositifs/statut/{statut}`
- **Params**: `statut` = `actif`, `inactif`, ou `maintenance`
- **Réponse**: `Array<DispositifResponseDTO>`
- **Priorité**: 🟡 **MOYENNE**

### 3.3 Obtenir les statistiques d'un dispositif
- **Méthode**: `GET`
- **Route**: `/api/dispositifs/{id}/statistiques`
- **Réponse**:
  ```json
  {
    "nombrePatientsSuivis": "number",
    "derniereMesure": "string (ISO date)",
    "mesuresAujourdHui": "number"
  }
  ```
- **Priorité**: 🟢 **BASSE**

---

## 🩺 4. Gestion des Paramètres (Extensions)

### 4.1 Obtenir les paramètres par statut
- **Méthode**: `GET`
- **Route**: `/api/parametres/statut/{statut}`
- **Params**: `statut` = `en_attente` ou `diagnostique`
- **Réponse**: `Array<ParametresResponseDTO>`
- **Priorité**: 🔴 **CRITIQUE**
- **Note**: Le frontend filtre les patients "en attente" pour la page médecin

### 4.2 Obtenir l'historique d'un patient
- **Méthode**: `GET`
- **Route**: `/api/parametres/patient/{identifiantPatient}/historique`
- **Query params**: `?limit=number` (optionnel)
- **Réponse**: `Array<ParametresResponseDTO>` (triés par date décroissante)
- **Priorité**: 🔴 **CRITIQUE**

### 4.3 Obtenir les dernières mesures d'un patient
- **Méthode**: `GET`
- **Route**: `/api/parametres/patient/{identifiantPatient}/dernieres`
- **Query params**: `?count=4` (par défaut 4)
- **Réponse**: `Array<ParametresResponseDTO>` (les N dernières mesures)
- **Priorité**: 🔴 **CRITIQUE**

### 4.4 Mettre à jour le statut d'un paramètre
- **Méthode**: `PATCH`
- **Route**: `/api/parametres/{id}/statut`
- **Body**:
  ```json
  {
    "statut": "en_attente | diagnostique"
  }
  ```
- **Priorité**: 🔴 **CRITIQUE**
- **Note**: Utilisé quand un médecin envoie un diagnostic

---

## 💊 5. Gestion des Diagnostics (Extensions)

### 5.1 Créer un diagnostic avec notification SMS
- **Méthode**: `POST`
- **Route**: `/api/diagnostics/avec-notification`
- **Body**:
  ```json
  {
    "parametresId": "number",
    "medecinId": "number",
    "contenu": "string",
    "envoyerSMS": "boolean"
  }
  ```
- **Réponse**:
  ```json
  {
    "diagnostic": { ... },
    "smsEnvoye": "boolean",
    "messageErreur": "string | null"
  }
  ```
- **Priorité**: 🔴 **CRITIQUE**

### 5.2 Obtenir les diagnostics récents
- **Méthode**: `GET`
- **Route**: `/api/diagnostics/recents`
- **Query params**: `?limit=10`
- **Réponse**: `Array<DiagnosticResponseDTO>` (triés par date décroissante)
- **Priorité**: 🟡 **MOYENNE**

---

## 📊 6. Statistiques Globales

### 6.1 Dashboard Admin - Statistiques générales
- **Méthode**: `GET`
- **Route**: `/api/statistiques/dashboard`
- **Réponse**:
  ```json
  {
    "dispositifsActifs": "number",
    "totalDispositifs": "number",
    "medecinsActifs": "number",
    "totalMedecins": "number",
    "patientsEnAttente": "number",
    "tauxReponse": "number",
    "diagnosticsAujourdHui": "number",
    "diagnosticsCetteSemaine": "number",
    "tempsReponseMoyen": "string"
  }
  ```
- **Priorité**: 🟡 **MOYENNE**

### 6.2 Statistiques détaillées
- **Méthode**: `GET`
- **Route**: `/api/statistiques/detaillees`
- **Réponse**:
  ```json
  {
    "diagnosticsParPeriode": {
      "jour": "Array<{ date: string, count: number }>",
      "semaine": "Array<{ semaine: string, count: number }>",
      "mois": "Array<{ mois: string, count: number }>"
    },
    "topMedecins": "Array<{ medecinId: number, nom: string, nombreDiagnostics: number }>",
    "dispositifsParStatut": {
      "actif": "number",
      "maintenance": "number",
      "inactif": "number"
    }
  }
  ```
- **Priorité**: 🟢 **BASSE**

---

## 🔍 7. Recherche & Filtrage

### 7.1 Recherche globale
- **Méthode**: `GET`
- **Route**: `/api/search`
- **Query params**: `?query=string&type=medecins|dispositifs|patients`
- **Réponse**:
  ```json
  {
    "medecins": "Array<MedecinResponseDTO>",
    "dispositifs": "Array<DispositifResponseDTO>",
    "patients": "Array<{ identifiantPatient: string, ... }>"
  }
  ```
- **Priorité**: 🟢 **BASSE**

---

## 📝 Résumé des Priorités

### 🔴 CRITIQUE (À implémenter en priorité)
1. Authentification (login)
2. Changement de statut médecins
3. Changement de statut dispositifs
4. Filtrage paramètres par statut (`en_attente`)
5. Historique patient
6. Dernières mesures patient
7. Mise à jour statut paramètre
8. Création diagnostic avec SMS

### 🟡 MOYENNE (Important mais non bloquant)
9. Vérification token
10. Logout
11. Recherche médecins
12. Statistiques médecin
13. Dispositifs par statut
14. Diagnostics récents
15. Dashboard statistiques

### 🟢 BASSE (Nice to have)
16. Médecins par statut
17. Statistiques dispositif
18. Statistiques détaillées
19. Recherche globale

---

## 🛠️ Notes d'Implémentation

### Authentification
- Utiliser JWT pour les tokens
- Stocker le rôle dans le token (admin/medecin)
- Implémenter un middleware de vérification de token

### Statuts
- **Médecin**: `actif`, `inactif`
- **Dispositif**: `actif`, `inactif`, `maintenance`
- **Paramètres**: `en_attente`, `diagnostique`

### SMS
- Intégrer un service SMS (Twilio, Nexmo, ou service local)
- Gérer les erreurs d'envoi gracieusement
- Logger tous les envois de SMS

### Performance
- Implémenter la pagination pour les listes longues
- Ajouter des index sur les colonnes fréquemment filtrées (statut, identifiantPatient)
- Mettre en cache les statistiques si nécessaire

---

## 📌 Endpoints Déjà Disponibles

✅ **Dispositifs**
- GET `/api/dispositifs` - Liste tous les dispositifs
- GET `/api/dispositifs/{id}` - Détails d'un dispositif
- POST `/api/dispositifs` - Créer un dispositif
- PUT `/api/dispositifs/{id}` - Modifier un dispositif
- DELETE `/api/dispositifs/{id}` - Supprimer un dispositif
- GET `/api/dispositifs/search?nomCentre=X` - Recherche par centre

✅ **Médecins**
- GET `/api/medecins` - Liste tous les médecins
- GET `/api/medecins/{id}` - Détails d'un médecin
- POST `/api/medecins` - Créer un médecin
- PUT `/api/medecins/{id}` - Modifier un médecin
- DELETE `/api/medecins/{id}` - Supprimer un médecin
- GET `/api/medecins/email/{email}` - Recherche par email

✅ **Paramètres**
- GET `/api/parametres` - Liste tous les paramètres
- GET `/api/parametres/{id}` - Détails de paramètres
- POST `/api/parametres` - Créer des paramètres
- PUT `/api/parametres/{id}` - Modifier des paramètres
- DELETE `/api/parametres/{id}` - Supprimer des paramètres
- GET `/api/parametres/patient/{identifiantPatient}` - Tous les paramètres d'un patient
- GET `/api/parametres/dispositif/{dispositifId}` - Paramètres par dispositif

✅ **Diagnostics**
- GET `/api/diagnostics` - Liste tous les diagnostics
- GET `/api/diagnostics/{id}` - Détails d'un diagnostic
- POST `/api/diagnostics` - Créer un diagnostic
- PUT `/api/diagnostics/{id}` - Modifier un diagnostic
- DELETE `/api/diagnostics/{id}` - Supprimer un diagnostic
- GET `/api/diagnostics/medecin/{medecinId}` - Diagnostics par médecin
- GET `/api/diagnostics/parametres/{parametresId}` - Diagnostics par paramètres
