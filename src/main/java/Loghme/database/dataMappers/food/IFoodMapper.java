package Loghme.database.dataMappers.food;

import Loghme.database.dataMappers.IMapper;
import Loghme.entities.Food;

import java.sql.SQLException;

public interface IFoodMapper extends IMapper<Food, Integer> {
    boolean insert(Food food) throws SQLException;
}
