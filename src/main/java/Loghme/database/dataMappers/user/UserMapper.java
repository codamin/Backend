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
            e.printStackTrace();
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
                "phone VARCHAR(11)," +
                "email VARCHAR(30)," +
                "credit INTEGER," +
                "PRIMARY KEY(email));";

        PreparedStatement createTableStatement = con.prepareStatement(query);
        // System.out.println(query);
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
        return "INSERT INTO user(firstname, lastname, phone, email, credit) VALUES(?,?,?,?,?);";
    }

    protected void fillInsertValues(PreparedStatement st, User user) throws SQLException {
        st.setString(1, user.getFirstName());
        st.setString(2, user.getLastName());
        st.setString(3, user.getPhone());
        st.setString(4, user.getEmail());
        st.setInt(5, user.getCredit());
    }

    @Override
    protected String getFindStatement(String id) {
        String query = "SELECT * FROM user WHERE email = \"" + id + "\";";
        // System.out.println(query);
        return query;
    }

    @Override
    protected String getDeleteStatement(String id) {
        return null;
    }

    protected User convertResultSetToObject(ResultSet rs) throws SQLException {
        String firstname = rs.getString(1);
        String lastname = rs.getString(2);
        String phone = rs.getString(3);
        String email = rs.getString(4);
        Integer credit = rs.getInt(5);
        User user = new User(firstname, lastname, phone, email, credit);
        return user;
    }

    @Override
    protected User getDAO(ResultSet rs) throws SQLException {
        String fname = rs.getString(1);
        String lname = rs.getString(2);
        String phone = rs.getString(3);
        String id = rs.getString(4);
        int credit = rs.getInt(5);
        User user = new User(fname, lname, phone, id, credit);
        OrderMapper orderMapper = OrderMapper.getInstance();
        System.out.println("before for in orderMapper  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        for(Order order: orderMapper.findAll(id)) {
            System.out.println("for step <<<<<<<<<<<<<<<<");
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
        // System.out.println(query);
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
            e.printStackTrace();
        }
    }

//    public
}
