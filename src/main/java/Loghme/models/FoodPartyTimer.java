package Loghme.models;

import Loghme.scheduler.HandleFoodPartyRemainingTime;

import java.util.Timer;
import java.util.TimerTask;

public class FoodPartyTimer {
    private int remainingTime;
    private int updatePeriod;
    private static FoodPartyTimer instance;

    public static FoodPartyTimer getInstance() {
        if (instance == null)
            instance = new FoodPartyTimer();
        return instance;
    }
    public FoodPartyTimer() {
        updatePeriod = 60;
        remainingTime = 60;
    }
    public int getUpdatePeriod() {
        return updatePeriod;
    }
    public int getRemainingTime() {
        return remainingTime;
    }
    public void resetRemainingTime() {
        remainingTime = updatePeriod;
    }
    public void decrementRemainingTime() {
        remainingTime--;
    }
    public void startTimer() {
        TimerTask handleFoodPartyRemainingTime = new HandleFoodPartyRemainingTime();
        Timer remainingTimer = new Timer();
        remainingTimer.schedule(handleFoodPartyRemainingTime, 0, 1 * 1000); //every 300 secs
    }
}
