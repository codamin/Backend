package Loghme.database.dataMappers.order;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.food.FoodMapper;
import Loghme.database.dataMappers.order.item.OrderItemMapper;
import Loghme.database.dataMappers.restaurant.IRestaurantMapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Food;
import Loghme.entities.Order;
import Loghme.entities.OrderItem;
import Loghme.entities.User;
import Loghme.exceptions.ForbiddenException;
import Loghme.exceptions.NotFoundException;
//import com.sun.tools.javac.tree.JCTree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderMapper extends Mapper<Order, Integer> implements IOrderMapper {

    private static OrderMapper instance;

    static {
        try {
            instance = new OrderMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static OrderMapper getInstance() {
        return instance;
    }

    private OrderMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER NOT NULL AUTO_INCREMENT," +
                "userId VARCHAR(30)," +
                "restaurantId VARCHAR(24)," +
                "state VARCHAR(20)," +
                "remMin INTEGER," +
                "remSec INTEGER," +
                "PRIMARY KEY(id)," +
                "FOREIGN KEY(userId) REFERENCES user(email)," +
                "FOREIGN KEY(restaurantId) REFERENCES restaurant(id)" +
                ");";

        PreparedStatement createTableStatement = con.prepareStatement(query);
        System.out.println(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    protected void fillInsertValues(PreparedStatement st, Order order) throws SQLException {
//        st.setString(1, order.);
        st.setString(1, order.getRestaurant().getId());
        st.setString(2, order.getRestaurant().getId());
        st.setString(3, order.getState());
        st.setInt(4, order.getRemMin());
        st.setInt(5, order.getRemSec());
    }

    @Override
    protected String getFindStatement(Integer id) {
        return null;
    }

    @Override
    protected String getFindAllStatement() throws SQLException {
        return null;
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT INTO orders(id, userId, restaurantId, state, remMin, remSec) VALUES(DEFAULT,?,?,?,?,?);";
    }

    @Override
    protected String getDeleteStatement(Integer id) {
        return null;
    }

    @Override
    protected Order getDAO(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String userId = rs.getString(2);
        String restaurantId = rs.getString(3);
        String state = rs.getString(4);
        int remMin = rs.getInt(5);
        int remSec = rs.getInt(6);
        return new Order(id, userId, restaurantId, state, remMin, remSec);
    }

    @Override
    protected ArrayList<Order> getDAOList(ResultSet rs) throws SQLException {
        ArrayList<Order> resp = new ArrayList<Order>();
        while(rs.next()) {
            resp.add(getDAO(rs));
        }
        return resp;
    }

    @Override
    public boolean insert(Order order) throws SQLException {
        return false;
    }

    private void fillInsertCartValues(PreparedStatement st, String userId, String restaurantId) throws SQLException {
        st.setString(1, userId);
        st.setString(2, restaurantId);
        st.setString(3, "nf");
        st.setInt(4, -1);
        st.setInt(5, -1);
    }

    private String getInsertCartStatement() {
        return "INSERT INTO orders(id , userId, restaurantId, state, remMin, remSec) VALUES(DEFAULT,?,?,?,?,?);";
    }

    public boolean insertCart(String userId, String restaurantId) throws SQLException {
        System.out.println("add new cart");
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertCartStatement());
        fillInsertCartValues(st, userId, restaurantId);
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

    private String getCartStatment(String userId) {
        String query = "SELECT * FROM orders\n" +
                " WHERE userId = " + String.format("'%s'", userId) + "\n" +
                " AND state = 'nf';";
        System.out.println(query);
        return query;
    }

    public Order getCart(String userId) throws SQLException {
        System.out.println("finding cart");
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getCartStatment(userId));
        try {
            ResultSet rs = st.executeQuery();
            if(rs.isClosed()) {
                st.close();
                con.close();
                System.out.println("rs is closed on search for not finalized order");
                return null;
            }
            System.out.println("to get dao lists");
            ArrayList<Order> orders = getDAOList(rs);
            st.close();
            con.close();
            System.out.println("get size");
            System.out.println(orders.size());
            if(orders.size() == 0)
                return null;
            else
                return orders.get(0);
        } catch (SQLException e) {
            System.out.println("error in find the not finalized order");
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
    }

    public void addToCart(String userId, String restaurantId, String foodName, int number) {
        Order order = null;
        try {
            order = getCart(userId);
            if(order == null) {
                System.out.println("cart found as null");
                boolean resp = insertCart(userId, restaurantId);
                if(!resp)
                    return;
                order = getCart(userId);
            }
            System.out.println("found order");
            System.out.println(order.getId());
            System.out.println(order.getRestaurantId());
            if(!order.getRestaurantId().equals(restaurantId))
                throw new ForbiddenException("The restaurant of the food does not match the current cart restaurant.");
            FoodMapper foodMapper = FoodMapper.getInstance();
            ArrayList<Food> foods = foodMapper.find(foodName, restaurantId);
            if(foods.size() == 0)
                throw new NotFoundException("such food could not be found.");
            int foodId = foods.get(0).getId();
            OrderItemMapper itemMapper = OrderItemMapper.getInstance();
            System.out.println("to insert ");
            System.out.println(order.getId());
            System.out.println(foodId);
            System.out.println(number);
            itemMapper.insert(order.getId(), foodId, number);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
