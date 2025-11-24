AssetTrack: Small Office Inventory System

1. Project Title
AssetTrack: Small Office Inventory System

2. Overview of the Project
AssetTrack is a Command Line Interface (CLI) application developed in Java for managing physical assets (laptops, monitors, furniture), employee assignments, and maintenance history within a small organization. It implements robust Object-Oriented Programming (OOP) principles, adheres to strict Non-Functional Requirements (NFRs) like input validation and modularity, and persists data to a simple file structure for reliability.

3. Features
The system provides three major functional modules:
I.Asset Management (CRUD): Create, View, Update, and Delete physical assets. Tracks asset name, category, serial number, and current status (e.g., Assigned, Unassigned, In Maintenance).
II.Employee Management & Assignment: Create and manage employee records (ID, name, department). Links assets directly to employees (One-to-Many relationship).
III.Maintenance Tracking: Allows users to record specific maintenance events (cost, date, description) and generate a full history report for any given asset.

4. Technologies/Tools Used
Language: Java (Core JDK)
Interface: Command Line Interface (CLI)
Persistence: File I/O (simulated data persistence in a .txt file).
Concepts: Object-Oriented Programming (OOP), Custom Input Validation, Layered Architecture.

5. Steps to Install & Run the Project
This is a single-file Java application.
Prerequisites: Ensure you have the Java Development Kit (JDK) installed (Java 8 or later).
Save the Code: Save the provided AssetTrack.java code into a file named AssetTrack.java.
Compile: Open your terminal or command prompt and navigate to the directory where you saved the file.
'javac AssetTrack.java'
Run: Execute the compiled class file.
'java AssetTrack'
Data Folder: The application will attempt to create a data folder and an inventory.txt file within the current directory if they do not exist.

6. Instructions for Testing
Upon running, you will be presented with the main menu. Follow these steps to test the core features:
Test Creation:
Select 1) Manage Assets → 1) Add New Asset. Create a Laptop (LAP-001).
Select 2) Manage Employees → 1) Add New Employee. Create an Employee (EMP-101).
Test Assignment (Integration):
Select 1) Manage Assets → 3) Update Asset Status → 1) Assign to Employee. Assign LAP-001 to EMP-101.
Test Maintenance:
Select 3) Maintenance Tracking → 1) Record Maintenance. Record an event for LAP-001.
Test Reporting (NFR 2):
Select 3) Maintenance Tracking → 2) View Maintenance History. Check the history for LAP-001.
Test Validation (NFR 1 - Negative Test):
Attempt to assign an asset to a non-existent employee ID (e.g., EMP-999). The system should report an error and not crash.
Attempt to use the Delete function (Admin-only access).

