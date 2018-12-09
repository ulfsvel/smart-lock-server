package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ro.cheiafermecata.smartlock.server.Data.*;
import ro.cheiafermecata.smartlock.server.Repository.DeviceRepository;
import ro.cheiafermecata.smartlock.server.Repository.EventRepository;
import java.security.Principal;


@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final EventRepository eventRepository;

    private final DeviceRepository deviceRepository;

    MessageController(SimpMessagingTemplate messagingTemplate, EventRepository eventRepository, DeviceRepository deviceRepository) {
        this.messagingTemplate = messagingTemplate;
        this.eventRepository = eventRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Sends the message to the device identified by {userId}-{deviceId}
     * @param principal the logged in user, the format is {userId}
     * @param message the message to send to the device
     */
    @MessageMapping("/sendToDevices")
    public void sendToDevices(Principal principal, SendToDeviceMessage message) {
        Device device = deviceRepository.getById(message.getDeviceId());
        try{
            eventRepository.save(new Event(device.getUserId(),message.getDeviceId(),Events.valueOf(message.getAction()).toString(), message.getAction(),message.getTime()));
            messagingTemplate.convertAndSendToUser(principal.getName()+"-"+message.getDeviceId(), "/usersData/influx", message);
            messagingTemplate.convertAndSendToUser(principal.getName(), "/devicesData/influx", new SendToUsersMessage(message.getAction(),message.getAction(),device.getId(),device.getName()));
        }catch (IllegalArgumentException e){
            eventRepository.save(new Event(device.getUserId(),message.getDeviceId(),Events.ERROR.toString(), "Invalid action exception"));
            messagingTemplate.convertAndSendToUser(
                    principal.getName(),
                    "/devicesData/influx",
                    new SendToUsersMessage(
                            Events.ERROR.toString(),
                            "Invalid action exception",
                            device.getId(),
                            device.getName()
                    )
            );
        }
    }

    /**
     * Sends the message to the user identified by {userId}
     * @param principal the logged in device, the format is {userId}-{deviceId} (called only from ws)
     * @param message the message to send to the user
     */
    @MessageMapping("/sendToUsers")
    public void sendToUsersAsDevice(Principal principal, SendToUsersMessage message) {
        String[] principalUserIdDeviceId = principal.getName().split("-");
        Long userId = Long.parseLong(principalUserIdDeviceId[0]);
        Long deviceId = Long.parseLong(principalUserIdDeviceId[1]);
        message.setDeviceId(deviceId);
        message.setDeviceName(deviceRepository.getById(deviceId).getName());
        try{
            eventRepository.save(new Event(userId,message.getDeviceId(), Events.valueOf(message.getActionType()).toString(), message.getActionContent(),message.getTime()));
            messagingTemplate.convertAndSendToUser(userId.toString(), "/devicesData/influx", message);
        }catch (IllegalArgumentException e){
            eventRepository.save(new Event(userId,message.getDeviceId(),Events.ERROR.toString(), "Invalid action exception"));
            messagingTemplate.convertAndSendToUser(principal.getName(), "/devicesData/influx", message);
        }
    }

    /**
     * Sends the message to the user identified by {userId}
     * @param principal the logged in user, the format is {userId}
     * @param message the message to send to the user
     */
    void sendToUsersAsUser(Principal principal, SendToUsersMessage message){
        try{
            eventRepository.save(new Event(Long.parseLong(principal.getName()),message.getDeviceId(), Events.valueOf(message.getActionType()).toString(), message.getActionContent(),message.getTime()));
            messagingTemplate.convertAndSendToUser(principal.getName(), "/devicesData/influx", message);
        }catch (IllegalArgumentException e){
            eventRepository.save(new Event(Long.parseLong(principal.getName()),message.getDeviceId(),Events.ERROR.toString(), "Invalid action exception"));
            messagingTemplate.convertAndSendToUser(principal.getName(), "/devicesData/influx", message);
        }
    }

}
