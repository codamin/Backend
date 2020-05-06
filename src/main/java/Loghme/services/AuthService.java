package Loghme.services;

import Loghme.DTOs.Token;
import Loghme.Utilities.JwtUtils;
import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.User;
import Loghme.exceptions.ForbiddenException;
import Loghme.requests.Login;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;

public class AuthService {
    public static String authUser(Login login) {
        if(login.getEmail() == null || login.getPassword() == null) {
            throw new ForbiddenException("both fields should not be empty to authenticate");
        }
        String email = login.getEmail();
        String pass = login.getPassword();
        User foundUser = null;
        try {
            foundUser = UserMapper.getInstance().find(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(!foundUser.getPassword().equals(DigestUtils.sha256Hex(login.getPassword().getBytes())))
            throw new ForbiddenException("wrong password!!!");

        return JwtUtils.createJWT(email);
    }
}
