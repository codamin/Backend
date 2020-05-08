package Loghme.database.dataMappers.user;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.order.OrderMapper;
import Loghme.entities.Order;
import Loghme.entities.Restaurant;
import Loghme.entities.User;
import org.apache.commons.codec.digest.DigestUtils;

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
                "password VARCHAR(585)," +
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
        return "INSERT IGNORE INTO user(firstname, lastname, phone, email, password, credit, location_x, location_y) VALUES(?,?,?,?,?,?,?,?);";
    }

    protected void fillInsertValues(PreparedStatement st, User user) throws SQLException {
        st.setString(1, user.getFirstName());
        st.setString(2, user.getLastName());
        st.setString(3, user.getPhone());
        st.setString(4, user.getEmail());
        st.setString(5, user.getPassword());
        st.setInt(6, user.getCredit());
        st.setInt(7, 0);
        st.setInt(8, 0);
    }

    @Override
    protected String getFindStatement(String id) {
        return null;
    }

    private String getFindStatement() {
        String query = "SELECT * FROM user WHERE email = ?;";
        return query;
    }

    private void fillFindValues(PreparedStatement st, String id) throws SQLException {
        st.setString(1, id);
    }

    @Override
    protected String getDeleteStatement(String id) {
        return null;
    }


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

    protected String getAddCreditStatment() {
        String query = "UPDATE IGNORE user SET credit = credit + ? WHERE email = ?;";
        return query;
    }

    private void fillAddCreditStatement(PreparedStatement st, String id, Integer amount) throws SQLException {
        st.setInt(1, amount);
        st.setString(2, id);
    }

    public void addCredit(String id, int amount) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getAddCreditStatment());
        fillAddCreditStatement(st, id, amount);
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

    public User find(String email) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindStatement());
        fillFindValues(st, email);
        try {
            ResultSet rs = st.executeQuery();
            if(rs.isClosed()) {
                st.close();
                con.close();
                return null;
            }
            if(!rs.next()) {
                st.close();
                con.close();
                return null;
            }
            User user = getDAO(rs);
            st.close();
            con.close();
            return user;
        } catch (SQLException e) {
            System.out.println("error occured in finding the user with email");
            st.close();
            con.close();
            throw e;
        }
    }
}
