package Loghme.models;

import java.util.ArrayList;

public class Order {
    private Integer id = null;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    private Restaurant restaurant;
    private ArrayList<OrderItem> orderItems;
    private String restaurantName;
    private String state;
    private int remMin;
    private int remSec;


    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Order(Cart cart, Restaurant cartRestaurant) {
        id = IeatRepository.getInstance().getOrderId();
        IeatRepository.getInstance().setOrderId(IeatRepository.getInstance().getOrderId() + 1);
        this.orderItems = cart.getOrderItems();
        this.restaurant = cartRestaurant;
        this.restaurantName = cart.getRestaurantName();
        state = "finding delivery";
        remMin = -1;
        remSec = -1;
    }

    public int getRemMin() {
        return remMin;
    }

    public void setRemMin(int remMin) {
        this.remMin = remMin;
    }

    public int getRemSec() {
        return remSec;
    }

    public void setRemSec(int remSec) {
        this.remSec = remSec;
    }

    public int getFinalPrice() {
        int price = 0;

        for(OrderItem orderItem : orderItems)
            price += orderItem.getPrice();

        return price;
    }
}