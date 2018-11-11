package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.User;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.UserRepository;


@Repository
public class UserRepositoryProvider implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    UserRepositoryProvider(final JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getByEmail(String email){
        return jdbcTemplate.queryForObject(
                "SELECT id, email, password FROM users WHERE email = ? AND ROWNUM = 1", new Object[] { email },
                (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email"), rs.getString("password"))
        );
    }

    public User getById(Long id){
        return jdbcTemplate.queryForObject(
                "SELECT id, email, password FROM users WHERE id = ? AND ROWNUM = 1", new Object[] { id },
                (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email"), rs.getString("password"))
        );
    }
}
