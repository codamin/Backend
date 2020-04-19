package Loghme.controllers;

import Loghme.entities.Cart;
import Loghme.entities.IeatRepository;
import Loghme.entities.OrderItem;
import Loghme.requests.AddToCart;
import Loghme.requests.DeleteFromCart;
import Loghme.services.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @GetMapping
    public Cart getCart() {
        return IeatRepository.getInstance().getCart();
    }

    @GetMapping("/{restaurantId}/{foodName}")
    public OrderItem getOrderItem(@PathVariable("restaurantId") String restaurantId, @PathVariable("foodName") String foodName) {
        return CartService.getOrderItem(restaurantId, foodName);
    }

    @PostMapping()
    public void addToCart(@RequestBody AddToCart req) {
        CartService.addToCart(req.getRestaurantId(), req.getFoodName(), req.getNum());
    }

    @DeleteMapping()
    public void deleteFromCart(@RequestBody DeleteFromCart req) {
        CartService.deleteFromCart(req.getRestaurantId(), req.getFoodName());
    }

    @PostMapping("/finalize")
    public void finalizeCart() {
        CartService.finalizeCart();
    }

}