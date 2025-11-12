package com.internshipapp.controllers;

import com.internshipapp.models.*;
import com.internshipapp.enums.*;
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
import java.util.stream.Stream; // Add this import

/**
 * Controller that manages internships: loading/saving, creation, approval,
 * visibility toggling and basic reporting.
 */
public class InternshipManager {

    private List<Internship> internships;
    private final String internshipsCsvFile = "internships.csv";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int lastInternshipNumericId = 0; // To track the last used ID number

    /**
     * Create an InternshipManager using an existing list instance.
     *
     * @param internships initial list to use
     */
    public InternshipManager(List<Internship> internships) {
        this.internships = internships;
    }

    /**
     * Create an InternshipManager with an empty internship list.
     */
    public InternshipManager() {
        this.internships = new java.util.ArrayList<Internship>();
    }

    /**
     * Load internships from the CSV resource and link to their owners.
     *
     * @param userManager used to resolve owner references
     */
    public void loadInternships(UserManager userManager) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(internshipsCsvFile)))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                String id = line[0];
                String title = line[1];
                String description = line[2];
                InternshipLevel level = InternshipLevel.valueOf(line[3]);
                String preferredMajor = line[4];
                Date openingDate = dateFormat.parse(line[5]);
                Date closingDate = dateFormat.parse(line[6]);
                InternshipStatus status = InternshipStatus.valueOf(line[7]);
                String companyName = line[8];
                CompanyRepresentative owner = userManager.findRepresentativeById(line[9]);
                int slots = Integer.parseInt(line[10]);
                int slotsFilled = Integer.parseInt(line[11]);
                boolean isVisible = Boolean.parseBoolean(line[12]);

                if (owner != null) {
                    Internship internship = new Internship(id, title, description, level, preferredMajor, openingDate, closingDate, status, companyName, owner, slots, slotsFilled, isVisible);
                    internships.add(internship);
                    owner.getCreatedInternships().add(internship); // Link back to owner

                    // Track the highest ID number to avoid duplicates
                    try {
                        int numericId = Integer.parseInt(id.substring(3));
                        if (numericId > lastInternshipNumericId) {
                            lastInternshipNumericId = numericId;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore if ID is not in the expected format
                    }
                }
            }
        } catch (IOException | CsvValidationException | ParseException | NullPointerException e) {
            System.out.println("Error loading internships: " + e.getMessage());
        }
    }

    /**
     * Persist internships to the internships CSV resource.
     */
    public void saveInternships() {
        try {
            URL resource = getClass().getClassLoader().getResource(internshipsCsvFile);
            if (resource == null) throw new IOException("Cannot find resource file: " + internshipsCsvFile);
            
            try (CSVWriter writer = new CSVWriter(new FileWriter(Paths.get(resource.toURI()).toFile()))) {
                String[] header = {"internshipID", "title", "description", "level", "preferedMajor", "openingDate", "closingDate", "status", "companyName", "ownerID", "slots", "slotsFilled", "isVisible"};
                writer.writeNext(header);
                for (Internship i : internships) {
                    writer.writeNext(new String[]{
                        i.getInternshipID(),
                        i.getTitle(),
                        i.getDescription(),
                        i.getLevel().name(),
                        i.getPreferedMajor(),
                        dateFormat.format(i.getOpeningDate()),
                        dateFormat.format(i.getClosingDate()),
                        i.getStatus().name(),
                        i.getCompanyName(),
                        i.getOwner().getUserID(),
                        String.valueOf(i.getSlots()),
                        String.valueOf(i.getSlotsFilled()),
                        String.valueOf(i.isVisible())
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving internships: " + e.getMessage());
        }
    }

    /**
     * Creates a new internship with a sequential ID, adds it to the list, and returns it.
     */
    /**
     * Create a new internship with a generated sequential ID and add it to
     * the managed list.
     *
     * @param title internship title
     * @param description internship description
     * @param level internship level
     * @param preferedMajor preferred major
     * @param openingDate opening date
     * @param closingDate closing date
     * @param owner owner representative who creates the internship
     * @param slots number of available slots (1-10)
     * @return the newly created Internship
     */
    public Internship createInternship(String title, String description, InternshipLevel level, String preferedMajor,
                                       Date openingDate, Date closingDate, CompanyRepresentative owner, int slots) {
        
        lastInternshipNumericId++; // Increment to get the next ID
        String newId = String.format("INT%03d", lastInternshipNumericId);

        Internship newInternship = new Internship(
            newId, title, description, level, preferedMajor,
            openingDate, closingDate, InternshipStatus.PENDING, 
            owner.getCompanyName(), owner, slots, 0, false
        );

        this.internships.add(newInternship);
        owner.getCreatedInternships().add(newInternship);
        
        return newInternship;
    }

    /**
     * Returns internships visible and available to the given student, applying
     * basic major/level filters.
     *
     * @param student the student viewing internships
     * @return list of internships available to the student
     */
    public List<Internship> viewAvailableInternships(Student student) {
        // Students should only see internships that are open and visible
        return internships.stream()
                .filter(i -> i.getStatus() == InternshipStatus.APPROVED && i.isVisible())
                .filter(i -> {
                    // Filter 1: Major must match the student's major.
                    boolean majorMatch = i.getPreferedMajor().equalsIgnoreCase(student.getMajor());

                    // Filter 2: Level must match the student's year of study.
                    boolean levelMatch;
                    if (student.getYearOfStudy() == 1) {
                        // Year 1 students can only see BASIC level internships.
                        levelMatch = (i.getLevel() == InternshipLevel.BASIC);
                    } else {
                        // Students in other years can see all levels.
                        levelMatch = true;
                    }
                    
                    return majorMatch && levelMatch;
                })
                .collect(Collectors.toList());
    }

    /**
     * @return the full list of internships (including pending ones)
     */
    public List<Internship> viewAllInternships() {
        // For staff to see all internships, including pending ones
        return internships;
    }

    /**
     * Approve the given internship and make it visible to students.
     *
     * @param internship internship to approve
     */
    public void approveInternship(Internship internship) {
        internship.setStatus(InternshipStatus.APPROVED);
        internship.setVisible(true);
    }

    /**
     * Reject the given internship and hide it from students.
     *
     * @param internship internship to reject
     */
    public void rejectInternship(Internship internship) {
        // Or you might want to remove it from the list entirely
        internship.setStatus(InternshipStatus.REJECTED);
        internship.setVisible(false);
    }

    /**
     * @param internship internship whose applications should be returned
     * @return list of applications submitted to the internship
     */
    public List<Application> viewApplicationsForInternship(Internship internship) {
        return internship.getApplications();
    }

    /**
     * Approve a specific application, if the internship has available slots.
     *
     * @param application the application to approve
     */
    public void approveApplication(Application application) {
        Internship internship = application.getInternship();
        if (internship.getSlotsFilled() < internship.getSlots()) {
            application.setStatus(ApplicationStatus.APPROVED);
        } else {
            System.out.println("Cannot approve application, all slots are filled.");
        }
    }

    /**
     * Mark an application as unsuccessful.
     *
     * @param application the application to reject
     */
    public void rejectApplication(Application application) {
        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
    }

    /**
     * Toggle the visibility flag on an internship.
     *
     * @param internship the internship to toggle
     */
    public void toggleInternshipVisibility(Internship internship) {
        internship.setVisible(!internship.isVisible());
        System.out.println("Visibility for internship '" + internship.getTitle() + "' is now " + (internship.isVisible() ? "ON" : "OFF"));
    }

    /**
     * Find an internship by ID.
     *
     * @param id internship identifier
     * @return matching Internship or null if not found
     */
    public Internship findInternshipById(String id) {
        return internships.stream()
                .filter(i -> i.getInternshipID().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * @return list of internships currently in PENDING state
     */
    public List<Internship> getPendingInternships() {
        return internships.stream()
                .filter(i -> i.getStatus() == InternshipStatus.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Remove an internship and all associated references (applications, owner link).
     *
     * @param internshipToRemove the internship to remove
     */
    public void removeInternship(Internship internshipToRemove) {
        if (internshipToRemove == null) {
            return;
        }

        // Remove applications associated with this internship from each student
        for (Application app : internshipToRemove.getApplications()) {
            app.getStudent().getApplications().remove(app);
        }

        // Remove from owner's list
        internshipToRemove.getOwner().getCreatedInternships().remove(internshipToRemove);

        // Remove from the main list
        internships.remove(internshipToRemove);
    }

    /**
     * Generate a filtered list of internships for reporting purposes.
     *
     * @param status optional status filter
     * @param major optional major filter
     * @param level optional level filter
     * @return filtered list of internships
     */
    public List<Internship> generateReport(String status, String major, String level) {
        Stream<Internship> stream = internships.stream();

        if (status != null && !status.trim().isEmpty()) {
            stream = stream.filter(i -> i.getStatus().name().equalsIgnoreCase(status));
        }
        if (major != null && !major.trim().isEmpty()) {
            stream = stream.filter(i -> i.getPreferedMajor().equalsIgnoreCase(major));
        }
        if (level != null && !level.trim().isEmpty()) {
            stream = stream.filter(i -> i.getLevel().name().equalsIgnoreCase(level));
        }

        return stream.collect(Collectors.toList());
    }

}