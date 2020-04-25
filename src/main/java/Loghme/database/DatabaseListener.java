package Loghme.database;

import Loghme.Utilities.FetchData;
import Loghme.database.dataMappers.food.party.PartyMapper;
import Loghme.database.dataMappers.order.OrderMapper;
import Loghme.database.dataMappers.order.item.OrderItemMapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.Restaurant;
import Loghme.entities.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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
            throw e;
        }

        OrderMapper.getInstance();
        OrderItemMapper.getInstance();
    }

    private static void addRestaurants() {
        List<Restaurant> restaurants = FetchData.fetchRestaurants();
        for(Restaurant restaurant: restaurants) {
            try {
                RestaurantMapper.getInstance().insert(restaurant);
            } catch (SQLException e) {
                System.out.println("error occured on insert restaurant");
            }
        }
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000) // 10 min
    public static void addFoodParty() {
        List<Restaurant> partyRestaurants = FetchData.fetchFoodParty();
        PartyMapper partyMapper = PartyMapper.getInstance();
        try {
            partyMapper.expireAll();
        } catch (SQLException e) {
            System.out.println("error occured in expire all previous food parties");
        }
        for(Restaurant restaurant: partyRestaurants) {
            try {
                RestaurantMapper.getInstance().insert(restaurant);
            } catch (SQLException e) {
                System.out.println("error occured on insert party rest");
            }
        }
    }
}
