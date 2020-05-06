package Loghme.controllers;
import Loghme.DTOs.Token;
import Loghme.requests.Login;
import Loghme.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public Token login(@RequestBody Login login) {
        return new Token(AuthService.authUser(login));
    }

}