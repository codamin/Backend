package Loghme.models;

public class Food {
    private String name;
    private String description;
    private float popularity;
    private int price;
    private String image;
    private String restaurantName;
    private String restaurantId;

    public void decrement() {
        System.out.println("failed dec called ;((");
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAvailable() {return true;}
}

