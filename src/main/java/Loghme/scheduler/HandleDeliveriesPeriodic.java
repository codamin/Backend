package Loghme.scheduler;

import Loghme.database.dataMappers.order.OrderMapper;
import Loghme.entities.Delivery;
import Loghme.entities.Location;
import Loghme.entities.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static Loghme.Utilities.FetchData.request;

public class HandleDeliveriesPeriodic extends TimerTask {

    Order order;

    public HandleDeliveriesPeriodic(Order order) {
        this.order = order;
    }

    private ArrayList<Delivery> getDeliveriesList(String data) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Delivery> deliveries = null;
        try {
            deliveries = mapper.readValue(data, new TypeReference<List<Delivery>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deliveries;
    }


    private float getDeliveryDelay(Location restaurantLocation, ArrayList<Delivery> deliveries) {
        float minDelay = Float.MAX_VALUE;
        Location userLocation = new Location(0,0);
        for(Delivery delivery: deliveries) {
            if(delivery.deliveryDelay(userLocation, restaurantLocation) < minDelay) {
                minDelay = delivery.deliveryDelay(userLocation, restaurantLocation);
            }
        }
        return minDelay;
    }

    @Override
    public void run() {
        String data = null;
        data = request("http://138.197.181.131:8080/deliveries");
        ArrayList<Delivery> deliveries = getDeliveriesList(data);
        if(deliveries.size() > 0) {

            OrderMapper orderMapper = OrderMapper.getInstance();
            try {
                orderMapper.setState(order.getId(), "delivering");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            float deliveryDelay = getDeliveryDelay(order.getRestaurant().getLocation(), deliveries);
            TimerTask setStateToDone = new SetStateToDone(order);
            Timer timer = new Timer();

            timer.schedule(setStateToDone, (long) deliveryDelay * 1000);
            this.cancel();
            cancel();
        }
    }
}