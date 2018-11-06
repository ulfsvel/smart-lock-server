package ro.cheiafermecata.smartlock.server.Interfaces.Repository;

import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.User;

@Repository
public interface UserRepository {

    User getByEmail(String email);
}
