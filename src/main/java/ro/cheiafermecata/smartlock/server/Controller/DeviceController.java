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

    /**
     * Returns a Device object for the device with id {deviceId}
     * @param principal the logged in user
     * @param deviceId the id of a device
     * @return the device with id = {deviceId}
     */
    @GetMapping("/api/deviceDetails/{deviceId}")
    public Device getDevice(Principal principal,@PathVariable("deviceId") Long deviceId) {
        Device device = deviceRepository.getById(deviceId);
        if(device.getUserId().equals(Long.parseLong(principal.getName()))){
            return device;
        }
        return null;
    }

    /**
     * Returns a List of devices for the logged in user
     * @param principal the logged in user
     * @return the devices that belong to the user
     */
    @GetMapping("/api/deviceDetails")
    public List<Device> getDevices(Principal principal) {
        return deviceRepository.getByUserId(Long.parseLong(principal.getName()));
    }

    /**
     * Creates a new device with name {deviceName} for the logged in user
     * @param principal the logged in user
     * @param deviceName the name of the device to create
     * @return the created Device
     */
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

    /**
     * Renames the device with id = {deviceId} to {deviceName}
     * @param principal the logged in user
     * @param deviceName the new name for the device
     * @param deviceId the id of the device
     * @return the edited Device
     */
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

    /**
     * Checks if a successful login was performed
     * @return true
     */
    @GetMapping("/api/deviceDetails/auth")
    public Boolean authenticateDevice() {
        return true;
    }

}
