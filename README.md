# Employee Management Backend

This is the backend application for the Employee Management system. It provides RESTful APIs to manage employee and department records. The application is built using Spring Boot.

Note: Department endpoints is used only for retrieving the departments althoug there are other REST API's of it defined in DepartmentController.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Accessing the H2 Database](#accessing-the-h2-database)

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) (>= 11)
- Maven (>= 3.6.0)

## Installation

Follow these steps to set up and run the application:

1. **Clone the repository:**

   git clone https://github.com/rewatpun10/employeemanagement.git
   cd employeemanagement

2.  **Confgure the database:**
    The application is pre-configured to use the H2 database. 
    You can find the configuration in the application.properties file:

    # Database H2 Configuration
    spring.datasource.url=jdbc:h2:mem:emp
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=
    
    spring.h2.console.enabled=true
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    
    logging.level.root=INFO
    logging.level.org.springframework.boot=INFO
    logging.level.com.example.demo=INFO

3.  **Build the application:**
     mvn clean install

## running-the-application
    mvn spring-boot:run
    The application will run at http://localhost:8080.

## api-endpoints

Employee Endpoints

GET /api/employees - Retrieve all employees

GET /api/employees/{id} - Retrieve an employee by ID

POST /api/employees - Create a new employee

PUT /api/employees/{id} - Update an existing employee

DELETE /api/employees/{id} - Delete an employee

GET /api/employees/search - Search employees by name with pagination

Department Endpoints
Note: Department endpoints is used only for retrieving the departments althoug there are other REST API's of it defined in DepartmentController

GET /api/departments - Retrieve all departments

## accessing-the-h2-database
To access the H2 database console, follow these steps:

Ensure the application is running.

Open your web browser and navigate to http://localhost:8080/h2-console.

Enter the following details to log in:

JDBC URL: jdbc:h2:mem:emp

Username: sa

Password: (leave this field empty)

Click the "Connect" button to access the H2 database console.

**Data Initialization**

The application uses a DataLoader class to populate the database with initial data(employees and departments) when the application starts.
