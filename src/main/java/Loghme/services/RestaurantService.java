package Loghme.services;

import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Food;
import Loghme.entities.Restaurant;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService {

    public static ArrayList<Restaurant> getRestaurantsList(int page, int items, String restaurantSearch, String foodSearch) throws SQLException {
        ArrayList<Restaurant> result = new ArrayList<Restaurant>();
        RestaurantMapper mapper = RestaurantMapper.getInstance();
        System.out.println("res search====================================" + restaurantSearch);
        System.out.println("food search====================================" + foodSearch);

        if(restaurantSearch == null && foodSearch == null) {
            System.out.println("find all called both was null");
            result = mapper.findAll(page, items);
        }
        else {
            System.out.println("#################################### search called");
            String searchFood = foodSearch==null ? "" : foodSearch;
            String searchRestaurant = restaurantSearch==null? "" : restaurantSearch;
            result = mapper.search(page, items, searchRestaurant, searchFood);
        }
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