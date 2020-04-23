package Loghme.database.dataMappers.partyFood;

import Loghme.database.dataMappers.IMapper;
import Loghme.entities.PartyFood;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IPartyFoodMapper extends IMapper<PartyFood, Integer> {

    //find
    ArrayList<PartyFood> findAll(String restaurantId) throws SQLException;

    //insert
    boolean insert(PartyFood partyFood) throws SQLException;

    //cancell
    void cancelAll() throws SQLException;
    String getCancelAllStatement() throws SQLException;
}