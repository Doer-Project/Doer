package model;

import java.time.LocalDate;

public class PastTask {
    private int pastId;
    private int taskId;
    private int workerId;
    private int householdId;
    private String title;
    private LocalDate date;
    private int rating;
    private String review;

    // getters


    public PastTask(int pastId, int taskId, int workerId, int householdId, String title, LocalDate date, int rating, String review) {
        this.pastId = pastId;
        this.taskId = taskId;
        this.workerId = workerId;
        this.householdId = householdId;
        this.title = title;
        this.date = date;
        this.rating = rating;
        this.review = review;
    }

    public int getPastId() {
        return pastId;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public int getHouseholdId() {
        return householdId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    // setters


    public void setPastId(int pastId) {
        this.pastId = pastId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
