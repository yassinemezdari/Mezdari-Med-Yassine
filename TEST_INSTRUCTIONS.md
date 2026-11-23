# Instructions pour résoudre le problème 403

## Étapes à suivre :

### 1. Arrêter complètement l'application
- Dans IntelliJ IDEA, cliquez sur le bouton Stop (carré rouge)
- Attendez que l'application s'arrête complètement

### 2. Nettoyer et recompiler
- Dans IntelliJ IDEA : Build → Rebuild Project
- Ou via Maven : `mvn clean install`

### 3. Redémarrer l'application
- Cliquez sur Run (triangle vert)
- Attendez de voir dans les logs : `Started PediaLinkApplication`

### 4. Vérifier les logs au démarrage
Cherchez ces lignes dans les logs :
```
Mapping [/api/suivi-croissance] to ...
```

### 5. Tester dans Postman

**Test 1 - GET (plus simple)**
- Méthode : GET
- URL : `http://localhost:8080/api/suivi-croissance`
- Résultat attendu : `[]` (liste vide) avec status 200

**Test 2 - POST**
- Méthode : POST
- URL : `http://localhost:8080/api/suivi-croissance`
- Headers : `Content-Type: application/json`
- Body :
```json
{
  "patientId": "patient123",
  "dateMesure": "2024-02-15",
  "poids": 12.5,
  "taille": 85.0,
  "ageEnMois": 18,
  "observation": "Test"
}
```

## Si le problème persiste

### Option A : Tester avec curl
```cmd
curl http://localhost:8080/api/suivi-croissance
```

### Option B : Vérifier le port
L'application tourne-t-elle vraiment sur le port 8080 ?
Cherchez dans les logs : `Tomcat started on port(s): 8080`

### Option C : Désactiver complètement la sécurité (temporaire)
Commentez `@EnableWebSecurity` dans SecurityConfig.java

## Fichiers modifiés
- `SecurityConfig.java` : Ajout de `/api/suivi-croissance/**` aux endpoints publics
- `JwtAuthFilter.java` : Skip du filtre JWT pour `/api/suivi-croissance/`

## Vérification
Les modifications ont été faites correctement. Le problème vient probablement du fait que l'application n'a pas été redémarrée ou que le port est différent.
