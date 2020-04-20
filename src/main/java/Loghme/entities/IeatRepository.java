package Loghme.entities;

import Loghme.Utilities.RequestApi;
import Loghme.exceptions.ForbiddenException;
import Loghme.exceptions.NotFoundException;
import Loghme.scheduler.HandleFoodPartyPeriodic;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.IOException;
import java.util.*;

public class IeatRepository {

    private int orderId;
    private static IeatRepository instance;
    private User user;
    private ArrayList<Restaurant> restaurants;
    private Cart cart;
//    private OrderRepository orderRepository;
    private FoodPartyTimer foodPartyTimer;

    private IeatRepository() {

        user = new User();
        restaurants = new ArrayList<Restaurant>();
        cart = new Cart();
        orderId = 0;
//        orderRepository = new OrderRepository();
        initDatabase();
        requestFoodPartyData();
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public static IeatRepository getInstance() {
        System.out.println("getInstance called :)");
        if (instance == null)
            instance = new IeatRepository();
        return instance;
    }

    public void initDatabase() {
        String data;
        try {
            data = RequestApi.request("http://138.197.181.131:8080/restaurants");
            addRestaurants(data);
            setFoodsRestaurant();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private boolean isAddedAlready(Restaurant newRestaurant) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(newRestaurant.getName())) {
                return true;
            }
        }
        return false;
    }

    private Food findfood(String jsonInfo) {
        TypeReference<HashMap<String, String>> typeRef =
                new TypeReference<HashMap<String, String>>() {
                };
        Map<String, String> map = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(jsonInfo, typeRef);
            String foodName = map.get("foodName");
            String restaurantName = map.get("restaurantName");
            for (Restaurant restaurant : restaurants) {
                if (!restaurantName.equals(restaurant.getName()))
                    continue;
                for (Food food : restaurant.getMenu()) {
                    String currentFoodName = food.getName();
                    if (foodName.equals(food.getName()))
                        return food;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> jsonParse(String jsonData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef =
                new TypeReference<HashMap<String, String>>() {
                };
        Map<String, String> map = null;
        try {
            map = mapper.readValue(jsonData, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Restaurant findRestaurant(String name) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equals(name))
                return restaurant;
        }
        return null;
    }

    public ArrayList<Restaurant> findNearRestaurants(int distance) {
        ArrayList<Restaurant> nearRestaurants = new ArrayList<Restaurant>();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getLocation().getDistance(new Location(0, 0)) <= distance)
                nearRestaurants.add(restaurant);
        }
        return nearRestaurants;
    }

    public Restaurant findRestaurantById(String id) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId().equals(id))
                return restaurant;
        }
        return null;
    }

    public Restaurant getRestaurantById(String id) {
        Restaurant restaurant = this.findRestaurantById(id);
        if (restaurant == null)
            throw new NotFoundException("Restaurant Not Found");
        if (restaurant.getLocation().getDistance(new Location(0, 0)) > 170)
            throw new ForbiddenException("The Restaurant Does Not Service In Your Region");
        return restaurant;
    }

    public void addToCart(String id, String foodName, int num) {
        Restaurant restaurant = findRestaurantById(id);
        if (restaurant == null)
            throw new NotFoundException("Restaurant Is Not Valid");

        Food food = restaurant.findFood(foodName);
        if (food == null)
            throw new NotFoundException("Food does not seem valid");

        if (!food.getAvailable()) {
            throw new ForbiddenException("The food have been expired.");
        }
        for(int i=0; i<num; i++) {
            cart.addFood(food);
        }
    }

    public void deleteFromCart(String id, String foodName) {
        Restaurant restaurant = findRestaurantById(id);
        if (restaurant == null)
            throw new NotFoundException("Restaurant not found");

        Food food = restaurant.findFood(foodName);
        if (food == null)
            throw new NotFoundException("Food not found");

        cart.deleteFood(food);
    }

    private void addRestaurants(String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Restaurant> newRestaurants = mapper.readValue(data,
                new TypeReference<List<Restaurant>>() {
                });
        for (Restaurant restaurant : newRestaurants) {
            if (isAddedAlready(restaurant))
                continue;
            restaurants.add(restaurant);
        }
    }

    public void clear() {
        restaurants.clear();
//        orderRepository.clear();
    }

    public void requestFoodPartyData() {
        foodPartyTimer.startTimer();
        TimerTask handleFoodPartyPeriodic = new HandleFoodPartyPeriodic(restaurants);
        Timer requestTimer = new Timer();
        requestTimer.schedule(handleFoodPartyPeriodic, 0, foodPartyTimer.getUpdatePeriod() * 1000); //every 3 secs
    }

    public ArrayList<PartyFood> getParty() {
        ArrayList<PartyFood> ans = new ArrayList<PartyFood>();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getPartyMenu() == null)
                continue;
            for (PartyFood food : restaurant.getPartyMenu()) {
                if(food.getAvailable())
                    ans.add(food);
            }
        }
        return ans;
    }

    public void finalizeCart() {
        if (cart.getOrderItems().isEmpty())
            throw new ForbiddenException("No Order In Cart To Be Finzalized");
        int finalPrice = cart.getFinalPrice();
        if (finalPrice > user.getCredit())
            throw new ForbiddenException("Not Enough Credit To Finalize The Cart");
        user.decreaseCredit(finalPrice);
        Order newOrder = new Order(cart, findRestaurantById(cart.getRestaurantId()));
        user.getOrderRepository().addOrder(newOrder);
        cart = new Cart();
    }
}