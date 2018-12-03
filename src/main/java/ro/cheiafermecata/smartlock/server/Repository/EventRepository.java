package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.Event;

import java.util.List;

@Repository
public interface EventRepository {

    /**
     * Gets the latest Events for the device with id = {deviceId}
     * @param deviceId the id of the device
     * @return a List of Events
     */
    List<Event> getLatestByDeviceId(Long deviceId);

    /**
     * Gets the events at page {page} for the user with id = {userId}
     * @param userId the id of a user
     * @param page the page number
     * @return a List of Events
     */
    List<Event> getByUserIdAtPage(Long userId, Long page);

    /**
     * Gets the events pages count for a user
     * @param userId the id of a user
     * @return the page count
     */
    Long getPageCountByUserId(Long userId);

    /**
     * Persists the Event
     * @param event the Event to persist
     */
    void save(Event event);
}
