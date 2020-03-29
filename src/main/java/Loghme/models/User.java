package Loghme.models;

public class User {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private int credit;

    public User() {
        firstName = new String("Ehsan");
        lastName = new String("KhamesPanah");
        phone = new String("09124820194");
        email = new String("ekhamespanah@yahoo.com");
        credit = 10000;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getCredit() {
        return credit;
    }

    public void chargeCredit(int amount) {
        credit += amount;
    }

    public void decreaseCredit(int amount) {
        credit -= amount;
    }
}
