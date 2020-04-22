package Loghme.services;

import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Food;
import Loghme.entities.Restaurant;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService {

    public static ArrayList<Restaurant> getRestaurantsList(String restaurantSearch, String foodSearch) throws SQLException {
        ArrayList<Restaurant> result = new ArrayList<Restaurant>();
        RestaurantMapper mapper = RestaurantMapper.getInstance();
        result = restaurantSearch == null && foodSearch == null ? mapper.findAll() : mapper.search(restaurantSearch, foodSearch);
        return result;
    }

    public static Restaurant getRestaurant(String id) throws SQLException {
        Restaurant result = RestaurantMapper.getInstance().find(id);
        System.out.println(result.getId());
        System.out.println(result.getLogo());
        System.out.println(result.getDescription());
        for(Food food: result.getMenu()) {
            System.out.println("*************food: " + food.getName());
        }
        return result;
    }
}