# Smart Team Workspace

Smart Team Workspace is a backend system for collaborative project management built with Spring Boot.
It allows teams to organize projects, manage members, assign tasks, track progress, upload attachments, and communicate through task comments.

The system is designed to support structured teamwork with clear roles and responsibilities.

---

## Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* Maven
* REST API

---

## Core Features

* User registration and management
* Team creation and member management
* Project creation with visibility control (Public / Private)
* Add teams to projects
* Add project members from teams
* Task creation and assignment
* Task status tracking (Pending, In Progress, Completed, Delayed)
* File attachments for tasks
* Comment system for tasks
* Role-based team structure (Owner, Leader, Member)

---

## Project Architecture

The backend follows a layered architecture to separate responsibilities and maintain clean code organization.

Client
↓
Controller Layer (REST APIs)
↓
Service Layer (Business Logic Interfaces)
↓
Service Implementation Layer
↓
Repository Layer (JPA Database Access)
↓
Database

Additional supporting layers:

* **Entity Layer** – Database table mappings
* **Exception Layer** – Global exception handling
* **Response Layer** – Standard API response structure
* **Enums** – Status and role definitions
* **Configuration Layer** – Application configuration (CORS, etc.)

---

## Package Structure

```
com.smartteamworkspace.main
│
├── controller        # REST API controllers
├── service           # Service interfaces
├── serviceimpl       # Service implementations
├── repository        # JPA repositories
├── entity            # Database entities
├── enums             # Role and status enums
├── exception         # Custom exceptions and global handler
├── response          # Standard API response structure
└── configuration     # Application configurations
```

---

## API Modules

* User API
* Team API
* Team Member API
* Project API
* Project Member API
* Task API
* Task Attachment API
* Comment API

---

## Frontend (Planned)

A frontend dashboard will be developed using React to provide a complete user interface for the system.

The dashboard will include:

* Project overview with progress indicators
* Project details with team and member information
* Task management interface
* Task assignment and status tracking
* File attachments and comments
* Personal dashboard showing assigned tasks

---

## Author

Suraj Jadhav
