package Loghme.entities;

import java.util.ArrayList;

public class Restaurant {
    private String id;
    private String name;
    private Location location;
    private String logo;
    private ArrayList<Food> menu;
    private ArrayList<PartyFood> partyMenu;
    private String description;

    public Restaurant() {
        id = "";
        name = "";
        location = new Location();
        logo = "";
        menu = new ArrayList<Food>();
        partyMenu = new ArrayList<PartyFood>();
        description = "";
    }

    public Restaurant(String id, String name, Location location, String logo, ArrayList<Food> menu, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.logo = logo;
        this.menu = menu;
        this.description = description;
    }

    public ArrayList<PartyFood> getPartyMenu() {
        return partyMenu;
    }

    public void setPartyMenu(ArrayList<PartyFood> partyMenu) {
        this.partyMenu = partyMenu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRestaurantIds() {
        if(menu != null)
            for(Food food: menu)
                food.setRestaurantId(id);

        if(partyMenu != null)
            for(PartyFood food: partyMenu)
                food.setRestaurantId(id);
    }

    public void setRestaurantNames() {
        if(menu != null)
            for(Food food: menu)
                food.setRestaurantName(name);

        if(partyMenu != null)
            for(PartyFood food: partyMenu)
                food.setRestaurantName(name);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Food> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Food> menu) {
        this.menu = menu;
    }

    public boolean addFood(Food newFood) {
        for (Food food : menu) {
            if (food.getName().equals(newFood.getName())) {
                return false;
            }
        }
        menu.add(newFood);
        return true;
    }

    public float score() {
        float sumPopularity = 0;
        for (Food food : menu) {
            sumPopularity = sumPopularity + food.getPopularity();
        }
        float distance = location.getDistance(new Location(0, 0));
        return (sumPopularity / menu.size()) / distance;
    }

    public Food findFood(String name) {
        if(menu != null)
            for (Food food : menu)
                if (food.getName().equals(name))
                    return food;

        if(partyMenu == null)
            return null;

        for(PartyFood food: partyMenu)
            if(food.getName().equals(name))
                return food;
        return null;
    }

    public void setFoodsRstaurantName() {
        for (Food food : menu)
            food.setRestaurantName(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void updateParty(ArrayList<PartyFood> newParty) {
        for(PartyFood newFood: newParty) {
            boolean found = false;
            for(PartyFood food: partyMenu) {
                if(food.getName() == newFood.getName()) {
                    food = newFood;
                    food.setAvailable(true);
                    found = true;
                    continue;
                }
            }
            if(!found) {
                partyMenu.add(newFood);
            }
        }
    }

    public void outDatePartyMenu() {
        for(PartyFood food: partyMenu)
            food.setAvailable(false);
    }
}