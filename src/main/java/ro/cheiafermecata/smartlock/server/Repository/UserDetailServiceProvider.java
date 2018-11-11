package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ro.cheiafermecata.smartlock.server.Data.User;
import ro.cheiafermecata.smartlock.server.Interfaces.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailServiceProvider implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailServiceProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email);

        if(user == null){
            throw new UsernameNotFoundException("Username not found");
        }

        List<GrantedAuthority> grantList = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        grantList.add(authority);

        return new UserDetailProvider(user.getId(), user.getPassword(), grantList);
    }

}
