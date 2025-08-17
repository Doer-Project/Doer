package model;

public class Household extends User {
    private String address;
    private String city;
    private int pin_code;

    public Household() {}

    public Household(String role, String firstName, String lastName, String email, String password, int age, String gender, String address, String city, int pin_code) {
        super(role, firstName, lastName, email, password, age, gender);
        this.address = address;
        this.pin_code = pin_code;
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public int getPin_code() {
        return pin_code;
    }

    @Override
    public String toString() {
        return "Household{" +
                "pin_code='" + pin_code + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", userID='" + userID + '\'' +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
