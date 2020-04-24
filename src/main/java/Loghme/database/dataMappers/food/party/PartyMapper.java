package Loghme.database.dataMappers.food.party;

import Loghme.database.ConnectionPool;
import Loghme.database.dataMappers.Mapper;
import Loghme.database.dataMappers.food.FoodMapper;
import Loghme.entities.Food;
import Loghme.entities.PartyFood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class PartyMapper extends Mapper<PartyFood, Integer> implements IPartyMapper {
    private static PartyMapper instance;
    static {
        try {
            instance = new PartyMapper();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public static PartyMapper getInstance() {
        return instance;
    }
    private PartyMapper() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS party (\n" +
                "id INTEGER NOT NULL,\n" +
                "oldPrice INTEGER NOT NULL,\n" +
                "count INTEGER NOT NULL,\n" +
                "expired BOOLEAN NOT NULL,\n" +
                "PRIMARY KEY(id),\n" +
                "FOREIGN KEY(id) REFERENCES food(id));";
        System.out.println(query);
        PreparedStatement st = con.prepareStatement(query);
        st.executeUpdate();
        st.close();
        con.close();
    }

    @Override
    protected String getInsertStatement() {
        return null;
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, PartyFood obj) throws SQLException {

    }

    @Override
    protected String getFindStatement(Integer id) {
        return null;
    }

    @Override
    protected String getFindAllStatement() throws SQLException {
        String query = "SELECT * FROM party WHERE expired = False;";
        System.out.println(query);
        return query;
    }

    @Override
    protected String getDeleteStatement(Integer id) {
        return null;
    }

    @Override
    protected PartyFood getDAO(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int oldPrice = rs.getInt(2);
        int count = rs.getInt(3);
        boolean expired = rs.getBoolean(4);
        FoodMapper foodMapper = FoodMapper.getInstance();
        Food food = foodMapper.find(id);
        PartyFood resp = new PartyFood(id, food.getName(), food.getDescription(), food.getPopularity(), food.getPrice(), food.getImage(), food.getRestaurantId(), true, count, oldPrice);
        return resp;
    }

    @Override
    protected ArrayList<PartyFood> getDAOList(ResultSet rs) throws SQLException {
        ArrayList<PartyFood> resp = new ArrayList<PartyFood>();
        while(rs.next()) {
            resp.add(getDAO(rs));
        }
        return resp;
    }

    private String getExpireAllStatement() {
        String query = "UPDATE IGNORE party SET expired = True WHERE expired = False;";
        System.out.println(query);
        return query;
    }
    public void expireAll() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getExpireAllStatement());
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
    }

    private String getInsertStatement(int foodId, int oldPrice, int count) {
        String query = "INSERT IGNORE INTO party (id, oldPrice, count, expired)\n" +
                "VALUES(" + String.valueOf(foodId) + "," + String.valueOf(oldPrice) + "," + String.valueOf(count) + ",False);";
        System.out.println(query);
        return query;
    }
    public boolean insert(int foodId, int oldPrice, int count) throws SQLException {
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement(foodId, oldPrice, count));
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

    public ArrayList<PartyFood> findAll() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindAllStatement());
        try {
            ResultSet rs = st.executeQuery();
            if(rs.isClosed()) {
                st.close();
                con.close();
                return null;
            }
            ArrayList<PartyFood> result = getDAOList(rs);
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
