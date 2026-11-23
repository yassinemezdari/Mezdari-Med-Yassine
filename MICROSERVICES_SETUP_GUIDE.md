# Guide de mise en place - Architecture Microservices PediaLink

## Architecture

```
┌─────────────────┐
│   Client/UI     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  API Gateway    │ Port 8080
│   (Routage)     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Eureka Server   │ Port 8761
│ (Discovery)     │
└────────┬────────┘
         │
    ┌────┴────┬──────────────┐
    ▼         ▼              ▼
┌────────┐ ┌──────────┐ ┌──────────┐
│ Auth   │ │  Suivi   │ │  User    │
│Service │ │Croissance│ │ Service  │
│ 8081   │ │  8082    │ │  8083    │
└────────┘ └──────────┘ └──────────┘
```

## Structure des projets

```
pedialink-microservices/
├── eureka-server/
│   ├── src/main/java/tn/pedialink/eureka/
│   │   └── EurekaServerApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── api-gateway/
│   ├── src/main/java/tn/pedialink/gateway/
│   │   └── ApiGatewayApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── suivi-croissance-service/
│   ├── src/main/java/tn/pedialink/suivicroissance/
│   │   ├── SuiviCroissanceServiceApplication.java
│   │   ├── controller/
│   │   │   └── SuiviCroissanceController.java
│   │   ├── service/
│   │   │   └── SuiviCroissanceService.java
│   │   ├── repository/
│   │   │   └── SuiviCroissanceRepository.java
│   │   ├── model/
│   │   │   └── SuiviCroissance.java
│   │   └── dto/
│   │       └── SuiviCroissanceDto.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
└── auth-service/ (votre projet actuel adapté)
    └── ...
```

## Étapes de mise en place

### 1. Créer les projets Maven

#### A. Eureka Server
```bash
mkdir -p eureka-server/src/main/java/tn/pedialink/eureka
mkdir -p eureka-server/src/main/resources
```

Copiez :
- `eureka-server-pom.xml` → `eureka-server/pom.xml`
- `eureka-server-application.yml` → `eureka-server/src/main/resources/application.yml`
- `EurekaServerApplication.java` → `eureka-server/src/main/java/tn/pedialink/eureka/`

#### B. API Gateway
```bash
mkdir -p api-gateway/src/main/java/tn/pedialink/gateway
mkdir -p api-gateway/src/main/resources
```

Copiez :
- `api-gateway-pom.xml` → `api-gateway/pom.xml`
- `api-gateway-application.yml` → `api-gateway/src/main/resources/application.yml`
- `ApiGatewayApplication.java` → `api-gateway/src/main/java/tn/pedialink/gateway/`

#### C. Suivi Croissance Service
```bash
mkdir -p suivi-croissance-service/src/main/java/tn/pedialink/suivicroissance
mkdir -p suivi-croissance-service/src/main/resources
```

Copiez :
- `suivi-croissance-service-pom.xml` → `suivi-croissance-service/pom.xml`
- `suivi-croissance-service-application.yml` → `suivi-croissance-service/src/main/resources/application.yml`
- `SuiviCroissanceServiceApplication.java` → `suivi-croissance-service/src/main/java/tn/pedialink/suivicroissance/`
- Les fichiers du package `tn.pedialink` (controller, service, repository, model, dto)

### 2. Ordre de démarrage

**IMPORTANT** : Démarrez les services dans cet ordre :

1. **Eureka Server** (Port 8761)
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```
   Vérifiez : http://localhost:8761

2. **Suivi Croissance Service** (Port 8082)
   ```bash
   cd suivi-croissance-service
   mvn spring-boot:run
   ```

3. **Auth Service** (Port 8081)
   ```bash
   cd auth-service
   mvn spring-boot:run
   ```

4. **API Gateway** (Port 8080)
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

### 3. Vérification

#### Eureka Dashboard
Ouvrez : http://localhost:8761

Vous devriez voir :
- SUIVI-CROISSANCE-SERVICE
- AUTH-SERVICE
- API-GATEWAY

#### Test des endpoints via Gateway

**Via API Gateway (Port 8080)** :
```
POST http://localhost:8080/api/suivi-croissance
GET  http://localhost:8080/api/suivi-croissance
POST http://localhost:8080/api/auth/login
```

**Direct (Port 8082)** :
```
POST http://localhost:8082/api/suivi-croissance
GET  http://localhost:8082/api/suivi-croissance
```

### 4. Adapter le projet actuel

Modifiez `application.yml` de votre projet actuel :

```yaml
server:
  port: 8081

spring:
  application:
    name: auth-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
```

Ajoutez dans `pom.xml` :
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

Ajoutez `@EnableDiscoveryClient` dans `PediaLinkApplication.java`

## Avantages de cette architecture

✅ **Scalabilité** : Chaque service peut être déployé indépendamment
✅ **Isolation** : Une panne dans un service n'affecte pas les autres
✅ **Technologie** : Chaque service peut utiliser sa propre base de données
✅ **Développement** : Équipes peuvent travailler indépendamment
✅ **Déploiement** : Déploiement continu par service

## Prochaines étapes

1. Implémenter la communication inter-services avec OpenFeign
2. Ajouter un Config Server pour la configuration centralisée
3. Implémenter Circuit Breaker avec Resilience4j
4. Ajouter le monitoring avec Spring Boot Admin
5. Implémenter le tracing distribué avec Sleuth/Zipkin
