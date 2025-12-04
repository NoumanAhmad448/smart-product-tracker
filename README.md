# **Smart Product Tracker - Spring Boot Application**

A comprehensive product tracking and management system built with Spring Boot, featuring user authentication, product management, and analytics.

## **🚀 Quick Start**

### **Prerequisites**

- **Java 17 or 21** (LTS versions)
- **Maven 3.8+** or **Gradle 7.x+**
- **PostgreSQL 14+**
- **Git**
- **IDE** (IntelliJ IDEA, VS Code, or Eclipse)

### **1. Clone the Repository**

```bash
git clone https://github.com/noumanahmad448/smart-product-tracker.git
cd smart-product-tracker
```

### **2. Database Setup**

#### **Option A: Using Docker (Recommended)**
```bash
# Start PostgreSQL with Docker
docker run --name smart-tracker-db \
  -e POSTGRES_DB=smart_product_tracker \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:15-alpine

# Or use docker-compose (if docker-compose.yml exists)
docker-compose up -d
```

#### **Option B: Manual PostgreSQL Setup**
```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE smart_product_tracker;

-- Create user (optional)
CREATE USER smart_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE smart_product_tracker TO smart_user;

-- Verify
\l  -- List databases
```

### **3. Configure Application**

Copy the example configuration file and update with your settings:

```bash
# Copy configuration template
cp src/main/resources/application-example.yml src/main/resources/application.yml

# Edit the configuration
nano src/main/resources/application.yml
```

**Update these key settings:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smart_product_tracker
    username: postgres      # Change if using different user
    password: password      # Change to your password
    
  jpa:
    hibernate:
      ddl-auto: update     # Use 'validate' in production
```

### **4. Build the Application**

#### **Using Maven:**
```bash
# Clean and build
./mvnw clean compile

# Run tests
./mvnw test

# Build JAR file
./mvnw clean package

# The JAR will be at: target/smart-product-tracker-0.0.1-SNAPSHOT.jar
```

#### **Using Gradle:**
```bash
# Build and test
./gradlew build

# Run tests
./gradlew test

# Build JAR
./gradlew bootJar
```

### **5. Run the Application**

#### **Option A: Run with Maven/Gradle**
```bash
# Maven
./mvnw spring-boot:run

# Gradle
./gradlew bootRun
```

#### **Option B: Run JAR directly**
```bash
# After building the JAR
java -jar target/smart-product-tracker-0.0.1-SNAPSHOT.jar

# With custom profile
java -jar target/smart-product-tracker-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=dev
```

#### **Option C: Run in Development Mode (Auto-reload)**
```bash
# Install Spring Boot DevTools for hot reload
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### **6. Verify Installation**

1. **Check health endpoint:**
   ```bash
   curl http://localhost:8080/api/health
   # Expected: "Smart Product Tracker is running!"
   ```

2. **Access Swagger UI:**
   Open browser: http://localhost:8080/swagger-ui/index.html

3. **Check Actuator endpoints:**
   - Health: http://localhost:8080/actuator/health
   - Info: http://localhost:8080/actuator/info
   - Metrics: http://localhost:8080/actuator/metrics

## **📁 Project Structure**

```
smart-product-tracker/
├── src/main/java/com/smarttracker/product/
│   ├── config/              # Configuration classes
│   │   ├── SecurityConfig.java
│   │   ├── DatabaseConfig.java
│   │   └── OpenApiConfig.java
│   ├── controller/          # REST Controllers
│   │   ├── AuthController.java
│   │   ├── ProductController.java
│   │   └── HealthController.java
│   ├── service/            # Business logic
│   │   ├── UserService.java
│   │   ├── ProductService.java
│   │   └── impl/           # Implementations
│   ├── repository/         # Data access layer
│   ├── model/             # JPA Entities
│   ├── dto/               # Data Transfer Objects
│   ├── exception/         # Custom exceptions
│   └── validation/        # Custom validators
├── src/main/resources/
│   ├── application.yml     # Main configuration
│   ├── application-dev.yml # Development config
│   ├── application-prod.yml# Production config
│   └── db/
│       └── migration/      # Database migrations
├── src/test/              # Tests
└── pom.xml / build.gradle # Build configuration
```

## **🔧 Configuration Profiles**

### **Development Profile**
```bash
# Run with dev profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Features enabled:
# - H2 in-memory database (optional)
# - Detailed logging
# - Disabled security for easier testing
# - Auto database creation
```

### **Production Profile**
```bash
# Run with production settings
java -jar app.jar --spring.profiles.active=prod

# Features:
# - PostgreSQL database
# - Enhanced security
# - Performance optimized
# - Externalized configuration
```

### **Profile Configuration Files**
- `application.yml` - Base configuration
- `application-dev.yml` - Development overrides
- `application-prod.yml` - Production overrides
- `application-test.yml` - Test configuration

## **🧪 Testing**

### **Run All Tests**
```bash
# Maven
./mvnw test

# Gradle
./gradlew test
```

### **Run Specific Tests**
```bash
# Run a single test class
./mvnw test -Dtest=AuthControllerTest

# Run tests with specific name pattern
./mvnw test -Dtest="*ServiceTest"

# Run with coverage report
./mvnw test jacoco:report
# Report at: target/site/jacoco/index.html
```

