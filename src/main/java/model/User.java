package model;

public abstract class User {
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String email;
    protected int age;
    protected String gender;
    private String photoPath;


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
    public String getPhotoPath() {
        return photoPath;
    }



    // setter

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

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
