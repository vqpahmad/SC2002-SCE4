# Internship Placement Management System

## Overview
The Internship Placement Management System is a command-line application designed to facilitate the management of internship opportunities, applications, and user interactions within a streamlined interface. The system supports various user roles, including students, company representatives, and career center staff, each with specific functionalities.

## Features
- **Role-Based Access Control**: Separate menus and functionalities for Students, Company Representatives, and Career Center Staff.
- **User Management**: Secure login, password changes, and staff authorization for new company representatives.
- **Internship Lifecycle**: Company representatives can create internships, which are then approved or rejected by staff.
- **Application Workflow**: Students can view and apply for internships, accept offers, and request withdrawals.
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
│       │           │   ├── ReportGenerator.java
│       │           │   └── UserManager.java
│       │           ├── models
│       │           │   ├── Application.java
│       │           │   ├── CareerCenterStaff.java
│       │           │   ├── CompanyRepresentative.java
│       │           │   ├── Internship.java
│       │           │   ├── LoginResult.java
│       │           │   ├── Report.java
│       │           │   ├── Student.java
│       │           │   ├── User.java
│       │           │   └── WithdrawalRequest.java
│       │           ├── enums
│       │           │   ├── ApplicationStatus.java
│       │           │   ├── AuthorizationStatus.java
│       │           │   ├── InternshipLevel.java
│       │           │   ├── InternshipStatus.java
│       │           │   ├── LoginStatus.java
│       │           │   └── RequestStatus.java
│       │           └── ui
│       │               ├── CompanyRepMenu.java
│       │               ├── StaffMenu.java
│       │               ├── StudentMenu.java
│       │               └── UserMenu.java
│       └── resources
│           ├── applications.csv
│           ├── internships.csv
│           └── users.csv
├── pom.xml
└── README.md
```

## Setup Instructions

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 or higher.
- **Apache Maven**: To build the project and manage dependencies.

### Steps
1. **Clone the Repository**: 
   ```bash
   git clone <repository-url>
   cd internship-management-system
   ```

2. **Build the Project**: 
   Use Maven to compile the code and download dependencies.
   ```bash
   mvn clean install
   ```

3. **Run the Application**: 
   Execute the main application using the Maven exec plugin.
   ```bash
   mvn exec:java -Dexec.mainClass="com.internshipapp.App"
   ```

## Usage
- Upon running the application, users will be prompted to log in with their User ID and password.
- **Students** can view available internships filtered by their profile, apply for them, and manage their applications.
- **Company Representatives** can create and manage internships for their company and process applications.
- **Career Center Staff** can authorize new company representatives, approve internship postings, and process student withdrawal requests.

## Contributing
Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.