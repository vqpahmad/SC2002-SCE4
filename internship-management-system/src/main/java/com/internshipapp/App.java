package com.internshipapp;

import com.internshipapp.controllers.ApplicationManager;
import com.internshipapp.controllers.InternshipManager;
import com.internshipapp.controllers.UserManager;
import com.internshipapp.models.*;
import com.internshipapp.ui.*;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import java.util.Scanner;

/**
 * Entry point for the Internship Management System console application.
 * <p>
 * Responsible for initializing managers, loading data from CSV resources,
 * and routing the user to the appropriate UI menu based on their user type.
 */
public class App {
    private static InternshipManager internshipManager;
    private static UserManager userManager;
    private static ApplicationManager applicationManager;
    private static UserMenu userMenu;
    private static User currentUser;

    /**
     * Application entry point.
     *
     * @param args command line arguments (ignored)
     */
    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        initialize();
        runApplication();
        AnsiConsole.systemUninstall();
    }

    /**
     * Initialize controllers and load data from CSV files into memory.
     */
    private static void initialize() {
        userManager = new UserManager();
        internshipManager = new InternshipManager();
        applicationManager = new ApplicationManager();
        
        System.out.println("Loading data from CSV files...");
        userManager.loadUsers();
        internshipManager.loadInternships(userManager);
        applicationManager.loadApplications(userManager, internshipManager);
        System.out.println(ansi().fg(GREEN).a("Data loaded.").reset());
    }

    /**
     * Main application loop that handles login, registration and delegates
     * to the appropriate user menu until the user exits.
     */
    private static void runApplication() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            if (currentUser == null) {
                System.out.println(ansi().fg(CYAN).a("\nWelcome to the Internship Management System").reset());
                System.out.println("1. Login");
                System.out.println("2. Register as Company Representative");
                System.out.println("3. Exit");
                System.out.print(ansi().fg(YELLOW).a("Choose an option: ").reset());

                try {
                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1:
                            System.out.print(ansi().fg(YELLOW).a("Enter User ID: ").reset());
                            String userId = scanner.nextLine();
                            System.out.print(ansi().fg(YELLOW).a("Enter Password: ").reset());
                            String password = scanner.nextLine();
                            currentUser = userManager.login(userId, password);
                            int loginResult = userManager.loginResult(userId, password);

                            if (loginResult == 0) {
                                System.out.println(ansi().fg(RED).a("Invalid User ID. Please try again.").reset());
                                currentUser = null;
                            } else if (loginResult == 1){
                                System.out.println(ansi().fg(RED).a("Incorrect password. Please try again.").reset());
                                currentUser = null;
                            } else if (loginResult == 2){
                                System.out.println(ansi().fg(RED).a("Your account is not approved yet. Please contact the career center.").reset());
                                currentUser = null;
                            } else {
                                System.out.println(ansi().fg(GREEN).a("Login successful. Welcome, " + currentUser.getName() + "!").reset());
                            }
                            break;
                        case 2:
                            System.out.print(ansi().fg(YELLOW).a("Enter User ID: ").reset());
                            String newUserId = scanner.nextLine();
                            System.out.print(ansi().fg(YELLOW).a("Enter Password: ").reset());
                            String newPassword = scanner.nextLine();
                            System.out.print(ansi().fg(YELLOW).a("Enter Your Name: ").reset());
                            String newName = scanner.nextLine();
                            System.out.print(ansi().fg(YELLOW).a("Enter Company Name: ").reset());
                            String companyName = scanner.nextLine();
                            System.out.print(ansi().fg(YELLOW).a("Enter Department: ").reset());
                            String department = scanner.nextLine();
                            System.out.print(ansi().fg(YELLOW).a("Enter Position: ").reset());
                            String position = scanner.nextLine();
                            
                            if (userManager.registerCompanyRepresentative(newUserId, newPassword, newName, companyName, department, position)) {
                                System.out.println(ansi().fg(GREEN).a("Registration successful. Your account is pending approval from the career center.").reset());
                            } else {
                                System.out.println(ansi().fg(RED).a("Registration failed. User ID might already exist.").reset());
                            }
                            break;
                        case 3:
                            running = false;
                            break;
                        default:
                            System.out.println(ansi().fg(RED).a("Invalid option. Please enter 1, 2, or 3.").reset());
                    }
                } catch (NumberFormatException e) {
                    System.out.println(ansi().fg(RED).a("Invalid input. Please enter a number.").reset());
                }
            }

            if (currentUser != null) {
                userMenu = getUserMenu(currentUser);
                if (userMenu != null) {
                    userMenu.displayMenu();
                }
                
                currentUser = null;
            }
        }

        System.out.println("\nSaving data to CSV files...");
        userManager.saveUsers();
        internshipManager.saveInternships();
        applicationManager.saveApplications();
        System.out.println(ansi().fg(GREEN).a("Exiting application.").reset());
        scanner.close();
    }

    /**
     * Returns the UI menu implementation for the given user instance.
     *
     * @param user the currently logged-in user
     * @return a {@link UserMenu} implementation for the user's type, or null
     *         if no suitable menu exists
     */
    private static UserMenu getUserMenu(User user) {
        if (user instanceof Student) {
            return new StudentMenu((Student) user, internshipManager, applicationManager);
        } else if (user instanceof CompanyRepresentative) {
            return new CompanyRepMenu((CompanyRepresentative) user, internshipManager);
        } else if (user instanceof CareerCenterStaff) {
            return new StaffMenu((CareerCenterStaff) user, userManager, internshipManager, applicationManager);
        }
        return null;
    }
}