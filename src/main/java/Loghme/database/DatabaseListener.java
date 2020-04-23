package Loghme.database;

import Loghme.Utilities.FetchData;
import Loghme.database.dataMappers.order.OrderMapper;
import Loghme.database.dataMappers.order.item.OrderItemMapper;
import Loghme.database.dataMappers.partyFood.PartyFoodMapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.PartyFood;
import Loghme.entities.Restaurant;
import Loghme.entities.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.w3c.dom.ls.LSOutput;

import javax.crypto.spec.PSource;
import java.sql.SQLException;
import java.util.List;

@Configuration
@EnableScheduling
public class DatabaseListener {

    public static void start() throws SQLException {

        addRestaurants();

        User khames = new User();
        try {
            UserMapper.getInstance().insert(khames);
            UserMapper.getInstance().find(khames.getEmail());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        OrderMapper.getInstance();
        OrderItemMapper.getInstance();
    }

    private static void addRestaurants() throws SQLException {
        List<Restaurant> restaurants = FetchData.fetchRestaurants();
        for(Restaurant restaurant: restaurants) {
            RestaurantMapper.getInstance().insert(restaurant);
        }
    }

    @Scheduled(fixedDelay = 5000)
    public static void addFoodParty() throws SQLException {
        List<Restaurant> partyRestaurants = FetchData.fetchFoodParty();
        System.out.println("after mapping..." + partyRestaurants.size());
        PartyFoodMapper.getInstance().cancelAll();
        for(Restaurant restaurant: partyRestaurants) {
            RestaurantMapper.getInstance().insert(restaurant);
        }
    }
}
