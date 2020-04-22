package Loghme.entities;

public class OrderItem {
    private Food food;
    private int number;
    private int orderId;
    private int foodId;


    public OrderItem() {
        food = null;
        orderId = 0;
        foodId = 0;
        number = 0;
    }

    public OrderItem(int _orderId, int _foodId, int _number) {
        food = null;
        orderId = _orderId;
        foodId = _foodId;
        number = _number;

    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public OrderItem(Food food) {
        this.food = food;
        this.number = 1;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPrice() {
        if (food==null)
                return 0;
        return number * food.getPrice();
    }
}