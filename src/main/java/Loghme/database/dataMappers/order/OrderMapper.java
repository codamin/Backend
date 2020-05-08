package Loghme.database.dataMappers.order;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.food.FoodMapper;
import Loghme.database.dataMappers.food.party.PartyMapper;
import Loghme.database.dataMappers.order.item.OrderItemMapper;
import Loghme.database.dataMappers.restaurant.IRestaurantMapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.*;
import Loghme.exceptions.ForbiddenException;
import Loghme.exceptions.NotFoundException;
import org.springframework.http.codec.multipart.Part;
//import com.sun.tools.javac.tree.JCTree;

import javax.security.auth.login.AccountExpiredException;
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
            System.out.println("error occured in gettring instance of OrderMapper");
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
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    protected void fillInsertValues(PreparedStatement st, Order order) throws SQLException {
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
    protected String getInsertStatement() {
        return "INSERT INTO orders(id, userId, restaurantId, state, remMin, remSec) VALUES(DEFAULT,?,?,?,?,?);";
    }

    @Override
    protected String getDeleteStatement(Integer id) {
        String query = "DELETE FROM orders WHERE id = ?";
        return query;
    }

    public void delete(Integer id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getDeleteStatement(id));
        st.setInt(1, id);
        try {
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLException ex) {
            System.out.println("error in Mapper.delete query.");
            st.close();
            con.close();
            throw ex;
        }
    }

    @Override
    protected Order getDAO(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String userId = rs.getString(2);
        String restaurantId = rs.getString(3);
        String state = rs.getString(4);
        int remMin = rs.getInt(5);
        int remSec = rs.getInt(6);
        Order order = new Order(id, userId, restaurantId, state, remMin, remSec);
        OrderItemMapper itemMapper = OrderItemMapper.getInstance();
        for(OrderItem item: itemMapper.findAll(order.getId())) {
            order.addItem(item);
        }
        RestaurantMapper restaurantMapper = RestaurantMapper.getInstance();
        Restaurant restaurant = restaurantMapper.find(restaurantId);
        order.setRestaurant(restaurant);
        order.setRestaurantName(restaurant.getName());
        return order;
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
        int result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertCartStatement());
        fillInsertCartValues(st, userId, restaurantId);
        try {
            result = st.executeUpdate();
            st.close();
            con.close();
            return result > 0;
        } catch (Exception e) {
            st.close();
            con.close();
            throw e;
        }
    }

    private String getCartStatment() {
        String query = "SELECT * FROM orders\n" +
                " WHERE userId = ? AND state = 'nf';";
        return query;
    }

    public Order getCart(String userId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getCartStatment());
        st.setString(1, userId);
        try {
            ResultSet rs = st.executeQuery();
            if(rs.isClosed()) {
                st.close();
                con.close();
                return new Order();
            }
            ArrayList<Order> orders = getDAOList(rs);
            st.close();
            con.close();
            if(orders.size() == 0) {
                return new Order();
            }
            else {
                return orders.get(0);
            }
        } catch (SQLException e) {
            st.close();
            con.close();
            throw e;
        }
    }

    public void addToCart(String userId, String restaurantId, String foodName, int number) throws SQLException {
        Order order = null;
        try {
            order = getCart(userId);
            if(order.getId() == null) {
                boolean resp = insertCart(userId, restaurantId);
                if(!resp)
                    return;
            }
            order = getCart(userId);
            if(!order.getRestaurantId().equals(restaurantId))
                throw new ForbiddenException("The restaurant of the food does not match the current cart restaurant.");
            FoodMapper foodMapper = FoodMapper.getInstance();
            ArrayList<Food> foods = foodMapper.find(foodName, restaurantId);
            if(foods.size() == 0)
                throw new NotFoundException("such food could not be found.");
            int foodId = foods.get(0).getId();
            OrderItemMapper itemMapper = OrderItemMapper.getInstance();
            itemMapper.insert(order.getId(), foodId, number);

        } catch (SQLException e) {
            throw e;
        }
    }

    private String getSetStateStatement() {
        String query = "UPDATE IGNORE orders SET state = ? WHERE id = ?";
        return query;

    }
    public void setState(int orderId, String state) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getSetStateStatement());
        st.setString(1, state);
        st.setInt(2, orderId);

        try {
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLException e) {
            st.close();
            con.close();
            throw e;
        }
    }

    private void handleFinalize(String userId, Order order) throws SQLException {
        setState(order.getId(), "finalized");
        PartyMapper partyMapper = PartyMapper.getInstance();
        for(OrderItem item: order.getOrderItems()) {
            if(item.getFood().isParty()) {
                try {
                    Food food = item.getFood();
                    partyMapper.decrease(food.getId(), item.getNumber());
                } catch (SQLException e) {
                    throw e;
                }
            }
        }
    }

    public Order finalizeCart(String userId) throws SQLException {
        PartyMapper partyMapper = PartyMapper.getInstance();
        try {
            Order order = getCart(userId);
            if(order.getId() == null) {
                throw new ForbiddenException("there is no order to finalize");
            }
            for(OrderItem item: order.getOrderItems()) {
                if(item.getFood().isParty()) {
                    Food food = item.getFood();
                    PartyFood pfood = partyMapper.find(food.getId());
                    if(pfood.isExpired())
                        throw new ForbiddenException("The food" + " has been expired.");
                    if(pfood.getCount() < item.getNumber())
                        throw new ForbiddenException("The food is just " + String.valueOf(pfood.getCount()) + " available.");
                }
            }
            int price = order.getFinalPrice();
            UserMapper userMapper = UserMapper.getInstance();
            int credit = userMapper.find(userId).getCredit();
            if(credit < price)
                throw new ForbiddenException("You do not have enough credit to purchase.");
            userMapper.addCredit(userId, -price);
            handleFinalize(userId, order);
            return order;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    protected String getFindAllStatement() {
        String query = "SELECT * FROM orders WHERE userId = ? AND state <> 'nf';";
        return query;
    }

    public ArrayList<Order> findAll(String userId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindAllStatement());
        st.setString(1, userId);

        try {
            ResultSet rs = st.executeQuery();
            if(rs.isClosed()) {
                st.close();
                con.close();
                return new ArrayList<Order>();
            }
            ArrayList<Order> result = getDAOList(rs);
            st.close();
            con.close();
            return result;
        } catch(SQLException e) {
            st.close();
            con.close();
            throw e;
        }
    }
}
