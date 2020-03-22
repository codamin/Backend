package repository;

import java.util.Comparator;

public class Delivery {

    String id;
    int velocity;

    Location location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public float deliveryDelay(Location userLocation, Location restaurantLocation) {
        float deliveryToRestaurant = location.getDistance(restaurantLocation);
        float restaurantToUser = restaurantLocation.getDistance(userLocation);
        float totalDistance = deliveryToRestaurant + restaurantToUser;
        float delay = totalDistance / velocity;
        return delay;
    }
}
