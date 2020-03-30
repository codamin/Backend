package Loghme.scheduler;

import Loghme.Utilities.RequestApi;
import Loghme.models.PartyFood;
import Loghme.models.Restaurant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import static Loghme.models.IeatRepository.resetRemainingTime;

public class HandleFoodPartyPeriodic extends TimerTask {

    ArrayList<Restaurant> restaurants;

    public HandleFoodPartyPeriodic(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    private boolean isAddedAlready(Restaurant newRestaurant) {
        for(Restaurant restaurant: restaurants) {
            if(restaurant.getName().equals(newRestaurant.getName())) {
                return true;
            }
        }
        return false;
    }

    public void setFoodsRestaurant() {
        for(Restaurant restaurant: restaurants) {
            restaurant.setRestaurantIds();
            restaurant.setRestaurantNames();
        }
    }

    private void clearPreviosFoodParty() {
        for(Restaurant restaurant: restaurants)
            restaurant.getPartyMenu().clear();
    }

    private void updateDataParty(String data) throws IOException {
        clearPreviosFoodParty();

        ObjectMapper mapper = new ObjectMapper();
        List<Restaurant> newRestaurants = mapper.readValue(data, new TypeReference<List<Restaurant>>(){});
        for(Restaurant restaurant: newRestaurants) {
            if(isAddedAlready(restaurant))
                restaurant.updateParty(restaurant.getPartyMenu());
            else
                restaurants.add(restaurant);
        }
        setFoodsRestaurant();
    }

    @Override
    public void run() {

        System.out.println("requesting food party data at = " + System.currentTimeMillis() / 1000);
        String data = null;
        try {
            data = RequestApi.request("http://138.197.181.131:8080/foodparty").replace("menu", "partyMenu");
            updateDataParty(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}