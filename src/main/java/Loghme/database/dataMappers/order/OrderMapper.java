package Loghme.database.dataMappers.order;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.entities.Order;
import Loghme.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper extends Mapper<Order, Integer> implements IOrderMapper {

    private static OrderMapper instance;

    static {
        try {
            instance = new OrderMapper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private OrderMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS order (" +
                "id INTEGER NOT NULL AUTO_INCREMENT" +
                "userId VARCHAR(30)," +
                "restaurantId VARCHAR(200)," +
                "state VARCHAR(20)," +
                "remMin INTEGER," +
                "remSec INTEGER," +
                "PRIMARY KEY(id)," +
                "FOREIGN KEY(userId) REFERENCES user," +
                "FOREIGN KEY(restaurantId) REFERENCES restaurant" +
                ");";

        PreparedStatement createTableStatement = con.prepareStatement(query);
        System.out.println(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    protected void fillInsertValues(PreparedStatement st, Order order) throws SQLException {
//        st.setString(1, order.);
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
        return "INSERT INTO order(userId, restaurantId, state, remMin, remSec) VALUES(?,?,?,?,?);";
    }

    @Override
    protected String getDeleteStatement(Integer id) {
        return null;
    }

    @Override
    protected Order convertResultSetToObject(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public boolean insert(Order order) throws SQLException {
        return false;
    }
}
