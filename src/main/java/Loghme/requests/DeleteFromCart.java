package Loghme.requests;

import org.springframework.web.bind.annotation.DeleteMapping;

public class DeleteFromCart {
    private String restaurantId;
    private String foodName;

    public DeleteFromCart() {}


    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
