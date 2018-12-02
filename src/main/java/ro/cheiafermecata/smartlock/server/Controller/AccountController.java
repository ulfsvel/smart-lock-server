package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    public String showLoginAction(
            Map<String, Object> model,
            @RequestParam(name = "error", required = false) String error,
            @RequestParam(name = "success", required = false) String success
    ) {
        if (error != null) {
            model.put("error", "Authentication failed");
        }
        if (success != null) {
            model.put("success", "Account created");
        }
        model.put("content", "login");
        return "account";
    }

    @GetMapping("/account/register")
    public String showRegisterAction(Map<String, Object> model) {
        model.put("content", "register");
        return "account";
    }

    @PostMapping("/account/register")
    public String doRegisterAction(
            Map<String, Object> model,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password") String repassword) {
        if (!password.equals(repassword)) {
            model.put("error", "The passwords do not match");
            model.put("content", "register");
            return "account";
        }
        try {
            userRepository.save(new User(username, password));
        } catch (Exception e) {
            model.put("error", "Unable to save user");
            model.put("content", "register");
            return "account";
        }

        return "redirect:/account/login?success";
    }

}