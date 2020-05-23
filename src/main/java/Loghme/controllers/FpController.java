package Loghme.controllers;

import Loghme.database.dataMappers.food.party.PartyMapper;
import Loghme.controllers.DTOs.FoodPartyTimerDTO;
import Loghme.entities.FoodPartyTimer;
import Loghme.entities.PartyFood;
import Loghme.services.FoodPartyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/party")
public class FpController {

    @GetMapping
    public ArrayList<PartyFood> getParty() throws SQLException {
        PartyMapper partyMapper = PartyMapper.getInstance();
        return partyMapper.findAll();
    }

    @GetMapping("/time")
    public FoodPartyTimerDTO getRemainingTime() {
        return FoodPartyService.getRemainingTime();
    }
}
