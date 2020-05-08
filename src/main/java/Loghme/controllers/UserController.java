package Loghme.controllers;

import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.User;
import Loghme.exceptions.ForbiddenException;
import Loghme.requests.AddCredit;
import Loghme.requests.AddUser;
import Loghme.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;
//import sun.awt.FontDescriptor;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public User getUser(@RequestAttribute("user") String mail) {
        try {
            return UserService.getUser(mail);
        } catch (SQLException e) {
            System.out.println("error occured in getting user information");
            throw new ForbiddenException("error occured in getting user information");
        }
    }

    @PostMapping("/credit")
    public void addCredit(@RequestBody AddCredit req, @RequestAttribute("user") String mail) {
        try {
            UserService.addCredit(req, mail);
        } catch (SQLException e) {
            System.out.println("error occured in adding credit request");
            throw new ForbiddenException("error occured in adding credit request");
        }
    }

    @PostMapping
    public void addUser(@RequestBody AddUser req) {
        try {
            UserService.addUser(req);
        } catch (SQLException e) {
            System.out.println("error occured in adding user.");
            throw new ForbiddenException("error occured in getting user information");
        }
    }
}