package Loghme.controllers;

import Loghme.entities.FoodPartyTimer;
import Loghme.entities.IeatRepository;
import Loghme.entities.PartyFood;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/party")
public class PartyController {

    @GetMapping
    public ArrayList<PartyFood> getParty() {
        return IeatRepository.getInstance().getParty();
    }

    @GetMapping("/time")
    public FoodPartyTimer getTime() {
        return IeatRepository.getInstance().getFoodPartyTimer().getInstance();
    }

}
