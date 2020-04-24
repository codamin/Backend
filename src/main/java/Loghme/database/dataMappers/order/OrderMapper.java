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
        String query = "DELETE FROM orders WHERE id = " + String.valueOf(id) + ";";
        System.out.println(query);
        return query;
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
        System.out.println("add new cart");
        int result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertCartStatement());
        fillInsertCartValues(st, userId, restaurantId);
        try {
            result = st.executeUpdate();
            System.out.println("here result");
            System.out.println(result);
            st.close();
            con.close();
            return result > 0;
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
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getCartStatment(userId));
        try {
            ResultSet rs = st.executeQuery();
            if(rs.isClosed()) {
                st.close();
                con.close();
                System.out.println("rs is closed on search for not finalized order");
                return new Order();
            }
            ArrayList<Order> orders = getDAOList(rs);
            st.close();
            con.close();
            if(orders.size() == 0) {
                System.out.println("Order Mapper found orders size 0");
                return new Order();
            }
            else {
                System.out.println("Order Mapper found orders size more than 0");
                return orders.get(0);
            }
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
            if(order.getId() == null) {
                System.out.println("cart found as null");
                boolean resp = insertCart(userId, restaurantId);
                System.out.println("our resp is ");
                System.out.println(resp);
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
            System.out.println("to insert ");
            System.out.println(order.getId());
            System.out.println(foodId);
            System.out.println(number);
            itemMapper.insert(order.getId(), foodId, number);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getSerStateStatement(String userId, String state) {
        String query = "UPDATE IGNORE orders SET state = " + String.format("'%s'", state) + " WHERE userId = " + String.format("'%s'", userId) + " AND state = " + String.format("'%s'", "nf") + ";";
        System.out.println(query);
        return query;
    }

    private void handleFinalize(String userId, Order order) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getSerStateStatement(userId, "finalized"));
        try {
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLException e) {
            st.close();
            con.close();
            e.printStackTrace();
            throw e;
        }
        PartyMapper partyMapper = PartyMapper.getInstance();
        for(OrderItem item: order.getOrderItems()) {
            if(item.getFood().isParty()) {
                try {
                    Food food = item.getFood();
                    partyMapper.decrease(food.getId(), item.getNumber());
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    public void finalizeCart(String userId) {
        PartyMapper partyMapper = PartyMapper.getInstance();
        try {
            Order order = getCart(userId);
            if(order.getId() == null) {
                throw new ForbiddenException("there is no order to finalize");
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>order mapper finalize cart before first for");
            for(OrderItem item: order.getOrderItems()) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>on first for");
                if(item.getFood().isParty()) {
                    System.out.println(item.getFood().isParty());
                    System.out.println("in if");
                    Food food = item.getFood();
                    PartyFood pfood = partyMapper.find(food.getId());
                    if(pfood.isExpired())
                        throw new ForbiddenException("The " + food.getName() + " has been expired.");
                    if(pfood.getCount() < item.getNumber())
                        throw new ForbiddenException(("The " + food.getName() + " is just " + String.valueOf(pfood.getCount()) + " available."));
                }
            }
            int price = order.getFinalPrice();
            UserMapper userMapper = UserMapper.getInstance();
            int credit = userMapper.find(userId).getCredit();
            if(credit < price)
                throw new ForbiddenException("You do not have enough credit to purchase.");
            userMapper.addCredit(userId, -price);
            handleFinalize(userId, order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getFindAllStatement(String userId) {
        String query = "SELECT * FROM orders WHERE userId = " + String.format("'%s'", userId) + " AND state <> 'nf';";
        System.out.println(query);
        return query;
    }

    public ArrayList<Order> findAll(String userId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindAllStatement(userId));
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
            e.printStackTrace();
            throw e;
        }
    }
}
