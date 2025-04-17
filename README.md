# Table of Contents
1. [Project Overview](#project-overview)
2. [Approach Overview](#approach-overview)
3. [Object-Oriented Concepts Applied](#object-oriented-concepts-applied)
4. [SOLID Principles Applied](#solid-principles-applied)
5. [General Structure](#general-structure)
6. [Assumptions](#assumptions)
7. [Core Features](#core-features)
8. [Additional Features](#additional-features)
9. [Installation](#installation)
10. [Running the Application](#running-the-application)
11. [Test Cases](#test-cases)
12. [UML Diagrams](#uml-diagrams)
13. [License](#license)
14. [Disclaimer](#disclaimer)

---

# Project Overview
The **BTO Management System (BTO-MS)** is designed to streamline the application and management process for Build-To-Order (BTO) projects. It facilitates the interactions between **Applicants**, **HDB Officers**, and **Managers** by providing an interface for project applications, status tracking, flat bookings, and enquiry handling. The system is built using **Java** and follows an **Object-Oriented Programming (OOP)** approach to ensure maintainability, scalability, and ease of testing.

# Approach Overview
We employed a **modular design approach** for the **BTO-MS**, dividing the system into distinct packages, each responsible for a specific aspect of the application. These packages include:
- **Boundary**: Handles all user interactions, such as input/output and display of information.
- **Controller**: Contains the business logic, responsible for coordinating the flow of data between the boundary and entity layers.
- **Entity**: Represents the core data models (e.g., Applicant, Project, Officer).
- **Utils**: Contains helper methods used across the system.

By following the **Model-View-Controller (MVC) architecture**, we ensured that the system is well-structured with a clear separation of concerns. The **Controller** layer interacts with the **Entity** and **Boundary** layers, while the **Boundary** layer manages user interaction.

# Object-Oriented Concepts Applied
In the development of the **BTO Management System (BTO-MS)**, we used several key **Object-Oriented Programming (OOP)** concepts to ensure that the system is clean, scalable, and maintainable:

- **Encapsulation**: We bundled the data (attributes) and methods (functions) into classes, and used **getter** and **setter** methods to control access to sensitive information.
- **Inheritance**: For example, the **Manager** class inherits from the **User** class, sharing common attributes and methods but adding its own specialized functionalities.
- **Abstraction**: We created abstract classes such as **User** to hide the implementation details and provide a clear interface for different types of users (Manager, Officer, Applicant).
- **Polymorphism**: Methods like **login** are overridden in the subclasses to provide specific login behaviors depending on the user type.

# SOLID Principles Applied
We applied the five **SOLID** principles to the system design to ensure a clean, modular, and maintainable codebase:

1. **Single Responsibility Principle (SRP)**: 
   - Each class is designed to handle a single responsibility.
2. **Open/Closed Principle (OCP)**: 
   - Classes are open for extension but closed for modification.
3. **Liskov Substitution Principle (LSP)**:
   - Derived classes can be used interchangeably with their base classes without affecting functionality.
4. **Interface Segregation Principle (ISP)**: 
   - The system creates specific interfaces for different roles.
5. **Dependency Inversion Principle (DIP)**: 
   - Higher-level modules do not depend on lower-level modules. Both depend on abstractions.

# General Structure
The system is divided into several key packages:
- **Boundary**: Contains classes like `ApplicantBoundary`, `ManagerBoundary`, `OfficerBoundary`, and `MainBoundary`. These manage user interaction and interface elements.
- **Controller**: Includes classes such as `ApplicationController`, `AuditController`, `ProjectController`, and `UserController` to handle the business logic.
- **Entity**: Contains core classes like `Applicant`, `Manager`, `Officer`, `Project`, `Flat`, and `Enquiry`.
- **Utils**: Includes utility functions to support various operations like file handling.

The project uses **Java 17** for compatibility and efficiency.

# Assumptions
1. All applicant data has already been registered and stored in a **CSV file**.
2. Initial user passwords are set to **"password"**.
3. All data is in the correct format, with **no null values**.
4. The **CSV data structure** follows the expected format and is free from any inconsistencies.
5. **Primary** and **foreign keys** are properly set up in the database, and all dependencies are intact.
6. The application runs using **Java 17**.

# Core Features
- **User Authentication**: Includes login functionality for Applicants, Officers, and Managers with role-based access.
- **Project Management**: Managers can create, edit, and delete BTO projects, as well as toggle project visibility.
- **Application Handling**: Applicants can apply for projects, track their application status, and withdraw applications. 
- **Flat Booking**: Officers handle flat bookings for approved applications and generate receipts.
- **Enquiry Management**: Applicants and Managers can submit, edit, or delete enquiries.
- **Audit Log**: Keeps track of critical system events (like user actions and system changes).

# Additional Features
- **Reset Password**: Users can reset their password if they forget it, ensuring continued access.
- **Audit Log**: Tracks system activities like user actions, changes to data, and login attempts.

# Installation
To run the system, follow these steps:
1. Clone the repository to your local machine.
2. Ensure you have **Java 17** installed.
3. Compile and run the application using your preferred IDE (e.g., IntelliJ IDEA) or command line.

# Running the Application
To run the application:
1. Navigate to the root folder of the project.
2. Open a terminal or IDE and run the `Main.java` file.

# Test Cases
We’ve tested the system for several key functionalities. Below are some of the test cases:
Full test cases can be found in the reports.

## 1. System Login Functionality
- **Test Case 1**: Valid login with correct NRIC and password.
- **Expected Result**: User logs in successfully, and the appropriate menu is displayed.
- **Test Case 2**: Invalid login with wrong NRIC or password.
- **Expected Result**: Error message displayed, access denied.

## 2. Manager Functionality
- **Test Case 1**: Manager creates a new BTO project.
- **Expected Result**: Project created successfully and displayed in the project list.
- **Test Case 2**: Manager processes officer registration requests.
- **Expected Result**: Officer registration status updated correctly.

## 3. Officer Functionality
- **Test Case 1**: Officer registers for a project.
- **Expected Result**: Registration status is **PENDING** until approved by Manager.
- **Test Case 2**: Officer views project details.
- **Expected Result**: Officer can view all project details, regardless of visibility settings.

## 4. Applicant Functionality
- **Test Case 1**: Married Applicant views and applies for a project.
- **Expected Result**: Project status is **PENDING**.
- **Test Case 2**: Single Applicant tries to apply for a 3-Room flat.
- **Expected Result**: System prevents application with an appropriate error message.

# UML Diagrams
You can view the UML Diagrams for the project in this [GitHub repository](https://github.com/ngzhiwei517/SC2002/tree/master/UML%20Diagram).

# License
This project is licensed under the MIT License.

# Disclaimer
This project is for academic purposes only and is not intended for commercial use.
