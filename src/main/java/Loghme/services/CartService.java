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
        OrderMapper orderMapper = OrderMapper.getInstance();
        Order resp = new Order();
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
