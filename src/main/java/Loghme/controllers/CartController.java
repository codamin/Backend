package Loghme.controllers;

//import Loghme.entities.IeatRepository;
import Loghme.DTOs.OrderDTO;
import Loghme.entities.Order;
//import Loghme.entities.OrderItem;
//import Loghme.requests.AddToCart;
import Loghme.exceptions.ForbiddenException;
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
    public OrderDTO getCart(@RequestAttribute("user") String mail) {
        return CartService.getCart(mail);
    }

    @PostMapping()
    public void addToCart(@RequestBody AddToCart req, @RequestAttribute("user") String mail) {
        try {
            CartService.addToCart(mail, req.getRestaurantId(), req.getFoodName(), req.getNum());
        } catch (SQLException e) {
            System.out.println("error occured in addToCart post request handling");
            throw new ForbiddenException("error occured in addToCart post request handling");
        }
    }

    @DeleteMapping()
    public void deleteFromCart(@RequestBody DeleteFromCart req, @RequestAttribute("user") String mail) {
        try {
            CartService.deleteFromCart(mail, req.getRestaurantId(), req.getFoodName());
        } catch (SQLException e) {
            System.out.println("error occured in deleteFrom cart delete request handling");
            throw new ForbiddenException("error occured in deleteFrom cart delete request handling");
        }
    }

    @PostMapping("/finalize")
    public void finalizeCart(@RequestAttribute("user") String mail) {
        try {
            CartService.finalizeCart(mail);
        } catch (SQLException e) {
            System.out.println("error occured in finalize cart");
            throw new ForbiddenException("order is not good");
        }
    }
}