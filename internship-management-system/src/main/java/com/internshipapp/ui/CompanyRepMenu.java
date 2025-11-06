package com.internshipapp.ui;

import com.internshipapp.models.Application;
import com.internshipapp.models.CompanyRepresentative;
import com.internshipapp.models.Internship;
import com.internshipapp.controllers.InternshipManager;
import com.internshipapp.enums.ApplicationStatus;
import com.internshipapp.enums.InternshipLevel;
import com.internshipapp.enums.InternshipStatus;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

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
            System.out.println(ansi().fg(BLUE).bold().a("\n=== Company Representative Menu ===").reset());
            System.out.println("1. Create Internship");
            System.out.println("2. View My Internships");
            System.out.println("3. Edit Internship");
            System.out.println("4. Remove Internship");
            System.out.println("5. View Applications");
            System.out.println("6. Approve Application");
            System.out.println("7. Reject Application");
            System.out.println("8. Toggle Internship Visibility");
            System.out.println("9. Change Password");
            System.out.println("0. Logout");
            System.out.print(ansi().fg(YELLOW).a("Enter your choice: ").reset());
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1; // Invalid choice to trigger default case
            }

            switch (choice) {
                case 1:
                    createInternship(scanner);
                    break;
                case 2:
                    viewMyInternships();
                    break;
                case 3:
                    editInternship(scanner);
                    break;
                case 4:
                    removeInternship(scanner);
                    break;
                case 5:
                    viewApplications(scanner);
                    break;
                case 6:
                    processApplication(scanner, true); // true for approve
                    break;
                case 7:
                    processApplication(scanner, false); // false for reject
                    break;
                case 8:
                    toggleInternshipVisibility(scanner);
                    break;
                case 9:
                    changePassword(scanner);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println(ansi().fg(RED).a("Invalid choice. Please try again.").reset());
            }
        } while (choice != 0);
    }

    private void viewMyInternships() {
        List<Internship> ownedInternships = getOwnedInternships();
        if (ownedInternships.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("You have not created any internships yet.").reset());
            return;
        }

        System.out.println(ansi().fg(CYAN).a("--- Your Created Internships ---").reset());
        for (Internship internship : ownedInternships) {
            System.out.println("ID: " + internship.getInternshipID() + " | Title: " + internship.getTitle() + " | Status: " + internship.getStatus() + " | Slots: " + internship.getSlotsFilled() + "/" + internship.getSlots());
        }
    }

    private List<Internship> getOwnedInternships() {
        return internshipManager.viewAllInternships().stream()
                .filter(i -> i.getOwner().getUserID().equals(companyRep.getUserID()))
                .collect(Collectors.toList());
    }

    private void viewApplications(Scanner scanner) {
        List<Internship> ownedInternships = getOwnedInternships();
        if (ownedInternships.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("You have not created any internships yet.").reset());
            return;
        }

        System.out.println(ansi().fg(CYAN).a("--- Your Internships ---").reset());
        ownedInternships.forEach(i -> System.out.println("ID: " + i.getInternshipID() + ", Title: " + i.getTitle()));
        System.out.print(ansi().fg(YELLOW).a("Enter Internship ID to view applications: ").reset());
        String internshipId = scanner.nextLine();

        Internship selectedInternship = internshipManager.findInternshipById(internshipId);
        if (selectedInternship != null && selectedInternship.getOwner().getUserID().equals(companyRep.getUserID())) {
            List<Application> applications = internshipManager.viewApplicationsForInternship(selectedInternship);
            if (applications.isEmpty()) {
                System.out.println(ansi().fg(YELLOW).a("No applications for this internship yet.").reset());
            } else {
                System.out.println(ansi().fg(CYAN).a("--- Applications for " + selectedInternship.getTitle() + " ---").reset());
                applications.forEach(app -> System.out.println("App ID: " + app.getApplicationID() + ", Student: " + app.getStudent().getName() + ", Status: " + app.getStatus()));
            }
        } else {
            System.out.println(ansi().fg(RED).a("Invalid Internship ID or you are not the owner.").reset());
        }
    }

    private void processApplication(Scanner scanner, boolean approve) {
        System.out.print(ansi().fg(YELLOW).a("Enter the Application ID to " + (approve ? "approve" : "reject") + ": ").reset());
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
                System.out.println(ansi().fg(GREEN).a("Application " + appId + " approved.").reset());
            } else {
                internshipManager.rejectApplication(applicationToProcess);
                System.out.println(ansi().fg(GREEN).a("Application " + appId + " rejected.").reset());
            }
        } else {
            System.out.println(ansi().fg(RED).a("Could not find a pending application with that ID.").reset());
        }
    }

    private void toggleInternshipVisibility(Scanner scanner) {
        System.out.print(ansi().fg(YELLOW).a("Enter the Internship ID to toggle visibility: ").reset());
        String internshipId = scanner.nextLine();
        Internship internship = internshipManager.findInternshipById(internshipId);

        if (internship != null && internship.getOwner().getUserID().equals(companyRep.getUserID())) {
            internshipManager.toggleInternshipVisibility(internship);
        } else {
            System.out.println(ansi().fg(RED).a("Invalid Internship ID or you are not the owner.").reset());
        }
    }

    private void createInternship(Scanner scanner) {
        System.out.println(ansi().fg(CYAN).a("--- Create New Internship ---").reset());
        System.out.print(ansi().fg(YELLOW).a("Enter Title: ").reset());
        String title = scanner.nextLine();

        System.out.print(ansi().fg(YELLOW).a("Enter Description: ").reset());
        String description = scanner.nextLine();

        System.out.print(ansi().fg(YELLOW).a("Enter Preferred Major: ").reset());
        String preferredMajor = scanner.nextLine();

        InternshipLevel level = null;
        while (level == null) {
            System.out.print(ansi().fg(YELLOW).a("Enter Level (BASIC, INTERMEDIATE, ADVANCED): ").reset());
            String levelStr = scanner.nextLine().toUpperCase();
            try {
                level = InternshipLevel.valueOf(levelStr);
            } catch (IllegalArgumentException e) {
                System.out.println(ansi().fg(RED).a("Invalid level. Please try again.").reset());
            }
        }

        int slots = 0;
        while (slots <= 0 || slots > 10) {
            System.out.print(ansi().fg(YELLOW).a("Enter Number of Slots (1-10): ").reset());
            slots = scanner.nextInt();
            if (slots <= 0 || slots > 10) {
                System.out.println(ansi().fg(RED).a("Invalid number of slots. Please enter a number between 1 and 10.").reset());
            }
        }
        scanner.nextLine(); // Consume newline

        // For simplicity, dates are set automatically. This could be expanded to ask for user input.
        Date openingDate = new Date();
        Date closingDate = new Date(System.currentTimeMillis() + 1209600000); // 2 weeks from now

        Internship newInternship = internshipManager.createInternship(
                title, description, level, preferredMajor,
                openingDate, closingDate, companyRep, slots
        );

        System.out.println(ansi().fg(GREEN).a("Internship created successfully with ID: " + newInternship.getInternshipID()).reset());
        System.out.println("It is now pending approval from the Career Center.");
    }

    private void changePassword(Scanner scanner) {
        System.out.print(ansi().fg(YELLOW).a("Enter your current password: ").reset());
        String currentPassword = scanner.nextLine();

        if (!companyRep.login(companyRep.getUserID(), currentPassword)) {
            System.out.println(ansi().fg(RED).a("Incorrect current password.").reset());
            return;
        }

        System.out.print(ansi().fg(YELLOW).a("Enter your new password: ").reset());
        String newPassword = scanner.nextLine();
        System.out.print(ansi().fg(YELLOW).a("Confirm your new password: ").reset());
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println(ansi().fg(RED).a("New passwords do not match. Please try again.").reset());
            return;
        }

        companyRep.setPassword(newPassword);
        System.out.println(ansi().fg(GREEN).a("Password changed successfully.").reset());
    }

    private void editInternship(Scanner scanner) {
        viewMyInternships();
        System.out.print(ansi().fg(YELLOW).a("Enter the ID of the internship you want to edit: ").reset());
        String internshipId = scanner.nextLine();
        Internship internship = internshipManager.findInternshipById(internshipId);

        if (internship == null || !internship.getOwner().getUserID().equals(companyRep.getUserID())) {
            System.out.println(ansi().fg(RED).a("Invalid Internship ID or you are not the owner.").reset());
            return;
        }

        if (internship.getStatus() != InternshipStatus.PENDING) {
            System.out.println(ansi().fg(RED).a("This internship cannot be edited as it is already " + internship.getStatus() + ".").reset());
            return;
        }

        System.out.println(ansi().fg(CYAN).a("\nEditing Internship: " + internship.getTitle()).reset());
        System.out.println("Press Enter at any prompt to keep the current value.");

        System.out.print(ansi().fg(YELLOW).a("Enter new Title (Current: " + internship.getTitle() + "): ").reset());
        String newTitle = scanner.nextLine();
        if (!newTitle.isEmpty()) {
            internship.setTitle(newTitle);
        }

        System.out.print(ansi().fg(YELLOW).a("Enter new Description (Current: " + internship.getDescription() + "): ").reset());
        String newDescription = scanner.nextLine();
        if (!newDescription.isEmpty()) {
            internship.setDescription(newDescription);
        }

        System.out.print(ansi().fg(YELLOW).a("Enter new Preferred Major (Current: " + internship.getPreferedMajor() + "): ").reset());
        String newPreferredMajor = scanner.nextLine();
        if (!newPreferredMajor.isEmpty()) {
            internship.setPreferedMajor(newPreferredMajor);
        }

        while (true) {
            System.out.print(ansi().fg(YELLOW).a("Enter new Level (BASIC, INTERMEDIATE, ADVANCED) (Current: " + internship.getLevel() + "): ").reset());
            String levelStr = scanner.nextLine().toUpperCase();
            if (levelStr.isEmpty()) {
                break;
            }
            try {
                internship.setLevel(InternshipLevel.valueOf(levelStr));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(ansi().fg(RED).a("Invalid level. Please try again.").reset());
            }
        }

        while (true) {
            System.out.print(ansi().fg(YELLOW).a("Enter new Number of Slots (1-10) (Current: " + internship.getSlots() + "): ").reset());
            String slotsStr = scanner.nextLine();
            if (slotsStr.isEmpty()) {
                break;
            }
            try {
                int newSlots = Integer.parseInt(slotsStr);
                if (newSlots > 0 && newSlots <= 10) {
                    internship.setSlots(newSlots);
                    break;
                } else {
                    System.out.println(ansi().fg(RED).a("Invalid number of slots. Please enter a number between 1 and 10.").reset());
                }
            } catch (NumberFormatException e) {
                System.out.println(ansi().fg(RED).a("Invalid input. Please enter a number.").reset());
            }
        }

        System.out.println(ansi().fg(GREEN).a("Internship updated successfully.").reset());
    }

    private void removeInternship(Scanner scanner) {
        viewMyInternships();
        System.out.print(ansi().fg(YELLOW).a("Enter the ID of the internship you want to remove: ").reset());
        String internshipId = scanner.nextLine();
        Internship internship = internshipManager.findInternshipById(internshipId);

        if (internship == null || !internship.getOwner().getUserID().equals(companyRep.getUserID())) {
            System.out.println(ansi().fg(RED).a("Invalid Internship ID or you are not the owner.").reset());
            return;
        }

        System.out.print(ansi().fg(RED).bold().a("Are you sure you want to remove '" + internship.getTitle() + "'? This will also remove all its applications. (Y/N): ").reset());
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {
            internshipManager.removeInternship(internship);
            System.out.println(ansi().fg(GREEN).a("Internship removed successfully.").reset());
        } else {
            System.out.println("Removal cancelled.");
        }
    }
}