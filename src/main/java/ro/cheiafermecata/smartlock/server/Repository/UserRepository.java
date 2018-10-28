package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User getByEmail(String email);
}
