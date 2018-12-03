package ro.cheiafermecata.smartlock.server.WebSocket;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import ro.cheiafermecata.smartlock.server.Controller.MessageController;
import ro.cheiafermecata.smartlock.server.Data.Events;
import ro.cheiafermecata.smartlock.server.Data.SendToUsersMessage;

import java.security.Principal;

@Component
public class WebSocketConnectHandler implements ApplicationListener<SessionConnectEvent> {

    private final MessageController messageController;

    public WebSocketConnectHandler(MessageController messageController) {
        this.messageController = messageController;
    }

    /**
     * Intercepts CONNECT events and persists them
     * @param sessionConnectEvent the CONNECT EVENT
     */
    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
        Principal principal = sessionConnectEvent.getUser();
        if(principal != null && principal.getName().contains("-")){
            messageController.sendToUsersAsDevice(
                    principal,
                    new SendToUsersMessage(
                            Events.CONNECT.toString(),
                            "Device connected")
            );
        }
    }
}