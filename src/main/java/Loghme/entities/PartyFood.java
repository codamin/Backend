package Loghme.entities;


public class PartyFood extends Food {
    private int count;
    private int oldPrice;

    public PartyFood(int id, String name, String description, float popularity, int price, String image, String restaurantId, boolean available) {
        super(id, name, description, popularity, price, image, restaurantId, available);
    }

    public PartyFood(int id, String name, String description, float popularity, int price, String image,
                     String restaurantId, boolean available, int count, int oldPrice) {
        super(id, name, description, popularity, price, image, restaurantId, available);
        this.count = count;
        this.oldPrice = oldPrice;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    @Override
    public void decrement() {count -= 1; }

    @Override
    public void increment() {
        count += 1;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean isAvailable() {return count > 0;}

}