package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ro.cheiafermecata.smartlock.server.Data.User;
import ro.cheiafermecata.smartlock.server.Repository.UserRepository;

import java.util.Map;


@Controller
public class AccountController {

    private final UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/account/login")
    public String showLogin(Map<String, Object> model, @RequestParam(name = "error", required = false) String error) {
        if(error != null){
            model.put("error","Authentication failed");
        }
        model.put("content","login");
        return "account";
    }

    @RequestMapping(name = "/account/register", method = RequestMethod.GET)
    public String showRegister(Map<String, Object> model) {
        model.put("content","register");
        return "account";
    }

    @RequestMapping(name = "/account/register", method = RequestMethod.POST)
    public String doRegister(
            Map<String, Object> model,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password") String repassword) {
        if(!password.equals(repassword)){
            model.put("error","The passwords do not match");
            model.put("content","register");
            return "account";
        }
        if(userRepository.getByEmail(username) != null){
            model.put("error","The user already exists");
            model.put("content","register");
            return "account";
        }
        try{
            userRepository.save(new User(username,password));
        }catch (Exception e){
            model.put("error","Unable to save user");
            model.put("content","register");
            return "account";
        }

        model.put("content","login");
        return "account";
    }

}