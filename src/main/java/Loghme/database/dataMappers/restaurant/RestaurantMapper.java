package Loghme.database.dataMappers.restaurant;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.food.FoodMapper;
import Loghme.entities.Food;
import Loghme.entities.Location;
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
                            "id VARCHAR(24)," +
                            "name VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
                            "location_x INTEGER," +
                            "description VARCHAR(200)," +
                            "location_y INTEGER," +
                            "logo VARCHAR(200)," +
                            "PRIMARY KEY(id));";
        PreparedStatement createTableStatement = con.prepareStatement(query);
        System.out.println(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public boolean insert(Restaurant restaurant) throws SQLException {
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement());
        fillInsertValues(st, restaurant);
        try {
            result = st.execute();
            FoodMapper foodMapper = FoodMapper.getInstance();
            restaurant.setRestaurantIds();
            restaurant.setRestaurantNames();
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
        return "SELECT * " +
                "FROM restaurant";
    }

    @Override
    protected String getFindAllStatement() throws SQLException {
        return "SELECT * " +
                "FROM restaurant";
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT INTO restaurant(id, name, location_x, location_y, logo, description) VALUES(?,?,?,?,?,?)";
    }

    protected void fillInsertValues(PreparedStatement st, Restaurant restaurant) throws SQLException {
        st.setString(1, restaurant.getId());
        st.setString(2, restaurant.getName());
        st.setInt(3, restaurant.getLocation().getX());
        st.setInt(4, restaurant.getLocation().getY());
        st.setString(5, restaurant.getLogo());
        st.setString(6, restaurant.getDescription());
    }

    @Override
    protected String getDeleteStatement(String id) {
        return null;
    }

    @Override
    protected Restaurant getDAO(ResultSet rs) throws SQLException {
        String id = rs.getString(1);
        String name = rs.getString(2);
        int loc_x = rs.getInt(3);
        int loc_y = rs.getInt(4);
        Location location = new Location(loc_x, loc_y);
        String logo = rs.getString(5);
        String description = rs.getString(6);
        ArrayList<Food> menu = new ArrayList<Food>();

        FoodMapper foodMapper = FoodMapper.getInstance();
        for(Food food: foodMapper.findAll()) {
            menu.add(food);
        }
        return new Restaurant(id, name, location, logo, menu, description);
    }

    @Override
    protected ArrayList<Restaurant> getDAOList(ResultSet rs) throws SQLException {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        while (rs.next()){
            restaurants.add(this.getDAO(rs));
        }
        return restaurants;
    }
}