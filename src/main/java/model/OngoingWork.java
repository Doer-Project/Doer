package model;

public class OngoingWork {
    private String taskName;
    private String description;
    private String date;
    private String status;

    public OngoingWork(String taskName, String description, String date, String status) {
        this.taskName = taskName;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
