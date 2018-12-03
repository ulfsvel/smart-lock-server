package ro.cheiafermecata.smartlock.server.WebSocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.cheiafermecata.smartlock.server.Repository.Implementations.UserDetailServiceProvider;

import java.security.Principal;


@Component
public class ConnectionHandler implements ChannelInterceptor {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailServiceProvider userDetailServiceProvider;

    ConnectionHandler(final PasswordEncoder passwordEncoder, UserDetailServiceProvider userDetailServiceProvider) {
        super();
        this.passwordEncoder = passwordEncoder;
        this.userDetailServiceProvider = userDetailServiceProvider;
    }

    /**
     * Intercepts WS connection request and checks the security headers
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        MessageHeaders headers = message.getHeaders();
        Principal principal = (Principal) headers.get("simpUser");

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand()) && principal == null) {

            String username = accessor.getFirstNativeHeader("username");
            String device = accessor.getFirstNativeHeader("device");
            String password = accessor.getFirstNativeHeader("password");

            UserDetails userDetails = userDetailServiceProvider.loadUserByUsername(username);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Authentication failure");
            }
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername()+"-"+device,
                    userDetails.getPassword(),
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            accessor.setUser(auth);
        }

        return message;
    }

}
