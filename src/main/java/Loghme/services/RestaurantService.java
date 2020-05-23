package Loghme.services;

import Loghme.controllers.DTOs.RestaurantDTO;
import Loghme.controllers.DTOs.RestaurantListDTO;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Food;
import Loghme.entities.Restaurant;

import java.sql.SQLException;
import java.util.ArrayList;

public class RestaurantService {

    public static ArrayList<RestaurantDTO> getRestaurantsList(int page, int items, String restaurantSearch,
                                                              String foodSearch, String mail) throws SQLException {
        ArrayList<Restaurant> result = new ArrayList<Restaurant>();
        RestaurantMapper mapper = RestaurantMapper.getInstance();

        if(restaurantSearch == null && foodSearch == null) {
            result = mapper.findAll(page, items, mail);
        }
        else {
            String searchFood = foodSearch==null ? "" : foodSearch;
            String searchRestaurant = restaurantSearch == null ? "" : restaurantSearch;
            result = mapper.search(page, items, searchRestaurant, searchFood, mail);
        }
        RestaurantListDTO resultDTO = new RestaurantListDTO(result);
        return resultDTO.getList();
    }

    public static Restaurant getRestaurant(String id) throws SQLException {
        Restaurant result = RestaurantMapper.getInstance().find(id);
        return result;
    }
}