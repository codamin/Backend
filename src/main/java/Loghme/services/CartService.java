package Loghme.services;

import Loghme.database.dataMappers.order.OrderMapper;
import Loghme.entities.Cart;
import Loghme.entities.IeatRepository;
import Loghme.entities.Order;
import Loghme.requests.DeleteFromCart;

import java.sql.SQLException;

public class CartService {
//    private static CartRepository cartRepository = new CarRepository();

    public static Order getCart(String email) {
        Cart cart = new Cart();
        OrderMapper orderMapper = OrderMapper.getInstance();
        Order resp = null;
        try {
            resp = orderMapper.getCart(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resp;
    }

//    public static Order getOrderItem(String restaurantId, String foodName) {
//        return cartRepository.getOrderItem(restaurantId, foodName);
//    }
//
    public static void addToCart(String userId, String restaurantId, String foodName, int number) {
        System.out.println("cart service : add to cart ");
        System.out.println(userId + restaurantId + foodName);
        System.out.println(number);
        System.out.println(">>>>>>>>>>>>>>>>");
        OrderMapper orderMapper = OrderMapper.getInstance();
        orderMapper.addToCart(userId, restaurantId, foodName, number);
    }
//
//    public static void deleteFromCart(String restaurantId, String foodName) {
//        cartRepository.deleteFromCart(restaurantId, foodName);
//    }
//
//    public static void finalizeCart() {
//        cartRepository.finalizeCart();
//    }
}
