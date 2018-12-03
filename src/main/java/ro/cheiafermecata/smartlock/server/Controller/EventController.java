package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ro.cheiafermecata.smartlock.server.Data.Device;
import ro.cheiafermecata.smartlock.server.Data.DeviceOverview;
import ro.cheiafermecata.smartlock.server.Data.Event;
import ro.cheiafermecata.smartlock.server.Repository.DeviceRepository;
import ro.cheiafermecata.smartlock.server.Repository.EventRepository;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Long.parseLong;

@RestController
public class EventController {

    private final EventRepository eventRepository;

    private final DeviceRepository deviceRepository;

    public EventController(EventRepository eventRepository, DeviceRepository deviceRepository) {
        this.eventRepository = eventRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Gets the event history ap page {page} for the logged in user
     * @param principal the logged in user
     * @param page the page number
     * @return a List of Events
     */
    @GetMapping("/api/eventHistory/{page}")
    public List<Event> getEventHistory(Principal principal, @PathVariable(name = "page") Long page){
        return eventRepository.getByUserIdAtPage(parseLong(principal.getName()),page);
    }

    /**
     * Gets the page count for the logged in user
     * @param principal the logged in user
     * @return the page count
     */
    @GetMapping("/api/eventHistoryPageCount")
    public Long getEventHistoryPageCount(Principal principal){
        return eventRepository.getPageCountByUserId(parseLong(principal.getName()));
    }

    /**
     * Return the devices that belong to the logged in user the last and the latest events for them
     * @param principal the logged in user
     * @return a List of DeviceOverview(one per device)
     */
    @GetMapping("/api/eventHistoryOverview")
    public List<DeviceOverview> getEventHistoryOverview(Principal principal){
        List<DeviceOverview> devices = new LinkedList<>();
        for (Device device: deviceRepository.getByUserId(parseLong(principal.getName()))) {
            List<Event> events = eventRepository.getLatestByDeviceId(device.getId());
            devices.add(new DeviceOverview(
                    device,
                    events.get(0).getEvent(),
                    events
            ));
        }
        return devices;
    }

}
