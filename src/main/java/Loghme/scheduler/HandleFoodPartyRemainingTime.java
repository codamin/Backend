package Loghme.scheduler;
import Loghme.entities.FoodPartyTimer;
import java.util.TimerTask;

public class HandleFoodPartyRemainingTime extends TimerTask {
    @Override
    public void run() {
        System.out.println(FoodPartyTimer.getInstance().getRemainingTime());
        FoodPartyTimer.getInstance().decrementRemainingTime();
        if (FoodPartyTimer.getInstance().getRemainingTime() == 0)
            FoodPartyTimer.getInstance().resetRemainingTime();
    }
}