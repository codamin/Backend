package scheduler;

import Utilities.RequestApi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import repository.Delivery;
import repository.Location;
import repository.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
        System.out.println("requesting for deliveries at time: " + System.currentTimeMillis() / 1000);
        String data = null;
        try {
            data = RequestApi.request("http://138.197.181.131:8080/deliveries");
            ArrayList<Delivery> deliveries = getDeliveriesList(data);
            if(deliveries.size() > 0) {

                System.out.println("A delivery found at time = " + System.currentTimeMillis() / 1000);

                order.setState("delivering");
                float deliveryDelay = getDeliveryDelay(order.getRestaurant().getLocation(), deliveries);
                TimerTask setStateToDone = new SetStateToDone(order);
                Timer timer = new Timer();

                System.out.println("delivery delay = " + deliveryDelay + " seconds");

                timer.schedule(setStateToDone, (long) deliveryDelay * 1000);
                this.cancel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}