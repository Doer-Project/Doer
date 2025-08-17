package model;

import java.time.LocalDate;

public class FutureWork {

    private int taskId;
    private String title;
    private int householdId;
    private int workerId;
    private LocalDate date;
    private String status;
    private int rating;
    private String address;

    // Constructor

    ///  for household
    public FutureWork(int taskId, String title, int workerId, LocalDate date, String status, Integer rating) {
        this.taskId = taskId;
        this.title = title;
        this.workerId = workerId;
        this.date = date;
        this.status = status;
        this.rating = rating;
    }

    ///  for Worker
    public FutureWork(int taskId, int householdId, String title, String address, LocalDate date, String status, Integer rating) {
        this.taskId = taskId;
        this.title = title;
        this.address = address;
        this.householdId = householdId;
        this.date = date;
        this.status = status;
        this.rating = rating;
    }

   // getters


    public int getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getHouseholdId() {
        return householdId;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public Integer getRating() {
        return rating;
    }

    // setters


    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHouseholdId(Integer householdId) {
        this.householdId = householdId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /// toString ( just for developers)
    @Override
    public String toString() {
        return "FutureWork{" +
                "workId=" + taskId +
                ", title='" + title + '\'' +
                ", householdId=" + householdId +
                ", workerId=" + workerId +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", rating=" + rating +
                '}';
    }
}
