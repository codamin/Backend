package Loghme.Utilities;

import Loghme.models.IeatRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Application {
    @PostConstruct
    public void init() {
        IeatRepository.getInstance();
    }
}
