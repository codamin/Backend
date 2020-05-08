package Loghme.database.dataMappers.food;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.food.party.PartyMapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Food;
import Loghme.entities.PartyFood;
import Loghme.entities.Restaurant;

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
            System.out.println("error occured in getting instance of FoodMapper");
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
                "popularity FLOAT," +
                "price INTEGER," +
                "image VARCHAR(200)," +
                "restaurantId VARCHAR(24)," +
                "party BOOLEAN," +
                "PRIMARY KEY(id)," +
                "FOREIGN KEY(restaurantId) REFERENCES restaurant(id)" +
                ");";
        PreparedStatement createTableStatement = con.prepareStatement(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    public boolean insert(PartyFood food) throws SQLException {
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement());
        fillInsertValues(st, food);
        try {
            result = st.execute();
            st.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("error in Mapper.insert query.");
            st.close();
            con.close();
            throw e;
        }
        if(!food.isParty())
            return result;
        ArrayList<Food> foods = find(food.getName(), food.getRestaurantId());
        if(foods.size() == 0)
            return false;
        int foodId = foods.get(0).getId();
        PartyMapper partyMapper = PartyMapper.getInstance();
        result = partyMapper.insert(foodId, food.getOldPrice(), food.getCount());
        return result;
    }

    @Override
    protected String getFindStatement(Integer id) {
        String query = "SELECT * " +
                "FROM food \n" +
                "WHERE id = ?";
        return query;
    }

    @Override
    protected String getFindAllStatement() throws SQLException {
        return "SELECT * " +
                "FROM food f " +
                "WHERE f.restaurantId = ? AND party = false";
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT IGNORE INTO food(id,name,description,popularity,price,image,restaurantId,party)" +
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
        float popularity = st.getFloat(4);
        int price = st.getInt(5);
        String image = st.getString(6);
        String restaurantId = st.getString(7);
        boolean party = st.getBoolean(8);
        Food food = new Food(id, name, description, popularity, price, image, restaurantId, "", party);
        return food;
    }

    @Override
    protected ArrayList<Food> getDAOList(ResultSet rs) throws SQLException {
        ArrayList<Food> foods = new ArrayList<>();
        while (rs.next()){
            Food food = this.getDAO(rs);
            foods.add(food);
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
        st.setBoolean(7, food.isParty());
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
                "name = ? AND \n" +
                "restaurantId = ?";
        return query;
    }

    public ArrayList<Food> find(String name, String restaurantId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindStatement(name, restaurantId));
        st.setString(1, name);
        st.setString(2, restaurantId);

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

    public Food find(Integer id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindStatement(id));
        st.setInt(1, id);

        ResultSet resultSet;
        try {
            resultSet = st.executeQuery();
            if (resultSet.isClosed()) {
                st.close();
                con.close();
                return null;
            }
            if(!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            Food food = getDAO(resultSet);
            st.close();
            con.close();
            return food;
        } catch (SQLException ex) {
            st.close();
            con.close();
            throw ex;
        }
    }
}
