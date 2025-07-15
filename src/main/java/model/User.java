package model;

public abstract class User {
    protected String name;
    protected String userName;
    protected String email;
    protected int age;
    protected String gender;
    protected String address;
    protected String city;

    public User() {
    }

    public User(String name, String userName, String email, int age, String gender, String address, String city) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.city = city;
    }

    // Common getters
    public String getName() { return name; }
    public String getUserName() { return userName; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getCity() { return city; }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
