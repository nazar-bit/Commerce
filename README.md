# E-Commerce REST API & Web Client

A full-stack e-commerce platform built to demonstrate RESTful API design, role-based access control, and database integration. The backend is powered by Java Spring Boot and PostgreSQL, serving a vanilla HTML/JS frontend client. 

## Live Demo
* URL: https://inquisitive-chebakia-be7525.netlify.app/
* Customer Demo Account: Login: customer | Password: customer
* Maintainer Demo Account: Login: maintainer | Password: maintainer
  
## Tech Stack
* Backend: Java, Spring Boot, Spring Security (JWT), Spring Data JPA, Hibernate.
* Database: PostgreSQL.
* Frontend: HTML5, CSS3, Vanilla JavaScript (Fetch API).
* Deployment: Render (Backend) / Netlify (Frontend) / Neon (Database).

## Key Features
* Role-Based Access Control (RBAC): Distinct permissions for CUSTOMER and MAINTAINER roles using JWT authentication.
* Product & Category Management: Maintainers can CRUD products, manage stock instances, and map complex category hierarchies.
* Order Processing System: Customers can manage a shopping cart, place orders, view order history, and request refunds.
* Inventory Tracking: Real-time stock decrementation when items are added to a cart and restored upon removal or order cancellation.

## Architecture & Design Patterns
* N-Tier Architecture: Clear separation of Controllers, Services, and Repositories.
* DTO Pattern: Data Transfer Objects used to prevent over-posting and hide internal entity structures from API responses.
* Global Exception Handling: @ControllerAdvice used to map backend exceptions to standardized HTTP responses.

## Testing
* JUnit: Automated unit and integration tests verify core business logic, service layer calculations, and database entity mapping. You can run the test suite locally by navigating to the backend directory and executing `./mvnw test`.
* Postman: Endpoint routing, JWT authorization filters, and HTTP status codes were validated using Postman.

## Local Setup Instructions

Prerequisites: Java 17+, Maven/Gradle, PostgreSQL (or Docker).

### Option A: Standard Local Setup
1. Clone the repository:
   git clone https://github.com/yourusername/ecommerce-project.git

2. Database Configuration:
   * Create a PostgreSQL database named "windfarm".
   * Update src/main/resources/application.properties with your local database credentials.

3. Run the Backend:
   cd backend
   ./mvnw spring-boot:run

### Option B: Running via Docker
If you prefer to run the backend in an isolated container, build and run it using the provided Dockerfile. Pass your database credentials as environment variables or link it to a running PostgreSQL container:

1. Build the image:
   docker build -t ecommerce-backend .

2. Run the container (replace with your local DB credentials or Docker network host):
   docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://<YOUR_DB_HOST>:5432/windfarm -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_PASSWORD=password ecommerce-backend

