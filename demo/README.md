# Project Title: User Management System

## Description
This project is a simple User Management System built using Spring Boot. It allows users to be created and fetched from a database. The application provides a web interface for displaying user data and supports basic CRUD operations.

## Project Structure
```
demo
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── demo
│   │   │               ├── controllers
│   │   │               │   └── UserController.java
│   │   │               ├── models
│   │   │               │   └── User.java
│   │   │               ├── repositories
│   │   │               │   └── UserRepository.java
│   │   │               └── services
│   │   │                   └── UserService.java
│   │   └── resources
│   │       ├── static
│   │       │   ├── css
│   │       │   │   └── styles.css
│   │       │   └── js
│   │       │       └── scripts.js
│   │       └── templates
│   │           └── User-list.html
├── pom.xml
└── README.md
```

## Setup Instructions
1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd demo
   ```

2. **Build the project**:
   Ensure you have Maven installed, then run:
   ```
   mvn clean install
   ```

3. **Run the application**:
   You can run the application using:
   ```
   mvn spring-boot:run
   ```

4. **Access the application**:
   Open your web browser and navigate to `http://localhost:8080/users` to view the user list.

## Usage
- **Create User**: Send a POST request to `/users` with a JSON body containing user details (name, email, etc.) to create a new user.
- **View Users**: Access the `/users` endpoint to see a list of all users.

## Technologies Used
- Spring Boot
- Thymeleaf
- Spring Data JPA
- Maven

## License
This project is licensed under the MIT License.