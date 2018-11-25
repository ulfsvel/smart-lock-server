package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ro.cheiafermecata.smartlock.server.Data.Device;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.DeviceRepository;

import java.security.Principal;
import java.util.List;


@Controller
public class DeviceController {

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/api/deviceDetails/{deviceId}")
    public Device getDevice(Principal principal, @PathVariable(name = "deviceId") Long deviceId){
        return deviceRepository.getById(deviceId);
    }

    @GetMapping("/api/deviceDetails")
    public List<Device> getDevices(Principal principal){
        return deviceRepository.getByUserId(Long.parseLong(principal.getName()));
    }

}
