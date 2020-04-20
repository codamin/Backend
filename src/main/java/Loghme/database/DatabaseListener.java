package Loghme.database;

import Loghme.Utilities.FetchData;
import Loghme.Utilities.RequestApi;
import Loghme.database.dataMappers.restaurant.IRestaurantMapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Restaurant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Configuration
@EnableScheduling
public class DatabaseListener {

    public static void start() {
        List<Restaurant> restaurants = FetchData.fetchRestaurants();
        for(Restaurant restaurant: restaurants) {
            try {
                RestaurantMapper.getInstance().insert(restaurant);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    public static void syncData() {
        System.out.println("tick tock :))))");
    }
}
