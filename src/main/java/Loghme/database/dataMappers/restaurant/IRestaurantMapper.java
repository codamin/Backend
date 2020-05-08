package Loghme.database.dataMappers.restaurant;

import Loghme.database.dataMappers.IMapper;
import Loghme.entities.Restaurant;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IRestaurantMapper extends IMapper<Restaurant, String> {
     String getFindAllStatement(int page, int items) throws SQLException;
     ArrayList<Restaurant> findAll(int page, int items) throws SQLException;
     String getSearchStatement(int page, int items, String restaurantSearch, String foodSearch);
}
