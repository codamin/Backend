package Loghme.DTOs;

import Loghme.entities.Food;
import Loghme.entities.Location;
import Loghme.entities.PartyFood;
import Loghme.entities.Restaurant;

import java.util.ArrayList;

public class RestaurantDTO {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String id;
    private String name;
    private Location location;
    private String logo;
    private String description;

    public RestaurantDTO(Restaurant r) {
        id = r.getId();
        name = r.getName();
        location = r.getLocation();
        logo = r.getLogo();
        description = r.getDescription();
    }

}
