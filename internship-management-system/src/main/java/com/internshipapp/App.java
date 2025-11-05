package com.internshipapp;

import com.internshipapp.controllers.ApplicationManager;
import com.internshipapp.controllers.InternshipManager;
import com.internshipapp.controllers.UserManager;
import com.internshipapp.models.*;
import com.internshipapp.ui.*;

import java.util.Scanner;

public class App {
    private static InternshipManager internshipManager;
    private static UserManager userManager;
    private static ApplicationManager applicationManager;
    private static UserMenu userMenu;
    private static User currentUser;

    public static void main(String[] args) {
        initialize();
        runApplication();
    }

    private static void initialize() {
        internshipManager = new InternshipManager();
        userManager = new UserManager();
        applicationManager = new ApplicationManager();
        // Load data
        userManager.loadUsers();
        // Example: internshipManager.loadInternships();
    }

    private static void runApplication() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            if (currentUser == null) {
                System.out.println("\nWelcome to the Internship Management System");
                System.out.println("1. Login");
                System.out.println("2. Exit");
                System.out.print("Choose an option: ");

                try {
                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1:
                            System.out.print("Enter User ID: ");
                            String userId = scanner.nextLine();
                            System.out.print("Enter Password: ");
                            String password = scanner.nextLine();
                            currentUser = userManager.login(userId, password);
                            int loginResult = userManager.loginResult(userId, password);

                            if (loginResult == 0) {
                                System.out.println("Invalid User ID. Please try again.");
                            }
                            else if (loginResult == 1){
                                System.out.println("Incorrect password. Please try again.");
                            }
                            break;
                        case 2:
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid option. Please enter 1 or 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            if (currentUser != null) {
                userMenu = getUserMenu(currentUser);
                if (userMenu != null) {
                    userMenu.displayMenu();
                }
                // After the user logs out from their menu, set currentUser to null
                // to show the login screen again.
                currentUser = null;
            }
        }

        System.out.println("Exiting application.");
        scanner.close();
    }

    // Method to get the appropriate user menu based on user type
    private static UserMenu getUserMenu(User user) {
        if (user instanceof Student) {
            return new StudentMenu((Student) user, internshipManager, applicationManager);
        } else if (user instanceof CompanyRepresentative) {
            return new CompanyRepMenu((CompanyRepresentative) user, internshipManager);
        } else if (user instanceof CareerCenterStaff) {
            return new StaffMenu((CareerCenterStaff) user, userManager, internshipManager, applicationManager);
            //return new StaffMenu((CareerCenterStaff) user, userManager, internshipManager, applicationManager);
        }
        return null;
    }
}