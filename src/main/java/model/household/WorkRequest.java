package model.household;

import java.time.*;
///  This is class basically have details of request creation by household side.
public class WorkRequest {
    private String title;
    private String  category;
    private String description;
    private LocalDate requestDate;
    private String area;
    private String city;
    private int pinCode;
    private String householdId;

    public WorkRequest(String title, String description, String category, String city, String area, int pinCode, LocalDate requestDate, String householdId) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.city = city;
        this.area = area;
        this.pinCode = pinCode;
        this.requestDate = requestDate;
        this.householdId = householdId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public int getPinCode() {
        return pinCode;
    }

    public String getHouseholdId() {
        return householdId;
    }
}