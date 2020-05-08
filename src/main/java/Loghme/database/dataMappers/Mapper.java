package Loghme.database.dataMappers;

import Loghme.database.ConnectionPool;
import Loghme.entities.Order;
import Loghme.entities.Restaurant;
import org.springframework.http.converter.json.GsonBuilderUtils;

import javax.crypto.spec.PSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Mapper<T, I> implements IMapper<T, I> {

    protected Map<I, T> loadedMap = new HashMap<I, T>();

    //insert
    abstract protected String getInsertStatement();
    abstract protected void fillInsertValues(PreparedStatement st, T obj) throws SQLException;

    //find
    abstract protected String getFindStatement(I id);
    abstract protected String getFindAllStatement() throws SQLException;

    //delete
    abstract protected String getDeleteStatement(I id);

    //DAO
    abstract protected T getDAO(ResultSet rs) throws SQLException;
    abstract protected ArrayList<T> getDAOList(ResultSet rs) throws SQLException;


    public T find(I id) throws SQLException {

        T result = loadedMap.get(id);
        if (result != null)
            return result;

        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement st = con.prepareStatement(getFindStatement(id))
        ) {
            ResultSet resultSet;
            try {
                resultSet = st.executeQuery();
                if(!resultSet.next())
                    return null;
                return getDAO(resultSet);
            } catch (SQLException ex) {
                throw ex;
            }
        }
    }

    public boolean insert(T obj) throws SQLException {
        boolean result;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getInsertStatement());
        fillInsertValues(st, obj);
        try {
            result = st.execute();
            st.close();
            con.close();
            return result;
        } catch (SQLException e) {
            System.out.println("error in Mapper.insert query.");
            st.close();
            con.close();
            throw e;
        }
    }

    public ArrayList<T> findAll() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindAllStatement());
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet.isClosed()) {
                st.close();
                con.close();
                return new ArrayList<T>();
            }
            ArrayList<T> result = getDAOList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (SQLException e) {
            System.out.println("error in Mapper.findAll query.");
            st.close();
            con.close();
            throw e;
        }
    }

    public void delete(I id) throws SQLException {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement st = con.prepareStatement(getDeleteStatement(id))
        ) {
            try {
                st.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("error in Mapper.delete query.");
                throw ex;
            }
        }
    }
}