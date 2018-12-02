package ro.cheiafermecata.smartlock.server.Repository.Implementations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.User;
import ro.cheiafermecata.smartlock.server.Repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;


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

    @Override
    public Long save(User user) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement("INSERT INTO `USERS`(`EMAIL`, `PASSWORD`) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, this.passwordEncoder.encode(user.getPassword()));
            return statement;
        }, holder);
        return holder.getKey().longValue();
    }

}
