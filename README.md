# 📚 Library Management Service

<p align="center">
  <img src="https://raw.githubusercontent.com/simple-icons/simple-icons/develop/icons/springboot.svg" width="48"/>
  <img src="java-icon.svg" width="48"/>
  <img src="https://raw.githubusercontent.com/simple-icons/simple-icons/develop/icons/postgresql.svg" width="48"/>
  <img src="https://raw.githubusercontent.com/simple-icons/simple-icons/develop/icons/docker.svg" width="48"/>
</p>

<p align="center">
  <b>Spring Boot 4 · Java 17 · PostgreSQL · Flyway · Redoc</b>
</p>

---

Library Management Service is a backend application built with **Spring Boot 4** and **Java 17** for managing books, members, and loan transactions.  
The application uses **PostgreSQL** as its database, supports database schema migration using **Flyway**, includes unit tests for business rules, and provides API documentation using **OpenAPI** rendered via **Redoc**.

---

## 🛠 Tech Stack

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
- Spring Boot Actuator & Micrometer (Metrics)
- Log4j (Logging)

---

## 📦 Prerequisites

- Java 17
- Maven
- Docker
- Docker Compose

---

## 🗄 Database

The database is provided using **Docker Compose**. PostgreSQL runs in a container with the following configuration:

- Database name: `library`
- Username: `library`
- Password: `library`
- Port: `5432`

Start the database:

```bash
docker compose up -d
```

---

## 🚀 Build and Run Application

Build the application using Maven:

```bash
mvn clean package
```

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

Application URL:

```
http://localhost:8080
```

---

## 🧬 Database Migration

Database schema migration is handled automatically using **Flyway** when the application starts.

Migration files location:

```
src/main/resources/db/migration
```

All schema changes must be applied via Flyway migration scripts.

---

## 🧪 Testing

Unit tests are implemented using **JUnit 5** and **Mockito**, focusing on service-layer business rules.

Run all tests:

```bash
mvn test
```

Test coverage includes:
1. Maximum active loans per member
2. Blocking borrowing when overdue loans exist
3. Successful borrowing flow and data persistence

---

## 📑 API Documentation

API documentation is provided using **Redoc** with a static OpenAPI specification.

- Redoc UI
  ```
  http://localhost:8080/redoc.html
  ```

- OpenAPI Spec
  ```
  http://localhost:8080/openapi.yaml
  ```

---

## 📊 Metrics & Monitoring

Application metrics are exposed using **Spring Boot Actuator** and **Micrometer**.

- Health endpoint
  ```
  http://localhost:8080/actuator/health
  ```

- Metrics endpoint
  ```
  http://localhost:8080/actuator/metrics
  ```

---

## 📝 Logging

Application logging is handled using **Log4j** through Spring Boot’s logging abstraction.

- Structured logging enabled
- Log levels configurable per environment
- Can be integrated with external log aggregation systems

---

## 🗂 Project Structure

```
management-service
├── docker-compose.yml
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   └── resources
    │       ├── static
    │       │   ├── redoc.html
    │       │   └── openapi.yaml
    │       └── db
    │           └── migration
    └── test
        └── java
            └── com.library.managementservice.service
                └── LoanServiceTest.java
```

---

<p align="center">
  🚀 Ready for GitHub · Assignment · Review
</p>
