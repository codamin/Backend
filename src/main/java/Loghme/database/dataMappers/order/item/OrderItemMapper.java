package Loghme.database.dataMappers.order.item;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.order.IOrderMapper;
import Loghme.database.dataMappers.restaurant.RestaurantMapper;
import Loghme.entities.Order;
import Loghme.entities.OrderItem;

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
        return null;
    }

    @Override
    protected ArrayList<OrderItem> getDAOList(ResultSet rs) throws SQLException {
        return null;
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
}
