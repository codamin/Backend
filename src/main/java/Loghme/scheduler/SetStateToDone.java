package Loghme.scheduler;

import Loghme.database.dataMappers.order.OrderMapper;
import Loghme.entities.Order;

import java.sql.SQLException;
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
        try {
            OrderMapper.getInstance().setState(order.getId(), "done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.cancel();
        cancel();
    }
}
