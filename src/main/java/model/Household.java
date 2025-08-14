package model;

public class Household extends User {
    private String address;
    private String pin_code;
    private String city;
    private String passwordHash;


    public Household() {
        super();
    }

    public Household(String firstName, String lastName, String userName, String email, int age, String gender, String address, String city, String pin_code, String passwordHash) {
        super(firstName, lastName, userName, email, age, gender);
        this.address = address;
        this.pin_code = pin_code;
        this.city = city;
        this.passwordHash = passwordHash;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPin_code() {
        return pin_code;
    }

    // setters

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Household{" + super.toString() +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
