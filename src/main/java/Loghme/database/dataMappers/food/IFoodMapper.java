package Loghme.database.dataMappers.food;

import Loghme.database.dataMappers.IMapper;
import Loghme.entities.Food;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IFoodMapper extends IMapper<Food, Integer> {
    ArrayList<Food> findAll(String restaurantId) throws SQLException;
    boolean insert(Food food) throws SQLException;
}
