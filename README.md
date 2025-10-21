# ✈️ SkyGo - Flight Booking System

**Author:** Mirona Botanel  
**Group:** 30233  

SkyGo is a flight booking application that allows users to search flights, make reservations, manage bookings, and enables administrators to manage flights and users. The system supports multiple roles and ensures a secure, scalable, and user-friendly experience.

---

## 📝 Table of Contents

1. [Project Specification](#project-specification)  
2. [Functional Requirements](#functional-requirements)  
3. [Use Case Model](#use-case-model)  
4. [Non-functional Requirements](#non-functional-requirements)  
5. [Design Constraints](#design-constraints)  
6. [Project Structure](#project-structure)  
7. [User Roles & Permissions](#user-roles--permissions)  
8. [Running the Project](#running-the-project)  
9. [Testing](#testing)  
10. [Future Improvements](#future-improvements)  
11. [Conclusion](#conclusion)  
12. [Glossary](#glossary)

---

## Project Specification

SkyGo is designed to handle flight search, bookings, and user management in a modern airline system.  
It supports two main roles: **Customers** and **Administrators**, each with specific permissions.

---

## Functional Requirements

1. **User Management**
   - Users can register and log in.
   - Administrators can manage user accounts.
2. **Flight Management**
   - View available flights by criteria: departure, destination, date.
   - Add, update, or delete flights (Admin only).
3. **Booking Management**
   - Users can book flights and specify seats.
   - Users can view and cancel bookings.
   - Total price is calculated automatically.
4. **Stopover Management**
   - Flights can have multiple stopovers.

---

## Use Case Model

### Use Case 1: User Registration
- **Level:** User  
- **Primary Actor:** Customer  
- **Scenario:**  
  1. User provides name, email, password.  
  2. System validates input and creates an account.  
  3. Confirmation email is sent.  
- **Extensions:** Error if email already exists.

### Use Case 2: Flight Search & Booking
- **Level:** User  
- **Primary Actor:** Customer  
- **Scenario:**  
  1. User inputs search criteria.  
  2. System shows available flights.  
  3. User selects flight, enters seats.  
  4. System calculates total price and confirms booking.  
- **Extensions:** Shows errors if flights unavailable or insufficient seats.

### Use Case 3: Flight Management (Admin)
- **Level:** Admin  
- **Primary Actor:** Administrator  
- **Scenario:**  
  1. Admin logs in.  
  2. Admin adds, updates, or deletes flights.  
- **Extensions:** Cannot delete flights that already have bookings.

---

## Non-functional Requirements

- **Security:** Encrypt sensitive data and transactions.  
- **Performance:** Support 1000+ concurrent users.  
- **Scalability:** Ready for future expansion (international flights).  
- **Usability:** User-friendly, responsive UI.

---

## Design Constraints

- Programming Language: Java (Spring Boot)  
- Database: MySQL  
- Frontend: React + Thymeleaf  
- Architecture: MVC (Model-View-Controller)  
- Security: Hashed passwords and encrypted sensitive data  
- Integration: External email service for account confirmations  

---

## Project Structure

```bash
src/
 ├── main/java/com/example/demo/
 │   ├── controller/   # Handles HTTP requests
 │   ├── service/      # Business logic
 │   ├── repository/   # JPA repositories
 │   ├── model/        # Entity classes
 │   ├── dto/          # Data Transfer Objects
 │   └── mapper/       # DTO <-> Entity mappings
 ├── main/resources/
 │   ├── templates/    # Thymeleaf HTML templates
 │   └── application.properties
 ├── FRONTEND/
 │   ├── src/          # React components
 │   ├── public/       # Static assets
 │   └── package.json
 └── pom.xml           # Maven dependencies
