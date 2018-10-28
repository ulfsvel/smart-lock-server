package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.User;


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

    @Override
    public <S extends User> S save(S s) {
        return null;
    }

    @Override
    public <S extends User> Iterable<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public User findOne(Long aLong) {
        return null;
    }

    @Override
    public boolean exists(Long aLong) {
        return false;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Iterable<User> findAll(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void delete(Iterable<? extends User> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}