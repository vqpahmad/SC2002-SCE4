# Internship Placement Management System

## Overview
The Internship Placement Management System is designed to facilitate the management of internship opportunities, applications, and user interactions within a streamlined interface. The system supports various user roles, including students, company representatives, and career center staff, each with specific functionalities.

## Features
- **User Management**: Different user roles with tailored functionalities.
- **Internship Management**: Create, view, and manage internship opportunities.
- **Application Handling**: Students can apply for internships and track their application status.
- **Reporting**: Generate reports based on internships and applications.

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
│       │           │   ├── InternshipManager.java
│       │           │   └── ReportGenerator.java
│       │           ├── models
│       │           │   ├── Application.java
│       │           │   ├── Internship.java
│       │           │   ├── Report.java
│       │           │   ├── User.java
│       │           │   ├── Student.java
│       │           │   ├── CompanyRepresentative.java
│       │           │   ├── CareerCenterStaff.java
│       │           │   └── WithdrawalRequest.java
│       │           ├── enums
│       │           │   ├── ApplicationStatus.java
│       │           │   ├── InternshipStatus.java
│       │           │   ├── RequestStatus.java
│       │           │   └── InternshipLevel.java
│       │           └── ui
│       │               ├── UserMenu.java
│       │               ├── StudentMenu.java
│       │               ├── CompanyRepMenu.java
│       │               └── StaffMenu.java
│       └── resources
│           ├── applications.csv
│           ├── internships.csv
│           └── users.csv
├── pom.xml
└── README.md
```

## Setup Instructions
1. **Clone the Repository**: 
   ```
   git clone <repository-url>
   cd internship-management-system
   ```

2. **Build the Project**: 
   Use Maven to build the project.
   ```
   mvn clean install
   ```

3. **Run the Application**: 
   Execute the main application.
   ```
   mvn exec:java -Dexec.mainClass="com.internshipapp.App"
   ```

## Usage
- Upon running the application, users will be prompted to log in based on their roles.
- Students can view available internships, apply for them, and check their application status.
- Company representatives can create and manage internships and view applications.
- Career center staff can authorize company representatives and manage internship approvals.

## Contributing
Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.