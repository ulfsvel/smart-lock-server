package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ro.cheiafermecata.smartlock.server.Data.Device;
import ro.cheiafermecata.smartlock.server.Data.DeviceOverview;
import ro.cheiafermecata.smartlock.server.Data.Event;
import ro.cheiafermecata.smartlock.server.Data.ResponseMessage;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.DeviceRepository;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.EventRepository;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedList;
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

    @GetMapping("/api/EventHistory/{page}/")
    public List<Event> getEventHistory(Principal principal, @PathVariable(name = "page") int page){
        return eventRepository.getByUserIdAtPage(parseLong(principal.getName()),page);
    }

    @GetMapping("/api/EventHistoryOverview/")
    public List<DeviceOverview> getEventHistoryOverview(Principal principal){
        List<DeviceOverview> devices = new LinkedList<>();
        for (Device device: deviceRepository.getByUserId(parseLong(principal.getName()))) {
            List<Event> events = eventRepository.getByDeviceId(device.getId(),5);
            devices.add(new DeviceOverview(
                    device,
                    events.get(0).getEvent(),
                    events
            ));
        }
        return devices;
    }

}
