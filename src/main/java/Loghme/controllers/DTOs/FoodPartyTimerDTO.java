package Loghme.controllers.DTOs;

import Loghme.entities.FoodPartyTimer;

public class FoodPartyTimerDTO {

    private int remainingTime;
    private int period;

    public FoodPartyTimerDTO(FoodPartyTimer fpt) {
        this.remainingTime = fpt.getRemainingTime();
        this.period = fpt.getUpdatePeriod();
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
