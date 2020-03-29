package Loghme.models;


public class PartyFood extends Food {
    private int count;
    private int oldPrice;


    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    @Override
    public void decrement() {count -= 1; }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean isAvailable() {return count > 0;}

}