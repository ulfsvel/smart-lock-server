package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.cheiafermecata.smartlock.server.Data.Device;
import ro.cheiafermecata.smartlock.server.Repository.UserRepository;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    private final EventController eventController;

    private final UserRepository userRepository;

    private final DeviceController deviceController;

    public HomeController(EventController eventController, UserRepository userRepository, DeviceController deviceController) {
        this.eventController = eventController;
        this.userRepository = userRepository;
        this.deviceController = deviceController;
    }

    /**
     * Shows an DeviceOverview for each device that belongs to the logged in user
     * @param principal the logged in user
     * @param model the data to send to the view
     * @return the template name
     */
    @RequestMapping("/")
    public String index(Principal principal,Map<String, Object> model) {
        model.put("content","home");
        model.put("devices",eventController.getEventHistoryOverview(principal));
        return "dashboard";
    }

    /**
     * Shows the Event history for the logged in user
     * @param principal the logged in user
     * @param model the data to send to the view
     * @param pageNumber the page number
     * @return the template name
     */
    @RequestMapping({"/eventHistory/{pageNumber}","/eventHistory"})
    public String eventHistory(Principal principal, Map<String, Object> model, @PathVariable(name = "pageNumber", required = false ) Long pageNumber){
        model.put("content","history");

        Long pageCount = eventController.getEventHistoryPageCount(principal);
        pageNumber = HomeController.preparePageNumber(pageNumber,pageCount);

        model.put("pageNumber",pageNumber);
        Map<Long,String> deviceIdToName = new HashMap<>();
        for (Device device : deviceController.getDevices(principal)){
            deviceIdToName.put(device.getId(),device.getName());
        }
        model.put("deviceIdToName",deviceIdToName);
        model.put("history",eventController.getEventHistory(principal,pageNumber));
        model.put("pageCount",pageCount);
        return "dashboard";
    }

    /**
     * Helper function to sanitize the page number
     * @param pageNumber the raw page number
     * @param pageCount the page count
     * @return the sanitized page number
     */
    private static Long preparePageNumber(Long pageNumber, Long pageCount){
        if(pageNumber == null){
            return 1L;
        }
        if(pageNumber < 1){
            return 1L;
        }
        if(pageNumber > pageCount){
            return pageCount;
        }
        return pageNumber;
    }

}
