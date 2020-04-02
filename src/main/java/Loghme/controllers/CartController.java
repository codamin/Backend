package Loghme.controllers;

import Loghme.models.Cart;
import Loghme.models.IeatRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @GetMapping
    public Cart getCart() {
        return IeatRepository.getInstance().getCart();
    }

    @PostMapping("/{restaurantId}/{foodName}/add")
    public void addToCart(@PathVariable("restaurantId") String restaurantId, @PathVariable("foodName") String foodName) {
        IeatRepository.getInstance().addToCart(restaurantId, foodName);
    }

}