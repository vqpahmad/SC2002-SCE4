package com.internshipapp.models;

import java.util.Date;

/**
 * Simple report container used by staff to present aggregated data.
 */
public class Report {
    private Date generatedDate;
    private String content;

    /**
     * Create a report with the provided textual content. The generated date is set to now.
     *
     * @param content textual content of the report
     */
    public Report(String content) {
        this.generatedDate = new Date();
        this.content = content;
    }

    /**
     * Returns the date/time when this report was generated.
     *
     * @return generation timestamp
     */
    public Date getGeneratedDate() {
        return generatedDate;
    }

    /**
     * Returns the textual content of this report.
     *
     * @return report content
     */
    public String getContent() {
        return content;
    }

    /**
     * Print a human-readable representation of the report to standard out.
     */
    public void printReport() {
        System.out.println("Report Date: " + generatedDate);
        System.out.println("Content: " + content);
    }
}