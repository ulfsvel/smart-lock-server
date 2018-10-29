package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import ro.cheiafermecata.smartlock.server.Data.Greeting;
import ro.cheiafermecata.smartlock.server.Data.HelloMessage;
import ro.cheiafermecata.smartlock.server.Data.SendToUsersMessage;

import java.security.Principal;


@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/sendToDevices")
    public void sendToDevice(Principal principal, HelloMessage message) {
        Greeting greeting = new Greeting();
        greeting.setContent("Hello !");
        messagingTemplate.convertAndSendToUser(principal.getName(), "/usersData/influx", greeting);
    }

    @MessageMapping("/sendToUsers")
    public void sendToUsers(Principal principal, SendToUsersMessage message) {
        if (!principal.getName().equals(message.getEmail())) {
            throw new BadCredentialsException("The destination user is not allowed for the given credentials");
        }
        messagingTemplate.convertAndSendToUser(principal.getName(), "/devicesData/influx", message);
    }

}
