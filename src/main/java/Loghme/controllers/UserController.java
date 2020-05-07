package Loghme.controllers;

import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.User;
import Loghme.exceptions.ForbiddenException;
import Loghme.requests.AddCredit;
import Loghme.requests.AddUser;
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
            return UserMapper.getInstance().find(mail);
        } catch (SQLException e) {
            System.out.println("error occured in getting user information");
            throw new ForbiddenException("error occured in getting user information");
        }
    }

    @PostMapping("/credit")
    public void addCredit(@RequestBody AddCredit req, @RequestAttribute("user") String mail) {
        try {
            UserMapper.getInstance().addCredit(mail, req.getCredit());
        } catch (SQLException e) {
            System.out.println("error occured in adding credit request");
            throw new ForbiddenException("error occured in adding credit request");
        }
    }

    @PostMapping
    public void addUser(@RequestBody AddUser req) {
        try {
            UserMapper.getInstance().insert(new User(req.getFirst_name(),req.getLast_name(), req.getEmail(), DigestUtils.sha256Hex(req.getPassword().getBytes()), "01234567891", 10000));
        } catch (SQLException e) {
            throw new ForbiddenException("error occured in getting user information");
        }
    }
}