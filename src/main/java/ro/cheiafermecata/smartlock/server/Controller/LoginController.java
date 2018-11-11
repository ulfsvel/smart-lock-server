package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin(Map<String, Object> model, @RequestParam(name = "error", required = false) String error) {
        if(error != null){
            model.put("error","Authentication failed");
        }
        return "login";
    }

}