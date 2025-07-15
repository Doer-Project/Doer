package model;

import java.lang.annotation.Inherited;

public class Worker extends User {

    private String category;
    private int experience;
    private String role;
    private String passwordHash;

    public Worker() {}

    public Worker(String name, String userName, String email, int age, String gender,
                  String address, String city, String category, int experience, String role, String passwordHash) {
        super(name, userName, email, age, gender, address, city);
        this.category = category;
        this.experience = experience;
        this.role = role;
        this.passwordHash = passwordHash;
    }

    public String getCategory() { return category; }
    public int getExperience() { return experience; }
    public String getRole() { return role; }
    public String getPasswordHash() { return passwordHash; }

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
