package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.User;

@Repository
public interface UserRepository {

    /**
     * Gets the user with email = {email}
     * @param email the email of the user to find
     * @return an User
     */
    User getByEmail(String email);

    /**
     * Persists the user and encodes its password
     * @param user the user to persist
     */
    void save(User user);

}
