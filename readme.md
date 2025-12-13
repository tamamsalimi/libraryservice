# Library Management Service

Library Management Service is a backend application built with **Spring Boot 4** and **Java 17** for managing books, members, and loan transactions. The application uses **PostgreSQL** as its database, supports database schema migration using **Flyway**, includes unit tests for business rules, and provides API documentation using **OpenAPI** rendered via **Redoc**.

---

## Tech Stack

- Java 17
- Spring Boot 4
- Maven
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Validation
- PostgreSQL 15
- Flyway (Database Migration)
- JUnit 5 & Mockito (Unit Testing)
- Docker & Docker Compose
- OpenAPI + Redoc (API Documentation)

---

## Prerequisites

- Java 17
- Maven
- Docker
- Docker Compose

---

## Database

The database is provided using Docker Compose. PostgreSQL runs in a container with the following configuration:

- Database name: `library`
- Username: `library`
- Password: `library`
- Port: `5432`

Start the database:

```bash
docker compose up -d
```

**Build and Run Application**

Build the application using Maven:
```bash
mvn clean package
```
Run the Spring Boot application:
```bash
mvn spring-boot:run
```

The application will be accessible at `http://localhost:8080`.

**Database Migration**

Database schema migration is handled automatically using Flyway when the application starts.
Migration files location:
`src/main/resources/db/migration`
All database schema changes must be applied via Flyway migration scripts.

**Testing**

Unit tests are implemented using JUnit 5 and Mockito, focusing on service-layer business rules.
Run all tests:
```
mvn test
```
**API Documentation**

API documentation is provided using Redoc with a static OpenAPI specification.

Redoc UI:
`http://localhost:8080/redoc.html`

OpenAPI Spec:
`http://localhost:8080/openapi.yaml`




