package Loghme.controllers;

//import Loghme.entities.IeatRepository;
import Loghme.entities.Order;
//import Loghme.entities.OrderItem;
//import Loghme.requests.AddToCart;
import Loghme.exceptions.NotFoundException;
import Loghme.requests.DeleteFromCart;
//import Loghme.services.CartService;
import Loghme.requests.AddToCart;
import Loghme.services.CartService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/cart")
public class CartController {

    @GetMapping
    public Order getCart() {
        return CartService.getCart("ekhamespanah@yahoo.com");
    }

//    @GetMapping("/{restaurantId}/{foodName}")
//    public OrderItem getOrderItem(@PathVariable("restaurantId") String restaurantId, @PathVariable("foodName") String foodName) {
//        return CartService.getOrderItem(restaurantId, foodName);
//    }
//
    @PostMapping()
    public void addToCart(@RequestBody AddToCart req) {
        CartService.addToCart("ekhamespanah@yahoo.com", req.getRestaurantId(), req.getFoodName(), req.getNum());
    }

    @DeleteMapping()
    public void deleteFromCart(@RequestBody DeleteFromCart req) {
        CartService.deleteFromCart("ekhamespanah@yahoo.com", req.getRestaurantId(), req.getFoodName());
    }

    @PostMapping("/finalize")
    public void finalizeCart() {
        try {
            System.out.println("fuck in cart controller>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>o");
            CartService.finalizeCart("ekhamespanah@yahoo.com");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NotFoundException("order is not good");
        }
    }

}