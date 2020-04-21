package Loghme.services;

import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class restaurantService {

    public static List<Restaurant> getRestaurants() {
        List<Restaurant> result = RestaurantMapper.findRestaurants();
        return result;
    }
}
