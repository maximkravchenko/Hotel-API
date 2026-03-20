# Hotel API

Hotel API is a RESTful service for working with hotel data.

It provides endpoints for browsing hotels, getting detailed information, searching by filters, creating hotels, adding amenities, and building histograms by hotel fields. The project is built with Spring Boot, Spring Data JPA, Liquibase, and H2 by default.

## What this project does

The API covers the main hotel management flows:

- get all hotels in short format
- get hotel details by id
- search hotels by name, brand, city, country, and amenities
- create a new hotel
- add amenities to an existing hotel
- get grouped statistics by brand, city, country, or amenities

## API Endpoints

All endpoints use the `/property-view` prefix.

- `GET /property-view/hotels` — returns all hotels in short format
- `GET /property-view/hotels/{id}` — returns detailed hotel information
- `GET /property-view/search` — searches hotels by optional filters
- `POST /property-view/hotels` — creates a new hotel
- `POST /property-view/hotels/{id}/amenities` — adds amenities to a hotel
- `GET /property-view/histogram/{param}` — returns grouped counts by `brand`, `city`, `country`, or `amenities`

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Liquibase
- H2 Database
- Maven
- JUnit 5
- Mockito
- Jacoco
- Springdoc OpenAPI / Swagger

## How to Run

The default profile is `h2`.

```bash
mvn spring-boot:run
```
The application runs on port 8092.

## Database Profiles

The project uses profile-based database configuration.

By default, the application starts with the `h2` profile.

### How to switch database
If you want to use another JDBC database, follow these steps:

1. Add the JDBC driver dependency for your database to `pom.xml`.

2. Copy `application-example.properties` and rename it to a new profile file, for example:
    - `application-local.properties`
    - `application-postgres.properties`
    - `application-mysql.properties`

3. Open the new file and replace the datasource settings with values for your database:
    - `spring.datasource.url`
    - `spring.datasource.driver-class-name`
    - `spring.datasource.username`
    - `spring.datasource.password`
    - `spring.jpa.database-platform`

4. In `application.properties`, change the active profile:
   ```properties
   spring.profiles.active=local
   ```
5. Run the application with the new profile.

### Example of a custom profile file:

```text
spring.datasource.url=jdbc:your_database_url
spring.datasource.driver-class-name=your.jdbc.Driver
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=your.hibernate.dialect
```

### Supported databases
Supported databases depend on the JDBC driver and Hibernate dialect you choose.
In this project, the default profile is H2, and the configuration is ready to be switched to another SQL database such as PostgreSQL or MySQL.

### Liquibase
Liquibase is used for schema migration, so the business logic stays unchanged when switching databases.

## Validation and Error Handling

The API validates incoming data for hotel creation and returns structured errors when the request is invalid.

The project includes:
- request validation for required fields
- a global exception handler for consistent error responses
- custom exceptions for not found and invalid request cases

Examples of handled errors:
- `400 Bad Request` for validation failures
- `404 Not Found` when a hotel is not found
- `400 Bad Request` when an invalid histogram parameter is used

This makes the API responses more predictable and easier to work with on the client side.

## Swagger
Swagger UI is available for exploring and testing the API.

You can use it to:
- inspect available endpoints
- check request and response models
- send test requests directly from the browser

Open Swagger UI after starting the application and use it as the main place to try the API before working with it from a client.

## Testing
The project includes:
- controller integration tests
- service unit tests
- mapper tests
- error handling tests

Run tests with:

```bash
mvn test
```
Jacoco coverage is included to track test coverage.

## Architecture

The project follows a layered architecture:

- `controller` — REST endpoints and request handling
- `service` — business logic
- `repository` — database access
- `mapping` — entity/DTO conversion
- `dto` — request and response models
- `exception` — custom exceptions and global error handling

This structure keeps the codebase simple, readable, and easy to extend.
