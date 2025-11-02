

import java.util.Objects;

/**
 * Abstract base class representing a generic system user.
 * Provides core authentication and password management behavior.
 * 
 * <p>Applies OO principles:
 * - Encapsulation: password is private
 * - Abstraction: base class defines shared behaviors for all user types
 * - Inheritance: extended by Student, CompanyRepresentative, and CareerCenterStaff
 * </p>
 * 
 * @author 
 * @version 1.0
 */
public abstract class User {
    protected String userID;
    protected String name;
    private String password;

    public User(String userID, String name, String password) {
        this.userID = userID;
        this.name = name;
        this.password = password;
    }

    /**
     * Validates login credentials.
     *
     * @param id    entered user ID
     * @param pass  entered password
     * @return true if both match this user's credentials
     */
    public boolean login(String id, String pass) {
        return Objects.equals(this.userID, id) && Objects.equals(this.password, pass);
    }

    /** Logs out the user (placeholder for CLI system). */
    public void logout() {
        System.out.println(name + " has logged out.");
    }

    /**
     * Changes this user's password after basic validation.
     *
     * @param newPass new password string
     */
    public void changePassword(String newPass) {
        if (newPass == null || newPass.isBlank()) {
            System.out.println("❌ Password cannot be empty.");
            return;
        }
        this.password = newPass;
        System.out.println("✅ Password updated successfully for user " + userID);
    }

    // --- Getters ---
    public String getUserID() { return userID; }
    public String getName() { return name; }

    // --- Setters ---
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return String.format("[%s] %s", userID, name);
    }
}
