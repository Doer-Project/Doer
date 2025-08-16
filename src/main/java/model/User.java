package model;

public abstract class User {
    protected String userID;
    protected String role;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected int age;
    protected String gender;

    public User() {
    }

    public User(String role, String firstName, String lastName, String email, String password, int age, String gender) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    public String getUserID() {
        return userID;
    }

    public String getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
}
