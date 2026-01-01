# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Cloud-based microservices banking system with four core services (accounts, loans, cards) and a centralized configuration server. The architecture uses Spring Cloud Config for externalized configuration, RabbitMQ for event-driven communication via Spring Cloud Bus, and Docker for containerization.

## Architecture

### Microservices
- **accounts** (port 8080): Customer account management service
- **loans** (port 8090/8091): Loan management service
- **cards** (port 9000): Card management service
- **configserver** (port 8071): Spring Cloud Config Server for centralized configuration

### Infrastructure Components
- **RabbitMQ** (ports 5672, 15672): Message broker for Spring Cloud Bus
- **H2 Database**: In-memory database for each microservice
- **Docker**: Containerization with multi-environment support (default, qa, prod)

### Configuration Management
- Config Server serves configuration from `configserver/src/main/resources/config/`
- Environment-specific configs: `{service}.yml`, `{service}-qa.yml`, `{service}-prod.yml`
- Services fetch config from Config Server on startup via `spring.config.import`
- RabbitMQ enables dynamic config refresh via `/actuator/busrefresh` endpoint
- Encryption key configured in Config Server for sensitive data (see encrypt.key in configserver application.yml)

### Service Communication
- Each service registers configuration properties via `@EnableConfigurationProperties`
- Spring Cloud Bus propagates configuration changes across all services
- Actuator endpoints exposed for health checks and management

## Build & Run Commands

### Build Individual Service
```bash
cd <service-name>
mvn clean package -DskipTests
```

### Build All Services
```bash
./rebuild-all.sh
```
This script builds Maven packages and Docker images for all services (configserver, accounts, loans, cards).

### Run with Docker Compose

**Default environment:**
```bash
cd docker-compose/default
docker-compose up -d
```

**QA environment:**
```bash
cd docker-compose/qa
docker-compose up -d
```

**Production environment:**
```bash
cd docker-compose/prod
docker-compose up -d
```

**Stop services:**
```bash
docker-compose down
```

### Run Individual Service Locally
```bash
cd <service-name>
mvn spring-boot:run
```
Note: Config Server must be running first for other services to start successfully.

## Testing

### Run Tests for a Service
```bash
cd <service-name>
mvn test
```

### Run Tests with Coverage
```bash
cd <service-name>
mvn test jacoco:report
```

## Key Technical Details

### Technology Stack
- **Java**: 21
- **Spring Boot**: 4.0.0+ (varies by service)
- **Spring Cloud**: 2025.1.0
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Base Image**: eclipse-temurin:21-jdk-alpine

### Code Structure
Each microservice follows standard Spring Boot structure:
- `controller/`: REST API endpoints
- `service/`: Business logic interfaces and implementations
- `repository/`: JPA repositories
- `entity/`: JPA entities with BaseEntity for audit fields
- `dto/`: Data Transfer Objects with validation
- `constants/`: Application constants

### Common Patterns
- All entities extend `BaseEntity` with JPA auditing enabled
- DTOs use Jakarta Bean Validation annotations
- Controllers use OpenAPI annotations for Swagger documentation
- Services use constructor injection with Lombok's `@RequiredArgsConstructor`
- Configuration properties injected via `@ConfigurationProperties` DTOs

### Docker Images
Images are tagged as `dangphat1412/<service>:latest` and should be rebuilt after code changes using `./rebuild-all.sh` or individual Maven + Docker build commands.

### Service Dependencies
Startup order is critical due to dependencies:
1. RabbitMQ (with health check)
2. Config Server (depends on RabbitMQ, with readiness probe)
3. Business services (accounts/loans/cards depend on Config Server)

### Environment Configuration
- Services use `spring.profiles.active` to determine environment (default, qa, prod)
- Docker Compose sets profiles via `SPRING_PROFILES_ACTIVE` environment variable
- Common configurations extracted to `common-config.yml` in docker-compose directories

## API Documentation

Each service exposes Swagger UI at `http://localhost:<port>/swagger-ui.html`:
- Accounts: http://localhost:8080/swagger-ui.html
- Loans: http://localhost:8090/swagger-ui.html (8091 in default docker-compose)
- Cards: http://localhost:9000/swagger-ui.html

## Actuator Endpoints

All services expose management endpoints at `/actuator/*`:
- Health: `/actuator/health`
- Readiness: `/actuator/health/readiness`
- Liveness: `/actuator/health/liveness`
- Refresh Config: `/actuator/busrefresh` (POST to refresh all services)

## Development Workflow

1. **Making Configuration Changes**: Update config files in `configserver/src/main/resources/config/`, then POST to any service's `/actuator/busrefresh` to propagate changes via RabbitMQ
2. **Adding New Service**: Follow the pattern of existing services - include spring-cloud-starter-config and spring-cloud-starter-bus-amqp dependencies, configure `spring.config.import` to point to Config Server
3. **Modifying APIs**: Update controller, service, and repository layers; DTOs should have validation annotations; maintain OpenAPI documentation
4. **Database Changes**: Update entity classes; H2 is configured with `ddl-auto: update` for automatic schema updates

## Important Files

- `rebuild-all.sh`: Script to rebuild all services (Maven + Docker)
- `docker-compose/*/common-config.yml`: Shared Docker Compose configurations
- `configserver/src/main/resources/config/*.yml`: Centralized configuration files for all services and environments