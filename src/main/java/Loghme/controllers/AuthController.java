package Loghme.controllers;
import Loghme.entities.User;
import Loghme.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

//    @PostMapping()
//    public String login(@RequestBody User newUser) {
////        return AuthService.authUser(newUser);
//    }

}