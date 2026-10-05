# E-commerce Workshop

This is a simple e-commerce backend project built with Spring Boot.

The project was created step by step during the Data JPA, Service Layer, and REST API workshops.

## Technologies

- Java 25
- Spring Boot 4.1.1
- Spring Data JPA
- Spring Web MVC
- Jakarta Validation
- H2 Database
- SpringDoc OpenAPI
- Maven
- JUnit

## Features

The application supports:

- Customer registration and updates
- Customer addresses and profiles
- Product and category management
- Product images and promotions
- Creating customer orders
- Saving the product price when an order is placed
- Request validation
- Global exception handling
- Swagger API documentation

## API Endpoints

### Customers

- `POST /api/v1/customers`
- `GET /api/v1/customers/{id}`
- `PUT /api/v1/customers/{id}`

### Products

- `POST /api/v1/products`
- `GET /api/v1/products`
- `GET /api/v1/products/search?name=...`

### Categories

- `POST /api/v1/categories`
- `GET /api/v1/categories`

### Orders

- `POST /api/v1/orders`

## Running the Project

Run the following command from the project folder:

```powershell
.\mvnw.cmd spring-boot:run
```

The application will start on:

```text
http://localhost:8081
```

## Swagger

Swagger UI can be opened at:

```text
http://localhost:8081/swagger-ui.html
```

## H2 Database

The H2 console is available at:

```text
http://localhost:8081/h2-console
```

Use these connection settings:

```text
JDBC URL: jdbc:h2:mem:ecommerce_db
Username: sa
Password:
```

## Tests

Run all tests with:

```powershell
.\mvnw.cmd test
```

The project includes tests for repositories, mappers, services, controllers, validation, and REST endpoints.
