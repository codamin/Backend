package Loghme.database.dataMappers.restaurant;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.food.FoodMapper;
import Loghme.entities.Food;
import Loghme.entities.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RestaurantMapper extends Mapper<Restaurant, String> implements IRestaurantMapper {

    private static RestaurantMapper instance;

    static {
        try {
            instance = new RestaurantMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static RestaurantMapper getInstance() {
        return instance;
    }

    private RestaurantMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS restaurant (" +
                            "id VARCHAR(200)," +
                            "name VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
                            "location_x INTEGER," +
                            "location_y INTEGER," +
                            "logo VARCHAR(200)," +
                            "PRIMARY KEY(id));";
        PreparedStatement createTableStatement = con.prepareStatement(query);
        System.out.println(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public ArrayList<Restaurant> getAllRestaurants() {
        ArrayList<Restaurant> result = new ArrayList<Restaurant>();
        try (Connection con = ConnectionPool.getConnection();
             Statement stmt = con.createStatement();
        ) {
            ResultSet resultSet;
            resultSet = stmt.executeQuery(getAllUsersStatement());
            while(resultSet.next())
            {
                result.add(convertResultSetToDomainModel(resultSet));
            }
            resultSet.close();
            stmt.close();
            con.close();
            return result;
        }
    }

    public boolean insert(Restaurant restaurant) throws SQLException {
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement());
        fillInsertValues(st, restaurant);
        try {
            result = st.execute();
            FoodMapper foodMapper = FoodMapper.getInstance();
            for(Food food: restaurant.getMenu()) {
                foodMapper.insert(food);
            }
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected String getFindStatement(String id) {
        return null;
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT INTO restaurant(id, name, location_x, location_y, logo) VALUES(?,?,?,?,?)";
    }

    protected void fillInsertValues(PreparedStatement st, Restaurant restaurant) throws SQLException {
        st.setString(1, restaurant.getId());
        st.setString(2, restaurant.getName());
        st.setInt(3, restaurant.getLocation().getX());
        st.setInt(4, restaurant.getLocation().getY());
        st.setString(5, restaurant.getLogo());
    }

    @Override
    protected String getDeleteStatement(String id) {
        return null;
    }

    @Override
    protected Restaurant convertResultSetToObject(ResultSet rs) throws SQLException {
        return null;
    }
}
