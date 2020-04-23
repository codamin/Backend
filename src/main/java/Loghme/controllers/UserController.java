package Loghme.controllers;

import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.User;
import Loghme.requests.AddCredit;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public User getUser() {
        try {
            return UserMapper.getInstance().find("ekhamespanah@yahoo.com");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping
    public void addCredit(@RequestBody AddCredit req) {
        try {
            UserMapper.getInstance().addCredit("ekhamespanah@yahoo.com", req.getCredit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
