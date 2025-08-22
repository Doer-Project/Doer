package model;

import java.time.LocalDate;

public class PastTask {
    private int pastId;
    private int taskId;
    private String workerId;
    private String householdId;
    private String title;
    private LocalDate date;
    private int rating;
    private String review;

    ///  household
    public PastTask(String title,int taskId, String workerId, LocalDate date, int rating, String review) {
        this.taskId = taskId;
        this.workerId = workerId;
        this.title = title;
        this.date = date;
        this.rating = rating;
        this.review = review;
    }

    ///  workers
    public PastTask(int taskId, String title, String householdId, LocalDate date, int rating, String review) {
        this.taskId = taskId;
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

    public String getWorkerId() {
        return workerId;
    }

    public String getHouseholdId() {
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

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public void setHouseholdId(String householdId) {
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
