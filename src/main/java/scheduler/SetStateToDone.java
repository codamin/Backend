package scheduler;

import repository.Order;

import java.util.TimerTask;

public class SetStateToDone extends TimerTask {

    Order order;

    public SetStateToDone(Order order) {
        this.order = order;
    }

    @Override
    public void run() {
        System.out.println("food delivered at time " + System.currentTimeMillis() / 1000);
        order.setState("done");
    }
}
