package ro.cheiafermecata.smartlock.server.Repository.Implementations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.User;
import ro.cheiafermecata.smartlock.server.Repository.UserRepository;


@Repository
public class UserRepositoryProvider implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final PasswordEncoder passwordEncoder;

    UserRepositoryProvider(final JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getByEmail(String email) {
        return jdbcTemplate.queryForObject(
                "SELECT `ID`, `EMAIL`, `PASSWORD` FROM `USERS` WHERE `EMAIL` = ? LIMIT 1", new Object[]{email},
                (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email"), rs.getString("password"))
        );
    }


    @Override
    public void save(User user) {
        jdbcTemplate.update(
                "INSERT INTO `USERS`(`EMAIL`, `PASSWORD`) VALUES (?, ?)",
                user.getEmail(),
                passwordEncoder.encode(user.getPassword())
        );
    }

}
