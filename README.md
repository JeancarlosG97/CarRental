# Car Rental Management API

A production-ready REST API built with Spring Boot simulating a real-world car rental service. Focuses on secure authentication, role-based access, and enforcing business rules like availability and ownership.

Live API Documentation (Swagger): [https://carrental-26hx.onrender.com/swagger-ui/index.html](https://carrental-26hx.onrender.com/swagger-ui/index.html)

---

## Demo Credentials

**Admin Account:**  
Email: `admin@demo.com`  
Password: `Password123`

**User Account:**  
Email: `user@demo.com`  
Password: `Password123`

> You can also register new accounts via `/auth/register`.

---

## Quick Overview

This API enables users to:

- Register and authenticate with JWT
- Browse cars and manage rentals
- Return cars and update availability

Administrators can:

- Manage cars and rentals for any customer
- View all customer profiles
- Override constraints when necessary

Key backend features:

- Stateless JWT authentication  
- Role-based access control (`USER`, `ADMIN`)  
- Ownership enforcement on rentals  
- Availability constraints to prevent double-booking  

---

## Tech Stack

- **Java** + **Spring Boot**  
- **Spring Security**, JWT, BCrypt  
- **MySQL** (AWS RDS)  
- **Docker**, Render  
- **Swagger / Postman** for testing  

---

## Core Endpoints

> This is a subset of endpoints; full API is available via Swagger UI.

**Authentication:**  
- `POST /auth/register` — Sign up  
- `POST /auth/login` — Receive JWT  

**Cars:**  
- `GET /cars` — List all cars (USER, ADMIN)  
- `POST /cars` — Create car (ADMIN only)  

**Rentals:**  
- `POST /rentals/{carId}/{days}` — Rent car (USER)  
- `POST /rentals/{customerId}/{carId}/{days}` — Admin creates rental  
- `PUT /rentals/{id}/return` — Return a car (OWNER or ADMIN)  

**Customers:**  
- `GET /customers/me` — Current user profile  
- `PUT /customers/me` — Update profile  
- `GET /customers` — All customers (ADMIN only)  

---

## Architecture & Design

- **Layered architecture:** Controller → Service → Repository  
- **Controllers** handle HTTP requests  
- **Services** implement business logic & validation  
- **Repositories** manage database interactions  

Business rules enforced:

- Rentals only for available cars  
- Ownership checks prevent users from modifying others’ rentals  
- Admins have full system access  

---

## Highlights for Recruiters

- **JWT + Spring Security:** Stateless, role-based authentication  
- **Global exception handling:** Standardized error responses  
- **Ownership enforcement:** Users can only modify their own rentals  
- **Production-style constraints:** Availability & rental rules  

---

## Potential Enhancements

- Rental transaction history  
- Pagination and filtering for large datasets  
- Unit & integration testing (JUnit, Mockito)  
- Caching for performance (Redis)  

---

## Author

**Jeancarlos Guerrero** — Aspiring Backend Java Developer  

---

## About

A real-world backend system demonstrating authentication, API design, and business logic enforcement in Spring Boot.
