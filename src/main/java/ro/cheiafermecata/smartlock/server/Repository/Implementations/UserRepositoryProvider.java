package ro.cheiafermecata.smartlock.server.Repository.Implementations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.User;
import ro.cheiafermecata.smartlock.server.Repository.UserRepository;


@Repository
public class UserRepositoryProvider implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    UserRepositoryProvider(final JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getByEmail(String email){
        return jdbcTemplate.queryForObject(
                "SELECT `ID`, `EMAIL`, `PASSWORD` FROM `USERS` WHERE `EMAIL` = ? LIMIT 1", new Object[] { email },
                (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email"), rs.getString("password"))
        );
    }

    @Override
    public User getById(Long id){
        return jdbcTemplate.queryForObject(
                "SELECT `ID`, `EMAIL`, `PASSWORD` FROM `USERS` WHERE `ID` = ? LIMIT 1", new Object[] { id },
                (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email"), rs.getString("password"))
        );
    }

}
