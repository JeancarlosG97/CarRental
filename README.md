# Car Rental Management API

A production-style backend system demonstrating secure authentication, role-based access control, and real-world business logic using Spring Boot.

---

## Live Demo
https://carrental-26hx.onrender.com/swagger-ui/index.html

---

## Highlights

- JWT Authentication & Role-Based Access (USER vs ADMIN)
- Rental system with real-world availability logic
- Ownership enforcement (users can only access their own data)
- Layered architecture (Controller → Service → Repository)
- Deployed API with Swagger documentation

---

## Tech Stack

- Java, Spring Boot  
- Spring Security + JWT  
- MySQL (AWS RDS)  
- Docker + Render  
- Maven, Swagger (OpenAPI)
- Postman

---

## Key Endpoints

*Note: This is a subset of core endpoints. Full API is available via Swagger UI.*

### Auth
- `POST /auth/register`
- `POST /auth/login`

### Cars
- `GET /cars` *(USER, ADMIN)*
- `POST /cars` *(ADMIN)*

### Rentals
- `POST /rentals/{carId}/{days}` *(USER, ADMIN)*
- `POST /rentals/admin/{customerId}/{carId}/{days}` *(ADMIN)*
- `PUT /rentals/{id}/return` *(OWNER or ADMIN)*

### Customers
- `GET /customers/me` *(USER, ADMIN)*
- `GET /customers` *(ADMIN)*

---

## Security

- JWT-based authentication (stateless)
- BCrypt password hashing
- Role-based access with `@PreAuthorize`
- Custom JWT filter for request validation

---

## Core Logic

- Cars cannot be rented if unavailable  
- Renting updates availability  
- Returning restores availability  
- Users can only access their own rentals  
- Admins have full system access  

---

## Deployment

- Dockerized with multi-stage build  
- Deployed on Render  
- Connected to AWS RDS MySQL  

---

## What I Learned

- Backend architecture design  
- JWT authentication & authorization  
- Ownership-based security  
- Deploying production-ready APIs  

---

## Author

**Jeancarlos Guerrero**  
Aspiring Backend Java Developer  
