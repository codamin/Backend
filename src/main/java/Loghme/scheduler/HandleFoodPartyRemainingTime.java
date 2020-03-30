package Loghme.scheduler;
import Loghme.models.IeatRepository;
import java.util.TimerTask;

public class HandleFoodPartyRemainingTime extends TimerTask {
    @Override
    public void run() {
        System.out.println(IeatRepository.getRemainingTime());
        IeatRepository.decrementRemainingTime();
        if (IeatRepository.getRemainingTime() == 0)
            IeatRepository.resetRemainingTime();
    }
}