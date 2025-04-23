# 🏗️ BTO Management System (BTO-MS)

## 📚 Table of Contents
- [Project Overview](#project-overview)
- [Approach Overview](#approach-overview)
- [Object-Oriented Concepts Applied](#object-oriented-concepts-applied)
- [SOLID Principles Applied](#solid-principles-applied)
- [General Structure](#general-structure)
- [Assumptions](#assumptions)
- [Core Features](#core-features)
- [Additional Features](#additional-features)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Test Cases](#test-cases)
- [UML Diagrams](#uml-diagrams)
- [License](#license)
- [Disclaimer](#disclaimer)

---

## Project Overview
The BTO Management System (BTO-MS) is designed to streamline the application and management process for Build-To-Order (BTO) projects. It facilitates interactions between Applicants, HDB Officers, and HDB Managers through a command-line interface (CLI). The system is implemented using Java and follows an Object-Oriented Programming (OOP) approach to ensure maintainability, scalability, and testability.

---

##  Approach Overview
We employed a modular design and followed the Model-View-Controller (MVC) architecture. The system is split into distinct packages:

- **Boundary**: Handles user interaction (input/output).
- **Controller**: Contains business logic and coordinates between layers.
- **Entity**: Core domain models (e.g., Applicant, Project, Flat).
- **DAO**: Handles all file I/O via CSV (data access abstraction).
- **Utils**: Helper utilities for file parsing, validation, etc.

This separation ensures a clean structure and promotes single-responsibility design.

---

##  Object-Oriented Concepts Applied
- **Encapsulation**: Each class restricts direct access to data using private attributes and public getters/setters.
- **Inheritance**: Subclasses like `Manager`, `Officer`, and `Applicant` extend from the base `User` class.
- **Abstraction**: Abstract classes and interfaces define high-level roles with implementation hidden.
- **Polymorphism**: Overridden methods like `login()` provide specific behavior per user role.

---

##  SOLID Principles Applied
- **S**ingle Responsibility: Each class handles one specific task.
- **O**pen/Closed: Classes can be extended without modifying existing code.
- **L**iskov Substitution: Subtypes are interchangeable with their parent types.
- **I**nterface Segregation: Specific capabilities are split across interfaces.
- **D**ependency Inversion: Controllers rely on interfaces (e.g., DAOs), not concrete implementations.

---

##  General Structure
btoSystem/  
├── data/                  # CSV files for persistent storage  
│   ├── ApplicantList.csv  
│   ├── ApplicationList.csv  
│   ├── AuditLog.csv  
│   ├── EnquiryList.csv  
│   ├── FlatList.csv  
│   ├── ProjectList.csv  
│   └── ReceiptList.csv  
├── src/  
│   ├── boundary/          # CLI interface classes  
│   ├── controller/        # Application logic and use case coordination  
│   ├── dao/               # Data Access Objects (CSV & abstract interfaces)  
│   └── entity/            # Core data models (Applicant, Project, etc.)

---

##  Assumptions
- All users are pre-registered in CSV files.
- Default password for all users is `"password"`.
- Data in CSV files is well-formed and consistent.
- The system runs using **Java 17**.
- No use of databases, JSON, or XML as per assignment constraints.

---

##  Core Features
- **Role-Based Login** (Applicant, Officer, Manager)
- **View and Filter Projects**
- **Project Application Handling**
- **Flat Booking and Receipt Generation**
- **Enquiry Submission and Reply**
- **Officer and Manager Approval Workflow**
- **Project Creation and Visibility Control**
- **Withdrawal Request and Approval**

---

##  Additional Features
- **DAO Layer Using CSV**  
  Abstracted data access via `DAO` and `CSVDAO` classes for all major entities. This design allows flexible data handling and future extensibility (e.g., database integration).

- **Audit Logging**  
  Logs all critical system events (logins, bookings, approvals) in `AuditLog.csv`.

- **Password Change Functionality**  
  Users can change their passwords after login to secure their account.

- **CSV-Based Persistence**  
  Application state is saved and loaded from `.csv` files for user, project, flat, application, enquiry, and receipt data.

---

##  Installation
To install and run the project:

1. Clone this repository.
2. Ensure Java 17+ is installed on your machine.
3. Open the project in a Java IDE (e.g., IntelliJ, Eclipse).
4. Compile the code using your IDE or terminal.

---

##  Running the Application
To run the application:

1. Navigate to the root project directory.
2. Locate the main class (`Main.java` inside `boundary/` or a designated launcher class).
3. Run the application via your IDE or the terminal.

---

##  Test Cases

###  Login
- **Valid Login:** NRIC + correct password → success  
- **Invalid Login:** Wrong NRIC/password → error message

###  Applicant
- Apply for eligible projects  
- Withdraw an application  
- View application status  
- Submit, edit, delete enquiries  

###  HDB Officer
- Register for a project  
- View project details (regardless of visibility)  
- Assist with booking and generate receipts  
- Respond to project enquiries  

###  HDB Manager
- Create/edit/delete projects  
- Toggle project visibility  
- Approve/reject officer registration  
- Approve/reject BTO applications  
- Generate filtered booking reports  

---

##  UML Diagrams
Both UML Class and Sequence diagrams can be found in the `UML Diagram/` folder of this repository

---

##  License
This project is licensed under the [MIT License](LICENSE).

---

##  Disclaimer
This project is created for academic purposes only for SC2002 at NTU. It is not intended for real-world deployment or production use.
