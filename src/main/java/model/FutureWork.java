package model;

import java.time.LocalDate;

public class FutureWork {

    private int taskId;
    private String title;
    private Integer householdId;  // nullable
    private Integer workerId;     // nullable
    private LocalDate date;
    private String status;
    private Integer rating;       // nullable
    private String address;

    // Constructor for Household view (show worker info only)
    public FutureWork(int taskId, String title, Integer workerId, LocalDate date, String status, Integer rating) {
        this.taskId = taskId;
        this.title = title;
        this.workerId = workerId;
        this.date = date;
        this.status = status;
        this.rating = rating;
    }

    // Constructor for Worker view (show household info + address)
    public FutureWork(int taskId, Integer householdId, String title, String address, LocalDate date, String status, Integer rating) {
        this.taskId = taskId;
        this.title = title;
        this.householdId = householdId;
        this.address = address;
        this.date = date;
        this.status = status;
        this.rating = rating;
    }

    // Full constructor (optional)
    public FutureWork(int taskId, String title, Integer householdId, Integer workerId, String address, LocalDate date, String status, Integer rating) {
        this.taskId = taskId;
        this.title = title;
        this.householdId = householdId;
        this.workerId = workerId;
        this.address = address;
        this.date = date;
        this.status = status;
        this.rating = rating;
    }

    // Getters
    public int getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public Integer getHouseholdId() { return householdId; }
    public Integer getWorkerId() { return workerId; }
    public LocalDate getDate() { return date; }
    public String getStatus() { return status; }
    public Integer getRating() { return rating; }
    public String getAddress() { return address; }

    // Setters
    public void setTaskId(int taskId) { this.taskId = taskId; }
    public void setTitle(String title) { this.title = title; }
    public void setHouseholdId(Integer householdId) { this.householdId = householdId; }
    public void setWorkerId(Integer workerId) { this.workerId = workerId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setStatus(String status) { this.status = status; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setAddress(String address) { this.address = address; }

    // toString for debugging
    @Override
    public String toString() {
        return "FutureWork{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", householdId=" + householdId +
                ", workerId=" + workerId +
                ", address='" + address + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", rating=" + rating +
                '}';
    }
}
