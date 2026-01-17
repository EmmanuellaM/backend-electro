# Backend Spring Boot - Guide d'intégration IA

## Nouveaux endpoints ajoutés

### Diagnostics IA

#### POST /api/diagnostics-ia

Créer un diagnostic IA pour des paramètres donnés

**Requête :**

```json
{
  "parametresId": 1,
  "includeExplanation": true
}
```

**Réponse :** `201 Created`

```json
{
  "id": 1,
  "classePredite": "normal",
  "scoreConfiance": 0.92,
  "probabilites": {
    "normal": 0.92,
    "pre_eclampsie": 0.03,
    ...
  },
  "explication": {
    "parametresInfluents": [
      {
        "nom": "pression_arterielle_systolique",
        "valeur": 118.0,
        "shapValue": 0.15,
        "impact": "positif"
      }
    ],
    "methode": "SHAP",
    "baseValue": 0.5
  },
  "recommandations": [
    {
      "categorie": "Suivi",
      "description": "Continuer le suivi prénatal régulier",
      "priorite": "moyenne"
    }
  ],
  "parametresId": 1,
  "identifiantPatient": "PAT-2024-001",
  "valideParMedecin": null,
  "createdAt": "2024-12-31T02:50:00"
}
```

#### GET /api/diagnostics-ia/{id}

Récupérer un diagnostic IA par son ID

#### GET /api/diagnostics-ia/parametres/{parametresId}

Récupérer le diagnostic IA pour des paramètres donnés

#### GET /api/diagnostics-ia/patient/{identifiantPatient}/history

Récupérer l'historique des diagnostics IA d'un patient

#### GET /api/diagnostics-ia/pending

Récupérer tous les diagnostics IA en attente de validation

#### PUT /api/diagnostics-ia/{id}/validate

Valider ou rejeter un diagnostic IA

**Requête :**

```json
{
  "isValid": true,
  "medecinId": 1,
  "commentaire": "Diagnostic confirmé après examen clinique"
}
```

#### GET /api/diagnostics-ia/ai-service/health

Vérifier l'état du service IA

#### GET /api/diagnostics-ia/ai-service/model-info

Récupérer les informations du modèle IA

## Configuration

Ajouter dans `application.properties` ou créer `application-ai.properties` :

```properties
ai.service.url=http://localhost:5000
ai.service.timeout=10
```

## Migration de base de données

Exécuter le script SQL :

```bash
# Le script V3__create_diagnostic_ia_table.sql sera exécuté automatiquement
# par Flyway/Liquibase au démarrage de l'application
```

## Dépendances ajoutées

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

## Utilisation

1. **Démarrer le service IA** (voir ai-service/README.md)
2. **Démarrer le backend Spring Boot**
3. **Créer des paramètres** via POST /api/parametres
4. **Générer un diagnostic IA** via POST /api/diagnostics-ia
5. **Consulter le diagnostic** avec explications SHAP
6. **Valider le diagnostic** via PUT /api/diagnostics-ia/{id}/validate
