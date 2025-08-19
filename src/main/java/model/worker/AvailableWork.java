package model.worker;

public class AvailableWork {
    private String householdName;
    private String title;
    private String description;
    private String date;
    private String address;
    private String pincode;

    public AvailableWork(String householdName, String title, String description, String date, String address, String pincode) {
        this.householdName = householdName;
        this.title = title;
        this.description = description;
        this.date = date;
        this.address = address;
        this.pincode = pincode;
    }

    public String getHouseholdName() { return householdName; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getAddress() { return address; }
    public String getPincode() { return pincode; }
}