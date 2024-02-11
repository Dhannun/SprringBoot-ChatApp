package click.touchit.chat.config;

import click.touchit.chat.chat.ChatMessage;
import click.touchit.chat.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static click.touchit.chat.chat.MessageType.LEAVE;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

  private final SimpMessageSendingOperations messageTemplate;

  @EventListener
  public void handleWebSocketDisconnectListener(
      SessionDisconnectEvent event
  ) {
    // TODO -- to be implemented
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String username = (String) headerAccessor.getSessionAttributes().get("username");

    if (username != null){
      log.info("User Disconnected: {}", username);
      var chatMessage = new ChatMessage(
          null, username, LEAVE
      );

      messageTemplate.convertAndSend("/topic/public", chatMessage);
    }
  }

}
