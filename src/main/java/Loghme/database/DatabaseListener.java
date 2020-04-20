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
    private static DatabaseListener instance;
    private static RestaurantMapper restaurantMapper = null;

    static {
        instance = new DatabaseListener();
    }

    public DatabaseListener() {
        System.out.println("print");
        restaurantMapper = RestaurantMapper.getInstance();
        System.out.println("shit");
        List<Restaurant> restaurants = FetchData.fetchRestaurants();
        System.out.println("fuck");
        for(Restaurant restaurant: restaurants) {
            try {
                System.out.println("try");
                RestaurantMapper.getInstance().insert(restaurant);
            } catch (SQLException e) {
                System.out.println("catch");
                e.printStackTrace();
            }
        }
        System.out.println("this");
    }

//    private static RestaurantMapper restaurantMapper;
////    private RestaurantMapper restaurantMapper;
//
//
//
//    public static void initialDatabase() throws SQLException {
//        restaurantMapper = RestaurantMapper.getInstance();
//    }

    @Scheduled(fixedDelay = 85)
    public static void syncData() {
        System.out.println("fuck :))))");
    }
}
