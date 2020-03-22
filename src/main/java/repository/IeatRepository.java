package repository;

import Utilities.RequestApi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import scheduler.HandleFoodPartyPeriodic;

import java.io.IOException;
import java.util.*;

public class IeatRepository {

    private int orderId;
    private static IeatRepository instance;
    private User user;
    private ArrayList<Restaurant> restaurants;
    private Cart cart;
    private OrderRepository orderRepository;

    private IeatRepository() {
        user = new User();
        restaurants = new ArrayList<Restaurant>();
        cart = new Cart();
        orderId = 0;
        orderRepository = new OrderRepository();
        initDatabase();
        requestFoodPartyData();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
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

    public ArrayList<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public static IeatRepository getInstance() {
        if(instance == null)
            instance = new IeatRepository();
        return instance;
    }

    public void initDatabase() {
        String data;
        try {
            data = RequestApi.request("http://138.197.181.131:8080/restaurants");
            addRestaurants(data);
            setFoodsRestaurant();
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("DataBase Initialized");
    }

    public void setFoodsRestaurant() {
        for(Restaurant restaurant: restaurants) {
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
        for(Restaurant restaurant: restaurants) {
            if(restaurant.getName().equals(newRestaurant.getName())) {
                return true;
            }
        }
        return false;
    }

    private Food findfood(String jsonInfo) {
        TypeReference<HashMap<String, String>> typeRef =
                new TypeReference<HashMap<String, String>>() {};
        Map<String, String> map = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(jsonInfo, typeRef);
            String foodName = map.get("foodName");
            String restaurantName = map.get("restaurantName");
            for (Restaurant restaurant : restaurants) {
                if(!restaurantName.equals(restaurant.getName()))
                    continue;
                for(Food food : restaurant.getMenu()) {
                    String currentFoodName = food.getName();
                    if (foodName.equals(food.getName()))
                        return food;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> jsonParse(String jsonData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef =
                new TypeReference<HashMap<String, String>>() {};
        Map<String, String> map = null;
        try {
            map = mapper.readValue(jsonData, typeRef);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Restaurant findRestaurant(String name) {
        for(Restaurant restaurant: restaurants) {
            if(restaurant.getName().equals(name))
                return restaurant;
        }
        return null;
    }

    public ArrayList<Restaurant> findNearRestaurants(int distance) {
        ArrayList<Restaurant> nearRestaurants = new ArrayList<Restaurant>();
        for(Restaurant restaurant: restaurants) {
            if(restaurant.getLocation().getDistance(new Location(0 ,0)) <= distance)
                nearRestaurants.add(restaurant);
        }
        return nearRestaurants;
    }

    public Restaurant findRestaurantById(String id) {
        for(Restaurant restaurant: restaurants) {
            if(restaurant.getId().equals(id))
                return restaurant;
        }
        return null;
    }

    public boolean addToCart(String id, String foodName) {
        Restaurant restaurant = findRestaurantById(id);
        if(restaurant == null)
            return false;
        System.out.println("restaurant found");
        Food food = restaurant.findFood(foodName);
        if (food == null)
            return false;
        System.out.println("food found");

        if(cart.addFood(food))
            return true;
        else
            return false;
    }

    private void addRestaurants(String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Restaurant> newRestaurants = mapper.readValue(data,
                new TypeReference<List<Restaurant>>(){});
        for(Restaurant restaurant: newRestaurants) {
            if(isAddedAlready(restaurant))
                continue;
            restaurants.add(restaurant);
        }
    }

    public void clear() {
        restaurants.clear();
//        orderRepository.clear();
    }

    public void requestFoodPartyData() {
        TimerTask handleFoodPartyPeriodic = new HandleFoodPartyPeriodic(restaurants);
        Timer timer = new Timer();
        timer.schedule(handleFoodPartyPeriodic, 0, 18 * 10000); //every 300 secs
    }

    public ArrayList<PartyFood> getParty() {
        ArrayList<PartyFood> ans = new ArrayList<PartyFood>();
        for(Restaurant restaurant: restaurants) {
            if(restaurant.getPartyMenu() == null)
                continue;

            for(PartyFood food: restaurant.getPartyMenu())
                ans.add(food);
        }
        return ans;
    }

    public boolean finalizeCart() {
        if(cart.getOrderItems().isEmpty())
            return false;
        int finalPrice = cart.getFinalPrice();
        if(finalPrice > user.getCredit())
            return false;
        user.decreaseCredit(finalPrice);
        Order newOrder = new Order(cart, findRestaurantById(cart.getRestaurantId()));
        orderRepository.addOrder(newOrder);
        cart = new Cart();
        return true;
    }
}