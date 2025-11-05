package com.internshipapp.ui;

import com.internshipapp.models.Application;
import com.internshipapp.models.CompanyRepresentative;
import com.internshipapp.models.Internship;
import com.internshipapp.controllers.InternshipManager;
import com.internshipapp.enums.ApplicationStatus;
import com.internshipapp.enums.InternshipLevel;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CompanyRepMenu implements UserMenu {
    private CompanyRepresentative companyRep;
    private InternshipManager internshipManager;

    public CompanyRepMenu(CompanyRepresentative companyRep, InternshipManager internshipManager) {
        this.companyRep = companyRep;
        this.internshipManager = internshipManager;
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("=== Company Representative Menu ===");
            System.out.println("1. Create Internship");
            System.out.println("2. View Applications");
            System.out.println("3. Approve Application");
            System.out.println("4. Reject Application");
            System.out.println("5. Toggle Internship Visibility");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createInternship(scanner);
                    break;
                case 2:
                    viewApplications(scanner);
                    break;
                case 3:
                    processApplication(scanner, true); // true for approve
                    break;
                case 4:
                    processApplication(scanner, false); // false for reject
                    break;
                case 5:
                    toggleInternshipVisibility(scanner);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private List<Internship> getOwnedInternships() {
        return internshipManager.viewAllInternships().stream()
                .filter(i -> i.getOwner().getUserID().equals(companyRep.getUserID()))
                .collect(Collectors.toList());
    }

    private void viewApplications(Scanner scanner) {
        List<Internship> ownedInternships = getOwnedInternships();
        if (ownedInternships.isEmpty()) {
            System.out.println("You have not created any internships yet.");
            return;
        }

        System.out.println("--- Your Internships ---");
        ownedInternships.forEach(i -> System.out.println("ID: " + i.getInternshipID() + ", Title: " + i.getTitle()));
        System.out.print("Enter Internship ID to view applications: ");
        String internshipId = scanner.nextLine();

        Internship selectedInternship = internshipManager.findInternshipById(internshipId);
        if (selectedInternship != null && selectedInternship.getOwner().getUserID().equals(companyRep.getUserID())) {
            List<Application> applications = internshipManager.viewApplicationsForInternship(selectedInternship);
            if (applications.isEmpty()) {
                System.out.println("No applications for this internship yet.");
            } else {
                System.out.println("--- Applications for " + selectedInternship.getTitle() + " ---");
                applications.forEach(app -> System.out.println("App ID: " + app.getApplicationID() + ", Student: " + app.getStudent().getName() + ", Status: " + app.getStatus()));
            }
        } else {
            System.out.println("Invalid Internship ID or you are not the owner.");
        }
    }

    private void processApplication(Scanner scanner, boolean approve) {
        System.out.print("Enter the Application ID to " + (approve ? "approve" : "reject") + ": ");
        String appId = scanner.nextLine();

        // Find the application across all owned internships
        Application applicationToProcess = null;
        for (Internship internship : getOwnedInternships()) {
            applicationToProcess = internship.getApplications().stream()
                    .filter(app -> app.getApplicationID().equals(appId) && app.getStatus() == ApplicationStatus.PENDING)
                    .findFirst().orElse(null);
            if (applicationToProcess != null) break;
        }

        if (applicationToProcess != null) {
            if (approve) {
                internshipManager.approveApplication(applicationToProcess);
                System.out.println("Application " + appId + " approved.");
            } else {
                internshipManager.rejectApplication(applicationToProcess);
                System.out.println("Application " + appId + " rejected.");
            }
        } else {
            System.out.println("Could not find a pending application with that ID.");
        }
    }

    private void toggleInternshipVisibility(Scanner scanner) {
        System.out.print("Enter the Internship ID to toggle visibility: ");
        String internshipId = scanner.nextLine();
        Internship internship = internshipManager.findInternshipById(internshipId);

        if (internship != null && internship.getOwner().getUserID().equals(companyRep.getUserID())) {
            internshipManager.toggleInternshipVisibility(internship);
        } else {
            System.out.println("Invalid Internship ID or you are not the owner.");
        }
    }

    private void createInternship(Scanner scanner) {
        System.out.println("--- Create New Internship ---");
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Preferred Major: ");
        String preferredMajor = scanner.nextLine();

        InternshipLevel level = null;
        while (level == null) {
            System.out.print("Enter Level (BASIC, INTERMEDIATE, ADVANCED): ");
            String levelStr = scanner.nextLine().toUpperCase();
            try {
                level = InternshipLevel.valueOf(levelStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid level. Please try again.");
            }
        }

        int slots = 0;
        while (slots <= 0 || slots > 10) {
            System.out.print("Enter Number of Slots (1-10): ");
            slots = scanner.nextInt();
            if (slots <= 0 || slots > 10) {
                System.out.println("Invalid number of slots. Please enter a number between 1 and 10.");
            }
        }
        scanner.nextLine(); // Consume newline

        // For simplicity, dates are set automatically. This could be expanded to ask for user input.
        Date openingDate = new Date();
        Date closingDate = new Date(System.currentTimeMillis() + 1209600000); // 2 weeks from now

        Internship newInternship = internshipManager.createInternship(
                title, description, level, preferredMajor,
                openingDate, closingDate, companyRep.getCompanyName(),
                companyRep, slots
        );

        System.out.println("Internship created successfully with ID: " + newInternship.getInternshipID());
        System.out.println("It is now pending approval from the Career Center.");
    }
}