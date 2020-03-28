package repository;

public class OrderItem {
    private Food food;
    private int number;

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
        return number * food.getPrice();
    }
}