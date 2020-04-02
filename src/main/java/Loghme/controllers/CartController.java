package Loghme.controllers;

import Loghme.models.Cart;
import Loghme.models.IeatRepository;
import Loghme.requests.AddToCart;
import Loghme.requests.DeleteFromCart;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @GetMapping
    public Cart getCart() {
        return IeatRepository.getInstance().getCart();
    }

    @PostMapping()
    public void addToCart(@RequestBody AddToCart req) {
        IeatRepository.getInstance().addToCart(req.getRestaurantId(), req.getFoodName());
    }

    @DeleteMapping()
    public void deleteFromCart(@RequestBody DeleteFromCart req) {
        IeatRepository.getInstance().deleteFromCart(req.getRestaurantId(), req.getFoodName());
    }

}