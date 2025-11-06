package com.internshipapp.controllers;

import com.internshipapp.models.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserManager {
    private List<User> users;
    // File names, not full paths
    private final String studentCsvFile = "students.csv";
    private final String staffCsvFile = "staff.csv";
    private final String companyRepCsvFile = "company_representatives.csv";

    public UserManager() {
        this.users = new ArrayList<>();
    }

    public void loadUsers() {
        users.clear(); // Clear any existing users before loading
        loadStudents();
        loadStaff();
        loadCompanyReps();
    }

    private void loadStudents() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(studentCsvFile)))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                users.add(new Student(line[0], line[1], line[2], Integer.parseInt(line[4]), line[3]));
            }
        } catch (IOException | CsvValidationException | NullPointerException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    private void loadStaff() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(staffCsvFile)))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                users.add(new CareerCenterStaff(line[0], line[1], line[2], line[4]));
            }
        } catch (IOException | CsvValidationException | NullPointerException e) {
            System.out.println("Error loading staff: " + e.getMessage());
        }
    }

    private void loadCompanyReps() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(companyRepCsvFile)))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                CompanyRepresentative rep = new CompanyRepresentative(line[0], line[1], line[2], line[3], line[4], line[5]);
                rep.setApproved(Boolean.parseBoolean(line[6]));
                users.add(rep);
            }
        } catch (IOException | CsvValidationException | NullPointerException e) {
            System.out.println("Error loading company representatives: " + e.getMessage());
        }
    }

    public void saveUsers() {
        saveStudents();
        saveStaff();
        saveCompanyReps();
    }

    private void saveStudents() {
        try {
            URL resource = getClass().getClassLoader().getResource(studentCsvFile);
            if (resource == null) {
                throw new IOException("Cannot find resource file: " + studentCsvFile);
            }
            try (CSVWriter writer = new CSVWriter(new FileWriter(Paths.get(resource.toURI()).toFile()))) {
                writer.writeNext(new String[]{"StudentID", "Name", "Password", "Major", "Year"});
                users.stream()
                    .filter(u -> u instanceof Student)
                    .map(u -> (Student) u)
                    .forEach(s -> writer.writeNext(new String[]{s.getUserID(), s.getName(), s.getPassword(), s.getMajor(), String.valueOf(s.getYearOfStudy())}));
            }
        } catch (Exception e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    private void saveStaff() {
        try {
            URL resource = getClass().getClassLoader().getResource(staffCsvFile);
            if (resource == null) {
                throw new IOException("Cannot find resource file: " + staffCsvFile);
            }
            try (CSVWriter writer = new CSVWriter(new FileWriter(Paths.get(resource.toURI()).toFile()))) {
                writer.writeNext(new String[]{"StaffID", "Name", "Password", "Role", "Department"});
                users.stream()
                    .filter(u -> u instanceof CareerCenterStaff)
                    .map(u -> (CareerCenterStaff) u)
                    .forEach(s -> writer.writeNext(new String[]{s.getUserID(), s.getName(), s.getPassword(), "Career Center Staff", "CCDS"}));
            }
        } catch (Exception e) {
            System.out.println("Error saving staff: " + e.getMessage());
        }
    }

    private void saveCompanyReps() {
        try {
            URL resource = getClass().getClassLoader().getResource(companyRepCsvFile);
            if (resource == null) {
                throw new IOException("Cannot find resource file: " + companyRepCsvFile);
            }
            try (CSVWriter writer = new CSVWriter(new FileWriter(Paths.get(resource.toURI()).toFile()))) {
                writer.writeNext(new String[]{"CompanyRepID", "Name", "Password", "CompanyName", "Department", "Position", "isApproved"});
                users.stream()
                    .filter(u -> u instanceof CompanyRepresentative)
                    .map(u -> (CompanyRepresentative) u)
                    .forEach(r -> writer.writeNext(new String[]{r.getUserID(), r.getName(), r.getPassword(), r.getCompanyName(), r.getDepartment(), r.getPosition(), String.valueOf(r.isApproved())}));
            }
        } catch (Exception e) {
            System.out.println("Error saving company representatives: " + e.getMessage());
        }
    }

    public boolean registerCompanyRepresentative(String userId, String password, String name, String companyName, String department, String position) {
        // Check if user ID already exists
        for (User user : users) {
            if (user.getUserID().equalsIgnoreCase(userId)) {
                return false; // User ID is already taken
            }
        }
        CompanyRepresentative newRep = new CompanyRepresentative(userId, name, password, companyName, department, position);
        users.add(newRep);
        return true;
    }

    // 0 = incorrect ID, 1 = incorrect password, 2 = unapproved rep, 3 = success
    public int loginResult(String userId, String password) {
        for (User user : users) {
            if (user.login(userId, password)){
                if (user instanceof CompanyRepresentative) {
                    CompanyRepresentative rep = (CompanyRepresentative) user;
                    if (!rep.isApproved()) {
                        return 2;
                    }
                    else {
                        return 3;
                    }
                }
                return 3;
            }
            else if (user.getUserID().equals(userId)){
                return 1;
            }
        }
        return 0; // Return 0 if login fails
    }

    public User login(String userId, String password) {
        for (User user : users) {
            if (user.login(userId, password)) {
                return user;
            }
        }
        return null; // Return null if login fails
    }

    public boolean changePassword(User user, String newPassword) {
        if  (user != null) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }

    public List<CompanyRepresentative> getPendingRepresentatives() {
        return users.stream()
                .filter(u -> u instanceof CompanyRepresentative)
                .map(u -> (CompanyRepresentative) u)
                .filter(rep -> rep.isApproved() == false)
                .collect(Collectors.toList());
    }

    public CompanyRepresentative findRepresentativeById(String id) {
        return users.stream()
                .filter(u -> u instanceof CompanyRepresentative && u.getUserID().equals(id))
                .map(u -> (CompanyRepresentative) u)
                .findFirst()
                .orElse(null);
    }
    

    public User findUserById(String id) {
        return users.stream()
                .filter(u -> u.getUserID().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void authorizeRepresentative(CompanyRepresentative rep, boolean authorize) {
        if (authorize) {
            rep.setApproved(true);
        } else {
            // do nothing
        }
    }
}