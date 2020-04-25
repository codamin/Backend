package Loghme.DTOs;

import Loghme.entities.Restaurant;

import java.util.ArrayList;

public class RestaurantListDTO {
    private ArrayList<RestaurantDTO> listR;

    public RestaurantListDTO(ArrayList<Restaurant> restaurants) {
        listR = new ArrayList<RestaurantDTO>();
        for(Restaurant restaurant: restaurants) {
            RestaurantDTO newR = new RestaurantDTO(restaurant);
            listR.add(newR);
        }
    }

    public ArrayList<RestaurantDTO> getList() {
        return listR;
    }
}
