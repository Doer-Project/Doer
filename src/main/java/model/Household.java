package model;

public class Household extends User {
    private String city;
    private String address;

    public Household() {
        super();
    }

    public Household(String firstName, String lastName, String userName, String email, int age, String gender, String address, String city) {
        super(firstName, lastName, userName, email, age, gender);
        this.address = address;
        this.city = city;
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

    @Override
    public String toString() {
        return "Household{" + super.toString() +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
