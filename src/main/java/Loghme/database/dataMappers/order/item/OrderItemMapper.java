package Loghme.database.dataMappers.order.item;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.order.IOrderMapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Order;
import Loghme.entities.OrderItem;
//import com.sun.org.apache.xalan.internal.xsltc.trax.XSLTCSource;

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
            e.printStackTrace();
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
        System.out.println(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
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
        System.out.println("add one dao");
        int orderId = rs.getInt(1);
        int foodId = rs.getInt(2);
        int num = rs.getInt(3);
        OrderItem item = new OrderItem(orderId, foodId, num);
        return item;
    }

    @Override
    protected ArrayList<OrderItem> getDAOList(ResultSet rs) throws SQLException {
        System.out.println("get dao list");
        ArrayList<OrderItem> resp = new ArrayList<OrderItem>();
        System.out.println("before while");
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

//    private void fillInsertValues(PreparedStatement st, OrderItem orderItem) {
//    }

    private String getFindStatement(int orderId, int foodId) {
        String query = "SELECT * FROM orderItem WHERE\n" +
                "orderId = " + String.valueOf(orderId) + " AND foodId = " + String.valueOf(foodId) + ";";
        System.out.println(query);
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
            e.printStackTrace();
            throw e;
        }
    }

    private String getInsertStatement(int orderId, int foodId, int number) {
        String query = "INSERT INTO orderItem(orderId, foodId, num)VALUES(" + String.valueOf(orderId) + "," + String.valueOf(foodId) + "," + String.valueOf(number) + ");";
        System.out.println(query);
        return query;
    }

    private String getChangeAmountStatement(int orderId, int foodId, int number) {
        String query = "UPDATE orderItem SET num = num + "
                + String.valueOf(number) + " WHERE orderId = "
                + String.valueOf(orderId) + " AND foodId = "
                + String.valueOf(foodId) + ";";
        System.out.println(query);
        return query;
    }

    private boolean changeNumber(int orderId, int foodId, int number) throws SQLException {
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getChangeAmountStatement(orderId, foodId, number));
        try {
            result = st.execute();
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

    public boolean insert(int orderId, int foodId, int number) throws SQLException {
        boolean result;
        OrderItem item = find(orderId, foodId);
        if(item != null) {
            result = changeNumber(orderId, foodId, number);
            return result;
        }
        System.out.println("run not null");
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
            e.printStackTrace();
            throw e;
        }
    }

    private String getFindAllStatement(int orderId) {
        String query = "SELECT * FROM orderItem WHERE orderId = " + String.valueOf(orderId) + ";";
        System.out.println("query");
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
            e.printStackTrace();
            throw e;
        }
    }
}
