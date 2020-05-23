package Loghme.services;

import Loghme.controllers.DTOs.FoodPartyTimerDTO;
import Loghme.entities.FoodPartyTimer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class FoodPartyService {
    private static FoodPartyTimer foodPartyTimer = FoodPartyTimer.getInstance();

    public static FoodPartyTimerDTO getRemainingTime() {
        return new FoodPartyTimerDTO(foodPartyTimer);
}

    @Scheduled(fixedDelay = 1000)
    public void decrementRemainingTime() {
        foodPartyTimer.decrementRemainingTime();
    }
}
