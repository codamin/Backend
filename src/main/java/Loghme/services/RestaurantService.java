package Loghme.services;

import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Restaurant;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService {

    public static ArrayList<Restaurant> getRestaurantsList() throws SQLException {
        ArrayList<Restaurant> result = RestaurantMapper.getInstance().findAll();
        for(Restaurant restaurant: result) {
            System.out.println(restaurant.getId());
        }
        return result;
    }

    public static Restaurant getRestaurant(String id) throws SQLException {
        Restaurant result = RestaurantMapper.getInstance().find(id);
        return result;
    }
}
