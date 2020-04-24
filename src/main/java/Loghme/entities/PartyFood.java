package Loghme.entities;


public class PartyFood extends Food {
    private int count;
    private int oldPrice;

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    private boolean expired;

    public PartyFood() {}

    public PartyFood(int id, String name, String description, float popularity, int price, String image, String restaurantId, String restaurantName, boolean party) {
        super(id, name, description, popularity, price, image, restaurantId, restaurantName, party);
    }

    public PartyFood(int id, String name, String description, float popularity, int price, String image,
                     String restaurantId, String restaurantName, boolean party, int count, int oldPrice, boolean expired) {
        super(id, name, description, popularity, price, image, restaurantId, restaurantName, party);
        this.count = count;
        this.oldPrice = oldPrice;
        this.expired = expired;
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

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean isAvailable() {return count > 0;}

    @Override
    public boolean isParty() {
        return true;
    }

}