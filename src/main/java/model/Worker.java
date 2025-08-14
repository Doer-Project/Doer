package model;

public class Worker extends User {

    private String category;
    private int experience;
    private String preferredLocation;
    private String passwordHash;


    public Worker() {}

    public Worker(String firstName, String lastName, String userName, String email, int age, String gender, String category, int experience, String passwordHash, String preferredLocation) {
        super(firstName, lastName, userName, email, age, gender);
        this.category = category;
        this.experience = experience;
        this.passwordHash = passwordHash;
        this.preferredLocation = preferredLocation;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    // setter

    public void setCategory(String category) {
        this.category = category;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Worker{" + super.toString() +
                ", category='" + category + '\'' +
                ", experience=" + experience +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
