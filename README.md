# Retail Order Management System

A RESTful backend application built using Java and Spring Boot to manage customers and their orders. The application provides CRUD operations, MySQL database integration, input validation, centralized exception handling, unit testing and interactive API documentation.

## 1. Technology Stack

- Java 21
- Spring Boot 4.1.2
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- JUnit 5
- Mockito
- Springdoc OpenAPI / Swagger UI

## 2. Project Architecture

The application follows a layered architecture:

```text
com.retail.retail_order_management
│
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── repository
├── service
│   └── impl
└── RetailOrderManagementApplication.java
```

### Layer Responsibilities

- **Controller:** Handles incoming REST API requests.
- **Service:** Contains business logic.
- **Repository:** Handles database operations using Spring Data JPA.
- **Entity:** Represents database tables.
- **DTO:** Separates API request and response objects.
- **Exception:** Contains custom exceptions and centralized exception handling.
- **Config:** Contains application configuration.

## 3. Database Configuration

Database: `retail_order_management`

Create the database in MySQL:

```sql
CREATE DATABASE retail_order_management;
```

Configure your MySQL credentials in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/retail_order_management
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Replace `YOUR_PASSWORD` with your local MySQL password.

## 4. REST API Endpoints

### Customer Management

Base URL: `/api/customers`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/customers` | Create customer |
| GET | `/api/customers` | Get all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |

### Order Management

Base URL: `/api/orders`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/orders` | Create order |
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders/customer/{customerId}` | Get orders by customer |
| PUT | `/api/orders/{id}` | Update order |
| DELETE | `/api/orders/{id}` | Delete order |

## 5. Validation and Exception Handling

The application implements:

- Mandatory field validation.
- Email format validation.
- Phone number validation.
- Maximum field length validation.
- Customer not found exception.
- Order not found exception.
- Duplicate email exception.
- Centralized exception handling using `@RestControllerAdvice`.

HTTP status codes include:

- 200 – OK
- 201 – Created
- 204 – No Content
- 400 – Bad Request
- 404 – Not Found
- 409 – Conflict

## 6. Unit Testing

JUnit 5 and Mockito are used to test the service layer independently of the database.

Test coverage includes:

- Customer creation and retrieval.
- Customer updates and deletion.
- Duplicate email handling.
- Customer not found scenarios.
- Order creation and retrieval.
- Order updates and deletion.
- Customer lookup during order creation.
- Order not found scenarios.

Run all tests:

```bash
./mvnw clean test
```

## 7. Swagger API Documentation

Interactive API documentation is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI allows users to explore endpoints, inspect request and response schemas, and execute API requests.

## 8. Running the Application

### Prerequisites

- Java 21 or later
- MySQL Server
- Maven (or use the included Maven wrapper)

### Clone the Repository

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
cd retail-order-management
```

### Run the Application

```bash
./mvnw spring-boot:run
```

The application starts at:

```text
http://localhost:8080
```

### Run Unit Tests

```bash
./mvnw clean test
```

---

## 9. Key Features

- RESTful Customer CRUD APIs.
- RESTful Order CRUD APIs.
- Customer–Order relationship using JPA.
- MySQL database integration.
- DTO-based request and response handling.
- Input validation.
- Centralized exception handling.
- JUnit 5 and Mockito unit tests.
- Interactive Swagger API documentation.