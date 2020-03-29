package Loghme.controllers;

import Loghme.models.Food;
import Loghme.models.IeatRepository;
import Loghme.models.PartyFood;
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

}
