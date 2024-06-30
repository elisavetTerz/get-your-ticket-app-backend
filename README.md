# Get Your Ticket App

## Description
The **Get Your Ticket App** is a web application designed to manage ticket sales for events. The application provides functionalities for user registration, login, and event management. It is built using Spring Boot and follows a RESTful architecture.

## Features
- User Registration
- User Login
- Event Creation
- Event Management
- Ticket Booking

## Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- MySQL
- Swagger for API Documentation

## Prerequisites
Before you begin, ensure you have met the following requirements:
- Java 17 installed
- Maven installed
- Git installed
- MySQL installed and running

## Installation
Follow these steps to install the project from GitHub:

1. **Clone the repository**:
    ```sh
    git clone https://github.com/your-username/get-your-ticket-app.git
    cd get-your-ticket-app
    ```

2. **Configure the database**:
    - Create a MySQL database for the project.
    - Update the `application.properties` file in the `src/main/resources` directory with your MySQL credentials and database details:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
      spring.datasource.username=your_mysql_username
      spring.datasource.password=your_mysql_password

      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.show-sql=true
      spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
      ```

3. **Build the project**:
    ```sh
    mvn clean install
    ```

4. **Run the application**:
    ```sh
    mvn spring-boot:run
    ```

    Alternatively, you can run the generated JAR file:
    ```sh
    java -jar target/get-your-ticket-app-0.0.1-SNAPSHOT.jar
    ```

## API Documentation
Swagger is used to generate API documentation. Once the application is running, you can access the API documentation at:
