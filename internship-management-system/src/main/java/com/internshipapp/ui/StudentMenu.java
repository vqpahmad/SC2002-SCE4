package com.internshipapp.ui;

import com.internshipapp.controllers.ApplicationManager;
import com.internshipapp.models.Application;
import com.internshipapp.models.Student;
import com.internshipapp.controllers.InternshipManager;
import com.internshipapp.models.Internship;
import com.internshipapp.models.WithdrawalRequest;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StudentMenu implements UserMenu {
    private Student student;
    private InternshipManager internshipManager;
    private ApplicationManager applicationManager;

    public StudentMenu(Student student, InternshipManager internshipManager, ApplicationManager applicationManager) {
        this.student = student;
        this.internshipManager = internshipManager;
        this.applicationManager = applicationManager;
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. View Available Internships");
            System.out.println("2. Apply for Internship");
            System.out.println("3. View My Applications");
            System.out.println("4. Accept Placement");
            System.out.println("5. Request Withdrawal");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewAvailableInternships();
                    break;
                case 2:
                    applyForInternship(scanner);
                    break;
                case 3:
                    viewApplicationStatus();
                    break;
                case 4:
                    acceptPlacement(scanner);
                    break;
                case 5:
                    requestWithdrawal(scanner);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void viewAvailableInternships() {
        List<Internship> internships = internshipManager.viewAvailableInternships(student);
        if (internships.isEmpty()) {
            System.out.println("No available internships at the moment.");
        } else {
            System.out.println("--- Available Internships ---");
            for (Internship internship : internships) {
                System.out.println("ID: " + internship.getInternshipID() + " | Title: " + internship.getTitle() + " | Company: " + internship.getCompanyName());
            }
        }
    }

    private void applyForInternship(Scanner scanner) {
        viewAvailableInternships();
        System.out.print("Enter the ID of the internship you want to apply for: ");
        String internshipId = scanner.nextLine();
        Internship internship = internshipManager.findInternshipById(internshipId);

        if (internship != null) {
            Application newApp = applicationManager.applyForInternship(student, internship);
            if (newApp != null) {
                System.out.println("Successfully applied for " + internship.getTitle() + ". Application ID: " + newApp.getApplicationID());
            }
        } else {
            System.out.println("Invalid internship ID.");
        }
    }

    private void viewApplicationStatus() {
        List<Application> applications = student.getApplications();
        if (applications.isEmpty()) {
            System.out.println("You have not made any applications yet.");
        } else {
            System.out.println("--- Your Applications ---");
            for (Application app : applications) {
                System.out.println("App ID: " + app.getApplicationID() + " | Internship: " + app.getInternship().getTitle() + " | Status: " + app.getStatus());
            }
        }
    }

    private void acceptPlacement(Scanner scanner) {
        List<Application> offeredPlacements = student.getApplications().stream()
                .filter(app -> app.getStatus() == com.internshipapp.enums.ApplicationStatus.APPROVED)
                .collect(Collectors.toList());

        if (offeredPlacements.isEmpty()) {
            System.out.println("You have no pending internship offers to accept.");
            return;
        }

        System.out.println("--- Your Internship Offers ---");
        offeredPlacements.forEach(app -> System.out.println("App ID: " + app.getApplicationID() + " | Internship: " + app.getInternship().getTitle()));
        System.out.print("Enter the Application ID of the placement you wish to accept: ");
        String appId = scanner.nextLine();

        Application toAccept = offeredPlacements.stream().filter(app -> app.getApplicationID().equals(appId)).findFirst().orElse(null);
        applicationManager.acceptPlacement(toAccept);
    }

    private void requestWithdrawal(Scanner scanner) {
        viewApplicationStatus();
        System.out.print("Enter the Application ID you wish to withdraw from: ");
        String appId = scanner.nextLine();
        Application toWithdraw = student.getApplications().stream().filter(app -> app.getApplicationID().equals(appId)).findFirst().orElse(null);

        if (toWithdraw != null) {
            WithdrawalRequest request = applicationManager.requestWithdrawal(toWithdraw);
            if (request != null) {
                System.out.println("Withdrawal request submitted successfully. Request ID: " + request.getRequestID());
            } else {
                System.out.println("Failed to submit withdrawal request.");
            }
        } else {
            System.out.println("Invalid Application ID.");
        }
    }
}