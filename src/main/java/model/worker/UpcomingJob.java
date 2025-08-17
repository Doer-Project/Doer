package model.worker;

public class UpcomingJob {
    private String workId;
    private String service;
    private String customer;
    private String scheduledDate;
    private String address;

    // Constructor
    public UpcomingJob(String workId, String service, String customer, String scheduledDate, String address) {
        this.workId = workId;
        this.service = service;
        this.customer = customer;
        this.scheduledDate = scheduledDate;
        this.address = address;
    }

    // Default constructor (needed by frameworks like JavaFX / JDBC)
    public UpcomingJob() {}

    // getters
    public String getWorkId() {
        return workId;
    }

    public String getService() {
        return service;
    }

    public String getCustomer() {
        return customer;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public String getAddress() {
        return address;
    }

    // setters

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
