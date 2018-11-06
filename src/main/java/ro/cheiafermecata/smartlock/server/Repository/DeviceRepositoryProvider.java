package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.Device;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.DeviceRepository;

import java.util.List;

@Repository
public class DeviceRepositoryProvider implements DeviceRepository {

    private final JdbcTemplate jdbcTemplate;

    DeviceRepositoryProvider(final JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Device> getByUserId(Long id) {
        return jdbcTemplate.query(
                "SELECT ID, USER_ID, NAME FROM DEVICES WHERE USER_ID = ?", new Object[] { id },
                (rs, rowNum) -> new Device(rs.getLong("id"), rs.getLong("user_id"), rs.getString("name"))
        );
    }

}
