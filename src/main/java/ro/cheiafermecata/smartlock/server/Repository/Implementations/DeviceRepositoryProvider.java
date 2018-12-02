package ro.cheiafermecata.smartlock.server.Repository.Implementations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.Device;
import ro.cheiafermecata.smartlock.server.Repository.DeviceRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class DeviceRepositoryProvider implements DeviceRepository {

    private final JdbcTemplate jdbcTemplate;

    DeviceRepositoryProvider(final JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Device> getByUserId(Long id) {
        return jdbcTemplate.query(
                "SELECT `ID`, `USER_ID`, `NAME` FROM `DEVICES` WHERE `USER_ID` = ? ORDER BY `NAME`", new Object[] { id },
                (rs, rowNum) -> new Device(rs.getLong("id"), rs.getLong("user_id"), rs.getString("name"))
        );
    }

    @Override
    public Device getById(Long id){
        return jdbcTemplate.queryForObject(
                "SELECT `ID`, `USER_ID`, `NAME` FROM `DEVICES` WHERE `ID` = ?", new Object[] { id },
                (rs, rowNum) -> new Device(rs.getLong("id"), rs.getLong("user_id"), rs.getString("name"))
        );
    }

    @Override
    public Long save(Device device) {
        if(device.getId() == null){
            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement("INSERT INTO `DEVICES`(`USER_ID`, `NAME`) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, device.getUserId());
                statement.setString(2, device.getName());
                return statement;
            }, holder);
            return holder.getKey().longValue();
        }else{
            jdbcTemplate.update(
                    "UPDATE `DEVICES` SET `NAME` = ? WHERE `ID` = ?",
                    device.getName(),
                    device.getId()
            );
            return device.getId();
        }
    }

}
