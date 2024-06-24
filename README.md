# Getting Started

## Logicea Cards Management APIs

Welcome to the Logicea Cards Management API project. This system focuses on efficient card management and is built using cutting-edge technologies such as Java, Spring Boot, and Spring Security with Bearer Token JWT authentication. Password encryption using BCrypt and role-based authorization integrated with Spring Security further enhance its security features.

### Technologies Used

- Java
- Spring Boot 3.0
- Spring Security with Bearer Token JWT authentication
- BCrypt for password encryption
- Role-based authorization using Spring Security
- Maven

### Authentication Users

For simplified testing, the application offers two predefined authentication users:

1. Normal User
   - Username/Password: `member/member123`

2. Admin User
   - Username/Password: `admin/admin123`

**Note:** Before proceeding, create a schema named `logicea_schema` in your MySQL database.

### Getting Started

To begin working with this project, ensure that you have the following dependencies installed on your local machine:

- JDK 19+
- Maven 3+
- MySQL

Follow these steps to build and run the project:

## Setting up the Project

To set up the project on your local machine, follow these steps:

1. Clone the repository using either of the following commands:
   - For HTTPS: `git clone https://github.com/fsdkibe/logicea-card-management.git`
   - For SSH: `git clone git@github.com:fsdkibe/logicea-card-management.git`

2. Navigate to the project directory and execute the following commands:

   - Build the project: `mvn clean install -DskipTests`
     This command will generate a JAR file within the `target` folder.

   - If you're planning to run the application as a standalone JAR file, make sure to copy the `application.properties` file to the same location as the JAR file. You can then run the application using the command: `java -jar jarname.jar &`. The `&` is included to run the application in the background.

   - If you prefer to run the project from an IDE or the command line, use the command: `mvn spring-boot:run`.

3. Once the application is running, it will be accessible at http://localhost:2023.

4. If you're interested in creating a Docker image for your project, make sure you have completed step 1 successfully and that the application is working without issues. You can then build a Docker image using the command: `docker build -t logicea-card-management .`

## Swagger Documentation

To access the Swagger documentation for the application, follow these steps:

1. Ensure that the application is up and running.

2. Access the Swagger documentation by visiting the endpoint URI appended with `/swagger-ui/index.html`. For example: `http://localhost:2023/swagger-ui/index.html`.
