package model;

public class Household extends User {


    public Household() {
        super();
    }

    public Household(String name, String userName, String email, int age, String gender, String addressIP, String cityIP) {
        super(name,userName,email,age,gender,addressIP,cityIP);
    }

    @Override
    public String toString() {
        return "Household{" + super.toString() + "}";
    }
}
