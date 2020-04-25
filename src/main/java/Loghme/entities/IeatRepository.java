package Loghme.entities;

import Loghme.exceptions.ForbiddenException;
import Loghme.exceptions.NotFoundException;
import Loghme.scheduler.HandleFoodPartyPeriodic;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

import static Loghme.Utilities.FetchData.request;

public class IeatRepository {

    private int orderId;
    private static IeatRepository instance;
    private User user;
    private ArrayList<Restaurant> restaurants;
//    private Cart cart;
//    private OrderRepository orderRepository;
    private FoodPartyTimer foodPartyTimer;

    private IeatRepository() {

        user = new User();
        restaurants = new ArrayList<Restaurant>();
//        cart = new Cart();
        orderId = 0;
//        orderRepository = new OrderRepository();
        initDatabase();
//        requestFoodPartyData();
    }

    public FoodPartyTimer getFoodPartyTimer() {
        return foodPartyTimer;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public static IeatRepository getInstance() {
        System.out.println("getInstance called :)");
        if (instance == null)
            instance = new IeatRepository();
        return instance;
    }

    public void initDatabase() {
        String data;
//        try {
//            data = request("http://138.197.181.131:8080/restaurants");
////            addRestaurants(data);
//            setFoodsRestaurant();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("DataBase Initialized");
    }

    public void setFoodsRestaurant() {
        for (Restaurant restaurant : restaurants) {
            restaurant.setRestaurantIds();
            restaurant.setRestaurantNames();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void charge(int amount) {
        user.chargeCredit(amount);
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }


}