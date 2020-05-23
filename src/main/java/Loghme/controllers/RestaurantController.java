package Loghme.controllers;

import Loghme.controllers.DTOs.RestaurantDTO;
import Loghme.entities.Restaurant;
import Loghme.services.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @GetMapping
    public ArrayList<RestaurantDTO> getRestaurants(
            @RequestParam(value = "restaurantSearch", required = false) String restaurantSearch,
            @RequestParam(value = "foodSearch", required = false) String foodSearch,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "items") int items,
            @RequestAttribute("user") String mail) throws SQLException {
        ArrayList<RestaurantDTO> res = RestaurantService.getRestaurantsList(page, items, restaurantSearch, foodSearch, mail);
        return res;
    }

    @GetMapping("/{restaurantId}")
    public Restaurant getRestaurant(@PathVariable("restaurantId") String restaurantId) throws SQLException {
        return RestaurantService.getRestaurant(restaurantId);
    }
}