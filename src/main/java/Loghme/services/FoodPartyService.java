package Loghme.services;

import Loghme.database.dataMappers.partyFood.PartyFoodMapper;
import Loghme.entities.PartyFood;

import java.sql.SQLException;
import java.util.ArrayList;

public class FoodPartyService {
    public static ArrayList<PartyFood> getFoodPartyFoods() throws SQLException {
        ArrayList<PartyFood> result = PartyFoodMapper.getInstance().findAll();
        return result;
    }
}
