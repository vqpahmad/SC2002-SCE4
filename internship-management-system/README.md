# Internship Placement Management System

## Overview
The Internship Placement Management System is a command-line application designed to facilitate the management of internship opportunities, applications, and user interactions within a streamlined interface. The system supports various user roles, including students, company representatives, and career center staff, each with specific functionalities.

## Features
- **Role-Based Access Control**: Separate menus and functionalities for Students, Company Representatives, and Career Center Staff.
- **Data Persistence**: Application state (users, internships, applications) is saved to and loaded from CSV files.
- **Colored Command-Line Interface**: Utilizes Jansi for a more readable and user-friendly console experience.
- **User Management**: Secure login, password changes, and staff authorization for new company representatives.
- **Internship Lifecycle**: Company representatives can create internships, which are then approved or rejected by staff.
- **Application Workflow**: Students can view and apply for internships, accept offers, and request withdrawals. Application logic prevents duplicate applications and enforces a maximum of 3 pending applications.
- **Advanced Reporting**: Staff can generate filtered reports on internships based on status, major, or level, and save them to a text file.
- **Data-Driven Filtering**: Internships are filtered for students based on their major and year of study.

## Project Structure
```
internship-management-system
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── internshipapp
│       │           ├── App.java
│       │           ├── controllers
│       │           │   ├── ApplicationManager.java
│       │           │   ├── InternshipManager.java
│       │           │   └── UserManager.java
│       │           ├── models
│       │           │   ├── Application.java
│       │           │   ├── CareerCenterStaff.java
│       │           │   ├── CompanyRepresentative.java
│       │           │   ├── Internship.java
│       │           │   ├── Report.java
│       │           │   ├── Student.java
│       │           │   ├── User.java
│       │           │   └── WithdrawalRequest.java
│       │           ├── enums
│       │           │   ├── ApplicationStatus.java
│       │           │   ├── InternshipLevel.java
│       │           │   ├── InternshipStatus.java
│       │           │   └── RequestStatus.java
│       │           └── ui
│       │               ├── CompanyRepMenu.java
│       │               ├── StaffMenu.java
│       │               ├── StudentMenu.java
│       │               └── UserMenu.java
│       └── resources
│           ├── applications.csv
│           ├── company_representatives.csv
│           ├── internships.csv
│           ├── staff.csv
│           └── students.csv
├── pom.xml
└── README.md
```

## Setup Instructions

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 or higher.
- **Apache Maven**: To build the project and manage dependencies.
- **VS Code Extension Pack for Java** (Recommended for VS Code users).

### Steps
1. **Clone the Repository**: 
   ```bash
   git clone <repository-url>
   cd SC2002-SCE4
   ```

2. **Build the Project**: 
   Use Maven to compile the code and download dependencies. This command should be run from the `internship-management-system` directory.
   ```bash
   mvn clean install
   ```

3. **Run the Application**: 
   - **From Command Line**: Execute the main application using the Maven exec plugin from the `internship-management-system` directory.
     ```bash
     mvn exec:java -Dexec.mainClass="com.internshipapp.App"
     ```
   - **From VS Code**: Use the "Run and Debug" panel. You can choose between:
     - `App (Run Only)`: Preserves data in the `target` directory between runs.
     - `App (Clean Build & Run)`: Resets the data to the default state from `src/main/resources` before running.

## Usage
- Upon running the application, users will be prompted to log in with their User ID and password.
- **Students** can view available internships filtered by their profile, apply for them (up to 3 pending), and manage their applications. A student can only accept one internship offer.
- **Company Representatives** can create and manage internships for their company and process applications.
- **Career Center Staff** can authorize new company representatives, approve internship postings, process student withdrawal requests, and generate detailed, filterable reports on internship opportunities.

## Contributing
Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.