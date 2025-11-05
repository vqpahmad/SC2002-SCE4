# SC2002-SCE4

Internship Management System — a simple command-line Java application (located in the `internship-management-system` folder).

## How to run (command line)

1. Open a terminal and change into the project folder:

```bash
cd internship-management-system
```

2. Build the project (optional but recommended):

```bash
mvn clean install
```

3. Run the application using Maven:

```bash
mvn exec:java -Dexec.mainClass="com.internshipapp.App"
```

Requirements: Java (JDK) and Maven must be installed and available on your PATH.

Optional: To run from VS Code using the Run button, create a `.vscode/launch.json` with a Java launch configuration that points to `com.internshipapp.App` as the `mainClass`.
