package Loghme.database.dataMappers.restaurant;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.entities.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantMapper extends Mapper<Restaurant, String> implements IRestaurantMapper {

    private static RestaurantMapper instance;

    static {
        try {
            instance = new RestaurantMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private RestaurantMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS restaurant (" +
                            "id VARCHAR(200)," +
                            "name VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
                            "location_x INTEGER," +
                            "location_y INTEGER," +
                            "logo VARCHAR(200));";
        PreparedStatement createTableStatement = con.prepareStatement(query);
        System.out.println(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public static RestaurantMapper getInstance() {
        return instance;
    }

    public boolean insert(Restaurant restaurant) throws SQLException {
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement());
        fillInsertValues(st, restaurant);
        try {
            result = st.execute();
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
//        return "INSERT INTO restaurant(id, name, location_x, location_y) VALUES(?,?,?,?)";
    }

//    @Override
    protected void fillInsertValues(PreparedStatement st, Restaurant restaurant) throws SQLException {
        st.setString(1, restaurant.getId());
        st.setString(2, restaurant.getName());
        st.setInt(3, restaurant.getLocation().getX());
        st.setInt(4, restaurant.getLocation().getY());
        st.setString(5, restaurant.getLogo());
        System.out.println("#####################################################################"+ restaurant.getLogo());
        System.out.println("#####################################################################"+ restaurant.getLogo().length());
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
