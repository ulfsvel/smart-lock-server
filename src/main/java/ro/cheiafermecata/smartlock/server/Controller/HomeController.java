package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.UserRepository;

import java.security.Principal;
import java.util.Map;

@Controller
public class HomeController {

    private final EventController eventController;

    private final UserRepository userRepository;

    public HomeController(EventController eventController, UserRepository userRepository) {
        this.eventController = eventController;
        this.userRepository = userRepository;
    }

    @RequestMapping("/")
    public String index(Principal principal,Map<String, Object> model) {
        model.put("devices",eventController.getEventHistoryOverview(principal));
        model.put("user",userRepository.getById(Long.parseLong(principal.getName())));
        model.put("history",eventController.getEventHistory(principal,1));
        return "index";
    }

}
