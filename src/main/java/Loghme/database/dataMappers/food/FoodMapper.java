package Loghme.database.dataMappers.food;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Food;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                "name VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
                "description VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
                "popularity INTEGER," +
                "price INTEGER," +
                "image VARCHAR(200)," +
                "restaurantId VARCHAR(24)," +
                "available INTEGER ," +
                "PRIMARY KEY(id)," +
                "FOREIGN KEY(restaurantId) REFERENCES restaurant(id)" +
                ");";
        PreparedStatement createTableStatement = con.prepareStatement(query);
        System.out.println(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    protected String getFindStatement(Integer id) {
        String query = "SELECT * " +
                "FROM food \n" +
                "WHERE id = " + id.toString() + ";";
        System.out.println(query);
        return query;
    }

    @Override
    protected String getFindAllStatement() throws SQLException {
        System.out.println("SELECT * " +
                "FROM food f " +
                "WHERE f.restaurantId = ?");
        return "SELECT * " +
                "FROM food f " +
                "WHERE f.restaurantId = ?";
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT IGNORE INTO food(id,name,description,popularity,price,image,restaurantId,available)" +
                " VALUES(DEFAULT,?,?,?,?,?,?,?)";
    }

    @Override
    protected String getDeleteStatement(Integer id) {
        return null;
    }

    @Override
    protected Food getDAO(ResultSet st) throws SQLException {
        int id = st.getInt(1);
        String name = st.getString(2);
        String description = st.getString(3);
        int popularity = st.getInt(4);
        int price = st.getInt(5);
        String image = st.getString(6);
        String restaurantId = st.getString(7);
        boolean available = st.getBoolean(8);
        return new Food(id, name, description, popularity, price, image, restaurantId, available);
    }

    @Override
    protected ArrayList<Food> getDAOList(ResultSet rs) throws SQLException {
        ArrayList<Food> foods = new ArrayList<>();
        while (rs.next()){
            foods.add(this.getDAO(rs));
        }
        return foods;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Food food) throws SQLException {
        st.setString(1, food.getName());
        st.setString(2, food.getDescription());
        st.setFloat(3, food.getPopularity());
        st.setInt(4, food.getPrice());
        st.setString(5,food.getImage());
        st.setString(6, food.getRestaurantId());
        st.setBoolean(7, food.getAvailable());
    }

    @Override
    public ArrayList<Food> findAll(String restaurantId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindAllStatement());
        st.setString(1, restaurantId);
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet.isClosed()) {
                st.close();
                con.close();
                return new ArrayList<Food>();
            }
            ArrayList<Food> result = getDAOList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (SQLException e) {
            System.out.println("error in FoodMapper.findAll query.");
            st.close();
            con.close();
            throw e;
        }
    }

    private String getFindStatement(String name, String restaurantId) {
        String query = "SELECT * FROM food WHERE\n" +
                "name = " + String.format("'%s'", name) + "AND \n" +
                "restaurantId = " + String.format("'%s'", restaurantId) + ";";
        return query;
    }

    public ArrayList<Food> find(String name, String restaurantId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindStatement(name, restaurantId));
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet.isClosed()) {
                st.close();
                con.close();
                return new ArrayList<Food>();
            }
            ArrayList<Food> result = getDAOList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (SQLException e) {
            System.out.println("error in FoodMapper.findBy name & resaurantId query.");
            st.close();
            con.close();
            throw e;
        }
    }

    public Food find(int id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindStatement(id));
        try {
            ResultSet resultSet = st.executeQuery();
            if(resultSet.isClosed()) {
                st.close();
                con.close();
                return null;
            }
            ArrayList<Food> result = getDAOList(resultSet);
            st.close();
            con.close();
            if(result.size() > 0)
                return result.get(0);
            else
                return null;
        } catch (SQLException e) {
            System.out.println("error in FoodMapper.findBy id query.");
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

}
