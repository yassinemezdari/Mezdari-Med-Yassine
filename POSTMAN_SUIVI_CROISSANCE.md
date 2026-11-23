# Tests Postman - API Suivi Croissance

## Configuration
- Base URL: `http://localhost:8080/api/suivi-croissance`
- Headers: `Content-Type: application/json`

## 1. Créer un suivi de croissance
**POST** `/api/suivi-croissance`

```json
{
  "patientId": "patient123",
  "dateMesure": "2024-02-15",
  "poids": 12.5,
  "taille": 85.0,
  "ageEnMois": 18,
  "observation": "Croissance normale, bon développement"
}
```

**Réponse attendue (201 Created):**
```json
{
  "id": "65d1234567890abcdef12345",
  "patientId": "patient123",
  "dateMesure": "2024-02-15",
  "poids": 12.5,
  "taille": 85.0,
  "imc": 17.3,
  "ageEnMois": 18,
  "statut": "NORMAL",
  "observation": "Croissance normale, bon développement",
  "createdAt": "2024-02-15T10:30:00"
}
```

## 2. Récupérer tous les suivis
**GET** `/api/suivi-croissance`

**Réponse attendue (200 OK):**
```json
[
  {
    "id": "65d1234567890abcdef12345",
    "patientId": "patient123",
    "dateMesure": "2024-02-15",
    "poids": 12.5,
    "taille": 85.0,
    "imc": 17.3,
    "ageEnMois": 18,
    "statut": "NORMAL",
    "observation": "Croissance normale",
    "createdAt": "2024-02-15T10:30:00"
  }
]
```

## 3. Récupérer un suivi par ID
**GET** `/api/suivi-croissance/{id}`

Exemple: `GET /api/suivi-croissance/65d1234567890abcdef12345`

**Réponse attendue (200 OK):** Même structure qu'au point 1

## 4. Récupérer tous les suivis d'un patient
**GET** `/api/suivi-croissance/patient/{patientId}`

Exemple: `GET /api/suivi-croissance/patient/patient123`

**Réponse attendue (200 OK):** Liste des suivis triés par date décroissante

## 5. Mettre à jour un suivi
**PUT** `/api/suivi-croissance/{id}`

Exemple: `PUT /api/suivi-croissance/65d1234567890abcdef12345`

```json
{
  "patientId": "patient123",
  "dateMesure": "2024-02-15",
  "poids": 13.0,
  "taille": 86.0,
  "ageEnMois": 19,
  "observation": "Prise de poids satisfaisante"
}
```

**Réponse attendue (200 OK):** Suivi mis à jour avec IMC recalculé

## 6. Supprimer un suivi
**DELETE** `/api/suivi-croissance/{id}`

Exemple: `DELETE /api/suivi-croissance/65d1234567890abcdef12345`

**Réponse attendue (200 OK):**
```json
{
  "message": "Suivi croissance supprimé avec succès"
}
```

## Cas de test supplémentaires

### Test avec retard léger
```json
{
  "patientId": "patient456",
  "dateMesure": "2024-02-15",
  "poids": 10.0,
  "taille": 80.0,
  "ageEnMois": 18,
  "observation": "À surveiller"
}
```
**Statut attendu:** `RETARD_LEGER` (IMC ≈ 15.6)

### Test avec retard sévère
```json
{
  "patientId": "patient789",
  "dateMesure": "2024-02-15",
  "poids": 8.0,
  "taille": 80.0,
  "ageEnMois": 18,
  "observation": "Consultation nutritionniste recommandée"
}
```
**Statut attendu:** `RETARD_SEVERE` (IMC ≈ 12.5)

## Erreurs attendues

### Validation - Poids manquant
**POST** `/api/suivi-croissance`
```json
{
  "patientId": "patient123",
  "dateMesure": "2024-02-15",
  "taille": 85.0
}
```
**Réponse (400 Bad Request):**
```json
{
  "message": "Poids doit être positif"
}
```

### ID inexistant
**GET** `/api/suivi-croissance/id_inexistant`

**Réponse (400 Bad Request):**
```json
{
  "message": "Suivi croissance non trouvé"
}
```

## Notes
- L'IMC est calculé automatiquement : IMC = poids (kg) / (taille (m))²
- Le statut est déterminé automatiquement selon l'IMC
- Les suivis d'un patient sont triés par date décroissante
