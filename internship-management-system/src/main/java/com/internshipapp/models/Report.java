package com.internshipapp.models;
import java.util.Date;

public class Report {
    private Date generatedDate;
    private String content;

    public Report(String content) {
        this.generatedDate = new Date();
        this.content = content;
    }

    public Date getGeneratedDate() {
        return generatedDate;
    }

    public String getContent() {
        return content;
    }

    public void printReport() {
        System.out.println("Report Date: " + generatedDate);
        System.out.println("Content: " + content);
    }
}