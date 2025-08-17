package model.worker;

import model.User;

public class Worker extends User {

    private String category;
    private String workArea;
    private int experience;

    public Worker() {}

    public Worker(String role, String firstName, String lastName, String email, String password, int age, String gender, String category, int experience, String workArea) {
        super(role, firstName, lastName, email, password, age, gender);
        this.category = category;
        this.experience = experience;
        this.workArea = workArea;
    }

    public String getCategory() {
        return category;
    }

    public String getWorkArea() {
        return workArea;
    }

    public int getExperience() {
        return experience;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "category='" + category + '\'' +
                ", workArea='" + workArea + '\'' +
                ", experience=" + experience +
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
