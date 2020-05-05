package Loghme.database.dataMappers.user;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.order.OrderMapper;
import Loghme.entities.Order;
import Loghme.entities.Restaurant;
import Loghme.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserMapper extends Mapper<User, String> implements IUserMapper {

    private static UserMapper instance;

    static {
        try {
            instance = new UserMapper();
        } catch (SQLException e) {
            System.out.println("error occured in getting instance of UserMapper");
        }
    }

    public static UserMapper getInstance() {
        return instance;
    }

    private UserMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS user (" +
                "firstname VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
                "lastname VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
                "email VARCHAR(30)," +
                "password VARCHAR(50)," +
                "phone VARCHAR(11)," +
                "credit INTEGER," +
                "location_x INTEGER," +
                "location_y INTEGER," +
                "PRIMARY KEY(email));";

        PreparedStatement createTableStatement = con.prepareStatement(query);
        createTableStatement.executeUpdate();
        createTableStatement.close();
        con.close();
    }

    @Override
    public boolean insert(User user) throws SQLException {
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement());
        fillInsertValues(st, user);
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

    @Override
    protected String getFindAllStatement() throws SQLException {
        return null;
    }

    @Override
    protected String getInsertStatement() {
        return "INSERT INTO user(firstname, lastname, email, password, phone, credit, location_x, location_y) VALUES(?,?,?,?,?,?,?,?);";
    }

    protected void fillInsertValues(PreparedStatement st, User user) throws SQLException {
        st.setString(1, user.getFirstName());
        st.setString(2, user.getLastName());
        st.setString(3, user.getEmail());
        st.setString(4, user.getPassword());
        st.setString(5, user.getPhone());
        st.setInt(6, user.getCredit());
        st.setInt(7, 0);
        st.setInt(8, 0);
    }

    @Override
    protected String getFindStatement(String id) {
        String query = "SELECT * FROM user WHERE email = \"" + id + "\";";
        return query;
    }

    @Override
    protected String getDeleteStatement(String id) {
        return null;
    }

//    protected User convertResultSetToObject(ResultSet rs) throws SQLException {
//        String firstname = rs.getString(1);
//        String lastname = rs.getString(2);
//        String phone = rs.getString(3);
//        String email = rs.getString(4);
//        Integer credit = rs.getInt(5);
//        User user = new User(firstname, lastname, phone, email, credit);
//        return user;
//    }

    @Override
    protected User getDAO(ResultSet rs) throws SQLException {
        String fname = rs.getString(1);
        String lname = rs.getString(2);
        String id = rs.getString(3);
        String password = rs.getString(4);
        String phone = rs.getString(5);
        int credit = rs.getInt(6);
        User user = new User(fname, lname, id, password, phone, credit);
        OrderMapper orderMapper = OrderMapper.getInstance();
        for(Order order: orderMapper.findAll(id)) {
            user.getOrderRepository().addOrder(order);
        }
        return user;
    }

    @Override
    protected ArrayList<User> getDAOList(ResultSet rs) throws SQLException {
        return null;
    }

    protected String getAddCreditStatment(String id, Integer amount) {
        String query = "UPDATE IGNORE user SET credit = credit + " + String.valueOf(amount) + " WHERE email = " + String.format("'%s'", id) + ";";
        return query;
    }

    public void addCredit(String id, int amount) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getAddCreditStatment(id, amount));
        try {
            st.execute();
            st.close();
            con.close();
        } catch (Exception e) {
            st.close();
            con.close();
            throw e;
        }
    }
}
