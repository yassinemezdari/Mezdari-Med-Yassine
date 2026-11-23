# Architecture Microservices - PediaLink

## Structure proposée

```
pedialink-platform/
├── eureka-server/              # Service Discovery
├── api-gateway/                # API Gateway
├── auth-service/               # Service d'authentification
├── suivi-croissance-service/   # Service de suivi de croissance
├── user-service/               # Service de gestion des utilisateurs
└── config-server/              # Configuration centralisée (optionnel)
```

## Services à créer

### 1. Eureka Server (Port 8761)
- Service de découverte
- Enregistrement automatique des microservices

### 2. API Gateway (Port 8080)
- Point d'entrée unique
- Routage vers les microservices
- Gestion de la sécurité centralisée

### 3. Auth Service (Port 8081)
- Authentification JWT
- Gestion des utilisateurs
- Endpoints : /api/auth/**

### 4. Suivi Croissance Service (Port 8082)
- Gestion du suivi de croissance
- Endpoints : /api/suivi-croissance/**

## Prochaines étapes

1. Créer les projets Maven pour chaque service
2. Configurer les dépendances Spring Cloud
3. Implémenter la communication inter-services
4. Configurer la base de données par service
