package ro.cheiafermecata.smartlock.server.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.cheiafermecata.smartlock.server.Repository.UserRepository;
import ro.cheiafermecata.smartlock.server.Repository.UserRepositoryProvider;

@Configuration
public class MiscConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    @Bean
    public UserRepository userRepository(){
        return new UserRepositoryProvider();
    }*/

}
