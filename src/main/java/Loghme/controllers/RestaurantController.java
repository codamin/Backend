package Loghme.controllers;

import Loghme.models.IeatRepository;
import Loghme.models.Restaurant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @GetMapping
    public ArrayList<Restaurant> getRestaurants() {
        return IeatRepository.getInstance().findNearRestaurants(170);
    }

    @GetMapping("/{restaurantId}")
    public Restaurant getRestaurant(@PathVariable("restaurantId") String restaurantId) {
        return IeatRepository.getInstance().getRestaurantById(restaurantId);
    }

}
