package model.household;

public class FutureWork {
    private String category;
    private String scheduledDate;
    private String time;
    private String description;
    private String status;

    public FutureWork(String category, String scheduledDate, String time, String description, String status) {
        this.category = category;
        this.scheduledDate = scheduledDate;
        this.time = time;
        this.description = description;
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
