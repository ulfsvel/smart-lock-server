package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ro.cheiafermecata.smartlock.server.Data.Device;
import ro.cheiafermecata.smartlock.server.Data.Event;
import ro.cheiafermecata.smartlock.server.Data.ResponseMessage;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.DeviceRepository;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.EventRepository;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;

@Controller
public class EventController {

    private final EventRepository eventRepository;

    private final DeviceRepository deviceRepository;

    public EventController(EventRepository eventRepository, DeviceRepository deviceRepository) {
        this.eventRepository = eventRepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/EventHistory/{page}/")
    public List<Event> getEventHistory(Principal principal, @PathVariable(name = "page") int page){
        return eventRepository.getByUserIdAtPage(parseLong(principal.getName()),page);
    }

    @GetMapping("/EventHistoryOverview/")
    public Map<Device,List<Event>> getEventHistoryOverview(Principal principal){
        Map<Device,List<Event>> result = new HashMap<>();
        for (Device device: deviceRepository.getByUserId(parseLong(principal.getName()))) {
            result.put(device,eventRepository.getByDeviceId(device.getId(),5));
        }
        return result;
    }

}
