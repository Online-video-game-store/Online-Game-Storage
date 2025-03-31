package mr.demonid.web.client.services;

import java.util.UUID;

public interface WebSocketService {

    void sendMessage(UUID userId, String message);

}
