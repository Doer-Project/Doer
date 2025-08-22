package model;

import java.time.LocalDate;

public class FutureWork {

    private int taskId;
    private String title;
    private String householdId;
    private String workerId;
    private String status;
    private String address;
    private LocalDate preferredDate;
    private String startTime;
    private String endTime;
    private String cost;

    // Household POV (show worker + high-level info)
    public FutureWork(int taskId, String title, String workerId,
                      LocalDate preferredDate, String startTime,
                      String endTime, String cost, String status) {
        this.taskId = taskId;
        this.title = title;
        this.workerId = workerId;
        this.preferredDate = preferredDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
        this.status = status;
    }

    // Worker POV (show household + job details)
    public FutureWork(int taskId, String householdId, String title, String address,
                      LocalDate preferredDate, String startTime, String endTime,
                      String cost, String status) {
        this.taskId = taskId;
        this.householdId = householdId;
        this.title = title;
        this.address = address;
        this.preferredDate = preferredDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
        this.status = status;
    }

    //getters
    public int getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getDate() {
        return preferredDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "FutureWork{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", householdId='" + householdId + '\'' +
                ", workerId='" + workerId + '\'' +
                ", status='" + status + '\'' +
                ", address='" + address + '\'' +
                ", preferredDate=" + preferredDate +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}
