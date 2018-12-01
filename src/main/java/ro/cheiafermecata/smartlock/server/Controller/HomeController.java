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

    @RequestMapping("/")
    public String index(Principal principal,Map<String, Object> model) {
        model.put("content","home");
        model.put("devices",eventController.getEventHistoryOverview(principal));
        model.put("user",userRepository.getById(Long.parseLong(principal.getName())));
        return "index";
    }

    @RequestMapping({"/eventHistory/{page}","/eventHistory"})
    public String eventHistory(Principal principal, Map<String, Object> model, @PathVariable(name = "page", required = false ) Long page){
        model.put("content","history");

        Long pageCount = eventController.getEventHistoryPageCount(principal);
        page = HomeController.preparePageNumber(page,pageCount);

        model.put("pageNumber",page);
        Map<Long,String> deviceIdToName = new HashMap<>();
        for (Device device : deviceController.getDevices(principal)){
            deviceIdToName.put(device.getId(),device.getName());
        }
        model.put("deviceIdToName",deviceIdToName);
        model.put("history",eventController.getEventHistory(principal,page));
        model.put("pageCount",pageCount);
        return "index";
    }

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
