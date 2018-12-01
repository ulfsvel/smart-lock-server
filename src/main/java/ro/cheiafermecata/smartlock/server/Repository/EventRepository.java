package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.Event;

import java.util.List;

@Repository
public interface EventRepository {

    List<Event> getLatestByDeviceId(Long deviceId, int limit);

    List<Event> getByUserIdAtPage(Long userId, Long page);

    Long getPageCountByUserId(Long userId);

    void save(Event event);
}