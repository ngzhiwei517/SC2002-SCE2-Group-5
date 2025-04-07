Project Overview
The Build-To-Order (BTO) Management System is a Java-based application developed as part of the SC/CE/CZ2002 Object-Oriented Design & Programming course at Nanyang Technological University. This system serves as a centralized hub for HDB BTO flat applications, enabling applicants to view and apply for BTO projects, while allowing HDB staff to manage applications and projects.

System Architecture
The application follows the Boundary-Control-Entity (BCE) architectural pattern:

Boundary Classes: Manage user interactions through the command-line interface
Controller Classes: Handle business logic and operations
Entity Classes: Represent system data and enforce business rules


Project Structure:
- src/
  - boundary/
    - ApplicantBoundary.java
    - MainBoundary.java
    - ManagerBoundary.java
    - OfficerBoundary.java
  - controller/
    - ApplicationController.java
    - EnquiryController.java
    - ProjectController.java
    - UserController.java
  - entity/
    - Applicant.java
    - Application.java
    - Enquiry.java
    - Flat.java
    - IEnquiryManageable.java
    - Manager.java
    - Officer.java
    - Project.java
    - Receipt.java
    - User.java
  - utils/
    - Main.java
