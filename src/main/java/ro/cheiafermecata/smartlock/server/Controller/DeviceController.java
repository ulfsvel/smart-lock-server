package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.web.bind.annotation.*;
import ro.cheiafermecata.smartlock.server.Data.Device;
import ro.cheiafermecata.smartlock.server.Data.Events;
import ro.cheiafermecata.smartlock.server.Data.SendToUsersMessage;
import ro.cheiafermecata.smartlock.server.Repository.DeviceRepository;

import java.security.Principal;
import java.util.List;


@RestController
public class DeviceController {

    private final DeviceRepository deviceRepository;

    private final MessageController messageController;

    public DeviceController(DeviceRepository deviceRepository, MessageController messageController) {
        this.deviceRepository = deviceRepository;
        this.messageController = messageController;
    }

    @GetMapping("/api/deviceDetails/{deviceId}")
    public Device getDevice(@PathVariable("deviceId") Long deviceId) {
        return deviceRepository.getById(deviceId);
    }

    @GetMapping("/api/deviceDetails")
    public List<Device> getDevices(Principal principal) {
        return deviceRepository.getByUserId(Long.parseLong(principal.getName()));
    }

    @PostMapping("/api/deviceDetails/new")
    public Device saveDevice(Principal principal, @RequestParam("name") String deviceName) {
        Device device = new Device(
                Long.parseLong(principal.getName()),
                deviceName
        );
        device.setId(deviceRepository.save(device));
        messageController.sendToUsersAsUser(
                principal,
                new SendToUsersMessage(
                        Events.NEW.toString(),
                        "Device created",
                        device.getId(),
                        device.getName()
                )
        );
        return device;
    }

    @PostMapping("/api/deviceDetails/rename")
    public Device renameDevice(
            Principal principal,
            @RequestParam("name") String deviceName,
            @RequestParam("id") Long deviceId
    ) {
        Device device = deviceRepository.getById(deviceId);
        if (!device.getUserId().toString().equals(principal.getName())) {
            return device;
        }
        device.setName(deviceName);
        device.setId(deviceRepository.save(device));
        return device;
    }

    @GetMapping("/api/deviceDetails/auth")
    public Boolean authenticateDevice() {
        return true;
    }

}
