package Loghme.database.dataMappers.food;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Food;

import java.awt.peer.CanvasPeer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodMapper extends Mapper<Food, Integer> implements IFoodMapper {

    private static FoodMapper instance;

    static {
        try {
            instance = new FoodMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static FoodMapper getInstance() {
        return instance;
    }

    private FoodMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS food (" +
                "id INTEGER NOT NULL AUTO_INCREMENT," +
                "PRIMARY KEY(id)," +
                "name VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
                "description VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
                "popularity INTEGER," +
                "price INTEGER," +
                "image VARCHAR(200)," +
                "restaurantName VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
                "restaurantId VARCHAR(200)," +
                "available INTEGER," +
                "FOREIGN KEY(restaurantId) REFERENCES restaurant(id));";
        PreparedStatement createTableStatement = con.prepareStatement(query);
        System.out.println(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindStatement(Integer id) {
        return "SELECT * " +
                "FROM food f" +
                "WHERE f.id = " + id.toString();
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT IGNORE INTO food(name,description,popularity,price,image,restaurantName,restaurantId,available)" +
                " VALUES(?,?,?,?,?,?,?,?)";
    }

    @Override
    protected String getDeleteStatement(Integer id) {
        return null;
    }

    void fillInsertValues(PreparedStatement st, Food food) throws SQLException {
        st.setString(1, food.getName());
        st.setString(2, food.getDescription());
        st.setFloat(3, food.getPopularity());
        st.setInt(4, food.getPrice());
        st.setString(5,food.getImage());
        st.setString(6, food.getRestaurantName());
        st.setString(7, food.getRestaurantId());
        st.setBoolean(8, food.getAvailable());
    }

    @Override
    protected Food convertResultSetToObject(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public Food find(Integer id) throws SQLException {
        return null;
    }

    @Override
    public boolean insert(Food food) throws SQLException {
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement());
        fillInsertValues(st, food);
        result = st.execute();
        st.close();
        con.close();
        return result;
    }
}
