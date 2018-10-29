package ro.cheiafermecata.smartlock.server.Websocket;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.cheiafermecata.smartlock.server.Data.User;
import ro.cheiafermecata.smartlock.server.Repository.UserRepository;


@Component
public class ConnectionHandler implements ChannelInterceptor {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    ConnectionHandler(final PasswordEncoder passwordEncoder, final UserRepository userRepository) {
        super();
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String username = accessor.getFirstNativeHeader("username");
            String password = accessor.getFirstNativeHeader("password");
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                throw new BadCredentialsException("Please provide both the user and password in the header");
            }
            Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
            User user = userRepository.getByEmail(auth.getName());
            if (!passwordEncoder.matches(auth.getCredentials().toString(), user.getPassword())) {
                throw new BadCredentialsException("The user does not exist or the password  is incorrect");
            }
            SecurityContextHolder.getContext().setAuthentication(auth);
            accessor.setUser(auth);

        }

        return message;
    }

}
