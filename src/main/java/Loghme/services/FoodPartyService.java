package Loghme.services;

//import Loghme.DTOs.FoodPartyTimerDTO;
import Loghme.DTOs.FoodPartyTimerDTO;
import Loghme.database.dataMappers.partyFood.PartyFoodMapper;
import Loghme.entities.FoodPartyTimer;
import Loghme.entities.PartyFood;
import Loghme.scheduler.HandleFoodPartyRemainingTime;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@Configuration
@EnableScheduling
public class FoodPartyService {
    private static FoodPartyTimer foodPartyTimer = FoodPartyTimer.getInstance();

    public static ArrayList<PartyFood> getFoodPartyFoods() throws SQLException {
        ArrayList<PartyFood> result = PartyFoodMapper.getInstance().findAll();
        return result;
    }

    public static FoodPartyTimerDTO getRemainingTime() {
        return new FoodPartyTimerDTO(foodPartyTimer);
}

    @Scheduled(fixedDelay = 1000)
    public void decrementRemainingTime() {
        foodPartyTimer.decrementRemainingTime();
    }
}
