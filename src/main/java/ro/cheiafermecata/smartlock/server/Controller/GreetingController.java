package ro.cheiafermecata.smartlock.server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ro.cheiafermecata.smartlock.server.Data.Greeting;
import ro.cheiafermecata.smartlock.server.Data.HelloMessage;

import java.security.Principal;


@Controller
public class GreetingController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/hello")
	public void greeting(Principal principal, HelloMessage message) throws  Exception {
		Greeting greeting = new Greeting();
		greeting.setContent("Hello !");

		messagingTemplate.convertAndSendToUser(message.getToUser(), "/queue/reply", greeting);
	}

}
