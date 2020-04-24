//package Loghme.database.dataMappers.partyFood;
//
//import Loghme.database.ConnectionPool;
//import Loghme.database.dataMappers.Mapper;
//import Loghme.entities.PartyFood;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//public class PartyFoodMapper extends Mapper<PartyFood, Integer> implements IPartyFoodMapper {
//
//    private static PartyFoodMapper instance;
//
//    static {
//        try {
//            instance = new PartyFoodMapper();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static PartyFoodMapper getInstance() {
//        return instance;
//    }
//
//    private PartyFoodMapper() throws SQLException {
//        Connection con = ConnectionPool.getConnection();
//        String query = "CREATE TABLE IF NOT EXISTS partyFood (" +
//                "id INTEGER NOT NULL AUTO_INCREMENT," +
//                "name VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
//                "description VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci," +
//                "popularity INTEGER," +
//                "price INTEGER," +
//                "image VARCHAR(200)," +
//                "restaurantId VARCHAR(24)," +
//                "available INTEGER," +
//                "count INTEGER," +
//                "oldPrice INTEGER," +
//                "PRIMARY KEY(id)," +
//                "FOREIGN KEY(restaurantId) REFERENCES restaurant(id)" +
//                ");";
//        PreparedStatement createTableStatement = con.prepareStatement(query);
//        System.out.println(query);
//        createTableStatement.executeUpdate();
//        createTableStatement.close();
//        con.close();
//    }
//
//    @Override
//    protected String getFindStatement(Integer id) {
//        return "SELECT * " +
//                "FROM partyFood f" +
//                "WHERE f.id = " + id.toString();
//    }
//
//    @Override
//    protected String getFindAllStatement() throws SQLException {
//        return "SELECT * " +
//                "FROM partyFood f " +
//                "WHERE f.restaurantId = ?";
//    }
//
//    @Override
//    protected String getInsertStatement() {
//        return "INSERT IGNORE INTO partyFood(id,name,description,popularity,price,image,restaurantId,available,count,oldPrice)" +
//                " VALUES(DEFAULT,?,?,?,?,?,?,?,?,?)";
//    }
//
//    @Override
//    protected void fillInsertValues(PreparedStatement st, PartyFood partyFood) throws SQLException {
//        st.setString(1, partyFood.getName());
//        st.setString(2, partyFood.getDescription());
//        st.setFloat(3, partyFood.getPopularity());
//        st.setInt(4, partyFood.getPrice());
//        st.setString(5,partyFood.getImage());
//        st.setString(6, partyFood.getRestaurantId());
//        st.setBoolean(7, partyFood.getAvailable());
//        st.setInt(8, partyFood.getCount());
//        st.setInt(9, partyFood.getOldPrice());
//    }
//
//    @Override
//    protected String getDeleteStatement(Integer id) {
//        return null;
//    }
//
//    @Override
//    protected PartyFood getDAO(ResultSet st) throws SQLException {
//        int id = st.getInt(1);
//        String name = st.getString(2);
//        String description = st.getString(3);
//        int popularity = st.getInt(4);
//        int price = st.getInt(5);
//        String image = st.getString(6);
//        String restaurantId = st.getString(7);
//        boolean available = st.getBoolean(8);
//        int count = st.getInt(9);
//        int oldPrice = st.getInt(10);
//        return new PartyFood(id, name, description, popularity, price, image, restaurantId, available, count, oldPrice, );
//    }
//
//    @Override
//    protected ArrayList getDAOList(ResultSet rs) throws SQLException {
//        ArrayList<PartyFood> partyFoods = new ArrayList<>();
//        while (rs.next()){
//            partyFoods.add(this.getDAO(rs));
//        }
//        return partyFoods;
//    }
//
//    @Override
//    public ArrayList<PartyFood> findAll(String restaurantId) throws SQLException {
//        Connection con = ConnectionPool.getConnection();
//        PreparedStatement st = con.prepareStatement(getFindAllStatement());
//        st.setString(1, restaurantId);
//        try {
//            ResultSet resultSet = st.executeQuery();
//            if (resultSet.isClosed()) {
//                st.close();
//                con.close();
//                return new ArrayList<PartyFood>();
//            }
//            ArrayList<PartyFood> result = getDAOList(resultSet);
//            st.close();
//            con.close();
//            return result;
//        } catch (SQLException e) {
//            System.out.println("error in PartyFoodMapper.findAll query.");
//            st.close();
//            con.close();
//            throw e;
//        }
//    }
//
//    @Override
//    public String getCancelAllStatement() throws SQLException {
//        return "UPDATE partyFood" +
//                "SET available = 0";
//    }
//
//    @Override
//    public void cancelAll() throws SQLException {
//        Connection con = ConnectionPool.getConnection();
//        PreparedStatement st = con.prepareStatement(getCancelAllStatement());
//
//        try {
//            st.executeUpdate();
//            st.close();
//            con.close();
//        } catch (SQLException e) {
//            System.out.println("error in FoodMapper: cancel all query.");
//            st.close();
//            con.close();
//            throw e;
//        }
//    }
//}
