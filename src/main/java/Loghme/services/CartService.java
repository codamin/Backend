package Loghme.services;

import Loghme.entities.IeatRepository;
import Loghme.entities.Order;
import Loghme.requests.DeleteFromCart;

public class CartService {
    private static CartRepository cartRepository = new CarRepository();

    public static Order getOrderItem(String restaurantId, String foodName) {
        return cartRepository.getOrderItem(restaurantId, foodName);
    }

    public static void addToCart(String restaurantId, String foodName, int number) {
        cartRepository.addToCart(restaurantId, foodName, number);
    }

    public static void deleteFromCart(String restaurantId, String foodName) {
        cartRepository.deleteFromCart(restaurantId, foodName);
    }

    public static void finalizeCart() {
        cartRepository.finalizeCart();
    }
}
