package Loghme.DTOs;

import Loghme.entities.Order;
import Loghme.entities.OrderItem;
import Loghme.entities.Restaurant;

import java.util.ArrayList;

public class OrderDTO {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    private String userId;
    private String restaurantId;
    private RestaurantDTO restaurant;
    private ArrayList<OrderItem> orderItems;
    private String restaurantName;
    private String state;
    private int remMin;
    private int remSec;

    public OrderDTO(Order order) {
        userId = order.getUserId();
        restaurantId = order.getRestaurantId();
        if(order.getRestaurant() == null)
            restaurant = null;
        else
            restaurant = new RestaurantDTO(order.getRestaurant());
        orderItems = order.getOrderItems();
        restaurantName = order.getRestaurantName();
        state = order.getState();
        remMin = order.getRemMin();
        remSec = order.getRemSec();
    }


}
