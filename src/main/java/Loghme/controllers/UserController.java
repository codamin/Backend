package Loghme.controllers;

import Loghme.models.IeatRepository;
import Loghme.models.User;
import Loghme.requests.AddCredit;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public User getUser() {
        return IeatRepository.getInstance().getUser();
    }

    @PostMapping
    public void addCredit(@RequestBody AddCredit req) {
        System.out.println("here");
        System.out.println(req.getCredit());
        IeatRepository.getInstance().getUser().chargeCredit(req.getCredit());
    }

}
