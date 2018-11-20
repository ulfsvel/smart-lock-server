package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ro.cheiafermecata.smartlock.server.Data.Device;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.DeviceRepository;

import java.security.Principal;


@Controller
public class DeviceController {

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/api/deviceDetails/{deviceId}")
    public Device getEventHistory(Principal principal, @PathVariable(name = "deviceId") Long deviceId){
        return deviceRepository.getById(deviceId);
    }

}
