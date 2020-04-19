package Loghme.controllers;

import Loghme.entities.IeatRepository;
import Loghme.entities.User;
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
        IeatRepository.getInstance().getUser().chargeCredit(req.getCredit());
    }

}
