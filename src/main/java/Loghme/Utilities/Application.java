package Loghme.Utilities;

import Loghme.database.DatabaseListener;
import Loghme.entities.IeatRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@Component
public class Application {
    @PostConstruct
    public void init() throws SQLException {
        DatabaseListener.start();
    }
}
