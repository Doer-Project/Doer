package model.worker;

public class AvailableWork {
    private String requestId;
    private String householdName;
    private String requestTitle; // renamed from 'title' for clarity
    private String description;
    private String date;
    private String address;
    private String pincode;
    
    public AvailableWork(String requestId, String householdName, String requestTitle, String description, String date, String address, String pincode) {
        this.requestId = requestId;
        this.householdName = householdName;
        this.requestTitle = requestTitle;
        this.description = description;
        this.date = date;
        this.address = address;
        this.pincode = pincode;
    }

    public String getRequestId() { return requestId; }
    public String getHouseholdName() { return householdName; }
    public String getRequestTitle() { return requestTitle; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getAddress() { return address; }
    public String getPincode() { return pincode; }
}