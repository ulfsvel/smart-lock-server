package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.Event;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.EventRepository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class EventRepositoryProvider implements EventRepository {

    private static final int PAGE_SIZE = 20;

    private final JdbcTemplate jdbcTemplate;

    EventRepositoryProvider(final JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    private Event mapToEvent(ResultSet rs, int rowNum){
        try {
            return new Event(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getLong("device_id"),
                    rs.getString("event"),
                    rs.getString("description"),
                    rs.getString("time")
            );
        }catch (Exception e){
            return null;
        }
    }

    public List<Event> getByDeviceId(Long deviceId, int limit) {
        return jdbcTemplate.query(
                "SELECT ID, USER_ID, DEVICE_ID, EVENT, DESCRIPTION, TIME FROM EVENTS WHERE DEVICE_ID = ? AND ROWNUM <= ? ORDER BY TIME DESC", new Object[] { deviceId, limit },
                this::mapToEvent
        );
    }


    public List<Event> getByUserIdAtPage(Long userId, int page){
        return jdbcTemplate.query(
                "SELECT ID, USER_ID, DEVICE_ID, EVENT, DESCRIPTION, TIME FROM EVENTS WHERE USER_ID = ? AND ROWNUM > ? AND ROWNUM < ? ORDER BY TIME DESC", new Object[] { userId, page, page + PAGE_SIZE },
                this::mapToEvent
        );
    }

    public void save(Event event) {
        //noinspection RedundantArrayCreation
        jdbcTemplate.update("INSERT INTO " +
                "EVENTS(USER_ID, DEVICE_ID, EVENT, DESCRIPTION) " +
                "VALUES (?, ?, ?, ?)",
                new Object[] {event.getUserId(), event.getDeviceId(), event.getEvent(), event.getDescription()});
    }

}
