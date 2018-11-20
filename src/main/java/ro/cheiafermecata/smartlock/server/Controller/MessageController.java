package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import ro.cheiafermecata.smartlock.server.Data.Event;
import ro.cheiafermecata.smartlock.server.Data.SendToDeviceMessage;
import ro.cheiafermecata.smartlock.server.Data.SendToUsersMessage;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.EventRepository;
import java.security.Principal;


@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final EventRepository eventRepository;

    MessageController(SimpMessagingTemplate messagingTemplate, EventRepository eventRepository) {
        this.messagingTemplate = messagingTemplate;
        this.eventRepository = eventRepository;
    }


    @MessageMapping("/sendToDevices")
    public void sendToDevice(Principal principal, SendToDeviceMessage message) {
        if (!principal.getName().equals(message.getUserId().toString())) {
            throw new BadCredentialsException("The destination user is not allowed for the given credentials");
        }
        eventRepository.save(new Event(message.getUserId(),message.getDeviceId(),message.getAction(), message.getAction(),message.getTime()));
        messagingTemplate.convertAndSendToUser(principal.getName(), "/usersData/influx", message);
    }

    @MessageMapping("/sendToUsers")
    public void sendToUsers(Principal principal, SendToUsersMessage message) {
        if (!principal.getName().equals(message.getUserId().toString())) {
            throw new BadCredentialsException("The destination user is not allowed for the given credentials");
        }
        eventRepository.save(new Event(message.getUserId(),message.getDeviceId(),message.getActionType(), message.getActionContent(),message.getTime()));
        messagingTemplate.convertAndSendToUser(principal.getName(), "/devicesData/influx", message);
    }

}
