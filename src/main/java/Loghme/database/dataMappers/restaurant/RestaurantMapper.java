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
        PreparedStatement createTableStatement = con.prepareStatement("CREATE TABLE IF NOT EXISTS restaurant (id CHAR,\n" +
                "name CHAR CHARACTER SET utf8 COLLATE utf8_unicode_ci,\n" +
                "location_x INTEGER,\n" +
                "location_y INTEGER, \n" +
                "logo CHAR,\n");
    }

    public static RestaurantMapper getInstance() {
        System.out.println("restaurant mapper getInstance called");
        return instance;
    }

    public boolean insert(Restaurant restaurant) throws SQLException {
        System.out.println("fuck1");
        boolean result;
        System.out.println("fuck1");

        Connection con = ConnectionPool.getConnection();
        System.out.println("fuck1");

        PreparedStatement st = con.prepareStatement(getInsertStatement());
        System.out.println("fuck1");

        fillInsertFields(st, restaurant);
        System.out.println("insert trying");
        try {
            System.out.println("insert trying");

            result = st.execute();
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            System.out.println("insert catch");
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

//    @Override
    protected void fillInsertFields(PreparedStatement st, Restaurant restaurant) throws SQLException {
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
