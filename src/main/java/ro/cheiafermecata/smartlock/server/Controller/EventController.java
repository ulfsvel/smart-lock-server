package ro.cheiafermecata.smartlock.server.Controller;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ro.cheiafermecata.smartlock.server.Data.Device;
import ro.cheiafermecata.smartlock.server.Data.DeviceOverview;
import ro.cheiafermecata.smartlock.server.Data.Event;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.DeviceRepository;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.EventRepository;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Long.parseLong;

@Controller
public class EventController {

    private final EventRepository eventRepository;

    private final DeviceRepository deviceRepository;

    public EventController(EventRepository eventRepository, DeviceRepository deviceRepository) {
        this.eventRepository = eventRepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/api/eventHistory/{page}")
    public List<Event> getEventHistory(Principal principal, @PathVariable(name = "page") Long page){
        return eventRepository.getByUserIdAtPage(parseLong(principal.getName()),page);
    }

    @GetMapping("/api/eventHistoryPageCount")
    public Long getEventHistoryPageCount(Principal principal){
        return eventRepository.getPageCountByUserId(parseLong(principal.getName()));
    }

    @GetMapping("/api/eventHistoryOverview")
    public List<DeviceOverview> getEventHistoryOverview(Principal principal){
        List<DeviceOverview> devices = new LinkedList<>();
        for (Device device: deviceRepository.getByUserId(parseLong(principal.getName()))) {
            List<Event> events = eventRepository.getLatestByDeviceId(device.getId(),5);
            devices.add(new DeviceOverview(
                    device,
                    events.isEmpty() ? "New Device" : events.get(0).getEvent(),
                    events
            ));
        }
        return devices;
    }

}
