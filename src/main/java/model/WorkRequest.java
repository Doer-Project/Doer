package model;

import java.time.*;
///  This is class basically have details of request creation by household side.
public class WorkRequest {
    private String title;
    private String description;
    private int categoryId;
    private String city;
    private String area;
    private double budget;
    private LocalDate requestDate;
    private String householdId;

    public WorkRequest(String title, String description, int categoryId, String city, String area, double budget, LocalDate requestDate, String householdId) {
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.city = city;
        this.area = area;
        this.budget = budget;
        this.requestDate = requestDate;
        this.householdId = householdId;
    }

    // Getters

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public double getBudget() {
        return budget;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }


    public String getHouseholdId() {
        return householdId;
    }


    // Setters


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }
}