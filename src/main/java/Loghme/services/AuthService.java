package Loghme.services;

import Loghme.DTOs.Token;
import Loghme.Utilities.JwtUtils;
import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.User;
import Loghme.exceptions.ForbiddenException;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;

public class AuthService {
    public String authUser(User user) {
        if(user.getEmail() == null || user.getPass() == null) {
            throw new ForbiddenException("both fields should not be empty to authenticate");
        }
        String email = user.getEmail();
        String pass = user.getPass();
        User foundUser = null;
        try {
            foundUser = UserMapper.getInstance().find(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(!foundUser.getPass().equals(DigestUtils.sha256Hex(user.getPass().getBytes())))
            throw new ForbiddenException("wrong password!!!");

        return JwtUtils.createJWT(email);
    }
}
