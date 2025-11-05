package com.internshipapp.controllers;

import com.internshipapp.models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserManager {
    private List<User> users;

    public UserManager() {
        this.users = new ArrayList<>();
    }

    public void loadUsers() {
        // In a real app, you would load this from a file or database.
        // For now, we'll hardcode some users for demonstration.
        users.add(new Student("a", "Alice", "p", 3, "CS"));
        users.add(new CompanyRepresentative("b", "Ahmed", "p", "tech", "tech", "Manager"));
        users.add(new CareerCenterStaff("c", "ali", "p", "Career Services"));
    }

    public int loginResult(String userId, String password) {
        for (User user : users) {
            if (user.login(userId, password)){
                return 2;
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
            user.changePassword(newPassword);
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

    public void authorizeRepresentative(CompanyRepresentative rep, boolean authorize) {
        if (authorize) {
            rep.setApproved(true);
        } else {
            //rep.setAuthStatus(AuthorizationStatus.REJECTED);
        }
    }
}