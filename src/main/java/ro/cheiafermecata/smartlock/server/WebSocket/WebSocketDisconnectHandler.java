package ro.cheiafermecata.smartlock.server.WebSocket;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ro.cheiafermecata.smartlock.server.Controller.MessageController;
import ro.cheiafermecata.smartlock.server.Data.Events;
import ro.cheiafermecata.smartlock.server.Data.SendToUsersMessage;

import java.security.Principal;

@Component
public class WebSocketDisconnectHandler implements ApplicationListener<SessionDisconnectEvent> {

    private final MessageController messageController;

    public WebSocketDisconnectHandler(MessageController messageController) {
        this.messageController = messageController;
    }

    /**
     * Intercepts DISCONNECT Events and persists them
     * @param sessionDisconnectEvent the DISCONNECT Event
     */
    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        Principal principal = sessionDisconnectEvent.getUser();
        if(principal != null && principal.getName().contains("-")){
            messageController.sendToUsersAsDevice(
                    principal,
                    new SendToUsersMessage(
                            Events.DISCONNECT.toString(),
                            "Device disconnected")
            );
        }
    }
}