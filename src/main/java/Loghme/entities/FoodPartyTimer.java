package Loghme.entities;

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
        updatePeriod = 300;
        remainingTime = 300;
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
        if(remainingTime == 0)
            remainingTime = updatePeriod;
        System.out.println(remainingTime);
    }
}
