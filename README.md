# Car Rental Management API

A **production-style backend system** built with **Spring Boot** that simulates a real-world car rental service. This project focuses on secure authentication, role-based access control, and enforcing business rules such as availability and ownership.

**Live API Documentation (Swagger):**
https://carrental-26hx.onrender.com/swagger-ui/index.html

**Admin Test Credentials / User Test Credentials

Admin
Email: Jean@admin.com
Password: Bani1809

User
Email: Ange@User.com
Password: Bani1809

---

## Overview

This API allows users to:

* Register and authenticate
* Browse available cars
* Create and manage rental bookings

It also includes **administrative functionality** for managing inventory and customer rentals.

The system is designed to reflect real backend constraints:

* Preventing **double-booking** of cars
* Enforcing **ownership of resources**
* Supporting **administrative overrides**

---

## Tech Stack

* **Backend:** Java, Spring Boot
* **Security:** Spring Security, JWT (stateless authentication)
* **Database:** MySQL (AWS RDS)
* **Deployment:** Docker, Render
* **Build Tool:** Maven
* **API Testing & Documentation:** Swagger (OpenAPI), Postman

---

## Security Features

* JWT-based authentication with **stateless session management**
* **BCrypt** password hashing
* **Role-based access control** (`USER`, `ADMIN`)
* Method-level authorization using `@PreAuthorize`
* Custom JWT filter for **token validation and request processing**

---

## Core Business Logic

This project goes beyond basic CRUD operations by enforcing real-world constraints:

* Cars **cannot be rented** if they are unavailable
* Renting a car **updates its availability status**
* Returning a car **restores availability**
* Users can only access and modify **their own rentals**
* Administrators have **full access** to all system resources

---

## Architecture

The application follows a layered architecture:

**Controller → Service → Repository**

* **Controller Layer:** Handles HTTP requests and responses
* **Service Layer:** Contains business logic and validation
* **Repository Layer:** Manages database interaction using JPA

---

## Key Endpoints

> Note: This is a subset of core endpoints. The full API is available via Swagger UI.

### Authentication

* `POST /auth/register` — Register a new user
* `POST /auth/login` — Authenticate and receive a JWT

### Cars

* `GET /cars` — Retrieve all cars (**USER, ADMIN**)
* `POST /cars` — Create a new car (**ADMIN only**)

### Rentals

* `POST /rentals/{carId}/{days}` — Create a rental (**USER**)
* `POST /rentals/admin/{customerId}/{carId}/{days}` — Admin creates rental
* `PUT /rentals/{id}/return` — Return a car (**OWNER or ADMIN**)

### Customers

* `GET /customers/me` — Retrieve current user profile
* `GET /customers` — Retrieve all customers (**ADMIN only**)

---

## Deployment

* Dockerized using a **multi-stage build**
* Deployed on **Render**
* Connected to **AWS RDS MySQL** database

---

## Challenges and Design Decisions

* Implemented **ownership-based access control** to ensure users cannot access or modify other users’ rentals
* Designed **stateless authentication** using JWT, requiring validation on every request
* Enforced **availability constraints** to prevent invalid rental states
* Structured the application with clear **separation of concerns** across layers

---

## Future Improvements

* Add **transaction history tracking** for rentals
* Implement **pagination and filtering** for large datasets
* Add **unit and integration testing** (JUnit, Mockito)
* Introduce **global exception handling** and standardized error responses
* Explore caching (e.g., Redis) for performance optimization

---

## Example Flow

1. User registers and logs in
2. Receives a **JWT token**
3. Uses the token to access **protected endpoints**
4. Creates a rental, updating car availability
5. Returns the car, restoring availability

---

## Author

**Jeancarlos Guerrero**
Aspiring Backend Java Developer

---

## About

This project was built to simulate a real-world backend system and strengthen skills in **backend architecture, authentication, and API design** using Spring Boot.
