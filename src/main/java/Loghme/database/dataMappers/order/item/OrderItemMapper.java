package Loghme.database.dataMappers.order.item;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.food.FoodMapper;
import Loghme.database.dataMappers.food.party.PartyMapper;
import Loghme.database.dataMappers.order.IOrderMapper;
import Loghme.database.dataMappers.order.OrderMapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Food;
import Loghme.entities.Order;
import Loghme.entities.OrderItem;
import Loghme.entities.PartyFood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderItemMapper extends Mapper<OrderItem, Integer> implements IOrderMapper {

    private static OrderItemMapper instance;

    static {
        try {
            instance = new OrderItemMapper();
        } catch (SQLException e) {
            System.out.println("error occured in getting instance of OrderItemMapper");
        }
    }

    public static OrderItemMapper getInstance() {
        return instance;
    }

    private OrderItemMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS orderItem (" +
                "orderId INTEGER," +
                "foodId INTEGER," +
                "num INTEGER NOT NULL," +
                "FOREIGN KEY(orderId) REFERENCES orders(id)," +
                "FOREIGN KEY(foodId) REFERENCES food(id)" +
                ");";
        PreparedStatement createTableStatement = con.prepareStatement(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    private String getDeleteStatement(int orderId, int foodId) {
        String query = "DELETE FROM orderItem WHERE orderId = " + String.valueOf(orderId) + " AND foodId = " + String.valueOf(foodId) + ";";
        return query;
    }

    private boolean delete(int orderId, int foodId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getDeleteStatement(orderId, foodId));
        try {
            int result = st.executeUpdate();
            if(result <= 0) {
                con.close();
                st.close();
                return false;
            }
            st.close();
            con.close();
            ArrayList<OrderItem> items = findAll(orderId);
            if(items.size() > 0) {
                return true;
            }
            OrderMapper orderMapper = OrderMapper.getInstance();
            orderMapper.delete(orderId);
            return true;
        } catch (SQLException e) {
            st.close();
            con.close();
            throw e;
        }
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
        return "INSERT INTO orderItem(id, name, location_x, location_y, logo) VALUES(?,?,?,?,?)";
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, OrderItem obj) throws SQLException {

    }

    @Override
    protected String getDeleteStatement(Integer id) {
        return null;
    }

    @Override
    protected OrderItem getDAO(ResultSet rs) throws SQLException {
        int orderId = rs.getInt(1);
        int foodId = rs.getInt(2);
        int num = rs.getInt(3);

        FoodMapper foodMapper = FoodMapper.getInstance();
        Food food = foodMapper.find(foodId);
        if(food.isParty()) {
            food.setParty(true);
            return new OrderItem(orderId, foodId, num, food);
        }
        else {
            OrderItem item = new OrderItem(orderId, foodId, num, food);
            return item;
        }
    }

    @Override
    protected ArrayList<OrderItem> getDAOList(ResultSet rs) throws SQLException {
        ArrayList<OrderItem> resp = new ArrayList<OrderItem>();
        while(rs.next()) {
            resp.add(getDAO(rs));
        }
        return resp;
    }

    //    @Override
    protected OrderItem convertResultSetToObject(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public boolean insert(OrderItem orderItem) throws SQLException {
        boolean result = false;
//        Connection con = ConnectionPool.getConnection();
//        PreparedStatement st = con.prepareStatement(getInsertStatement());
//        fillInsertValues(st, orderItem);
//        result = st.execute();
//        st.close();
//        con.close();
        return result;
    }

    private String getFindStatement(int orderId, int foodId) {
        String query = "SELECT * FROM orderItem WHERE\n" +
                "orderId = " + String.valueOf(orderId) + " AND foodId = " + String.valueOf(foodId) + ";";
        return query;
    }

    private OrderItem find(int orderId, int foodId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindStatement(orderId, foodId));
        try {
            ResultSet rs = st.executeQuery();
            if(rs.isClosed()) {
                st.close();
                con.close();
                return null;
            }
            ArrayList<OrderItem> items = getDAOList(rs);
            st.close();
            con.close();
            if(items.size() == 0)
                return null;
            else
                return items.get(0);
        } catch (SQLException e) {
            st.close();
            con.close();
            throw e;
        }
    }

    private String getInsertStatement(int orderId, int foodId, int number) {
        String query = "INSERT INTO orderItem(orderId, foodId, num)VALUES(" + String.valueOf(orderId) + "," + String.valueOf(foodId) + "," + String.valueOf(number) + ");";
        return query;
    }

    private String getChangeAmountStatement(int orderId, int foodId, int number) {
        String query = "UPDATE orderItem SET num = num + "
                + String.valueOf(number) + " WHERE orderId = "
                + String.valueOf(orderId) + " AND foodId = "
                + String.valueOf(foodId) + ";";
        return query;
    }

    private String getGetNubmerStatement(int orderId, int foodId) {
        String query = "SELECT num FROM orderItem WHERE orderId = " + String.valueOf(orderId) + " AND foodId = " + String.valueOf(foodId) + ";";
        return query;
    }

    private int getNumber(int orderId, int foodId) throws SQLException {
        int resp = 0;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getGetNubmerStatement(orderId, foodId));
        try {
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                resp = rs.getInt(1);
                st.close();
                con.close();
                return resp;
            }
            else {
                st.close();
                con.close();
                return 0;
            }
        } catch (SQLException e) {
            st.close();
            con.close();
            throw e;
        }
    }

    private boolean handleDeleteFromCart(int orderId, int foodId) throws SQLException {
        int num = getNumber(orderId, foodId);
        if(num > 0)
            return true;
        boolean result = delete(orderId, foodId);
        return true;
    }

    private boolean changeNumber(int orderId, int foodId, int number) throws SQLException {
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getChangeAmountStatement(orderId, foodId, number));
        try {
            result = st.execute();
            st.close();
            con.close();
            if(number < 0) {
                handleDeleteFromCart(orderId, foodId);
            }
            return result;
        } catch(SQLException e) {
            st.close();
            con.close();
            throw e;
        }
    }

    public boolean insert(int orderId, int foodId, int number) throws SQLException {
        boolean result;
        OrderItem item = find(orderId, foodId);
        if(item != null) {
            result = changeNumber(orderId, foodId, number);
            return result;
        }
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement(orderId, foodId, number));
        try {
            result = st.execute();
            st.close();
            con.close();
            return result;
        } catch (SQLException e) {
            st.close();
            con.close();
            throw e;
        }
    }

    private String getFindAllStatement(int orderId) {
        String query = "SELECT * FROM orderItem WHERE orderId = " + String.valueOf(orderId) + ";";
        return query;
    }

    public ArrayList<OrderItem> findAll(int orderId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindAllStatement(orderId));
        try {
            ResultSet rs = st.executeQuery();
            if(rs.isClosed()) {
                st.close();
                con.close();
                return new ArrayList<OrderItem>();
            }
            ArrayList<OrderItem> items = getDAOList(rs);
            st.close();
            con.close();
            return items;
        } catch (SQLException e) {
            st.close();
            con.close();
            throw e;
        }
    }
}
