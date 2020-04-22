package Loghme.controllers;

import Loghme.entities.IeatRepository;
import Loghme.entities.Restaurant;
import Loghme.services.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @GetMapping
    public ArrayList<Restaurant> getRestaurants() throws SQLException {
//        return IeatRepository.getInstance().findNearRestaurants(170);
        return RestaurantService.getRestaurantsList();
    }

    @GetMapping("/{restaurantId}")
    public Restaurant getRestaurant(@PathVariable("restaurantId") String restaurantId) {
//        return IeatRepository.getInstance().getRestaurantById(restaurantId);
        return RestaurantService.getRestaurant();
    }
}
