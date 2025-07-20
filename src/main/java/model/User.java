package model;

public abstract class User {
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String email;
    protected int age;
    protected String gender;

    public User() {
    }

    public User(String firstName, String lastName, String userName, String email, int age, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    // Common getters
    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract String getUserName();

    public abstract String getEmail();

    public abstract int getAge();

    public abstract String getGender();

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
