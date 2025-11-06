package com.internshipapp.controllers;

import com.internshipapp.models.*;
import com.internshipapp.enums.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class ApplicationManager {
    private List<Application> applications;
    private List<WithdrawalRequest> withdrawalRequests;
    private final String applicationsCsvFile = "applications.csv";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int lastApplicationNumericId = 0; // To track the last used ID number

    public ApplicationManager() {
        this.applications = new ArrayList<>();
        this.withdrawalRequests = new ArrayList<>();
    }

    public void loadApplications(UserManager userManager, InternshipManager internshipManager) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(applicationsCsvFile)))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                String id = line[0];
                Student student = (Student) userManager.findUserById(line[1]);
                Internship internship = internshipManager.findInternshipById(line[2]);
                ApplicationStatus status = ApplicationStatus.valueOf(line[3]);
                Date appDate = dateFormat.parse(line[4]);

                if (student != null && internship != null) {
                    Application app = new Application(id, student, internship, status);
                    app.setApplicationDate(appDate); // Set persistent Date
                    applications.add(app);
                    
                    // Link the application back to the student and internship
                    student.addApplication(app);
                    internship.addApplication(app);

                    // Track the highest ID number to avoid duplicates
                    try {
                        int numericId = Integer.parseInt(id.substring(3));
                        if (numericId > lastApplicationNumericId) {
                            lastApplicationNumericId = numericId;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore if ID is not in the expected format
                    }
                }
            }
        } catch (IOException | CsvValidationException | ParseException | NullPointerException e) {
            System.out.println("Error loading applications: " + e.getMessage());
        }
    }

    public void saveApplications() {
        try {
            URL resource = getClass().getClassLoader().getResource(applicationsCsvFile);
            if (resource == null) throw new IOException("Cannot find resource file: " + applicationsCsvFile);

            try (CSVWriter writer = new CSVWriter(new FileWriter(Paths.get(resource.toURI()).toFile()))) {
                String[] header = {"applicationID", "studentID", "internshipID", "status", "applicationDate"};
                writer.writeNext(header);
                for (Application app : applications) {
                    writer.writeNext(new String[]{
                        app.getApplicationID(),
                        app.getStudent().getUserID(),
                        app.getInternship().getInternshipID(),
                        app.getStatus().name(),
                        dateFormat.format(app.getApplicationDate())
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving applications: " + e.getMessage());
        }
    }

    /**
     * Creates a new application with a sequential ID, adds it to all relevant lists, and returns it.
     */
    public Application createApplication(Student student, Internship internship) {
        lastApplicationNumericId++; // Increment to get the next ID
        String newId = String.format("APP%03d", lastApplicationNumericId);

        Application newApplication = new Application(newId, student, internship, ApplicationStatus.PENDING);
        
        this.applications.add(newApplication);
        student.addApplication(newApplication);
        internship.addApplication(newApplication);

        return newApplication;
    }

    public Application applyForInternship(Student student, Internship internship) {
        if (student.getApplications().size() >= 3) {
            System.out.println("Error: Cannot apply for more than 3 internships.");
            return null;
        }
        for (Application existingApp : student.getApplications()) {
            if (existingApp.getInternship().getInternshipID().equals(internship.getInternshipID())) {
                System.out.println("Error: You have already applied for this internship.");
                return null;
            }
        }

        Application newApplication = createApplication(student, internship);
        return newApplication;
    }

    public void acceptPlacement(Application application) {
        if (application != null && application.getStatus() == ApplicationStatus.APPROVED && application.getInternship().getSlotsFilled() < application.getInternship().getSlots()) {
            application.setStatus(ApplicationStatus.ACCEPTED);
            application.getInternship().setSlotsFilled(application.getInternship().getSlotsFilled() + 1);
            if (application.getInternship().getSlotsFilled() >= application.getInternship().getSlots()) {
                application.getInternship().setStatus(InternshipStatus.FILLED);
                application.getInternship().setVisible(false);
            }
            // Logic to withdraw other pending applications
            Student student = application.getStudent();
            for (Application otherApp : student.getApplications()) {
                if (otherApp.getStatus() == ApplicationStatus.PENDING) {
                    otherApp.setStatus(ApplicationStatus.UNSUCCESSFUL);
                }
            }
            System.out.println("Placement accepted for internship: " + application.getInternship().getTitle());
        } else {
            System.out.println("Error: This placement cannot be accepted. It may not have been offered to you.");
        }
    }

    public WithdrawalRequest requestWithdrawal(Application application) {
        if (application != null) {
            WithdrawalRequest request = new WithdrawalRequest(application);
            this.withdrawalRequests.add(request);
            return request;
        }
        return null;
    }

    public void addWithdrawalRequest(WithdrawalRequest request) {
        this.withdrawalRequests.add(request);
    }

    public List<WithdrawalRequest> getPendingWithdrawalRequests() {
        return withdrawalRequests.stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public WithdrawalRequest findRequestById(String id) {
        return withdrawalRequests.stream()
                .filter(r -> r.getRequestID().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void processWithdrawalRequest(WithdrawalRequest request, boolean approve) {
        if (approve) {
            request.setStatus(RequestStatus.APPROVED);
            // Also update the original application status
            request.getApplication().setStatus(ApplicationStatus.UNSUCCESSFUL);
            // Optional: Re-open a slot in the internship
            Internship internship = request.getApplication().getInternship();
            if (internship.getSlotsFilled() > 0) {
                internship.setSlotsFilled(internship.getSlotsFilled() - 1);
            }
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }
    }
}