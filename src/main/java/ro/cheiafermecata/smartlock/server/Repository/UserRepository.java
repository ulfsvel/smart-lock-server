package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.User;

@Repository
public interface UserRepository {

    User getByEmail(String email);

    User getById(Long id);

    Long save(User user);

}
