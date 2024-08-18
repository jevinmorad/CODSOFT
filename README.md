# Student Course Registration System

## Overview

The Student Course Registration System is a Java-based application that allows students to sign up, sign in, and manage course registrations. It connects to a Microsoft SQL Server database to store and retrieve information about students and courses.

## Features

- **Student Registration:** Sign up for a new student account.
- **Student Login:** Sign in using your student account.
- **Course Management:**
  - View all available courses.
  - View all registered courses.
  - Register for a new course.
  - Unregister from an existing course.

## Project Structure

```plaintext
src/main/java/com/stu/
├── Main.java              # Main application entry point
├── DatabaseManager.java   # Manages database connections and operations
├── Course.java            # Represents a course with its details
└── Student.java           # Represents a student with their details
```
## Database Setup

- The application uses a Microsoft SQL Server database. To set up the database:
- Create the database and tables using the provided setup.sql script in resources folder.
- Update the DatabaseManager.java file with your database connection details if needed.

## Prerequisites

- Java 22 or higher.
- Maven for managing project dependencies.
- Microsoft SQL Server for the database.

## Dependencies

The project uses the following dependencies, managed by Maven:

- JUnit for testing.
- Microsoft SQL Server JDBC Driver for database connectivity.

These dependencies are specified in the pom.xml file.

# Running the Application

1. Clone Clone the Repository:
```bash
git clone https://github.com/yourusername/student_course_registration.git
cd student_course_registration
```

2. Build the Project:
```bash
mvn clean install
```

3. Run the Application:
```bash
mvn exec:java -Dexec.mainClass="com.stu.Main"
```

4. Interacting with the Application:

- Follow the on-screen instructions to sign up, sign in, and manage courses.

## Troubleshooting
- This specific exception is thrown by the Microsoft SQL Server JDBC driver if there's an issue connecting to the SQL Server database.
```plaintext
com.microsoft.sqlserver.jdbc.SQLServerException: The TCP/IP connection to the host localhost, port 1433 has failed.
```
-> Check for port 1433 is open to connect

- This occurs if the JDBC driver class is not found. It typically happens if the JDBC driver is not added to the classpath.
```plaintext
java.lang.ClassNotFoundException: com.microsoft.sqlserver.jdbc.SQLServerDriver
```
-> Check JDBC driver is added in pom.xml
```plaintext
<dependencies>
  <dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>12.8.0.jre11</version>
  </dependency>
</dependencies>
```
## Contributing

Contributions are welcome! Please fork this repository and submit a pull request for any improvements or bug fixes.
