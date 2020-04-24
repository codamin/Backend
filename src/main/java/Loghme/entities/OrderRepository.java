package Loghme.entities;


import Loghme.scheduler.HandleDeliveriesPeriodic;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class OrderRepository {

    private ArrayList<Order> orders;
    public OrderRepository() {
        orders = new ArrayList<Order>();
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order newOrder) {
         orders.add(newOrder);
    }

    public Order findOrderById(int id) {
        if(orders == null)
            return null;
        for(Order order: orders)
            if(order.getId() == id)
                return order;
        return null;
    }
}