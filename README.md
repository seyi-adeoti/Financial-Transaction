# Moneymanager

A Spring Boot application for managing financial transactions, with profiles and expense records.

## New feature: Expense and Profile support

This release adds the following relationships:

- `Transaction` can be associated with a `Profile`.
- `Expense` is tied to a `Transaction` and also associated with a `Profile`.
- `Profile` holds ownership information for transactions and expenses.

## Main API endpoints

### Profiles

- `POST /api/v1/profiles`
  - Create a new profile.
  - Request body: `name`, `email`.

- `GET /api/v1/profiles`
  - List all profiles.

- `GET /api/v1/profiles/{id}`
  - Get a single profile by ID.

### Transactions

- `POST /api/v1/transactions`
  - Create a transaction.
  - Optional field: `profileId` to attach a profile.

- `PUT /api/v1/transactions/{id}`
  - Update a transaction.
  - Optional field: `profileId` to attach or change the profile.

- `GET /api/v1/transactions`
  - Retrieve transactions with existing filter options.

- `GET /api/v1/transactions/{id}`
  - Retrieve a transaction by ID, including attached profile info when present.

### Expenses

- `POST /api/v1/expenses`
  - Create an expense tied to a transaction and profile.
  - Request body: `vendor`, `description`, `transactionId`, `profileId`.

- `GET /api/v1/expenses`
  - Retrieve all expense records.

- `GET /api/v1/expenses/{id}`
  - Retrieve an expense record by ID.

## Authentication

### Open endpoints

- `POST /api/v1/auth/signup` — register a new user
- `POST /api/v1/auth/login` — sign in and receive a JWT token
- `POST /api/v1/auth/forgot-password` — request a password reset token
- `POST /api/v1/auth/reset-password` — reset the password using the token

### JWT usage

- Include `Authorization: Bearer <token>` in each protected request.
- Tokens are returned by `POST /api/v1/auth/login`.

### Role-based access

- `transactions` endpoints: `USER`, `MANAGER`, and `ADMIN`
- `expenses` endpoints: `MANAGER` and `ADMIN`
- `profiles` endpoints: `ADMIN` only

### Default accounts

- `admin` / `Admin123!` with role `ADMIN`
- `manager` / `Manager123!` with role `MANAGER`
- `user` / `User123!` with role `USER`

## Running the application

```bash
./mvnw spring-boot:run
```

## Notes

- Profiles must exist before they can be attached to transactions or expenses.
- Expenses are intended to represent expense-specific details that are tied to an existing transaction.
- Transaction responses now include profile details when a profile was assigned.
