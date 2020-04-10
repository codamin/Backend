package Loghme.models;

import Loghme.exceptions.ForbiddenException;

public class User {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private int credit;
    private OrderRepository orderRepository;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public User() {
        firstName = new String("Ehsan");
        lastName = new String("KhamesPanah");
        phone = new String("09124820194");
        email = new String("ekhamespanah@yahoo.com");
        credit = 10000000;
        orderRepository = new OrderRepository();
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
        if(amount <= 0) {
            throw new ForbiddenException("the entered amount is not acceptable");
        }
        credit += amount;
    }

    public void decreaseCredit(int amount) {
        credit -= amount;
    }
}
