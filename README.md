# MoneyManager

MoneyManager is a Spring Boot personal finance backend that helps users track transactions, expenses, budgets, and notifications.

## Features

- JWT-based authentication with role-based access control
- User signup, login, forgot-password, and password reset
- Transaction creation, update, deletion, and summary retrieval
- Expense and budget management
- Profile and category support
- Email notification support for budget alerts
- OpenAPI / Swagger documentation
- Docker and CI support for reproducible development

## Tech stack

- Java 17
- Spring Boot 3.3
- Spring Security
- Spring Data JPA
- PostgreSQL (production)
- H2 (test)
- SpringDoc OpenAPI
- Docker
- GitHub Actions

## Prerequisites

- Java 17
- Docker (optional)
- Maven (optional, since `./mvnw` is included)

## Setup

1. Copy the example environment file:

```bash
cp .env.example .env
```

2. Update `.env` with your database and JWT values.

3. Start PostgreSQL locally or use Docker Compose.

## Run locally

```bash
./mvnw spring-boot:run
```

The application starts on port `9090` by default.

## Run with Docker

```bash
docker compose up --build
```

Then visit `http://localhost:9090/swagger-ui.html` for API documentation.

## Run tests

```bash
./mvnw test
```

A test profile with H2 is configured in `src/test/resources/application-test.properties`.

## API documentation

Swagger UI is available at:

- `http://localhost:9090/swagger-ui.html`

OpenAPI JSON is available at:

- `http://localhost:9090/api-docs`

## Auth endpoints

- `POST /api/v1/auth/signup`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`

## Protected endpoints

- `GET /api/v1/transactions`
- `POST /api/v1/transactions`
- `GET /api/v1/expenses`
- `POST /api/v1/expenses`
- `GET /api/v1/profiles`

## Default demo accounts

- `admin` / `Admin123!` — role `ADMIN`
- `manager` / `Manager123!` — role `MANAGER`
- `user` / `User123!` — role `USER`

## Notes

- Environment variables are supported for database credentials, JWT secret, and mail configuration.
- `application.properties` uses fallback values when environment variables are not provided.
- CI is configured in `.github/workflows/ci.yml`.
- Docker support is provided by `Dockerfile` and `docker-compose.yml`.

## Next improvements

- Add more end-to-end tests for budgets and expenses
- Add a frontend or Postman collection for manual demo
- Add database migration support with Flyway or Liquibase
