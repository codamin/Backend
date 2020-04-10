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
        IeatRepository.getInstance().getUser().chargeCredit(req.getCredit());
    }

}
