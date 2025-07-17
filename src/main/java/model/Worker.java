package model;

public class Worker extends User {

    private String category;
    private int experience;
    private String role;
    private String passwordHash;

    public Worker() {}

    public Worker(String firstName, String lastName, String userName, String email, int age, String gender, String category, int experience, String role, String passwordHash) {
        super(firstName, lastName, userName, email, age, gender);
        this.category = category;
        this.experience = experience;
        this.role = role;
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

    public String getCategory() {
        return category;
    }

    public int getExperience() {
        return experience;
    }

    public String getRole() {
        return role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return "Worker{" + super.toString() +
                ", category='" + category + '\'' +
                ", experience=" + experience +
                ", role='" + role + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
