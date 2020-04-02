package Loghme.models;

import Loghme.scheduler.HandleFoodPartyRemainingTime;

import java.util.Timer;
import java.util.TimerTask;

public class FoodPartyTimer {
    private static int remainingTime;
    private static int updatePeriod;
    private static FoodPartyTimer instance;

    public static FoodPartyTimer getInstance() {
        if (instance == null)
            instance = new FoodPartyTimer();
        return instance;
    }
    public FoodPartyTimer() {
        updatePeriod = 20;
        remainingTime = 20;
    }
    public static int getUpdatePeriod() {
        return updatePeriod;
    }
    public static int getRemainingTime() {
        return remainingTime;
    }
    public static void resetRemainingTime() {
        remainingTime = updatePeriod;
    }
    public static void decrementRemainingTime() {
        remainingTime--;
    }
    public static void startTimer() {
        TimerTask handleFoodPartyRemainingTime = new HandleFoodPartyRemainingTime();
        Timer remainingtimer = new Timer();
        remainingtimer.schedule(handleFoodPartyRemainingTime, 0, 1 * 1000); //every 300 secs
    }
}
