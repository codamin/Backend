package Loghme.controllers;

import Loghme.models.IeatRepository;
import Loghme.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public User getUser() {
        return IeatRepository.getInstance().getUser();
    }

}
