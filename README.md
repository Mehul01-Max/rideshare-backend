# RideShare Backend

A mini Ride Sharing backend built with Spring Boot, MongoDB, JWT Authentication, Input Validation, and Global Exception Handling.

## Features

- **User Registration & Login**: JWT-based authentication for users and drivers
- **Ride Management**: Create, accept, and complete rides
- **Role-based Access**: Separate endpoints for users and drivers
- **Input Validation**: Jakarta validations on DTOs
- **Global Exception Handling**: Consistent error responses

## Tech Stack

- Spring Boot 3.2.0
- MongoDB
- JWT (JSON Web Tokens)
- Spring Security
- Jakarta Validation

## Project Structure

```
src/
 ├── main/
 │    ├── java/
 │    │     └── org/example/rideshare/
 │    │           ├── model/          # Entity classes
 │    │           ├── repository/     # MongoDB repositories
 │    │           ├── service/        # Business logic
 │    │           ├── controller/     # REST endpoints
 │    │           ├── config/         # Security configuration
 │    │           ├── dto/            # Data transfer objects
 │    │           ├── exception/      # Custom exceptions
 │    │           └── util/           # JWT utilities
 │    └── resources/
 │            └── application.properties
```

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user/driver
- `POST /api/auth/login` - Login and get JWT token

### Rides

- `POST /api/v1/rides` - Create a new ride (USER only)
- `GET /api/v1/driver/rides/requests` - View pending rides (DRIVER only)
- `POST /api/v1/driver/rides/{rideId}/accept` - Accept a ride (DRIVER only)
- `POST /api/v1/rides/{rideId}/complete` - Complete a ride (USER/DRIVER)
- `GET /api/v1/user/rides` - Get user's rides (USER only)

## Setup & Run

1. **Prerequisites**:

   - Java 17
   - MongoDB running on localhost:27017

2. **Clone and build**:

   ```bash
   mvn clean install
   ```

3. **Run the application**:

   ```bash
   mvn spring-boot:run
   ```

4. **Test the API**:
   Use the provided CURL commands or import into Postman.

## Sample Requests

### Register User

```bash
curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234","role":"ROLE_USER"}'
```

### Register Driver

```bash
curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"driver1","password":"abcd","role":"ROLE_DRIVER"}'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234"}'
```

### Create Ride

```bash
curl -X POST http://localhost:8080/api/v1/rides \
-H "Authorization: Bearer <token>" \
-H "Content-Type: application/json" \
-d '{"pickupLocation":"Koramangala","dropLocation":"Indiranagar"}'
```

## Error Handling

All errors return a consistent JSON format:

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Pickup is required",
  "timestamp": "2025-01-20T12:00:00Z"
}
```

## Security

- JWT tokens required for protected endpoints
- Passwords encrypted with BCrypt
- Role-based access control (USER/DRIVER)
