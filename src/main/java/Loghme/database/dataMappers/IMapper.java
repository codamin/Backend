package Loghme.database.dataMappers;

import java.sql.SQLException;

public interface IMapper<T, I> {
    T find(I id) throws SQLException;
    boolean insert(T data) throws SQLException;
    void delete(I id) throws SQLException;
}
