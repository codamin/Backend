package Loghme.controllers;

import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.User;
import Loghme.exceptions.ForbiddenException;
import Loghme.requests.AddCredit;
import org.springframework.web.bind.annotation.*;
//import sun.awt.FontDescriptor;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public User getUser() {
        try {
            return UserMapper.getInstance().find("ekhamespanah@yahoo.com");
        } catch (SQLException e) {
            System.out.println("error occured in getting user information");
            throw new ForbiddenException("error occured in getting user information");
        }
    }

    @PostMapping
    public void addCredit(@RequestBody AddCredit req) {
        try {
            UserMapper.getInstance().addCredit("ekhamespanah@yahoo.com", req.getCredit());
        } catch (SQLException e) {
            System.out.println("error occured in adding credit request");
            throw new ForbiddenException("error occured in adding credit request");
        }
    }

}
