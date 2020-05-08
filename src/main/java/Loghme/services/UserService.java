package Loghme.services;

import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.User;
import Loghme.exceptions.ForbiddenException;
import Loghme.requests.AddCredit;
import Loghme.requests.AddUser;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;

public class UserService {

    public static void addUser(AddUser req) throws SQLException {
        if (UserMapper.getInstance().find(req.getEmail()) != null) {
            throw new ForbiddenException("duplicated user mail.");
        }
        UserMapper.getInstance().insert(new User(req.getFirst_name(), req.getLast_name(), req.getEmail(), DigestUtils.sha256Hex(req.getPassword().getBytes()), "01234567891", 10000));
    }

    public static void addCredit(AddCredit req, String mail) throws SQLException {
        UserMapper.getInstance().addCredit(mail, req.getCredit());
    }

    public static User getUser(String mail) throws SQLException {
        return UserMapper.getInstance().find(mail);
    }

}
