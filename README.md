# Myronabotanel Flight Booking System

A comprehensive flight booking system built with **Java (Spring Boot)** for the backend and **React/JavaScript** for the frontend. The system allows users to search for flights, make reservations, manage bookings, and provides administrators with management capabilities for flights and users.

## Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Functional Requirements](#functional-requirements)
- [Non-Functional Requirements](#non-functional-requirements)
- [Project Structure](#project-structure)
- [Testing](#testing)
- [Future Improvements](#future-improvements)
- [Getting Started](#getting-started)
- [Author](#author)
- [References](#references)

---

## Features
- **User Management**: Registration, login, and profile management.
- **Flight Management (Admin Only)**: Add, edit, and delete flights.
- **Booking Management**: Users can book, view, and cancel flights. Total price is calculated automatically.
- **Stopover Management**: Flights can include multiple stopovers.
- **Favorite Flights**: Users can mark flights as favorites.
- **Secure Authentication**: Passwords are hashed, and sensitive data is encrypted.

---

## Technology Stack
- **Backend**: Java, Spring Boot, JPA/Hibernate
- **Frontend**: React.js, JavaScript, CSS
- **Database**: MySQL
- **Build Tools**: Maven
- **Version Control**: Git, GitHub
- **Testing**: JUnit 5, Mockito

---

## Architecture
The system follows a **modular MVC architecture**, with separation of concerns:
- **Controller Layer**: Handles incoming requests and maps them to services.
- **Service Layer**: Contains business logic and orchestrates operations.
- **Repository Layer**: Interacts with the database using JPA/Hibernate.
- **Frontend**: React components communicate with backend APIs.

![Architecture Diagram](src/main/resources/diagram.uml)  

---

## Functional Requirements
1. **User Management**
   - Register, login, and manage account.
   - Admins can manage all users.
2. **Flight Management**
   - Search and view available flights.
   - Admins can add, update, delete flights.
3. **Booking Management**
   - Book flights, cancel bookings, and calculate total price.
   - View all user bookings.
4. **Stopover Management**
   - Support flights with multiple stopovers.
5. **Favorite Flights**
   - Users can add/remove flights from favorites.

---

## Non-Functional Requirements
- **Security**: Data encrypted and passwords hashed.
- **Performance**: Handles 1000+ concurrent users.
- **Scalability**: Supports future expansions (e.g., international flights).
- **Usability**: Responsive and user-friendly interface.

---

## Project Structure

