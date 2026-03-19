# Car Rental API

## Overview
A **Spring Boot** backend REST API for managing cars, customers, and rentals.  
Demonstrates clean architecture, RESTful design, validation, error handling, JWT-based authentication, and MySQL integration.

---

## Tech Stack
- **Backend:** Spring Boot  
- **Database:** MySQL (`CarRental`)  
- **API Testing:** Postman  
- **Version Control:** Git / Github 
- **Other Features:** DTOs, `@Valid` validation, custom exceptions, JWT authentication, ResponseEntity HTTP responses  

---

## Key Features
- **Cars Management:** CRUD operations with availability tracking  
- **Customers Management:** CRUD operations with input validation  
- **Rentals Management:** Create rentals, check availability, update rental status
- **User Authentification: **JWT login with roles(USER, ADMIN) 
- **Error Handling:** Centralized custom exceptions with meaningful HTTP responses  
- **RESTful Design:** Thin controllers with all business logic in the service layer  

---

Authentication Example

Login Request

-POST /auth/login
-Content-Type: application/json
    {
      "username": "jean"
      "password": "1234"
    }

-Login Response
    {
      "id": 1,
      "username": "jean"
      "role": "ADMIN",
      "token":
    }  "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqZWFuIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNjc4MzgyNTYyLCJleHAiOjE2NzgzODYxNjJ9.xxxx"


## Architecture Highlights
- **Controller Layer:** Handles HTTP requests  
- **Service Layer:** Contains all business logic  
- **Repository Layer:** Manages database access using Spring Data JPA  
- **DTO Layer:** Ensures clean API responses and prevents direct entity exposure  
- **Validation & Exceptions:** Improves reliability and maintainability  
