

import java.util.*;

/**
 * Represents a Student user.
 */
public class Student extends User {
    private int yearOfStudy;
    private String major;
    private List<Application> applications = new ArrayList<>();

    public Student(String userID, String name, String password, int yearOfStudy, String major) {
        super(userID, name, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
    }

    // --- UML method 1 ---
    public List<Internship> viewAvailableInternships() {
        // Implementation depends on system data source; placeholder here
        return new ArrayList<>();
    }

    // --- UML method 2 ---
    public boolean applyForInternship(Internship internship) {
        if (applications.size() >= 3) {
            System.out.println("⚠️ Max 3 applications reached.");
            return false;
        }
        Application app = new Application(this, internship);
        applications.add(app);
        return true;
    }

    // --- UML method 3 ---
    public Map<Internship, ApplicationStatus> viewApplicationStatus() {
        Map<Internship, ApplicationStatus> statusMap = new LinkedHashMap<>();
        for (Application a : applications) {
            statusMap.put(a.getInternship(), a.getStatus());
        }
        return statusMap;
    }

    // --- UML method 4 ---
    public void acceptPlacement(Application application) {
        if (application.getStatus() == ApplicationStatus.SUCCESSFUL) {
            System.out.println("✅ Placement accepted for " + application.getInternship().getTitle());
            for (Application a : applications) {
                if (a != application) a.withdraw();
            }
        } else {
            System.out.println("❌ Only successful applications can be accepted.");
        }
    }

    // --- UML method 5 ---
    public WithdrawalRequest requestWithdrawal(Application application) {
        WithdrawalRequest req = new WithdrawalRequest(this, application);
        System.out.println("📝 Withdrawal requested for " + application.getInternship().getTitle());
        return req;
    }

    // --- Getters/Setters ---
    public int getYearOfStudy() { return yearOfStudy; }
    public String getMajor() { return major; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    public void setMajor(String major) { this.major = major; }
}
