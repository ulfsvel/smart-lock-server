package ro.cheiafermecata.smartlock.server.Repository.Implementations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.Event;
import ro.cheiafermecata.smartlock.server.Repository.EventRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EventRepositoryProvider implements EventRepository {

    private static final Long PAGE_SIZE = 10L;
    
    private static final Long OVERVIEW_SIZE = 5L;

    private final JdbcTemplate jdbcTemplate;

    EventRepositoryProvider(final JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    private Event mapToEvent(ResultSet rs, int rowNum) throws SQLException {
        return new Event(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("device_id"),
                rs.getString("event"),
                rs.getString("description"),
                rs.getString("event_time")
        );
    }

    public List<Event> getLatestByDeviceId(Long deviceId) {
        return jdbcTemplate.query(
                "SELECT `ID`, `USER_ID`, `DEVICE_ID`, `EVENT`, `DESCRIPTION`, `EVENT_TIME` FROM `EVENTS` WHERE `DEVICE_ID` = ? ORDER BY ID DESC LIMIT ?", new Object[]{deviceId, OVERVIEW_SIZE},
                this::mapToEvent
        );
    }


    public List<Event> getByUserIdAtPage(Long userId, Long page) {
        page -= 1;
        return jdbcTemplate.query(
                "SELECT `ID`, `USER_ID`, `DEVICE_ID`, `EVENT`, `DESCRIPTION`, `EVENT_TIME` FROM `EVENTS` WHERE `USER_ID` = ? ORDER BY `ID` DESC LIMIT ?,? ", new Object[]{userId, page * PAGE_SIZE, PAGE_SIZE},
                this::mapToEvent
        );
    }

    public Long getPageCountByUserId(Long userId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(1) AS `PAGES` FROM `EVENTS` WHERE `USER_ID` = ?", new Object[]{userId},
                (rs, rowNum) -> (rs.getLong("pages") / PAGE_SIZE + 1)
        );
    }

    public void save(Event event) {
        jdbcTemplate.update(
                "INSERT INTO " +
                        "`EVENTS`(`USER_ID`, `DEVICE_ID`, `EVENT`, `DESCRIPTION`, `EVENT_TIME`) " +
                        "VALUES (?, ?, ?, ?, ?)",
                event.getUserId(),
                event.getDeviceId(),
                event.getEvent(),
                event.getDescription(),
                event.getEventTime()
        );
    }

}