### **Integration Tests**
```bash
# Run with test profile (uses test database)
./mvnw test -Dspring.profiles.active=test

# Run with Testcontainers
./mvnw verify
```

## **🌐 API Documentation**

### **Swagger UI**
- **URL:** http://localhost:8080/swagger-ui/index.html
- **Features:**
  - Interactive API documentation
  - Try endpoints directly
  - Model schemas
  - Authentication testing

### **OpenAPI Specification**
- **JSON:** http://localhost:8080/v3/api-docs
- **YAML:** http://localhost:8080/v3/api-docs.yaml

### **Postman Collection**
```bash
# Generate OpenAPI spec
./mvnw springdoc:generate

# Import to Postman using:
# 1. Open Postman
# 2. Import → Link
# 3. Paste: http://localhost:8080/v3/api-docs
```

## **📦 Database Migrations**

### **Initial Setup**
```bash
# Database will be auto-created on first run
# Set in application.yml:
spring:
  jpa:
    hibernate:
      ddl-auto: update  # For development
      
# For production, use Flyway or Liquibase
```

### **Reset Database**
```bash
# Drop and recreate (development only)
DROP DATABASE smart_product_tracker;
CREATE DATABASE smart_product_tracker;

# Or use JPA to recreate
spring.jpa.hibernate.ddl-auto=create-drop
```

## **🔐 Default Users & Authentication**

### **Initial Admin User**
On first run, the system creates a default admin user:
- **Username:** admin
- **Password:** admin123
- **Email:** admin@smarttracker.local

**Change immediately after first login!**

### **Registration Endpoint**
```bash
# Register new user
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "youruser",
    "email": "your@email.com",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!",
    "firstName": "Your",
    "lastName": "Name"
  }'
```

## **🐳 Docker Deployment**

### **Build Docker Image**
```bash
# Build image
docker build -t smart-product-tracker:latest .

# Or using Maven
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=smart-product-tracker

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/smart_product_tracker \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  smart-product-tracker:latest
```

### **Docker Compose (All-in-one)**
```bash
# Start entire stack
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop everything
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

## **🚨 Troubleshooting**

### **Common Issues**

#### **1. Port 8080 already in use**
```bash
# Check what's using port 8080
sudo lsof -i :8080

# Kill process
sudo kill -9 <PID>

# Or run on different port
./mvnw spring-boot:run -Dserver.port=8081
```

#### **2. Database Connection Failed**
```bash
# Check if PostgreSQL is running
sudo systemctl status postgresql

# Start PostgreSQL
sudo systemctl start postgresql

# Test connection
psql -h localhost -U postgres -d smart_product_tracker
```

#### **3. Java Version Issues**
```bash
# Check Java version
java -version

# Should show Java 17 or 21
# If wrong version, set JAVA_HOME
export JAVA_HOME=/path/to/java17
```

#### **4. Build Failures**
```bash
# Clean and rebuild
./mvnw clean compile

# Skip tests
./mvnw clean install -DskipTests

# Update dependencies
./mvnw versions:display-dependency-updates
```

### **Logs & Debugging**
```bash
# View application logs
tail -f logs/application.log

# Enable debug logging
# Add to application.yml:
logging:
  level:
    com.smarttracker: DEBUG
    org.springframework: DEBUG

# Access Actuator logs endpoint
curl http://localhost:8080/actuator/loggers/com.smarttracker
```

## **📈 Monitoring**

### **Spring Boot Actuator Endpoints**
- **Health:** `GET /actuator/health`
- **Info:** `GET /actuator/info`
- **Metrics:** `GET /actuator/metrics`
- **Env:** `GET /actuator/env`
- **Beans:** `GET /actuator/beans`
- **Mappings:** `GET /actuator/mappings`

### **Prometheus Metrics**
```bash
# Enable in application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
  metrics:
    export:
      prometheus:
        enabled: true

# Access metrics
curl http://localhost:8080/actuator/prometheus
```

## **🔧 Development Tips**

### **IDE Setup**
#### **IntelliJ IDEA:**
1. Open as Maven/Gradle project
2. Enable annotation processing (Lombok)
3. Install Spring Boot plugin
4. Configure Run Configuration:
   - Main class: `com.smarttracker.product.SmartProductTrackerApplication`
   - Active profiles: `dev`

#### **VS Code:**
1. Install extensions:
   - Java Extension Pack
   - Spring Boot Extension Pack
   - Lombok Annotations Support
2. Open folder and trust project
3. Use Spring Boot Dashboard

### **Code Quality**
```bash
# Check code style
./mvnw checkstyle:check

# Run PMD for static analysis
./mvnw pmd:check

# SpotBugs for bug patterns
./mvnw spotbugs:check
```

## **🤝 Contributing**

1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### **Commit Message Convention**
```
feat: add user registration endpoint
fix: resolve password validation issue
docs: update API documentation
style: format code according to guidelines
refactor: improve service layer structure
test: add unit tests for auth service
chore: update dependencies
```

## **📄 License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## **📞 Support**

- **Issues:** [GitHub Issues](https://github.com/noumanahmad448/smart-product-tracker/issues)
- **Documentation:** [Wiki](https://github.com/noumanahmad448/smart-product-tracker/wiki)
- **Email:** support@smarttracker.com

---

**Happy Coding! 🚀**

*If you encounter any issues, please check the troubleshooting section or open an issue on GitHub.*